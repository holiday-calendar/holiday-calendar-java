# CNY — China (PBOC) Holidays

**Standard:** ISO 4217 `CNY`
**Category:** Central Bank/Settlement
**Sibling calendars:** [CN](./CN.md) (national) — `CNY` models the full operational closure windows (7-day Spring Festival, 3-day Labour Day, 7-day National Day) actually observed by the PBOC/CNAPS system, versus `CN`'s statutory-minimum windows.
**Service class:** `HolidayCalendarServiceCNY` (`org.holiday.calendar.impl`, module `org.holiday.calendar.apac`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.noRoll()` — all dates reported on their natural calendar positions; see Notes of Interest for why this differs from `CN`'s approach
- **Rollability exceptions:** all 21 holidays are `rollable(false)`, consistent with the no-roll strategy

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | No | |
| Spring Festival (Day 1) | FLOATING | — | No | Full 7-day statutory window (vs. 3 days on `CN`) |
| Spring Festival (Day 2) | FLOATING | — | No | |
| Spring Festival (Day 3) | FLOATING | — | No | |
| Spring Festival (Day 4) | FLOATING | — | No | |
| Spring Festival (Day 5) | FLOATING | — | No | |
| Spring Festival (Day 6) | FLOATING | — | No | |
| Spring Festival (Day 7) | FLOATING | — | No | |
| Qingming Festival | FLOATING | — | No | Tomb Sweeping Day |
| Labour Day (Day 1) | FIXED | — | No | Full 3-day window (vs. 1 day on `CN`) |
| Labour Day (Day 2) | FIXED | — | No | |
| Labour Day (Day 3) | FIXED | — | No | |
| Dragon Boat Festival | FLOATING | — | No | |
| Mid-Autumn Festival | FLOATING | — | No | |
| National Day (Day 1) | FIXED | — | No | Full 7-day "Golden Week" (vs. 3 days on `CN`); founding of the PRC (1949) |
| National Day (Day 2) | FIXED | — | No | |
| National Day (Day 3) | FIXED | — | No | |
| National Day (Day 4) | FIXED | — | No | |
| National Day (Day 5) | FIXED | — | No | |
| National Day (Day 6) | FIXED | — | No | |
| National Day (Day 7) | FIXED | — | No | |

## Early Closes

Not applicable — `CNY` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**`CNY` exposes a genuinely different API surface than every other calendar in this codebase: `getCompensatoryWorkingDays(int year)`.** Since `HolidayCalendar` tracks closures, not "this Saturday is actually a workday," this project models China's *tiaoxiu* compensatory working days as a *separate*, manually-curated method rather than trying to force them into the `Holiday`/`DateRoll` abstraction. This data has its own independent ceiling, exposed via `compensatoryDataValidThrough()` — a genuinely distinct concept from `dataValidThrough()`, which governs the holiday dates themselves (empty here, since those are computed algorithmically via Time4J with no ceiling). If a consumer requests compensatory-day data for a year beyond that ceiling, the method logs a warning and returns an empty list rather than throwing — callers must not interpret an empty result as "no make-up days that year" versus "data not yet published."

**The compensatory data has a documented, explicit annual maintenance process**, unusual in its specificity compared to most other calendars in this codebase: monitor gov.cn in November–December for the following year's notice, append rows to `cny-compensatory-working-days.csv`, then re-run the APAC module's test suite. This is a recurring, by-design maintenance burden (not a one-time setup), since China's State Council publishes this schedule fresh every year with no long-range projection equivalent to the Umm al-Qura tables used elsewhere in this codebase's MENA module.

**No roll strategy at all, unlike `CN`.** Where `CN` applies `followingMonday()` to its single-day holidays, `CNY` uses a flat `noRoll()` for everything — since `CNY` already models the full real-world closure windows (including the days `CN`'s statutory-minimum model would otherwise need a roll rule to approximate), there's no remaining gap for a roll rule to fill.

## Sources

- The `getCompensatoryWorkingDays`/`compensatoryDataValidThrough` API design, the annual maintenance process, and the exact day-count windows are documented directly in this project's own source comments (`HolidayCalendarServiceCNY.java`); no official gov.cn primary-source page in English was independently captured with a stable citation URL in this pass
- See [CN.md](./CN.md) for corroboration of the general *tiaoxiu* mechanism from a third-party source
