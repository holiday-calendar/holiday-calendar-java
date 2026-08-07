# CHF — Switzerland (SIC/SNB) Holidays

- **Standard:** ISO 4217 `CHF`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [CH](./CH.md) (national), [XSWX](./XSWX.md) (SIX Swiss Exchange) — `CHF` adds Berchtoldstag (January 2) and St. Stephen's Day naming for Boxing Day; unlike both siblings, `CHF` uses a strict no-adjustment convention (see below) instead of a weekend-roll rule.
- **Service class:** `HolidayCalendarServiceCHF` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.noRoll()` — the SIC (Swiss Interbank Clearing) system follows a no-adjustment convention: holidays are observed on their published calendar dates regardless of day of week; if a closure date falls on a weekend, no compensatory weekday closure is designated
- **Rollability exceptions:** all 10 holidays are `rollable(false)` — consistent with the no-roll strategy, since rolling wouldn't apply to any of them anyway

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | No | |
| Berchtoldstag | FIXED | — | No | January 2; SIC closure day observed as a cantonal holiday across most Swiss cantons. Not present on `CH` or `XSWX` — see Notes of Interest |
| Good Friday | FLOATING | — | No | |
| Easter Monday | FLOATING | — | No | |
| Labour Day | FIXED | — | No | May 1 |
| Ascension Day | FLOATING | — | No | 39 days after Easter Sunday |
| Whit Monday | FLOATING | — | No | |
| Swiss National Day | FIXED | — | No | August 1 |
| Christmas Day | FIXED | — | No | |
| St. Stephen's Day | FIXED | — | No | December 26 — same calendar date as `CH`/`XSWX`'s "Boxing Day," different name |

## Early Closes

Not applicable — `CHF` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**Berchtoldstag (January 2) is unique to `CHF`** among the three Swiss-related calendars in this codebase — neither `CH` nor `XSWX` includes it. It's a cantonal holiday observed across most Swiss cantons the day after New Year's Day, distinct from Swiss National Day as the one point where `CHF`'s 10-holiday list diverges upward from `CH`'s 9-holiday list (in addition to naming Boxing Day "St. Stephen's Day" instead).

**No-roll convention is a hard behavioral difference from both siblings.** `CH` and `XSWX` both use `DateRolls.previousFridayOrFollowingMonday()`; `CHF` uses `DateRolls.noRoll()`. A holiday landing on a Saturday or Sunday is simply not observed on any other day for `CHF` settlement purposes — this is a genuinely different substitution philosophy, not just a different weekend-days configuration.

## Sources

- [SIX — SIC Settlement Services](https://www.six-group.com/en/products-services/banking-services/interbank-clearing/settlement-services/sic.html) — cited directly in `HolidayCalendarServiceCHF`'s own Javadoc; official primary source for the SIC no-adjustment convention

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
