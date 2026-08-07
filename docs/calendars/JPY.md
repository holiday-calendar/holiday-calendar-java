# JPY — Japan (Bank of Japan) Holidays

- **Standard:** ISO 4217 `JPY`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [JP](./JP.md) (national) — `JPY` shares every `JP` holiday and both the cascade (振替休日) and sandwiched-day (国民の休日) rules verbatim, adding 3 BOJ-specific operational closures on top.
- **Service class:** `HolidayCalendarServiceJPY` (`org.holiday.calendar.impl`, module `org.holiday.calendar.apac`), wrapped by the same package-private `JapaneseHolidayCalendar` used by `JP`

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.sundayToMonday()` — identical to `JP`, including the cascading substitute-holiday and sandwiched-day logic layered on top by `JapaneseHolidayCalendar` (see [JP.md](./JP.md) for the full mechanism)
- **Rollability exceptions:** identical to `JP` for the shared holidays. The 3 BOJ-specific closures (Year-Start Holiday ×2, Year-End Holiday) are `rollable(false)` and are **not** subject to the cascade rule — see Notes of Interest.

## Holidays

Shares all 18 of `JP`'s holidays (including both 2019 imperial-transition entries) verbatim — see [JP.md](./JP.md) for the full table. In addition:

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| Year-Start Holiday | FIXED | — | No | January 2; BOJ operational closure |
| Year-Start Holiday | FIXED | — | No | January 3; BOJ operational closure |
| Year-End Holiday | FIXED | — | No | December 31; BOJ operational closure |

## Early Closes

Not applicable — `JPY` carries no `EARLY_CLOSE` entries; the three BOJ closures above are modeled as full `FIXED` holidays, not half-days.

## Notes of Interest

**The BOJ-specific closures were themselves the subject of a closed bug (#133): "cascade logic incorrectly bypasses BOJ Year-Start Holiday when rolling New Year's Day."** Because `JPY` reuses the same `JapaneseHolidayCalendar` cascade/sandwich wrapper as `JP`, and because January 2/3 sit immediately adjacent to New Year's Day, there was a real risk of the cascade mechanism (see [JP.md](./JP.md)) mis-handling the interaction between New Year's Day rolling off a Sunday and the fixed, non-rollable Jan 2/3 closures already occupying those dates. This is now fixed, but is a good illustration of why layering BOJ-specific closures onto an already-complex cascade/sandwich calendar is riskier than it looks — a naive "just add three more FIXED holidays" implementation could plausibly reintroduce this exact class of bug.

**`JPY` is the only calendar in this codebase's APAC module with genuine bank-specific closures layered on top of a national calendar that itself has non-trivial computed logic** (cascade + sandwich), rather than a simple superset of Gregorian dates. Every other sibling pair in this codebase (Singapore's `SG`/`SGD`/`XSES`, China's `CN`/`CNY`) adds its market- or bank-specific content to a comparatively simpler base list.

## Sources

- See [JP.md](./JP.md) — `JPY` inherits the same underlying facts, sources, and closed-issue history for the cascade/sandwich mechanism
- The BOJ Jan 2/Jan 3/Dec 31 closures and the #133 cascade-interaction history are documented directly in this project's own source comments (`HolidayCalendarServiceJPY.java`); no official Bank of Japan primary-source page was independently captured with a stable citation URL in this pass

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
