# JOD — Jordan (ASE/CBJ) Holidays

- **Standard:** ISO 4217 `JOD`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [JO](./JO.md) (national) — `JOD` shares the identical 15-holiday list with `JO`, differing only in rollability (fully non-rollable) and roll strategy.
- **Service class:** `HolidayCalendarServiceJOD` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday
- **Roll strategy:** `DateRolls.noRoll()` — settlement requires both counterparties to be available on the same calendar date; no roll-forward convention
- **Rollability exceptions:** all 15 holidays are `rollable(false)`, consistent with the no-roll strategy

## Holidays

Shares the identical 15-holiday list with `JO` — see [JO.md](./JO.md) for the full table, including the 4-day Eid observances and Christmas Day's status as a genuine gazetted national holiday.

## Early Closes

Not applicable — `JOD` carries no `EARLY_CLOSE` entries.

## Notes of Interest

Covers market closure days for the Amman Stock Exchange (ASE) and Central Bank of Jordan (CBJ). Like `AE`/`AED`, `SA`/`SAR`, `KW`/`KWD`, `BH`/`BHD`, and `MA`/`MAD`, this pair mirrors the national calendar exactly with no additions or omissions — including Christmas Day, confirmed by official ASE schedules as a genuine settlement closure day, not just a national holiday the exchange happens to also observe informally.

## Sources

- See [JO.md](./JO.md) — `JOD` inherits the identical underlying facts and sourcing gaps
