# CA — Canada National Holidays

**Standard:** ISO 3166-1 alpha-2 `CA`
**Category:** National
**Sibling calendars:** [CAD](./CAD.md) (Bank of Canada/Lynx settlement), [XTSE](./XTSE.md) (Toronto Stock Exchange) — `CA` and `XTSE` share 10 holidays via the `CanadaHolidays` factory; `XTSE` additionally has a Christmas Eve early close. `CAD` independently duplicates most of the same holidays but omits Easter Monday and gates Family Day/National Day for Truth and Reconciliation to specific inception years.
**Service class:** `HolidayCalendarServiceCA` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.followingMonday()` — both Saturday and Sunday roll forward to the following Monday (never backward)
- **Rollability exceptions:** all `FLOATING` holidays (Family Day, Good Friday, Easter Monday, Victoria Day, Civic Holiday, Labour Day, Thanksgiving Day) are `rollable(false)` — weekday-anchored. `FIXED` holidays (New Year's Day, Canada Day, Remembrance Day, Christmas Day, Boxing Day) and National Day For Truth and Reconciliation are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Family Day | FLOATING | — | No | Provincial-origin holiday, nationally modeled here |
| Good Friday | FLOATING | — | No | |
| Easter Monday | FLOATING | — | No | Not observed by `CAD` — see [CAD.md](./CAD.md) |
| Victoria Day | FLOATING | — | No | Official celebration of the Sovereign's birthday |
| Canada Day | FIXED | — | Yes | July 1 |
| Civic Holiday | FLOATING | — | No | Varies by province in real-world observance; modeled here as a single national date |
| Labour Day | FLOATING | — | No | |
| Thanksgiving Day | FLOATING | — | No | |
| Remembrance Day | FIXED | — | Yes | November 11 |
| National Day For Truth and Reconciliation | FLOATING | 2021 | Yes | First observed September 30, 2021 |
| Christmas Day | FIXED | — | No | |
| Boxing Day | FIXED | — | No | |

## Early Closes

Not applicable — the `CA` national calendar never includes early-close (half-day) sessions. TSX's Christmas Eve early close is modeled separately under `XTSE`.

## Notes of Interest

**Christmas Day and Boxing Day are `rollable(false)` on `CA`, unlike most other calendars in this codebase.** This appears to reflect that the federal statutory holiday schedule for these two dates doesn't use a simple weekend-roll substitution the way `CAD`/`XTSE` do (see their collision-aware `BoxingDayCAD` observance) — a consumer expecting `CA` to roll Christmas/Boxing Day off weekends the same way `CAD` or `XTSE` do will get different results.

**Shared-factory pattern.** `CA` and `XTSE` both draw their 10 common holidays from a package-private `CanadaHolidays.baseHolidays()` factory, then each adds its own remaining holidays independently (`CA` adds National Day For Truth and Reconciliation, Christmas Day, and Boxing Day as `rollable(false)`; `XTSE` adds the same three days but with different rollability and a collision-aware Boxing Day observance — see [XTSE.md](./XTSE.md)).

## Sources

- No single official Government of Canada page enumerating all of `CA`'s specific 13 dates (as opposed to the general federal statutory holidays list) was located and cross-checked in this pass; the general holiday set matches widely-corroborated public sources
- National Day For Truth and Reconciliation's first-observed date (September 30, 2021) is well-established public record (created by federal legislation in 2021)
