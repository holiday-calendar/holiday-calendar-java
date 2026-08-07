# EG — Egypt (National) Holidays

- **Standard:** ISO 3166-1 alpha-2 `EG`
- **Category:** National
- **Sibling calendars:** [EGP](./EGP.md) (EGX/CBE settlement) — `EGP` omits Arafat Day (which `EG` carries), for 16 vs. `EG`'s 17 holidays.
- **Service class:** `HolidayCalendarServiceEG` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (GCC market convention)
- **Roll strategy:** `DateRolls.followingSunday()` — fixed holidays falling on Friday or Saturday roll forward to the following Sunday
- **Rollability exceptions:** Sham El-Nessim and all Islamic holidays are `rollable(false)`. The 7 `FIXED` Gregorian holidays are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| Coptic Christmas | FIXED | — | Yes | January 7 |
| Revolution Day | FIXED | — | Yes | January 25; start of the 2011 uprising |
| Sinai Liberation Day | FIXED | — | Yes | April 25; return of Sinai to Egyptian sovereignty (1982) |
| Sham El-Nessim | FLOATING | — | No | Day after Coptic Easter; observed by all Egyptians regardless of religion — see Notes of Interest |
| Labour Day | FIXED | — | Yes | May 1 |
| June 30 Revolution | FIXED | — | Yes | Removal of Mohamed Morsi from the presidency (2013) |
| Revolution Day (July 23) | FIXED | — | Yes | Free Officers' overthrow of King Farouk (1952) |
| Armed Forces Day | FIXED | — | Yes | October 6; 1973 October War Suez Canal crossing |
| Arafat Day | FLOATING | — | No | 9 Dhu al-Hijjah AH; **not** carried by `EGP` — see [EGP.md](./EGP.md) |
| Eid al-Fitr | FLOATING | — | No | 1 Shawwal AH |
| Eid al-Fitr (2nd Day) | FLOATING | — | No | 2 Shawwal AH |
| Eid al-Fitr (3rd Day) | FLOATING | — | No | 3 Shawwal AH |
| Eid al-Adha | FLOATING | — | No | 10 Dhu al-Hijjah AH |
| Eid al-Adha (2nd Day) | FLOATING | — | No | 11 Dhu al-Hijjah AH |
| Eid al-Adha (3rd Day) | FLOATING | — | No | 12 Dhu al-Hijjah AH |
| Islamic New Year | FLOATING | — | No | 1 Muharram AH |
| Prophet's Birthday | FLOATING | — | No | 12 Rabi' al-Awwal AH |

## Early Closes

Not applicable — `EG` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**Sham El-Nessim isn't CSV-backed, and has no data ceiling.** Unlike every other floating holiday in this calendar, Sham El-Nessim is computed algorithmically as the day after Coptic Easter (which shares the same Julian-calendar algorithm as Orthodox Easter used elsewhere in this codebase — see [western observance package](../OBSERVANCE_PATTERNS.md)) — it's available for any year within that algorithm's valid range (530–3399 AD per the implementation's own comment), not subject to the 2055 CSV ceiling that governs the Islamic holidays here.

**Egypt has the highest holiday count of any MENA calendar in this codebase (17)**, driven by four distinct revolution/liberation anniversaries (1952, 1973, 1982, 2011/2013) in addition to the standard Islamic and Gregorian holidays — a reflection of Egypt's modern political history rather than a data-modeling artifact.

**Real-world ad hoc bridge days aren't captured here, and the implementation says so explicitly.** Egypt frequently announces additional holidays and bridge days via Prime Minister's decree on short notice — this project's own comment flags that these "are not capturable in a static calendar," meaning a consumer needing the *actual* current-year closure schedule should treat this codebase's 17-holiday list as a floor, not a guarantee of completeness for any specific year.

## Sources

- Islamic holiday sourcing (CBE/EGX 2024–2025 announcements, Umm al-Qura projection for 2026–2055) and the ad hoc bridge-day caveat are documented directly in this project's own source comments (`EgyptHolidays.java`); no official CBE/EGX primary-source page was independently captured with a stable citation URL in this pass

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
