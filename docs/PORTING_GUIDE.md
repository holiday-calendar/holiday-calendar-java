# Porting Guide

This guide documents the design of Holiday Calendar (Java) at a level that lets you
re-implement a compatible library in another language — JavaScript, Python, Go, or
anything else. It is not a language binding or FFI wrapper: a port is a fresh
implementation of the same *model* (data types, algorithms, and file formats), not a
call-through to this Java code.

Read this if you are building `holiday-calendar-js`, `holiday-calendar-py`, etc., or
porting a single regional calendar into an existing non-Java project that needs the
same semantics.

Every code example below is either copied verbatim from this repository or is
pseudocode explicitly marked as such. Class and file names are given so you can check
current behavior against the source rather than trusting this document as it ages.

## Table of contents

1. [Core abstractions](#1-core-abstractions)
2. [Design patterns](#2-design-patterns)
3. [Data organization](#3-data-organization)
4. [Testing patterns](#4-testing-patterns)
5. [The v2.0.0 / v2.1.0 breaking changes](#5-the-v200--v210-breaking-changes)
6. [End-to-end example: USD (Federal Reserve) in Python](#6-end-to-end-example-usd-federal-reserve-in-python)
7. [Porting checklist](#7-porting-checklist)

---

## 1. Core abstractions

### 1.1 `Holiday` — closed set of four holiday kinds

`Holiday` is a Java `sealed interface` — a closed union type. Every holiday in the
library is exactly one of four kinds, chosen by how its date is computed:

| Java type | Date computed by | Rollable by default | Included in `calculate()`? |
|---|---|---|---|
| `FixedHoliday` | Same `MonthDay` (month + day) every year | yes | yes |
| `FloatingHoliday` | An `Observance` function, evaluated per year | configurable | yes |
| `SpecialAnniversary` | A fixed `LocalDate`; only "occurs" in that one year | no (by default) | yes |
| `EarlyCloseHoliday` | An `Observance`, plus a local close time and time zone | always `false` | **no** — see §1.2 |

Source: [`Holiday.java`](../holiday-calendar-core/src/main/java/org/holiday/calendar/Holiday.java).

In a language without sealed interfaces/discriminated unions, model this as:
- **TypeScript**: a discriminated union with a `kind` tag field (`"fixed" | "floating" | "anniversary" | "earlyClose"`), or four classes implementing a common interface plus an exhaustive `switch` helper that TypeScript can narrow on.
- **Python**: an `abc.ABC` base class with four concrete subclasses, or a `dataclass` + `Enum` tag if you prefer data-oriented modeling. `match`/`case` on `isinstance` gives you the exhaustiveness Java's sealed `switch` provides, though Python won't statically enforce it — add a runtime `else: raise AssertionError("unreachable")` in every `match` over `Holiday`.
- **Go**: a small closed interface with an unexported marker method, satisfied by exactly the four struct types in the same package.

Every holiday, regardless of type, exposes the same base contract:

```java
public sealed interface Holiday permits FixedHoliday, FloatingHoliday, SpecialAnniversary, EarlyCloseHoliday {
    String getName();
    String getDescription();
    boolean isRollable();
    Optional<LocalDate> dateForYear(int year);   // empty if not observed that year
}
```

`dateForYear` returning "empty" (not an exception) for a year in which the holiday
doesn't apply is deliberate — it's how a holiday like Juneteenth (a US federal holiday
only from 2021 onward) or a `SpecialAnniversary` (occurs in exactly one year) is
allowed to be silently absent from `calculate()` for out-of-range years, rather than
forcing every caller to catch an exception. Preserve this "empty/None/nil, not throw"
convention in your port — it is relied on throughout the calculation pipeline.

Construction goes through a builder (`Holiday.builder()...build()`), not public
constructors directly, so that one factory method can dispatch on a `Type` enum to the
right concrete class. A port doesn't need to reproduce the builder pattern exactly —
use whatever idiomatic construction your language favors (keyword args in Python,
object literals in TypeScript, functional options in Go) — but keep the same set of
required fields per type, listed in the table above.

### 1.2 `EarlyCloseHoliday` and the two-path API

`EarlyCloseHoliday` (added in v2.0.0) models a **partial** trading day — the exchange
is open but closes at, say, 13:00 instead of the normal close. It carries two extra
fields beyond a normal holiday:

```java
public record EarlyCloseHoliday(String name, String description, Observance observance,
                                 LocalTime closeTime, ZoneId zoneId) implements Holiday {
    @Override
    public boolean isRollable() { return false; }   // always — never weekend-adjusted
    ...
}
```

Two invariants your port must reproduce exactly, because downstream consumers depend
on them:

1. **`isRollable()` is always `false`.** An early close is pinned to a specific
   calendar day (e.g. Christmas Eve always means December 24, never a substitute
   date) — it is never weekend-rolled.
2. **Early closes are excluded from the "regular" holiday calculation and retrieved
   through a separate method.** See §1.3.

`closeTime`/`zoneId` must always be expressed in the exchange's own local time zone —
do not normalize to UTC or to the caller's zone. NYSE's 13:00 `America/New_York` and
LSE's 12:30 `Europe/London` early closes on the same calendar day are not the "same
time" and should never be compared without first converting through the zone.

### 1.3 `HolidayCalendar` — the core API contract

A `HolidayCalendar` is a named, immutable bundle of: a short `code`, a `name`, a
`DateRoll` weekend-adjustment strategy, a set of `weekendDays`, and a set of
`Holiday` objects. Its public contract — the part every port must replicate — is:

```java
List<HolidayDate> calculate(int year);
// Full-day closures for `year`, chronologically sorted, EXCLUDING EarlyCloseHoliday.
// Each holiday's raw date is computed via dateForYear(year); if it falls on a
// weekend AND the holiday isRollable(), the DateRoll strategy adjusts it.

List<HolidayDate> calculateEarlyCloses(int year);
// ONLY EarlyCloseHoliday entries for `year`, chronologically sorted.
// No rolling is applied (isRollable() is always false for these).

boolean hasEarlyCloses();
// Cheap, year-independent check: does this calendar define ANY EarlyCloseHoliday?
// Lets callers branch without calling calculateEarlyCloses().

boolean isWeekendUTC(Instant instant);
// Is this instant, interpreted in UTC, a weekend day per this calendar's weekendDays?

HolidayCalendar merge(HolidayCalendar other);
// Union of two calendars: holidays, weekendDays combined; codes/names concatenated
// with "/" and " + "; the merged DateRoll applies `other`'s roll first, then this one's.
```

Two overloads worth including in a port even though the issue tracker doesn't call
them out as strictly required — real consumers use them heavily:

```java
List<HolidayDate> calculate(int fromYear, int toYear);          // flattened, sorted
Map<Integer, List<HolidayDate>> calculateByYear(int fromYear, int toYear); // dense — every year present, even if empty
```

**The single most important behavioral rule to port correctly** is the split between
`calculate()` and `calculateEarlyCloses()`. This was a breaking change introduced in
v2.0.0 specifically because early closes are a different *kind* of thing (a half day,
not a closure) and conflating them silently changed what `calculate()` meant for
existing consumers. See §5 for the full history.

Source: [`HolidayCalendar.java`](../holiday-calendar-core/src/main/java/org/holiday/calendar/HolidayCalendar.java).

### 1.4 `HolidayDate` — the resolved (holiday, date) pair

```java
public record HolidayDate(Holiday holiday, LocalDate date) { }
```

A simple immutable pair: which `Holiday` definition produced this entry, and the
*actual, resolved* `LocalDate` for the year in question (post date-rolling for regular
holidays; the raw `Observance` result for early closes, which are never rolled). Model
this as a plain value/record type in your port — a tuple, frozen dataclass, or
readonly struct, whichever your language's idiom is for "small immutable pair."

### 1.5 `Observance` — the floating-date plug-in point

```java
@FunctionalInterface
public interface Observance extends Function<Integer, LocalDate>, Predicate<Integer> {
    // apply(year) -> LocalDate: the computed date, or null if not observed that year
    // test(year)  -> boolean:   default true; override to restrict validity range
}
```

This is the extension point for "how is this floating holiday's date computed for a
given year?" Three implementation strategies appear throughout the codebase, and a
port should support all three as the same interface/protocol:

1. **Pure algorithm** — e.g. `WesternEaster` implements the Butcher/Jones/Meeus
   Computus algorithm directly in code; every year is valid, there is no data file.
2. **Nth-weekday-of-month rule** — e.g. `MartinLutherKingJrDay`:
   ```java
   public class MartinLutherKingJrDay implements Observance {
       @Override
       public LocalDate apply(Integer year) {
           if (!test(year)) return null;
           return Year.of(year).atMonth(Month.JANUARY).atDay(1)
                      .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY));
       }
       @Override
       public boolean test(Integer year) { return 1986 <= year; }  // first observed 1986
   }
   ```
   In your language, use whatever date library gives you "nth weekday of month"
   (Python: `dateutil.rrule` or hand-rolled arithmetic; JS: `date-fns` or manual;
   Go: manual — `time.Date` + weekday offset math).
3. **CSV-backed lookup table with astronomical fallback** — for non-Gregorian
   calendars (Islamic, Chinese lunar). See §2.2 and §2.3.

A base class worth porting is `AbstractObservance`, which centralizes the "null year
in → null out, delegate validity to a hook" boilerplate:

```java
public abstract class AbstractObservance implements Observance {
    @Override
    public final LocalDate apply(Integer year) {
        return (year != null && isValidYear(year)) ? computeDate(year) : null;
    }
    @Override
    public final boolean test(Integer year) {
        return year != null && isValidYear(year);
    }
    protected abstract LocalDate computeDate(int year);
    protected boolean isValidYear(int year) { return true; }  // override to restrict
}
```

### 1.6 `DateRoll` — the weekend-adjustment strategy

```java
@FunctionalInterface
public interface DateRoll {
    LocalDate rollToObservedDate(LocalDate dateToRoll);
}
```

A single-method strategy interface — a plain function type in most target languages.
`HolidayCalendar.calculate()` calls it only when a holiday's raw date falls on a
weekend day **and** the holiday `isRollable()`. The library ships these named
strategies (`DateRolls` factory class); reproduce all of them, since real regional
calendars use every one:

| Factory method | Rule | Used by (examples) |
|---|---|---|
| `noRoll()` | No adjustment, ever | SIC (Swiss settlement) |
| `previousFridayOrFollowingMonday()` | Sat → preceding Fri; Sun → following Mon | US Federal Reserve, most Western markets |
| `followingMonday()` | Sat and Sun both → following Mon | CAD (Bank of Canada) |
| `sundayToMonday()` | Only Sun → following Mon; Sat unchanged | Japan (国民の祝日 substitute-holiday rule) |
| `followingSunday()` | Fri and Sat both → following Sun | GCC markets (Friday+Saturday weekend: SA, AE, KW, BH) |
| `previousThursdayOrFollowingSunday()` | Fri → preceding Thu; Sat → following Sun | Qatar |

Two rolls can be composed — `DateRolls.compose(first, second)` or the instance method
`DateRoll.andThen(after)` — which `HolidayCalendar.merge()` uses to combine two
calendars' rolls. Port at least a "compose two rolls in sequence" helper even if you
don't need every named strategy on day one.

### 1.7 `HolidayCalendarService` — the plug-in/extension contract

```java
public interface HolidayCalendarService {
    boolean isProvided(String code);
    HolidayCalendar getHolidayCalendar();
    default String getCode() { return null; }
    default String getRegion() { return null; }
    default OptionalInt dataValidThrough() { return OptionalInt.empty(); }
}
```

One implementation per calendar code (`US`, `XNYS`, `SG`, `TR`, …). `getCode()` /
`getRegion()` are metadata; `getHolidayCalendar()` builds and returns the actual
`HolidayCalendar`. `dataValidThrough()` is an *advisory* signal — if any holiday in
the calendar is backed by a finite lookup table (see §2.2), this returns the last year
that table covers; callers who need a hard guarantee should check it before requesting
years beyond it, since `calculate()` silently omits table-backed holidays past that
point rather than throwing.

`AbstractHolidayCalendarService` factors out the `code`/`name` bookkeeping so concrete
services only implement `getHolidayCalendar()`:

```java
public abstract class AbstractHolidayCalendarService implements HolidayCalendarService {
    protected AbstractHolidayCalendarService(String code, String name) { ... }
    @Override public boolean isProvided(String code) { return this.code.equalsIgnoreCase(code); }
    @Override public String getCode() { return code; }
    @Override public String getRegion() { return name; }
}
```

## 2. Design patterns

### 2.1 Plugin / ServiceLoader pattern

New regional calendars are added **without modifying core code** — this is the
central extensibility promise of the library. In Java this is done with
`java.util.ServiceLoader`:

1. Each module lists its `HolidayCalendarService` implementations in
   `META-INF/services/org.holiday.calendar.HolidayCalendarService` (one fully-qualified
   class name per line), *and* mirrors the same list in a `provides ... with ...`
   clause in `module-info.java` (required for JPMS module resolution):

   ```java
   // holiday-calendar-western/src/main/java/module-info.java
   module org.holiday.calendar.western {
       requires org.holiday.calendar.core;
       exports org.holiday.calendar.observance.christian;
       exports org.holiday.calendar.observance.us;
       // ... one exports per observance sub-package ...

       provides org.holiday.calendar.HolidayCalendarService with
           org.holiday.calendar.impl.HolidayCalendarServiceUS,
           org.holiday.calendar.impl.HolidayCalendarServiceUSD,
           org.holiday.calendar.impl.HolidayCalendarServiceXNYS,
           // ... one entry per calendar code in this module ...
           org.holiday.calendar.impl.HolidayCalendarServiceXSWX;
   }
   ```

2. `HolidayCalendarFactory` discovers every registered service at runtime and picks
   the one whose `isProvided(code)` matches:

   ```java
   public HolidayCalendar create(String code) {
       return cache.computeIfAbsent(code, k -> getService(k).getHolidayCalendar());
   }
   public HolidayCalendarService getService(String code) {
       return StreamSupport.stream(serviceLoader.spliterator(), false)
           .filter(service -> service.isProvided(code))
           .findFirst()
           .orElseThrow(() -> new HolidayCalendarNotFoundException(code, listAvailableCodes()));
   }
   ```

Java's `ServiceLoader` is a JVM-specific mechanism, but the *pattern* — a registry that
discovers implementations without the core module importing them — ports cleanly:

- **Python**: [entry points](https://packaging.python.org/en/latest/specifications/entry-points/)
  declared in each package's `pyproject.toml` (`[project.entry-points."holiday_calendar.services"]`),
  discovered at runtime via `importlib.metadata.entry_points(group=...)`.
- **JavaScript/TypeScript**: no built-in equivalent; use an explicit registration
  function each regional package calls on import (`registerCalendar(service)`), or a
  well-known `package.json` field your factory scans `node_modules` for.
- **Go**: package-level `init()` functions that call a `Register(service)` function
  exported by the core package — the standard Go idiom for this pattern (see
  `database/sql`'s driver registry, or `image`'s format registry).

Whatever mechanism you choose, preserve the two properties that make this pattern
useful: (1) the core module never imports a specific regional module, and (2) adding a
new calendar means adding a new file/package, not editing an existing one.

Cache calendar instances by code (as `HolidayCalendarFactory` does with a
`ConcurrentHashMap`) — building a `HolidayCalendar` means constructing potentially
dozens of `Holiday`/`Observance` objects, and callers frequently request the same code
repeatedly.

### 2.2 CSV-backed lookup tables for non-Gregorian observances

Islamic-calendar and Chinese lunar-calendar holidays are not the output of a clean
year → date function — they depend on lunar sightings, regional astronomical rules,
and (for Turkey) a religious authority's published calendar. The library stores these
as CSV files loaded at class-load time.

**CSV format** (from `CsvObservanceLoader`, which every non-Gregorian `Observance`
delegates to):

```
year,YYYY-MM-DD[,optional comment]
```

- Blank lines and lines starting with `#` are ignored (used for header/source
  commentary — see the sample below).
- Malformed rows (bad year, bad date, fewer than 2 fields) are logged and **skipped**,
  not fatal — one bad row doesn't take down the whole table.
- `loadSingle()` returns `Map<year, date>` (last row wins on duplicate year);
  `loadMultiple()` returns `Map<year, List<date>>` for holidays that can recur more
  than once in a year.

Example, `eid-al-fitr-tr.csv` (comment header trimmed for brevity):

```csv
# Ramazan Bayramı (Eid al-Fitr, 1 Shawwal) — Turkey public holiday dates
# SOURCE TIERS:
#   2024-2035: Diyanet officially-published dates
#   2036-2055: Computed via IlmiTakvimCalculator (see below)
2024,2024-04-10
2025,2025-03-30
2026,2026-03-20
```

Port this as: a plain-text (or JSON, if you'd rather not hand-roll CSV parsing) data
file per holiday-per-country, bundled as a package resource, loaded once and cached —
not re-parsed on every call. Keep the "skip malformed rows, log a warning" behavior;
don't let one bad data row throw for the whole calendar.

### 2.3 Astronomical calculators for complex observances

Two examples worth understanding before you port either:

**Easter (Computus)** — `WesternEaster` implements the Butcher/Jones/Meeus algorithm
directly (no lookup table; every Gregorian year is computable). It falls back to
`OrthodoxEaster`'s (Julian-calendar-based) computation for years before 1583, since
the Gregorian calendar didn't exist yet. This is pure integer arithmetic — trivially
portable to any language; see [`WesternEaster.java`](../holiday-calendar-western/src/main/java/org/holiday/calendar/observance/christian/WesternEaster.java)
for the exact steps (`a` through `p` intermediate variables, all integer division).

**Diyanet ilmi takvim (Turkish scientific calendar)** — `IlmiTakvimCalculator`
computes Eid al-Fitr / Eid al-Adha dates for years beyond Diyanet's own published CSV
range (2036–2055 as of this writing). Methodology: a Hijri month begins the day after
the true (perturbed) astronomical lunar conjunction at Ankara, computed via a
"new moon" ephemeris function — unless the conjunction falls after Ankara's local
sunset, in which case the month starts two days later instead of one. The calculator
walks forward/backward one full Hijri year (12 lunations, ~354.37 days) at a time from
a verified anchor date, re-snapping to the true conjunction at each step, rather than
computing each target year independently — this avoids compounding rounding error
from repeatedly adding a mean synodic-month estimate.

This is the one part of the library that depends on a real astronomical ephemeris
(moon-phase and sunset calculations), not just calendar arithmetic — see §2.4 for what
that means for your Time4J dependency in a port.

**Fallback pattern, applicable everywhere in the library**: CSV/lookup-table data
always takes precedence when it's present for a year; an algorithmic calculator (or a
hard error) is used only for years beyond the table's coverage. Never let an
algorithmic fallback silently override officially-published data for a year the table
already covers — this is why `IlmiTakvimCalculator` is calibrated to *exactly*
reproduce all 24 Diyanet-published dates from 2024–2035 before being trusted for
2036+.

### 2.4 Time4J dependency (Java-specific) and what a port needs instead

This library uses [Time4J](https://github.com/MenoData/Time4J) for two things Java's
built-in `java.time` doesn't provide: (1) a Chinese lunisolar calendar
(`net.time4j.calendar.ChineseCalendar`) for Chinese New Year, and (2) an astronomical
ephemeris (`net.time4j.calendar.astro.MoonPhase`, `SolarTime`) for the Diyanet
calculator's true-conjunction and sunset calculations.

A port needs equivalents only if it implements APAC lunar holidays or extends the
Turkish calculator beyond its current CSV range — not for the Western/MENA CSV-backed
holidays, which need no ephemeris at all (they read pre-computed dates from CSV). Look
for:

- **Python**: [`skyfield`](https://rhodesmill.org/skyfield/) or
  [`astropy`](https://www.astropy.org/) for lunar-phase/solar ephemeris;
  [`convertdate`](https://github.com/fitnr/convertdate) or
  [`lunarcalendar`](https://pypi.org/project/LunarCalendar/) for Chinese calendar
  conversion.
- **JavaScript**: [`lunar-javascript`](https://github.com/6tail/lunar-javascript) for
  Chinese calendar; [`astronomy-engine`](https://github.com/cosinekitty/astronomy) for
  moon-phase/solar-position ephemeris.
- **Go**: no single dominant library as of this writing — expect to either bind to a
  C ephemeris library or port the ephemeris math (Meeus' *Astronomical Algorithms* is
  the reference both Time4J and most alternatives ultimately implement) by hand.

Whatever you choose, **prefer CSV/lookup-table data over live ephemeris computation
for any year within the published data's range** (recommended: 2026–2055, matching
this library's own `dataValidThrough()` convention — see §1.7 and §4.1) and reserve
ephemeris calculation for years beyond it. This keeps your port's day-to-day behavior
independent of ephemeris library correctness and matches how this library treats its
own astronomical fallback as a last resort, not a primary source.

## 3. Data organization

### 3.1 Regional module boundaries

The Java project splits calendars into three modules by region, each with its own
`observance` sub-packages:

- **Western** (`holiday-calendar-western`): US, USD, CA, CAD, UK, GBP, CH, CHF, DE,
  EUR, FR, AU, AUD, plus the MIC-coded exchange calendars XNYS, XTSE, XLON, XASX,
  XPAR, XETR, XSWX.
- **APAC** (`holiday-calendar-apac`): SG, SGD, XSES, JP, JPY, CN, CNY. Uses Time4J for
  the Chinese lunisolar calendar and lookup tables for Vesak Day / Hari Raya /
  Deepavali.
- **MENA** (`holiday-calendar-mena`): AE, AED, SA, SAR, IL, ILS, TR, TRY, QA, QAR, EG,
  EGP, KW, KWD, BH, BHD, MA, MAD, JO, JOD. Uses CSV-backed Islamic-calendar lookup
  tables with the Diyanet ilmi takvim astronomical fallback described in §2.3.

A port doesn't have to preserve this exact three-way split — pick module boundaries
that fit your language's packaging conventions (npm scopes, PyPI extras, Go modules).
What's worth preserving is the *principle*: each region's `Observance` implementations
live in their own namespace/sub-package, distinct from the generic `function`/
`observance` abstractions in core, so a consumer that only needs, say, MENA calendars
doesn't have to pull in Chinese-calendar or Easter-computation code.

### 3.2 National vs. market/central-bank calendar pairs — and why they must not be conflated

This is the single most important data-modeling decision to get right in a port, and
the library got it *wrong* until v2.1.0 (see §5 for the full history). The rule now
enforced throughout the codebase:

- **ISO 3166-1 alpha-2 country code** (`US`, `JP`, `CA`, …) → the country's national
  public holidays *only*. No market-convention closures, no early closes that aren't
  a genuine nationwide statutory half-day (Turkey's `TR` is the one documented
  exception — see the FAQ in `MIGRATION.md`).
- **ISO 4217 currency code or ISO 10383 Market Identifier Code** (`USD`, `XNYS`, `SGD`,
  `XSES`, …) → a specific market/settlement system's trading calendar, which may add
  market-convention-only closures (e.g. NYSE observes Good Friday; the US federal
  government does not) and/or early closes.

**Do not model a single calendar object as serving both purposes** — that conflation
is exactly the bug #229 in this repository's issue tracker fixed. If your port only
implements one country's holidays to start, still pick a code that signals which kind
it is (national vs. market), so that adding the other one later doesn't require
renaming the first.

### 3.3 Observance sub-packages by domain, not just region

Within a region, observances are further grouped by *domain* — this makes shared logic
(e.g. all Easter-derived holidays across every Western country) discoverable in one
place rather than duplicated per country:

- `observance.christian` — `WesternEaster`, `OrthodoxEaster`, and `CompositeObservance`
  subclasses like `GoodFriday`, `EasterMonday` that compute an offset from a shared
  Easter observance.
- `observance.islamic.mena` — Eid al-Fitr, Eid al-Adha, Islamic New Year, Ashura;
  CSV-backed, described in §2.2.
- `observance.lunar` (APAC) — Chinese New Year and related lunar-calendar dates.
- `observance.us`, `observance.jp`, `observance.eg`, `observance.qa`, `observance.hebrew`,
  `observance.hindu`, `observance.islamic.apac` — single-country-specific rules that
  don't generalize (e.g. `MartinLutherKingJrDay`, Japan's substitute-holiday logic).

`CompositeObservance` is worth porting as a pattern even if you don't port the exact
class: it lets a holiday be defined as "N days offset from another `Observance`"
(`GoodFriday` = Easter − 2 days) without recomputing the base algorithm.

### 3.4 CSV data format, extended

Beyond the format itself (§2.2), two conventions matter for maintainability in a port:

1. **Every CSV file's header comment documents its source tiers** — which year range
   came from an official publication vs. an algorithmic projection, and which
   publication/algorithm. See the `eid-al-fitr-tr.csv` excerpt in §2.2. Carry this
   convention forward; it is what lets a future maintainer (or you, a year later)
   trust or distrust a given date without archaeology.
2. **Extending beyond published data**: when official data runs out, either (a) throw
   or return "no data" for that year (safe default — matches
   `HolidayCalendarService#dataValidThrough()`'s advisory-only contract, since
   `calculate()` silently omits the holiday rather than erroring), or (b) add an
   algorithmic fallback calibrated to reproduce every published year exactly before
   trusting it for new years (the Diyanet ilmi takvim approach, §2.3). Option (b) is
   more work and only worth it if you have a verified rule to calibrate against —
   don't guess at a projection algorithm without one.

## 4. Testing patterns

### 4.1 30-year integration tests (2026–2055)

The acceptance test for "does this port actually work end-to-end" is a single
integration suite that runs every calendar code through a fixed 30-year range and
checks structural invariants — not exact dates (those are covered by per-holiday unit
tests), but shape:

```java
private static final int FROM_YEAR = 2026;
private static final int TO_YEAR   = 2055;   // 30 years

@Test(dataProvider = "allCalendarCodes")
public void testRangeCalculationProducesResult(String code) {
    HolidayCalendar calendar = new HolidayCalendarFactory().create(code);
    Map<Integer, List<HolidayDate>> byYear = calendar.calculateByYear(FROM_YEAR, TO_YEAR);
    assertEquals(byYear.size(), 30);                 // dense: every year present
    for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
        assertTrue(byYear.containsKey(year));
    }
}
```

Reproduce these four checks (see
[`HolidayCalendar30YearIT.java`](../tests/src/test/java/org/holiday/calendar/HolidayCalendar30YearIT.java)
for the full Java suite, including per-calendar early-close count assertions) against
**every calendar code your port implements**:

1. **Range calculation succeeds and is dense** — `calculateByYear` returns exactly one
   entry per year in range, with no missing keys.
2. **Minimum holiday count per year** — a cheap floor (the Java suite uses 5) that
   catches silent data loss (e.g. a broken CSV load returning zero rows) without
   needing an exact expected count per calendar.
3. **No nulls** — no null/undefined/None holiday, date, or list entry anywhere in a
   30-year flat result.
4. **Chronological ordering** — the flat `calculate(fromYear, toYear)` list is
   non-decreasing by date throughout. Note a rolled date can land in the *following*
   year's January (e.g. a Dec 31 holiday rolling to Jan 1) — this is expected and your
   ordering check should tolerate it, not treat it as a bug.

Why 2026–2055 specifically: this is the range this project's own CSV lookup tables and
astronomical calibrations are verified against (see `dataValidThrough()`, §1.7). Pick
whatever forward-looking range your own data actually covers — the point is picking
one fixed range and running every calendar through it, not this exact span.

### 4.2 Parametrized/data-driven tests

TestNG's `@DataProvider` supplies one row per calendar code to a single test method,
rather than writing N nearly-identical test methods. Whatever your language's testing
framework calls this (`pytest.mark.parametrize`, `it.each` in Jest, table-driven tests
in Go), use it for the same reason: the 30-year suite above, and any "verify every
holiday type is representable" suite, should be one parametrized test per invariant,
not copy-pasted per calendar.

Test each `Holiday` subtype explicitly at least once:
- **Fixed**: assert the date equals `LocalDate.of(year, month, day)` for several years,
  including a roll case (the fixed date lands on a weekend).
- **Floating**: assert against known correct historical dates (Easter 2026 is April 5;
  MLK Day 2026 is January 19) rather than re-deriving the algorithm in the test.
- **Early close**: assert `isRollable()` is always `false`, and that the date/time/zone
  triple matches expectations — including the "presence correlates with the anchor
  holiday's day-of-week" pattern used for suppressed-not-shifted early closes (see
  the XNYS/XTSE tests referenced below).

### 4.3 Verification checks beyond the four structural ones

- **Timezone consistency for early closes**: assert `zoneId` is the *exchange's* zone,
  not UTC and not the test machine's default zone — a classic latent bug is a
  hardcoded `LocalTime` compared without its `ZoneId`, which silently breaks the first
  time a test runs in a different CI timezone.
- **Suppressed-not-shifted vs. shifted-not-suppressed early closes**: some markets
  simply omit an early close when its date lands on a weekend (NYSE, TSX — "count
  varies 1–3 per year, re-derive expected presence from the anchor date's
  day-of-week rather than hand-fixturing it"); others shift it like a normal holiday
  (LSE — "count is always exactly 2 per year, the shift-to-preceding-Friday rule never
  suppresses"). Get this distinction wrong and your 30-year test will intermittently
  fail depending on which years' weekday alignments you happened to hand-pick as
  fixtures — which is precisely why the Java suite re-derives expected presence from
  a day-of-week rule inside the loop instead of hardcoding a fixture per year.
- **Cross-list date collisions**: a rolled regular holiday can land on the same date as
  an unrelated early close (e.g. Christmas Day rolling back onto December 24 in a
  year where December 25 is a Saturday, colliding with a Christmas Eve early close).
  Assert your port handles this as two independent `HolidayDate` entries on the same
  date, not a silent drop of one.

### 4.4 Edge cases

- **Leap years** — a `FixedHoliday` on Feb 29 (rare, but plan for it: `dateForYear`
  needs to either return empty for non-leap years or use a `MonthDay` that Java's
  `LocalDate.of` handles by throwing — decide explicitly, don't let it be an accident).
- **Year boundaries** — a holiday rolled from Dec 31 to Jan 1 of the *next* year (or
  vice versa) must still sort correctly in a flattened multi-year list, and must not
  silently disappear when a caller only requests one of the two adjacent years via
  `calculate(int year)` — that's an intentional per-year snapshot, not a bug, but your
  port's documentation should be explicit that a single-year `calculate()` call can
  miss a rolled-in holiday from the *previous* year and can produce a rolled-out date
  in the *next* year.
- **Cascading substitutes (Japan's 振替休日)** — the hardest edge case in the whole
  library, worth understanding in detail if you port `JP`:
  ```java
  // JapaneseHolidayCalendar.calculate(year), simplified:
  // 1. Compute base holidays (superclass calculate()).
  // 2. Cascade: if a holiday falls on Sunday and its naive Monday substitute is
  //    already occupied by ANOTHER holiday, advance day-by-day to the next
  //    weekday that is itself unoccupied and not a weekend. (Only Sunday-origin
  //    substitutes cascade; a Saturday holiday has no substitute at all, per the
  //    2007 Holiday Act amendment.)
  // 3. Sandwich rule (国民の休日): AFTER cascading, scan consecutive holiday pairs;
  //    if exactly one non-holiday weekday sits between them, inject it as a
  //    synthetic "National Holiday".
  ```
  The ordering matters: cascade resolution must run *before* sandwich detection, so a
  cascaded date is visible to the sandwich scan. This is implemented as a
  `HolidayCalendar` subclass overriding `calculate(int)` (see
  [`JapaneseHolidayCalendar.java`](../holiday-calendar-apac/src/main/java/org/holiday/calendar/impl/JapaneseHolidayCalendar.java))
  rather than as a `DateRoll` strategy, because it needs visibility into *all* of the
  year's holidays simultaneously — a `DateRoll` only ever sees one date at a time and
  can't detect "is this Monday already taken by another holiday."

## 5. The v2.0.0 / v2.1.0 breaking changes

Two separate breaking changes, in two separate releases, are worth understanding
before you port — not because your port needs to reproduce the *migration path*, but
because the *end state* they arrived at is the correct target to port directly,
skipping the intermediate (buggy) v2.0.0 shape entirely.

**v2.0.0** added `EarlyCloseHoliday` as a fourth permitted `Holiday` subtype and
changed `calculate(int year)` to exclude it, adding `calculateEarlyCloses(int year)`
as the separate retrieval path (§1.2, §1.3). At this point, though, the early closes
were still hung on national-code calendars that were *actually* exchange trading
calendars under a misleading national-sounding code (`US` returned NYSE's calendar,
not US federal holidays).

**v2.1.0** (issue #229) is the fix: national holiday calendars and equities-exchange
market calendars were split into genuinely separate codes (§3.2). Seven Western codes
and one APAC code changed meaning:

| National code (unchanged meaning) | New market/exchange code | Exchange |
|---|---|---|
| `US` | `XNYS` | New York Stock Exchange |
| `CA` | `XTSE` | Toronto Stock Exchange |
| `UK` | `XLON` | London Stock Exchange |
| `AU` | `XASX` | Australian Securities Exchange |
| `FR` | `XPAR` | Euronext Paris |
| `CH` | `XSWX` | SIX Swiss Exchange (no pre-existing national-only calendar) |
| `DE` | `XETR` | Xetra / Deutsche Börse (no pre-existing national-only calendar) |
| `SG` | `XSES` | Singapore Exchange (SGX) |

`JP` was **not** part of this split — its data was always genuinely Japan's national
Holiday Act, never TSE-specific, and it never carried early closes even after v2.0.0;
it was simply redocumented. `CN` and all ten MENA national/settlement pairs were
already correctly separated and needed no change. Central-bank/settlement calendars
(`USD`, `CAD`, `GBP`, `AUD`, `CHF`, `EUR`, `SGD`) were unaffected throughout — they
were correctly scoped from the start.

**Recommendation for new ports: implement the v2.1.0 shape directly.** Give a national
code (`US`, `JP`, …) only genuine national public holidays, and give any market
calendar its own distinct code (prefer ISO 10383 MIC codes like `XNYS` for equities
exchanges, or the currency's ISO 4217 code for a central-bank/settlement calendar like
`USD`) from the start. There is no reason for a new port to reproduce the v2.0.0
conflation even transiently — it was a bug, not a stepping-stone design.

If you're building a port specifically to consume or mirror this Java library's own
released versions, see `MIGRATION.md` at the repository root (added alongside issue
#224) for the full consumer-facing migration guide — code examples, FAQ, and a
verification checklist for upgrading from v1.4.0. That document is written for *users
upgrading a dependency version*, whereas this section is written for *implementers
deciding a new port's initial data model*.

## 6. End-to-end example: USD (Federal Reserve) in Python

This walks through porting one real calendar — `USD`, the US Federal Reserve's
Fedwire/RTGS settlement holiday calendar — end to end, from the actual Java source
([`HolidayCalendarServiceUSD.java`](../holiday-calendar-western/src/main/java/org/holiday/calendar/impl/HolidayCalendarServiceUSD.java))
to idiomatic Python. It covers all three `Holiday` subtypes actually used by a real
calendar (`FixedHoliday`, `FloatingHoliday`; USD has no early closes — see XNYS in the
Java source for an `EarlyCloseHoliday` example) and the
`previousFridayOrFollowingMonday` roll rule.

**Core types** (§1.1, §1.4–1.6, translated):

```python
from abc import ABC, abstractmethod
from dataclasses import dataclass
from datetime import date, timedelta
from typing import Callable, Optional

class Holiday(ABC):
    name: str
    description: str
    @abstractmethod
    def is_rollable(self) -> bool: ...
    @abstractmethod
    def date_for_year(self, year: int) -> Optional[date]: ...

@dataclass(frozen=True)
class FixedHoliday(Holiday):
    name: str
    description: str
    month: int
    day: int
    rollable: bool = True
    def is_rollable(self) -> bool: return self.rollable
    def date_for_year(self, year: int) -> Optional[date]:
        return date(year, self.month, self.day)

# Observance: Callable[[int], Optional[date]] is enough for the common case;
# wrap in a class when you also need a validity predicate (test()) distinct
# from "returns None for invalid years" (AbstractObservance's behavior, §1.5).
Observance = Callable[[int], Optional[date]]

@dataclass(frozen=True)
class FloatingHoliday(Holiday):
    name: str
    description: str
    observance: Observance
    rollable: bool = True
    def is_rollable(self) -> bool: return self.rollable
    def date_for_year(self, year: int) -> Optional[date]:
        return self.observance(year)

@dataclass(frozen=True)
class HolidayDate:
    holiday: Holiday
    date: date

DateRoll = Callable[[date], date]

def previous_friday_or_following_monday() -> DateRoll:
    def roll(d: date) -> date:
        if d.weekday() == 5:    # Saturday -> preceding Friday
            return d - timedelta(days=1)
        if d.weekday() == 6:    # Sunday -> following Monday
            return d + timedelta(days=1)
        return d
    return roll
```

See the Java original in `DateRolls.java` (§1.6) for the analogous per-weekday logic
to replicate for the other five roll strategies (`followingMonday`, `sundayToMonday`,
`followingSunday`, `previousThursdayOrFollowingSunday`, `noRoll`).

**`HolidayCalendar.calculate()`**, the core algorithm (§1.3), translated directly:

```python
@dataclass
class HolidayCalendar:
    code: str
    name: str
    date_roll: DateRoll
    weekend_days: set[int]        # Python weekday(): Mon=0 ... Sun=6
    holidays: list[Holiday]

    def calculate(self, year: int) -> list[HolidayDate]:
        result = []
        for holiday in self.holidays:
            raw = holiday.date_for_year(year)
            if raw is None:
                continue
            if holiday.is_rollable() and raw.weekday() in self.weekend_days:
                observed = self.date_roll(raw)
            else:
                observed = raw
            result.append(HolidayDate(holiday, observed))
        return sorted(result, key=lambda hd: hd.date)
```

**The observances USD needs** — nth-weekday-of-month (MLK Day, §1.5 pattern #2) and a
year-gated fixed-ish date (Juneteenth, federal holiday only from 2021 onward):

```python
from calendar import monthrange

def nth_weekday_of_month(year: int, month: int, weekday: int, n: int) -> date:
    """weekday: Mon=0..Sun=6. n: 1-indexed occurrence."""
    first = date(year, month, 1)
    offset = (weekday - first.weekday()) % 7
    return date(year, month, 1 + offset + 7 * (n - 1))

def martin_luther_king_jr_day(year: int) -> Optional[date]:
    if year < 1986:
        return None
    return nth_weekday_of_month(year, 1, 0, 3)   # 3rd Monday in January

def juneteenth(year: int) -> Optional[date]:
    return date(year, 6, 19) if year >= 2021 else None

def last_monday_of_may(year: int) -> date:
    last_day = date(year, 5, monthrange(year, 5)[1])
    return last_day - timedelta(days=(last_day.weekday() - 0) % 7)
```

**Assembling the calendar** — directly mirrors the Java `HolidayCalendarServiceUSD`
(§6 source link above), field for field:

```python
def build_usd_calendar() -> HolidayCalendar:
    holidays = [
        FixedHoliday("New Year's Day", "First day of new year in the Common Era (CE)", 1, 1),
        FloatingHoliday("Martin Luther King Jr. Day",
                         "Observed birthday of Martin Luther King, Jr.",
                         martin_luther_king_jr_day, rollable=False),
        FloatingHoliday("Presidents' Day", "Commemoration of Presidents of the United States",
                         lambda y: nth_weekday_of_month(y, 2, 0, 3), rollable=False),
        FloatingHoliday("Memorial Day", "Commemoration of fallen service members of US armed forces",
                         lambda y: last_monday_of_may(y), rollable=False),
        FloatingHoliday("Juneteenth", "Commemoration of emancipation of African-American slaves",
                         juneteenth, rollable=True),
        FixedHoliday("Independence Day", "Celebration of US Declaration of Independence", 7, 4),
        FloatingHoliday("Labor Day", "US Labor Day",
                         lambda y: nth_weekday_of_month(y, 9, 0, 1), rollable=False),
        FloatingHoliday("Columbus Day", "Anniversary of the arrival of Christopher Columbus in the Americas",
                         lambda y: nth_weekday_of_month(y, 10, 0, 2), rollable=False),
        FixedHoliday("Veterans Day", "Commemoration of all US veterans of foreign wars", 11, 11),
        FloatingHoliday("Thanksgiving", "Day to give thanks",
                         lambda y: nth_weekday_of_month(y, 11, 3, 4), rollable=False),  # 4th Thursday
        FixedHoliday("Christmas Day", "Celebration of traditional Christmas holiday", 12, 25),
    ]
    return HolidayCalendar(
        code="USD",
        name="United States (Federal Reserve) Holidays",
        date_roll=previous_friday_or_following_monday(),
        weekend_days={5, 6},   # Saturday, Sunday
        holidays=holidays,
    )
```

Note what's deliberately **absent** compared to the `XNYS` (NYSE) calendar in the Java
source: no Good Friday, no Day-After-Thanksgiving early close — both are NYSE-only
market conventions, not Federal Reserve settlement holidays. This is the national-vs-
market distinction from §3.2/§5 showing up directly in which holidays two same-country
calendars include.

**Service registration** (§2.1 — using the Python entry-points pattern):

```toml
# pyproject.toml, in the package providing this calendar
[project.entry-points."holiday_calendar.services"]
USD = "holiday_calendar_western.usd:build_usd_calendar"
```

```python
# core factory
from importlib.metadata import entry_points

def create_calendar(code: str) -> HolidayCalendar:
    for ep in entry_points(group="holiday_calendar.services"):
        if ep.name == code:
            return ep.load()()
    raise CalendarNotFoundError(code, available_codes())
```

This is enough to reproduce `calculate(2026)` for USD and get results matching the
Java implementation date-for-date. From here, a 30-year integration test (§4.1) run
against this calendar is the concrete way to verify your port's `calculate()`,
`calculateByYear()`, and roll logic all agree with the Java original before you move
on to the next calendar.

## 7. Porting checklist

Use this to sanity-check a new regional-calendar port, or a full new-language port,
against the source of truth in this repository.

- [ ] All four `Holiday` kinds modeled (§1.1), with `EarlyCloseHoliday.isRollable()`
      hardcoded `false` (§1.2).
- [ ] `calculate(year)` excludes `EarlyCloseHoliday`; `calculateEarlyCloses(year)` is
      the only path to them; `hasEarlyCloses()` is a cheap year-independent check
      (§1.3).
- [ ] `Observance.apply(year)` returns null/None/nil (not an exception) for years the
      holiday doesn't apply in (§1.1, §1.5).
- [ ] At least the `previousFridayOrFollowingMonday`, `followingMonday`, and `noRoll`
      `DateRoll` strategies implemented; add others (§1.6) as needed by the calendars
      you port.
- [ ] A plug-in/registry mechanism where core never imports a specific regional
      calendar (§2.1).
- [ ] CSV (or equivalent) lookup tables for any non-Gregorian observance, with a
      documented source-tier header comment and graceful skip-on-malformed-row
      behavior (§2.2, §3.4).
- [ ] National codes and market/settlement codes are **never** the same calendar
      object (§3.2, §5) — decide this before writing your first regional calendar,
      not after.
- [ ] A 30-year (or your own fixed forward range) integration suite covering every
      calendar code: dense year coverage, minimum holiday count, no nulls,
      chronological order (§4.1).
- [ ] Timezone-aware assertions on every `EarlyCloseHoliday`'s close time (§1.2, §4.3).
- [ ] If porting Japan: cascade resolution runs before sandwich-day detection, and
      only Sunday-origin substitutes cascade (§4.4).
