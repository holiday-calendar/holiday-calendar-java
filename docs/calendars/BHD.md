# BHD — Bahrain (Boursa Bahrain/CBB) Holidays

- **Standard:** ISO 4217 `BHD`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [BH](./BH.md) (national) — `BHD` shares the identical 15-holiday list with `BH`, differing only in rollability (fully non-rollable) and roll strategy.
- **Service class:** `HolidayCalendarServiceBHD` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (GCC market convention)
- **Roll strategy:** `DateRolls.noRoll()` — settlement requires both counterparties to be available on the same calendar date; no roll-forward convention
- **Rollability exceptions:** all 15 holidays are `rollable(false)`, consistent with the no-roll strategy

## Holidays

Shares the identical 15-holiday list with `BH` — see [BH.md](./BH.md) for the full table, including the two-day Ashura observance and the National/Accession Day "observed date" notes.

## Early Closes

Not applicable — `BHD` carries no `EARLY_CLOSE` entries.

## Notes of Interest

Covers market closure days for Boursa Bahrain and the Central Bank of Bahrain (CBB). Like `AE`/`AED`, `SA`/`SAR`, and `KW`/`KWD`, this pair mirrors the national calendar exactly, with no additions or omissions on the settlement side — including Ashura, which CBB circulars confirm as a genuine two-day Boursa Bahrain closure.

## Sources

- See [BH.md](./BH.md) — `BHD` inherits the identical underlying facts and sourcing gaps

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
