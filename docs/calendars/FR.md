# FR — France National Holidays

**Standard:** ISO 3166-1 alpha-2 `FR`
**Category:** National
**Sibling calendars:** [XPAR](./XPAR.md) (Euronext Paris) — `FR` and `XPAR` share all 11 holidays verbatim via the `FrHolidays` factory; `XPAR` additionally adds Good Friday (a market-only convention, not a French national holiday) plus two early closes.
**Service class:** `HolidayCalendarServiceFR` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.previousFridayOrFollowingMonday()`
- **Rollability exceptions:** Easter Monday, Ascension Day, and Whit Monday are `rollable(false)` (weekday-anchored). All 8 `FIXED` holidays are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Easter Monday | FLOATING | — | No | |
| Labour Day | FIXED | — | Yes | May 1 |
| Victory in Europe Day | FIXED | — | Yes | May 8 |
| Ascension Day | FLOATING | — | No | 39 days after Easter Sunday |
| Whit Monday | FLOATING | — | No | |
| Bastille Day | FIXED | — | Yes | July 14; French National Day |
| Assumption Day | FIXED | — | Yes | August 15 |
| All Saints' Day | FIXED | — | Yes | November 1 |
| Armistice Day | FIXED | — | Yes | November 11 |
| Christmas Day | FIXED | — | Yes | |

Good Friday is **not** included — confirmed not a French national holiday (unlike its inclusion on `XPAR`, a market-only convention there).

## Early Closes

Not applicable — the `FR` national calendar never includes early-close (half-day) sessions. Euronext Paris's Christmas Eve/New Year's Eve early closes are market-only, modeled separately under `XPAR`.

## Notes of Interest

`FR`'s 11 holidays are the complete official "jours fériés" (statutory public holidays) list under French labor law. Unlike Germany's or Switzerland's national calendars in this codebase, France's list requires no state/cantonal caveat — French public holidays are set uniformly at the national level (Alsace-Moselle observes two additional regional holidays, Good Friday and St. Stephen's Day, due to a historical legal carve-out predating 1918 reunification with France, but this codebase does not model that regional exception here).

## Sources

- No single official French government (service-public.fr or legifrance.gouv.fr) primary source was captured with a stable citation URL enumerating exactly these 11 dates in this pass; the "jours fériés" list is well-established public record under French labor law (Code du travail, Article L3133-1) and not independently disputed across sources checked
- The Alsace-Moselle regional exception (mentioned in Notes of Interest) is well-known public information but was not independently re-verified via primary source in this pass
