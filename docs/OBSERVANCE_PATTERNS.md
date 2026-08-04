# Observance Patterns

A guide for contributors adding or modifying floating holidays and half-day
early closes in this codebase. If you're implementing a new `Observance` —
or trying to understand an existing one — start here.

**Audience:** Java contributors to *this* repository. If you're instead
porting this library's algorithms to another language, see
[`PORTING_GUIDE.md`](PORTING_GUIDE.md), which covers the same core
abstractions from a language-agnostic angle and goes deeper into the
Computus/Gauss Easter math and the Islamic astronomical calculator's
internals. The two documents overlap on purpose — expect to have both open
if you're implementing an Easter-relative or Islamic-calendar observance —
this doc stays focused on "which Java class do I write and where does it go."

## Table of Contents

* [1. Mental Model](#1-mental-model)
* [2. The `Observance` Contract](#2-the-observance-contract)
* [3. Base Classes](#3-base-classes)
* [4. Implementation Patterns](#4-implementation-patterns)
* [5. The Early Close Pattern](#5-the-early-close-pattern)
* [6. How to Add a New Observance to an Existing Calendar](#6-how-to-add-a-new-observance-to-an-existing-calendar)
* [7. Adding an Observance That Needs a New Calendar Code](#7-adding-an-observance-that-needs-a-new-calendar-code)
* [8. Testing Checklist](#8-testing-checklist)
* [9. Related Documentation](#9-related-documentation)

## 1. Mental Model

An `Observance` is a pure function from a calendar year to a `LocalDate`. It
is the plug-in point used by `Holiday.Type.FLOATING` and
`Holiday.Type.EARLY_CLOSE` holidays — dates that move from year to year for
reasons a generic weekend roll can't express (a specific weekday rule, a
lookup table, an astronomical calculation).

`Holiday.Type.FIXED` holidays (same `MonthDay` every year, e.g. New Year's
Day) **don't need an `Observance`** — they're defined directly on the
builder and weekend-adjusted by the calendar's `DateRoll` strategy. Before
writing a custom `Observance`, always check whether `FIXED` + the calendar's
existing `DateRoll` already solves your problem (see the worked example in
§6 — this is a common false start).

## 2. The `Observance` Contract

```java
package org.holiday.calendar.function;

@FunctionalInterface
public interface Observance extends Function<Integer, LocalDate>, Predicate<Integer> {

    @Override
    default boolean test(Integer year) {
        return true;
    }

}
```

`Observance` extends both `Function<Integer, LocalDate>` (compute the date)
and `Predicate<Integer>` (does this observance apply this year at all).
`test(year)` defaults to `true`; implementations that aren't valid for every
year override it.

**Invariants every implementation must satisfy:**

- **Deterministic.** The same year always produces the same result — either
  the same date, or the same `null`. No randomness, no I/O beyond one-time
  loading of static/cached reference data (e.g. a CSV table loaded once and
  cached — see §4.5).
- **`apply(year)` returns `null` for years it doesn't apply to — it does not
  throw.** Callers (including `HolidayCalendar.calculate()`) expect to
  branch on a `null`/empty result, not catch an exception.
- **`test(year)` and `apply(year)` must agree**: `test(year) == false`
  implies `apply(year) == null`, and vice versa.

```java
// Wrong — throws for an out-of-range year
public LocalDate apply(Integer year) {
    if (year < 1986) throw new IllegalArgumentException("no MLK Day before 1986");
    return ...;
}

// Right — returns null; test() reports the same boundary
public LocalDate apply(Integer year) {
    return test(year) ? computeDate(year) : null;
}
public boolean test(Integer year) {
    return year != null && year >= 1986;
}
```

## 3. Base Classes

### 3.1 `AbstractObservance`

The standard base class for new `Observance` implementations:

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

    protected boolean isValidYear(int year) {
        return true;   // default: applies to every year
    }

}
```

`apply`/`test` are `final` — the null-guard and the `test`/`apply` agreement
required by §2 are handled for you and cannot be gotten wrong. Subclasses
implement two methods:

- `computeDate(int year)` — required. Only ever called when
  `isValidYear(year)` is `true`.
- `isValidYear(int year)` — optional, defaults to `true`. Override this to
  gate applicability (a data ceiling, an algorithm's valid range, a
  first-observed year, a day-of-week suppression rule).

This is the class you should extend for new observances (see §3.3 for why).

### 3.2 `CompositeObservance`

For observances defined *relative to* another `Observance` — most commonly,
holidays computed relative to Easter:

```java
public abstract class CompositeObservance extends AbstractObservance {

    protected final Observance base;

    protected CompositeObservance(Observance base) {
        this.base = Objects.requireNonNull(base, "Argument 'base' cannot be null");
    }

    @Override
    protected boolean isValidYear(int year) {
        return base.test(year);   // validity delegates to the base observance
    }

}
```

`GoodFriday` and `EasterMonday`
(`holiday-calendar-western/.../observance/christian/`) are the reference
examples — each is a two-line `computeDate`:

```java
public class GoodFriday extends CompositeObservance {
    public GoodFriday(EasterObservance easterObservance) { super(easterObservance); }
    @Override
    protected LocalDate computeDate(int year) { return base.apply(year).minusDays(2); }
}
```

```java
public class EasterMonday extends CompositeObservance {
    public EasterMonday(EasterObservance easterObservance) { super(easterObservance); }
    @Override
    protected LocalDate computeDate(int year) { return base.apply(year).plusDays(1); }
}
```

`isValidYear` is inherited from `CompositeObservance` — you don't restate
Easter's valid-year range in every dependent holiday.

### 3.3 Direct `Observance` implementation

`AbstractObservance` isn't the only valid style. Roughly half the
observances in this codebase implement `Observance` directly instead, e.g.
`MartinLutherKingJrDay` (`observance/us`):

```java
public class MartinLutherKingJrDay implements Observance {

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        return Year.of(year).atMonth(Month.JANUARY).atDay(1)
                   .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY));
    }

    @Override
    public boolean test(Integer year) { return 1986 <= year; }

}
```

**Both styles are valid and intentional — the direct style is not legacy
code to "clean up."** That said, **prefer `AbstractObservance` for new
code**: the null-guard is handled for you, so it's structurally impossible
to forget the `if (!test(year)) return null;` check that a direct
implementation must hand-write correctly every time.

> **Common mistake:** `CivicHoliday` (`observance/ca`) implements
> `Observance` directly but never calls `test(year)` inside `apply()`:
> ```java
> public LocalDate apply(Integer year) {
>     return Year.of(year).atMonth(Month.AUGUST).atDay(1)
>                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
> }
> ```
> `Year.of(year)` unboxes `year` immediately, so `apply(null)` throws an
> unguarded `NullPointerException` instead of returning `null`. This is a
> known latent bug, tracked as
> [#267](https://github.com/holiday-calendar/holiday-calendar-java/issues/267)
> (not fixed by this doc) — flagged here as the cautionary example of what
> the direct style makes easy to get wrong.

## 4. Implementation Patterns

Pick the pattern that matches how your holiday's date actually varies.

### 4.1 Algorithm-based — no data table, no ceiling

**Example:** `OrthodoxEaster` (`observance/christian`) — implements the
Gauss algorithm, valid for years 530–3399, entirely computed, no lookup
table. `WesternEaster` layers the Computus algorithm on top and falls back
to `OrthodoxEaster` for pre-1583 (pre-Gregorian) years:

```java
public class WesternEaster extends AbstractObservance implements EasterObservance {
    private static final OrthodoxEaster PRE_1583 = new OrthodoxEaster();

    @Override
    protected LocalDate computeDate(int year) {
        if (1583 > year) return PRE_1583.apply(year);
        // ... Computus arithmetic ...
    }

    @Override
    protected boolean isValidYear(int year) {
        return OrthodoxEaster.MIN_VALID_YEAR <= year;
    }
}
```

**When to use:** whenever the date follows a deterministic, published
algorithm. Preferred over a data table when available — no data ceiling to
maintain, and it's correct for any year, not just the years someone
remembered to add rows for. For the full Computus/Gauss derivations, see
`PORTING_GUIDE.md` §1 — not reproduced here.

### 4.2 Nth-weekday-of-month

**Example:** `MartinLutherKingJrDay` (§3.3 above) —
`TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY)` for "third Monday
in January." `CivicHoliday` uses the related
`TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)` for "first Monday."

**When to use:** any holiday statutorily defined as "Nth `DayOfWeek` of
`Month`" — this is usually the simplest correct implementation and needs no
external data.

### 4.3 Fixed date + predicate gate

**Example:** `ChristmasEveEarlyClose` (`observance/us`) — the date itself
never moves (always Dec 24), but the observance is *suppressed entirely* in
years it doesn't apply:

```java
public class ChristmasEveEarlyClose extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        return LocalDate.of(year, Month.DECEMBER, 24);
    }

    @Override
    protected boolean isValidYear(int year) {
        DayOfWeek christmasDay = LocalDate.of(year, Month.DECEMBER, 25).getDayOfWeek();
        return switch (christmasDay) {
            case MONDAY, SATURDAY, SUNDAY -> false;   // no NYSE early close these years
            default -> true;
        };
    }
}
```

**When to use:** a single fixed calendar date whose *applicability* (not
position) depends on a simple, computable rule — most often a day-of-week
check on itself or a related date. This is the pattern to reach for instead
of a hardcoded per-year lookup table when the rule is simple and doesn't
need external data.

### 4.4 Date-collision / conditional

**Example:** `BoxingDayCAD` (`observance/ca`), used by the `XTSE`/`CAD`
market calendars. Boxing Day (Dec 26) and Christmas Day (Dec 25) can collide
under weekend rolling — e.g. if Dec 25 is a Saturday, Christmas rolls to
Monday Dec 27, so Boxing Day can't also land on Dec 27 and must roll further,
to Dec 28. The generic `DateRoll` mechanism can't express this: it operates
on one holiday at a time and has no way to know another holiday has already
"claimed" a date.

```java
public class BoxingDayCAD implements Observance {
    @Override
    public LocalDate apply(Integer year) {
        final LocalDate christmas = LocalDate.of(year, Month.DECEMBER, 25);
        final LocalDate boxingDay = LocalDate.of(year, Month.DECEMBER, 26);
        final DayOfWeek christmasDow = christmas.getDayOfWeek();
        final DayOfWeek boxingDow    = boxingDay.getDayOfWeek();

        if (christmasDow == DayOfWeek.SATURDAY) return boxingDay.plusDays(2L); // Dec 28
        if (christmasDow == DayOfWeek.SUNDAY)   return boxingDay.plusDays(1L); // Dec 27
        if (boxingDow    == DayOfWeek.SATURDAY) return boxingDay.plusDays(2L); // Dec 28
        if (boxingDow    == DayOfWeek.SUNDAY)   return boxingDay.plusDays(1L); // Dec 27
        return boxingDay;
    }
}
```

**When to use:** two adjacent fixed holidays where one holiday's own
weekend-rolling can push it onto the other's natural date. Encapsulate the
*whole pair's* substitution logic inside one `Observance` rather than trying
to coordinate two independent `DateRoll`-adjusted `FIXED` holidays.

Notably, `HolidayCalendarServiceCA` (the *national* CA calendar) does **not**
use `BoxingDayCAD` — it defines Boxing Day as a plain non-rollable `FIXED`
holiday instead, because the national calendar doesn't observe the
market-specific substitution rule. Which pattern you need depends on the
calendar, not just the holiday — see §6 for how this plays out concretely.

### 4.5 CSV-backed lookup

**Example:** `EidAlFitr` (`holiday-calendar-mena`,
`observance/islamic/mena`). Islamic calendar dates are set by moon sighting
and can't be computed algorithmically, so they're published, tabulated data.

`CsvObservanceLoader` (`holiday-calendar-core`, `util` package) is the
shared utility for this:

```java
public static Map<Integer, LocalDate> loadSingle(Class<?> anchor, String classpathResource)
public static Map<Integer, List<LocalDate>> loadMultiple(Class<?> anchor, String classpathResource)
```

CSV format: `year,YYYY-MM-DD[,comment]`. Blank lines and lines starting with
`#` are ignored. Malformed rows are logged at WARN and skipped — one bad row
doesn't fail the whole file. `anchor` is the class whose package the CSV
resource lives alongside (JPMS resource resolution).

