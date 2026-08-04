#!/usr/bin/env python3
"""
Fail the build if a PR changes a calendar's source without updating that
calendar's reference doc under docs/calendars/<CODE>.md (see #243).

The Java-file -> calendar-CODE dependency graph is computed dynamically from
each HolidayCalendarService<CODE> class's import statements (transitively),
rather than hand-maintained, so this check can't silently drift out of sync
with the code it's meant to protect. It necessarily can't distinguish "this
shared class is used by CODE" from "this shared class merely imports a type
used by CODE" — false positives (asking for a doc update that wasn't
strictly needed) are possible and are an acceptable tradeoff for never
silently missing a real one.

Known scope limit: this only traces the import graph reachable from each
HolidayCalendarService<CODE> class. It will not catch changes to files that
affect a calendar's *behavior* without being imported by its service class
(e.g. a shared utility invoked only via reflection or SPI). That residual
gap is a manual PR-review responsibility, per docs/243_CALENDAR_REFERENCE_DOC_PLAN.md.
"""
import os
import re
import subprocess
import sys

DOCS_DIR = "docs/calendars"
SERVICE_RE = re.compile(r"HolidayCalendarService([A-Z0-9]+)\.java$")
IMPORT_RE = re.compile(r"^\s*import\s+(?:static\s+)?([A-Za-z0-9_.]+?)(\.\*)?;\s*$", re.MULTILINE)
PACKAGE_PREFIX = "org.holiday.calendar"


def run(*args):
    return subprocess.check_output(args).decode().strip()


def repo_root():
    return run("git", "rev-parse", "--show-toplevel")


def changed_files():
    """Determine the changed-file set for this run.

    - In a PR, diff against the merge-base with the PR base branch.
    - Otherwise (push), diff against the previous commit.
    Falls back to an empty diff (never fails the build) if history is too
    shallow to compute either.
    """
    base_ref = os.environ.get("CALENDAR_DOCS_BASE_REF")
    try:
        if base_ref:
            merge_base = run("git", "merge-base", "HEAD", base_ref)
            out = run("git", "diff", "--name-only", merge_base, "HEAD")
        else:
            out = run("git", "diff", "--name-only", "HEAD~1", "HEAD")
    except subprocess.CalledProcessError:
        return set()
    return set(line for line in out.splitlines() if line)


def all_java_files(root):
    out = run("git", "ls-files", "*.java")
    return [p for p in out.splitlines() if "/src/main/java/" in p]


def build_indices(java_files, root):
    """class simple name -> [paths]; package path suffix -> [paths] (for wildcard imports)."""
    by_class = {}
    by_package_dir = {}
    for path in java_files:
        base = os.path.basename(path)
        if not base.endswith(".java"):
            continue
        cls = base[:-5]
        by_class.setdefault(cls, []).append(path)
        pkg_dir = os.path.dirname(path)
        by_package_dir.setdefault(pkg_dir, []).append(path)
    return by_class, by_package_dir


def extract_imports(path):
    with open(path, "r", encoding="utf-8") as fh:
        content = fh.read()
    imports = []
    for match in IMPORT_RE.finditer(content):
        fqcn, wildcard = match.group(1), bool(match.group(2))
        if fqcn.startswith(PACKAGE_PREFIX):
            imports.append((fqcn, wildcard))
    return imports, content


def same_package_dependencies(path, content, by_package_dir):
    """Classes in the same package need no import statement (e.g. a
    HolidayCalendarService referencing its sibling <Country>Holidays factory
    class). Detect these by checking whether every other class defined in
    the same package directory is referenced by name in this file's source.
    """
    pkg_dir = os.path.dirname(path)
    own_class = os.path.basename(path)[:-5]
    deps = set()
    for sibling_path in by_package_dir.get(pkg_dir, []):
        sibling_class = os.path.basename(sibling_path)[:-5]
        if sibling_class == own_class:
            continue
        if re.search(r"\b" + re.escape(sibling_class) + r"\b", content):
            deps.add(sibling_path)
    return deps


def resolve_import(fqcn, wildcard, by_class, by_package_dir):
    if wildcard:
        pkg_path_suffix = "/" + fqcn.replace(".", "/")
        matches = set()
        for pkg_dir, files in by_package_dir.items():
            if ("/" + pkg_dir).endswith(pkg_path_suffix):
                matches.update(files)
        return matches
    # A regular class import is fully qualified, so resolve by matching the
    # exact package+class path suffix — NOT just the bare simple name, which
    # collides across region packages that reuse the same class name (e.g.
    # observance/ca/ChristmasEveEarlyClose.java vs .../sg/ChristmasEveEarlyClose.java).
    class_path_suffix = "/" + fqcn.replace(".", "/") + ".java"
    matches = {path for path in _all_paths(by_class) if ("/" + path).endswith(class_path_suffix)}
    if matches:
        return matches
    # Fall back to the second-to-last segment (handles
    # `import static ...Class.FIELD;`, where fqcn's last segment is a field,
    # not a class), matched the same exact-path way.
    segments = fqcn.split(".")
    if len(segments) > 1:
        owner_fqcn = ".".join(segments[:-1])
        owner_suffix = "/" + owner_fqcn.replace(".", "/") + ".java"
        return {path for path in _all_paths(by_class) if ("/" + path).endswith(owner_suffix)}
    return set()


def _all_paths(by_class):
    for paths in by_class.values():
        for path in paths:
            yield path


def build_code_dependencies(root):
    java_files = all_java_files(root)
    by_class, by_package_dir = build_indices(java_files, root)

    code_to_files = {}
    for path in java_files:
        m = SERVICE_RE.search(os.path.basename(path))
        if not m:
            continue
        code = m.group(1)
        visited = {path}
        frontier = [path]
        while frontier:
            current = frontier.pop()
            full_path = os.path.join(root, current)
            if not os.path.isfile(full_path):
                continue
            imports, content = extract_imports(full_path)
            deps = same_package_dependencies(current, content, by_package_dir)
            for fqcn, wildcard in imports:
                deps |= resolve_import(fqcn, wildcard, by_class, by_package_dir)
            for dep in deps:
                if dep not in visited:
                    visited.add(dep)
                    frontier.append(dep)
        code_to_files[code] = visited
    return code_to_files


def main():
    root = repo_root()
    os.chdir(root)
    diff = changed_files()
    if not diff:
        print("check_calendar_docs: no changed files detected, nothing to check.")
        return 0

    code_deps = build_code_dependencies(root)

    violations = []
    for code, files in sorted(code_deps.items()):
        triggering = sorted(files & diff)
        if not triggering:
            continue
        doc_path = f"{DOCS_DIR}/{code}.md"
        if doc_path not in diff:
            violations.append((code, doc_path, triggering))

    if violations:
        print("check_calendar_docs: the following calendars changed without a matching reference doc update:\n")
        for code, doc_path, triggering in violations:
            print(f"  {code} -> expected {doc_path} in the diff, triggered by:")
            for f in triggering:
                print(f"    - {f}")
        print(
            "\nIf this calendar's reference doc genuinely doesn't exist yet, "
            "create it from docs/calendars/TEMPLATE.md as part of this PR. "
            "See docs/243_CALENDAR_REFERENCE_DOC_PLAN.md for the full requirement."
        )
        return 1

    print("check_calendar_docs: OK — no calendar source changes are missing a doc update.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
