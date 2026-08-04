# MA — Morocco (National) Holidays

**Standard:** ISO 3166-1 alpha-2 `MA`
**Category:** National
**Sibling calendars:** [MAD](./MAD.md) (CSE/BAM settlement) — `MA` and `MAD` share the identical 17-holiday list via `MoroccoHolidays.baseHolidays()`, differing only in rollability and roll strategy.
**Service class:** `HolidayCalendarServiceMA` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday — **not** the Friday+Saturday GCC convention used by most other national calendars in this MENA module (Turkey is the only other exception). Morocco switched to the standard Monday–Friday workweek in 2004 to align with European trading partners.
- **Roll strategy:** `DateRolls.followingMonday()` — fixed Gregorian holidays falling on Saturday or Sunday roll forward to the following Monday, per Moroccan Labour Code practice
- **Rollability exceptions:** all Islamic holidays are `rollable(false)` — per this project's own documentation, Islamic holiday substitutions in Morocco are issued by case-by-case government decree, not a statutory automatic roll rule, so no generic roll is modeled for them. The 10 `FIXED` Gregorian holidays are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Manifesto of Independence Day | FIXED | — | Yes | January 11; 1944 Independence Manifesto submitted to the French Protectorate |
| Amazigh New Year | FIXED | 2024 | Yes | January 14 (Yennayer); gazetted November 2023 — see Notes of Interest |
| Eid al-Fitr | FLOATING | — | No | 1 Shawwal AH |
| Eid al-Fitr (2nd Day) | FLOATING | — | No | 2 Shawwal AH |
| Labour Day | FIXED | — | Yes | May 1 |
| Eid al-Adha | FLOATING | — | No | 10 Dhu al-Hijjah AH |
| Eid al-Adha (2nd Day) | FLOATING | — | No | 11 Dhu al-Hijjah AH |
| Throne Day | FIXED | — | Yes | July 30; accession of King Mohammed VI (1999) |
| Islamic New Year | FLOATING | — | No | 1 Muharram AH |
| Oued Ed-Dahab Allegiance Day | FIXED | — | Yes | August 14; allegiance of Oued Ed-Dahab to Morocco (1979) |
| Revolution of the King and the People | FIXED | — | Yes | August 20; exile of Sultan Mohammed V (1953) |
| Youth Day | FIXED | — | Yes | August 21; King Mohammed VI's birthday (1963) |
| Prophet's Birthday (Mawlid an-Nabi) | FLOATING | — | No | 12 Rabi' al-Awwal AH |
| Prophet's Birthday (2nd Day) | FLOATING | — | No | 13 Rabi' al-Awwal AH |
| Green March Anniversary | FIXED | — | Yes | November 6; 1975 Green March, Morocco's Western Sahara claim |
| Independence Day | FIXED | — | Yes | November 18; independence from the French Protectorate (1956) |

## Early Closes

Not applicable — `MA` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**Morocco's weekend, workweek, and roll direction all diverge from the GCC pattern that dominates this MENA module.** Where AE/BH/JO/KW/SA/QA all use a Friday+Saturday weekend with Sunday as the first business day, Morocco uses a Saturday+Sunday Western-style weekend with a standard Monday–Friday workweek — established by a 2004 policy change to align with European trading partners. The roll rule (`followingMonday()`) matches this Western-calendar orientation rather than the GCC's `followingSunday()` pattern.

**Amazigh New Year (Yennayer) is the newest holiday across this entire MENA batch, and is inherently a partial-history holiday.** Gazetted as a national public holiday in November 2023 and effective from 2024, it's modeled unconditionally as a `FixedHoliday` here (no built-in inception-year gating) — per the implementation's own comment, "callers computing dates prior to 2024 should discard this holiday as it was not legally observed before that year." This is a different mechanism than, e.g., US's Juneteenth (see [US.md](./US.md)), which does have inception-year filtering built into `HolidayCalendar.calculate()` — a consumer computing Morocco's 2020 holidays via this codebase would get an incorrect Yennayer entry unless they filter it out themselves.

**Islamic holiday substitution is explicitly *not* automated here, unlike every roll-based calendar elsewhere in this codebase.** Morocco's government issues holiday-shift decisions case-by-case rather than via a predictable statutory rule, so this project deliberately leaves all Islamic holidays `rollable(false)` with no substitute-date logic — a correctness-over-completeness choice, since attempting to guess Morocco's ad hoc decree pattern algorithmically would likely produce wrong dates.

**"Corrections require a new JAR release; no runtime update mechanism exists"** — this exact phrase appears in Morocco's own source comments regarding the Islamic-holiday CSV data, a level of explicitness about the CSV-backed approach's operational limitation not spelled out quite as directly in some sibling countries' documentation.

## Sources

- Islamic holiday sourcing (Moroccan government/CSE 2024–2025 announcements, Umm al-Qura projection for 2026–2055, data ceiling 2055), the Amazigh New Year gazetting date, the 2004 workweek change, and the CSE circular AV-2025-078 basis for the no-roll settlement convention are documented directly in this project's own source comments (`MoroccoHolidays.java`); no official Moroccan government or CSE primary-source page was independently captured with a stable citation URL in this pass
