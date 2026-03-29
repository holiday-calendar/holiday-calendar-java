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

import org.testng.annotations.DataProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;

public class HolidayCalendarServiceFRTest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "FR";

    public HolidayCalendarServiceFRTest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        final Object[] bastilleDay = {"Bastille Day"};
        final Object[] armisticeDay = {"Armistice Day"};
        return Arrays.asList(bastilleDay, armisticeDay).listIterator();
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayOccurrences() {
        // Christmas Day 2021: Dec 25 is Saturday -> rolls to Friday Dec 24
        final Object[] christmas21 = {2021, "Christmas Day", LocalDate.of(2021, Month.DECEMBER, 24)};
        // Christmas Day 2022: Dec 25 is Sunday -> rolls to Monday Dec 26
        final Object[] christmas22 = {2022, "Christmas Day", LocalDate.of(2022, Month.DECEMBER, 26)};
        // Christmas Day 2023: Dec 25 is Monday -> no roll
        final Object[] christmas23 = {2023, "Christmas Day", LocalDate.of(2023, Month.DECEMBER, 25)};
        // Bastille Day 2021: Jul 14 is Wednesday -> no roll
        final Object[] bastilleDay21 = {2021, "Bastille Day", LocalDate.of(2021, Month.JULY, 14)};
        // Bastille Day 2024: Jul 14 is Sunday -> rolls to Monday Jul 15
        final Object[] bastilleDay24 = {2024, "Bastille Day", LocalDate.of(2024, Month.JULY, 15)};
        return Arrays.asList(christmas21, christmas22, christmas23,
                             bastilleDay21, bastilleDay24).listIterator();
    }

}
