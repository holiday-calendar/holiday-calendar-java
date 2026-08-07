# XLON — London Stock Exchange (LSE) Holidays

- **Standard:** ISO 10383 MIC `XLON`
- **Category:** Market/Exchange
- **Sibling calendars:** [UK](./UK.md) (national), [GBP](./GBP.md) (CHAPS) — `XLON` shares all 12 base holidays (including all four historical Jubilee entries) with `UK` verbatim via the `UkHolidays` factory, and additionally carries two early closes that neither sibling has.
- **Service class:** `HolidayCalendarServiceXLON` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `UKDateRolls.fixedHolidayRoll(newYearsDay, christmasDay, boxingDay)` — identical to `UK` (see [UK.md](./UK.md) for the exact substitution table)
- **Rollability exceptions:** identical to `UK` — Good Friday, Easter Monday, Early May/Spring/Summer Bank Holidays, and the four Jubilee entries are `rollable(false)`; New Year's Day, Christmas Day, and Boxing Day are `rollable(true)`.

## Holidays

Identical to [UK.md](./UK.md)'s 12 holidays (New Year's Day through Boxing Day, including all four Jubilee `SPECIAL_ANNIVERSARY` entries) — see that document for the full table. LSE is closed on every one of them.

## Early Closes

| Name | Close Time | Time Zone | Notes |
|------|-----------|-----------|-------|
| Christmas Eve | 12:30 | Europe/London | Shifts to the preceding Friday when December 24 falls on a Saturday or Sunday |
| New Year's Eve | 12:30 | Europe/London | Shifts to the preceding Friday when December 31 falls on a Saturday or Sunday |

## Notes of Interest

LSE's two early closes are the only point of difference from `UK` — both shift to the preceding Friday rather than being suppressed entirely when they'd fall on a weekend, unlike some other exchanges in this codebase (e.g. Xetra's or Euronext Paris's Christmas Eve/New Year's Eve, which are omitted outright on a weekend rather than shifted — see [XETR.md](./XETR.md), [XPAR.md](./XPAR.md)).

## Sources

- No official LSE primary-source page for the 12:30 Christmas Eve/New Year's Eve early-close time was located and independently verified in this pass; the time and shift-to-Friday behavior match this project's existing implementation comments

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
