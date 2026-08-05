# BH — Bahrain (National) Holidays

- **Standard:** ISO 3166-1 alpha-2 `BH`
- **Category:** National
- **Sibling calendars:** [BHD](./BHD.md) (Boursa Bahrain/CBB settlement) — `BH` and `BHD` share the identical 15-holiday list via `BahrainHolidays.baseHolidays()`, differing only in rollability and roll strategy.
- **Service class:** `HolidayCalendarServiceBH` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (GCC market convention)
- **Roll strategy:** `DateRolls.followingSunday()` — fixed holidays falling on Friday or Saturday roll forward to the following Sunday
- **Rollability exceptions:** Arafat Day and all Islamic holidays are `rollable(false)`. The 4 `FIXED` Gregorian holidays are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Eid al-Fitr | FLOATING | — | No | 1 Shawwal AH |
| Eid al-Fitr (2nd Day) | FLOATING | — | No | 2 Shawwal AH |
| Eid al-Fitr (3rd Day) | FLOATING | — | No | 3 Shawwal AH |
| Labour Day | FIXED | — | Yes | May 1 |
| Arafat Day | FLOATING | — | No | 9 Dhu al-Hijjah AH |
| Eid al-Adha | FLOATING | — | No | 10 Dhu al-Hijjah AH |
| Eid al-Adha (2nd Day) | FLOATING | — | No | 11 Dhu al-Hijjah AH |
| Eid al-Adha (3rd Day) | FLOATING | — | No | 12 Dhu al-Hijjah AH |
| Islamic New Year | FLOATING | — | No | 1 Muharram AH |
| Ashura | FLOATING | — | No | 10 Muharram AH; see Notes of Interest |
| Ashura (2nd Day) | FLOATING | — | No | 11 Muharram AH |
| Prophet's Birthday | FLOATING | — | No | 12 Rabi' al-Awwal AH |
| National Day | FIXED | — | Yes | December 16; independence from Britain (1971), observed on this date |
| Accession Day | FIXED | — | Yes | December 17; accession of King Hamad bin Isa Al Khalifa (1999), observed on this date |

## Early Closes

Not applicable — `BH` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**Ashura is a genuine two-day national holiday in Bahrain**, gazetted under the Labour Law and confirmed by CBB circulars that Boursa Bahrain closes both days — this is the only calendar in this codebase's MENA module to observe Ashura as a public holiday rather than omitting it (a notable choice given Bahrain's significant Shia Muslim population, for whom Ashura carries particular religious significance).

**National Day and Accession Day both use "observed" dates that differ from their historical anniversary dates.** National Day marks independence from Britain on 15 August 1971, but is observed December 16; Accession Day marks King Hamad's 6 March 1999 accession, but is observed December 17. Both are officially commemorated on consecutive December dates rather than their actual historical anniversaries — worth knowing since a naive reader might expect the holiday dates to match the described historical events.

**Bahrain's own moon-sighting can differ from Saudi Arabia's by ±1 day**, per this project's own documentation — consistent with the general MENA-wide pattern (Morocco, Egypt, Jordan, and others all carry similar caveats) that Umm al-Qura projections are a convenience baseline, not a guarantee of matching every country's actual observed date.

## Sources

- Islamic holiday sourcing (CBB/Boursa Bahrain 2024–2025 announcements, Umm al-Qura projection for 2026–2055, data ceiling 2055), the Ashura gazetting under the Labour Law, and the National/Accession Day historical dates are documented directly in this project's own source comments (`BahrainHolidays.java`); no official CBB/Boursa Bahrain primary-source page was independently captured with a stable citation URL in this pass

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
