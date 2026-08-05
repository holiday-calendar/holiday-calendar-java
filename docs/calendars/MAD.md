# MAD — Morocco (CSE/BAM) Holidays

- **Standard:** ISO 4217 `MAD`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [MA](./MA.md) (national) — `MAD` shares the identical 17-holiday list with `MA`, differing only in rollability (fully non-rollable) and roll strategy.
- **Service class:** `HolidayCalendarServiceMAD` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday — same as `MA`, not the GCC Friday+Saturday convention
- **Roll strategy:** `DateRolls.noRoll()` — the Casablanca Stock Exchange (CSE) observes holidays on their natural calendar dates, per official CSE circular AV-2025-078; settlement requires both counterparties to be available on the same calendar date
- **Rollability exceptions:** all 17 holidays are `rollable(false)`, consistent with the no-roll strategy

## Holidays

Shares the identical 17-holiday list with `MA` — see [MA.md](./MA.md) for the full table, including the Amazigh New Year's 2024 effective date and its lack of built-in inception-year filtering.

## Early Closes

Not applicable — `MAD` carries no `EARLY_CLOSE` entries.

## Notes of Interest

Covers market closure days for the Bourse de Casablanca (CSE) and Bank Al-Maghrib (BAM). Per this project's own documentation, "the CSE observes the same public holidays as the national calendar (`MA`); there are no exchange-specific exclusions or additions" — a genuine 1:1 mirror, unlike Egypt's or Qatar's settlement calendars, which each differ from their national counterpart by one holiday (see [EGP.md](./EGP.md), [QAR.md](./QAR.md)).

## Sources

- See [MA.md](./MA.md) — `MAD` inherits the identical underlying facts and sourcing gaps

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
