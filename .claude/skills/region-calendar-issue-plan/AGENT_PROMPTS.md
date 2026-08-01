# Agent prompt templates

Placeholders: `{ISSUE_NUMBER}`, `{ISSUE_TEXT}` (full `gh issue view` output),
`{CODE}` (region/exchange code, e.g. `US`), `{HOLIDAY_OR_CHANGE_DESC}` (one
sentence naming what's being added/changed), `{EXPLORE_FINDINGS}` (Phase 1
results, summarized), `{RESEARCH_FINDINGS}`, `{ENGINEER_DESIGN}`,
`{COMBINED_DRAFT}` (research + engineer + test sections concatenated).

Launch each via the `Agent` tool, `subagent_type: general-purpose`,
`run_in_background: false` — each depends on the prior one's output.

---

## 1. Research analyst

```
You are acting as a research analyst with expertise in [world equities market
conventions | regional/cultural holiday conventions — pick whichever fits
{HOLIDAY_OR_CHANGE_DESC}]. I need you to verify the factual claims in GitHub
issue #{ISSUE_NUMBER} of this holiday-calendar library against authoritative
primary sources before an engineer builds on them.

Issue text:
{ISSUE_TEXT}

Use WebSearch/WebFetch. Prefer the exchange's/authority's own official
calendar or press releases over secondary aggregators — if a secondary
source and the primary source disagree, trust the primary source and say so.

I need:
1. Exact date(s)/rule(s) as officially published — confirm or correct the
   issue's stated rule. Pay special attention to boundary conditions (e.g.
   "falls on a Monday" vs "falls Tuesday-Friday" are NOT the same set of
   days — the issue text has been wrong about exactly this kind of boundary
   before).
2. Any conditions under which the holiday/close does NOT occur, and whether
   in that case it's suppressed entirely for the year or shifted to another
   date — these are different semantics and matter a lot for implementation.
3. Exact time/timezone if applicable (IANA zone id, not just an offset).
4. A multi-year table (at least one full cycle of the relevant day-of-week
   pattern, typically 7-9 years) of concrete example years with the actual
   observed date/absence, sourced from primary references, suitable for use
   as test fixtures.
5. Cite every source URL you used.

Report structured findings (numbered per point above), flagging explicitly
anywhere the issue text was imprecise or wrong.
```

---

## 2. Senior engineer

```
You are a senior Java engineer (Java 21+) proposing an implementation for
GitHub issue #{ISSUE_NUMBER} in the holiday-calendar-java repo (multi-module
Maven, TestNG). Do NOT write code files — produce a detailed implementation
PLAN a plan document can include verbatim.

Issue: {HOLIDAY_OR_CHANGE_DESC} in HolidayCalendarService{CODE}.

Verified research findings (primary-sourced, treat as ground truth over the
issue's own text):
{RESEARCH_FINDINGS}

Codebase context already confirmed by exploration:
{EXPLORE_FINDINGS}

This repo's reusable conventions to follow:
- `Holiday.builder()...type(Holiday.Type.X)...build()` — Type values include
  FIXED, FLOATING, SPECIAL_ANNIVERSARY, EARLY_CLOSE. Pick the one that
  actually matches the semantics confirmed by research, not the one the
  issue assumed.
- For EARLY_CLOSE: `IsraelHolidays.earlyCloseHolidays()` (mena module) is the
  original pattern; `HolidayCalendarServiceUK`'s Christmas Eve/New Year's Eve
  is the most recent precedent. Both ALWAYS produce a date (UK shifts
  weekend dates to the preceding Friday; Israel's Erev holidays are always
  the day before their anchor). If your research found a "sometimes there is
  no early close/holiday at all this year" rule, that is a DIFFERENT
  semantic from either precedent — say so explicitly, and use
  `AbstractObservance.isValidYear(int)` returning `false` (not a shifting
  `computeDate`) to represent absence. Note in the new Observance's Javadoc
  that this is a different use of `isValidYear` than its only other existing
  use (`WesternEaster`/`OrthodoxEaster`, an algorithm-validity bound, not a
  business-rule exclusion).
- Observance classes live in `observance/<lowercase-code>/`, extend
  `AbstractObservance`, override `computeDate` and (if needed) `isValidYear`.
  Small single-purpose classes — this repo's precedent (UK) does NOT share a
  helper between near-identical eligibility checks; follow that unless you
  have a concrete reason not to.
- Registration happens in `HolidayCalendarService{CODE}.getHolidayCalendar()`
  via `Holiday.builder()...build()` then `.holiday(...)` in the
  `HolidayCalendar.builder()` chain (or `.holidays(list)` if there's a shared
  factory list like Israel's).

Propose:
1. Package/class layout — new Observance class(es), and whether any existing
   Observance needs modification (usually not — only its registration type
   changes) or reuse.
2. Exact `computeDate`/`isValidYear` logic (or `test`/`apply` if implementing
   `Observance` directly), matching the confirmed semantics from research.
3. Exact new/changed `Holiday.builder()` blocks and `HolidayCalendar.builder()`
   chain additions for `HolidayCalendarService{CODE}`.
4. Naming — check for existing naming conventions in this calendar and
   others; flag if your chosen name is a stylistic outlier (e.g. ordinals,
   redundant type-suffixes).
5. Whether `holiday-calendar-core` needs any changes — usually none, if this
   fits an existing `Holiday.Type`; confirm by reading the relevant core
   classes rather than assuming.
6. Breaking-change / migration considerations — does this move a holiday
   between `calculate()` and `calculateEarlyCloses()`, or change a name any
   consumer might match on? State the impact plainly. Do not assert
   CHANGELOG.md conventions without checking `git log -- CHANGELOG.md` and
   recent precedent commits first.

Report a structured, detailed plan (headers per point above).
```

