<!--
Template for a per-calendar reference doc (see #243 / docs/243_CALENDAR_REFERENCE_DOC_PLAN.md).

To author a new one: copy this file to docs/calendars/<CODE>.md, fill in every
placeholder in angle brackets, delete this comment block, and delete the
"Early Closes" section entirely if the calendar has no EarlyCloseHoliday
entries (don't leave an empty table).

Every fact in "Notes of Interest" must be checked against the actual source
code or a primary source before being written down — do not carry forward
descriptions from an issue, PR, or commit message without independently
verifying them.
-->
# <CODE> — <Country/Market Name>

- **Standard:** <ISO 3166-1 alpha-2 | ISO 4217 | ISO 10383 MIC> `<CODE>`
- **Category:** National | Central Bank/Settlement | Market/Exchange
- **Sibling calendars:** [<CODE2>](./<CODE2>.md), [<CODE3>](./<CODE3>.md) — or "None"
- **Service class:** `HolidayCalendarService<CODE>` (`org.holiday.calendar.impl`, module `org.holiday.calendar.<western|apac|mena>`)

## Weekend & Date Roll

- **Weekend days:** Saturday+Sunday | Friday+Saturday
- **Roll strategy:** `DateRolls.<method>` — <one-line rationale, with primary-source citation if the rule is a market/government convention rather than an obvious default>
- **Rollability exceptions:** <holidays marked `rollable(false)`, or "None">

## Holidays

| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|
| <Holiday Name> | FIXED / FLOATING / SPECIAL_ANNIVERSARY | <year, or "—" if since inception> | Yes/No | <calendar-specific observance notes> |

## Early Closes

<!-- Delete this section entirely if the calendar has no EarlyCloseHoliday entries. -->

| Name | Close Time | Time Zone | Notes |
|------|-----------|-----------|-------|
| <Holiday Name> | <e.g. 13:00> | <e.g. America/New_York> | <suppression rules, e.g. "suppressed when Dec 25 falls Mon/Sat/Sun"> |

## Notes of Interest

<Freeform. Peculiarities, collision/rollover interactions between holidays,
state/cantonal/provincial-level variation, and — for CSV-backed or
astronomical-fallback calendars (Islamic, Hebrew, lunar) — the
`dataValidThrough()` ceiling, e.g. "Data valid through <year>; see
`<Country>Holidays.DATA_VALID_THROUGH`." This is the only freeform section —
everything else follows the fixed structure above.>

## Sources

- <Official authority — central bank circular, exchange trading-calendar page, government gazette. Link to a specific dated page/PDF where possible.>
- <If no stable URL exists, note the retrieval date and, where practical, an archived (e.g. Wayback Machine) snapshot link alongside the live one.>
- <If no primary source could be located at all, say so explicitly here rather than omitting the section, e.g. "No primary source located as of <date>; verified against `<ClassName>` behavior only.">
