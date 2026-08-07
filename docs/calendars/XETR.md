# XETR — Deutsche Börse Xetra Holidays

- **Standard:** ISO 10383 MIC `XETR`
- **Category:** Market/Exchange
- **Sibling calendars:** [DE](./DE.md) (national) — `XETR` shares all 9 of `DE`'s holidays verbatim via the `DeHolidays` factory, adding Christmas Eve and New Year's Eve as market-only closures on top.
- **Service class:** `HolidayCalendarServiceXETR` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.previousFridayOrFollowingMonday()` — same as `DE`
- **Rollability exceptions:** Good Friday, Easter Monday, Ascension Day, and Whit Monday are `rollable(false)` (inherited from `DeHolidays`). Christmas Eve and New Year's Eve are `rollable(false)` by design — see Notes of Interest. All other `FIXED` holidays are `rollable(true)`.

## Holidays

Shares all 9 of `DE`'s holidays verbatim — see [DE.md](./DE.md) for the full table. Xetra/Frankfurt is closed on every one of them.

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| Christmas Eve | FLOATING | — | No | Full non-trading day (Erfüllungstag) at Xetra/Frankfurt; confirmed **not** a German public holiday |
| New Year's Eve | FLOATING | — | No | Full non-trading day (Erfüllungstag) at Xetra/Frankfurt; confirmed **not** a German public holiday |

## Early Closes

Not applicable — Christmas Eve and New Year's Eve are modeled as full `FLOATING` holidays here, not `EARLY_CLOSE` entries; see Notes of Interest for what actually happens on these days.

## Notes of Interest

**"Erfüllungstag" means settlement day, and the real-world behavior is more nuanced than "closed."** Per Deutsche Börse's own trading-calendar pages, Christmas Eve and New Year's Eve are formally settlement days (Erfüllungstage) — they count toward settlement-date calculation for trades — but on-venue behavior that day is restricted rather than fully suspended: existing orders can still be queried, but participants cannot enter, change, or delete individual orders, and end-of-day processing (including publishing the securities master data file) proceeds at the usual time. This project models both days as full `FLOATING` non-trading holidays (`Holiday.Type.FLOATING`, not `EARLY_CLOSE`), which is directionally correct (no new trading activity) but doesn't capture the "orders queryable, master data still published" nuance — worth knowing if a consumer needs to distinguish "fully dark" from "restricted."

**Omitted, not shifted, on a weekend.** Both dates are modeled with `rollable(false)` and no weekend-substitution behavior — if December 24 or 31 falls on a Saturday or Sunday, there is simply no Xetra closure that year for that date, rather than a shifted weekday closure. This is a different behavior from calendars where an equivalent early close shifts to an adjacent weekday (e.g. LSE's, see [XLON.md](./XLON.md)).

## Sources

- [Deutsche Börse Xetra — Christmas Eve (trading calendar)](https://www.xetra.com/xetra-en/newsroom/trading-calendar/non-trading-days/Christmas-Eve-2352286) — official primary source
- [Deutsche Börse Xetra — New Year's Eve (trading calendar)](https://www.xetra.com/xetra-en/newsroom/trading-calendar/non-trading-days/New-Years-Eve-1669824) — official primary source
- [Deutsche Börse — Trading Calendar Xetra and Frankfurt](https://www.cashmarket.deutsche-boerse.com/cash-en/trading/trading-calendar-and-trading-hours) — official trading calendar and hours reference

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
