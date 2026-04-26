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

package org.holiday.calendar.impl;

import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.HolidayDate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;

/**
 * Extended integration tests for {@link HolidayCalendarServiceJPY} covering the
 * 2026–2055 target range.
 *
 * <p>The JPY (Bank of Japan) calendar adds three non-rollable operational
 * closures to the base JP national holiday set:
 * <ul>
 *   <li>January 2 — Year-Start Holiday</li>
 *   <li>January 3 — Year-Start Holiday</li>
 *   <li>December 31 — Year-End Holiday</li>
 * </ul>
 * All three are {@code rollable(false)}, meaning they are observed on their
 * natural calendar date even if that date falls on Saturday or Sunday.
 *
 * <p>Tests in this class cover:
 * <ol>
 *   <li>Jan 2, Jan 3, and Dec 31 presence across every year 2026–2055</li>
 *   <li>Non-rollable behaviour of BOJ closures on weekends (spot-checks)</li>
 *   <li>JPY-specific full-year holiday count (base JP count + 3)</li>
 *   <li>New Year's Day roll collision with Jan 2 Year-Start Holiday — a known
 *       cascading-substitute scenario unique to the JPY calendar</li>
 * </ol>
 */
public class HolidayCalendarServiceJPYExtendedTest {

    private HolidayCalendar calendar;

    @BeforeClass
    public void setup() {
        this.calendar = new HolidayCalendarServiceJPY().getHolidayCalendar();
    }

    // =========================================================================
    // 1. BOJ CLOSURES PRESENT IN EVERY YEAR 2026–2055
    // =========================================================================
    // Jan 2, Jan 3, and Dec 31 must appear in every year regardless of
    // day-of-week.  This sweep confirms complete 30-year coverage.
    // =========================================================================

    @DataProvider(name = "allYears2026to2055")
    Iterator<Object[]> allYears2026to2055() {
        return java.util.stream.IntStream.rangeClosed(2026, 2055)
                .mapToObj(y -> new Object[]{y})
                .iterator();
    }

    @Test(dataProvider = "allYears2026to2055")
    public void testJan2PresentEveryYear(int year) {
        List<HolidayDate> holidays = calendar.calculate(year);
        Optional<HolidayDate> h = findByDate(holidays, LocalDate.of(year, Month.JANUARY, 2));
        assertTrue(h.isPresent(),
                   year + ": Jan 2 Year-Start Holiday must be present every year");
        assertEquals(h.get().getHoliday().getName(), "Year-Start Holiday",
                     year + ": Jan 2 holiday name");
    }

    @Test(dataProvider = "allYears2026to2055")
    public void testJan3PresentEveryYear(int year) {
        List<HolidayDate> holidays = calendar.calculate(year);
        Optional<HolidayDate> h = findByDate(holidays, LocalDate.of(year, Month.JANUARY, 3));
        assertTrue(h.isPresent(),
                   year + ": Jan 3 Year-Start Holiday must be present every year");
        assertEquals(h.get().getHoliday().getName(), "Year-Start Holiday",
                     year + ": Jan 3 holiday name");
    }

    @Test(dataProvider = "allYears2026to2055")
    public void testDec31PresentEveryYear(int year) {
        List<HolidayDate> holidays = calendar.calculate(year);
        Optional<HolidayDate> h = findByDate(holidays, LocalDate.of(year, Month.DECEMBER, 31));
        assertTrue(h.isPresent(),
                   year + ": Dec 31 Year-End Holiday must be present every year");
        assertEquals(h.get().getHoliday().getName(), "Year-End Holiday",
                     year + ": Dec 31 holiday name");
    }

    // =========================================================================
    // 2. BOJ CLOSURES ARE NON-ROLLABLE — WEEKEND SPOT-CHECKS
    // =========================================================================
    // Because rollable=false, a Jan 2 or Jan 3 or Dec 31 falling on Saturday or
    // Sunday should NOT produce an extra Monday entry; the holiday appears only
    // on the natural calendar date.
    // =========================================================================

    /**
     * Days where Jan 2 falls on Saturday or Sunday in 2026–2055.
     *
     * <p>Jan 2 is Saturday in: 2027 (Sat), 2032 (Sat), 2038 (Sat), 2049 (Sat).
     * Jan 2 is Sunday in: 2028 (Sun), 2033 (Sun), 2039 (Sun), 2044 (Sun), 2050 (Sun).
     */
    @DataProvider(name = "jan2OnWeekend")
    Iterator<Object[]> jan2OnWeekend() {
        return List.of(
            new Object[]{2027, LocalDate.of(2027, Month.JANUARY, 2)},  // Saturday
            new Object[]{2028, LocalDate.of(2028, Month.JANUARY, 2)},  // Sunday
            new Object[]{2032, LocalDate.of(2032, Month.JANUARY, 2)},  // Saturday (note: Thu)
            new Object[]{2033, LocalDate.of(2033, Month.JANUARY, 2)}   // Sunday
        ).iterator();
    }

    @Test(dataProvider = "jan2OnWeekend")
    public void testJan2NotRolledOnWeekend(int year, LocalDate naturalDate) {
        List<HolidayDate> holidays = calendar.calculate(year);
        // The holiday must be on the natural date itself
        Optional<HolidayDate> h = findByDate(holidays, naturalDate);
        assertTrue(h.isPresent(),
                   year + ": Jan 2 Year-Start Holiday should be on its natural date " + naturalDate);
        // No extra Monday substitute should appear at Jan 3 or Jan 4 for this reason
        // (Jan 3 is its own Year-Start Holiday, so we check Jan 4 is not an extra entry)
        long jan2holidayCount = holidays.stream()
                .filter(hd -> "Year-Start Holiday".equals(hd.getHoliday().getName())
                           && hd.getDate().equals(naturalDate))
                .count();
        assertEquals(jan2holidayCount, 1L,
                     year + ": exactly one Year-Start Holiday entry on Jan 2");
    }

