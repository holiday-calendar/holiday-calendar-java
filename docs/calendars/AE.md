# AE — UAE (National) Holidays

- **Standard:** ISO 3166-1 alpha-2 `AE`
- **Category:** National
- **Sibling calendars:** [AED](./AED.md) (CBUAE/DFM/ADX settlement) — `AE` and `AED` share the identical 10-holiday list via `UaeHolidays.baseHolidays()`, differing only in rollability and roll strategy.
- **Service class:** `HolidayCalendarServiceAE` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (GCC market convention; Sunday is the first business day)
- **Roll strategy:** `DateRolls.followingSunday()` — fixed holidays falling on Friday or Saturday roll forward to the following Sunday
- **Rollability exceptions:** all 6 Islamic holidays are `rollable(false)` — Hijri-calendar dates observed on their specific calendar day regardless of Gregorian weekday. The 4 `FIXED` Gregorian holidays are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Eid al-Fitr | FLOATING | — | No | 1 Shawwal AH; CSV-backed, see Notes of Interest |
| Eid al-Fitr (2nd Day) | FLOATING | — | No | 2 Shawwal AH |
| Eid al-Adha | FLOATING | — | No | 10 Dhu al-Hijjah AH |
| Eid al-Adha (2nd Day) | FLOATING | — | No | 11 Dhu al-Hijjah AH |
| Islamic New Year | FLOATING | — | No | 1 Muharram AH |
| Prophet's Birthday | FLOATING | — | No | 12 Rabi' al-Awwal AH |
| Commemoration Day | FIXED | — | Yes | November 30; honors Emirati Martyrs |
| National Day | FIXED | — | Yes | December 2; formation of the UAE (1971) |
| National Day (2nd Day) | FIXED | — | Yes | December 3 |

Arafat Day and Isra' Mi'raj are **deliberately not modeled** — see Notes of Interest.

## Early Closes

Not applicable — `AE` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**Data ceiling: 2055, CSV-backed with two distinct sourcing eras.** All 6 Islamic holidays are populated via country-specific CSV lookup tables (`dataValidThrough()` returns 2055). Per the implementation's own documentation, 2024–2026 dates are official UAE SCA/DFM announcements (i.e. actual observed dates); 2027–2055 are *projected* from the Umm al-Qura tabular Islamic calendar rather than observed — a consumer relying on far-future dates (e.g. 2050) should treat them as astronomical projections, not confirmed government announcements, and should re-verify against official sources as each year approaches.

**⚠️ 2026 Eid al-Adha date is under active investigation — see bug #322.** `eid-al-adha-ae.csv`'s `2026-05-26` row appears to record Arafat Day (9 Dhu al-Hijjah), not Eid al-Adha's actual first day (10 Dhu al-Hijjah) — multiple UAE-specific sources (Khaleej Times, GulfToday, Economy Middle East) confirm the UAE Fatwa Council's actual 2026 Eid al-Adha first day is May 27, one day later than currently recorded here. This is the same Arafat-Day/Eid-Day mixup this codebase's `eid-al-adha-tr.csv` comment documents catching once before for a 2025 Turkey entry — filed as #322 rather than corrected here, since it's a code/data fix, not a docs fix.

**Deliberate omissions, not oversights.** Arafat Day (9 Dhu al-Hijjah) and Isra' Mi'raj (27 Rajab) are explicitly not modeled here — the implementation's own comment notes these should be added "if the UAE SCA declares them as settlement closure days," implying they are not currently gazetted UAE closures, unlike in some neighboring GCC states (e.g. Bahrain and Kuwait both include Arafat Day; Kuwait includes Isra and Mi'raj — see [BH.md](./BH.md), [KW.md](./KW.md)).

## Sources

- No official UAE SCA/DFM primary-source page was captured with a stable citation URL in this pass; the 2024–2026 announced dates and 2027–2055 Umm al-Qura projection methodology are documented directly in this project's own source comments (`UaeHolidays.java`), not independently re-verified against a live UAE government page here
