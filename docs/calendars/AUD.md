# AUD — Australia (RBA) Holidays

- **Standard:** ISO 4217 `AUD`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [AU](./AU.md) (national), [XASX](./XASX.md) (ASX) — `AUD` omits Easter Saturday (which both `AU` and `XASX` carry) and instead adds a New South Wales-specific "Bank Holiday" that neither sibling has.
- **Service class:** `HolidayCalendarServiceAUD` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.previousFridayOrFollowingMonday()` — same as `AU`
- **Rollability exceptions:** Good Friday, Easter Monday, King's Birthday, and Bank Holiday are `rollable(false)` (weekday-anchored). All `FIXED` holidays are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Australia Day | FIXED | — | Yes | January 26 |
| Good Friday | FLOATING | — | No | |
| Easter Monday | FLOATING | — | No | |
| ANZAC Day | FIXED | — | Yes | April 25 |
| King's Birthday | FLOATING | — | No | Second Monday in June — see `AU`'s Notes of Interest for the Queensland/WA date variation this project doesn't model |
| Bank Holiday | FLOATING | — | No | New South Wales-specific; first Monday in August — see Notes of Interest |
| Christmas Day | FIXED | — | Yes | |
| Boxing Day | FIXED | — | Yes | |

Easter Saturday is **not** observed by `AUD` (unlike `AU` and `XASX`).

## Early Closes

Not applicable — `AUD` carries no `EARLY_CLOSE` entries.

## Notes of Interest

**A genuinely asymmetric holiday set relative to `AU`/`XASX`, not just a subset.** `AUD` isn't simply "`AU` minus Easter Saturday" — it independently *adds* the New South Wales Bank Holiday (first Monday in August), which neither `AU` nor `XASX` includes at all. Since RBA (Reserve Bank of Australia) settlement is headquartered in Sydney, NSW, including an NSW-specific holiday on the settlement calendar is a defensible modeling choice, but it means `AUD` can't be treated as a strict subset of `AU`'s holiday list — it has one holiday the others don't, and is missing one they both have.

## Sources

- Easter Saturday's WA/Tasmania exclusion and King's Birthday's state variation are documented in [AU.md](./AU.md); not re-verified independently here since `AUD` inherits the same underlying facts
- No official Reserve Bank of Australia primary-source page confirming the NSW Bank Holiday's inclusion rationale on the RBA settlement calendar was located in this pass; the first-Monday-in-August NSW Bank Holiday itself is well-established public record