`EidAlFitr` loads a per-country CSV (`eid-al-fitr-<countryCode>.csv`,
resolved relative to `EidAlFitr.class`'s package), cached per country code:

```java
public class EidAlFitr extends AbstractObservance {
    private static final ConcurrentHashMap<String, Map<Integer, LocalDate>> CACHE = new ConcurrentHashMap<>();

    private final String countryCode;
    private final Map<Integer, LocalDate> dates;

    public EidAlFitr(String countryCode) {
        this.countryCode = countryCode.toLowerCase();
        this.dates = CACHE.computeIfAbsent(this.countryCode,
                cc -> CsvObservanceLoader.loadSingle(EidAlFitr.class, "eid-al-fitr-" + cc + ".csv"));
    }
    // ...
}
```

**When to use:** dates set by an external authority (moon sighting,
government announcement) that cannot be derived by algorithm. Document the
data's valid year range explicitly (see `DATA_VALID_FROM`/
`DATA_VALID_THROUGH` below) and cite the source in the class Javadoc.

> **Not `CNY`.** The issue that prompted this doc suggested `CNY` (China) as
> the CSV-lookup example — it isn't one. `CNY`'s statutory holidays (Spring
> Festival, Qingming, Dragon Boat, Mid-Autumn) are computed *algorithmically*
> via Time4J's `ChineseCalendar` (see `ChineseNewYearDay`,
> `holiday-calendar-apac`). `CsvObservanceLoader` is used there only for a
> narrower, separate concern — the compensatory make-up working-day table —
> not for the holiday dates themselves. `EidAlFitr` above is the real
> CSV-backed-lookup example to follow.

