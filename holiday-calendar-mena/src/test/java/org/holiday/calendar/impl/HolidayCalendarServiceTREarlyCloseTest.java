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

import org.holiday.calendar.EarlyCloseHoliday;
import org.holiday.calendar.HolidayDate;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Tests for the {@code TR} calendar's early-close (nationwide Republic Day Eve
 * half-day per Law No. 2429) holiday, provided by {@link TurkeyHolidays#earlyCloseHolidays()}.
 */
public class HolidayCalendarServiceTREarlyCloseTest {

    private static final int EARLY_CLOSE_COUNT = 1;
    private static final int FULL_DAY_HOLIDAY_COUNT = 14;
    private static final LocalTime EXPECTED_CLOSE_TIME = LocalTime.of(13, 0);
    private static final ZoneId EXPECTED_ZONE = ZoneId.of("Europe/Istanbul");

    private final HolidayCalendarServiceTR service = new HolidayCalendarServiceTR();

    @Test
    public void testEarlyCloseHolidayCount2025() {
        // Oct 28, 2025 is a Tuesday
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(2025);
        assertNotNull(earlyCloses);
        assertEquals(earlyCloses.size(), EARLY_CLOSE_COUNT);
    }

    @Test
    public void testFullDayHolidayCount_Unchanged() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), FULL_DAY_HOLIDAY_COUNT);
    }

    @Test
    public void testRepublicDayEve_NameCloseTimeAndZone2025() {
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(2025);
        HolidayDate republicDayEve = earlyCloses.stream()
                .filter(hd -> "Republic Day Eve".equals(hd.getHoliday().getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Republic Day Eve not found in calculateEarlyCloses(2025)"));
        assertEquals(republicDayEve.getDate(), LocalDate.of(2025, Month.OCTOBER, 28));
        assertTrue(republicDayEve.getHoliday() instanceof EarlyCloseHoliday);
        EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) republicDayEve.getHoliday();
        assertEquals(earlyClose.getCloseTime(), EXPECTED_CLOSE_TIME, "Republic Day Eve must close at 13:00");
        assertEquals(earlyClose.getZoneId(), EXPECTED_ZONE, "Republic Day Eve must be expressed in Europe/Istanbul");
    }

    @Test
    public void testRepublicDayEve_SkippedWhenOct28FallsOnWeekend2023() {
        // Oct 28, 2023 is a Saturday (Oct 29, 2023 is a Sunday)
        assertEquals(LocalDate.of(2023, Month.OCTOBER, 28).getDayOfWeek(), DayOfWeek.SATURDAY);
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(2023);
        assertTrue(earlyCloses.isEmpty(),
                "Republic Day Eve does not shift to the preceding Friday when Oct 28 falls on a weekend");
    }

}
