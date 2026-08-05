# SAR — Saudi Arabia (Tadawul/SAMA) Holidays

- **Standard:** ISO 4217 `SAR`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [SA](./SA.md) (national) — `SAR` shares the identical 10-holiday list with `SA`, differing only in rollability (fully non-rollable) and roll strategy.
- **Service class:** `HolidayCalendarServiceSAR` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (GCC market convention)
- **Roll strategy:** `DateRolls.noRoll()` — settlement requires both counterparties to be available on the same calendar date; no roll-forward convention
- **Rollability exceptions:** all 10 holidays are `rollable(false)`, consistent with the no-roll strategy

## Holidays

Shares the identical 10-holiday list with `SA` — see [SA.md](./SA.md) for the full table and sourcing notes.

## Early Closes

Not applicable — `SAR` carries no `EARLY_CLOSE` entries.

## Notes of Interest

Covers settlement closure days for the Saudi Central Bank (SAMA) and the Saudi Exchange (Tadawul/TASI). Like `AE`/`AED`, this is a "mirror the national calendar exactly, just change rollability" pair — unlike Egypt's or Qatar's settlement calendars, which each add or remove specific holidays relative to their national counterpart (see [EGP.md](./EGP.md), [QAR.md](./QAR.md)).

## Sources

- See [SA.md](./SA.md) — `SAR` inherits the identical underlying facts and sourcing gaps
