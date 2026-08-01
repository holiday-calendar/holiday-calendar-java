---
name: region-calendar-issue-plan
description: Turn a GitHub issue about a region/exchange-specific holiday-calendar change (new national/market calendar, new holiday, EARLY_CLOSE/half-day modeling, roll-rule change) into a reviewed implementation plan for this repo, using a 4-agent workflow (research analyst, senior engineer, test engineer, devil's advocate). Use when planning work for a HolidayCalendarService<CODE> change, or any issue that names a specific market/region convention (close times, eligibility rules, observance dates) that must be verified before implementing.
---

# Region/exchange calendar issue → implementation plan

Produces a written plan (Plan Mode plan file) for a GitHub issue that adds or
changes holidays in a `HolidayCalendarService<CODE>` in this repo. Do not
write implementation code from this skill — it only produces the plan.
Requires: a GitHub issue number, and the region/exchange code (e.g. `US`,
`UK`, `CHF`) the issue targets. If the user hasn't given both, ask first.

Run this inside Plan Mode. If not already in Plan Mode, invoke `EnterPlanMode`
before starting.

## Phase 1 — Read the issue, explore the codebase (parallel)

1. `gh issue view <number>` to get the exact requirements text. Do not
   paraphrase from memory — the issue text itself may contain an incorrect
   rule (this has happened before; catching it is the research analyst's job
   in Phase 2).
2. Launch up to 3 `Explore` agents in parallel:
   - **Core abstractions**: `Holiday` sealed interface + builder, `Holiday.Type`
     enum, `Observance`/`AbstractObservance`, `HolidayCalendar.calculate()` /
     `calculateEarlyCloses()`, and (if the issue is about a half-day/early
     close) `EarlyCloseHoliday` plus its two real precedents:
     `IsraelHolidays.earlyCloseHolidays()` (mena module) and
     `HolidayCalendarServiceUK`'s Christmas Eve/New Year's Eve early closes
     (western module). Read the actual source, not just names.
   - **Closest precedent for this specific issue**: find the
     `HolidayCalendarService<CODE>` and its `observance/<code>/` package for
     the target region, and the most recently-added holiday of the *same
     kind* elsewhere in the codebase (another EARLY_CLOSE, another new
     calendar, another roll-rule change — whichever matches this issue).
   - **Test infrastructure**: `AbstractHolidayCalendarServiceTest`,
     `AbstractObservanceTest`, the closest `HolidayCalendarService<CODE>Test`
     / `<Code>EarlyCloseTest` precedent, and the `tests` module's
     `HolidayCalendar30YearIT`.

## Phase 2 — Four specialist agents, run sequentially in the foreground

Run in the foreground (`run_in_background: false`) and in this order —
each later agent depends on the previous one's output, so do not parallelize:

1. **Research analyst** — verifies the issue's factual claims (dates, close
   times, eligibility rules, timezone) against primary sources (official
   exchange/market calendars, not secondary aggregators). Must explicitly
   flag any place the issue text is wrong or imprecise, and produce a
   multi-year fixture table an engineer/tester can build from.
2. **Senior engineer** — given the research findings and Phase 1 codebase
   context, proposes concrete classes/files, registration changes, naming,
   and states plainly whether core-module changes are needed (usually none
   are, if this fits an existing `Holiday.Type`). Must not assume the closest
   precedent's *mechanism* transfers unchanged — state explicitly what's the
   same and what's different (e.g. "shift to nearest weekday" vs "suppress
   entirely in some years" are different semantics that look similar).
3. **Test engineer** — given the engineer's finalized design, proposes new
   test files/methods grounded in the research analyst's fixture table,
   reusing `AbstractObservanceTest`/`AbstractHolidayCalendarServiceTest` and
   proposing a `HolidayCalendar30YearIT` addition if the change is long-lived.
4. **Devil's advocate** — given the combined draft (research + engineering +
   test sections), actively looks for holes: mismatched precedent semantics,
   unverified assumptions, missing edge-year coverage, breaking-change
   handling, and repo-convention claims that aren't actually true (verify
   against real commit history, e.g. whether CHANGELOG.md is actually
   updated per-PR or only at release — check, don't assume either way).

Prompt templates for all four agents: see [AGENT_PROMPTS.md](AGENT_PROMPTS.md).

## Phase 3 — Resolve findings, don't just collect them

Every devil's-advocate finding must be resolved before the plan is written,
not appended as a footnote:
- If it's a factual question only the user can answer (naming preference,
  version/release targeting, scope) → `AskUserQuestion`.
- If it's a verifiable claim (e.g. "does this repo actually update
  CHANGELOG.md per PR?") → check it yourself (`git log`, `git show` on the
  closest precedent commits) and correct the plan.
- If it's a design concern → make the call and state the reasoning in the
  plan; don't leave it open.

## Phase 4 — Write the plan and exit

Write the plan file with these sections (see the `#205` NYSE half-day-close
plan in this repo's history for a worked example of this shape):
- **Context** — why this change, what precedent it follows, what is
  genuinely different from that precedent (don't overstate similarity).
- **Implementation** — concrete new/changed files, registration blocks,
  naming decisions, explicit "no core-module changes needed" confirmation
  (or what's needed if not).
- **Tests** — concrete new/updated test files, fixture tables sourced from
  the research analyst's verified data, and any `HolidayCalendar30YearIT`
  addition.
- **Verification** — the `mvn` commands to run before considering this done.

Call `ExitPlanMode` once the plan is written and any open questions are
resolved.