### 4.6 CSV lookup with an astronomical fallback

**Example:** `EidAlFitr` again — its `computeDate` first checks the CSV
table; only for Turkey (`countryCode == "tr"`) does a CSV miss fall through
to a live astronomical calculation:

```java
@Override
protected LocalDate computeDate(int year) {
    LocalDate csvDate = dates.get(year);
    if (csvDate != null) return csvDate;
    if (ILMI_TAKVIM_COUNTRY_CODE.equals(countryCode)) {
        try {
            return IlmiTakvimCalculator.eidAlFitr(year);
        } catch (RuntimeException e) {
            log.warn("Ilmi takvim calculation failed for Eid al-Fitr {}; no fallback data available", year, e);
            return null;   // swallow, don't propagate — apply() must never throw
        }
    }
    return null;
}

@Override
protected boolean isValidYear(int year) {
    if (year > DATA_VALID_THROUGH) return false;
    return year >= DATA_VALID_FROM
            && (dates.containsKey(year) || ILMI_TAKVIM_COUNTRY_CODE.equals(countryCode));
}
```

`IlmiTakvimCalculator` (`observance/islamic/mena/ilmitakvim`) implements the
Diyanet (Turkish Presidency of Religious Affairs) *ilmi takvim* methodology —
true astronomical lunar conjunction (Time4J `MoonPhase.NEW_MOON`) gated by an
Ankara-sunset visibility rule. It's calibrated to reproduce all 24 of
Diyanet's officially published dates for 2024–2035 exactly. In this
codebase it is used only as a **defensive fallback**, not the primary path
— `tr`'s CSV already has rows through `DATA_VALID_THROUGH` (2055), so the
calculator only fires if that data ceiling is raised before the CSV catches
up. See `PORTING_GUIDE.md` §2 for the calculator's internals.

