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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class HolidayCalendarServiceDETest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "DE";

    public HolidayCalendarServiceDETest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        final Object[] germanUnityDay = {"German Unity Day"};
        final Object[] labourDay = {"Labour Day"};
        final Object[] christmasEve = {"Christmas Eve"};
        final Object[] newYearsEve = {"New Year's Eve"};
        return Arrays.asList(germanUnityDay, labourDay, christmasEve, newYearsEve).listIterator();
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
        // Christmas Eve 2024: Dec 24 is Tuesday -> present, unrolled (primary-sourced)
        final Object[] christmasEve24 = {2024, "Christmas Eve", LocalDate.of(2024, Month.DECEMBER, 24)};
        // New Year's Eve 2025: Dec 31 is Wednesday -> present, unrolled (primary-sourced)
        final Object[] newYearsEve25 = {2025, "New Year's Eve", LocalDate.of(2025, Month.DECEMBER, 31)};
        // Christmas Eve 2021: Dec 24 is Friday -> present, unrolled (collision year, see
        // testChristmasEveAndChristmasDayCoexistOn24Dec2021 below)
        final Object[] christmasEve21 = {2021, "Christmas Eve", LocalDate.of(2021, Month.DECEMBER, 24)};
        return Arrays.asList(christmas21, christmas22, christmas23,
                             boxingDay21, boxingDay22, boxingDay23,
                             christmasEve24, newYearsEve25, christmasEve21).listIterator();
    }

    @Test
    public void testChristmasEveAndNewYearsEveOmittedOnWeekend() {
        // 2023: Dec 24 and Dec 31 both fall on a Sunday, so Xetra/FWB simply doesn't
        // list an exception day at all -- no shift, no entry.
        final HolidayCalendar calendar = factory.create(CODE);
        final Set<String> names = calendar.calculate(2023).stream()
                .map(hd -> hd.getHoliday().getName())
                .collect(Collectors.toSet());
        assertFalse(names.contains("Christmas Eve"), "Christmas Eve must be omitted in 2023 (Sunday)");
        assertFalse(names.contains("New Year's Eve"), "New Year's Eve must be omitted in 2023 (Sunday)");
    }

    @Test
    public void testChristmasEveAndChristmasDayCoexistOn24Dec2021() {
        // Dec 25, 2021 is a Saturday and rolls to Friday Dec 24 under DE's
        // previousFridayOrFollowingMonday roll rule, landing on the same calendar date
        // as the new non-rolling Christmas Eve holiday. Both facts are independently
        // true and must both appear -- no accidental de-duplication.
        final HolidayCalendar calendar = factory.create(CODE);
        final List<HolidayDate> dec24 = calendar.calculate(2021).stream()
                .filter(hd -> LocalDate.of(2021, Month.DECEMBER, 24).equals(hd.getDate()))
                .toList();

        assertEquals(dec24.size(), 2, "Expected both Christmas Day (rolled) and Christmas Eve on 2021-12-24");
        final Set<String> names = dec24.stream().map(hd -> hd.getHoliday().getName()).collect(Collectors.toSet());
        assertTrue(names.contains("Christmas Day"));
        assertTrue(names.contains("Christmas Eve"));
    }

}
