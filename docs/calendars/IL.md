# IL — Israel National Holidays

- **Standard:** ISO 3166-1 alpha-2 `IL`
- **Category:** National
- **Sibling calendars:** [ILS](./ILS.md) (TASE/Bank of Israel settlement) — `ILS` mirrors 9 of `IL`'s 10 holidays (omitting Yom Hazikaron, since TASE remains open on it) and adds 6 `EARLY_CLOSE` holidays that `IL` doesn't carry. `IL` and `ILS` share their holiday-list source via a package-private `IsraelHolidays` factory class.
- **Service class:** `HolidayCalendarServiceIL` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (Israeli work week; Sunday is the first business day)
- **Roll strategy:** `DateRolls.followingSunday()` — a holiday falling on Friday or Saturday rolls forward to the following Sunday
- **Rollability exceptions:** all 10 holidays are `rollable(false)`. Every holiday here is computed from the Hebrew calendar (via Time4J's `HebrewCalendar`), and Hebrew-calendar dates are observed on their specific calendar day regardless of the Gregorian day of week — the concept of "rolling" a Hebrew-calendar holiday doesn't apply the way it does to a Gregorian `FIXED` holiday like New Year's Day.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| Rosh Hashanah | FLOATING | — | No | Jewish New Year, 1 Tishri |
| Rosh Hashanah (2nd Day) | FLOATING | — | No | 2 Tishri |
| Yom Kippur | FLOATING | — | No | Day of Atonement, 10 Tishri |
| Sukkot | FLOATING | — | No | Festival of Tabernacles, first day, 15 Tishri |
| Shemini Atzeret / Simchat Torah | FLOATING | — | No | 22 Tishri; combined onto a single day in Israel (observed as two separate days in most Diaspora communities) |
| Passover | FLOATING | — | No | First day, 15 Nisan |
| Passover (7th Day) | FLOATING | — | No | 21 Nisan; Israel observes a 7-day Passover (Diaspora communities generally observe 8) |
| Yom Hazikaron | FLOATING | — | No | Israeli Memorial Day, 4 Iyar; not carried by `ILS` — see Notes of Interest |
| Yom Ha'atzmaut | FLOATING | — | No | Independence Day, 5 Iyar; date is statutorily postponed when its natural date falls on Sun/Mon/Fri/Sat — see Notes of Interest |
| Shavuot | FLOATING | — | No | Feast of Weeks / Pentecost, 6 Sivan |

## Early Closes

Not applicable — the `IL` national calendar carries no `EARLY_CLOSE` entries. TASE/Bank of Israel holiday-eve half-day closures are modeled separately under `ILS`.

## Notes of Interest

**Hebrew-calendar computation, no data ceiling.** All 10 holidays are computed algorithmically from the Hebrew calendar via Time4J's `net.time4j.calendar.HebrewCalendar` — there is no CSV lookup table and no `dataValidThrough()` ceiling for this calendar (unlike, for example, this project's Islamic-calendar-based MENA calendars, which are CSV-backed with an astronomical-fallback ceiling). `IL` can compute correct dates arbitrarily far into the future or past.

**Yom Hazikaron / Yom Ha'atzmaut coupling.** Yom Hazikaron (Memorial Day) is always observed exactly one day before Yom Ha'atzmaut (Independence Day) — a linkage set by a 1963 statutory shift rule, not an independent calendar calculation. Yom Ha'atzmaut's own natural Hebrew-calendar date (5 Iyar) is itself statutorily postponed when it would otherwise fall on a Sunday, Monday, Friday, or Saturday, to keep the preceding Memorial Day off Shabbat and to avoid extending the weekend disruption. Both rules are implemented in `IndependenceDay.computeDate` and `YomHazikaron`.

**Deliberately omitted holidays.** The source (`IsraelHolidays`) documents two conscious omissions: Yom HaShoah (Holocaust Remembrance Day, 27 Nisan) is a national commemoration but not a statutory public rest holiday under Israel's Work and Rest Hours Law, and TASE remains open on it; Sigd (29 Heshvan) was omitted for low market relevance, also not a TASE closure day.

**`IL` vs `ILS` divergence.** `ILS` (Israel's settlement calendar) omits Yom Hazikaron entirely, because TASE and the Bank of Israel remain open on it even though it's a statutory day of national observance for government offices and schools. This is the one point where the "shared source, two consumers" pattern still produces genuinely different holiday counts (10 for `IL`, 9 base + 6 early-closes for `ILS`) rather than just different formatting of the same list.

**Possible staleness flag — not yet verified against `IL` itself, but relevant to `ILS`.** While researching this doc, primary-source research turned up that the Tel Aviv Stock Exchange shifted its trading week from Sunday–Thursday to **Monday–Friday**, effective January 2026 — ending 72 years of Sunday trading sessions, partly to qualify for MSCI Europe index inclusion. Per contemporaneous reporting, this affected not just trading but also TASE's own reporting, clearing, and oversight mechanisms. It is a TASE-specific change, not a change to Israel's national work week — Bank of Israel's ZAHAV RTGS settlement system reportedly still operates Sunday–Friday (Friday as a shortened business day, full closure only on Saturday), which is a *third*, distinct schedule from both `IL`'s Friday+Saturday weekend and TASE's apparent new Saturday+Sunday weekend. This doesn't affect `IL`'s own correctness (it's a national/government calendar, unrelated to TASE), but it strongly suggests `ILS`'s current `Friday+Saturday` `weekendDays` setting (shared verbatim from `IsraelHolidays.ISRAEL_WEEKEND`) may now be stale for the TASE side of what that calendar represents. Recommend opening a separate issue to investigate before the `ILS` reference doc (#305) is written, rather than resolving it here.

## Sources

- [Bank of Israel — Markets Department business days for 2026](https://www.boi.org.il/media/rcukpxgx/markets-department-business-days-for-2026.pdf) (PDF; not machine-extractable via automated fetch as of 2026-08-04, but confirms the existence and title of the official annual schedule)
- [Bank of Israel — ZAHAV RTGS system business days during 2026](https://www.boi.org.il/media/1x1a4n3r/zahav-holidays-2026-eng.pdf) — cited for the Sunday–Friday RTGS operating week / Friday short-day claim in Notes of Interest (PDF; content confirmed via search snippet, not full document extraction)
- [Ynet News — "After 72 years, why is the Tel Aviv Stock Exchange ending Sunday trading?"](https://www.ynetnews.com/business/article/sjcdaq0xwe) — cited for the TASE Monday–Friday trading-week transition in Notes of Interest
- No official Israeli government (e.g. Ministry of Interior or Knesset) primary source was located as of 2026-08-04 specifically enumerating the 10 statutory national holidays; the holiday list and the Yom Hazikaron/Yom Ha'atzmaut postponement rules are corroborated by the codebase's own algorithmic implementation (Time4J `HebrewCalendar`) rather than an external citation — flagged here rather than omitted, per this project's citation standard
