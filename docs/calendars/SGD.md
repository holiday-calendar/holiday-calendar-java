# SGD — Singapore (MAS/MEPS+) Holidays

**Standard:** ISO 4217 `SGD`
**Category:** Central Bank/Settlement
**Sibling calendars:** [SG](./SG.md) (national), [XSES](./XSES.md) (Singapore Exchange) — `SGD` shares the identical 11-holiday list with `SG`, differing only in rollability and roll strategy; unlike `XSES`, it carries no early closes.
**Service class:** `HolidayCalendarServiceSGD` (`org.holiday.calendar.impl`, module `org.holiday.calendar.apac`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.noRoll()` — RTGS settlement requires both counterparties to be available on the same calendar date; no roll-forward convention
- **Rollability exceptions:** all 11 holidays are `rollable(false)`, consistent with the no-roll strategy

## Holidays

Shares the identical 11-holiday list with `SG` — see [SG.md](./SG.md) for the full table and the algorithmic-vs-lookup-table data model notes.

## Early Closes

Not applicable — `SGD` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**MEPS+ (MAS Electronic Payment System Plus) is Singapore's RTGS system for SGD interbank payments**, governed by the MAS MEPS+ Service Agreement, which defines "business day" as any Monday–Friday excluding Singapore public holidays — directly corroborated against the actual official MAS Service Agreement document (not just a secondary summary).

**No BOJ-style additional bank-specific closures.** Unlike `JPY` (which adds 3 BOJ operational closures on top of Japan's national calendar — see [JPY.md](./JPY.md)), this project's own documentation confirms MAS publishes no additional closure days for MEPS+ beyond Singapore's standard public holiday schedule — `SGD` is a pure rollability/roll-strategy variant of `SG`, not a superset.

## Sources

- [MAS — MEPS+ Service Agreement](https://www.mas.gov.sg/-/media/MAS/Singapore-Financial-Centre/Why-Singapore/MEPS/Agreements/MEPSplus-Svc-Agreement_wef-8-Feb-2021_v2.pdf) — official primary source; directly confirms the "business day" definition (Monday–Friday excluding Singapore public/bank holidays) cited in this project's own implementation comment
- See [SG.md](./SG.md) for sourcing on the shared holiday list
