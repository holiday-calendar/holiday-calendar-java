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
| `CN` | China National Holidays | National | [CN.md](./CN.md) |
| `CNY` | China (PBOC) Holidays | Central Bank/Settlement | [CNY.md](./CNY.md) |
| `JP` | Japan National Holidays | National | [JP.md](./JP.md) |
| `JPY` | Japan (BOJ) Holidays | Central Bank/Settlement | [JPY.md](./JPY.md) |
| `SG` | Singapore National Holidays | National | [SG.md](./SG.md) |
| `SGD` | Singapore (MAS/MEPS+) Holidays | Central Bank/Settlement | [SGD.md](./SGD.md) |
| `XSES` | Singapore Exchange (SGX) Holidays | Market/Exchange | [XSES.md](./XSES.md) |
<!-- All 7 APAC calendars now documented. -->

### MENA (`holiday-calendar-mena`)

| Code | Name | Category | Doc |
|------|------|----------|-----|
| `IL` | Israel National Holidays | National | [IL.md](./IL.md) |
| `ILS` | Israel (TASE/Bank of Israel) Holidays | Central Bank/Settlement | [ILS.md](./ILS.md) |
| `AE` | UAE (National) Holidays | National | [AE.md](./AE.md) |
| `AED` | UAE (CBUAE/DFM/ADX) Holidays | Central Bank/Settlement | [AED.md](./AED.md) |
| `SA` | Saudi Arabia (National) Holidays | National | [SA.md](./SA.md) |
| `SAR` | Saudi Arabia (Tadawul/SAMA) Holidays | Central Bank/Settlement | [SAR.md](./SAR.md) |
| `TR` | Turkey (National) Holidays | National | [TR.md](./TR.md) |
| `TRY` | Turkey (BIST/TCMB) Holidays | Central Bank/Settlement | [TRY.md](./TRY.md) |
| `QA` | Qatar (National) Holidays | National | [QA.md](./QA.md) |
| `QAR` | Qatar (QSE/QCB) Holidays | Central Bank/Settlement | [QAR.md](./QAR.md) |
| `EG` | Egypt (National) Holidays | National | [EG.md](./EG.md) |
| `EGP` | Egypt (EGX/CBE) Holidays | Central Bank/Settlement | [EGP.md](./EGP.md) |
| `KW` | Kuwait (National) Holidays | National | [KW.md](./KW.md) |
| `KWD` | Kuwait (Boursa Kuwait/CBK) Holidays | Central Bank/Settlement | [KWD.md](./KWD.md) |
| `BH` | Bahrain (National) Holidays | National | [BH.md](./BH.md) |
| `BHD` | Bahrain (Boursa Bahrain/CBB) Holidays | Central Bank/Settlement | [BHD.md](./BHD.md) |
| `MA` | Morocco (National) Holidays | National | [MA.md](./MA.md) |
| `MAD` | Morocco (CSE/BAM) Holidays | Central Bank/Settlement | [MAD.md](./MAD.md) |
| `JO` | Jordan (National) Holidays | National | [JO.md](./JO.md) |
| `JOD` | Jordan (ASE/CBJ) Holidays | Central Bank/Settlement | [JOD.md](./JOD.md) |
<!-- All 20 MENA calendars now documented. -->

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
| `CN` | APAC | [CN.md](./CN.md) |
| `JP` | APAC | [JP.md](./JP.md) |
| `SG` | APAC | [SG.md](./SG.md) |
| `IL` | MENA | [IL.md](./IL.md) |
| `AE` | MENA | [AE.md](./AE.md) |
| `SA` | MENA | [SA.md](./SA.md) |
| `TR` | MENA | [TR.md](./TR.md) |
| `QA` | MENA | [QA.md](./QA.md) |
| `EG` | MENA | [EG.md](./EG.md) |
| `KW` | MENA | [KW.md](./KW.md) |
| `BH` | MENA | [BH.md](./BH.md) |
| `MA` | MENA | [MA.md](./MA.md) |
| `JO` | MENA | [JO.md](./JO.md) |
<!-- All national calendars now documented. -->

### Central Bank / Settlement

| Code | Region | Doc |
|------|--------|-----|
| `CHF` | Western | [CHF.md](./CHF.md) |
| `USD` | Western | [USD.md](./USD.md) |
| `CAD` | Western | [CAD.md](./CAD.md) |
| `GBP` | Western | [GBP.md](./GBP.md) |
| `AUD` | Western | [AUD.md](./AUD.md) |
| `EUR` | Western | [EUR.md](./EUR.md) |
| `CNY` | APAC | [CNY.md](./CNY.md) |
| `JPY` | APAC | [JPY.md](./JPY.md) |
| `SGD` | APAC | [SGD.md](./SGD.md) |
| `ILS` | MENA | [ILS.md](./ILS.md) |
| `AED` | MENA | [AED.md](./AED.md) |
| `SAR` | MENA | [SAR.md](./SAR.md) |
| `TRY` | MENA | [TRY.md](./TRY.md) |
| `QAR` | MENA | [QAR.md](./QAR.md) |
| `EGP` | MENA | [EGP.md](./EGP.md) |
| `KWD` | MENA | [KWD.md](./KWD.md) |
| `BHD` | MENA | [BHD.md](./BHD.md) |
| `MAD` | MENA | [MAD.md](./MAD.md) |
| `JOD` | MENA | [JOD.md](./JOD.md) |
<!-- All central-bank/settlement calendars now documented. -->

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
| `XSES` | APAC | [XSES.md](./XSES.md) |
<!-- All market/exchange calendars now documented. All 47 calendars are now documented. -->

## Related Documentation

- [Observance Pattern Guide](../OBSERVANCE_PATTERNS.md)
- [Porting Guide](../PORTING_GUIDE.md)
- [Implementation Plan (#243)](../243_CALENDAR_REFERENCE_DOC_PLAN.md)
