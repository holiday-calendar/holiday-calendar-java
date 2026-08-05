# XSES — Singapore Exchange (SGX) Holidays

- **Standard:** ISO 10383 MIC `XSES`
- **Category:** Market/Exchange
- **Sibling calendars:** [SG](./SG.md) (national), [SGD](./SGD.md) (MAS/MEPS+) — `XSES` shares the identical 11-holiday base list with both, adding 2 `EARLY_CLOSE` entries neither carries.
- **Service class:** `HolidayCalendarServiceXSES` (`org.holiday.calendar.impl`, module `org.holiday.calendar.apac`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.followingMonday()` — same as `SG`
- **Rollability exceptions:** identical to `SG` for the base holidays. Christmas Eve and New Year's Eve early closes are `rollable(false)` by construction (early closes are never rolled).

## Holidays

Shares the identical 11-holiday base list with `SG` — see [SG.md](./SG.md) for the full table.

## Early Closes

| Name | Close Time | Time Zone | Notes |
|------|-----------|-----------|-------|
| Christmas Eve | 12:00 | Asia/Singapore | Half-day trading, 09:00–12:00 SGT; occurs whenever December 24 falls Monday through Friday |
| New Year's Eve | 12:00 | Asia/Singapore | Half-day trading, 09:00–12:00 SGT; occurs whenever December 31 falls Monday through Friday |

## Notes of Interest

**`XSES` and `SGD` deliberately diverge, and the implementation is explicit about why: SGX equities trading and MEPS+ RTGS settlement are distinct systems.** Per this project's own documentation: "The MAS/MEPS+ settlement calendar (`HolidayCalendarServiceSGD`) does not include these [early closes] — MEPS+ RTGS settlement and SGX equities trading are distinct systems." This is a useful general pattern to recognize across this codebase: a country's exchange and its RTGS settlement system are not always run by the same institution on the same schedule, even when (as with MAS in Singapore) a single regulator oversees both.

**Both early closes are suppressed entirely (not shifted) when they'd fall on a weekend** — occurring "whenever December 24/31 falls Monday through Friday," per the implementation's own description — the same suppression pattern used by Xetra's, Euronext Paris's, and ASX's equivalent early closes elsewhere in this codebase (see [XETR.md](./XETR.md), [XPAR.md](./XPAR.md), [XASX.md](./XASX.md)), rather than LSE's shift-to-preceding-Friday approach (see [XLON.md](./XLON.md)).

## Sources

- No official SGX primary-source page for the 12:00 SGT (09:00–12:00 half-day) early-close time was independently captured with a stable citation URL in this pass; the time and suppression condition match this project's existing implementation comments

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
