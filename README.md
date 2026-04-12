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
| `US` | United States National Holidays |
| `CA` | Canada National Holidays |
| `UK` | United Kingdom National Holidays |
| `CH` | Switzerland National Holidays |
| `DE` | Germany National Holidays |
| `EUR` | Euro (TARGET2) Holidays |
| `FR` | France National Holidays |
| `AU` | Australian Securities Exchange (ASX) Holidays |
| `AUD` | Australia (RBA) Holidays |
| `SG` | Singapore Exchange (SGX) Holidays |
| `JP` | Japan — Tokyo Stock Exchange (TSE/JPX) Holidays |
| `JPY` | Japan — Bank of Japan (BOJ/BOJNET) Holidays |

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
  <groupId>com.github.davejoyce.calendar</groupId>
  <artifactId>holiday-calendar-core</artifactId>
  <version>0.0.1</version>
</dependency>

<!-- Western calendars: US, USD, CA, UK, CH, DE, EUR, FR, AU, AUD -->
<dependency>
  <groupId>com.github.davejoyce.calendar</groupId>
  <artifactId>holiday-calendar-western</artifactId>
  <version>0.0.1</version>
</dependency>

<!-- APAC calendars: SG, JP, JPY -->
<dependency>
  <groupId>com.github.davejoyce.calendar</groupId>
  <artifactId>holiday-calendar-apac</artifactId>
  <version>0.0.1</version>
</dependency>
```

## Usage

### Look up a calendar and calculate holidays for a year

```java
import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.HolidayCalendarFactory;
import com.github.davejoyce.calendar.HolidayDate;

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
// ["AU", "AUD", "CA", "DE", "EUR", "FR", "JP", "JPY", "SG", "CH", "UK", "US", "USD"]
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

Register the service in `META-INF/services/com.github.davejoyce.calendar.HolidayCalendarService` and add a `provides` directive to `module-info.java`.

## Contributing

Contributions are welcome! Please read the [Contributing Guide](CONTRIBUTING.md) before opening an issue or pull request. This project adheres to a [Code of Conduct](CODE_OF_CONDUCT.md) — all participants are expected to uphold it.

## License

Holiday Calendar is released under the [GNU Lesser General Public License, version 2.1](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html).