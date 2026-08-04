# SA — Saudi Arabia (National) Holidays

**Standard:** ISO 3166-1 alpha-2 `SA`
**Category:** National
**Sibling calendars:** [SAR](./SAR.md) (Tadawul/SAMA settlement) — `SA` and `SAR` share the identical 10-holiday list via `SaudiHolidays.baseHolidays()`, differing only in rollability and roll strategy.
**Service class:** `HolidayCalendarServiceSA` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (GCC market convention)
- **Roll strategy:** `DateRolls.followingSunday()` — fixed holidays falling on Friday or Saturday roll forward to the following Sunday
- **Rollability exceptions:** all 8 Islamic holidays are `rollable(false)`. The 2 `FIXED` Gregorian holidays (Saudi Founding Day, Saudi National Day) are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| Saudi Founding Day | FIXED | — | Yes | February 22; commemorates the founding of the (first) Saudi state in 1727 |
| Eid al-Fitr | FLOATING | — | No | 1 Shawwal AH |
| Eid al-Fitr (2nd Day) | FLOATING | — | No | 2 Shawwal AH |
| Eid al-Fitr (3rd Day) | FLOATING | — | No | 3 Shawwal AH |
| Eid al-Adha | FLOATING | — | No | 10 Dhu al-Hijjah AH |
| Eid al-Adha (2nd Day) | FLOATING | — | No | 11 Dhu al-Hijjah AH |
| Eid al-Adha (3rd Day) | FLOATING | — | No | 12 Dhu al-Hijjah AH |
| Islamic New Year | FLOATING | — | No | 1 Muharram AH |
| Prophet's Birthday | FLOATING | — | No | 12 Rabi' al-Awwal AH |
| Saudi National Day | FIXED | — | Yes | September 23; unification of the Kingdom (1932) |

## Early Closes

Not applicable — `SA` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**Saudi Arabia is the Umm al-Qura calendar's own home jurisdiction, yet still isn't guaranteed to match every neighboring country.** Saudi Arabia uses the Umm al-Qura calendar as its official state calendar, and the Umm al-Qura tabular projection is also the fallback data source this project uses for other GCC countries' far-future years — but the implementation's own comment cautions that Saudi Arabia's actual moon-sighting determinations may still differ from UAE announcements by ±1 day in a given year, since Umm al-Qura is a civil/administrative calendar, not a substitute for the religious moon-sighting process that ultimately determines each country's *observed* holiday.

**Three days each for both Eids**, unlike some neighbors — Bahrain, Jordan, and Turkey each add a 4th Eid al-Adha day (or more), while Qatar and the UAE keep both Eids at a shorter 2-day span. This project's holiday-count table across MENA countries (see [README.md](./README.md)) is the fastest way to compare durations at a glance.

## Sources

- Islamic holiday sourcing (Tadawul/SAMA 2024–2026 announcements, Umm al-Qura projection for 2027–2055, data ceiling 2055) is documented directly in this project's own source comments (`SaudiHolidays.java`); no official Tadawul/SAMA primary-source page was independently captured with a stable citation URL in this pass
