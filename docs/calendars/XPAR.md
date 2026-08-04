# XPAR — Euronext Paris Holidays

**Standard:** ISO 10383 MIC `XPAR`
**Category:** Market/Exchange
**Sibling calendars:** [FR](./FR.md) (national) — `XPAR` shares all 11 of `FR`'s holidays verbatim via the `FrHolidays` factory, adding Good Friday and two early closes on top.
**Service class:** `HolidayCalendarServiceXPAR` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.previousFridayOrFollowingMonday()` — same as `FR`
- **Rollability exceptions:** Easter Monday, Whit Monday, and Good Friday are `rollable(false)` (inherited/weekday-anchored). Christmas Eve and New Year's Eve early closes are `rollable(false)` by design — see Notes of Interest. All other `FIXED` holidays are `rollable(true)`.

## Holidays

Shares all 11 of `FR`'s holidays verbatim — see [FR.md](./FR.md) for the full table. Euronext Paris is closed on every one of them.

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| Good Friday | FLOATING | — | No | Euronext Paris market closure; confirmed **not** a French national holiday (contrast with `FR`, which excludes it) |

## Early Closes

| Name | Close Time | Time Zone | Notes |
|------|-----------|-----------|-------|
| Christmas Eve | 14:05 | Europe/Paris | Suppressed entirely (not shifted) when December 24 falls on a Saturday or Sunday |
| New Year's Eve | 14:05 | Europe/Paris | Suppressed entirely (not shifted) when December 31 falls on a Saturday or Sunday |

## Notes of Interest

Euronext Paris's own public trading-hours page lists only 5 full-closure holidays (New Year's Day, Good Friday, Easter Monday, Labour Day, Christmas Day) — narrower than the 12-holiday full-closure list this project actually implements (the 11-holiday `FrHolidays` base list plus Good Friday, including Victory in Europe Day, Ascension Day, Whit Monday, Bastille Day, Assumption Day, All Saints' Day, and Armistice Day). Euronext's summary page appears to only describe headline closures rather than the complete list; this project's implementation should be treated as the more complete source, but the discrepancy is worth flagging rather than silently reconciling.

The 14:05 CET early-close time for both Christmas Eve and New Year's Eve was independently confirmed via Euronext's own trading-hours page during this research pass, and matches this project's implementation exactly.

## Sources

- [Euronext — Trading hours & holidays](https://www.euronext.com/en/trading/trading-hours-holidays) — official primary source; confirms the 14:05 CET (2:05pm) early-close time for Christmas Eve and New Year's Eve, matching this project's implementation exactly. Euronext's own summary lists only 5 full-closure holidays (New Year's Day, Good Friday, Easter Monday, Labour Day, Christmas Day) — narrower than this project's modeled 12-holiday full-closure list; see Notes of Interest
