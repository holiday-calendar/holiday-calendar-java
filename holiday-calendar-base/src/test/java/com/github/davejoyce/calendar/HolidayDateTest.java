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

import java.time.LocalDate;
import java.time.Month;

import static org.testng.Assert.*;

public class HolidayDateTest {

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "holiday is marked non-null .+")
    public void testConstructor_NullHoliday() {
        LocalDate date = LocalDate.now();
        new HolidayDate(null, date);
        fail("Expected NullPointerException");
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "date is marked non-null .+")
    public void testConstructor_NullDate() {
        Holiday christmas = new FixedHoliday("Christmas", "Celebration of birth of Christ", Month.DECEMBER, 25);
        new HolidayDate(christmas, null);
        fail("Expected NullPointerException");
    }

    @Test
    public void testGetHoliday() {
        Holiday christmas = new FixedHoliday("Christmas", "Celebration of birth of Christ", Month.DECEMBER, 25);
        LocalDate christmasDay = LocalDate.of(2021, Month.DECEMBER, 25);
        HolidayDate christmas2021 = new HolidayDate(christmas, christmasDay);
        assertEquals(christmas2021.getHoliday(), christmas);
    }

    @Test
    public void testGetDate() {
        Holiday christmas = new FixedHoliday("Christmas", "Celebration of birth of Christ", Month.DECEMBER, 25);
        LocalDate christmasDay = LocalDate.of(2021, Month.DECEMBER, 25);
        HolidayDate christmas2021 = new HolidayDate(christmas, christmasDay);
        assertEquals(christmas2021.getDate(), christmasDay);
    }

    @Test
    public void testEquals() {
        Holiday christmas = new FixedHoliday("Christmas", "Celebration of birth of Christ", Month.DECEMBER, 25);
        LocalDate christmasDate = LocalDate.of(2021, Month.DECEMBER, 25);
        HolidayDate christmas1 = new HolidayDate(christmas, christmasDate);
        HolidayDate christmas2 = new HolidayDate(christmas, christmasDate);

        Holiday boxingDay = new FixedHoliday("Boxing Day", "Day after Christmas", Month.DECEMBER, 26);
        LocalDate boxingDayDate = LocalDate.of(2021, Month.DECEMBER, 26);
        HolidayDate boxingDay2021 = new HolidayDate(boxingDay, boxingDayDate);

        Object notaHolidayDate = new Object();

        assertEquals(christmas1, christmas1);
        assertEquals(christmas2, christmas1);
        assertNotEquals(boxingDay2021, christmas1);
        assertNotEquals(notaHolidayDate, christmas1);
    }

    @Test
    public void testHashCode() {
        Holiday christmas = new FixedHoliday("Christmas", "Celebration of birth of Christ", Month.DECEMBER, 25);
        LocalDate christmasDate = LocalDate.of(2021, Month.DECEMBER, 25);
        HolidayDate christmas1 = new HolidayDate(christmas, christmasDate);
        HolidayDate christmas2 = new HolidayDate(christmas, christmasDate);

        assertEquals(christmas2.hashCode(), christmas1.hashCode());
    }

    @Test
    public void testToString() {
        Holiday christmas = new FixedHoliday("Christmas", "Celebration of birth of Christ", Month.DECEMBER, 25);
        LocalDate christmasDate = LocalDate.of(2021, Month.DECEMBER, 25);
        HolidayDate christmas2021 = new HolidayDate(christmas, christmasDate);

        String expected = "HolidayDate(holiday=Holiday[name='Christmas', description='Celebration of birth of Christ', month=DECEMBER, dayOfMonth=25], date=2021-12-25)";
        assertEquals(christmas2021.toString(), expected);
    }
}