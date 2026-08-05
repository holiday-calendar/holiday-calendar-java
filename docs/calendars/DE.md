# DE — Germany National Holidays

- **Standard:** ISO 3166-1 alpha-2 `DE`
- **Category:** National
- **Sibling calendars:** [XETR](./XETR.md) (Deutsche Börse Xetra) — `DE` and `XETR` share all 9 holidays verbatim via the `DeHolidays` factory; `XETR` additionally has Christmas Eve and New Year's Eve as market-only closures.
- **Service class:** `HolidayCalendarServiceDE` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.previousFridayOrFollowingMonday()`
- **Rollability exceptions:** Good Friday, Easter Monday, Ascension Day, and Whit Monday are `rollable(false)` (weekday-anchored). All `FIXED` holidays (New Year's Day, Labour Day, German Unity Day, Christmas Day, Boxing Day) are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Good Friday | FLOATING | — | No | |
| Easter Monday | FLOATING | — | No | |
| Labour Day | FIXED | — | Yes | May 1 |
| Ascension Day | FLOATING | — | No | 39 days after Easter Sunday |
| Whit Monday | FLOATING | — | No | |
| German Unity Day | FIXED | — | Yes | October 3 |
| Christmas Day | FIXED | — | Yes | |
| Boxing Day | FIXED | — | Yes | |

These are exactly Germany's 9 official nationwide (bundesweite) public holidays — no regional/state (Länder) holidays are included.

## Early Closes

Not applicable — the `DE` national calendar never includes early-close (half-day) sessions. Xetra's Christmas Eve/New Year's Eve closures are market-only, modeled separately under `XETR`.

## Notes of Interest

Germany observes many additional public holidays at the individual *Land* (state) level (e.g. Epiphany, Corpus Christi, Reformation Day, All Saints' Day — each recognized in only some states), none of which are included here. `DE` models only the 9 holidays observed nationwide across all 16 states, consistent with this project's general pattern of modeling a single canonical national list rather than per-region variants (see also [CH.md](./CH.md)'s cantonal-variation note for a similar simplification in Switzerland).

## Sources

- No single official German federal government page enumerating exactly these 9 nationwide holidays (as distinct from state-level ones) was captured with a stable citation URL in this pass; the 9-holiday nationwide list is well-established public record, consistent across German public-holiday reference sources
