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
import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.HolidayDate;
import org.holiday.calendar.observance.uk.ChristmasEveEarlyClose;
import org.holiday.calendar.observance.uk.NewYearsEveEarlyClose;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

/**
 * Tests for the {@code UK} calendar's early-close (LSE half-day closure)
 * holidays: Christmas Eve and New Year's Eve.
 */
public class HolidayCalendarServiceUKEarlyCloseTest {

    private static final int EARLY_CLOSE_COUNT = 2;
    // 8 full-day holidays apply in 2025: the 4 Jubilee SPECIAL_ANNIVERSARY
    // holidays only occur in their specific anniversary years (1977/2002/2012/2022).
    private static final int FULL_DAY_HOLIDAY_COUNT = 8;
    private static final LocalTime EXPECTED_CLOSE_TIME = LocalTime.of(12, 30);
    private static final ZoneId EXPECTED_ZONE = ZoneId.of("Europe/London");

    private final HolidayCalendarServiceUK service = new HolidayCalendarServiceUK();

    // -------------------------------------------------------------------------
    // Count
    // -------------------------------------------------------------------------

    @Test
    public void testEarlyCloseHolidayCount() {
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

    // -------------------------------------------------------------------------
    // Names
    // -------------------------------------------------------------------------

    @Test
    public void testEarlyCloseHolidayNames() {
        Set<String> expectedNames = Set.of("Christmas Eve", "New Year's Eve");
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(2025);
        Set<String> actualNames = earlyCloses.stream()
                .map(hd -> hd.getHoliday().getName())
                .collect(Collectors.toSet());
        assertEquals(actualNames, expectedNames);
    }

    // -------------------------------------------------------------------------
    // Close time / zone
    // -------------------------------------------------------------------------

    @Test
    public void testAllEarlyCloses_CloseTimeIs12_30() {
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(2025);
        assertEquals(earlyCloses.size(), EARLY_CLOSE_COUNT);
        for (HolidayDate hd : earlyCloses) {
            assertTrue(hd.getHoliday() instanceof EarlyCloseHoliday);
            EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) hd.getHoliday();
            assertEquals(earlyClose.getCloseTime(), EXPECTED_CLOSE_TIME,
                    earlyClose.getName() + " must close at 12:30");
        }
    }

    @Test
    public void testAllEarlyCloses_ZoneId_IsLondon() {
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(2025);
        assertEquals(earlyCloses.size(), EARLY_CLOSE_COUNT);
        for (HolidayDate hd : earlyCloses) {
            EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) hd.getHoliday();
            assertEquals(earlyClose.getZoneId(), EXPECTED_ZONE,
                    earlyClose.getName() + " must be expressed in Europe/London");
        }
    }

    // -------------------------------------------------------------------------
    // Rollability
    // -------------------------------------------------------------------------

    @Test
    public void testEarlyCloses_NotRollable() {
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(2025);
        assertEquals(earlyCloses.size(), EARLY_CLOSE_COUNT);
        for (HolidayDate hd : earlyCloses) {
            assertFalse(hd.getHoliday().isRollable(), hd.getHoliday().getName() + " must not be rollable");
        }
    }

    // -------------------------------------------------------------------------
    // Cross-check calculateEarlyCloses() dates against raw Observance output,
    // across weekday and weekend-shift years
    // -------------------------------------------------------------------------

    @DataProvider(name = "earlyCloseDates")
    public Iterator<Object[]> earlyCloseDates() {
        List<Object[]> data = Arrays.asList(
                new Object[]{"Christmas Eve", 2022},
                new Object[]{"Christmas Eve", 2023},
                new Object[]{"Christmas Eve", 2024},
                new Object[]{"Christmas Eve", 2025},
                new Object[]{"Christmas Eve", 2026},
                new Object[]{"Christmas Eve", 2028},
                new Object[]{"New Year's Eve", 2022},
                new Object[]{"New Year's Eve", 2023},
                new Object[]{"New Year's Eve", 2024},
                new Object[]{"New Year's Eve", 2025},
                new Object[]{"New Year's Eve", 2026},
                new Object[]{"New Year's Eve", 2028}
        );
        return data.iterator();
    }

    @Test(dataProvider = "earlyCloseDates")
    public void testEarlyCloseDateMatchesRawObservance(String holidayName, int year) {
        LocalDate expected = rawObservanceDate(holidayName, year);

        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
        HolidayDate matched = earlyCloses.stream()
                .filter(hd -> holidayName.equals(hd.getHoliday().getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError(holidayName + " not found in calculateEarlyCloses(" + year + ")"));
        assertEquals(matched.getDate(), expected,
                holidayName + " via calculateEarlyCloses(" + year + ") must match production Observance");
    }

    private LocalDate rawObservanceDate(String holidayName, int year) {
        return switch (holidayName) {
            case "Christmas Eve" -> new ChristmasEveEarlyClose().apply(year);
            case "New Year's Eve" -> new NewYearsEveEarlyClose().apply(year);
            default -> throw new IllegalArgumentException("Unknown holiday: " + holidayName);
        };
    }

}
