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

package org.holiday.calendar.observance.hebrew;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public class YomKippurTest {

    @DataProvider
    Iterator<Object[]> knownDates() {
        // 10 Tishri: hebrewYear = gregorianYear + 3761
        // Dates verified via net.time4j.calendar.HebrewCalendar
        return List.of(
            new Object[]{2024, LocalDate.of(2024, 10, 12)},  // 10 Tishri 5785 = Saturday
            new Object[]{2025, LocalDate.of(2025, 10,  2)},  // 10 Tishri 5786 = Thursday
            new Object[]{2026, LocalDate.of(2026,  9, 21)},  // 10 Tishri 5787 = Monday
            new Object[]{2027, LocalDate.of(2027, 10, 11)},  // 10 Tishri 5788 = Monday
            new Object[]{2028, LocalDate.of(2028,  9, 30)}   // 10 Tishri 5789 = Saturday
        ).iterator();
    }

    @Test(dataProvider = "knownDates")
    public void testKnownDates(int year, LocalDate expected) {
        assertEquals(new YomKippur().apply(year), expected,
                "Yom Kippur (10 Tishri) for " + year + " must be " + expected);
    }

    @Test
    public void testTestReturnsTrueForValidYear() {
        assertTrue(new YomKippur().test(2025));
    }

    @Test
    public void testYomKippurAlways9DaysAfterRoshHashanah() {
        for (int year = 2024; year <= 2035; year++) {
            LocalDate rh1 = new RoshHashanah().apply(year);
            LocalDate yk  = new YomKippur().apply(year);
            assertNotNull(rh1);
            assertNotNull(yk);
            assertEquals(yk, rh1.plusDays(9),
                    "Yom Kippur must be exactly 9 days after Rosh Hashanah Day 1 (year " + year + ")");
        }
    }

}
