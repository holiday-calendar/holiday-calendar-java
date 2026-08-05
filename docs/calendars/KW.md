# KW — Kuwait (National) Holidays

- **Standard:** ISO 3166-1 alpha-2 `KW`
- **Category:** National
- **Sibling calendars:** [KWD](./KWD.md) (Boursa Kuwait/CBK settlement) — `KW` and `KWD` share the identical 13-holiday list via `KuwaitHolidays.baseHolidays()`, differing only in rollability and roll strategy.
- **Service class:** `HolidayCalendarServiceKW` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (GCC market convention)
- **Roll strategy:** `DateRolls.followingSunday()` — fixed holidays falling on Friday or Saturday roll forward to the following Sunday
- **Rollability exceptions:** Isra and Mi'raj and all Islamic holidays are `rollable(false)`. The 3 `FIXED` Gregorian holidays (New Year's Day, National Day, Liberation Day) are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| National Day | FIXED | — | Yes | February 25; independence from Britain (1961) |
| Liberation Day | FIXED | — | Yes | February 26; liberation from Iraqi occupation (1991) |
| Isra and Mi'raj | FLOATING | — | No | 27 Rajab AH — see Notes of Interest, absent from most other MENA calendars in this codebase |
| Eid al-Fitr | FLOATING | — | No | 1 Shawwal AH |
| Eid al-Fitr (2nd Day) | FLOATING | — | No | 2 Shawwal AH |
| Eid al-Fitr (3rd Day) | FLOATING | — | No | 3 Shawwal AH |
| Eid al-Adha | FLOATING | — | No | 10 Dhu al-Hijjah AH |
| Eid al-Adha (2nd Day) | FLOATING | — | No | 11 Dhu al-Hijjah AH |
| Eid al-Adha (3rd Day) | FLOATING | — | No | 12 Dhu al-Hijjah AH |
| Arafat Day | FLOATING | — | No | 9 Dhu al-Hijjah AH |
| Islamic New Year | FLOATING | — | No | 1 Muharram AH |
| Prophet's Birthday | FLOATING | — | No | 12 Rabi' al-Awwal AH |

## Early Closes

Not applicable — `KW` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**National Day (Feb 25) and Liberation Day (Feb 26) fall on consecutive calendar days**, both fixed and both independently rollable — a two-day national commemoration block honoring two distinct historical events (1961 independence, 1991 liberation) rather than a single multi-day holiday.

**Isra and Mi'raj is observed here, unlike most of this codebase's other MENA calendars.** UAE explicitly does *not* model it (see [AE.md](./AE.md)'s Notes of Interest, which frames it as a candidate for future addition only "if the UAE SCA declares [it] as a settlement closure day"). Kuwait is the one calendar in this codebase that does observe it as a full public holiday.

## Sources

- Islamic holiday sourcing (CBK/Boursa Kuwait 2024–2026 announcements, Umm al-Qura projection for 2027–2055, data ceiling 2055) is documented directly in this project's own source comments (`KuwaitHolidays.java`); no official CBK/Boursa Kuwait primary-source page was independently captured with a stable citation URL in this pass
