# AED — UAE (CBUAE/DFM/ADX) Holidays

- **Standard:** ISO 4217 `AED`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [AE](./AE.md) (national) — `AED` shares the identical 10-holiday list with `AE`, differing only in rollability (`AED` is fully non-rollable) and roll strategy.
- **Service class:** `HolidayCalendarServiceAED` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (GCC market convention)
- **Roll strategy:** `DateRolls.noRoll()` — settlement requires both counterparties to be available on the same calendar date; no roll-forward convention
- **Rollability exceptions:** all 10 holidays are `rollable(false)`, consistent with the no-roll strategy

## Holidays

Shares the identical 10-holiday list with `AE` — see [AE.md](./AE.md) for the full table, data-ceiling notes, and the Arafat Day / Isra' Mi'raj omission rationale.

## Early Closes

Not applicable — `AED` carries no `EARLY_CLOSE` entries.

## Notes of Interest

`AED` covers settlement closure days for the Central Bank of the UAE (CBUAE), Dubai Financial Market (DFM), and Abu Dhabi Securities Exchange (ADX) — three institutions represented by a single calendar, since the settlement holiday schedule mirrors the national calendar exactly. Unlike some other MENA country pairs in this codebase (e.g. Egypt's `EG`/`EGP`, which differ by one holiday — see [EGP.md](./EGP.md)), `AE` and `AED` carry the exact same holiday set with no additions or omissions on either side.

## Sources

- See [AE.md](./AE.md) — `AED` inherits the identical underlying facts and sourcing gaps

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
