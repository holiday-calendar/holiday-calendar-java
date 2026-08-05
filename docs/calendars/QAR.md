# QAR — Qatar (QSE/QCB) Holidays

- **Standard:** ISO 4217 `QAR`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [QA](./QA.md) (national) — `QAR` adds one holiday (Qatar Banks Holiday) that `QA` doesn't carry, on top of the same 9 base holidays, for a total of 10.
- **Service class:** `HolidayCalendarServiceQAR` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (GCC market convention)
- **Roll strategy:** `DateRolls.noRoll()` — settlement requires both counterparties to be available on the same calendar date; no roll-forward convention. Notably, `QAR` does **not** inherit `QA`'s distinctive Thursday/Sunday asymmetric roll — it uses the same plain no-roll convention as every other MENA settlement calendar in this codebase.
- **Rollability exceptions:** all 10 holidays are `rollable(false)`, consistent with the no-roll strategy.

## Holidays

Shares `QA`'s 9 base holidays (see [QA.md](./QA.md) for the full table) plus:

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| Qatar Banks Holiday | FLOATING | — | No | First Sunday of March, per Cabinet Decision No. (33) of 2009 — applies to all QCB-supervised financial institutions |

Islamic New Year and Prophet's Birthday are **not** gazetted public holidays in Qatar and are excluded here too (same as `QA`).

## Early Closes

Not applicable — `QAR` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**`QAR` is a genuine superset of `QA`, not just a rollability variant** — unlike most other national/settlement pairs in this codebase, which carry the identical holiday list and differ only in roll behavior (e.g. `AE`/`AED`, `SA`/`SAR`). The Qatar Banks Holiday is a QCB-specific closure with no national-calendar counterpart, added specifically for financial-institution settlement purposes under a distinct Cabinet Decision (No. 33 of 2009) separate from the general public-holiday legislation.

**`QA`'s unusual Thursday/Sunday roll doesn't carry over to `QAR`.** Since `QAR` uses `noRoll()` like every other MENA settlement calendar, the asymmetric roll direction that makes `QA` distinctive (see [QA.md](./QA.md)) is specific to the national calendar and doesn't apply here at all.

## Sources

- Qatar Banks Holiday's basis (Cabinet Decision No. 33 of 2009, first Sunday of March) is documented directly in this project's own source comments (`QatarHolidays.java`); no official QCB primary-source page was independently captured with a stable citation URL in this pass
- See [QA.md](./QA.md) for sourcing on the shared base holidays
