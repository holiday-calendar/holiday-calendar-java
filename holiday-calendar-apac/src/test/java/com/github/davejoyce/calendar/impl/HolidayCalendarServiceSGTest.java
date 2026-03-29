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

package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.HolidayCalendarFactory;
import com.github.davejoyce.calendar.HolidayDate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;

public class HolidayCalendarServiceSGTest {

    private final HolidayCalendarServiceSG service = new HolidayCalendarServiceSG();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("SG"));
        assertFalse(service.isProvided("US"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "SG");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Singapore (SGX) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("SG");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "SG");
    }

    @Test
    public void testCalculate2024() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);
        assertNotNull(holidays);
        // 11 holidays defined; all lookup-table ones cover 2024, Time4J covers CNY
        assertTrue(holidays.size() >= 7, "Expected at least 7 holidays for 2024, got: " + holidays.size());
    }

    @Test
    public void testChronologicalOrder() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);
        for (int i = 1; i < holidays.size(); i++) {
            assertTrue(
                holidays.get(i).getDate().compareTo(holidays.get(i - 1).getDate()) >= 0,
                "Holidays should be in chronological order"
            );
        }
    }

    @Test
    public void testChineseNewYear2024() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);

        Optional<HolidayDate> cny1 = holidays.stream()
                .filter(hd -> "Chinese New Year (1st Day)".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(cny1.isPresent(), "Chinese New Year 1st Day should be present for 2024");
        assertEquals(cny1.get().getDate(), LocalDate.of(2024, Month.FEBRUARY, 10),
                "Chinese New Year 1st Day 2024 should be Feb 10");

        Optional<HolidayDate> cny2 = holidays.stream()
                .filter(hd -> "Chinese New Year (2nd Day)".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(cny2.isPresent(), "Chinese New Year 2nd Day should be present for 2024");
        assertEquals(cny2.get().getDate(), LocalDate.of(2024, Month.FEBRUARY, 11),
                "Chinese New Year 2nd Day 2024 should be Feb 11");
    }

    @Test
    public void testGoodFriday2024() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);

        Optional<HolidayDate> goodFriday = holidays.stream()
                .filter(hd -> "Good Friday".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(goodFriday.isPresent(), "Good Friday should be present for 2024");
        assertEquals(goodFriday.get().getDate(), LocalDate.of(2024, Month.MARCH, 29),
                "Good Friday 2024 should be March 29");
    }

    @Test
    public void testDeepavali2024() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);

        Optional<HolidayDate> deepavali = holidays.stream()
                .filter(hd -> "Deepavali".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(deepavali.isPresent(), "Deepavali should be present for 2024");
        assertEquals(deepavali.get().getDate(), LocalDate.of(2024, Month.OCTOBER, 31),
                "Deepavali 2024 should be October 31");
    }

    @Test
    public void testNationalDayRoll() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        // National Day 2020: Aug 9 is Sunday -> rolls to Monday Aug 10
        List<HolidayDate> holidays2020 = calendar.calculate(2020);
        Optional<HolidayDate> nationalDay2020 = holidays2020.stream()
                .filter(hd -> "National Day".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(nationalDay2020.isPresent());
        assertEquals(nationalDay2020.get().getDate(), LocalDate.of(2020, Month.AUGUST, 10),
                "National Day 2020 (Aug 9, Sunday) should roll to Monday Aug 10");

        // National Day 2023: Aug 9 is Wednesday -> no roll
        List<HolidayDate> holidays2023 = calendar.calculate(2023);
        Optional<HolidayDate> nationalDay2023 = holidays2023.stream()
                .filter(hd -> "National Day".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(nationalDay2023.isPresent());
        assertEquals(nationalDay2023.get().getDate(), LocalDate.of(2023, Month.AUGUST, 9),
                "National Day 2023 (Aug 9, Wednesday) should not roll");
    }

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"New Year's Day"},
            new Object[]{"Chinese New Year (1st Day)"},
            new Object[]{"Good Friday"},
            new Object[]{"Labour Day"},
            new Object[]{"National Day"},
            new Object[]{"Christmas Day"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        boolean found = calendar.getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName()));
        assertTrue(found, "Calendar should contain holiday: " + holidayName);
    }

}
