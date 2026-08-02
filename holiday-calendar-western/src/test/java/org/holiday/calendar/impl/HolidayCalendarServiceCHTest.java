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

public class HolidayCalendarServiceCHTest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "CH";

    public HolidayCalendarServiceCHTest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        final Object[] swissNationalDay = new Object[]{"Swiss National Day"};
        final Object[] boxingDay = new Object[]{"Boxing Day"};
        return Arrays.asList(swissNationalDay, boxingDay).listIterator();
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
        return Arrays.asList(swissNationalDay21, swissNationalDay22, swissNationalDay23,
                             christmas21, christmas22, christmas23).listIterator();
    }

    @Test
    public void testEarlyCloseHolidaysAbsentFromCalculate() {
        // Trivially true: CH never had any EarlyCloseHoliday entries. Retained as an
        // explicit regression guard alongside the sibling XSWX/AU/FR/CA national tests.
        HolidayCalendarService service = factory.getService(CODE);
        for (int year : List.of(2021, 2023, 2024, 2025)) {
            List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
            Set<String> actualNames = holidays.stream()
                    .map(hd -> hd.getHoliday().getName())
                    .collect(Collectors.toSet());
            assertFalse(actualNames.contains("Christmas Eve"),
                    "Christmas Eve must not appear in calculate(" + year + ") — moved to XSWX");
            assertFalse(actualNames.contains("New Year's Eve"),
                    "New Year's Eve must not appear in calculate(" + year + ") — moved to XSWX");
        }
    }

    @Test
    public void testChHasNoEarlyCloses() {
        HolidayCalendarService service = factory.getService(CODE);
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertFalse(calendar.hasEarlyCloses(), "CH: national calendar must have zero early closes");
    }

}
