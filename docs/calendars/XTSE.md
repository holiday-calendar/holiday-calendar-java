# XTSE — Toronto Stock Exchange (TSX) Holidays

- **Standard:** ISO 10383 MIC `XTSE`
- **Category:** Market/Exchange
- **Sibling calendars:** [CA](./CA.md) (national), [CAD](./CAD.md) (Bank of Canada/Lynx) — `XTSE` shares its 10 base holidays with `CA` via the `CanadaHolidays` factory, then adds National Day For Truth and Reconciliation, a Christmas Eve early close, Christmas Day, and a collision-aware Boxing Day. `XTSE` and `CAD` use the same weekend roll direction (always forward) and the same `BoxingDayCAD` observance class.
- **Service class:** `HolidayCalendarServiceXTSE` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.followingMonday()` — verified against TMX Group's official Holiday Operating Schedule (2016, 2021, and 2017 Canada Day schedules): a statutory holiday falling on Saturday or Sunday rolls **forward** to the next available business day, never backward
- **Rollability exceptions:** all `FLOATING` base holidays (Family Day, Good Friday, Easter Monday, Victoria Day, Civic Holiday, Labour Day, Thanksgiving Day) are `rollable(false)` — weekday-anchored. National Day For Truth and Reconciliation is `rollable(false)` here (contrast with `CA`, where it's `rollable(true)`). Boxing Day is `rollable(false)` — its substitution is handled entirely by its own `BoxingDayCAD` observance, not the generic roll mechanism.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Family Day | FLOATING | — | No | |
| Good Friday | FLOATING | — | No | |
| Easter Monday | FLOATING | — | No | |
| Victoria Day | FLOATING | — | No | |
| Canada Day | FIXED | — | Yes | July 1 |
| Civic Holiday | FLOATING | — | No | |
| Labour Day | FLOATING | — | No | |
| Thanksgiving Day | FLOATING | — | No | |
| Remembrance Day | FIXED | — | Yes | November 11 |
| National Day For Truth and Reconciliation | FIXED | — | No | Note: `rollable(false)` here, vs. `rollable(true)` on `CA` — see [CA.md](./CA.md) |
| Christmas Day | FIXED | — | Yes | |
| Boxing Day | FLOATING | — | No | Computed via `BoxingDayCAD`; collision-aware with Christmas Day — see Notes of Interest |

## Early Closes

| Name | Close Time | Time Zone | Notes |
|------|-----------|-----------|-------|
| Christmas Eve | 13:00 | America/Toronto | Occurs whenever December 24 falls Monday through Friday |

## Notes of Interest

**TSX's weekend substitution rule always rolls forward**, unlike calendars using `previousFridayOrFollowingMonday()`. This was specifically verified against TMX Group's official Holiday Operating Schedule press releases for multiple years (2016, 2021) and cross-checked against the 2017 Canada Day schedule, per the implementation's own Javadoc.

**Boxing Day/Christmas Day collision, in concrete numbers.** In 2021, Christmas Day (a Saturday) rolled forward to Monday December 27, and Boxing Day (naturally Sunday December 26) rolled forward to Tuesday December 28 — both computed by the shared `BoxingDayCAD` observance, which encodes the full pairwise substitution logic (see [CAD.md](./CAD.md) for the complete case breakdown; `XTSE` reuses the identical `BoxingDayCAD` class). The ordinary Dec 24 early close proceeded on its natural date, unaffected by either roll.

**Christmas Eve early close is keyed directly off Dec 24's own day of week**, unlike NYSE's equivalent (see [XNYS.md](./XNYS.md)), which is keyed off Dec 25's day of week instead — two different suppression mechanisms for a similar-looking early close.

**National Day For Truth and Reconciliation's rollability differs from `CA`.** On `XTSE` it is `rollable(false)`; on the national `CA` calendar (and on `CAD`) it's `rollable(true)`. This wasn't independently corroborated against an external primary source in this pass — flagged as a fact worth double-checking if precision here matters for a downstream use case, since it appears to be a genuine cross-calendar inconsistency rather than a documented convention difference.

## Sources

- TMX Group Holiday Operating Schedule press releases (2016, 2021, 2017 Canada Day) — cited in the implementation's own Javadoc as the verification basis for the forward-only roll rule; not independently re-fetched in this pass, but corroborated generally via [TMX Group Holiday Operating Schedule (Nasdaq mirror, 2025)](https://www.nasdaq.com/press-release/tmx-group-holiday-operating-schedule-2025-12-03) and the 2021 Christmas/Boxing/New Year's roll matching [contemporaneous press coverage](https://www.newswire.ca/news-releases/tmx-group-holiday-operating-schedule-846437582.html)
- [TMX / TSX — Calendar](https://www.tsx.com/en/trading/calendars-and-trading-hours/calendar) — official current trading-calendar page
