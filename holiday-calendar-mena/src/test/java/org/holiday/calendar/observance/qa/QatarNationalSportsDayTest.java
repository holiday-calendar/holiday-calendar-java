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

public class QatarNationalSportsDayTest {

    private final QatarNationalSportsDay sportsDay = new QatarNationalSportsDay();

    @DataProvider
    Iterator<Object[]> knownDates() {
        return List.of(
            // Amiri Diwan confirmed dates
            new Object[]{2023, LocalDate.of(2023, Month.FEBRUARY, 14)},
            new Object[]{2024, LocalDate.of(2024, Month.FEBRUARY, 13)},
            new Object[]{2025, LocalDate.of(2025, Month.FEBRUARY, 11)},
            new Object[]{2026, LocalDate.of(2026, Month.FEBRUARY, 10)}
        ).iterator();
    }

    @Test(dataProvider = "knownDates")
    public void testSecondTuesdayOfFebruary(int year, LocalDate expected) {
        assertEquals(sportsDay.apply(year), expected,
                "Qatar National Sports Day " + year + " must be " + expected);
    }

    @Test(dataProvider = "knownDates")
    public void testAlwaysFallsOnTuesday(int year, LocalDate expected) {
        assertEquals(sportsDay.apply(year).getDayOfWeek(), DayOfWeek.TUESDAY,
                "Qatar National Sports Day must always fall on Tuesday");
    }

    @Test
    public void testValidFrom2012() {
        assertNotNull(sportsDay.apply(2012),
                "First Sports Day (2012) must be computable");
        assertEquals(sportsDay.apply(2012).getDayOfWeek(), DayOfWeek.TUESDAY);
    }

    @Test
    public void testInvalidBefore2012ReturnsNull() {
        assertNull(sportsDay.apply(2011),
                "Sports Day before 2012 must return null — Decree enacted 2011, first observed 2012");
    }

    @Test
    public void testTestReturnsFalseBeforeInception() {
        assertFalse(sportsDay.test(2011));
    }

    @Test
    public void testTestReturnsTrueFrom2012() {
        assertTrue(sportsDay.test(2025));
    }
}
