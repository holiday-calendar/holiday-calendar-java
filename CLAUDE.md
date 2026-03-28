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
mvn -pl holiday-calendar-western test -Dtest=WesternEasterTest

# Run a single test method
mvn test -Dtest=HolidayCalendarTest#testCalculate

# Build with code coverage (activates default 'coverage' profile)
mvn clean verify

# Build a single module
mvn -pl holiday-calendar-core clean install
```

**Testing framework:** TestNG 7.7.1 (not JUnit). Use `@DataProvider` for parametrized tests.

## Module Structure

This is a multi-module Maven project:

| Module | JPMS Module Name | Purpose |
|--------|------------------|---------|
| `holiday-calendar-core` | `holiday.calendar.core` | Core API and abstractions |
| `holiday-calendar-western` | `holiday.calendar.western` | Western calendars: US, CA, UK, CH, DE, FR, AU |
| `holiday-calendar-apac` | `holiday.calendar.apac` | APAC calendars: SG (uses Time4J for non-Gregorian) |
| `tests` | — | Test aggregation and JaCoCo coverage reporting for SonarCloud |

## Architecture

### Core Abstractions (`holiday-calendar-core`)

**`Holiday`** (sealed interface) — base for all holidays; three permitted types selected via builder:
- `FixedHoliday` — same `MonthDay` every year (e.g., New Year's Day)
- `FloatingHoliday` — date computed per year via an `Observance` function (e.g., Easter)
- `SpecialAnniversary` — anniversary-based holidays

**`HolidayCalendar`** — named collection of holidays with a `DateRoll` strategy and configurable `weekendDays`. Its `calculate(int year)` returns sorted `HolidayDate` instances with rolling applied (respects the `rollable` flag per holiday).

**`HolidayDate`** — Java 21 record pairing a `Holiday` with its resolved `LocalDate` for a year.

**Functional interfaces** (`function` package):
- `Observance` — extends `Function<Integer, LocalDate>` + `Predicate<Integer>`; the plug-in point for floating holiday date calculation
- `DateRoll` — adjusts a date when it falls on a weekend; `DateRolls` provides common strategies (`noRoll`, `previousFridayOrFollowingMonday`, `followingMonday`)

**`HolidayCalendarNotFoundException`** — thrown by `HolidayCalendarFactory` when a code is not found; includes list of available codes.

### Service Loader Pattern

`HolidayCalendarService` is the extension interface (with `getCode()` and `getRegion()` default methods). Implementations are discovered at runtime via `HolidayCalendarFactory` using Java's `ServiceLoader`. To add a new regional calendar, implement `HolidayCalendarService` and register it in both `META-INF/services/` and the module's `provides` directive in `module-info.java`.

### Implementations (`holiday-calendar-western`)

Regional `HolidayCalendarService` implementations: `HolidayCalendarServiceUS`, `HolidayCalendarServiceCA`, `HolidayCalendarServiceUK`, `HolidayCalendarServiceCH`, `HolidayCalendarServiceDE`, `HolidayCalendarServiceFR`, `HolidayCalendarServiceAU`.

Observances are organized by region under the `observance` package. Easter-related observances live in `observance.christian` and extend `CompositeObservance` (e.g., `GoodFriday`, `EasterMonday`). `WesternEaster` and `OrthodoxEaster` extend `AbstractObservance` directly. Regional sub-packages: `us/`, `ca/`, `uk/`, `eu/`, `au/`.

### Implementations (`holiday-calendar-apac`)

`HolidayCalendarServiceSG` (SGX). Non-Gregorian holidays use Time4J (`ChineseCalendar` for Chinese New Year) or lookup tables (Vesak Day, Hari Raya Puasa/Haji, Deepavali).