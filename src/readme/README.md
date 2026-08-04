# Holiday Calendar (Java)

[![Build](https://github.com/holiday-calendar/holiday-calendar-java/actions/workflows/maven-build.yml/badge.svg)](https://github.com/holiday-calendar/holiday-calendar-java/actions/workflows/maven-build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=holiday-calendar_holiday-calendar-java&metric=alert_status)](https://sonarcloud.io/project/overview?id=holiday-calendar_holiday-calendar-java)
[![License: LGPL v2.1](https://img.shields.io/badge/License-LGPL_v2.1-blue.svg)](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html)

A Java library for defining and calculating holiday calendars. Provides an extensible foundation for generating the calendars used to determine when holidays occur and when they are observed worldwide.

> **Upgrading from v1.4.0 or earlier?** v2.0.0 introduced the `EarlyCloseHoliday`
> API, and v2.1.0 separates national holiday calendars from equities-exchange
> market calendars. See [MIGRATION.md](MIGRATION.md) for the full breaking-changes
> guide.

> **Porting to another language?** See [docs/PORTING_GUIDE.md](docs/PORTING_GUIDE.md) for the core abstractions, design patterns, and data formats needed to build a compatible implementation in JavaScript, Python, Go, or any other language.

## About

Holiday Calendar (Java) answers common needs in financial, scheduling, and business applications: _"Is this date a business day?"_ and _"When is this holiday observed this year?"_

Key design goals:

- **Correct date rolling** — when a holiday falls on a weekend, the library applies configurable rolling rules (e.g. following Monday, previous Friday) to compute the observed date.
- **Extensible by design** — new regional calendars are added via Java's `ServiceLoader` mechanism; no changes to core code required.
- **Modern Java** — built on Java 21, using sealed interfaces, records, and the module system (JPMS).

### Supported Calendars

Each region may publish up to three distinct calendars: a **National** calendar (public holidays
only, no early closes — with one exception, noted below), a **Central Bank/Settlement** calendar
(currency/RTGS system holidays), and a **Market/Exchange** calendar (a specific exchange's trading
holidays, generally including half-day early closes). Not every country has all three — see the
tables below. `factory.create("CODE")` accepts any code from any column.

> **Exception:** `TR` (Turkey's national calendar) is the one national code that includes an
> early-close entry (Republic Day Eve), because it is a statutory closure rather than a
> market-only convention. The same entry is also carried by `TRY`.

#### Western (`holiday-calendar-western`)

| Country/Region | National | Central Bank/Settlement | Market/Exchange (MIC) | Early closes? |
|---|---|---|---|---|
| Australia | `AU` | `AUD` RBA | `XASX` ASX | ✅ (XASX) |
| Canada | `CA` | `CAD` Bank of Canada (Lynx) | `XTSE` TSX | ✅ (XTSE) |
| France | `FR` | — | `XPAR` Euronext Paris | ✅ (XPAR) |
| Germany | `DE` | — | `XETR` Xetra | — |
| Switzerland | `CH` | `CHF` SIC/SNB | `XSWX` SIX | — |
| United Kingdom | `UK` | `GBP` CHAPS | `XLON` LSE | ✅ (XLON) |
| United States | `US` | `USD` Federal Reserve | `XNYS` NYSE | ✅ (XNYS) |
| **Eurozone** (multi-country) | — | `EUR` TARGET2 | — | — |

Germany and France have no dedicated currency-code row (no separate settlement system) — the
shared Eurozone/`EUR` row above serves that role instead of being duplicated per country.

#### APAC (`holiday-calendar-apac`)

| Country/Region | National | Central Bank/Settlement | Market/Exchange (MIC) | Early closes? |
|---|---|---|---|---|
| China | `CN` | `CNY` PBOC | — | — |
| Japan | `JP` | `JPY` BOJ | — | — |
| Singapore | `SG` | `SGD` MAS/MEPS+ | `XSES` SGX | ✅ (XSES) |

Japan has no dedicated exchange (MIC) calendar — `JP` is documented as national-only.

#### MENA (`holiday-calendar-mena`)

No Market/Exchange (MIC) calendars exist yet in this module — each country has only a National and
a Currency/Exchange code, with the currency code doubling as the de facto exchange calendar where
applicable (e.g. `ILS` for TASE, `TRY` for BIST).

| Country | National | Currency/Exchange | Early closes? |
|---|---|---|---|
| UAE | `AE` | `AED` CBUAE/DFM/ADX | — |
| Saudi Arabia | `SA` | `SAR` Tadawul/SAMA | — |
| Israel | `IL` | `ILS` TASE/Bank of Israel | ✅ (ILS only, 6 entries) |
| Turkey | `TR` | `TRY` BIST/TCMB | ✅ (both TR and TRY — shared Republic Day Eve entry) |
| Qatar | `QA` | `QAR` QSE/QCB | — |
| Egypt | `EG` | `EGP` EGX/CBE | — |
| Kuwait | `KW` | `KWD` Boursa Kuwait/CBK | — |
| Bahrain | `BH` | `BHD` Boursa Bahrain/CBB | — |
| Morocco | `MA` | `MAD` CSE/BAM | — |
| Jordan | `JO` | `JOD` ASE/CBJ | — |

For information on adding new calendars or maintaining existing ones, see the [Contributing Guide](CONTRIBUTING.md).

## Installation

Holiday Calendar requires **Java 21** and **Maven 3.5.0** or higher.

The library is published to the GitHub Packages Maven registry. Add the repository and dependency to your `pom.xml`:

```xml
<repositories>
  <repository>
    <id>github</id>
    <name>GitHub holiday-calendar Apache Maven Packages</name>
    <url>https://maven.pkg.github.com/holiday-calendar/holiday-calendar-java</url>
  </repository>
</repositories>
```

Then add the modules you need:

```xml
<!-- Core API (required) -->
<dependency>
  <groupId>org.holiday.calendar</groupId>
  <artifactId>holiday-calendar-core</artifactId>
  <version>@project.version@</version>
</dependency>

<!-- Western calendars: US, USD, CA, CAD, UK, GBP, CH, CHF, DE, EUR, FR, AU, AUD -->
<dependency>
  <groupId>org.holiday.calendar</groupId>
  <artifactId>holiday-calendar-western</artifactId>
  <version>@project.version@</version>
</dependency>

<!-- APAC calendars: SG, SGD, JP, JPY, CN, CNY -->
<dependency>
  <groupId>org.holiday.calendar</groupId>
  <artifactId>holiday-calendar-apac</artifactId>
  <version>@project.version@</version>
</dependency>

<!-- MENA calendars: AE, AED, BH, BHD, EG, EGP, IL, ILS, JO, JOD, KW, KWD, MA, MAD, QA, QAR, SA, SAR, TR, TRY -->
<dependency>
  <groupId>org.holiday.calendar</groupId>
  <artifactId>holiday-calendar-mena</artifactId>
  <version>@project.version@</version>
</dependency>
```

## Usage

### Look up a calendar and calculate holidays for a year

```java
import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.HolidayCalendarFactory;
import org.holiday.calendar.HolidayDate;

HolidayCalendarFactory factory = new HolidayCalendarFactory();

// Get the US holiday calendar
HolidayCalendar usCalendar = factory.create("US");

// Calculate observed holiday dates for 2025
List<HolidayDate> holidays = usCalendar.calculate(2025);
holidays.forEach(hd ->
    System.out.printf("%s  %s%n", hd.getDate(), hd.getHoliday().getName())
);
```

### Check if a date is a weekend

```java
boolean isWeekend = usCalendar.isWeekendUTC(Instant.now());
```

### Merge two calendars

```java
HolidayCalendar ukCalendar = factory.create("UK");
HolidayCalendar combined = usCalendar.merge(ukCalendar);
List<HolidayDate> combined2025 = combined.calculate(2025);
```

### List all available calendar codes

```java
List<String> codes = factory.listAvailableCodes();
// ["AE", "AED", "AU", "AUD", "BH", "BHD", "CA", "CAD", "CH", "CHF", "CN", "CNY", "DE", "EUR", "FR", "GBP", "IL", "ILS", "JO", "JOD", "JP", "JPY", "KW", "KWD", "MA", "MAD", "QA", "QAR", "SA", "SAR", "SG", "SGD", "TR", "TRY", "UK", "US", "USD"]
```

### Define a custom holiday calendar

Implement `HolidayCalendarService`, register it via `ServiceLoader`, and it will be discovered automatically by `HolidayCalendarFactory`.

```java
public class HolidayCalendarServiceJP implements HolidayCalendarService {

    @Override
    public boolean isProvided(String code) {
        return "JP".equalsIgnoreCase(code);
    }

    @Override
    public String getCode() { return "JP"; }

    @Override
    public String getRegion() { return "Japan National Holidays"; }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
            .code("JP")
            .name("Japan National Holidays")
            .dateRoll(DateRolls.followingMonday())
            .holiday(Holiday.builder()
                .name("New Year's Day")
                .monthDay(Month.JANUARY, 1)
                .build())
            // ... additional holidays
            .build();
    }
}
```

Register the service in `META-INF/services/org.holiday.calendar.HolidayCalendarService` and add a `provides` directive to `module-info.java`.

## Contributing

Contributions are welcome! Please read the [Contributing Guide](CONTRIBUTING.md) before opening an issue or pull request. This project adheres to a [Code of Conduct](CODE_OF_CONDUCT.md) — all participants are expected to uphold it.

## License

Holiday Calendar is released under the [GNU Lesser General Public License, version 2.1](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html).
