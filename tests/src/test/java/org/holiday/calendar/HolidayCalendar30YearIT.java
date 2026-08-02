/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2021-2026 The Holiday Calendar Project Contributors
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA
 ******************************************************************************/

package org.holiday.calendar;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

/**
 * 30-year integration test suite validating all 31 implemented holiday calendars
 * over the 2026–2055 target range (issue #117).
 *
 * <p>For every calendar code the suite verifies:
 * <ol>
 *   <li>Range calculation completes without exception and produces a result with
 *       exactly one entry per nominal year.</li>
 *   <li>Each year contains at least {@value #MIN_HOLIDAYS_PER_YEAR} holiday
 *       dates — a baseline guard against silent data loss.</li>
 *   <li>The flat list returned by {@link HolidayCalendar#calculate(int, int)}
 *       contains no null {@code HolidayDate}, holiday, or date references.</li>
 *   <li>That flat list is chronologically ordered (non-decreasing dates).</li>
 * </ol>
 *
 * <p>This class is the acceptance criterion for GA Release 1.1.0.
 */
public class HolidayCalendar30YearIT {

    private static final int FROM_YEAR = 2026;
    private static final int TO_YEAR   = 2055;
    private static final int RANGE_SIZE = TO_YEAR - FROM_YEAR + 1; // 30
    private static final int MIN_HOLIDAYS_PER_YEAR = 5;

    @DataProvider(name = "allCalendarCodes")
    public Iterator<Object[]> allCalendarCodes() {
        return List.of(
                new Object[]{"AE"},
                new Object[]{"AED"},
                new Object[]{"AU"},
                new Object[]{"AUD"},
                new Object[]{"CA"},
                new Object[]{"CAD"},
                new Object[]{"CH"},
                new Object[]{"CHF"},
                new Object[]{"CN"},
                new Object[]{"CNY"},
                new Object[]{"DE"},
                new Object[]{"EUR"},
                new Object[]{"FR"},
                new Object[]{"GBP"},
                new Object[]{"IL"},
                new Object[]{"ILS"},
                new Object[]{"JP"},
                new Object[]{"JPY"},
                new Object[]{"KW"},
                new Object[]{"KWD"},
                new Object[]{"QA"},
                new Object[]{"QAR"},
                new Object[]{"SA"},
                new Object[]{"SAR"},
                new Object[]{"SG"},
                new Object[]{"SGD"},
                new Object[]{"TR"},
                new Object[]{"TRY"},
                new Object[]{"UK"},
                new Object[]{"US"},
                new Object[]{"USD"},
                new Object[]{"XASX"},
                new Object[]{"XLON"},
                new Object[]{"XNYS"},
                new Object[]{"XPAR"},
                new Object[]{"XTSE"}
        ).iterator();
    }

    // =========================================================================
    // 1. RANGE CALCULATION PRODUCES RESULT
    // =========================================================================