---

## 3. Test engineer

```
You are a test engineer reviewing this proposed implementation for GitHub
issue #{ISSUE_NUMBER} (holiday-calendar-java, TestNG 7.7.1 with
@DataProvider). Examine existing test patterns for the relevant holiday type
in this codebase, identify best practices, and propose concrete tests.
Do not write code files — produce a test plan section.

Finalized engineering design:
{ENGINEER_DESIGN}

Verified research fixture data (use this for DataProvider rows, not
invented dates):
{RESEARCH_FINDINGS}

This repo's test conventions:
- `AbstractHolidayCalendarServiceTest` (holiday-calendar-western,
  org.holiday.calendar.impl) — every `HolidayCalendarService<CODE>Test`
  extends this; supplies `expectedHolidayNames()` and
  `expectedHolidayOccurrences()` DataProviders.
- `AbstractObservanceTest` (org.holiday.calendar.western.test) — every
  `<Observance>Test` extends this, passing the Observance instance to
  `super()` and overriding `createData()` with `{year, expectedDateOrNull}`
  rows.
- The closest `<Code>EarlyCloseTest`/equivalent precedent class (e.g.
  `HolidayCalendarServiceUKEarlyCloseTest`) for count/presence/closeTime/
  zoneId/rollability assertions — adapt for this issue's actual semantics
  (fixed count vs. variable count per year, always-present vs.
  sometimes-absent).
- `tests/src/test/java/org/holiday/calendar/HolidayCalendar30YearIT.java` —
  add a rule-derived (not hand-fixtured) 30-year section if the change is a
  recurring annual rule, re-deriving expected presence/date from the
  day-of-week rule for every year in range rather than hardcoding results.

Propose:
1. New/updated test files — exact paths, one-line purpose each.
2. Observance unit test(s) — fixture rows from the verified research table,
   plus 1-2 edge years outside the sampled cycle (compute and verify the
   actual day-of-week yourself before hardcoding — do not guess).
3. Any explicitly-named regression test for a boundary case the research
   analyst flagged as easy to get wrong (e.g. an off-by-one weekday) — this
   must be a named test, not incidental DataProvider coverage.
4. Integration-level test class changes/additions (count, presence/absence
   per holiday name — not just count, since count alone can mask one
   holiday's presence covering for another's incorrect absence).
5. `HolidayCalendar30YearIT` addition, if applicable.
6. Any risk/gap you'd flag (e.g. a date-collision test between this change
   and an existing rollable holiday landing on the same computed date).

Report a structured plan (headers per point above).
```

---

## 4. Devil's advocate

```
You are a devil's advocate reviewer. Below is a draft implementation plan
(research + engineering + test sections) for GitHub issue #{ISSUE_NUMBER} in
this holiday-calendar-java repo. Find weaknesses, challenge assumptions, and
verify claims against the actual codebase — don't just approve. Cite the
specific claim you're challenging. If something is genuinely fine after
scrutiny, say so briefly rather than manufacturing a complaint.

{COMBINED_DRAFT}

Specifically check:
- Does the cited precedent actually support the proposed design, or is it a
  superficially-similar-but-semantically-different case (e.g. "shifts to
  another date" vs. "is absent this year" are not the same mechanism, even
  if both are called EARLY_CLOSE)?
- Any reused core-abstraction hook (e.g. `isValidYear`) being used for a
  purpose different from its only other existing use in this codebase — is
  that a problem, or just worth a comment?
- Any repo-convention claim (CHANGELOG.md handling, README update rules,
  versioning) — verify against actual `git log`/recent commits rather than
  trusting the engineer agent's assertion.
- Test coverage gaps: does a boundary/edge case the research analyst flagged
  get an explicitly-named test, or only incidental coverage? Is there a
  same-date collision risk between the new holiday and an existing rollable
  holiday that isn't tested?
- Naming: is the proposed name consistent with this codebase's existing
  naming conventions, or an outlier worth a second opinion?
- Anything module-info.java/JPMS-related, or any mismatch between the
  explored package names and this repo's documented module names.

Report findings as a concise, prioritized list (most important first), each
with: the specific claim being challenged, why it's a concern, and a
concrete suggestion or question to resolve it before implementation begins.
```