**When to use:** you have published data for a bounded window but want
graceful degradation (not a hard failure) beyond it, and a reliable
astronomical/algorithmic approximation exists. Always catch the calculator's
exceptions and return `null` rather than propagating — per §2's contract,
`apply()` must never throw.

## 5. The Early Close Pattern

An early close is a half-day close, not a full holiday — the market is open
but closes at a specific local time. It's modeled as `Holiday.Type.EARLY_CLOSE`
via `EarlyCloseHoliday`, a **record**, always non-rollable:

```java
public record EarlyCloseHoliday(String name, String description, Observance observance,
                                 LocalTime closeTime, ZoneId zoneId) implements Holiday {
    @Override
    public boolean isRollable() { return false; }   // always false, unconditionally
    // ...
}
```

Built the same way as any other holiday, via `Holiday.builder()`:

```java
Holiday christmasEveEarlyClose = Holiday.builder()
    .name("Christmas Eve")
    .description("LSE half-day close; shifts to the preceding Friday when " +
                 "December 24 falls on a Saturday or Sunday")
    .type(Holiday.Type.EARLY_CLOSE)
    .rollable(false)
    .observance(new ChristmasEveEarlyClose())
    .closeTime(LocalTime.of(12, 30))
    .zoneId(ZoneId.of("Europe/London"))
    .build();
```

