# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build all modules
mvn clean install

# Run all tests
mvn clean test

# Run a single test class (run from the module directory, or use -pl)
mvn test -Dtest=HolidayCalendarTest
mvn -pl holiday-calendar-eur test -Dtest=WesternEasterTest

# Run a single test method
mvn test -Dtest=HolidayCalendarTest#testCalculate

# Build with code coverage (activates default 'coverage' profile)
mvn clean verify

# Build a single module
mvn -pl holiday-calendar-base clean install
```

**Testing framework:** TestNG 7.7.1 (not JUnit). Use `@DataProvider` for parametrized tests.

## Module Structure

This is a multi-module Maven project:

| Module | Purpose |
|--------|---------|
| `holiday-calendar-base` | Core abstractions and framework |
| `holiday-calendar-eur` | European + US/CA/UK calendar implementations |
| `holiday-calendar-noneur` | Non-European calendars (future; uses Time4J for non-Gregorian support) |
| `tests` | Test aggregation and JaCoCo coverage reporting for SonarCloud |

## Architecture

### Core Abstractions (`holiday-calendar-base`)

**`Holiday`** (abstract) — base for all holidays; three types selected via builder:
- `FixedHoliday` — same `MonthDay` every year (e.g., New Year's Day)
- `FloatingHoliday` — date computed per year via an `Observance` function (e.g., Easter)
- `SpecialAnniversary` — anniversary-based holidays

**`HolidayCalendar`** (Lombok `@Builder`) — named collection of holidays with a `DateRoll` strategy and configurable `weekendDays`. Its `calculate(int year)` returns sorted `HolidayDate` instances with rolling applied.

**`HolidayDate`** — immutable pairing of a `Holiday` and its resolved `LocalDate` for a year.

**Functional interfaces** (`function` package):
- `Observance` — extends `Function<Integer, LocalDate>` + `Predicate<Integer>`; the plug-in point for floating holiday date calculation
- `DateRoll` — adjusts a date when it falls on a weekend (e.g., roll to Friday or Monday)

### Service Loader Pattern

`HolidayCalendarService` is the extension interface. Implementations are discovered at runtime via `HolidayCalendarFactory` using Java's `ServiceLoader`. To add a new regional calendar, implement `HolidayCalendarService` and register it in `META-INF/services/`.

### Implementations (`holiday-calendar-eur`)

Regional `HolidayCalendarService` implementations: `HolidayCalendarServiceUS`, `HolidayCalendarServiceCA`, `HolidayCalendarServiceUK`, `HolidayCalendarServiceCH`.

Observances are organized by region under the `observance` package (`us/`, `ca/`, `uk/`, `eu/`). Easter-related observances implement the `EasterObservance` marker interface and are located directly under `observance/`: `WesternEaster`, `OrthodoxEaster`, `GoodFriday`, `EasterMonday`, `WhitSunday`, etc. Derived holidays (e.g., `GoodFriday`) accept an `EasterObservance` in their constructor to offset from Easter.

### Lombok Source Layout

Lombok-annotated classes live in `src/main/lombok/` (not `src/main/java/`) within each module. The build delomboks them automatically.