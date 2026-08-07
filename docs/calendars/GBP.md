# GBP — United Kingdom (CHAPS) Holidays

- **Standard:** ISO 4217 `GBP`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [UK](./UK.md) (national), [XLON](./XLON.md) (London Stock Exchange) — `GBP` omits the four historical Jubilee `SPECIAL_ANNIVERSARY` entries that `UK`/`XLON` carry, and instead has its own one-off entries for the 2022 State Funeral and 2023 Coronation, which neither sibling carries.
- **Service class:** `HolidayCalendarServiceGBP` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `UKDateRolls.fixedHolidayRoll(newYearsDay, christmasDay, boxingDay)` — the same custom roll strategy `UK` and `XLON` use (see [UK.md](./UK.md) for the exact substitution table)
- **Rollability exceptions:** Good Friday, Easter Monday, Early May/Spring/Summer Bank Holidays, and both one-off `SPECIAL_ANNIVERSARY` entries (State Funeral, Coronation) are `rollable(false)`. New Year's Day, Christmas Day, and Boxing Day are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | Custom roll |
| Good Friday | FLOATING | — | No | |
| Easter Monday | FLOATING | — | No | |
| Early May Bank Holiday | FLOATING | — | No | |
| Spring Bank Holiday | FLOATING | — | No | Note: `GBP` does **not** carry a Jubilee-year Spring-Bank-Holiday-shift entry point the same way `UK` does — see Notes of Interest |
| Summer Bank Holiday | FLOATING | — | No | |
| Christmas Day | FIXED | — | Yes | Custom roll |
| Boxing Day | FIXED | — | Yes | Custom roll |
| State Funeral of Queen Elizabeth II | SPECIAL_ANNIVERSARY | 2022 | No | Monday, September 19, 2022 |
| Coronation Bank Holiday | SPECIAL_ANNIVERSARY | 2023 | No | Monday, May 8, 2023 |

## Early Closes

Not applicable — `GBP` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**`GBP` does not model the historical Jubilee bank holidays at all** (Silver 1977, Golden 2002, Diamond 2012, Platinum 2022) — those exist only on `UK`/`XLON`. Since `GBP`'s `SpringBankHoliday` observance is the same class used by `UK`/`XLON` (`org.holiday.calendar.observance.uk.SpringBankHoliday`), `GBP`'s Spring Bank Holiday date is *still* shifted in those same Jubilee years (its hardcoded date map is not conditioned on which calendar is asking) — but `GBP` never adds the corresponding one-off "extra day" entry the way `UK`/`XLON` do for the Silver/Golden/Diamond/Platinum Jubilees. In other words: on `GBP`, in a year like 2022, the Spring Bank Holiday moves to Thursday June 2 exactly as it does on `UK`, but Friday June 3 (Platinum Jubilee's extra day) is **not** a `GBP` holiday. Anyone relying on `GBP` to mirror `UK`'s historical Jubilee calendar should be aware of this asymmetry.

**Two different one-off `SPECIAL_ANNIVERSARY` mechanisms exist across the UK-related calendars** — `UK`/`XLON` for the Jubilees, `GBP` for the Funeral/Coronation — with no shared factory between them, each independently declared in its own service class.

## Sources

- [Bank of England — CHAPS Settlement Calendar](https://www.bankofengland.co.uk/payment-and-settlement/chaps/chaps-settlement-calendar) — cited directly in `HolidayCalendarServiceGBP`'s own Javadoc; official primary source
- [Labour Relations Agency — Bank holiday confirmed for Queen Elizabeth's funeral](https://www.lra.org.uk/bank-holiday-confirmed-queen-elizabeths-funeral-guidance-employers-and-employees) — confirms Monday September 19, 2022
- [Highland Council — King's Coronation bank holiday – 8 May 2023](https://www.highland.gov.uk/news/article/15144/king-s-coronation-bank-holiday-8-may-2023) — confirms Monday May 8, 2023

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
