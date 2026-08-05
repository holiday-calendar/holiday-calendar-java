# KWD — Kuwait (Boursa Kuwait/CBK) Holidays

- **Standard:** ISO 4217 `KWD`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [KW](./KW.md) (national) — `KWD` shares the identical 13-holiday list with `KW`, differing only in rollability (fully non-rollable) and roll strategy.
- **Service class:** `HolidayCalendarServiceKWD` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (GCC market convention)
- **Roll strategy:** `DateRolls.noRoll()` — settlement requires both counterparties to be available on the same calendar date; no roll-forward convention
- **Rollability exceptions:** all 13 holidays are `rollable(false)`, consistent with the no-roll strategy

## Holidays

Shares the identical 13-holiday list with `KW` — see [KW.md](./KW.md) for the full table, including the Isra and Mi'raj observance this codebase omits for most other MENA calendars.

## Early Closes

Not applicable — `KWD` carries no `EARLY_CLOSE` entries.

## Notes of Interest

Covers market closure days for Boursa Kuwait and the Central Bank of Kuwait (CBK). Like `AE`/`AED` and `SA`/`SAR`, this is a "mirror the national calendar exactly, just change rollability" pair, with no additions or omissions on the settlement side.

## Sources

- See [KW.md](./KW.md) — `KWD` inherits the identical underlying facts and sourcing gaps
