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
| `holiday-calendar-core` | `org.holiday.calendar.core` | Core API and abstractions |
| `holiday-calendar-western` | `org.holiday.calendar.western` | Western calendars: US, CA, UK, CH, DE, FR, AU |
| `holiday-calendar-apac` | `org.holiday.calendar.apac` | APAC calendars: SG, JP, CN (uses Time4J for non-Gregorian) |
| `holiday-calendar-mena` | `org.holiday.calendar.mena` | MENA calendars: AE, SA, IL, TR, QA, EG, KW, BH, MA, JO (uses Time4J for Islamic calendar) |
| `tests` | — | Test aggregation and JaCoCo coverage reporting for SonarCloud |

## Architecture

### Core Abstractions (`holiday-calendar-core`)

**`Holiday`** (sealed interface) — base for all holidays; four permitted types selected via builder:
- `FixedHoliday` — same `MonthDay` every year (e.g., New Year's Day)
- `FloatingHoliday` — date computed per year via an `Observance` function (e.g., Easter)
- `SpecialAnniversary` — anniversary-based holidays
- `EarlyCloseHoliday` — half-day market close (`Holiday.Type.EARLY_CLOSE`); carries a `closeTime`/`zoneId`, is always non-rollable, and is excluded from `HolidayCalendar.calculate()` — use `calculateEarlyCloses(int year)` instead. See `IsraelHolidays.earlyCloseHolidays()` (mena) and `HolidayCalendarServiceXLON`'s Christmas Eve/New Year's Eve (western) for precedent.

**`HolidayCalendar`** — named collection of holidays with a `DateRoll` strategy and configurable `weekendDays`. Its `calculate(int year)` returns sorted `HolidayDate` instances with rolling applied (respects the `rollable` flag per holiday).

**`HolidayDate`** — Java 21 record pairing a `Holiday` with its resolved `LocalDate` for a year.

**Functional interfaces** (`function` package):
- `Observance` — extends `Function<Integer, LocalDate>` + `Predicate<Integer>`; the plug-in point for floating holiday date calculation
- `DateRoll` — adjusts a date when it falls on a weekend; `DateRolls` provides common strategies (`noRoll`, `previousFridayOrFollowingMonday`, `followingMonday`)

**`HolidayCalendarNotFoundException`** — thrown by `HolidayCalendarFactory` when a code is not found; includes list of available codes.

### Service Loader Pattern

`HolidayCalendarService` is the extension interface (with `getCode()` and `getRegion()` default methods). Implementations are discovered at runtime via `HolidayCalendarFactory` using Java's `ServiceLoader`. To add a new regional calendar, implement `HolidayCalendarService` and register it in both `META-INF/services/` and the module's `provides` directive in `module-info.java`.

### Implementations (`holiday-calendar-western`)

National and market/central-bank `HolidayCalendarService` implementations, one pair per country where both exist:
- `US` (United States National) / `USD` (Federal Reserve)
- `CA` (Canada National) / `CAD` (Bank of Canada / Lynx)
- `UK` (United Kingdom National) / `GBP` (CHAPS)
- `CH` (Switzerland / SIX) / `CHF` (SIC/SNB)
- `DE` (Germany / Xetra)
- `FR` (France / Euronext Paris)
- `AU` (Australian Securities Exchange) / `AUD` (RBA)
- `EUR` (TARGET2)

Observances are organized by region under the `observance` package. Easter-related observances live in `observance.christian` and extend `CompositeObservance` (e.g., `GoodFriday`, `EasterMonday`). `WesternEaster` and `OrthodoxEaster` extend `AbstractObservance` directly. Regional sub-packages: `us/`, `ca/`, `uk/`, `eu/`, `au/`.

### Implementations (`holiday-calendar-apac`)

- `SG` (Singapore SGX) / `SGD` (MAS/MEPS+)
- `JP` (Tokyo Stock Exchange) / `JPY` (Bank of Japan)
- `CN` (China National) / `CNY` (People's Bank of China)

Non-Gregorian holidays use Time4J (`ChineseCalendar` for Chinese New Year) or lookup tables (Vesak Day, Hari Raya Puasa/Haji, Deepavali). Observance sub-packages: `observance.lunar`, `observance.islamic.apac`, `observance.hindu`, `observance.jp`.

### Implementations (`holiday-calendar-mena`)

- `AE` (United Arab Emirates National) / `AED` (CBUAE/DFM/ADX)
- `SA` (Saudi Arabia National) / `SAR` (Tadawul/SAMA)
- `IL` (Israel National) / `ILS` (TASE/Bank of Israel)
- `TR` (Turkey National) / `TRY` (BIST/TCMB)
- `QA` (Qatar National) / `QAR` (QSE/QCB)
- `EG` (Egypt National) / `EGP` (EGX/CBE)
- `KW` (Kuwait National) / `KWD` (Boursa Kuwait/CBK)
- `BH` (Bahrain National) / `BHD` (Boursa Bahrain/CBB)
- `MA` (Morocco National) / `MAD` (CSE/BAM)
- `JO` (Jordan National) / `JOD` (ASE/CBJ)

Islamic-calendar holidays (Eid al-Fitr, Eid al-Adha, Islamic New Year, Ashura, etc.) use CSV-backed lookup data with a Diyanet "ilmi takvim" astronomical fallback calculator for years beyond the data ceiling; see `observance.islamic.mena`. `IsraelHolidays` (package-private) centralizes the shared holiday list — including `earlyCloseHolidays()` — consumed by both `HolidayCalendarServiceIL` and `HolidayCalendarServiceILS`. Observance sub-packages: `observance.eg`, `observance.islamic.mena`, `observance.hebrew`, `observance.qa`.