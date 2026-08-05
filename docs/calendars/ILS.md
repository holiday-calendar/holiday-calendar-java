# ILS — Israel (TASE/Bank of Israel) Holidays

- **Standard:** ISO 4217 `ILS`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [IL](./IL.md) (national) — `ILS` omits Yom Hazikaron (which `IL` carries) and adds 6 `EARLY_CLOSE` holidays that `IL` doesn't carry. Both share their base holiday list via the same package-private `IsraelHolidays` factory.
- **Service class:** `HolidayCalendarServiceILS` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday — **confirmed still correct for settlement
  purposes, investigated under issue #321.** TASE's January 2026 shift to a
  Monday–Friday *trading* week did not change its *settlement/clearing*
  calendar, which TASE's own Clearing House By-Laws define as anchored to the
  Bank of Israel's business days (Sunday–Friday, unchanged). See "Notes of
  Interest" below for the sourcing.
  Because all `ILS` holidays are `rollable(false)` with `DateRolls.noRoll()`,
  this value has no effect on `calculate()` — its only live effect is the
  `isWeekend()`/`isWeekendUTC()` query API.
- **Roll strategy:** `DateRolls.noRoll()` — settlement requires both counterparties to be available on the same calendar date; there is no roll-forward convention
- **Rollability exceptions:** all holidays are `rollable(false)`, consistent with the no-roll strategy

## Holidays

Shares 9 of `IL`'s 10 holidays (all except Yom Hazikaron) via `IsraelHolidays.baseHolidays()` — see [IL.md](./IL.md) for the full table and the Hebrew-calendar/Time4J computation notes. TASE and the Bank of Israel remain open on Yom Hazikaron, so it isn't part of this list.

## Early Closes

| Name | Close Time | Time Zone | Notes |
|------|-----------|-----------|-------|
| Erev Rosh Hashanah | 13:00 | Asia/Jerusalem | TASE half-day closure |
| Erev Yom Kippur | 13:00 | Asia/Jerusalem | TASE half-day closure |
| Erev Passover | 13:00 | Asia/Jerusalem | TASE half-day closure |
| Erev Shavuot | 13:00 | Asia/Jerusalem | TASE half-day closure |
| Erev Sukkot | 13:00 | Asia/Jerusalem | TASE half-day closure |
| Hoshana Raba | 13:15 | Asia/Jerusalem | See Notes of Interest — models Bank of Israel's reduced hours, not TASE's actual full-day closure |

## Notes of Interest

**Hoshana Raba is modeled as a Bank of Israel early close, but TASE itself fully closes that day.** Per this project's own implementation comments (confirmed against official TASE vacation schedules for 2022–2025), TASE closes completely on Hoshana Raba (7th day of Sukkot, 21 Tishri). The Bank of Israel, by contrast, operates with reduced hours until approximately 13:15 IST per its own Markets Department schedules. Since `ILS` is documented as representing both TASE and Bank of Israel settlement, and only one `EARLY_CLOSE` entry exists per holiday, this project chose to model Hoshana Raba as a 13:15 early close — correct for the Bank of Israel side, but understating TASE's actual behavior (a full closure) on that specific day. A consumer building a TASE-only trading calendar from `ILS` should treat Hoshana Raba as a full closure, not an early close.

**✅ Weekend staleness question — investigated under issue #321, resolved: `Friday+Saturday` is still correct.**

*What is confirmed* (primary sources read directly, see Sources below):

- **TASE trading week:** effective January 5, 2026, TASE moved its trading schedule from Sunday–Thursday to Monday–Friday (Monday–Thursday 09:59–17:25, Friday 09:59–13:50 local time). Independently corroborated by MSCI's and Solactive's index-provider announcements, and by TASE's own official trading-schedule page. This is specifically a *trading*-hours/days change.
- **TASE settlement/clearing calendar is unaffected — confirmed directly from TASE's own Clearing House By-Laws** (Part One — General, reflecting the "Update of March 03, 2026," i.e. current as of two months after the trading-day shift). The By-Laws define `"business day"`/`"clearing day"` as "a day on which the Bank of Israel performs monetary activity," a term deliberately distinct from `"trading day"` ("a day on which trading takes place on TASE"); the By-Laws' own "day of receipt" clause explicitly contemplates a business day that is not a trading day. TASE's clearing/settlement calendar is therefore anchored to the Bank of Israel's business week, not TASE's own trading week, and did not move to a Saturday+Sunday weekend.
- **Bank of Israel ZAHAV (RTGS settlement):** per the Bank of Israel's own 2026 business-days schedule, read directly: "the operating days of the RTGS system are Sunday through Friday... on a short business day — Friday and holiday eves — the banking business day ends at 14:00." Saturday is the standing non-operating day; Friday is short-hours, not closed.
- **Bank of Israel Markets Department:** per its own 2026 schedule, read directly, holidays are listed "except Saturdays," and "on Fridays, holiday eve, [and Passover/Sukkot intermediate days]... the Bank's working hours are until 13:15." Friday is short-hours here too, using a slightly different close time (13:15) than ZAHAV's (14:00), since these are two different Bank of Israel units.
- **Practical consequence:** since `ILS` uses `DateRolls.noRoll()` with all holidays `rollable(false)`, the `Friday+Saturday` `weekendDays` value has no effect on `calculate()` — its only live effect is on the `isWeekend()`/`isWeekendUTC()` query methods.

