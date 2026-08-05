# EGP — Egypt (EGX/CBE) Holidays

- **Standard:** ISO 4217 `EGP`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [EG](./EG.md) (national) — `EGP` omits Arafat Day, for 16 holidays vs. `EG`'s 17.
- **Service class:** `HolidayCalendarServiceEGP` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (GCC market convention)
- **Roll strategy:** `DateRolls.noRoll()` — settlement requires both counterparties to be available on the same calendar date; no roll-forward convention
- **Rollability exceptions:** all 16 holidays are `rollable(false)`, consistent with the no-roll strategy

## Holidays

Shares `EG`'s holidays minus Arafat Day (16 of 17) — see [EG.md](./EG.md) for the full table and Sham El-Nessim's algorithmic-computation notes.

## Early Closes

Not applicable — `EGP` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**Arafat Day is a confirmed, deliberate exclusion — a national public holiday that isn't a settlement closure day.** Per this project's own implementation comment: "Arafat Day is excluded from this calendar: it is a national public holiday but is not an EGX / CBE settlement closure day." This is the same "9 Dhu al-Hijjah is a national holiday somewhere but not a settlement holiday" pattern seen with a few other pairs in this codebase, but here it's explicitly one holiday fewer on the settlement side rather than a rollability-only difference.

## Sources

- The Arafat Day exclusion rationale is documented directly in this project's own source comments (`EgyptHolidays.java`, `HolidayCalendarServiceEGP.java`); no official CBE/EGX primary-source page was independently captured with a stable citation URL in this pass
- See [EG.md](./EG.md) for sourcing on the shared holidays
