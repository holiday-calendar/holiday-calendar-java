# Calendar Reference Index

This directory holds a permanent, per-calendar reference document for every
implemented `HolidayCalendarService` in this project — primary-source-cited
weekend/roll conventions, holiday tables, early closes, and notes on
peculiarities that aren't obvious from the code. See
[docs/243_CALENDAR_REFERENCE_DOC_PLAN.md](../243_CALENDAR_REFERENCE_DOC_PLAN.md)
for the full plan, and [TEMPLATE.md](./TEMPLATE.md) for the authoring
template used by every file in this directory.

Rows below are added incrementally as each calendar's reference doc merges —
this index is not expected to be fully populated on day one.

## By Region

### Western (`holiday-calendar-western`)

| Code | Name | Category | Doc |
|------|------|----------|-----|
| `CH` | Switzerland National Holidays | National | [CH.md](./CH.md) |
| `CHF` | Switzerland (SIC/SNB) Holidays | Central Bank/Settlement | [CHF.md](./CHF.md) |
| `XSWX` | SIX Swiss Exchange Holidays | Market/Exchange | [XSWX.md](./XSWX.md) |
| `US` | United States National Holidays | National | [US.md](./US.md) |
| `USD` | United States (Federal Reserve) Holidays | Central Bank/Settlement | [USD.md](./USD.md) |
| `XNYS` | New York Stock Exchange (NYSE) Holidays | Market/Exchange | [XNYS.md](./XNYS.md) |
| `CA` | Canada National Holidays | National | [CA.md](./CA.md) |
| `CAD` | Bank of Canada (Lynx) Holiday Schedule | Central Bank/Settlement | [CAD.md](./CAD.md) |
| `XTSE` | Toronto Stock Exchange (TSX) Holidays | Market/Exchange | [XTSE.md](./XTSE.md) |
| `UK` | United Kingdom National Holidays | National | [UK.md](./UK.md) |
| `GBP` | United Kingdom (CHAPS) Holidays | Central Bank/Settlement | [GBP.md](./GBP.md) |
| `XLON` | London Stock Exchange (LSE) Holidays | Market/Exchange | [XLON.md](./XLON.md) |
| `DE` | Germany National Holidays | National | [DE.md](./DE.md) |
| `XETR` | Deutsche Börse Xetra Holidays | Market/Exchange | [XETR.md](./XETR.md) |
| `FR` | France National Holidays | National | [FR.md](./FR.md) |
| `XPAR` | Euronext Paris Holidays | Market/Exchange | [XPAR.md](./XPAR.md) |
| `AU` | Australia National Holidays | National | [AU.md](./AU.md) |
| `AUD` | Australia (RBA) Holidays | Central Bank/Settlement | [AUD.md](./AUD.md) |
| `XASX` | Australian Securities Exchange (ASX) Holidays | Market/Exchange | [XASX.md](./XASX.md) |
| `EUR` | Euro (TARGET2) Holidays | Central Bank/Settlement | [EUR.md](./EUR.md) |
<!-- All 20 Western calendars now documented. -->

### APAC (`holiday-calendar-apac`)

| Code | Name | Category | Doc |
|------|------|----------|-----|
<!-- Add one row per merged calendar. -->

### MENA (`holiday-calendar-mena`)

| Code | Name | Category | Doc |
|------|------|----------|-----|
| `IL` | Israel National Holidays | National | [IL.md](./IL.md) |
<!-- Add one row per merged calendar. -->

## By Category

### National

| Code | Region | Doc |
|------|--------|-----|
| `CH` | Western | [CH.md](./CH.md) |
| `US` | Western | [US.md](./US.md) |
| `CA` | Western | [CA.md](./CA.md) |
| `UK` | Western | [UK.md](./UK.md) |
| `DE` | Western | [DE.md](./DE.md) |
| `FR` | Western | [FR.md](./FR.md) |
| `AU` | Western | [AU.md](./AU.md) |
| `IL` | MENA | [IL.md](./IL.md) |
<!-- Add one row per merged national calendar. -->

### Central Bank / Settlement

| Code | Region | Doc |
|------|--------|-----|
| `CHF` | Western | [CHF.md](./CHF.md) |
| `USD` | Western | [USD.md](./USD.md) |
| `CAD` | Western | [CAD.md](./CAD.md) |
| `GBP` | Western | [GBP.md](./GBP.md) |
| `AUD` | Western | [AUD.md](./AUD.md) |
| `EUR` | Western | [EUR.md](./EUR.md) |
<!-- Add one row per merged central-bank/settlement calendar. -->

### Market / Exchange

| Code | Region | Doc |
|------|--------|-----|
| `XSWX` | Western | [XSWX.md](./XSWX.md) |
| `XNYS` | Western | [XNYS.md](./XNYS.md) |
| `XTSE` | Western | [XTSE.md](./XTSE.md) |
| `XLON` | Western | [XLON.md](./XLON.md) |
| `XETR` | Western | [XETR.md](./XETR.md) |
| `XPAR` | Western | [XPAR.md](./XPAR.md) |
| `XASX` | Western | [XASX.md](./XASX.md) |
<!-- Add one row per merged market/exchange calendar. -->

## Related Documentation

- [Observance Pattern Guide](../OBSERVANCE_PATTERNS.md)
- [Porting Guide](../PORTING_GUIDE.md)
- [Implementation Plan (#243)](../243_CALENDAR_REFERENCE_DOC_PLAN.md)
