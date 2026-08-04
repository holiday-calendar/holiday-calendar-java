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
<!-- Add one row per merged calendar, e.g.: | `US` | United States National | National | [US.md](./US.md) | -->

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
| `IL` | MENA | [IL.md](./IL.md) |
<!-- Add one row per merged national calendar. -->

### Central Bank / Settlement

| Code | Region | Doc |
|------|--------|-----|
<!-- Add one row per merged central-bank/settlement calendar. -->

### Market / Exchange

| Code | Region | Doc |
|------|--------|-----|
<!-- Add one row per merged market/exchange calendar. -->

## Related Documentation

- [Observance Pattern Guide](../OBSERVANCE_PATTERNS.md)
- [Porting Guide](../PORTING_GUIDE.md)
- [Implementation Plan (#243)](../243_CALENDAR_REFERENCE_DOC_PLAN.md)
