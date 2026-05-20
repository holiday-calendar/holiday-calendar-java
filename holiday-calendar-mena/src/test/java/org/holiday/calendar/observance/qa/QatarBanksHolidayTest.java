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

package org.holiday.calendar.observance.qa;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public class QatarBanksHolidayTest {

    private final QatarBanksHoliday banksHoliday = new QatarBanksHoliday();

    @DataProvider
    Iterator<Object[]> knownDates() {
        return List.of(
            // 2024-03-03 confirmed via QCB official announcement (Gulf Times)
            new Object[]{2024, LocalDate.of(2024, Month.MARCH, 3)},
            new Object[]{2025, LocalDate.of(2025, Month.MARCH, 2)},
            new Object[]{2026, LocalDate.of(2026, Month.MARCH, 1)},
            new Object[]{2027, LocalDate.of(2027, Month.MARCH, 7)}
        ).iterator();
    }

    @Test(dataProvider = "knownDates")
    public void testFirstSundayOfMarch(int year, LocalDate expected) {
        assertEquals(banksHoliday.apply(year), expected,
                "Qatar Banks Holiday " + year + " must be " + expected);
    }

    @Test(dataProvider = "knownDates")
    public void testAlwaysFallsOnSunday(int year, LocalDate expected) {
        assertEquals(banksHoliday.apply(year).getDayOfWeek(), DayOfWeek.SUNDAY,
                "Qatar Banks Holiday must always fall on Sunday");
    }

    @Test
    public void testValidFrom2009() {
        assertNotNull(banksHoliday.apply(2009),
                "Qatar Banks Holiday must be computable from 2009 (Cabinet Decision No. (33) of 2009)");
        assertEquals(banksHoliday.apply(2009).getDayOfWeek(), DayOfWeek.SUNDAY);
    }

    @Test
    public void testInvalidBefore2009ReturnsNull() {
        assertNull(banksHoliday.apply(2008),
                "Qatar Banks Holiday before 2009 must return null — Cabinet Decision not yet in force");
    }

    @Test
    public void testTestReturnsFalseBeforeInception() {
        assertFalse(banksHoliday.test(2008));
    }

    @Test
    public void testTestReturnsTrueFrom2009() {
        assertTrue(banksHoliday.test(2024));
    }
}