> **⚠️ The single most consequential fact about early closes (v2.0.0
> breaking change):** `HolidayCalendar.calculate(int year)` **silently
> excludes every `EarlyCloseHoliday`.** To retrieve them, call
> `calculate​EarlyCloses(int year)` instead:
>
> ```java
> List<HolidayDate> fullClosures = calendar.calculate(2026);
> List<HolidayDate> earlyCloses  = calendar.calculateEarlyCloses(2026);
> ```
>
> If you add a new `EARLY_CLOSE` holiday and it never shows up when you call
> `calculate()`, this is why — it's working as designed, not a bug. (Prior
> to v2.0.0, early closes were mixed into `calculate()`'s results; this
> changed to give callers an explicit, type-safe way to separate full-day
> and half-day closures.) `calculateEarlyCloses()` applies no date rolling
> (moot, since `isRollable()` is always `false`). A cheap
> `calendar.hasEarlyCloses()` check is available if you need to branch
> without computing a specific year.

**Timezone handling:** `closeTime`/`zoneId` are separate fields because the
close time is exchange-local, not UTC — e.g. "12:30 Europe/London" for LSE,
"13:00 America/New_York" for NYSE. Always construct `zoneId` with a fixed
`ZoneId` (e.g. `ZoneId.of("Australia/Sydney")`), never a `ZoneOffset` — early
close times are **not** adjusted for daylight saving; the fixed zone ID
handles DST transitions correctly on its own, a numeric offset would not.

**Representative examples** (19 early-close observances exist in total
across `XNYS`, `XTSE`, `XLON`, `XASX`, `XPAR`, `XSES`, `ILS`, `TRY` — this
table is representative, not exhaustive; find the rest with
`find . -iname "*EarlyClose*.java"`):

| Market | Holiday | Close time | Zone | Gating rule |
|---|---|---|---|---|
| XNYS (NYSE) | Christmas Eve | 13:00 | America/New_York | Suppressed if Dec 25 is Mon/Sat/Sun (§4.3) |
| XTSE (TSX) | Christmas Eve | 13:00 | America/Toronto | Suppressed if Dec 24 itself is Sat/Sun |
| XLON (LSE) | Christmas Eve / New Year's Eve | 12:30 | Europe/London | Shifted to preceding Friday if Sat/Sun |
| XASX (ASX) | Christmas Eve / New Year's Eve | 14:10 | Australia/Sydney | Suppressed (not shifted) if Sat/Sun (§4.3-style, shared via package-private `AsxEveEarlyClose`) |

Note the differing suppression styles: XNYS/XASX *suppress* the early close
entirely on a colliding weekend; XLON *shifts* it to the preceding Friday
instead. Check your market's actual published rule before assuming one
style over the other — they are genuinely different conventions, not a
bug in either.

The precedent for a national-vs-market split is `HolidayCalendarServiceXLON`
(the LSE trading calendar, which has early closes) versus
`HolidayCalendarServiceUK` (the national calendar, which has **zero** —
confirmed by its own Javadoc and by
`HolidayCalendar30YearIT.testUKHasNoEarlyClosesOver30Years()`). If you're
looking for "where do I add an early close for a market whose national
calendar already exists," `XLON` is the model, not `UK`.

## 6. How to Add a New Observance to an Existing Calendar

### Part A — check whether you need one at all

