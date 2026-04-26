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
 * Extended parametrized tests for {@link AutumnalEquinoxDay} covering every
 * year in the 2026–2055 target range.
 *
 * <p>Expected raw dates are derived from the NAOJ formula:
 * <pre>
 *   day = floor(23.2488 + 0.242194 * (year - 1980) - floor((year - 1980) / 4))
 * </pre>
 * The formula yields Sep 22 in 2028, 2032, 2036, 2040, 2044, 2045, 2048, 2049,
 * 2052, and 2053; all other years in this range give Sep 23.
 *
 * <p>Years where the raw date falls on a Sunday (rolls to Monday at service level):
 * 2029 (Sep 23 Sun → Sep 24 Mon), 2035 (Sep 23 Sun → Sep 24 Mon),
 * 2046 (Sep 23 Sun → Sep 24 Mon), 2052 (Sep 22 Sun → Sep 23 Mon).
 * These rolls are tested at the {@link HolidayCalendarServiceJPExtendedTest}
 * integration level; this class tests only the raw formula output.
 */
public class AutumnalEquinoxDayExtendedTest {

    private final AutumnalEquinoxDay observance = new AutumnalEquinoxDay();

    // ── 2026–2055 full parametrized sweep ─────────────────────────────────────

    /**
     * All 30 autumnal equinox raw dates from 2026 to 2055.
     *
     * <p>Sep 22 years: 2028, 2032, 2036, 2040, 2044, 2045, 2048, 2049, 2052, 2053.
     * Sep 23 years: 2026, 2027, 2029–2031, 2033–2035, 2037–2039, 2041–2043,
     * 2046–2047, 2050–2051, 2054–2055.
     */
    @DataProvider(name = "autumnalEquinox2026to2055")
    Iterator<Object[]> autumnalEquinox2026to2055() {
        return List.of(
            new Object[]{2026, LocalDate.of(2026, Month.SEPTEMBER, 23)},  // Wed
            new Object[]{2027, LocalDate.of(2027, Month.SEPTEMBER, 23)},  // Thu
            new Object[]{2028, LocalDate.of(2028, Month.SEPTEMBER, 22)},  // Fri
            new Object[]{2029, LocalDate.of(2029, Month.SEPTEMBER, 23)},  // Sun (rolls at service level)
            new Object[]{2030, LocalDate.of(2030, Month.SEPTEMBER, 23)},  // Mon
            new Object[]{2031, LocalDate.of(2031, Month.SEPTEMBER, 23)},  // Tue
            new Object[]{2032, LocalDate.of(2032, Month.SEPTEMBER, 22)},  // Wed
            new Object[]{2033, LocalDate.of(2033, Month.SEPTEMBER, 23)},  // Fri
            new Object[]{2034, LocalDate.of(2034, Month.SEPTEMBER, 23)},  // Sat
            new Object[]{2035, LocalDate.of(2035, Month.SEPTEMBER, 23)},  // Sun (rolls at service level)
            new Object[]{2036, LocalDate.of(2036, Month.SEPTEMBER, 22)},  // Mon
            new Object[]{2037, LocalDate.of(2037, Month.SEPTEMBER, 23)},  // Wed
            new Object[]{2038, LocalDate.of(2038, Month.SEPTEMBER, 23)},  // Thu
            new Object[]{2039, LocalDate.of(2039, Month.SEPTEMBER, 23)},  // Fri
            new Object[]{2040, LocalDate.of(2040, Month.SEPTEMBER, 22)},  // Sat
            new Object[]{2041, LocalDate.of(2041, Month.SEPTEMBER, 23)},  // Mon
            new Object[]{2042, LocalDate.of(2042, Month.SEPTEMBER, 23)},  // Tue
            new Object[]{2043, LocalDate.of(2043, Month.SEPTEMBER, 23)},  // Wed
            new Object[]{2044, LocalDate.of(2044, Month.SEPTEMBER, 22)},  // Thu
            new Object[]{2045, LocalDate.of(2045, Month.SEPTEMBER, 22)},  // Fri
            new Object[]{2046, LocalDate.of(2046, Month.SEPTEMBER, 23)},  // Sun (rolls at service level)
            new Object[]{2047, LocalDate.of(2047, Month.SEPTEMBER, 23)},  // Mon
            new Object[]{2048, LocalDate.of(2048, Month.SEPTEMBER, 22)},  // Tue
            new Object[]{2049, LocalDate.of(2049, Month.SEPTEMBER, 22)},  // Wed
            new Object[]{2050, LocalDate.of(2050, Month.SEPTEMBER, 23)},  // Fri
            new Object[]{2051, LocalDate.of(2051, Month.SEPTEMBER, 23)},  // Sat
            new Object[]{2052, LocalDate.of(2052, Month.SEPTEMBER, 22)},  // Sun (rolls at service level)
            new Object[]{2053, LocalDate.of(2053, Month.SEPTEMBER, 22)},  // Mon
            new Object[]{2054, LocalDate.of(2054, Month.SEPTEMBER, 23)},  // Wed
            new Object[]{2055, LocalDate.of(2055, Month.SEPTEMBER, 23)}   // Thu
        ).iterator();
    }

