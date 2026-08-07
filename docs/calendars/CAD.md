# CAD — Bank of Canada (Lynx) Holiday Schedule

- **Standard:** ISO 4217 `CAD`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [CA](./CA.md) (national), [XTSE](./XTSE.md) (Toronto Stock Exchange) — `CAD` omits Easter Monday (not a Bank Act statutory holiday) and gates Family Day and National Day For Truth and Reconciliation to specific federal-adoption years, unlike `CA`'s unconditional inclusion.
- **Service class:** `HolidayCalendarServiceCAD` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** inline lambda — Saturday rolls forward 2 days (to Monday), Sunday rolls forward 1 day (to Monday); equivalent in effect to `DateRolls.followingMonday()`, but written inline
- **Rollability exceptions:** Family Day, Good Friday, Victoria Day, Civic Holiday, Labour Day, Thanksgiving Day, and Boxing Day are `rollable(false)` — the floating holidays are weekday-anchored, and Boxing Day is `rollable(false)` because its substitution logic is handled entirely inside its own `BoxingDayCAD` observance rather than via the generic roll mechanism (see Notes of Interest). National Day For Truth and Reconciliation is `rollable(true)` (see Holidays table).

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Family Day | FLOATING | 2013 | No | Gated to reflect its *federal* (Bank Act) adoption date — not the earlier date at which various provinces independently adopted their own Family Day |
| Good Friday | FLOATING | — | No | |
| Victoria Day | FLOATING | — | No | |
| Canada Day | FIXED | — | Yes | July 1 |
| Civic Holiday | FLOATING | — | No | First Monday in August |
| Labour Day | FLOATING | — | No | |
| National Day For Truth and Reconciliation | FLOATING | 2021 | Yes | |
| Thanksgiving Day | FLOATING | — | No | |
| Remembrance Day | FIXED | — | Yes | November 11 |
| Christmas Day | FIXED | — | Yes | |
| Boxing Day | FLOATING | — | No | Computed via `BoxingDayCAD` — see Notes of Interest |

Easter Monday is **not** observed by `CAD` (unlike `CA` and `XTSE`) — it is not a Bank Act statutory holiday.

## Early Closes

Not applicable — `CAD` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**Boxing Day/Christmas Day collision handling.** `CAD`'s generic `DateRoll` mechanism operates independently per holiday and can't detect that Christmas Day's own roll has already consumed December 26 as its substitute date. To handle this, Boxing Day is modeled as a `FLOATING` holiday computed by a dedicated `BoxingDayCAD` observance that encodes the full pairwise substitution logic directly:

- Dec 25 = Saturday → Christmas observed Mon 27; Boxing Day (naturally Sunday Dec 26) observed Tue 28
- Dec 25 = Sunday → Christmas observed Mon 26; Boxing Day displaced to Tue 27
- Dec 26 = Saturday (Christmas on Friday) → Christmas not rolled; Boxing Day observed Mon 28
- Dec 26 = Sunday (and Christmas isn't Saturday, already covered above) → Boxing Day observed Mon 27
- All other cases → Boxing Day observed on its natural date, December 26

This same `BoxingDayCAD` class is reused verbatim by `XTSE` (see [XTSE.md](./XTSE.md)).

**Family Day's 2013 gate is a federal-adoption date, not a provincial one.** Several Canadian provinces created their own "Family Day" holidays well before 2013 (Alberta and Saskatchewan from 2008, for example), but `CAD` models the date the *federal* Bank Act adopted Family Day for Lynx settlement purposes — a materially later date than when the holiday first existed anywhere in Canada.

## Sources

- [Bank of Canada — An Overview of Lynx, Canada's High-Value Payment System (May 2022)](https://www.bankofcanada.ca/wp-content/uploads/2022/05/Overview-Lynx-Canadas-High-Value-Payment-System.pdf) — official primary source confirming Lynx operates on all weekdays that are not statutory holidays
- [Bank of Canada — Bank of Canada holiday schedule](https://www.bankofcanada.ca/press/upcoming-events/bank-of-canada-holiday-schedule/) — official annual schedule page (referenced, not independently cross-checked against every date in the Holidays table above)
- Family Day's 2013 federal-adoption date and the Boxing Day/Christmas collision logic are corroborated by this project's own implementation comments (`HolidayCalendarServiceCAD.java`, `BoxingDayCAD.java`); not independently re-verified against a Bank Act primary text in this pass

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
