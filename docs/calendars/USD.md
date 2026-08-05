# USD — United States (Federal Reserve) Holidays

- **Standard:** ISO 4217 `USD`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [US](./US.md) (national), [XNYS](./XNYS.md) (NYSE) — `USD` adds Columbus Day and Veterans Day relative to `XNYS` (which excludes both), and excludes Good Friday and Day After Thanksgiving (NYSE-only market conventions) that `XNYS` includes.
- **Service class:** `HolidayCalendarServiceUSD` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.previousFridayOrFollowingMonday()`
- **Rollability exceptions:** MLK Day, Presidents' Day, Memorial Day, Labor Day, and Columbus Day are `rollable(false)` (already weekday-anchored). All other holidays, including Juneteenth (see Notes of Interest), are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Martin Luther King Jr. Day | FLOATING | — | No | Third Monday in January |
| Presidents' Day | FLOATING | — | No | Third Monday in February |
| Memorial Day | FLOATING | — | No | Last Monday in May |
| Juneteenth | FLOATING | 2021 | Yes | See Notes of Interest for how the pre-2021 cutoff is implemented |
| Independence Day | FIXED | — | Yes | July 4 |
| Labor Day | FLOATING | — | No | First Monday in September |
| Columbus Day | FLOATING | — | No | Observed by the Federal Reserve; **not** observed by NYSE |
| Veterans Day | FIXED | — | Yes | November 11; observed by the Federal Reserve; **not** observed by NYSE |
| Thanksgiving | FLOATING | — | No | Fourth Thursday in November |
| Christmas Day | FIXED | — | Yes | |

## Early Closes

Not applicable — `USD` carries no `EARLY_CLOSE` entries. NYSE's half-day closes (Day After Thanksgiving, July 3rd, Christmas Eve) are market-only conventions specific to `XNYS`, not Federal Reserve settlement closures.

## Notes of Interest

**Juneteenth's pre-2021 cutoff, implemented differently than `US`.** `USD` declares Juneteenth as a `FLOATING` holiday with an inline `Observance` lambda (`year -> year < 2021 ? null : LocalDate.of(year, Month.JUNE, 19)`) that returns `null` for years before 2021, which `HolidayCalendar.calculate()` interprets as "not observed that year." `US`, by contrast, declares Juneteenth as a plain `FIXED` holiday and relies on a different, calendar-level inception-year mechanism (see [US.md](./US.md)) to suppress it before 2021. Both achieve the same effect but via different code paths — worth knowing if debugging why Juneteenth behaves differently across the two calendars' source.

**Columbus Day / Veterans Day divide the three US-related calendars into two camps**, not a strict superset relationship: `USD` and `US` both include Columbus Day and Veterans Day; `XNYS` (NYSE) excludes both. Conversely, `XNYS` includes Good Friday, which neither `US` nor `USD` observes.

## Sources

- [Federal Reserve — Holiday Schedule (K.8)](https://www.federalreserve.gov/aboutthefed/k8.htm) — cited directly in `HolidayCalendarServiceUSD`'s own Javadoc; official primary source for the 11 Federal Reserve holidays
