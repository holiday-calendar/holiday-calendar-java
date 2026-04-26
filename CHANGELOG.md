# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - 2026-04-26

### Added

- Year-range calculation API: `HolidayCalendar.calculate(LocalDate, LocalDate)` (#109)
- `HolidayCalendarService.dataValidThrough()` to expose the upper bound of reliable data for each calendar (#110)
- Extended Vesak Day (SG) lookup table through 2055 (#111)
- Extended Hari Raya Puasa (SG) lookup table through 2055 (#112)
- Extended Hari Raya Haji (SG) lookup table through 2055 (#113)
- Extended Deepavali (SG) lookup table through 2055 (#114)
- Bounded and documented CNY compensatory working day year range (#115)
- JP/JPY 30-year data coverage audit and findings document (#116)
- 30-year integration test suite covering all 17 calendar codes (2026–2055) (#117)

### Fixed

- JP/JPY: Saturday national holidays incorrectly received a Monday substitute (#125)
- JP/JPY: Cascading 振替休日 not applied when substitute Monday is already a holiday (#126)
- JP/JPY: Sports Day, Marine Day, and Mountain Day returned wrong dates for 2021 Tokyo Olympics relocation (#127)
- JP/JPY: 2020 Tokyo Olympics holiday relocations not implemented (#132)
- JPY: BOJ cascade logic incorrectly bypassed the BOJ Year-Start Holiday when rolling New Year's Day (#133)

## [1.0.0] - 2026-04-16

Initial release.