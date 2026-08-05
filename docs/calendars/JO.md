# JO — Jordan (National) Holidays

- **Standard:** ISO 3166-1 alpha-2 `JO`
- **Category:** National
- **Sibling calendars:** [JOD](./JOD.md) (ASE/CBJ settlement) — `JO` and `JOD` share the identical 15-holiday list via `JordanHolidays.baseHolidays()`, differing only in rollability and roll strategy.
- **Service class:** `HolidayCalendarServiceJO` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday
- **Roll strategy:** `DateRolls.followingSunday()` — fixed holidays falling on Friday or Saturday roll forward to the following Sunday
- **Rollability exceptions:** Arafat Day and all Islamic holidays are `rollable(false)`. The 4 `FIXED` holidays (New Year's Day, Labour Day, Independence Day, Christmas Day) are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Eid al-Fitr | FLOATING | — | No | 1 Shawwal AH |
| Eid al-Fitr (2nd Day) | FLOATING | — | No | 2 Shawwal AH |
| Eid al-Fitr (3rd Day) | FLOATING | — | No | 3 Shawwal AH |
| Eid al-Fitr (4th Day) | FLOATING | — | No | 4 Shawwal AH — a 4-day observance, unlike the 2–3 day norm elsewhere in this codebase |
| Labour Day | FIXED | — | Yes | May 1 |
| Independence Day | FIXED | — | Yes | May 25; independence from the British Mandate (1946) |
| Arafat Day | FLOATING | — | No | 9 Dhu al-Hijjah AH |
| Eid al-Adha | FLOATING | — | No | 10 Dhu al-Hijjah AH |
| Eid al-Adha (2nd Day) | FLOATING | — | No | 11 Dhu al-Hijjah AH |
| Eid al-Adha (3rd Day) | FLOATING | — | No | 12 Dhu al-Hijjah AH |
| Eid al-Adha (4th Day) | FLOATING | — | No | 13 Dhu al-Hijjah AH — also a 4-day observance |
| Islamic New Year | FLOATING | — | No | 1 Muharram AH |
| Prophet's Birthday | FLOATING | — | No | 12 Rabi' al-Awwal AH |
| Christmas Day | FIXED | — | Yes | December 25 |

## Early Closes

Not applicable — `JO` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**Jordan is the only Muslim-majority-country calendar in this codebase's MENA module that includes Christmas Day as a gazetted national public holiday and confirmed ASE closure day**, per this project's own documentation, citing official ASE holiday schedules for 2025 and 2026. This reflects Jordan's officially-recognized Christian minority population and is a genuine national statutory holiday, not a market convention borrowed from a Western calendar (contrast with NYSE's or Euronext Paris's Good Friday, both explicitly *not* national holidays in their respective countries — see [XNYS.md](./XNYS.md), [XPAR.md](./XPAR.md)).

**Both Eids run a full 4 days each (1–4 Shawwal, 10–13 Dhu al-Hijjah)** — the longest observance for either Eid among all MENA calendars in this codebase; most neighbors use 2–3 days per Eid (see the comparison note in [SA.md](./SA.md)).

**Jordan's own religious authority (Ministry of Awqaf / Higher Judiciary Council) determines Islamic dates by moon sighting**, and per this project's own documentation, may differ from Saudi Arabia's Umm al-Qura-based dates by ±1 day — the same general MENA-wide caveat found in Bahrain's and Morocco's documentation.

## Sources

- Islamic holiday sourcing (CBJ/ASE 2024–2026 announcements, Umm al-Qura projection for 2027–2055, data ceiling 2055), the Christmas Day ASE-closure confirmation (2025/2026 schedules), and the 4-day Eid durations (confirmed by official ASE and CBJ holiday announcements for 2024–2026) are documented directly in this project's own source comments (`JordanHolidays.java`); no official CBJ/ASE primary-source page was independently captured with a stable citation URL in this pass
