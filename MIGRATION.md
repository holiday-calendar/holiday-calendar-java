# Migration Guide: v1.4.0 → v2.1.0

This guide covers the breaking changes introduced across **v2.0.0** and **v2.1.0**
for users upgrading from v1.4.0 or earlier. Two separate breaking changes are
involved, in two separate releases:

1. **v2.0.0** added the `EarlyCloseHoliday` API — but at that point, early closes
   were modeled on the *same* national-code calendars that also served as
   equities-exchange calendars (e.g. `US` was still NYSE's calendar).
2. **v2.1.0** (this release) is the architectural fix: national holiday calendars
   and equities-exchange market calendars have been separated (#229). The
   early-close data added in v2.0.0 moves off the national codes and onto new,
   dedicated exchange codes.

If you're upgrading directly from v1.4.0 to v2.1.0, you need both sections below.
If you're already on v2.0.0, you only need the v2.1.0 section.

> [!WARNING]
> **Skip v2.0.0.** It shipped the new `EarlyCloseHoliday` API on top of the still-conflated
> national/market calendars, so codes like `US`, `CA`, `UK`, `AU`, `FR`, `CH`, `DE`,
> and `SG` return an equities exchange's trading calendar under a national code —
> the exact bug v2.1.0 fixes. Any consumer relying on `hasEarlyCloses()` /
> `calculateEarlyCloses()` on v2.0.0 is reading market data off a national code and
> will see behavior change again on the very next upgrade. Go directly from v1.4.0
> (or earlier) to **v2.1.0 or later**.

## Step 1: Update your dependency version

```xml
<dependency>
    <groupId>org.holiday.calendar</groupId>
    <artifactId>holiday-calendar-western</artifactId>
    <version>2.1.0</version>
</dependency>
```

(Substitute `holiday-calendar-apac` / `holiday-calendar-mena` as applicable.)

## v2.0.0: `EarlyCloseHoliday` and the early-close API

v2.0.0 added `EarlyCloseHoliday` as a fourth permitted subtype of the sealed
`Holiday` interface, and changed what `HolidayCalendar.calculate(int year)`
returns. At this point in the library's history, the calendars that gained early
closes were still the conflated national codes — `US`, `CA`, `UK`, `AU`, `FR`,
`CH`, `DE`, `SG` (see the v2.1.0 section below for what happens to those codes
next).

### `Holiday` sealed interface gained a permitted subtype

```java
public sealed interface Holiday permits FixedHoliday, FloatingHoliday, SpecialAnniversary, EarlyCloseHoliday
```

Any exhaustive `switch` over `Holiday` written against v1.x will now fail to compile
until it handles `EarlyCloseHoliday`:

```java
// v1.x code — no longer compiles from v2.0.0 onward (non-exhaustive switch)
for (HolidayDate hd : calendar.calculate(2026)) {
    switch (hd.getHoliday()) {
        case FixedHoliday fh -> System.out.println("Fixed: " + fh.getName());
        case FloatingHoliday fh -> System.out.println("Floating: " + fh.getName());
        case SpecialAnniversary sa -> System.out.println("Anniversary: " + sa.getName());
    }
}

// v2.x code — add the new case
for (HolidayDate hd : calendar.calculate(2026)) {
    switch (hd.getHoliday()) {
        case FixedHoliday fh -> System.out.println("Fixed: " + fh.getName());
        case FloatingHoliday fh -> System.out.println("Floating: " + fh.getName());
        case SpecialAnniversary sa -> System.out.println("Anniversary: " + sa.getName());
        case EarlyCloseHoliday ech -> System.out.println("Early close: " + ech.closeTime());
    }
}
```

### `calculate(int year)` excludes early closes

From v2.0.0 onward, `HolidayCalendar.calculate(int year)` never returns
`EarlyCloseHoliday` entries. Use `calculateEarlyCloses(int year)` to retrieve them,
and `hasEarlyCloses()` to check whether a calendar defines any:

```java
HolidayCalendar us = factory.create("US"); // v2.0.0: still NYSE's calendar

List<HolidayDate> holidays = us.calculate(2026);              // full-day closures only
List<HolidayDate> earlyCloses = us.calculateEarlyCloses(2026); // half-day closures only

if (us.hasEarlyCloses()) {
    for (HolidayDate hd : earlyCloses) {
        EarlyCloseHoliday ech = (EarlyCloseHoliday) hd.getHoliday();
        System.out.printf("%s closes at %s %s%n", hd.getDate(), ech.closeTime(), ech.zoneId());
    }
}
```

### Early closes carry an explicit time zone

`EarlyCloseHoliday.closeTime()` and `.zoneId()` are both non-null. Compare closures
across calendars in each exchange's own local time — don't assume a shared time
zone (e.g. NYSE's 13:00 `America/New_York` vs. LSE's 12:30 `Europe/London` on the
same calendar day).

## v2.1.0: national calendars no longer double as exchange calendars

In v1.x and v2.0.0, seven of the national-code calendars in `holiday-calendar-western`
and `holiday-calendar-apac` actually returned an **equities exchange's** trading
calendar — including the early closes added in v2.0.0 — rather than pure national
public holidays:

| Code | Pre-v2.1.0 identity | Early closes carried (since v2.0.0) |
|---|---|---|
| `US` | NYSE trading calendar | Day-after-Thanksgiving, July 3rd, Christmas Eve |
| `CA` | TSX trading calendar | Christmas Eve |
| `UK` | LSE trading calendar | Christmas Eve, New Year's Eve |
| `AU` | ASX trading calendar | Christmas Eve, New Year's Eve |
| `FR` | Euronext Paris trading calendar | Christmas Eve, New Year's Eve |
| `CH` | SIX trading calendar (no national-only calendar existed) | — |
| `DE` | Xetra trading calendar (no national-only calendar existed) | — |
| `SG` | SGX trading calendar | Christmas Eve, New Year's Eve |

As of v2.1.0, each of these codes now returns **only** the country's national
public holidays, with zero `EarlyCloseHoliday` entries. The exchange-specific
content that used to live under the national code has moved to a **new calendar
code**, named after the exchange's [ISO 10383 Market Identifier Code (MIC)](https://www.iso20022.org/market-identifier-codes):

| National code (unchanged) | New market/exchange code (v2.1.0) | Exchange |
|---|---|---|
| `US` | `XNYS` | New York Stock Exchange |
| `CA` | `XTSE` | Toronto Stock Exchange |
| `UK` | `XLON` | London Stock Exchange |
| `AU` | `XASX` | Australian Securities Exchange |
| `FR` | `XPAR` | Euronext Paris |
| `CH` | `XSWX` | SIX Swiss Exchange |
| `DE` | `XETR` | Xetra (Deutsche Börse) |
| `SG` | `XSES` | Singapore Exchange (SGX) |

**`CH` and `DE` are new in v2.1.0** — no plain national-only calendar existed for
Switzerland or Germany prior to this release, so if your code newly needs
Swiss/German national public holidays, that capability is new rather than moved.

Central-bank / settlement calendars (`USD`, `CAD`, `GBP`, `AUD`, `CHF`, `EUR`,
`SGD`) are **unaffected** — they were already correctly scoped and require no
changes.

### `JP` — renamed only, not a breaking change

`JP` was documented as "Tokyo Stock Exchange" but its actual holiday data, roll
rule, and 国民の休日 (sandwiched-day) logic were always Japan's national Holiday
Act — never TSE-specific, and it never carried any `EarlyCloseHoliday` entries even
after v2.0.0. In v2.1.0 it is redocumented as "Japan National Holidays," with **no
change to the returned dates**. There is no separate Japan exchange calendar;
`JPY` (Bank of Japan) remains the settlement calendar. No code changes required.

### `CN` and MENA calendars — unaffected

`CN` never had a market-calendar conflation. All ten MENA national/settlement pairs
(`AE`/`AED`, `SA`/`SAR`, `IL`/`ILS`, `TR`/`TRY`, `QA`/`QAR`, `EG`/`EGP`, `KW`/`KWD`,
`BH`/`BHD`, `MA`/`MAD`, `JO`/`JOD`) were already correctly separated into national
and settlement/exchange codes before v1.4.0 and require no changes for v2.1.0. No
standalone equities-exchange (MIC-coded) calendar exists yet for any MENA market.

### Migration decision tree

- **"Am I checking whether offices/government/business are closed?"** → Keep using
  the national code (`US`, `SG`, `CA`, …). No change needed.
- **"Am I checking whether an exchange is open or determining trading days?"** → You
  were relying on the pre-v2.1.0 conflation. Switch to the new MIC-coded calendar
  (`XNYS`, `XSES`, `XTSE`, …).
- **"Am I checking for early closes / half-days?"** → Early closes now live only on
  market calendars (with one documented exception below). Use the market code's
  `calculateEarlyCloses(year)`.

### Before / after example

```java
HolidayCalendarFactory factory = new HolidayCalendarFactory();

// Before v2.1.0: 'US' returned NYSE's trading calendar, including early closes.
HolidayCalendar nyse = factory.create("US");                     // ❌ wrong from v2.1.0
List<HolidayDate> closures = nyse.calculate(2026);                // now US NATIONAL holidays
List<HolidayDate> earlyCloses = nyse.calculateEarlyCloses(2026);  // now empty

// v2.1.0: request each concept explicitly.
HolidayCalendar usNational = factory.create("US");             // pure national holidays
HolidayCalendar xnys = factory.create("XNYS");                 // NYSE trading calendar
List<HolidayDate> officeClosures = usNational.calculate(2026);
List<HolidayDate> marketClosures = xnys.calculate(2026);
List<HolidayDate> marketEarlyCloses = xnys.calculateEarlyCloses(2026);
```

## FAQ

**Does this break all my code?**
Only if you used a national code (`US`, `SG`, `JP`, `CA`, `UK`, `AU`, `FR`, `CH`,
`DE`) expecting exchange trading days or early closes, or if you have an exhaustive
`switch` over `Holiday` (breaking since v2.0.0). Office/business-closure checks
using national codes are unaffected.

**What are the new market/exchange calendar codes?**
`XNYS` (NYSE), `XTSE` (TSX), `XLON` (LSE), `XASX` (ASX), `XPAR` (Euronext Paris),
`XSWX` (SIX), `XETR` (Xetra), `XSES` (SGX) — all [ISO 10383 MIC](https://www.iso20022.org/market-identifier-codes)
codes, introduced in v2.1.0. See the README calendar table for the full, current
list.

**Do national calendars ever have early closes in v2.1.0?**
Almost never — `hasEarlyCloses()` is `false` for every national calendar except one
documented exception: `TR` (Turkey) legitimately carries a Republic Day Eve (28
October) early close, because Law No. 2429 declares it a nationwide statutory
half-day for all public institutions — not a Borsa Istanbul-only convention. The
same closure is also carried by `TRY` (BIST/TCMB) for consistency. If you rely on
`hasEarlyCloses() == false` as an invariant for *all* national calendars, special-case
`TR` or check `hasEarlyCloses()`/`calculateEarlyCloses()` directly rather than
assuming.

**How do I get both national holidays and market early closes?**
Use two calendar instances: the national code's `calculate(year)` for office/business
closures, and the market code's `calculateEarlyCloses(year)` for half-days.

**Are early closes considered holidays?**
They're a distinct `Holiday` subtype (`EarlyCloseHoliday`) representing a partial
closure, always non-rollable, and excluded from `calculate(int year)` — use
`calculateEarlyCloses(int year)` to retrieve them.

## Verification checklist

- [ ] Search your codebase for hardcoded calendar codes (`"US"`, `"CA"`, `"UK"`,
      `"AU"`, `"FR"`, `"CH"`, `"DE"`, `"SG"`) and classify each usage as
      office/business-closure (no change) vs. trading/market (switch to the new MIC
      code).
- [ ] Update any exhaustive `switch` over `Holiday` to add an `EarlyCloseHoliday` case.
- [ ] Replace any reliance on `calculate(year)` returning early closes with
      `calculateEarlyCloses(year)` against the appropriate market calendar.
- [ ] Re-run your test suite against v2.1.0 and diff calendar output for each code
      you consume.

## Related issues

- #176 — `EarlyCloseHoliday` type and early-close API (v2.0.0)
- #229 — root architectural redesign, national vs. market separation (v2.1.0)
- #231 (Western), #232 (APAC) — per-module split implementation
- #234–#241 — per-country Western sub-issues
- #233 — TR/TRY early-close placement investigation
