# XASX — Australian Securities Exchange (ASX) Holidays

- **Standard:** ISO 10383 MIC `XASX`
- **Category:** Market/Exchange
- **Sibling calendars:** [AU](./AU.md) (national), [AUD](./AUD.md) (RBA) — `XASX` shares all 9 of `AU`'s holidays verbatim via the `AuHolidays` factory, adding two early closes on top. Unlike `AUD`, `XASX` includes Easter Saturday and excludes the NSW Bank Holiday.
- **Service class:** `HolidayCalendarServiceXASX` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.previousFridayOrFollowingMonday()` — same as `AU`
- **Rollability exceptions:** Good Friday, Easter Saturday, Easter Monday, and King's Birthday are `rollable(false)` (inherited from `AuHolidays`). Christmas Eve and New Year's Eve early closes are `rollable(false)` by design. All other `FIXED` holidays are `rollable(true)`.

## Holidays

Shares all 9 of `AU`'s holidays verbatim, including Easter Saturday (with its WA/Tasmania exclusion caveat) and the single national King's Birthday date — see [AU.md](./AU.md) for the full table and state-variation notes. ASX is closed on every one of them.

## Early Closes

| Name | Close Time | Time Zone | Notes |
|------|-----------|-----------|-------|
| Christmas Eve | 14:10 | Australia/Sydney | Suppressed entirely (not shifted) when December 24 falls on a Saturday or Sunday |
| New Year's Eve | 14:10 | Australia/Sydney | Suppressed entirely (not shifted) when December 31 falls on a Saturday or Sunday |

## Notes of Interest

The 14:10 (2:10pm) AEDT/AEST early-close time for both Christmas Eve and New Year's Eve was independently confirmed via ASX's own official trading-hours notice during this research pass, including the detail that the Cash Market Closing Single Price Auction runs 30 seconds after the 14:10 cutoff, with late-trading session states starting at 14:21:30 — matching this project's implementation exactly.

## Sources

- [ASX Online — ASX Trade trading hours for Christmas and New Year 2025/2026](https://asxonline.com/content/asxonline/public/notices/2025/november/1402.25.11.html) — official primary source; confirms the 14:10 Sydney-time early close for both Christmas Eve and New Year's Eve, matching this project's implementation exactly
