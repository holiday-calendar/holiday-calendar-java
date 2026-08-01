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

import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.HolidayCalendarFactory;
import org.holiday.calendar.HolidayDate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;

public class HolidayCalendarServiceCHTest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "CH";

    public HolidayCalendarServiceCHTest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        final Object[] swissNationalDay = new Object[]{"Swiss National Day"};
        final Object[] christmasEve = new Object[]{"Christmas Eve"};
        final Object[] boxingDay = new Object[]{"Boxing Day"};
        final Object[] newYearsEve = new Object[]{"New Year's Eve"};
        return Arrays.asList(swissNationalDay, christmasEve, boxingDay, newYearsEve).listIterator();
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayOccurrences() {
        // Swiss National Day: Aug 1
        // 2021: Aug 1 is Sunday -> rolls to Monday Aug 2
        final Object[] swissNationalDay21 = {2021, "Swiss National Day", LocalDate.of(2021, Month.AUGUST, 2)};
        // 2022: Aug 1 is Monday -> no roll
        final Object[] swissNationalDay22 = {2022, "Swiss National Day", LocalDate.of(2022, Month.AUGUST, 1)};
        // 2023: Aug 1 is Tuesday -> no roll
        final Object[] swissNationalDay23 = {2023, "Swiss National Day", LocalDate.of(2023, Month.AUGUST, 1)};
        // Christmas Day 2021: Dec 25 is Saturday -> rolls to Friday Dec 24
        final Object[] christmas21 = {2021, "Christmas Day", LocalDate.of(2021, Month.DECEMBER, 24)};
        // Christmas Day 2022: Dec 25 is Sunday -> rolls to Monday Dec 26
        final Object[] christmas22 = {2022, "Christmas Day", LocalDate.of(2022, Month.DECEMBER, 26)};
        // Christmas Day 2023: Dec 25 is Monday -> no roll
        final Object[] christmas23 = {2023, "Christmas Day", LocalDate.of(2023, Month.DECEMBER, 25)};
        // Christmas Eve: Dec 24
        // 2021: Dec 24 is Friday -> no roll
        final Object[] christmasEve21 = {2021, "Christmas Eve", LocalDate.of(2021, Month.DECEMBER, 24)};
        // 2022: Dec 24 is Saturday -> rolls to Friday Dec 23
        final Object[] christmasEve22 = {2022, "Christmas Eve", LocalDate.of(2022, Month.DECEMBER, 23)};
        // 2023: Dec 24 is Sunday -> rolls to Monday Dec 25
        final Object[] christmasEve23 = {2023, "Christmas Eve", LocalDate.of(2023, Month.DECEMBER, 25)};
        // New Year's Eve: Dec 31
        // 2021: Dec 31 is Friday -> no roll
        final Object[] newYearsEve21 = {2021, "New Year's Eve", LocalDate.of(2021, Month.DECEMBER, 31)};
        // 2022: Dec 31 is Saturday -> rolls to Friday Dec 30
        final Object[] newYearsEve22 = {2022, "New Year's Eve", LocalDate.of(2022, Month.DECEMBER, 30)};
        // 2023: Dec 31 is Sunday -> rolls to Monday Jan 1, 2024
        final Object[] newYearsEve23 = {2023, "New Year's Eve", LocalDate.of(2024, Month.JANUARY, 1)};
        return Arrays.asList(swissNationalDay21, swissNationalDay22, swissNationalDay23,
                             christmas21, christmas22, christmas23,
                             christmasEve21, christmasEve22, christmasEve23,
                             newYearsEve21, newYearsEve22, newYearsEve23).listIterator();
    }

    // -------------------------------------------------------------------------
    // Same-resolved-date collisions between independently-rolled fixed holidays
    // -------------------------------------------------------------------------

    @Test
    public void testCollision_ChristmasDayRollAndChristmasEveShareDec24_2021() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        HolidayCalendar calendar = factory.create(CODE);
        assertNotNull(calendar);

        List<HolidayDate> holidays2021 = calendar.calculate(2021);

        Optional<HolidayDate> christmasDay = holidays2021.stream()
                .filter(hd -> "Christmas Day".equals(hd.getHoliday().getName()))
                .findFirst();
        Optional<HolidayDate> christmasEve = holidays2021.stream()
                .filter(hd -> "Christmas Eve".equals(hd.getHoliday().getName()))
                .findFirst();

        assertTrue(christmasDay.isPresent());
        assertTrue(christmasEve.isPresent());
        assertEquals(christmasDay.get().getDate(), LocalDate.of(2021, Month.DECEMBER, 24));
        assertEquals(christmasEve.get().getDate(), LocalDate.of(2021, Month.DECEMBER, 24));
    }

    @Test
    public void testCollision_NewYearsDayRollAndNewYearsEveShareDec31_2021() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        HolidayCalendar calendar = factory.create(CODE);
        assertNotNull(calendar);

        // 2022: Jan 1 is Saturday -> New Year's Day rolls back to Friday Dec 31, 2021
        List<HolidayDate> holidays2022 = calendar.calculate(2022);
        Optional<HolidayDate> newYearsDay = holidays2022.stream()
                .filter(hd -> "New Year's Day".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(newYearsDay.isPresent());
        assertEquals(newYearsDay.get().getDate(), LocalDate.of(2021, Month.DECEMBER, 31));

        // 2021: Dec 31 is Friday -> New Year's Eve stays on Dec 31, 2021
        List<HolidayDate> holidays2021 = calendar.calculate(2021);
        Optional<HolidayDate> newYearsEve = holidays2021.stream()
                .filter(hd -> "New Year's Eve".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(newYearsEve.isPresent());
        assertEquals(newYearsEve.get().getDate(), LocalDate.of(2021, Month.DECEMBER, 31));
    }

}
