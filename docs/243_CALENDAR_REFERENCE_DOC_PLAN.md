# Implementation Plan — Issue #243: Per-Calendar Reference Documentation

(Researcher + tech-writer draft, corrected per devil's-advocate critique — see "Corrections" section)

## Corrections Applied After Devil's-Advocate Review

- **Factual error retracted:** the earlier "Israel Hoshana Raba dual-purpose early close" claim is **false**. `IsraelHolidays.java` defines Hoshana Raba only in `earlyCloseHolidays()` — it never appears in the base/full-closure holiday list. It is single-purpose (early close only). Every "notes of interest" fact must be checked against source code/primary sources during authoring, not carried forward from secondary descriptions (including this issue's own text) unverified.
- **"Boxing Day collision-cascade":** the underlying mechanism (bespoke day-of-week branching in `BoxingDayCAD.java`, bypassing the generic `DateRoll`) is real, but "cascade" is not an established codebase term — use it only as descriptive prose, not as if it were existing terminology.
- **CI check was broken as originally specified:** it referenced `org.holiday.calendar.core.HolidayCalendarService`, which doesn't exist — the real package is `org.holiday.calendar.HolidayCalendarService` (the `.core` suffix is the JPMS module name, not the Java package). See revised CI section below.
- **CI check only covered the rare case:** a new `provides` line in `module-info.java` fires only when a calendar is *added*. The common case — an existing calendar's holiday list, roll strategy, or observance logic changing — touched none of that and would previously slip through with zero doc-drift check. Revised below.
- **Second pilot added:** CH alone doesn't stress-test CSV-backed/Islamic-calendar complexity or multi-country settlement systems (EUR). Added a MENA pilot before mass production.
- **README cross-linking de-gated:** changed from "only after all 47 exist" to incremental, per merged file — no reason to withhold discoverability for months.
- **Citation standard and link-rot handling added** (previously undefined, flagged as a bikeshedding risk).

## Scope

47 calendar codes across 3 modules:
- **Western (20):** US, USD, XNYS, CA, CAD, XTSE, UK, GBP, XLON, CH, CHF, XSWX, DE, XETR, FR, XPAR, AU, AUD, XASX, EUR
- **APAC (7):** CN, CNY, JP, JPY, SG, SGD, XSES
- **MENA (20):** AE, AED, SA, SAR, IL, ILS, TR, TRY, QA, QAR, EG, EGP, KW, KWD, BH, BHD, MA, MAD, JO, JOD

## Directory Structure

- Flat files: `docs/calendars/<CODE>.md` (uppercase, matches code exactly, e.g. `docs/calendars/XNYS.md`)
- `docs/calendars/TEMPLATE.md` — the authoring template
- `docs/calendars/README.md` — single index, two tables (by Region, by Category), plus links to OBSERVANCE_PATTERNS.md / PORTING_GUIDE.md
- No separate WESTERN.md/APAC.md/MENA.md regional guide files (rejected as redundant fourth navigation layer over a 47-row table)

## Per-Calendar Template (6 sections)

```markdown
# <CODE> — <Country/Market Name>

**Standard:** <ISO 3166-1 alpha-2 | ISO 4217 | ISO 10383 MIC> `<CODE>`
**Category:** National | Central Bank/Settlement | Market/Exchange
**Sibling calendars:** [<CODE2>](./CODE2.md), [<CODE3>](./CODE3.md) — or "None"
**Service class:** `HolidayCalendarService<CODE>` (`org.holiday.calendar.<module>`)

## Weekend & Date Roll
- Weekend days: Saturday+Sunday | Friday+Saturday
- Roll strategy: `DateRolls.<method>` — <one-line rationale + primary source citation>
- Rollability exceptions: <holidays marked non-rollable, or "None">

## Holidays
| Name | Type | First Year | Rollable | Notes |
|------|------|-----------|----------|-------|

## Early Closes
(Omit entirely if calendar has no EarlyCloseHoliday entries)
| Name | Close Time | Time Zone | Notes |
|------|-----------|-----------|-------|

## Notes of Interest
Freeform: peculiarities, cascade/collision logic, data ceilings for CSV-backed
calendars (e.g. dataValidThrough()), cantonal/state-level variation, etc.
This is the only freeform section.

## Sources
- <Official authority — central bank circular, exchange trading-calendar page>
```

Sections deliberately dropped from the researcher's original 8-section proposal:
"Data Scope & Reliability" (folded into Notes of Interest, one bullet, only when relevant),
"Testing & Validation" (dropped — test names churn independently of docs, no per-file value),
"Related Documentation" (moved to README index once, not duplicated 47x).

## Answers to Open Questions

1. EUR/TARGET2: document as one settlement system, mirroring the single `HolidayCalendarService` class — no per-country decomposition.
2. Sibling duplication: self-contained tables per file + a "Sibling calendars" link line (not cross-reference-only).
3. Observance algorithm detail: link to Javadoc; don't duplicate algorithm prose in the doc (avoids drift).
4. Year boundaries: only document `dataValidThrough()` ceiling, only for CSV-backed calendars.
5. Early closes: separate table (incompatible columns vs. main holiday table).
6. National/market hierarchy: flat files; "Sibling calendars" line is the only hierarchy signal.
7. Scope prioritization: phase by region, Western → APAC → MENA.
8. Standard labels: explicit (ISO 3166-1 / ISO 4217 / ISO 10383 MIC), never a generic placeholder.

## Contributor Workflow (CONTRIBUTING.md addition)

> **Calendar reference docs.** Any PR that adds a new `HolidayCalendarService` implementation, or changes an existing one's holiday list, roll strategy, weekend days, or early-close behavior, must add or update the corresponding file in `docs/calendars/<CODE>.md` using the template in `docs/calendars/TEMPLATE.md`. New calendars must also add a row to both tables in `docs/calendars/README.md`. PRs that touch a `HolidayCalendarService*` class, its `impl/`, or `observance/` sources without a corresponding doc change will be asked to update docs before merge.

**Enforcement (revised):** a single provides-line check is insufficient — it only catches new calendars, not changes to existing ones, which is the more common case. The CI check must instead diff the PR's changed files against two sets:
- Java changes: any file under a module's `impl/`, `observance/`, or a `HolidayCalendarService*.java` file, OR a new `provides org.holiday.calendar.HolidayCalendarService` line in `module-info.java` (note: real package is `org.holiday.calendar`, not `org.holiday.calendar.core` — that's the JPMS module name).
- Doc changes: any `docs/calendars/*.md` file.

If the Java-changes set is non-empty and touches a specific calendar's code, the corresponding `docs/calendars/<CODE>.md` must appear in the same diff; fail the check otherwise. Mapping a changed Java file to its calendar CODE(s) can be done via a simple filename/package convention lookup (e.g. class name suffix matches CODE) — this needs a short mapping table built during phase 1, since it's not perfectly mechanical (e.g. shared factories like `IsraelHolidays` feed both `IL` and `ILS`).

No CI validation of table *content* against runtime output (out of scope for a docs issue) — this remains a known gap; sibling-table consistency (e.g. US.md vs USD.md diverging) is a manual PR-review responsibility, not automated.

**Citation standard:** a citation is "good enough" if it links to an official government/exchange/central-bank page or a specific dated PDF/circular. If the source has no stable URL (common for older PDFs), note the retrieval date and, where practical, archive it via a Wayback Machine snapshot link alongside the live link. If no primary source is locatable at all, say so explicitly in the Sources section ("No primary source located as of <date>; verified against `<code>` behavior only") rather than omitting the section — an honest gap is better than a missing one that looks like an oversight.

## Rollout Sequencing (task list)

1. Finalize `docs/calendars/TEMPLATE.md`, create `docs/calendars/README.md` skeleton (empty tables), add CONTRIBUTING.md paragraph, add the revised CI check (Java-changes-to-docs mapping, including the shared-factory edge cases like `IsraelHolidays` → IL/ILS).
2. Pilot #1: `CH` — stress-tests cantonal nuance in "Notes of Interest".
3. Pilot #2: one MENA CSV-backed calendar (e.g. `IL` or `AE`) — stress-tests the Islamic/Hebrew data-ceiling case and dual-code shared-factory documentation, which CH does not exercise. Do not begin mass production until both pilots are reviewed/merged.
4. Western region — remaining 18 files, cross-linking each into the main README's calendar table and `docs/calendars/README.md` as it merges (no end-of-phase gate).
5. APAC region — 7 files, same incremental cross-linking.
6. MENA region — remaining 19 files, same incremental cross-linking.
7. Follow-up issue: dedicated citation-quality audit pass across all files once initial authorship is complete (not a blocker to merging 1–6) — expect inconsistent citation depth from first-pass authoring across many contributors/sessions.

Note on effort: this is a large writing effort — each file requires primary-source research, not just code transcription. Track it as ~50 discrete units of work (47 calendars + template + 2 pilots), not as "3 region phases," so progress and remaining effort stay visible; don't assume a single-sprint timeline.

Phases 4–6 are mutually independent and could parallelize across contributors, but must not start before phases 1–3 land, since the template may still change shape during the pilots.

## Flagged as Over/Under-Engineered

- Over: 3 regional guide files (rejected, see above); a maintained per-file Testing section (rejected).
- Under (now addressed above): explicit CI enforcement for doc drift; explicit file-naming casing (uppercase to match code); explicit home for TEMPLATE.md.
