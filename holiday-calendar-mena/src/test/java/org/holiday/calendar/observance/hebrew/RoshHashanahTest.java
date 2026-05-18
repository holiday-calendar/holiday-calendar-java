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

public class RoshHashanahTest {

    @DataProvider
    Iterator<Object[]> knownDates() {
        // 1 Tishri: hebrewYear = gregorianYear + 3761
        // Dates verified via net.time4j.calendar.HebrewCalendar
        return List.of(
            new Object[]{2024, LocalDate.of(2024, 10,  3)},  // 1 Tishri 5785 = Thursday
            new Object[]{2025, LocalDate.of(2025,  9, 23)},  // 1 Tishri 5786 = Tuesday
            new Object[]{2026, LocalDate.of(2026,  9, 12)},  // 1 Tishri 5787 = Saturday
            new Object[]{2027, LocalDate.of(2027, 10,  2)},  // 1 Tishri 5788 = Saturday
            new Object[]{2028, LocalDate.of(2028,  9, 21)}   // 1 Tishri 5789 = Thursday
        ).iterator();
    }

    @Test(dataProvider = "knownDates")
    public void testKnownDates(int year, LocalDate expected) {
        assertEquals(new RoshHashanah().apply(year), expected,
                "Rosh Hashanah (1 Tishri) for " + year + " must be " + expected);
    }

    @DataProvider
    Iterator<Object[]> knownDatesDay2() {
        return List.of(
            new Object[]{2024, LocalDate.of(2024, 10,  4)},  // 2 Tishri 5785
            new Object[]{2025, LocalDate.of(2025,  9, 24)},  // 2 Tishri 5786
            new Object[]{2026, LocalDate.of(2026,  9, 13)}   // 2 Tishri 5787
        ).iterator();
    }

    @Test(dataProvider = "knownDatesDay2")
    public void testKnownDatesDay2(int year, LocalDate expected) {
        assertEquals(new RoshHashanahDay2().apply(year), expected,
                "Rosh Hashanah Day 2 (2 Tishri) for " + year + " must be " + expected);
    }

    @Test
    public void testDay2IsAlwaysOneDayAfterDay1() {
        for (int year = 2024; year <= 2035; year++) {
            LocalDate day1 = new RoshHashanah().apply(year);
            LocalDate day2 = new RoshHashanahDay2().apply(year);
            assertNotNull(day1);
            assertNotNull(day2);
            assertEquals(day2, day1.plusDays(1),
                    "Rosh Hashanah Day 2 must always be exactly 1 day after Day 1 (year " + year + ")");
        }
    }

    @Test
    public void testTestReturnsTrueForValidYear() {
        assertTrue(new RoshHashanah().test(2025));
    }

}