Before writing any `Observance` code, check whether `FIXED` + the calendar's
existing `DateRoll` already covers your case. For example, "Canada Day,
observed the following Monday when it falls on a weekend" needs **no
`Observance` at all** — `HolidayCalendarServiceCA` already handles it with a
plain `FIXED` holiday plus the calendar-level roll strategy:

```java
// CanadaHolidays.baseHolidays()
Holiday.builder()
    .name("Canada Day")
    .type(Holiday.Type.FIXED)
    .monthDay(Month.JULY, 1)
    .rollable(true)
    .build()

// HolidayCalendarServiceCA.getHolidayCalendar()
HolidayCalendar.builder()
    // ...
    .dateRoll(DateRolls.followingMonday())
    // ...
    .build()
```

`Observance` is for dates that move for reasons a generic weekend roll
can't express — a specific weekday rule (§4.2), a lookup table (§4.5), an
astronomical calculation (§4.6), or a collision with another holiday's own
rolling (§4.4).

### Part B — worked tutorial

The rest of this section walks through adding a genuinely new observance,
using a **fictional** holiday — **"Founders' Day," third Monday in
June** — so nothing here is asserting a real Canadian statutory holiday
exists. (This example is illustrative only: don't actually merge it. This
codebase's value proposition is factual accuracy of real calendar data, and
every real holiday's Javadoc cites an authoritative source — see §8 — which
a fictional holiday structurally cannot do.)

**Step 1 — Design.** "Third Monday in June" is the nth-weekday-of-month
pattern (§4.2). Use `AbstractObservance` (§3.1/§3.3 — the recommended style
for new code). No `isValidYear` override needed if it applies to every year.

**Step 2 — Implement.** New file,
`holiday-calendar-western/src/main/java/org/holiday/calendar/observance/ca/FoundersDay.java`
(package matches the region the holiday belongs to; include the standard
LGPL-2.1 copyright header — see `CONTRIBUTING.md`'s "Copyright Header"
section, your IDE can insert this for you):

```java
/******************************************************************************
 * ... standard LGPL-2.1 header, see CONTRIBUTING.md ...
 ******************************************************************************/
package org.holiday.calendar.observance.ca;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;

/**
 * FICTIONAL — tutorial example only, not a real observance. See
 * docs/OBSERVANCE_PATTERNS.md §6.
 */
public class FoundersDay extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        return Year.of(year).atMonth(Month.JUNE).atDay(1)
                   .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY));
    }

}
```

