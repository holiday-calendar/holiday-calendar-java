# TR — Turkey (National) Holidays

- **Standard:** ISO 3166-1 alpha-2 `TR`
- **Category:** National
- **Sibling calendars:** [TRY](./TRY.md) (BIST/TCMB settlement) — `TR` and `TRY` share the identical 14-holiday base list plus the same Republic Day Eve early close via `TurkeyHolidays`, differing only in rollability and roll strategy.
- **Service class:** `HolidayCalendarServiceTR` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

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
- Diyanet's method genuinely can, and does, diverge from the Umm al-Qura calendar. **Saudi Arabia is the correct benchmark for this comparison, not the UAE** — the Umm al-Qura calendar is Saudi Arabia's own official Hijri calendar (maintained by King Abdulaziz City for Science and Technology), computed astronomically for Mecca's coordinates, with religious months (Ramadan, Shawwal, Dhu al-Hijjah) specifically confirmed by Saudi Arabia's own moon-sighting process. Other GCC states generally align with Saudi Arabia's announcement — not because they mechanically derive from Saudi's published calendar, but because each runs its own moon-sighting committee and, given comparable longitude/latitude to Mecca, those committees usually (not always) reach the same crescent-visibility conclusion Saudi Arabia does.
- The code's own internal example — "2026 Eid al-Adha: Diyanet 27 May vs. UAE/Umm al-Qura 26 May" (`eid-al-adha-tr.csv`'s comment, corroborated by `eid-al-adha-ae.csv`'s `2026,2026-05-26` row) — traces to a **specific, higher-confidence root cause**, not just conflicting secondary reporting: multiple UAE-specific sources (Khaleej Times, GulfToday, Economy Middle East) confirm the UAE Fatwa Council's actual 2026 announcement was Arafat Day (9 Dhu al-Hijjah) on Tuesday May 26, with Eid al-Adha's first day (10 Dhu al-Hijjah) on Wednesday May 27 — matching Diyanet and Saudi Arabia exactly. The `2026-05-26` value recorded in `eid-al-adha-ae.csv` appears to be **Arafat Day mistaken for Eid al-Adha's first day**, not a genuine UAE moon-sighting divergence. This is the identical failure mode `eid-al-adha-tr.csv`'s own comment already documents catching once before, for a different year: "A previously-recorded 2025 divergence in this file was found to be a data error — 2025-06-05 is Arefe, the eve of Bayram, not the 1st day." Filed as bug #322 against the `AE`/`AED` CSV data, since correcting it would also remove the false "known divergence" premise from `TR`'s own source comments.
- A genuine, independently-verified divergence exists instead for **Ramadan 2026's start date**: Saudi Arabia, Qatar, the UAE, Kuwait, and Bahrain all began Wednesday, February 18, 2026, while Turkey (Diyanet), Egypt, Jordan, and Morocco all began Thursday, February 19, 2026 — a real 1-day gap between Diyanet's pre-calculated astronomical date and Saudi Arabia's moon-sighting-confirmed date. Notably, Egypt, Jordan, and Morocco (all separately documented in this codebase — see [EG.md](./EG.md), [JO.md](./JO.md), [MA.md](./MA.md)) sided with Turkey's date that year rather than Saudi Arabia's, despite those three countries' own CSV data in this project being sourced as Umm al-Qura projections for far-future years — a reminder that even countries whose *far-future* dates this codebase projects from Umm al-Qura don't necessarily track Saudi Arabia's *actual* moon-sighting outcome every year in practice.

**A 4th Eid al-Adha day**, unlike Saudi Arabia's, the UAE's, or Qatar's 2–3 day observances — see the Holidays table above.

## Sources

- [Umm Al-Qura Calendar — official "About" page](https://www.ummulqura.org.sa/en/about-calendar) — confirms Umm al-Qura is Saudi Arabia's own official Hijri calendar, computed for Mecca and maintained by King Abdulaziz City for Science and Technology, with religious months confirmed by Saudi Arabia's own moon-sighting process — establishes Saudi Arabia, not the UAE, as the correct comparison point for "Umm al-Qura" claims
- [GulfToday — "Dhul Hijjah crescent sighted in UAE, first day of Eid Al Adha on May 27"](https://www.gulftoday.ae/news/2026/05/17/dhul-hijjah-crescent-sighted-in-uae-first-day-of-eid-al-adha-on-may-27), [Economy Middle East — "UAE, Saudi Arabia confirm moon sighting: Dhul Hijjah begins Monday, Eid Al Adha on May 27"](https://economymiddleeast.com/news/uae-saudi-arabia-confirm-moon-sighting-dhul-hijjah-begins-monday-eid-al-adha-on-may-27/), and [Khaleej Times' UAE moon-sighting coverage](https://www.khaleejtimes.com/uae/uae-eid-al-adha-2026-dhul-hijjah-crescent-moon-sighted) — all confirm the UAE Fatwa Council's actual 2026 Eid al-Adha first day is May 27 (Arafat Day was May 26), the basis for identifying `eid-al-adha-ae.csv`'s `2026-05-26` entry as a likely Arafat-Day/Eid-Day data mixup rather than a genuine divergence
- [Economy Middle East — Ramadan 2026 start-date split](https://economymiddleeast.com/news/which-countries-start-ramadan-2026-on-thursday-february-19-and-why-dates-differ-ramadan-2026-start-date-thursday-february-19-countries-list/) and [Ahram Online](https://english.ahram.org.eg/NewsContent/2/8/562558/World/Region/Saudi-Arabia,-Qatar,-UAE-announce-Wednesday-as-fir.aspx) — confirm the verified Diyanet/Saudi Arabia divergence for Ramadan 2026 (Feb 18 vs Feb 19) cited above
- Diyanet sourcing methodology, the GitHub issue #180 correction, and the Republic Day Eve statutory basis (Law No. 2429) are documented directly in this project's own source comments (`TurkeyHolidays.java`, `HolidayCalendarServiceTR.java`); no official Diyanet or BIST primary-source page was independently captured with a stable citation URL in this pass
