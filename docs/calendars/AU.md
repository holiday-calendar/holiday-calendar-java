# AU — Australia National Holidays

- **Standard:** ISO 3166-1 alpha-2 `AU`
- **Category:** National
- **Sibling calendars:** [AUD](./AUD.md) (RBA), [XASX](./XASX.md) (Australian Securities Exchange) — `AU` and `XASX` share all 9 holidays via the `AuHolidays` factory; `AUD` independently duplicates most of the same holidays but omits Easter Saturday and adds an NSW-specific Bank Holiday that neither `AU` nor `XASX` carries.
- **Service class:** `HolidayCalendarServiceAU` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.previousFridayOrFollowingMonday()`
- **Rollability exceptions:** Good Friday, Easter Saturday, Easter Monday, and King's Birthday are `rollable(false)` (weekday-anchored). All `FIXED` holidays (New Year's Day, Australia Day, ANZAC Day, Christmas Day, Boxing Day) are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Australia Day | FIXED | — | Yes | January 26 |
| Good Friday | FLOATING | — | No | |
| Easter Saturday | FLOATING | — | No | Day after Good Friday; **not observed in Western Australia or Tasmania** — see Notes of Interest |
| Easter Monday | FLOATING | — | No | |
| ANZAC Day | FIXED | — | Yes | April 25 |
| King's Birthday | FLOATING | — | No | Second Monday in June nationally; Queensland and Western Australia use different dates — see Notes of Interest |
| Christmas Day | FIXED | — | Yes | |
| Boxing Day | FIXED | — | Yes | |

## Early Closes

Not applicable — the `AU` national calendar never includes early-close (half-day) sessions. ASX's Christmas Eve/New Year's Eve early closes are market-only, modeled separately under `XASX`.

## Notes of Interest

**Easter Saturday is a genuine state-level exception, not modeled per-state.** It's included here as a single national entry, but is confirmed **not** observed in Western Australia or Tasmania — a consumer needing a WA- or Tasmania-specific calendar should be aware this codebase doesn't fork the holiday list by state.

**King's Birthday's date varies by state more than the single national entry suggests.** Most Australian states observe it on the 2nd Monday in June (aligned with the historical UK monarch's-birthday celebration), but Queensland has observed it on the 1st Monday in October since 2016 (moved specifically to spread public holidays more evenly through the year), and Western Australia observes it on a locally-decided date in late September or early October (typically the last Monday of September), set annually by the state governor. This codebase models only the 2nd-Monday-in-June national convention — the Queensland and Western Australia dates are not separately represented.

Both Easter Saturday and King's Birthday are retained in this single national list specifically because each is a genuine, state-backed public holiday *somewhere* in the country — the tradeoff is a national calendar that overstates observance for any single state and understates the two outlier King's Birthday dates.

## Sources

- Easter Saturday's exclusion in WA/Tasmania and King's Birthday's Queensland (1st Monday October, since 2016) and Western Australia (governor-set late-September/early-October date) variations are corroborated across [SBS News](https://www.sbs.com.au/news/article/kings-birthday-public-holiday-which-states-get-day-off/tz6nfmiar) and [Office Holidays — King's Birthday in Queensland](https://www.officeholidays.com/holidays/australia/queensland/australia-kings-birthday)
- No single official Australian federal government primary source enumerating all state-level public holiday variations was captured with a stable citation URL in this pass
