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
import org.holiday.calendar.HolidayCalendarService;
import org.holiday.calendar.HolidayDate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class HolidayCalendarServiceAUTest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "AU";

    public HolidayCalendarServiceAUTest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        final Object[] australiaDay = {"Australia Day"};
        final Object[] anzacDay = {"ANZAC Day"};
        final Object[] easterSaturday = {"Easter Saturday"};
        final Object[] kingsBirthday = {"King's Birthday"};
        return Arrays.asList(australiaDay, anzacDay, easterSaturday, kingsBirthday).listIterator();
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
        // Boxing Day 2021: Dec 26 is Sunday -> rolls to Monday Dec 27
        final Object[] boxingDay21 = {2021, "Boxing Day", LocalDate.of(2021, Month.DECEMBER, 27)};
        // Boxing Day 2022: Dec 26 is Monday -> no roll
        final Object[] boxingDay22 = {2022, "Boxing Day", LocalDate.of(2022, Month.DECEMBER, 26)};
        // Boxing Day 2023: Dec 26 is Tuesday -> no roll
        final Object[] boxingDay23 = {2023, "Boxing Day", LocalDate.of(2023, Month.DECEMBER, 26)};
        // Australia Day 2020: Jan 26 is Sunday -> rolls to Monday Jan 27
        final Object[] australiaDay20 = {2020, "Australia Day", LocalDate.of(2020, Month.JANUARY, 27)};
        // Australia Day 2023: Jan 26 is Thursday -> no roll
        final Object[] australiaDay23 = {2023, "Australia Day", LocalDate.of(2023, Month.JANUARY, 26)};
        return Arrays.asList(christmas21, christmas22, christmas23,
                             boxingDay21, boxingDay22, boxingDay23,
                             australiaDay20, australiaDay23).listIterator();
    }

    @Test
    public void testEarlyCloseHolidaysAbsentFromCalculate() {
        HolidayCalendarService service = factory.getService(CODE);
        for (int year : List.of(2021, 2023, 2024, 2025)) {
            List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
            Set<String> actualNames = holidays.stream()
                    .map(hd -> hd.getHoliday().getName())
                    .collect(Collectors.toSet());
            assertFalse(actualNames.contains("Christmas Eve"),
                    "Christmas Eve must not appear in calculate(" + year + ") — moved to XASX");
            assertFalse(actualNames.contains("New Year's Eve"),
                    "New Year's Eve must not appear in calculate(" + year + ") — moved to XASX");
        }
    }

    @Test
    public void testAuHasNoEarlyCloses() {
        HolidayCalendarService service = factory.getService(CODE);
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertFalse(calendar.hasEarlyCloses(), "AU: national calendar must have zero early closes");
    }

    @Test
    public void testEasterSaturdayAndKingsBirthdayPresentInCalculate() {
        HolidayCalendarService service = factory.getService(CODE);
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        Set<String> actualNames = holidays.stream()
                .map(hd -> hd.getHoliday().getName())
                .collect(Collectors.toSet());
        assertTrue(actualNames.contains("Easter Saturday"), "AU: Easter Saturday must remain in the national calendar");
        assertTrue(actualNames.contains("King's Birthday"), "AU: King's Birthday must remain in the national calendar");
    }

}