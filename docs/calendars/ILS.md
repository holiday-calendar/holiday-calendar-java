# ILS — Israel (TASE/Bank of Israel) Holidays

- **Standard:** ISO 4217 `ILS`
- **Category:** Central Bank/Settlement
- **Sibling calendars:** [IL](./IL.md) (national) — `ILS` omits Yom Hazikaron (which `IL` carries) and adds 6 `EARLY_CLOSE` holidays that `IL` doesn't carry. Both share their base holiday list via the same package-private `IsraelHolidays` factory.
- **Service class:** `HolidayCalendarServiceILS` (`org.holiday.calendar.impl`, module `org.holiday.calendar.mena`)

## Weekend & Date Roll

- **Weekend days:** Friday+Saturday (Israeli market convention) — **see the staleness flag below; this is now under active investigation as issue #321**
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

**⚠️ Weekend staleness flag — tracked as bug #321, not resolved in this doc.** Primary-source research (during #274, the `IL` pilot) found that TASE shifted its trading week from Sunday–Thursday to **Monday–Friday**, effective January 2026, for MSCI Europe index inclusion — a TASE-specific change, confirmed to also affect TASE's own reporting, clearing, and oversight mechanisms, not just the ticker. Separately, the Bank of Israel's ZAHAV RTGS settlement system appears to still operate Sunday–Friday (Friday as a short day, full closure only Saturday) per its own 2026 business-days schedule. If both of these are accurate, `ILS`'s single `Friday+Saturday` `weekendDays` value (shared verbatim from `IsraelHolidays.ISRAEL_WEEKEND`, the same constant `IL` uses) may no longer correctly represent either side of what `ILS` claims to model — TASE's weekend looks like it may now be Saturday+Sunday, while the Bank of Israel's "weekend" isn't cleanly two full days at all (Friday is short, not closed). This needs primary-source verification directly against TASE's and the Bank of Israel's own official schedules (attempted in this pass but blocked by JS-rendered pages that couldn't be fetched as plain text) before any code change — see #321 for the full investigation writeup and suggested next steps. This doc describes the calendar's *current* implemented behavior; it does not assert that behavior is still correct.

## Sources

- [Bank of Israel — Markets Department business days for 2026](https://www.boi.org.il/media/rcukpxgx/markets-department-business-days-for-2026.pdf) (PDF; existence and title confirmed, not machine-extracted)
- [Bank of Israel — ZAHAV RTGS system business days during 2026](https://www.boi.org.il/media/1x1a4n3r/zahav-holidays-2026-eng.pdf) — cited for the Sunday–Friday RTGS operating week claim above
- [Ynet News — "After 72 years, why is the Tel Aviv Stock Exchange ending Sunday trading?"](https://www.ynetnews.com/business/article/sjcdaq0xwe) — cited for the TASE Monday–Friday trading-week transition
- TASE's own official vacation-schedule page (https://www.tase.co.il/en/content/knowledge_center/trading_vacation_schedule/) was attempted directly in this pass but returned only a JavaScript app shell, not fetchable content — flagged rather than silently skipped; see #321 for follow-up
