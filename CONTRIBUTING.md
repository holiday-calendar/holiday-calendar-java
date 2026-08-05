# Contributing to Holiday Calendar (Java)

First off, thank you for taking the time to contribute! :+1:

### Table of Contents

* [Code of Conduct](#code-of-conduct)
* [Copyright Header](#copyright-header)
* [Adding a New Observance](#adding-a-new-observance)
* [Calendar Reference Docs](#calendar-reference-docs)
* [How to Contribute](#how-to-contribute)
  * [Create an issue](#create-an-issue)
  * [Report a security vulnerability](#report-a-security-vulnerability)
  * [Submit a pull request](#submit-a-pull-request)

### Code of Conduct

This project is governed by the [Holiday Calendar Code of Conduct](CODE_OF_CONDUCT.md).
By participating, you are expected to uphold this code. Please report
unacceptable behavior to conduct@holiday-calendar.org.

### Copyright Header

All Java source files must include the project's LGPL-2.1 copyright header. IDE templates are provided to make this easy when creating new files.

**IntelliJ IDEA:** The copyright profile is automatically loaded from `.idea/copyright/`. With the Copyright plugin enabled, use **Code > Update Copyright** to insert or refresh the header in a file.

**Eclipse:** Import the template via **Window > Preferences > Java > Code Style > Code Templates > Import**, selecting `ide-templates/eclipse/codetemplates.xml`. Eclipse will then insert the header automatically when creating new Java files.

**Visual Studio Code:** Type `hcheader` and press Tab in any `.java` file to expand the full header. No extension required — the snippet is defined in `.vscode/holiday-calendar.code-snippets`.

### Adding a New Observance

Contributing a new floating holiday or half-day-close (early close) date? See
[docs/OBSERVANCE_PATTERNS.md](docs/OBSERVANCE_PATTERNS.md) for the `Observance`
interface contract, the implementation patterns used across the codebase
(algorithm-based, nth-weekday-of-month, CSV-backed lookup, astronomical
fallback), the Early Close pattern, a step-by-step guide, and the testing
checklist. Start there before opening a PR that adds or changes an observance.

### Calendar Reference Docs

Any PR that adds a new `HolidayCalendarService` implementation, or changes an
existing one's holiday list, roll strategy, weekend days, or early-close
behavior, must add or update the corresponding file at
`docs/calendars/<CODE>.md` (see the [docs/calendars/](docs/calendars/)
directory) using the template in
[docs/calendars/TEMPLATE.md](docs/calendars/TEMPLATE.md). New calendars must
also add a row to both tables in
[docs/calendars/README.md](docs/calendars/README.md). A CI check flags PRs
that change a calendar's source without a matching doc change; if it fires
on your PR, update the doc before requesting review.

### How to Contribute

#### Create an issue

If you find a problem with the source code, [search if an issue already exists](https://github.com/holiday-calendar/holiday-calendar-java/issues).
If one doesn't exist, you can [open a new issue](https://github.com/holiday-calendar/holiday-calendar-java/issues/new/choose).

#### Report a security vulnerability

If you believe you have found a security vulnerability, follow the [Security Policy](SECURITY.md) to report it correctly.

#### Submit a pull request

Scan through our [existing issues](https://github.com/holiday-calendar/holiday-calendar-java/issues) to find one that
interests you. You can narrow down the search using `labels` as filters. If you find an issue to work on, you are
welcome to:
1. [Fork this project](https://github.com/holiday-calendar/holiday-calendar-java/fork).
2. In your fork repository, create the fix, including unit tests that validate it.
3. Open a PR with the fix.
