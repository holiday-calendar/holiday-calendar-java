# QA — Qatar (National) Holidays

- **Standard:** ISO 3166-1 alpha-2 `QA`
- **Category:** National
- **Sibling calendars:** [QAR](./QAR.md) (QSE/QCB settlement) — `QAR` adds one holiday (Qatar Banks Holiday) that `QA` doesn't carry, on top of the same 9 base holidays.
- **Service class:** `HolidayCalendarServiceQA` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (GCC market convention)
- **Roll strategy:** `DateRolls.previousThursdayOrFollowingSunday()` — **asymmetric and distinct from every other GCC calendar in this codebase.** A holiday falling on Friday rolls *backward* to the preceding Thursday; a holiday falling on Saturday rolls *forward* to the following Sunday. Confirmed (per the implementation's own Javadoc) against Amiri Diwan precedents for National Day 2020 (fell on a Friday) and 2021 (fell on a Saturday).
- **Rollability exceptions:** Qatar National Sports Day and all Islamic holidays are `rollable(false)` (weekday-anchored or Hijri-calendar). New Year's Day and National Day are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Qatar National Sports Day | FLOATING | — | No | Second Tuesday of February, per Emiri Decree No. 80 of 2011 |
| Eid al-Fitr | FLOATING | — | No | 1 Shawwal AH |
| Eid al-Fitr (2nd Day) | FLOATING | — | No | 2 Shawwal AH |
| Eid al-Fitr (3rd Day) | FLOATING | — | No | 3 Shawwal AH |
| Eid al-Adha | FLOATING | — | No | 10 Dhu al-Hijjah AH |
| Eid al-Adha (2nd Day) | FLOATING | — | No | 11 Dhu al-Hijjah AH |
| Eid al-Adha (3rd Day) | FLOATING | — | No | 12 Dhu al-Hijjah AH |
| National Day | FIXED | — | Yes | December 18; independence from Britain (1971) |

Islamic New Year and Prophet's Birthday are **not** gazetted public holidays in Qatar and are deliberately excluded — unlike the UAE and Saudi Arabia, which observe both (see [AE.md](./AE.md), [SA.md](./SA.md)).

## Early Closes

Not applicable — `QA` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**The Friday→Thursday / Saturday→Sunday roll direction is the standout peculiarity of this entire MENA module.** Every other GCC national calendar in this codebase (`AE`, `BH`, `JO`, `KW`, `SA`) uses a uniform `followingSunday()` roll — both Friday and Saturday roll *forward* to Sunday. Qatar instead rolls Friday *backward*. The implementation's own Javadoc cites concrete precedent: Qatar National Day (December 18) fell on a Friday in 2020 and a Saturday in 2021 — external research corroborates both weekend-day occurrences actually happened in those years, though the specific *substitute* dates the Amiri Diwan observed weren't independently re-confirmed against a primary government source in this pass (only the code's own citation was available).

**Two holidays are conspicuously absent, and it's confirmed deliberate, not an oversight.** Islamic New Year and Prophet's Birthday are gazetted public holidays in several neighboring GCC states (UAE, Saudi Arabia, Bahrain, Kuwait) but not in Qatar, per this project's own documentation. A consumer expecting parity across GCC calendars should not assume Qatar mirrors its neighbors' full Islamic-holiday set.

**Only 2–3 days per Eid**, the shortest observance among GCC countries in this codebase alongside the UAE (2 days each) — contrast with Bahrain and Turkey's 3–4 day observances.

## Sources

- Qatar National Day's Friday (2020) and Saturday (2021) occurrences are corroborated by [Al Jazeera's 2020 coverage](https://www.aljazeera.com/amp/news/2020/12/18/qatar-celebrates-national-day-2020-amid-covid-pandemic) and [2021 coverage](https://www.aljazeera.com/amp/news/2021/12/18/qatar-celebrates-national-day-2021-ahead-of-fifa-arab-cup-final) — confirming the weekend-day collisions existed, though not the specific observed-substitute dates
- The `previousThursdayOrFollowingSunday()` roll rule itself and the Islamic New Year / Prophet's Birthday exclusions are documented directly in this project's own source comments (`HolidayCalendarServiceQA.java`, `QatarHolidays.java`); no official Amiri Diwan or QCB primary-source page was independently captured with a stable citation URL in this pass

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