    @Test(dataProvider = "allCalendarCodes",
          description = "calculateByYear(2026, 2055) must return a non-null map with exactly 30 year keys")
    public void testRangeCalculationProducesResult(String code) {
        HolidayCalendar calendar = new HolidayCalendarFactory().create(code);
        Map<Integer, List<HolidayDate>> byYear = calendar.calculateByYear(FROM_YEAR, TO_YEAR);

        assertNotNull(byYear, code + ": calculateByYear result must not be null");
        assertEquals(byYear.size(), RANGE_SIZE,
                code + ": result must contain exactly " + RANGE_SIZE + " year keys");

        for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
            assertTrue(byYear.containsKey(year),
                    code + ": result must contain key for year " + year);
        }
    }

    // =========================================================================
    // 2. MINIMUM HOLIDAY COUNT PER YEAR
    // =========================================================================

    @Test(dataProvider = "allCalendarCodes",
          description = "Every year in 2026-2055 must have at least " + MIN_HOLIDAYS_PER_YEAR + " holidays")
    public void testEveryYearMeetsMinimumHolidayCount(String code) {
        HolidayCalendar calendar = new HolidayCalendarFactory().create(code);
        Map<Integer, List<HolidayDate>> byYear = calendar.calculateByYear(FROM_YEAR, TO_YEAR);

        for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
            List<HolidayDate> yearHolidays = byYear.get(year);
            assertNotNull(yearHolidays,
                    code + ": holiday list for " + year + " must not be null");
            assertTrue(yearHolidays.size() >= MIN_HOLIDAYS_PER_YEAR,
                    code + " " + year + ": expected >= " + MIN_HOLIDAYS_PER_YEAR
                            + " holidays, got " + yearHolidays.size());
        }
    }

    // =========================================================================
    // 3. NO NULL DATES OR HOLIDAYS IN FLAT LIST
    // =========================================================================

    @Test(dataProvider = "allCalendarCodes",
          description = "calculate(2026, 2055) flat list must contain no null HolidayDate, holiday, or date references")
    public void testNoNullsInFlatList(String code) {
        HolidayCalendar calendar = new HolidayCalendarFactory().create(code);
        List<HolidayDate> flat = calendar.calculate(FROM_YEAR, TO_YEAR);

        assertNotNull(flat, code + ": flat list must not be null");
        for (int i = 0; i < flat.size(); i++) {
            HolidayDate hd = flat.get(i);
            assertNotNull(hd,
                    code + ": flat list entry at index " + i + " must not be null");
            assertNotNull(hd.holiday(),
                    code + ": holiday at index " + i + " must not be null");
            assertNotNull(hd.date(),
                    code + ": date at index " + i + " must not be null");
        }
    }

    // =========================================================================
    // 4. CHRONOLOGICAL ORDERING
    // =========================================================================

    @Test(dataProvider = "allCalendarCodes",
          description = "calculate(2026, 2055) flat list must be in non-decreasing chronological order")
    public void testFlatListIsChronologicallyOrdered(String code) {
        HolidayCalendar calendar = new HolidayCalendarFactory().create(code);
        List<HolidayDate> flat = calendar.calculate(FROM_YEAR, TO_YEAR);

        for (int i = 1; i < flat.size(); i++) {
            LocalDate prev = flat.get(i - 1).date();
            LocalDate curr = flat.get(i).date();
            assertFalse(curr.isBefore(prev),
                    code + ": dates out of order at index " + i
                            + " — " + prev + " followed by " + curr);
        }
    }

    // =========================================================================
    // 5. ILS EARLY CLOSES (TASE half-day closures) OVER 30 YEARS
    // =========================================================================

    private static final int MIN_EARLY_CLOSES = 6 * RANGE_SIZE; // 6/year * 30 years

    @Test(description = "ILS calculateEarlyCloses across 2026-2055 must yield >= 180 entries, "
            + "no nulls, and be chronologically ordered")
    public void testILSEarlyClosesOver30Years() {
        HolidayCalendar calendar = new HolidayCalendarFactory().create("ILS");

        List<HolidayDate> allEarlyCloses = new java.util.ArrayList<>();
        for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
            List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
            assertNotNull(earlyCloses, "ILS: calculateEarlyCloses(" + year + ") must not be null");
            allEarlyCloses.addAll(earlyCloses);
        }

        assertTrue(allEarlyCloses.size() >= MIN_EARLY_CLOSES,
                "ILS: expected >= " + MIN_EARLY_CLOSES + " early-close entries over "
                        + RANGE_SIZE + " years, got " + allEarlyCloses.size());

        for (int i = 0; i < allEarlyCloses.size(); i++) {
            HolidayDate hd = allEarlyCloses.get(i);
            assertNotNull(hd, "ILS: early-close entry at index " + i + " must not be null");
            assertNotNull(hd.holiday(), "ILS: early-close holiday at index " + i + " must not be null");
            assertNotNull(hd.date(), "ILS: early-close date at index " + i + " must not be null");
        }

        for (int i = 1; i < allEarlyCloses.size(); i++) {
            LocalDate prev = allEarlyCloses.get(i - 1).date();
            LocalDate curr = allEarlyCloses.get(i).date();
            assertFalse(curr.isBefore(prev),
                    "ILS: early-close dates out of order at index " + i
                            + " — " + prev + " followed by " + curr);
        }
    }

    // =========================================================================
    // 6. XLON EARLY CLOSES (LSE Christmas Eve / New Year's Eve half-day closures) OVER 30 YEARS
    // =========================================================================

    // The shift-to-preceding-Friday rule always yields a date (never suppressed),
    // so the count is deterministic: 2 holidays/year * 30 years.
    private static final int EXPECTED_XLON_EARLY_CLOSES = 2 * RANGE_SIZE;

    @Test(description = "XLON calculateEarlyCloses across 2026-2055 must yield exactly 60 entries, "
            + "no nulls, and be chronologically ordered")
    public void testXLONEarlyClosesOver30Years() {
        HolidayCalendar calendar = new HolidayCalendarFactory().create("XLON");

        List<HolidayDate> allEarlyCloses = new java.util.ArrayList<>();
        for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
            List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
            assertNotNull(earlyCloses, "XLON: calculateEarlyCloses(" + year + ") must not be null");
            assertEquals(earlyCloses.size(), 2,
                    "XLON: expected exactly 2 early closes in " + year + ", got " + earlyCloses.size());
            allEarlyCloses.addAll(earlyCloses);
        }

        assertEquals(allEarlyCloses.size(), EXPECTED_XLON_EARLY_CLOSES,
                "XLON: expected exactly " + EXPECTED_XLON_EARLY_CLOSES + " early-close entries over "
                        + RANGE_SIZE + " years, got " + allEarlyCloses.size());

        for (int i = 0; i < allEarlyCloses.size(); i++) {
            HolidayDate hd = allEarlyCloses.get(i);
            assertNotNull(hd, "XLON: early-close entry at index " + i + " must not be null");
            assertNotNull(hd.holiday(), "XLON: early-close holiday at index " + i + " must not be null");
            assertNotNull(hd.date(), "XLON: early-close date at index " + i + " must not be null");
        }

        for (int i = 1; i < allEarlyCloses.size(); i++) {
            LocalDate prev = allEarlyCloses.get(i - 1).date();
            LocalDate curr = allEarlyCloses.get(i).date();
            assertFalse(curr.isBefore(prev),
                    "XLON: early-close dates out of order at index " + i
                            + " — " + prev + " followed by " + curr);
        }
    }

    @Test(description = "UK (national) calculateEarlyCloses must be empty across 2026-2055 "
            + "— early closes are LSE-only market convention, moved to XLON")
    public void testUKHasNoEarlyClosesOver30Years() {
        HolidayCalendar calendar = new HolidayCalendarFactory().create("UK");
        for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
            List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
            assertNotNull(earlyCloses, "UK: calculateEarlyCloses(" + year + ") must not be null");
            assertTrue(earlyCloses.isEmpty(),
                    "UK " + year + ": national calendar must have zero early closes, got " + earlyCloses.size());
        }
    }

    // =========================================================================
    // 7. XNYS EARLY CLOSES (NYSE half-day closures) OVER 30 YEARS
    // =========================================================================

    // Day After Thanksgiving is unconditional; Christmas Eve / July 3rd are suppressed
    // (not shifted) when their anchor holiday falls Monday, Saturday, or Sunday. Count
    // therefore varies 1-3 per year, so expected presence is re-derived from the
    // day-of-week rule for every year rather than hand-fixtured.
    @Test(description = "XNYS calculateEarlyCloses across 2026-2055: count always in [1,3], "
            + "Day After Thanksgiving always present, July 3rd/Christmas Eve presence "
            + "correlates exactly with July 4/December 25 day-of-week, no nulls, chronological order")
    public void testXNYSEarlyClosesOver30Years() {
        HolidayCalendar calendar = new HolidayCalendarFactory().create("XNYS");

        List<HolidayDate> allEarlyCloses = new java.util.ArrayList<>();
        for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
            List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
            assertNotNull(earlyCloses, "XNYS: calculateEarlyCloses(" + year + ") must not be null");

            int count = earlyCloses.size();
            assertTrue(count >= 1 && count <= 3,
                    "XNYS " + year + ": expected count in [1,3], got " + count);

            Set<String> names = earlyCloses.stream()
                    .map(hd -> hd.holiday().getName())
                    .collect(Collectors.toSet());
            assertTrue(names.contains("Day After Thanksgiving"),
                    "XNYS " + year + ": Day After Thanksgiving must always be present");

            DayOfWeek july4Dow = LocalDate.of(year, Month.JULY, 4).getDayOfWeek();
            boolean july3Expected = !DayOfWeek.MONDAY.equals(july4Dow)
                    && !DayOfWeek.SATURDAY.equals(july4Dow)
                    && !DayOfWeek.SUNDAY.equals(july4Dow);
            assertEquals(names.contains("July 3rd"), july3Expected,
                    "XNYS " + year + ": July 3rd presence must match July 4 dow rule (dow=" + july4Dow + ")");

            DayOfWeek dec25Dow = LocalDate.of(year, Month.DECEMBER, 25).getDayOfWeek();
            boolean dec24Expected = !DayOfWeek.MONDAY.equals(dec25Dow)
                    && !DayOfWeek.SATURDAY.equals(dec25Dow)
                    && !DayOfWeek.SUNDAY.equals(dec25Dow);
            assertEquals(names.contains("Christmas Eve"), dec24Expected,
                    "XNYS " + year + ": Christmas Eve presence must match December 25 dow rule (dow=" + dec25Dow + ")");

            allEarlyCloses.addAll(earlyCloses);
        }

        for (int i = 0; i < allEarlyCloses.size(); i++) {
            HolidayDate hd = allEarlyCloses.get(i);
            assertNotNull(hd, "XNYS: early-close entry at index " + i + " must not be null");
            assertNotNull(hd.holiday(), "XNYS: early-close holiday at index " + i + " must not be null");
            assertNotNull(hd.date(), "XNYS: early-close date at index " + i + " must not be null");
        }

        for (int i = 1; i < allEarlyCloses.size(); i++) {
            LocalDate prev = allEarlyCloses.get(i - 1).date();
            LocalDate curr = allEarlyCloses.get(i).date();
            assertFalse(curr.isBefore(prev),
                    "XNYS: early-close dates out of order at index " + i
                            + " — " + prev + " followed by " + curr);
        }
    }

    @Test(description = "US (national) calculateEarlyCloses must be empty across 2026-2055 "
            + "— early closes are NYSE-only market convention, moved to XNYS")
    public void testUSHasNoEarlyClosesOver30Years() {
        HolidayCalendar calendar = new HolidayCalendarFactory().create("US");
        for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
            List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
            assertNotNull(earlyCloses, "US: calculateEarlyCloses(" + year + ") must not be null");
            assertTrue(earlyCloses.isEmpty(),
                    "US " + year + ": national calendar must have zero early closes, got " + earlyCloses.size());
        }
    }

    // =========================================================================
    // 8. XTSE EARLY CLOSES (TSX Christmas Eve half-day closure) OVER 30 YEARS
    // =========================================================================

    // TSX's Christmas Eve early close is suppressed (not shifted) when December 24
    // falls on a weekend, so count is either 0 or 1 per year; expected presence is
    // re-derived from December 24's day-of-week rule for every year rather than
    // hand-fixtured.
    @Test(description = "XTSE calculateEarlyCloses across 2026-2055: count always in [0,1], "
            + "Christmas Eve presence matches December 24 dow rule (excludes Sat/Sun), "
            + "no nulls, chronological order")
    public void testXTSEEarlyClosesOver30Years() {
        HolidayCalendar calendar = new HolidayCalendarFactory().create("XTSE");

        List<HolidayDate> allEarlyCloses = new java.util.ArrayList<>();
        for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
            List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
            assertNotNull(earlyCloses, "XTSE: calculateEarlyCloses(" + year + ") must not be null");

            int count = earlyCloses.size();
            assertTrue(count == 0 || count == 1,
                    "XTSE " + year + ": expected count in [0,1], got " + count);

            DayOfWeek dec24Dow = LocalDate.of(year, Month.DECEMBER, 24).getDayOfWeek();
            boolean dec24Expected = !DayOfWeek.SATURDAY.equals(dec24Dow)
                    && !DayOfWeek.SUNDAY.equals(dec24Dow);
            Set<String> names = earlyCloses.stream()
                    .map(hd -> hd.holiday().getName())
                    .collect(Collectors.toSet());
            assertEquals(names.contains("Christmas Eve"), dec24Expected,
                    "XTSE " + year + ": Christmas Eve presence must match December 24 dow rule (dow=" + dec24Dow + ")");

            allEarlyCloses.addAll(earlyCloses);
        }

        for (int i = 0; i < allEarlyCloses.size(); i++) {
            HolidayDate hd = allEarlyCloses.get(i);
            assertNotNull(hd, "XTSE: early-close entry at index " + i + " must not be null");
            assertNotNull(hd.holiday(), "XTSE: early-close holiday at index " + i + " must not be null");
            assertNotNull(hd.date(), "XTSE: early-close date at index " + i + " must not be null");
        }

        for (int i = 1; i < allEarlyCloses.size(); i++) {
            LocalDate prev = allEarlyCloses.get(i - 1).date();
            LocalDate curr = allEarlyCloses.get(i).date();
            assertFalse(curr.isBefore(prev),
                    "XTSE: early-close dates out of order at index " + i
                            + " — " + prev + " followed by " + curr);
        }
    }

    @Test(description = "CA (national) calculateEarlyCloses must be empty across 2026-2055 "
            + "— early closes are TSX-only market convention, moved to XTSE")
    public void testCAHasNoEarlyClosesOver30Years() {
        HolidayCalendar calendar = new HolidayCalendarFactory().create("CA");
        for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
            List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
            assertNotNull(earlyCloses, "CA: calculateEarlyCloses(" + year + ") must not be null");
            assertTrue(earlyCloses.isEmpty(),
                    "CA " + year + ": national calendar must have zero early closes, got " + earlyCloses.size());
        }
    }

    // =========================================================================
    // 9. DE CHRISTMAS EVE / NEW YEAR'S EVE (Xetra/FWB full non-trading days) OVER 30 YEARS
    // =========================================================================

    // Xetra/FWB's Christmas Eve and New Year's Eve are full non-trading days, not
    // half-day early closes, so they're asserted via calculate() rather than
    // calculateEarlyCloses(). Each is omitted (not shifted) when its date falls on a
    // weekend; expected presence is re-derived from each date's day-of-week rule for
    // every year rather than hand-fixtured.
    @Test(description = "DE calculate() across 2026-2055: Christmas Eve/New Year's Eve present "
            + "unrolled whenever not Sat/Sun, absent (not shifted) when Sat/Sun, no nulls")
    public void testDEChristmasEveAndNewYearsEveOver30Years() {
        HolidayCalendar calendar = new HolidayCalendarFactory().create("DE");

        for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
            List<HolidayDate> all = calendar.calculate(year);
            assertNotNull(all, "DE: calculate(" + year + ") must not be null");

            LocalDate dec24 = LocalDate.of(year, Month.DECEMBER, 24);
            DayOfWeek dec24Dow = dec24.getDayOfWeek();
            boolean dec24Expected = !DayOfWeek.SATURDAY.equals(dec24Dow)
                    && !DayOfWeek.SUNDAY.equals(dec24Dow);
            boolean dec24Present = all.stream().anyMatch(hd ->
                    "Christmas Eve".equals(hd.holiday().getName()) && dec24.equals(hd.date()));
            assertEquals(dec24Present, dec24Expected,
                    "DE " + year + ": Christmas Eve presence/date must match December 24 dow rule (dow=" + dec24Dow + ")");

            LocalDate dec31 = LocalDate.of(year, Month.DECEMBER, 31);
            DayOfWeek dec31Dow = dec31.getDayOfWeek();
            boolean dec31Expected = !DayOfWeek.SATURDAY.equals(dec31Dow)
                    && !DayOfWeek.SUNDAY.equals(dec31Dow);
            boolean dec31Present = all.stream().anyMatch(hd ->
                    "New Year's Eve".equals(hd.holiday().getName()) && dec31.equals(hd.date()));
            assertEquals(dec31Present, dec31Expected,
                    "DE " + year + ": New Year's Eve presence/date must match December 31 dow rule (dow=" + dec31Dow + ")");
        }
    }

    // =========================================================================
    // 10. SG EARLY CLOSES (SGX Christmas Eve / New Year's Eve half-day closures) OVER 30 YEARS
    // =========================================================================

    // Both SGX Eve early closes are suppressed (not shifted) when their date falls on a
    // weekend; since December 24 and December 31 are always exactly 7 days apart, they
    // always share the same day-of-week within a year, so count is always 0 or 2, never 1.
    // Expected presence is re-derived from December 24's day-of-week rule for every year
    // rather than hand-fixtured.
    @Test(description = "SG calculateEarlyCloses across 2026-2055: count always in {0,2}, "
            + "Christmas Eve/New Year's Eve presence matches December 24 dow rule (excludes Sat/Sun) "
            + "and always co-occur, no nulls, chronological order")
    public void testSGEarlyClosesOver30Years() {
        HolidayCalendar calendar = new HolidayCalendarFactory().create("SG");

        List<HolidayDate> allEarlyCloses = new java.util.ArrayList<>();
        for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
            List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
            assertNotNull(earlyCloses, "SG: calculateEarlyCloses(" + year + ") must not be null");

            int count = earlyCloses.size();
            assertTrue(count == 0 || count == 2,
                    "SG " + year + ": expected count in {0,2}, got " + count);

            DayOfWeek dec24Dow = LocalDate.of(year, Month.DECEMBER, 24).getDayOfWeek();
            boolean expected = !DayOfWeek.SATURDAY.equals(dec24Dow)
                    && !DayOfWeek.SUNDAY.equals(dec24Dow);
            Set<String> names = earlyCloses.stream()
                    .map(hd -> hd.holiday().getName())
                    .collect(Collectors.toSet());
            assertEquals(names.contains("Christmas Eve"), expected,
                    "SG " + year + ": Christmas Eve presence must match December 24 dow rule (dow=" + dec24Dow + ")");
            assertEquals(names.contains("New Year's Eve"), expected,
                    "SG " + year + ": New Year's Eve presence must match December 24 dow rule (dow=" + dec24Dow + ")");

            allEarlyCloses.addAll(earlyCloses);
        }

        for (int i = 0; i < allEarlyCloses.size(); i++) {
            HolidayDate hd = allEarlyCloses.get(i);
            assertNotNull(hd, "SG: early-close entry at index " + i + " must not be null");
            assertNotNull(hd.holiday(), "SG: early-close holiday at index " + i + " must not be null");
            assertNotNull(hd.date(), "SG: early-close date at index " + i + " must not be null");
        }

        for (int i = 1; i < allEarlyCloses.size(); i++) {
            LocalDate prev = allEarlyCloses.get(i - 1).date();
            LocalDate curr = allEarlyCloses.get(i).date();
            assertFalse(curr.isBefore(prev),
                    "SG: early-close dates out of order at index " + i
                            + " — " + prev + " followed by " + curr);
        }
    }

    // =========================================================================
    // 11. XPAR EARLY CLOSES (Euronext Paris Christmas Eve / New Year's Eve half-day closures) OVER 30 YEARS
    // =========================================================================

    // Both Euronext Eve early closes are suppressed (not shifted) when their date falls on a
    // weekend; since December 24 and December 31 are always exactly 7 days apart, they always
    // share the same day-of-week within a year, so count is always 0 or 2, never 1. Expected
    // presence is re-derived from December 24's day-of-week rule for every year rather than
    // hand-fixtured.
    //
    // XPAR's Christmas Day and New Year's Day are both rollable under
    // previousFridayOrFollowingMonday, unlike SG, so this section additionally verifies the two
    // genuine cross-list date collisions this produces: Christmas Day rolling back onto
    // December 24 in Dec-25-Saturday years, and New Year's Day rolling back onto the prior
    // December 31 in Jan-1-Saturday years (a year-boundary-crossing collision that a same-year
    // intersection check alone would miss).
    @Test(description = "XPAR calculateEarlyCloses across 2026-2055: count always in {0,2}, "
            + "Christmas Eve/New Year's Eve presence matches December 24 dow rule (excludes Sat/Sun) "
            + "and always co-occur, no nulls, chronological order, and known Christmas Day/New Year's "
            + "Day roll collisions are accounted for")
    public void testXPAREarlyClosesOver30Years() {
        HolidayCalendar calendar = new HolidayCalendarFactory().create("XPAR");

        List<HolidayDate> allEarlyCloses = new java.util.ArrayList<>();
        for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
            List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
            assertNotNull(earlyCloses, "XPAR: calculateEarlyCloses(" + year + ") must not be null");

            int count = earlyCloses.size();
            assertTrue(count == 0 || count == 2,
                    "XPAR " + year + ": expected count in {0,2}, got " + count);

            DayOfWeek dec24Dow = LocalDate.of(year, Month.DECEMBER, 24).getDayOfWeek();
            boolean expected = !DayOfWeek.SATURDAY.equals(dec24Dow)
                    && !DayOfWeek.SUNDAY.equals(dec24Dow);
            Set<String> names = earlyCloses.stream()
                    .map(hd -> hd.holiday().getName())
                    .collect(Collectors.toSet());
            assertEquals(names.contains("Christmas Eve"), expected,
                    "XPAR " + year + ": Christmas Eve presence must match December 24 dow rule (dow=" + dec24Dow + ")");
            assertEquals(names.contains("New Year's Eve"), expected,
                    "XPAR " + year + ": New Year's Eve presence must match December 24 dow rule (dow=" + dec24Dow + ")");

            // Cross-list date collision: empty in ordinary years, exactly one date
            // (December 24) in Dec-25-Saturday years, when Christmas Day rolls back
            // onto Christmas Eve.
            List<HolidayDate> fullDay = calendar.calculate(year);
            Set<LocalDate> fullDayDates = fullDay.stream().map(HolidayDate::date).collect(Collectors.toSet());
            Set<LocalDate> earlyCloseDates = earlyCloses.stream().map(HolidayDate::date).collect(Collectors.toSet());
            Set<LocalDate> intersection = fullDayDates.stream()
                    .filter(earlyCloseDates::contains)
                    .collect(Collectors.toSet());
            boolean isChristmasDaySaturdayRollYear =
                    DayOfWeek.SATURDAY.equals(LocalDate.of(year, Month.DECEMBER, 25).getDayOfWeek());
            if (isChristmasDaySaturdayRollYear) {
                assertEquals(intersection, Set.of(LocalDate.of(year, Month.DECEMBER, 24)),
                        "XPAR " + year + ": expected exactly the known Christmas Day/Christmas Eve collision");
            } else {
                assertTrue(intersection.isEmpty(), "XPAR " + year + ": unexpected cross-list date collision: " + intersection);
            }

            // Cross-year collision: New Year's Day rolling back onto this year's December 31
            // whenever January 1 of the following year falls on a Saturday.
            boolean isNewYearsDaySaturdayRollYear =
                    DayOfWeek.SATURDAY.equals(LocalDate.of(year + 1, Month.JANUARY, 1).getDayOfWeek());
            if (isNewYearsDaySaturdayRollYear) {
                LocalDate dec31 = LocalDate.of(year, Month.DECEMBER, 31);
                boolean newYearsDayPresent = calendar.calculate(year + 1).stream()
                        .anyMatch(hd -> "New Year's Day".equals(hd.holiday().getName()) && dec31.equals(hd.date()));
                boolean newYearsEvePresent = names.contains("New Year's Eve");
                assertTrue(newYearsDayPresent,
                        "XPAR " + year + ": New Year's Day (rolled back from Jan 1, " + (year + 1) + ") must appear on Dec 31, " + year);
                assertTrue(newYearsEvePresent,
                        "XPAR " + year + ": New Year's Eve early close must independently appear on Dec 31, " + year);
            }

            allEarlyCloses.addAll(earlyCloses);
        }

        for (int i = 0; i < allEarlyCloses.size(); i++) {
            HolidayDate hd = allEarlyCloses.get(i);
            assertNotNull(hd, "XPAR: early-close entry at index " + i + " must not be null");
            assertNotNull(hd.holiday(), "XPAR: early-close holiday at index " + i + " must not be null");
            assertNotNull(hd.date(), "XPAR: early-close date at index " + i + " must not be null");
        }

        for (int i = 1; i < allEarlyCloses.size(); i++) {
            LocalDate prev = allEarlyCloses.get(i - 1).date();
            LocalDate curr = allEarlyCloses.get(i).date();
            assertFalse(curr.isBefore(prev),
                    "XPAR: early-close dates out of order at index " + i
                            + " — " + prev + " followed by " + curr);
        }
    }

    // =========================================================================
    // 12. XASX EARLY CLOSES (ASX Christmas Eve / New Year's Eve half-day closures) OVER 30 YEARS
    // =========================================================================

    // Both ASX Eve early closes are suppressed (not shifted) when their date falls on a
    // weekend; since December 24 and December 31 are always exactly 7 days apart, they always
    // share the same day-of-week within a year, so count is always 0 or 2, never 1. Expected
    // presence is re-derived from December 24's day-of-week rule for every year rather than
    // hand-fixtured.
    //
    // XASX's Christmas Day and New Year's Day are both rollable under
    // previousFridayOrFollowingMonday, same as FR, so this section additionally verifies the
    // two genuine cross-list date collisions this produces: Christmas Day rolling back onto
    // December 24 in Dec-25-Saturday years, and New Year's Day rolling back onto the prior
    // December 31 in Jan-1-Saturday years (a year-boundary-crossing collision that a same-year
    // intersection check alone would miss).
    @Test(description = "XASX calculateEarlyCloses across 2026-2055: count always in {0,2}, "
            + "Christmas Eve/New Year's Eve presence matches December 24 dow rule (excludes Sat/Sun) "
            + "and always co-occur, no nulls, chronological order, and known Christmas Day/New Year's "
            + "Day roll collisions are accounted for")
    public void testXASXEarlyClosesOver30Years() {
        HolidayCalendar calendar = new HolidayCalendarFactory().create("XASX");

        List<HolidayDate> allEarlyCloses = new java.util.ArrayList<>();
        for (int year = FROM_YEAR; year <= TO_YEAR; year++) {
            List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
            assertNotNull(earlyCloses, "XASX: calculateEarlyCloses(" + year + ") must not be null");

            int count = earlyCloses.size();
            assertTrue(count == 0 || count == 2,
                    "XASX " + year + ": expected count in {0,2}, got " + count);

            DayOfWeek dec24Dow = LocalDate.of(year, Month.DECEMBER, 24).getDayOfWeek();
            boolean expected = !DayOfWeek.SATURDAY.equals(dec24Dow)
                    && !DayOfWeek.SUNDAY.equals(dec24Dow);
            Set<String> names = earlyCloses.stream()
                    .map(hd -> hd.holiday().getName())
                    .collect(Collectors.toSet());
            assertEquals(names.contains("Christmas Eve"), expected,
                    "XASX " + year + ": Christmas Eve presence must match December 24 dow rule (dow=" + dec24Dow + ")");
            assertEquals(names.contains("New Year's Eve"), expected,
                    "XASX " + year + ": New Year's Eve presence must match December 24 dow rule (dow=" + dec24Dow + ")");

            // Cross-list date collision: empty in ordinary years, exactly one date
            // (December 24) in Dec-25-Saturday years, when Christmas Day rolls back
            // onto Christmas Eve.
            List<HolidayDate> fullDay = calendar.calculate(year);
            Set<LocalDate> fullDayDates = fullDay.stream().map(HolidayDate::date).collect(Collectors.toSet());
            Set<LocalDate> earlyCloseDates = earlyCloses.stream().map(HolidayDate::date).collect(Collectors.toSet());
            Set<LocalDate> intersection = fullDayDates.stream()
                    .filter(earlyCloseDates::contains)
                    .collect(Collectors.toSet());
            boolean isChristmasDaySaturdayRollYear =
                    DayOfWeek.SATURDAY.equals(LocalDate.of(year, Month.DECEMBER, 25).getDayOfWeek());
            if (isChristmasDaySaturdayRollYear) {
                assertEquals(intersection, Set.of(LocalDate.of(year, Month.DECEMBER, 24)),
                        "XASX " + year + ": expected exactly the known Christmas Day/Christmas Eve collision");
            } else {
                assertTrue(intersection.isEmpty(), "XASX " + year + ": unexpected cross-list date collision: " + intersection);
            }

            // Cross-year collision: New Year's Day rolling back onto this year's December 31
            // whenever January 1 of the following year falls on a Saturday.
            boolean isNewYearsDaySaturdayRollYear =
                    DayOfWeek.SATURDAY.equals(LocalDate.of(year + 1, Month.JANUARY, 1).getDayOfWeek());
            if (isNewYearsDaySaturdayRollYear) {
                LocalDate dec31 = LocalDate.of(year, Month.DECEMBER, 31);
                boolean newYearsDayPresent = calendar.calculate(year + 1).stream()
                        .anyMatch(hd -> "New Year's Day".equals(hd.holiday().getName()) && dec31.equals(hd.date()));
                boolean newYearsEvePresent = names.contains("New Year's Eve");
                assertTrue(newYearsDayPresent,
                        "XASX " + year + ": New Year's Day (rolled back from Jan 1, " + (year + 1) + ") must appear on Dec 31, " + year);
                assertTrue(newYearsEvePresent,
                        "XASX " + year + ": New Year's Eve early close must independently appear on Dec 31, " + year);
            }

            allEarlyCloses.addAll(earlyCloses);
        }

        for (int i = 0; i < allEarlyCloses.size(); i++) {
            HolidayDate hd = allEarlyCloses.get(i);
            assertNotNull(hd, "XASX: early-close entry at index " + i + " must not be null");
            assertNotNull(hd.holiday(), "XASX: early-close holiday at index " + i + " must not be null");
            assertNotNull(hd.date(), "XASX: early-close date at index " + i + " must not be null");
        }

        for (int i = 1; i < allEarlyCloses.size(); i++) {
            LocalDate prev = allEarlyCloses.get(i - 1).date();
            LocalDate curr = allEarlyCloses.get(i).date();
            assertFalse(curr.isBefore(prev),
                    "XASX: early-close dates out of order at index " + i
                            + " — " + prev + " followed by " + curr);
        }
    }

}
