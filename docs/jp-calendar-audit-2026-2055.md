# JP / JPY Calendar Audit — 30-Year Data Coverage (Issue #116)

This document summarises the findings and follow-on work from the audit of
`HolidayCalendarServiceJP` (TSE) and `HolidayCalendarServiceJPY` (BOJ) required
to confirm correct operation across the 2026–2055 target range.

---

## Audit Checklist Results

| # | Requirement | Finding | Resolution |
|---|-------------|---------|------------|
| 1 | **Lookup table check** — confirm whether any holidays rely on lookup tables; extend to 2055 if so | No lookup tables. The vernal and autumnal equinox dates are computed via the NAOJ astronomical formula (valid 1980–2099). All other floating holidays use Happy Monday rules or fixed dates. | Nothing to extend. |
| 2 | **Substitute holiday rules (振替休日)** — verify correct application for all years in the 30-year window, including edge cases | Two bugs found: (a) Saturday holidays were incorrectly receiving a Monday substitute; (b) cascading substitute was not applied when the immediate Monday was already occupied by another holiday. | Fixed by #125 and #126. The calendar now uses `sundayToMonday()` — only Sunday holidays roll; Saturday holidays remain on their natural date. |
| 3 | **Sandwich day logic (国民の休日)** — confirm rule is implemented and correct | Working correctly. Silver Week sandwiches (September) and Golden Week sandwiches are generated in the right years. | Verified by extended tests. |
| 4 | **Special closures** — document how extraordinary one-time closures are represented | The 2019 imperial transition is modelled as two `SpecialAnniversary` entries in `JapaneseHolidays.baseHolidays()`: Emperor's Abdication Day (Apr 30) and Enthronement Day (May 1). | Verified. |
| 5 | **Range test** — run `calculate(2026, 2055)` and spot-check results | Extended test suite added (PR #134). Two additional defects were uncovered during testing — see below. | PR #134 (extended tests); defects tracked as #132 and #133. |
| 6 | **Olympic relocations 2021** | Sports Day, Marine Day, and Mountain Day were relocated by special ordinance for the Tokyo 2020 Games (held in 2021). | Fixed by #127. |

---

## Extended Test Coverage (PR #134)

Four new test classes were added to `holiday-calendar-apac`:

| Class | Coverage |
|-------|----------|
| `HolidayCalendarServiceJPExtendedTest` | Equinox observed dates all 30 years; Silver Week sandwiches; Golden Week cascades; Emperor's Birthday weekend behaviour; full-year holiday counts 2026–2055; Olympic 2021 overrides |
| `HolidayCalendarServiceJPYExtendedTest` | BOJ closures (Jan 2, Jan 3, Dec 31) every year 2026–2055; non-rollable weekend spot-checks; JPY full-year counts |
| `AutumnalEquinoxDayExtendedTest` | Raw NAOJ formula output for every year 2026–2055 |
| `VernalEquinoxDayExtendedTest` | Raw NAOJ formula output for every year 2026–2055 |

### Saturday no-roll behaviour (post-#125)

Tests were written with the initial assumption that `followingMonday()` was in use
(Saturday +2 → Monday). After merging the #125 fix, the JP and JPY calendars use
`sundayToMonday()`: Saturday holidays stay on their natural calendar date. The
extended tests reflect this corrected behaviour throughout.

---

## Remaining Defects

Two defects were uncovered during range testing and are tracked as separate issues.
Disabled sentinel tests for both are included in `HolidayCalendarServiceJPExtendedTest`.

### Issue #132 — 2020 Olympic holiday relocations not implemented

The Tokyo Olympic Special Measures Act relocated three holidays in **both** 2020 and
2021. The 2021 relocations were fixed by #127; the 2020 dates remain wrong.

| Holiday | 2020 Actual (wrong) | 2020 Expected (correct) |
|---------|---------------------|------------------------|
| Sports Day | Oct 12 (normal) | Jul 24 |
| Marine Day | Jul 20 (normal) | Jul 23 |
| Mountain Day | Aug 11 (normal) | Aug 10 |

**Fix guidance:** Add 2020-year overrides to `SportsDay.java`, `MarineDay.java`, and
the Mountain Day observance, mirroring the existing 2021 overrides added by #127.
Re-enable `testSportsDayOlympic2020`, `testMarineDayOlympic2020`, and
`testMountainDayOlympic2020` in `HolidayCalendarServiceJPExtendedTest` once done.

### Issue #133 — JPY cascade bypasses BOJ Year-Start Holiday

In years where January 1 falls on Sunday, New Year's Day rolls to Monday January 2
via `sundayToMonday()`. The JPY calendar also has a non-rollable BOJ operational
closure on January 2 ("Year-Start Holiday"). `JapaneseHolidayCalendar.applyCascade()`
treats that closure as a blocker, causing New Year's Day to cascade to January 4
instead of coexisting with the Year-Start Holiday on January 2.

The cascade rule (振替休日, Article 3 §3 of the Holiday Act) triggers only when
**another national holiday** occupies the Monday substitute. A BOJ operational closure
is not a national holiday and should not block the roll.

**Affected years (Jan 1 Sunday, 2026–2055):** 2034, 2040, 2045, 2051.

**Current behaviour (buggy):**
- Jan 2: Year-Start Holiday only
- Jan 3: Year-Start Holiday
- Jan 4: New Year's Day (incorrectly cascaded)

**Expected behaviour (correct):**
- Jan 2: New Year's Day + Year-Start Holiday (coexist)
- Jan 3: Year-Start Holiday

**Fix guidance:** In `JapaneseHolidayCalendar.findCascadeTarget()`, restrict
`hasOtherHolidayOn()` to only consider holidays whose raw natural date would
itself conflict (i.e., rollable national holidays), excluding non-rollable
operational closures. After fixing, re-enable the New Year's Day collision tests
in `HolidayCalendarServiceJPYExtendedTest` and update the JPY full-year count
expectations for the affected years (each gains +1 entry on Jan 2).

---

## Recommended Fix Order

1. Review and merge PR #134 → close issue #116.
2. Fix issue #132 (Olympic 2020 relocations) — scope is narrow, implementation-only.
3. Fix issue #133 (JPY cascade) — requires a targeted change to `applyCascade()`
   logic and updates to the JPY count tests.

Issues #132 and #133 are independent of each other and can be worked in either
order or in parallel.