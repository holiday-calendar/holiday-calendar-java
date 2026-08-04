# TRY — Turkey (BIST/TCMB) Holidays

**Standard:** ISO 4217 `TRY`
**Category:** Central Bank/Settlement
**Sibling calendars:** [TR](./TR.md) (national) — `TRY` shares the identical 14-holiday base list and the same Republic Day Eve early close with `TR`, differing only in rollability (fully non-rollable) and roll strategy.
**Service class:** `HolidayCalendarServiceTRY` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday — same as `TR`, not the GCC Friday+Saturday convention
- **Roll strategy:** `DateRolls.noRoll()` — settlement requires both counterparties to be available on the same calendar date; no roll-forward convention
- **Rollability exceptions:** all 14 base holidays are `rollable(false)`, consistent with the no-roll strategy. The Republic Day Eve early close is `rollable(false)` by construction.

## Holidays

Shares the identical 14-holiday base list with `TR` — see [TR.md](./TR.md) for the full table and the Diyanet-sourcing notes.

## Early Closes

| Name | Close Time | Time Zone | Notes |
|------|-----------|-----------|-------|
| Republic Day Eve | 13:00 | Europe/Istanbul | Same statutory closure as `TR`; does not shift on a weekend |

## Notes of Interest

Covers settlement closure days for Borsa Istanbul (BIST) and the Central Bank of the Republic of Turkey (TCMB). Republic Day Eve is included here not because it's a market-specific convention but because Law No. 2429 makes it a nationwide statutory closure applying to all public institutions — the same reasoning that puts it on `TR` as well, unlike this codebase's more typical pattern where early closes are market-only additions absent from the national calendar (compare with NYSE's or LSE's early closes, entirely absent from `US`/`UK`).

## Sources

- See [TR.md](./TR.md) — `TRY` inherits the identical underlying facts and sourcing gaps
