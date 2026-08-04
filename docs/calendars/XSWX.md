# XSWX — SIX Swiss Exchange Holidays

**Standard:** ISO 10383 MIC `XSWX`
**Category:** Market/Exchange
**Sibling calendars:** [CH](./CH.md) (national), [CHF](./CHF.md) (SIC/SNB settlement) — `XSWX` shares all 9 of `CH`'s holidays verbatim via the `ChHolidays` factory, adding Christmas Eve and New Year's Eve as market-only closures on top. Unlike `CHF`, `XSWX` uses the same weekend-roll rule as `CH`.
**Service class:** `HolidayCalendarServiceXSWX` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.previousFridayOrFollowingMonday()` — same as `CH`
- **Rollability exceptions:** Good Friday, Easter Monday, Ascension Day, and Whit Monday are `rollable(false)` (weekday-anchored, inherited from `ChHolidays`). All `FIXED` holidays, including the two market-only additions, are `rollable(true)`.

## Holidays

Shares all 9 of `CH`'s holidays verbatim (New Year's Day through Boxing Day) — see [CH.md](./CH.md) for the full table, including the Good Friday/Ticino/Valais caveat. SIX is closed on every one of them.

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| Christmas Eve | FIXED | — | Yes | SIX-only market holiday per the official SIX Trading Calendar; **not** a cantonal-law public holiday |
| New Year's Eve | FIXED | — | Yes | SIX-only market holiday per the official SIX Trading Calendar; **not** a cantonal-law public holiday |

## Early Closes

Not applicable — SIX has no `EARLY_CLOSE` (half-day) sessions; Christmas Eve and New Year's Eve are modeled as full `FIXED` holidays, not early closes.

## Notes of Interest

Christmas Eve and New Year's Eve are the only two dates where `XSWX` diverges from `CH`'s national list, and both are explicitly confirmed (per the implementation's own Javadoc) to be SIX-specific market conventions rather than cantonal public holidays — worth noting since they could be mistaken for Swiss public holidays given how common Dec 24/31 closures are across other calendars in this codebase (compare with Xetra's and Euronext Paris's early closes on the same two dates, which are half-days rather than full closures).

## Sources

- [SIX — Trading & Currency Holiday Calendar](https://www.six-group.com/en/market-data/news-tools/trading-currency-holiday-calendar.html) — official primary source for SIX's trading calendar, including the Christmas Eve/New Year's Eve market-only closures
