# Holiday Calendar (Java)

[![Build](https://github.com/holiday-calendar/holiday-calendar-java/actions/workflows/maven-build.yml/badge.svg)](https://github.com/holiday-calendar/holiday-calendar-java/actions/workflows/maven-build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=holiday-calendar_holiday-calendar-java&metric=alert_status)](https://sonarcloud.io/project/overview?id=holiday-calendar_holiday-calendar-java)
[![License: LGPL v2.1](https://img.shields.io/badge/License-LGPL_v2.1-blue.svg)](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html)

A Java library for defining and calculating holiday calendars. Provides an extensible foundation for generating the calendars used to determine when holidays occur and when they are observed worldwide.

## About

Holiday Calendar (Java) answers a common need in financial, scheduling, and business applications: _"Is this date a business day?"_ and _"When is this holiday observed this year?"_

Key design goals:

- **Correct date rolling** — when a holiday falls on a weekend, the library applies configurable rolling rules (e.g. following Monday, previous Friday) to compute the observed date.
- **Extensible by design** — new regional calendars are added via Java's `ServiceLoader` mechanism; no changes to core code required.
- **Modern Java** — built on Java 21, using sealed interfaces, records, and the module system (JPMS).

### Supported Calendars

| Code | Region |
|------|--------|
| `AU` | Australian Securities Exchange (ASX) Holidays |
| `AUD` | Australia (RBA) Holidays |
| `CA` | Canada National Holidays |
| `CAD` | Bank of Canada (Lynx) Holiday Schedule |
| `CH` | Switzerland (SIX) Holidays |
| `CHF` | Switzerland (SIC/SNB) Holidays |
| `CNY` | China (PBOC) Holidays |
| `DE` | Germany (Xetra) Holidays |
| `EUR` | Euro (TARGET2) Holidays |
| `FR` | France (Euronext Paris) Holidays |
| `GBP` | United Kingdom (CHAPS) Holidays |
| `JP` | Japan (TSE) Holidays |
| `JPY` | Japan (BOJ) Holidays |
| `SG` | Singapore (SGX) Holidays |
| `UK` | United Kingdom National Holidays |
| `US` | United States National Holidays |
| `USD` | United States (Federal Reserve) Holidays |

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
  <version>1.1.0-SNAPSHOT</version>
</dependency>

<!-- Western calendars: US, USD, CA, CAD, UK, GBP, CH, CHF, DE, EUR, FR, AU, AUD -->
<dependency>
  <groupId>org.holiday.calendar</groupId>
  <artifactId>holiday-calendar-western</artifactId>
  <version>1.1.0-SNAPSHOT</version>
</dependency>

<!-- APAC calendars: SG, JP, JPY, CNY -->
<dependency>
  <groupId>org.holiday.calendar</groupId>
  <artifactId>holiday-calendar-apac</artifactId>
  <version>1.1.0-SNAPSHOT</version>
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
// ["AU", "AUD", "CA", "CAD", "CH", "CHF", "CNY", "DE", "EUR", "FR", "GBP", "JP", "JPY", "SG", "UK", "US", "USD"]
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