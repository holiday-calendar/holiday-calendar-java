# CN — China National Holidays

- **Standard:** ISO 3166-1 alpha-2 `CN`
- **Category:** National
- **Sibling calendars:** [CNY](./CNY.md) (PBOC/CNAPS) — `CN` models the statutory *minimum* windows from China's State Council Ordinance; `CNY` models the *full* operational closure windows actually observed in practice (longer for Spring Festival, National Day, and Labour Day). See Notes of Interest for the exact day-count differences.
- **Service class:** `HolidayCalendarServiceCN` (`org.holiday.calendar.impl`, module `org.holiday.calendar.apac`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.followingMonday()` — applied only to single-day holidays (see Notes of Interest for why multi-day blocks are excluded)
- **Rollability exceptions:** the 3-day Spring Festival block and the 3-day National Day block are `rollable(false)` in their entirety. New Year's Day, Qingming Festival, Labour Day, Dragon Boat Festival, and Mid-Autumn Festival are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Spring Festival (Day 1) | FLOATING | — | No | First day of the Chinese lunisolar new year |
| Spring Festival (Day 2) | FLOATING | — | No | |
| Spring Festival (Day 3) | FLOATING | — | No | 3-day statutory minimum window (vs. 7 days on `CNY`) |
| Qingming Festival | FLOATING | — | Yes | Tomb Sweeping Day; solar term at ecliptic longitude 15° (~April 4–5) |
| Labour Day | FIXED | — | Yes | 1-day statutory minimum (vs. 3 days on `CNY`) |
| Dragon Boat Festival | FLOATING | — | Yes | Duanwu; 5th day of the 5th lunar month |
| Mid-Autumn Festival | FLOATING | — | Yes | 15th day of the 8th lunar month |
| National Day (Day 1) | FIXED | — | No | October 1; founding of the PRC (1949) |
| National Day (Day 2) | FIXED | — | No | |
| National Day (Day 3) | FIXED | — | No | 3-day statutory minimum window (vs. 7 days on `CNY`) |

## Early Closes

Not applicable — `CN` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**China's weekend-adjustment mechanism (调休, *tiaoxiu*) is fundamentally incompatible with this library's per-holiday `DateRoll` model, and the implementation says so directly.** Real-world *tiaoxiu* is block-based: when Spring Festival or National Day falls near a weekend, the State Council shifts the *entire block* into a contiguous window and separately designates specific nearby Saturdays or Sundays as compensatory working days (补班) — a mechanism this library's `HolidayCalendar` abstraction (which tracks closures, not "this weekend day is actually a workday") cannot represent. Per the implementation's own reasoning: applying `followingMonday()` independently to each day of a 3-day block would produce "definitively wrong results (e.g., all three Spring Festival days collapsing to the same date when Day 1 falls on a Saturday)." The chosen compromise — multi-day blocks reported on their natural calendar positions, `rollable(false)` — is a deliberate accuracy tradeoff, not an oversight. For year-specific tiaoxiu adjustments, this project's own `CNY` calendar exposes curated compensatory-working-day data (see [CNY.md](./CNY.md)); `CN` does not.

**`CN` vs `CNY` day-count differences are the main practical distinction between these two calendars**, not different holidays: both cover the same 7 statutory occasions, but `CN` models the Ordinance's statutory minimum (3-day Spring Festival, 1-day Labour Day, 3-day National Day) while `CNY` models the fuller windows actually observed via the State Council's annual adjustment notices (7-day Spring Festival, 3-day Labour Day, 7-day National Day). The 5-day extended Labour Day window observed in practice since 2019 comes from that annual notice process, not from the underlying Ordinance itself — worth knowing if a consumer expects `CN`'s Labour Day count to track real-world practice.

**No data ceiling.** All holidays are computed algorithmically via Time4J Chinese calendar mathematics; `dataValidThrough()` returns empty, and `HolidayCalendar.calculate(int)` is authoritative for any year.

## Sources

- [China Britain Business Council — What is China's 'compensatory working day' system?](https://focus.cbbc.org/what-is-chinas-compensatory-working-day-system/) — corroborates the *tiaoxiu* block-shift mechanism and its incompatibility with a simple per-day roll rule
- The statutory basis (State Council Ordinance on Public Holidays for National Festivals and Memorial Days, as amended 2007/2013) and the exact day-count distinctions from `CNY` are documented directly in this project's own source comments (`HolidayCalendarServiceCN.java`); no official gov.cn primary-source page in English was independently captured with a stable citation URL in this pass
