# SG — Singapore National Holidays

- **Standard:** ISO 3166-1 alpha-2 `SG`
- **Category:** National
- **Sibling calendars:** [SGD](./SGD.md) (MAS/MEPS+ settlement), [XSES](./XSES.md) (Singapore Exchange) — all three share the identical 11-holiday base list via `SingaporeHolidays.baseHolidays()`; `XSES` additionally carries 2 `EARLY_CLOSE` entries that neither `SG` nor `SGD` has.
- **Service class:** `HolidayCalendarServiceSG` (`org.holiday.calendar.impl`, module `org.holiday.calendar.apac`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.followingMonday()` — fixed holidays falling on Saturday or Sunday roll forward to the following Monday
- **Rollability exceptions:** Chinese New Year (both days), Good Friday, Vesak Day, Hari Raya Puasa, Hari Raya Haji, and Deepavali are `rollable(false)` — computed via lookup table or algorithm and reported on their natural dates. New Year's Day, Labour Day, National Day, and Christmas Day are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Chinese New Year (1st Day) | FLOATING | — | No | Computed algorithmically via Time4J, no data ceiling |
| Chinese New Year (2nd Day) | FLOATING | — | No | |
| Good Friday | FLOATING | — | No | |
| Labour Day | FIXED | — | Yes | May 1 |
| Vesak Day | FLOATING | — | No | Gazetted lookup table; see Notes of Interest |
| Hari Raya Puasa | FLOATING | — | No | Eid al-Fitr; gazetted lookup table |
| National Day | FIXED | — | Yes | August 9; independence (1965) |
| Hari Raya Haji | FLOATING | — | No | Eid al-Adha; gazetted lookup table |
| Deepavali | FLOATING | — | No | Hindu Festival of Lights; gazetted lookup table |
| Christmas Day | FIXED | — | Yes | |

## Early Closes

Not applicable — the `SG` national calendar never includes early-close (half-day) sessions. SGX's Christmas Eve/New Year's Eve half-day trading closes are modeled separately under `XSES`.

## Notes of Interest

**A genuine two-tier data model within a single 11-holiday list.** Chinese New Year is computed algorithmically via Time4J with no upper bound, while Vesak Day, Hari Raya Puasa, Hari Raya Haji, and Deepavali are populated from gazetted lookup tables valid through 2055 (`dataValidThrough()` returns 2055) — a consumer requesting `SG` holidays for a far-future year gets a mix of algorithmically-certain and lookup-table-limited results within the same `calculate()` call, without any per-holiday indication of which is which from the API alone (this doc is the place that distinction is recorded).

**Singapore's Employment Act has its own "fell on a non-working day" substitution rule** (per external corroboration): if a public holiday falls on a non-working day such as Sunday, the following working day automatically becomes a paid public holiday. This project models the roll via a generic `followingMonday()` for the 4 fixed holidays here — worth being aware this is a simplification of the statutory language ("the following working day," not strictly "the following Monday"), though the two produce the same result for a holiday falling on a plain Sunday.

## Sources

- [Singapore Ministry of Manpower — public holidays](https://www.mom.gov.sg/) — the official source for Singapore's 11 gazetted public holidays and the Employment Act's non-working-day substitution rule; the specific list was corroborated via secondary aggregators referencing MOM (no single stable MOM URL enumerating the historical list was captured in this pass)
