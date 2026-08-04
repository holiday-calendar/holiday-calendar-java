# EUR — Euro (TARGET2) Holidays

**Standard:** ISO 4217 `EUR`
**Category:** Central Bank/Settlement
**Sibling calendars:** None — `EUR` represents the Eurozone-wide TARGET2 settlement system as a single calendar, not a per-country decomposition; it is not a sibling of any individual Eurozone country's national calendar (e.g. `DE`, `FR`) in this codebase.
**Service class:** `HolidayCalendarServiceEUR` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.noRoll()` — TARGET2 publishes six fixed closure dates per year with no date-substitution convention; closures are observed on the stated calendar date only. If a closure date falls on a weekend, no compensatory weekday closure is designated, since the system is already non-operating on weekends.
- **Rollability exceptions:** all 6 holidays are `rollable(false)` — consistent with the no-roll strategy.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | No | |
| Good Friday | FLOATING | — | No | Closed across the whole Eurozone system regardless of whether Good Friday is a public holiday in every member country |
| Easter Monday | FLOATING | — | No | |
| Labour Day | FIXED | — | No | May 1 |
| Christmas Day | FIXED | — | No | |
| Boxing Day | FIXED | — | No | TARGET2 documentation refers to this as "Second day of Christmas" |

## Early Closes

Not applicable — `EUR` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**A single system-wide calendar, not a composite of member-state calendars.** TARGET2's 6 fixed closure dates apply uniformly to every participating country's settlement activity, regardless of that country's own national or regional holidays — e.g. Bastille Day (France) or German Unity Day are not TARGET2 closures. This is a deliberate simplification decision for this codebase: `EUR` is modeled as the single settlement system it actually is in reality, rather than invented per-country delegate calendars, since TARGET2 itself has no such per-country variation.

**Only 6 closures — fewer than most national calendars in this codebase.** TARGET2's short, fixed list reflects its narrow purpose (interbank settlement) rather than a general public-holiday calendar — it's not intended to capture every Eurozone country's own national holidays, only the days the shared settlement infrastructure itself is closed.

## Sources

- [ECB — TARGET2 Calendar](https://www.ecb.europa.eu/paym/target/target2/profuse/calendar/html/index.en.html) — cited directly in `HolidayCalendarServiceEUR`'s own Javadoc; official primary source for TARGET2's 6 annual closure dates and no-roll convention
