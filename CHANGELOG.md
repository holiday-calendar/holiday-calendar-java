# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.4.0] - 2026-05-22

### Added

- Qatar holiday calendars: QA (national) + QAR (QSE/Qatar Central Bank) (#162)
- Kuwait holiday calendars: KW (national) + KWD (Boursa Kuwait/CBK) (#163)
- Egypt holiday calendars: EG (national) + EGP (EGX/Central Bank of Egypt) (#164)
- Bahrain holiday calendars: BH (national) + BHD (Boursa Bahrain/CBB) (#165)
- Morocco holiday calendars: MA (national) + MAD (Casablanca SE/Bank Al-Maghrib) (#166)
- Jordan holiday calendars: JO (national) + JOD (ASE/Central Bank of Jordan) (#167)

### Changed

- Upgraded GitHub Actions to Node.js 24-compatible versions (#185)

## [1.3.1] - 2026-05-19

### Fixed

- Missing `<caption>` in `IndependenceDay` Javadoc table causing Javadoc generation failure

## [1.3.0] - 2026-05-19

### Added

- New `holiday-calendar-mena` Maven module scaffolding (#155)
- Islamic holiday CSV lookup infrastructure for MENA calendars (#156)
- `holiday-calendar-mena` added to tests aggregation module (#157)
- UAE holiday calendars: AE (national) + AED (CBUAE/DFM/ADX) (#158)
- Saudi Arabia holiday calendars: SA (national) + SAR (Tadawul/SAMA) (#159)
- Israel holiday calendars: IL (national) + ILS (TASE/Bank of Israel) (#160)
- Turkey holiday calendars: TR (national) + TRY (BIST/TCMB) (#161)
- Yom Hazikaron (4 Iyar) added to IL national calendar (#177)

### Fixed

- Undefined `aggregate.report.dir` in child module `sonar.coverage.jacoco.xmlReportPaths` (#171)

## [1.2.0] - 2026-05-07

### Added

- Singapore MAS MEPS+ holiday calendar (SGD) (#139)
- China National Public Holidays calendar (CN) (#140)
- Shared `CsvObservanceLoader` extracted from CNY pattern (#141)
- SG non-Gregorian observances migrated to CSV-backed implementation (#142)
- Automated README.md version synchronisation with POM (#143)
- Updated README template with all calendars (#150)

## [1.1.0] - 2026-04-26

### Added

- Year-range calculation API: `HolidayCalendar.calculate(LocalDate, LocalDate)` (#109)
- `HolidayCalendarService.dataValidThrough()` to expose the upper bound of reliable data for each calendar (#110)
- Extended Vesak Day (SG) lookup table through 2055 (#111)
- Extended Hari Raya Puasa (SG) lookup table through 2055 (#112)
- Extended Hari Raya Haji (SG) lookup table through 2055 (#113)
- Extended Deepavali (SG) lookup table through 2055 (#114)
- Bounded and documented CNY compensatory working day year range (#115)
- Extended JP/JPY range tests and 振替休日 sentinel coverage through 2055 (#116)
- 30-year integration test suite covering all 17 calendar codes (2026–2055) (#117)

### Fixed

- JP/JPY: Saturday national holidays incorrectly received a Monday substitute (#125)
- JP/JPY: Cascading 振替休日 not applied when substitute Monday is already a holiday (#126)
- JP/JPY: Sports Day, Marine Day, and Mountain Day returned wrong dates for 2021 Tokyo Olympics relocation (#127)
- JP/JPY: 2020 Tokyo Olympics holiday relocations not implemented (#132)
- JPY: BOJ cascade logic incorrectly bypassed the BOJ Year-Start Holiday when rolling New Year's Day (#133)

## [1.0.0] - 2026-04-16

Initial release.