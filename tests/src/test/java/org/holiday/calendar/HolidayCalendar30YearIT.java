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

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
                new Object[]{"USD"}
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

}
