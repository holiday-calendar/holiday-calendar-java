# JP — Japan National Holidays

**Standard:** ISO 3166-1 alpha-2 `JP`
**Category:** National
**Sibling calendars:** [JPY](./JPY.md) (Bank of Japan) — `JPY` shares all of `JP`'s holidays and cascade/sandwich logic verbatim, adding 3 BOJ-specific operational closures (Jan 2, Jan 3, Dec 31) on top. Japan has no dedicated exchange (MIC) calendar in this codebase — `JP` is documented as national-only.
**Service class:** `HolidayCalendarServiceJP` (`org.holiday.calendar.impl`, module `org.holiday.calendar.apac`), wrapped by the package-private `JapaneseHolidayCalendar` (see Notes of Interest)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.sundayToMonday()` — a holiday falling on Sunday rolls to the following Monday. **Saturday holidays are never rolled** — this is a real, statutory asymmetry (see Notes of Interest), not an oversight.
- **Rollability exceptions:** Coming of Age Day, Marine Day, Respect for the Aged Day, and Sports Day are `rollable(false)` — each is already anchored to "Nth Monday of the month," so it structurally can never fall on a Sunday and the roll doesn't apply. The two 2019 imperial-transition `SPECIAL_ANNIVERSARY` entries are also non-rollable, being one-off fixed dates. All other holidays are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Coming of Age Day | FLOATING | — | No | Fixed Jan 15 until 1999; 2nd Monday in January from 2000 |
| National Foundation Day | FLOATING | 1967 | Yes | February 11 |
| Emperor's Birthday | FLOATING | — | Yes | April 29 (Hirohito, 1949–1988); December 23 (Akihito, 1989–2018); February 23 (Naruhito, 2020+); **not observed in 2019** — see Notes of Interest |
| Vernal Equinox Day | FLOATING | — | Yes | Astronomical equinox in JST; NAOJ formula, valid 1980–2099 |
| Showa Day | FIXED | — | Yes | April 29; formerly Emperor's Birthday (1949–1988) and Greenery Day (1989–2006) |
| Constitution Memorial Day | FIXED | — | Yes | May 3 |
| Greenery Day | FLOATING | 2007 | Yes | Fixed May 4 from 2007 |
| Children's Day | FIXED | — | Yes | May 5 |
| Marine Day | FLOATING | — | No | Fixed July 20 (1996–1999); 3rd Monday in July from 2000 |
| Mountain Day | FLOATING | 2016 | Yes | Normally August 11; relocated for the Tokyo Olympics — see Notes of Interest |
| Respect for the Aged Day | FLOATING | — | No | Fixed Sept 15 (1966–2002); 3rd Monday in September from 2003 |
| Autumnal Equinox Day | FLOATING | — | Yes | Astronomical equinox in JST; NAOJ formula, valid 1980–2099 |
| Sports Day | FLOATING | — | No | Fixed Oct 10 (1966–1999); 2nd Monday in October from 2000; renamed from Health and Sports Day in 2020 |
| Culture Day | FIXED | — | Yes | November 3 |
| Labour Thanksgiving Day | FIXED | — | Yes | November 23 |
| Emperor's Abdication Day | SPECIAL_ANNIVERSARY | 2019 | No | April 30, 2019 — Emperor Akihito's abdication (one-time) |
| Enthronement Day | SPECIAL_ANNIVERSARY | 2019 | No | May 1, 2019 — Emperor Naruhito's accession (one-time) |

Plus a synthetic "National Holiday" entry (国民の休日) injected at runtime when applicable — see Notes of Interest.

## Early Closes

Not applicable — the `JP` national calendar never includes early-close (half-day) sessions.

## Notes of Interest

**`JP` is not a plain `HolidayCalendar` — it's wrapped by a package-private `JapaneseHolidayCalendar` subclass that layers two additional statutory rules on top of the base holiday list**, because neither rule fits the standard per-holiday `DateRoll` abstraction used everywhere else in this codebase:

1. **Cascading substitute-holiday rule (振替休日, Article 3 §3 of the 2007 Holiday Act amendment).** When a holiday falls on Sunday, its Monday substitute normally applies — but if that Monday is *already* occupied by a different holiday, the substitute cascades forward day-by-day to the next weekday that isn't already a holiday. This is a genuinely different mechanism than every other roll strategy in this codebase, which only ever look at the holiday's own natural date, never at what other holidays already occupy candidate substitute dates. Per the implementation's own comment, only Sunday→Monday rolls trigger a cascade; Saturday holidays are unaffected and simply stay on their natural date — a real statutory asymmetry, not a bug (this exact point was the subject of issue #125, now closed/fixed).
2. **Sandwiched-day rule (国民の休日, "citizens' holiday").** After the cascade is applied, a synthetic "National Holiday" is injected on any weekday that falls exactly between two consecutive holidays — most famously the day between Respect for the Aged Day and the Autumnal Equinox in years when they're 2 days apart. This synthetic entry doesn't appear in the source holiday list at all; it's computed dynamically per year inside `JapaneseHolidayCalendar.calculate()`.

**This is the most heavily bug-fixed calendar in the entire codebase.** Five closed issues (#125, #126, #127, #132, #133) — all specifically about `JP`/`JPY`'s cascade logic and 2020/2021 Tokyo Olympics relocations — directly shaped the current implementation. The comments throughout `JapaneseHolidayCalendar.java` and `JapaneseHolidays.java` (e.g. "unaffected (they stay on their natural date per issue #125)") are direct artifacts of that history — this is a rare case in this codebase where the *reason* a specific line of logic exists is preserved in a code comment rather than only in closed-issue history, which is exactly the kind of thing issue #243 is meant to make discoverable without archaeology.

**Mountain Day's Olympic relocation is a concrete, dated example of a broader pattern**: Mountain Day normally falls August 11, but was moved to August 10 in 2020 and August 9 in 2021 specifically to accommodate the (COVID-delayed) Tokyo Olympics broadcast schedule — implemented as a direct date override in the `Observance` lambda that bypasses the normal rollable pipeline entirely (the comment notes neither relocated date is a Sunday, so the cascade logic never needed to engage for these years).

**Emperor's Birthday is discontinuous across three different calendar dates tied to three different reigns**, and is genuinely *not observed at all* in 2019 — the single-year gap between Emperor Akihito's abdication (April 30, 2019) and Emperor Naruhito's birthday-based holiday not yet existing for that calendar year (Naruhito's Emperor's Birthday, February 23, only starts being observed from 2020). The two `SPECIAL_ANNIVERSARY` entries (Abdication Day, Enthronement Day) exist specifically to fill that 2019 gap with one-time holidays.

## Sources

- [Japanese Law Translation — Act on National Holidays (official English translation)](https://www.japaneselawtranslation.go.jp/en/laws/view/4846/en) — official government translation of the underlying statute (国民の祝日に関する法律, Act No. 178 of 1948, as amended)
- The 2007 amendment's cascade-rule mechanics, the Saturday/Sunday asymmetry, and the sandwiched-day rule are independently corroborated by third-party sources (e.g. Wikipedia's "Public holidays in Japan" and "Happy Monday System" articles) consistent with this project's own implementation
- The closed-issue history (#125, #126, #127, #132, #133) driving this implementation is this project's own GitHub history, not an external source — cited here because it's directly discoverable evidence of why the code looks the way it does