    /**
     * Verifies that the NAOJ formula produces the expected raw date for every
     * year in 2026–2055.
     */
    @Test(dataProvider = "autumnalEquinox2026to2055")
    public void testAutumnalEquinoxRawDate(int year, LocalDate expected) {
        LocalDate actual = observance.apply(year);
        assertNotNull(actual, "AutumnalEquinoxDay should not return null for " + year);
        assertEquals(actual, expected,
                     "Autumnal Equinox Day raw formula date for " + year);
    }

    // ── Sep 22 vs Sep 23 boundary spot-checks ─────────────────────────────────

    /**
     * Verifies the transition between Sep-23 and Sep-22 years at representative
     * boundaries within the 2026–2055 range.
     */
    @DataProvider(name = "autumnalEquinoxDayValueCases")
    Iterator<Object[]> autumnalEquinoxDayValueCases() {
        return List.of(
            // Sep 23 years
            new Object[]{2026, 23},
            new Object[]{2030, 23},
            new Object[]{2037, 23},
            new Object[]{2047, 23},
            new Object[]{2054, 23},
            // Sep 22 years
            new Object[]{2028, 22},
            new Object[]{2032, 22},
            new Object[]{2044, 22},
            new Object[]{2045, 22},
            new Object[]{2049, 22}
        ).iterator();
    }

    @Test(dataProvider = "autumnalEquinoxDayValueCases")
    public void testAutumnalEquinoxDayOfMonth(int year, int expectedDay) {
        assertEquals(observance.apply(year).getDayOfMonth(), expectedDay,
                     "Autumnal Equinox day-of-month for " + year);
    }

    // ── Sunday-roll years: raw date is Sunday ─────────────────────────────────

    /**
     * Confirms that the raw formula output for Sunday-roll years is indeed a
     * Sunday.  The actual observed (rolled) date is tested at the integration
     * layer in {@link HolidayCalendarServiceJPExtendedTest}.
     */
    @DataProvider(name = "autumnalEquinoxSundayRawYears")
    Iterator<Object[]> autumnalEquinoxSundayRawYears() {
        return List.of(
            new Object[]{2029, LocalDate.of(2029, Month.SEPTEMBER, 23)},
            new Object[]{2035, LocalDate.of(2035, Month.SEPTEMBER, 23)},
            new Object[]{2046, LocalDate.of(2046, Month.SEPTEMBER, 23)},
            new Object[]{2052, LocalDate.of(2052, Month.SEPTEMBER, 22)}
        ).iterator();
    }

    @Test(dataProvider = "autumnalEquinoxSundayRawYears")
    public void testAutumnalEquinoxRawDateIsSunday(int year, LocalDate expectedSunday) {
        LocalDate raw = observance.apply(year);
        assertEquals(raw, expectedSunday,
                     "Autumnal Equinox raw date for " + year + " should be a Sunday");
        assertEquals(raw.getDayOfWeek(), java.time.DayOfWeek.SUNDAY,
                     "Raw autumnal equinox date for " + year + " must be SUNDAY");
    }
}