*What this rules out, and what remains a narrower open nuance:*

- **Ruled out:** a Saturday+Sunday TASE settlement weekend. TASE's own By-Laws confirm settlement stayed anchored to the Bank of Israel's Sunday–Friday business week.
- **Narrower nuance, not actioned in this pass:** the Bank of Israel's own schedules show Friday as short-hours, not a full closure — while `ILS` currently models Friday as a full `weekendDays` entry. Per this calendar's own precedent (see Hoshana Raba above), this is the kind of difference normally modeled as an `EARLY_CLOSE` holiday, not a `weekendDays` change; see the follow-up recommendation below.
- Two unreliable claims surfaced and were rejected during this investigation and must not be reused: (1) an earlier research pass's citation to a Bank of Israel press release about "redefining the definition of a banking business day" — the URL is unreachable/bot-blocked and the quote may be fabricated; (2) a Clearstream market-coverage page describing Friday→Sunday settlement, which is dated **September 2024** (predates the January 2026 change by 16 months) and describes the superseded regime — superseded by the TASE By-Laws evidence above, which is both more current and more authoritative.

*Bottom line:* the code's current `Friday+Saturday` `weekendDays` value for `ILS` is confirmed correct for settlement purposes as of this investigation. No code change is proposed.

**Possible follow-up (separate issue, not actioned here):** the confirmed Bank-of-Israel short-Friday/closed-Saturday pattern is structurally the same shape of fact as the Hoshana Raba TASE/BOI divergence already modeled above as an `EARLY_CLOSE` holiday. A future issue could explore whether Fridays should get similar `EARLY_CLOSE` treatment for the Bank-of-Israel side.

## Sources

- [TASE — Trading Vacation Schedule (trading hours tables)](https://www.tase.co.il/en/content/knowledge_center/trading_vacation_schedule) — TASE's own official page; retrieved and read directly. Confirms Monday–Friday trading, Friday trading closing 13:44–13:50 local time, no Sunday trading sessions.
- **TASE Clearing House By-Laws, Part One — General** (`content.tase.co.il`, "Update of March 03, 2026") — TASE's own official Clearing House rulebook, read directly. Defines `"business day"`/`"clearing day"` as anchored to Bank of Israel monetary activity, distinct from `"trading day"`; this is the source that resolves the TASE-settlement question — TASE's settlement calendar remained Bank-of-Israel-anchored (Sunday–Friday) and did not move with the trading-day shift.
- [Bank of Israel — ZAHAV RTGS system business days during 2026](https://www.boi.org.il/media/1x1a4n3r/zahav-holidays-2026-eng.pdf) — PDF read directly; confirms Sunday–Friday RTGS operation, Friday short day ending 14:00 IST, Saturday the standing closure day
- [Bank of Israel — Markets Department business days for 2026](https://www.boi.org.il/media/rcukpxgx/markets-department-business-days-for-2026.pdf) — PDF read directly; confirms holidays listed "except Saturdays" and Friday/holiday-eve short hours ending 13:15 IST
- [MSCI — Index announcement on TASE trading schedule change](https://app2.msci.com/webapp/index_ann/DocGet?pub_key=5P%2FP7%2F0GDSk%3D&lang=en&format=html) — fetched and confirmed; states TASE adopts Monday–Friday trading effective January 5, 2026, moving from Sunday–Thursday
- [Solactive — Announcement on TASE Monday–Friday trading schedule](https://www.solactive.com/announcements/56778) — fetched and confirmed live; independently corroborates the January 5, 2026 effective date and the first Friday trading session (January 9, 2026)
- **Caution note (superseded/rejected sources — do not cite):** (1) a Clearstream Israel market-coverage page describing Friday→Sunday settlement, dated September 2024, 16 months stale and describing the pre-transition regime; (2) an earlier research pass's citation to a Bank of Israel press release "redefining the definition of a banking business day" — the URL is unreachable/bot-blocked and the quote may be fabricated; (3) a search-engine-summary claim that TASE "clears Sunday to Friday" attributed to tase.co.il — unverifiable because the underlying TASE page never rendered fetchable content at the time it was attempted. None of these should be cited going forward now that direct primary sources are available.
