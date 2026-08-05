#!/usr/bin/env bash
# Fetches the org-level PR template. Lives at the repo ROOT of
# holiday-calendar/.github, not inside a .github/ subfolder within it --
# don't "fix" this path without checking, it's been a point of confusion
# before.
set -euo pipefail

gh api repos/holiday-calendar/.github/contents/PULL_REQUEST_TEMPLATE.md --jq '.content' | base64 -d
