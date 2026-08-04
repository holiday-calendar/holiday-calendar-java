# US — United States National Holidays

**Standard:** ISO 3166-1 alpha-2 `US`
**Category:** National
**Sibling calendars:** [USD](./USD.md) (Federal Reserve settlement), [XNYS](./XNYS.md) (New York Stock Exchange) — `US` contains only holidays observed by the federal government; `USD` and `XNYS` each add/omit specific holidays (see their own docs).
**Service class:** `HolidayCalendarServiceUS` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** inline lambda — Saturday rolls back to the preceding Friday, Sunday rolls forward to the following Monday (equivalent to `DateRolls.previousFridayOrFollowingMonday()`, but written inline rather than reused from that factory method)
- **Rollability exceptions:** all 5 `FLOATING` holidays (MLK Day, Presidents' Day, Memorial Day, Labor Day, Columbus Day, Thanksgiving) are `rollable(false)` — each is already anchored to a specific weekday (e.g. "third Monday in January"), so it can never fall on a weekend and rolling doesn't apply. The 6 `FIXED` holidays are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Martin Luther King Jr. Day | FLOATING | — | No | Third Monday in January |
| Presidents' Day | FLOATING | — | No | Third Monday in February |
| Memorial Day | FLOATING | — | No | Last Monday in May |
| Juneteenth | FIXED | 2021 | Yes | Federal holiday since June 17, 2021 |
| Independence Day | FIXED | — | Yes | July 4 |
| Labor Day | FLOATING | — | No | First Monday in September |
| Columbus Day | FLOATING | — | No | Second Monday in October |
| Veterans Day | FIXED | — | Yes | November 11 |
| Thanksgiving | FLOATING | — | No | Fourth Thursday in November |
| Christmas Day | FIXED | — | Yes | |

## Early Closes

Not applicable — the `US` national calendar never includes early-close (half-day) sessions. NYSE's Day-After-Thanksgiving, July 3rd, and Christmas Eve half-day closes are market-only conventions, modeled separately under `XNYS`.

## Notes of Interest

`US`'s 11 holidays are a strict subset/superset comparison point for its two siblings, not an identical list: `XNYS` (NYSE) adds Good Friday — a market convention, not a federal holiday — on top of all 11 `US` holidays, plus 3 early closes. `USD` (Federal Reserve) shares 9 of `US`'s 11 holidays but is implemented as an entirely separate class rather than reusing `US`'s list (this codebase does not have a shared factory for the US calendars, unlike Switzerland's `ChHolidays` or Canada's `CanadaHolidays` — each of `US`, `USD`, and `XNYS` independently duplicates the identical `Holiday` definitions for shared holidays like New Year's Day and MLK Day).

Juneteenth was signed into federal law on June 17, 2021, and is modeled here as a `FIXED` holiday with `2021` as its effective first year via `HolidayCalendar.calculate()`'s inception-year filtering; contrast with `USD`'s implementation of Juneteenth (see [USD.md](./USD.md)), which uses an explicit `FLOATING` observance function returning `null` for years before 2021 — two different mechanisms achieving the same "don't appear before 2021" behavior.

## Sources

- No single official U.S. government page enumerates all 11 federal holidays with a citation-friendly URL as of 2026-08-04; the list matches the well-established public federal holiday schedule (5 U.S.C. § 6103) and is not independently disputed across sources checked
- Juneteenth's 2021 federal-holiday status: signed into law June 17, 2021 (Juneteenth National Independence Day Act) — corroborated across multiple secondary sources; no direct primary citation captured during this pass
