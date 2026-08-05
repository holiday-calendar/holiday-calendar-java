---
name: ship-issue
description: Commit pending changes with an issue-number prefix, push the branch, and open a PR against develop using the org-level PULL_REQUEST_TEMPLATE.md, assigned to the invoker with holiday-calendar/dev as reviewer. Use when the user asks to commit and create/open a PR for the current issue/branch, or says something like "ship this", "commit and push and open a PR", or names an issue number to associate a commit/PR with.
---

# Ship issue PR

Commits the current change set, pushes, and opens a PR against `develop` —
this repo's PRs never target `main`. Works for any issue number; nothing
here is specific to one issue.

## 1. Determine the issue number

Run `.claude/skills/ship-issue/scripts/infer-issue-number.sh`. It prints
the leading number from the current branch name (e.g. branch
`227-add-early-close-api-usage` → `227`), or `NONE` if the branch name
doesn't start with a number.

- If it prints a number and the user didn't specify one, use it.
- If it prints `NONE`, or the user explicitly gave a different issue number,
  ask which issue to associate this with before proceeding — don't guess.

## 2. Commit

Run `git status` first and review what's changed — do not `git add -A`
blindly. Stage the files that are actually part of this change (ask the user
if it's ambiguous which pending changes belong in this commit).

Match this repo's existing commit style (see `git log` for more examples):

```
#<issue-number> <concise summary, imperative mood>

<optional body: why, not what, if the change needs context>

Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>
```

Never use `--no-verify` or otherwise skip hooks. If a hook fails, fix the
underlying issue and create a new commit — don't amend past a hook failure.

## 3. Push

`git push`, adding `-u origin <branch>` if the branch has no upstream yet.
Never force-push.

## 4. Fetch the org PR template

Run `.claude/skills/ship-issue/scripts/fetch-pr-template.sh`. This pulls
`PULL_REQUEST_TEMPLATE.md` live from the `holiday-calendar/.github` repo
root — fetch it fresh each time rather than assuming a cached copy, since the
org can change it. Fill in its fields yourself (don't just paste it back
unfilled):

- **Description** — what changed and why, written for a reviewer.
- **Related Issue** — link to `https://github.com/holiday-calendar/holiday-calendar-java/issues/<issue-number>`.
- **Type of Change** — check the box(es) that actually apply.
- **Checklist** — check off what's genuinely true (tests run locally, docs
  updated if needed, etc.); don't check boxes you haven't verified.

## 5. Open the PR

```bash
gh pr create --base develop --head <branch> \
  --assignee "@me" --reviewer "holiday-calendar/dev" \
  --title "<title>" --body "<filled-in template>"
```

- Base is always `develop`, never `main`.
- If `gh pr create` fails because a PR already exists for this branch, print
  `PR #<number> already exists, editing instead`, then use `gh pr edit` to
  update title/body/assignee/reviewer instead of erroring out.

## 6. Report back

Give the user the PR URL. Don't summarize the whole diff again — they just
watched it happen.
