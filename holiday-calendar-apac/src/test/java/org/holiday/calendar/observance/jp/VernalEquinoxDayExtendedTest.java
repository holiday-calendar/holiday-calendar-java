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

package org.holiday.calendar.observance.jp;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Extended parametrized tests for {@link VernalEquinoxDay} covering every year
 * in the 2026–2055 target range.
 *
 * <p>Expected dates are derived from the NAOJ formula:
 * <pre>
 *   day = floor(20.8431 + 0.242194 * (year - 1980) - floor((year - 1980) / 4))
 * </pre>
 * The formula yields March 20 for most years in this range, but gives March 21 in
 * 2027, 2031, 2035, 2039, 2043, 2047, 2051, and 2055 — all years of the form
 * (year − 1980) mod 4 == 3.  No March 22 results occur before 2100.
 *
 * <p>The rolling of these raw dates (when Sunday → Monday) is tested at the
 * {@link HolidayCalendarServiceJPExtendedTest} integration level; this class
 * tests only the observance formula output.
 */
public class VernalEquinoxDayExtendedTest {

    private final VernalEquinoxDay observance = new VernalEquinoxDay();

    // ── 2026–2055 full parametrized sweep ─────────────────────────────────────

    /**
     * All 30 vernal equinox dates from 2026 to 2055.
     *
     * <p>Mar 21 years: 2027, 2031, 2035, 2039, 2043, 2047, 2051, 2055.
     * Mar 20 years: all remaining (2026, 2028–2030, 2032–2034, 2036–2038,
     * 2040–2042, 2044–2046, 2048–2050, 2052–2054).
     */
    @DataProvider(name = "vernalEquinox2026to2055")
    Iterator<Object[]> vernalEquinox2026to2055() {
        return List.of(
            new Object[]{2026, LocalDate.of(2026, Month.MARCH, 20)},  // Fri
            new Object[]{2027, LocalDate.of(2027, Month.MARCH, 21)},  // Sun (rolls at service level)
            new Object[]{2028, LocalDate.of(2028, Month.MARCH, 20)},  // Mon
            new Object[]{2029, LocalDate.of(2029, Month.MARCH, 20)},  // Tue
            new Object[]{2030, LocalDate.of(2030, Month.MARCH, 20)},  // Wed
            new Object[]{2031, LocalDate.of(2031, Month.MARCH, 21)},  // Fri
            new Object[]{2032, LocalDate.of(2032, Month.MARCH, 20)},  // Sat (no roll — Sat not rollable)
            new Object[]{2033, LocalDate.of(2033, Month.MARCH, 20)},  // Sun (rolls at service level)
            new Object[]{2034, LocalDate.of(2034, Month.MARCH, 20)},  // Mon
            new Object[]{2035, LocalDate.of(2035, Month.MARCH, 21)},  // Wed
            new Object[]{2036, LocalDate.of(2036, Month.MARCH, 20)},  // Thu
            new Object[]{2037, LocalDate.of(2037, Month.MARCH, 20)},  // Fri
            new Object[]{2038, LocalDate.of(2038, Month.MARCH, 20)},  // Sat
            new Object[]{2039, LocalDate.of(2039, Month.MARCH, 21)},  // Mon
            new Object[]{2040, LocalDate.of(2040, Month.MARCH, 20)},  // Tue
            new Object[]{2041, LocalDate.of(2041, Month.MARCH, 20)},  // Wed
            new Object[]{2042, LocalDate.of(2042, Month.MARCH, 20)},  // Thu
            new Object[]{2043, LocalDate.of(2043, Month.MARCH, 21)},  // Sat
            new Object[]{2044, LocalDate.of(2044, Month.MARCH, 20)},  // Sun (rolls at service level)
            new Object[]{2045, LocalDate.of(2045, Month.MARCH, 20)},  // Mon
            new Object[]{2046, LocalDate.of(2046, Month.MARCH, 20)},  // Tue
            new Object[]{2047, LocalDate.of(2047, Month.MARCH, 21)},  // Thu
            new Object[]{2048, LocalDate.of(2048, Month.MARCH, 20)},  // Fri
            new Object[]{2049, LocalDate.of(2049, Month.MARCH, 20)},  // Sat
            new Object[]{2050, LocalDate.of(2050, Month.MARCH, 20)},  // Sun (rolls at service level)
            new Object[]{2051, LocalDate.of(2051, Month.MARCH, 21)},  // Tue
            new Object[]{2052, LocalDate.of(2052, Month.MARCH, 20)},  // Wed
            new Object[]{2053, LocalDate.of(2053, Month.MARCH, 20)},  // Thu
            new Object[]{2054, LocalDate.of(2054, Month.MARCH, 20)},  // Fri
            new Object[]{2055, LocalDate.of(2055, Month.MARCH, 21)}   // Sun (rolls at service level)
        ).iterator();
    }

    /**
     * Verifies that the NAOJ formula produces the expected raw date for every
     * year in 2026–2055.  Raw means the formula output before any Sunday roll.
     */
    @Test(dataProvider = "vernalEquinox2026to2055")
    public void testVernalEquinoxRawDate(int year, LocalDate expected) {
        LocalDate actual = observance.apply(year);
        assertNotNull(actual, "VernalEquinoxDay should not return null for " + year);
        assertEquals(actual, expected,
                     "Vernal Equinox Day raw formula date for " + year);
    }

    // ── Boundary spot-checks: Mar 20 vs Mar 21 years ─────────────────────────

    /**
     * Spot-checks that verify the formula transition from Mar 20 to Mar 21 at
     * the four-year cycle boundary (2030→2031, 2034→2035, 2046→2047).
     */
    @DataProvider(name = "vernalEquinoxBoundaryPairs")
    Iterator<Object[]> vernalEquinoxBoundaryPairs() {
        return List.of(
            // Year before Mar-21 / Year of Mar-21
            new Object[]{2030, 20, 2031, 21},
            new Object[]{2034, 20, 2035, 21},
            new Object[]{2046, 20, 2047, 21},
            new Object[]{2050, 20, 2051, 21}
        ).iterator();
    }

    @Test(dataProvider = "vernalEquinoxBoundaryPairs")
    public void testVernalEquinoxBoundaryTransition(int yearBefore, int dayBefore,
                                                     int yearAfter, int dayAfter) {
        assertEquals(observance.apply(yearBefore).getDayOfMonth(), dayBefore,
                     "Vernal Equinox should be Mar " + dayBefore + " in " + yearBefore);
        assertEquals(observance.apply(yearAfter).getDayOfMonth(), dayAfter,
                     "Vernal Equinox should be Mar " + dayAfter + " in " + yearAfter);
    }
}
