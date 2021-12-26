/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2021 David Joyce
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

package com.github.davejoyce.calendar;

import org.testng.annotations.Test;

import java.time.Month;

import static org.testng.Assert.*;

public class HolidayTest {

    @Test
    public void testGetName() {
        Holiday holiday = new FixedHoliday("New Year's Day", "", Month.JANUARY, 1);
        assertEquals(holiday.getName(), "New Year's Day");
    }

    @Test
    public void testGetDescription() {
        Holiday holiday = new FixedHoliday("New Year's Day", null, Month.JANUARY, 1);
        assertNotNull(holiday.getDescription());
        assertEquals(holiday.getDescription(), "");
    }

    @Test
    public void testIsRollable() {
        Holiday holiday = new FixedHoliday("New Year's Day", null, Month.JANUARY, 1);
        assertTrue(holiday.isRollable());
    }

}