# XNYS — New York Stock Exchange (NYSE) Holidays

- **Standard:** ISO 10383 MIC `XNYS`
- **Category:** Market/Exchange
- **Sibling calendars:** [US](./US.md) (national), [USD](./USD.md) (Federal Reserve) — `XNYS` adds Good Friday (a market convention, not a federal holiday) on top of every `US` holiday, and carries 3 `EARLY_CLOSE` entries neither sibling has. Unlike `USD`, `XNYS` excludes Columbus Day and Veterans Day.
- **Service class:** `HolidayCalendarServiceXNYS` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** inline lambda — Saturday rolls back to the preceding Friday, Sunday rolls forward to the following Monday (same effect as `DateRolls.previousFridayOrFollowingMonday()`, written inline)
- **Rollability exceptions:** MLK Day, Presidents' Day, Good Friday, Memorial Day, Labor Day, Columbus Day (n/a — not observed, see Holidays table), and Thanksgiving are `rollable(false)` (weekday-anchored floating holidays). `FIXED` holidays are `rollable(true)`. All `EARLY_CLOSE` entries are `rollable(false)` by construction (see `Holiday.Type.EARLY_CLOSE` — early closes are never date-rolled).

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Martin Luther King Jr. Day | FLOATING | — | No | Third Monday in January |
| Presidents' Day | FLOATING | — | No | Third Monday in February |
| Good Friday | FLOATING | — | No | NYSE market closure by long-standing convention; **not** a US federal holiday — the one regularly-scheduled NYSE closure with no federal-holiday counterpart |
| Memorial Day | FLOATING | — | No | Last Monday in May |
| Juneteenth | FIXED | 2021 | Yes | |
| Independence Day | FIXED | — | Yes | July 4 |
| Labor Day | FLOATING | — | No | First Monday in September |
| Veterans Day | FIXED | — | Yes | November 11 |
| Thanksgiving | FLOATING | — | No | Fourth Thursday in November |
| Christmas Day | FIXED | — | Yes | |

Columbus Day is **not** observed by `XNYS` (unlike `US` and `USD`).

## Early Closes

| Name | Close Time | Time Zone | Notes |
|------|-----------|-----------|-------|
| Day After Thanksgiving | 13:00 | America/New_York | Unconditional — occurs every year |
| July 3rd | 13:00 | America/New_York | Occurs only when July 4 falls Tuesday through Friday (i.e. is suppressed when it would coincide with or immediately follow a weekend) |
| Christmas Eve | 13:00 | America/New_York | Occurs only when December 25 falls Tuesday through Friday |

## Notes of Interest

Good Friday is the standout peculiarity of this calendar: it is the only regularly-scheduled NYSE closure that has no US federal-holiday counterpart, observed purely by market convention — confirmed across multiple financial-news sources describing NYSE's holiday schedule.

The July 3rd and Christmas Eve early closes share a common suppression pattern — both are keyed off whether the *following* federal holiday (Independence Day, Christmas Day) falls on a day that would already create a long weekend without an early close (i.e. Mon/Sat/Sun), in which case the early close doesn't apply. This is a different mechanism than TSX's Christmas Eve early close (see [XTSE.md](./XTSE.md)), which is keyed directly off December 24's own day of week rather than off December 25's.

## Sources

- Good Friday as NYSE's sole non-federal closure is corroborated across multiple financial-media sources describing the NYSE holiday schedule (e.g. Benzinga, IBTimes); no single official NYSE primary-source page was captured during this pass — flagged rather than omitted, per this project's citation standard
- Early-close times (13:00 ET) and suppression conditions match this project's existing implementation comments; not independently re-verified against an official NYSE circular in this pass
