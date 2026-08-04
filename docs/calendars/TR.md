# TR — Turkey (National) Holidays

**Standard:** ISO 3166-1 alpha-2 `TR`
**Category:** National
**Sibling calendars:** [TRY](./TRY.md) (BIST/TCMB settlement) — `TR` and `TRY` share the identical 14-holiday base list plus the same Republic Day Eve early close via `TurkeyHolidays`, differing only in rollability and roll strategy.
**Service class:** `HolidayCalendarServiceTR` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday — **not** the Friday+Saturday GCC convention used by every other national calendar in this MENA module
- **Roll strategy:** `DateRolls.followingMonday()` — fixed holidays falling on Saturday or Sunday roll forward to the following Monday
- **Rollability exceptions:** all 7 Islamic holidays are `rollable(false)`. The 6 `FIXED` Gregorian holidays are `rollable(true)`. The Republic Day Eve early close is `rollable(false)` by construction (early closes are never rolled) and additionally does **not** shift when 28 October falls on a weekend — see Notes of Interest.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| National Sovereignty and Children's Day | FIXED | — | Yes | April 23; opening of the Grand National Assembly (1920) |
| Labour and Solidarity Day | FIXED | — | Yes | May 1 |
| Commemoration of Atatürk, Youth and Sports Day | FIXED | — | Yes | May 19; Atatürk's 1919 arrival in Samsun |
| Eid al-Fitr (Ramazan Bayramı) | FLOATING | — | No | 1 Shawwal AH — see Notes of Interest for Diyanet sourcing |
| Eid al-Fitr (2nd Day) | FLOATING | — | No | 2 Shawwal AH |
| Eid al-Fitr (3rd Day) | FLOATING | — | No | 3 Shawwal AH |
| Democracy and National Unity Day | FIXED | — | Yes | July 15; commemorates the 2016 failed coup attempt |
| Eid al-Adha (Kurban Bayramı) | FLOATING | — | No | 10 Dhu al-Hijjah AH |
| Eid al-Adha (2nd Day) | FLOATING | — | No | 11 Dhu al-Hijjah AH |
| Eid al-Adha (3rd Day) | FLOATING | — | No | 12 Dhu al-Hijjah AH |
| Eid al-Adha (4th Day) | FLOATING | — | No | 13 Dhu al-Hijjah AH — a 4th day, unlike Saudi Arabia's 3-day observance |
| Victory Day | FIXED | — | Yes | August 30; Battle of Dumlupınar (1922) |
| Republic Day | FIXED | — | Yes | October 29; proclamation of the Republic (1923) |

## Early Closes

| Name | Close Time | Time Zone | Notes |
|------|-----------|-----------|-------|
| Republic Day Eve | 13:00 | Europe/Istanbul | October 28; does **not** shift when it falls on a Saturday or Sunday — see Notes of Interest |

## Notes of Interest

**Turkey is the one MENA-module national calendar with a genuine `EARLY_CLOSE` entry.** Per this project's own README, "`TR` (Turkey's national calendar) is the one national code that includes an early-close entry (Republic Day Eve), because it is a statutory closure rather than a market-only convention" (Law No. 2429 applies to all public institutions, not just Borsa Istanbul or the Central Bank). The same entry is carried verbatim by `TRY`.

**Turkey uses a Western-style Saturday+Sunday weekend, not the GCC's Friday+Saturday** — the only national calendar in this MENA module to do so (Israel's `IL` uses Friday+Saturday like the GCC countries, not Saturday+Sunday like Turkey).

**Diyanet sourcing is a genuinely different data pipeline than every other MENA Islamic calendar in this codebase**, not just a different set of CSV rows:
- 2024–2035 dates are actual official Diyanet (Presidency of Religious Affairs) *published* dates — not projections — because Diyanet publishes several years ahead of the current year (confirmed against BIST market-calendar announcements for 2024–2026, and against GitHub issue #180 in this project's own history, which corrected an earlier assumption that Diyanet only published 1–2 years ahead).
- 2036–2055 dates are pre-computed via an `IlmiTakvimCalculator` (true lunar conjunction + Ankara sunset visibility rule) and baked into the CSV ahead of time — calibrated to exactly reproduce all 24 Diyanet-published dates from 2024–2035 — pending Diyanet's own future publication of those years. A live fallback to the same calculator exists in code as a defensive backstop, but is not the normal runtime lookup path since the CSV already contains rows through 2055.
- Diyanet's method is confirmed (per this project's own source comments) to differ from the Umm al-Qura calendar used by other MENA countries by ±1–2 days in some years — the 2026 Eid al-Adha date is cited internally as an example (Diyanet 27 May vs. UAE/Umm al-Qura 26 May). This specific comparison was not independently re-confirmed in this pass: external search found Saudi Arabia's own 2026 Eid al-Adha date (per its Supreme Court's moon-sighting announcement) is also May 27 — matching Diyanet's date in this instance rather than the UAE date cited in the code comment. This doesn't necessarily mean the code's claim is wrong (UAE's own moon-sighting committee is independent of Saudi Arabia's and could still differ by a day even when Diyanet and Saudi Arabia agree), but it wasn't possible to fully reconcile all three data points (Diyanet, UAE, Saudi Arabia) for 2026 specifically in this pass — flagged rather than asserted.

**A 4th Eid al-Adha day**, unlike Saudi Arabia's, the UAE's, or Qatar's 2–3 day observances — see the Holidays table above.

## Sources

- [Eventbrite listing citing Wednesday, May 27, 2026 for Eid al-Adha](https://www.eventbrite.com/e/eid-al-adha-kurban-bayrami-2026-tickets-1988457176944) and multiple corroborating secondary sources citing the Saudi Supreme Court's moon-sighting announcement for the same date — used only to attempt (inconclusively) cross-checking the Diyanet/Umm al-Qura divergence example above
- Diyanet sourcing methodology, the GitHub issue #180 correction, and the Republic Day Eve statutory basis (Law No. 2429) are documented directly in this project's own source comments (`TurkeyHolidays.java`, `HolidayCalendarServiceTR.java`); no official Diyanet or BIST primary-source page was independently captured with a stable citation URL in this pass
