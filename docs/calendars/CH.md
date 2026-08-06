# CH — Switzerland National Holidays

- **Standard:** ISO 3166-1 alpha-2 `CH`
- **Category:** National
- **Sibling calendars:** [CHF](./CHF.md) (SIC/SNB settlement), [XSWX](./XSWX.md) (SIX Swiss Exchange) — SIX is closed on every holiday observed by `CH`, plus two additional market-only holidays (see XSWX)
- **Service class:** `HolidayCalendarServiceCH` (`org.holiday.calendar.impl`, module `org.holiday.calendar.western`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday
- **Roll strategy:** `DateRolls.previousFridayOrFollowingMonday()` — Saturday rolls back to the preceding Friday, Sunday rolls forward to the following Monday
- **Rollability exceptions:** Good Friday, Easter Monday, Ascension Day, and Whit Monday are `rollable(false)` — as floating holidays already anchored to a specific weekday relative to Easter, they never fall on a weekend and rolling doesn't apply. All four fixed holidays (New Year's Day, Labour Day, Swiss National Day, Christmas Day, Boxing Day) are `rollable(true)`.

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| New Year's Day | FIXED | — | Yes | |
| Good Friday | FLOATING | — | No | Not observed in the cantons of Ticino or Valais — see Notes of Interest |
| Easter Monday | FLOATING | — | No | |
| Labour Day | FIXED | — | Yes | International Workers' Day (May 1) |
| Ascension Day | FLOATING | — | No | 39 days after Easter Sunday (the "40th day," counting Easter Sunday as day 1) |
| Whit Monday | FLOATING | — | No | Monday after Pentecost |
| Swiss National Day | FIXED | — | Yes | August 1; commemorates the Federal Charter of 1291. The only holiday in this list mandated by federal law — see Notes of Interest |
| Christmas Day | FIXED | — | Yes | |
| Boxing Day | FIXED | — | Yes | Day after Christmas; cantonal, not federal |

## Early Closes

Not applicable — the `CH` national calendar never includes early-close (half-day) sessions. SIX Swiss Exchange's Christmas Eve/New Year's Eve closures are modeled separately under `XSWX`; they are market-only holidays, not part of the national calendar.

## Notes of Interest

Switzerland has only **one** federally-mandated nationwide public holiday: Swiss National Day (August 1), enshrined by the 1993 federal popular initiative that made it a mandatory paid holiday under the Federal Constitution, in effect since 1994. Every other holiday in this list — including New Year's Day and Christmas — is decided at the cantonal level; the 8 non-federal holidays here represent the **majority-cantonal convention** (observed by most of the 26 cantons), not uniform federal law. There is no single Swiss national holiday list defined by federal statute covering all 9 dates.

Good Friday is a concrete example of this cantonal variation: it is a public holiday in 24 of the 26 cantons, but **not** in Ticino or Valais — despite both being majority-Catholic cantons, where one might expect broader observance of Christian holidays. Ticino's cantonal parliament has debated and rejected proposals to add it. Most banks and some businesses in both cantons still close or reduce hours on the day regardless.

This codebase models `CH` as a single canonical list (the majority-cantonal convention) rather than per-canton variants — a simplification worth knowing if a consumer needs holidays for a specific canton rather than the national default.

## Sources

- [Federal Department of Foreign Affairs (EDA) — National holiday and national anthem](https://www.eda.admin.ch/aboutswitzerland/en/home/gesellschaft/traditionen/nationalfeiertag.html) — official confirmation of Swiss National Day's status as the sole federally-mandated public holiday, and its basis in the 1993 popular initiative
- [Swiss Federalism — Public holidays in the Swiss Confederation](https://swissfederalism.ch/en/public-holidays-swiss-confederation/) — cantonal-variation overview; not a primary government source, but the clearest single account of which cantons observe which holidays (retrieved 2026-08-04)
- Good Friday's Ticino/Valais exception is corroborated across multiple third-party Swiss holiday aggregators (e.g. tour-switzerland.ch, karpeo.ch); no single official cantons-comparison page was located as of 2026-08-04

<!-- #325: source @author email updated to dave@holiday-calendar.org; no calendar content changed. -->
