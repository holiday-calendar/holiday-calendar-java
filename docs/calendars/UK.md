# UK — United Kingdom National Holidays

- **Standard:** ISO 3166-1 alpha-2 `UK`
- **Category:** National
- **Sibling calendars:** [GBP](./GBP.md) (CHAPS settlement), [XLON](./XLON.md) (London Stock Exchange) — `UK` and `XLON` share all 12 holidays via the `UkHolidays` factory; `XLON` additionally has two early closes. `GBP` independently duplicates most of the same holidays but omits the four Jubilee `SPECIAL_ANNIVERSARY` entries and instead has its own one-off entries for the 2022 State Funeral and 2023 Coronation.
- **Service class:** `HolidayCalendarServiceUK` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `UKDateRolls.fixedHolidayRoll(newYearsDay, christmasDay, boxingDay)` — a custom strategy specific to these three named holidays (see Notes of Interest for the exact substitution table). All other holidays are floating and weekday-anchored, so rolling doesn't apply to them.
- **Rollability exceptions:** Good Friday, Easter Monday, Early May/Spring/Summer Bank Holidays are `rollable(false)` (weekday-anchored). The four Jubilee `SPECIAL_ANNIVERSARY` entries are also `rollable(false)` — as one-off anniversary dates, not recurring annual holidays. New Year's Day, Christmas Day, and Boxing Day are `rollable(true)`, using the custom roll above rather than a generic weekend-substitution rule.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | Custom roll — see Notes of Interest |
| Good Friday | FLOATING | — | No | |
| Easter Monday | FLOATING | — | No | |
| Early May Bank Holiday | FLOATING | — | No | |
| Spring Bank Holiday | FLOATING | — | No | Shifted in Jubilee years to adjoin the Jubilee holiday — see Notes of Interest |
| Silver Jubilee Bank Holiday | SPECIAL_ANNIVERSARY | 1977 | No | Tuesday, June 7, 1977 |
| Golden Jubilee Bank Holiday | SPECIAL_ANNIVERSARY | 2002 | No | Monday, June 3, 2002 |
| Diamond Jubilee Bank Holiday | SPECIAL_ANNIVERSARY | 2012 | No | Tuesday, June 5, 2012 |
| Platinum Jubilee Bank Holiday | SPECIAL_ANNIVERSARY | 2022 | No | Friday, June 3, 2022 — see Notes of Interest, this was a 2-day extension, not 1 |
| Summer Bank Holiday | FLOATING | — | No | Last Monday in August |
| Christmas Day | FIXED | — | Yes | Custom roll |
| Boxing Day | FIXED | — | Yes | Custom roll |

## Early Closes

Not applicable — the `UK` national calendar never includes early-close (half-day) sessions. LSE's Christmas Eve/New Year's Eve early closes are market-only, modeled separately under `XLON`.

## Notes of Interest

**Custom fixed-holiday roll table (`UKDateRolls.fixedHolidayRoll`):**

| Holiday | Falls on Saturday | Falls on Sunday |
|---|---|---|
| Christmas Day / Boxing Day | +2 days (following Monday/Tuesday) | +2 days (following Monday/Tuesday) |
| New Year's Day | +2 days (following Monday) | +1 day (following Monday) |

This one roll strategy identifies which of the three specific holidays a given date belongs to (by comparing against each holiday's own computed date for that year) rather than applying a single generic rule to all fixed holidays — a future rollable holiday added to this calendar would need this roll strategy updated to recognize it, per the implementation's own warning comment.

**Jubilee Bank Holiday mechanism — two cooperating pieces, not one.** Each Jubilee year combines a one-off `SPECIAL_ANNIVERSARY` entry (the "extra" day) with the regular `Spring Bank Holiday`'s own `Observance` (`SpringBankHoliday.java`), which contains a hardcoded map of Jubilee-year dates that *moves* the regular Spring Bank Holiday itself to adjoin the extra day:

- **1977 (Silver):** Spring Bank Holiday stays Monday June 6 (unmoved); extra day Tuesday June 7 → 4-day weekend
- **2002 (Golden):** Spring Bank Holiday moves to Tuesday June 4; extra day Monday June 3 → 5-day weekend (per contemporaneous reporting)
- **2012 (Diamond):** Spring Bank Holiday moves to Monday June 4; extra day Tuesday June 5 → 4-day weekend
- **2022 (Platinum):** Spring Bank Holiday moves to Thursday June 2; extra day Friday June 3 → 4-day weekend (a genuine 2-day extension, unlike the single extra day in the other three years)

This project's `HolidayCalendar` model captures both halves correctly — verified against external reporting for all four years — but a reader looking only at the Holidays table above (which lists just the `SPECIAL_ANNIVERSARY` entry) would miss that the "ordinary" Spring Bank Holiday date itself also moves in those specific years; the actual mechanism lives in `SpringBankHoliday.java`'s `Observance` implementation, not in a `Holiday` entry.

## Sources

- [Bank Holidays for Queen Elizabeth's Passing (leavedates.com)](https://www.leavedates.com/articles/queen-elizabeth-death-public-holiday-2022) and [Highland Council — King's Coronation bank holiday](https://www.highland.gov.uk/news/article/15144/king-s-coronation-bank-holiday-8-may-2023) — corroborate the 2022/2023 one-off dates (modeled on `GBP`, see [GBP.md](./GBP.md))
- [UK Parliament Commons Library — How are royal jubilees celebrated in the UK?](https://commonslibrary.parliament.uk/how-are-royal-jubilees-celebrated-in-the-uk/) — corroborates all four Jubilee bank holiday dates and the Spring Bank Holiday move pattern described above
- No single official gov.uk page listing all four historical Jubilee bank holiday dates together was located as of 2026-08-04; individual dates are well-corroborated across the Commons Library and contemporaneous news sources cited above

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