A real observance's Javadoc must cite an authoritative source instead of
the "FICTIONAL" disclaimer above — see `MartinLutherKingJrDay` or
`ChristmasEveEarlyClose` (§4.1/§4.3) for the citation style to follow
(e.g. "Verified against NYSE Group's official Holiday and Early Closings
Calendar press releases").

**Step 3 — Wire it in.** This is where you must make a real decision, not
skip one. `CanadaHolidays.baseHolidays()` (`impl` package) is a
package-private factory **shared by both the `CA` (national) and `XTSE`
(Toronto Stock Exchange) calendars** — its own Javadoc says so explicitly.
Editing it affects *both* calendars. If your holiday should apply to both,
add it there:

```java
// CanadaHolidays.baseHolidays(), inside the List.of(...)
Holiday.builder()
    .name("Founders' Day")
    .type(Holiday.Type.FLOATING)
    .rollable(false)
    .observance(new FoundersDay())
    .build()
```

If it should apply to only *one* of the two calendars, add it directly in
that service's `getHolidayCalendar()` instead of `CanadaHolidays` — this is
exactly how Christmas Day and Boxing Day are handled today: they're
deliberately left **out** of `CanadaHolidays.baseHolidays()` and defined
separately in `HolidayCalendarServiceCA` (plain `FIXED`, `rollable(false)`)
and independently again in `HolidayCalendarServiceXTSE`/
`HolidayCalendarServiceCAD` (`FLOATING`, using `BoxingDayCAD` from §4.4) —
because the market and national calendars genuinely disagree on how those
two days behave. Use `CanadaHolidays` for holidays both calendars agree on;
add per-calendar otherwise.

**Step 4 — Test.** New file,
`holiday-calendar-western/src/test/java/org/holiday/calendar/observance/ca/FoundersDayTest.java`,
following `WesternEasterTest`'s TestNG `@DataProvider` style:

```java
public class FoundersDayTest {
    private final FoundersDay foundersDay = new FoundersDay();

    @Test(dataProvider = "data")
    public void testApply(int year, LocalDate expected) {
        assertEquals(foundersDay.apply(year), expected);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 2024, LocalDate.of(2024, Month.JUNE, 17) });
        data.add(new Object[]{ 2025, LocalDate.of(2025, Month.JUNE, 16) });
        data.add(new Object[]{ 2026, LocalDate.of(2026, Month.JUNE, 15) });
        return data.iterator();
    }
}
```

Since this observance applies every year, there's no boundary/invalid-year
case to add here — if yours has an `isValidYear` gate (§4.3, §4.5), test
just below and at the threshold, per §8.

Also confirm the change doesn't break
`tests/src/test/java/org/holiday/calendar/HolidayCalendar30YearIT.java` —
the **single** 30-year integration test class for the whole project (it
lives in the `tests` aggregation module, not per-module). Its generic
structural checks (30 year-keys, ≥5 holidays/year, no nulls, chronological
order) run automatically against every registered calendar code and will
pick up a newly-wired holiday without changes on your part. If your change
affects a *market-specific* assertion with a hardcoded count (e.g. an
early-close count for a specific exchange), you'll need to find and update
that assertion by hand — it won't fail loudly in a way that tells you where
to look otherwise.

**Step 5 — Docs.** Adding to an *existing* calendar code (as above) needs no
README changes. If you were instead introducing a brand-new calendar code,
see §7 and the 3 README spots noted there.

## 7. Adding an Observance That Needs a New Calendar Code

If the observance is for a market or country with no existing
`HolidayCalendarService`, that's a larger unit of work than this doc covers:
a new `HolidayCalendarService` implementation, a `META-INF/services` entry,
a `module-info.java` `provides` directive, and updates to all three spots in
`README.md` (the calendar table, the dependency comment, and the code
list). See `CONTRIBUTING.md` and the module-info/package-info Javadoc
(added in #225) for that process — not duplicated here.

**Note:** there is no separate registration step for an individual
`Observance` — no `provides Observance with ...` directive, no
`META-INF/services` entry for `Observance` itself. That ServiceLoader/SPI
mechanism exists only for `HolidayCalendarService` (one entry per calendar
*code*). An `Observance` implementation is just a plain class, `new`'d
directly wherever it's used (§6, Step 3).

## 8. Testing Checklist

- [ ] Known correct dates asserted via `@DataProvider` for at least 3–5 real
      years, with the source cited in a comment or the class Javadoc.
- [ ] Boundary/invalid year returns `null` from `apply()` and `false` from
      `test()` — not an exception.
- [ ] If gated by `isValidYear`, test a year just below and a year exactly
      at the threshold.
- [ ] If CSV-backed, test a year missing from the CSV table (and the
      fallback path too, if one exists — §4.6).
- [ ] Covered by
      `tests/src/test/java/org/holiday/calendar/HolidayCalendar30YearIT.java`
      — the generic structural assertions run automatically; update any
      market-specific hardcoded-count assertion your change affects.
- [ ] Javadoc cites an authoritative source (government or exchange
      publication), matching the project's existing convention — see
      `ChristmasEveEarlyClose`'s NYSE citation or `EidAlFitr`'s SCA/Tadawul/
      Diyanet citations for the style. (Not applicable to this doc's own
      fictional §6 tutorial example, which is explicitly labeled as such
      instead.)

## 9. Related Documentation

- [`PORTING_GUIDE.md`](PORTING_GUIDE.md) — language-agnostic architecture,
  full Computus/Gauss Easter derivations, the astronomical calculator's
  internals, the `HolidayCalendarService` ServiceLoader pattern, and the
  30-year integration test pattern.
- [`CONTRIBUTING.md`](../CONTRIBUTING.md) — copyright header, PR process,
  code of conduct.
- Module-info and package-info Javadoc across all four modules (added in
  #225) — module boundaries and per-package purpose.