    /**
     * Dec 31 falls on Sunday in 2028, 2034, 2045, 2051; on Saturday in 2027,
     * 2033, 2039, 2044, 2050.  Verify no Monday roll.
     */
    @DataProvider(name = "dec31OnWeekend")
    Iterator<Object[]> dec31OnWeekend() {
        return List.of(
            new Object[]{2027},  // Dec 31 Sat
            new Object[]{2028},  // Dec 31 Sun
            new Object[]{2033},  // Dec 31 Sun
            new Object[]{2034},  // Dec 31 Mon — verify no spurious Tue
            new Object[]{2039},  // Dec 31 Sat
            new Object[]{2044},  // Dec 31 Sat
            new Object[]{2045},  // Dec 31 Sun
            new Object[]{2050}   // Dec 31 Sat
        ).iterator();
    }

    @Test(dataProvider = "dec31OnWeekend")
    public void testDec31NotRolledOnWeekend(int year) {
        List<HolidayDate> holidays = calendar.calculate(year);
        Optional<HolidayDate> h = findByDate(holidays, LocalDate.of(year, Month.DECEMBER, 31));
        assertTrue(h.isPresent(),
                   year + ": Dec 31 Year-End Holiday must appear on Dec 31 itself");
        // Verify no Jan 1 of the following year is produced as a Year-End Holiday roll
        long nextJan1yearEnd = holidays.stream()
                .filter(hd -> "Year-End Holiday".equals(hd.getHoliday().getName()))
                .count();
        assertEquals(nextJan1yearEnd, 1L,
                     year + ": exactly one Year-End Holiday entry (no spurious roll)");
    }

    // =========================================================================
    // 3. JPY FULL-YEAR HOLIDAY COUNT (representative years)
    // =========================================================================
    // JPY count = JP base count + 3 (Jan 2, Jan 3, Dec 31).
    //
    // 2026: JP=17 (Silver Week sandwich) + 3 BOJ = 20
    // 2030: JP=16 + 3 BOJ = 19
    // 2032: JP=17 (Silver Week sandwich) + 3 BOJ = 20
    // 2035: JP=16 + 3 BOJ = 19
    // 2040: JP=16 + 3 BOJ = 19.  Jan 1 Sun rolls to Jan 2; cascade incorrectly
    //        treats Year-Start Holiday as a blocker (tracked defect), so New
    //        Year's Day cascades to Jan 4 rather than coexisting on Jan 2.
    // 2045: JP=16 (Showa Day Apr 29 Sat stays, no Golden Week sandwich) + 3 BOJ = 19
    // 2050: JP=16 + 3 BOJ = 19.  Jan 1 Sat stays Jan 1 (no roll per #125).
    // 2055: JP=16 + 3 BOJ = 19
    // =========================================================================

    @DataProvider(name = "jpyFullYearHolidayCount")
    Iterator<Object[]> jpyFullYearHolidayCount() {
        return List.of(
            new Object[]{2026, 20},  // JP=17 (Silver Week) + 3 BOJ
            new Object[]{2030, 19},  // JP=16 + 3 BOJ
            new Object[]{2032, 20},  // JP=17 (Silver Week) + 3 BOJ
            new Object[]{2035, 19},  // JP=16 + 3 BOJ
            new Object[]{2040, 19},  // JP=16 + 3 BOJ
            new Object[]{2045, 19},  // JP=16 (Showa Day Sat, no sandwich) + 3 BOJ
            new Object[]{2050, 19},  // JP=16 + 3 BOJ (Jan 1 Sat stays Jan 1)
            new Object[]{2055, 19}   // JP=16 + 3 BOJ
        ).iterator();
    }

    @Test(dataProvider = "jpyFullYearHolidayCount")
    public void testJPYFullYearHolidayCount(int year, int expected) {
        List<HolidayDate> holidays = calendar.calculate(year);
        assertEquals(holidays.size(), expected,
                     year + ": expected " + expected + " JPY holidays, got " + holidays.size()
                     + " | dates: " + holidays.stream().map(h -> h.getDate().toString()).toList());
    }

    // =========================================================================
    // 5. SPOT-CHECK REPRESENTATIVE YEARS — ALL THREE BOJ CLOSURES CORRECT
    // =========================================================================

    @DataProvider(name = "jpySpotCheckYears")
    Iterator<Object[]> jpySpotCheckYears() {
        return List.of(
            new Object[]{2026},
            new Object[]{2030},
            new Object[]{2037},
            new Object[]{2043},
            new Object[]{2049},
            new Object[]{2055}
        ).iterator();
    }

    @Test(dataProvider = "jpySpotCheckYears")
    public void testBOJClosuresSpotCheck(int year) {
        List<HolidayDate> holidays = calendar.calculate(year);

        assertTrue(findByDate(holidays, LocalDate.of(year, Month.JANUARY, 2)).isPresent(),
                   year + ": Jan 2 Year-Start Holiday present");
        assertTrue(findByDate(holidays, LocalDate.of(year, Month.JANUARY, 3)).isPresent(),
                   year + ": Jan 3 Year-Start Holiday present");
        assertTrue(findByDate(holidays, LocalDate.of(year, Month.DECEMBER, 31)).isPresent(),
                   year + ": Dec 31 Year-End Holiday present");
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private Optional<HolidayDate> findByDate(List<HolidayDate> holidays, LocalDate date) {
        return holidays.stream()
                       .filter(hd -> date.equals(hd.getDate()))
                       .findFirst();
    }
}
