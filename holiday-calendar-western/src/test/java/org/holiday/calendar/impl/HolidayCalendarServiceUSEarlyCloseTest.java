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
import org.holiday.calendar.observance.us.ChristmasEveEarlyClose;
import org.holiday.calendar.observance.us.DayAfterThanksgiving;
import org.holiday.calendar.observance.us.JulyThirdEarlyClose;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
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
 * Tests for the {@code US} calendar's early-close (NYSE half-day closure)
 * holidays: Day After Thanksgiving (unconditional), Christmas Eve, and July
 * 3rd (both conditional on the day-of-week of their anchor holiday). Unlike
 * the UK's early closes, which always occur exactly twice per year, the US
 * count varies from 1 to 3 depending on where Dec 25 / Jul 4 fall — so this
 * test asserts explicit per-holiday presence/absence per year, not just a
 * fixed count.
 */
public class HolidayCalendarServiceUSEarlyCloseTest {

    private static final LocalTime EXPECTED_CLOSE_TIME = LocalTime.of(13, 0);
    private static final ZoneId EXPECTED_ZONE = ZoneId.of("America/New_York");

    // 12 full-day holidays in calculate() once Day After Thanksgiving moves to EARLY_CLOSE
    // (13 holidays registered total, minus 1 EarlyCloseHoliday excluded by calculate()).
    private static final int FULL_DAY_HOLIDAY_COUNT = 12;

    private final HolidayCalendarServiceUS service = new HolidayCalendarServiceUS();

    // -------------------------------------------------------------------------
    // Count / presence per year (verified 2019-2027 NYSE cycle)
    // -------------------------------------------------------------------------

    @DataProvider(name = "usEarlyCloseFixture")
    public Iterator<Object[]> usEarlyCloseFixture() {
        List<Object[]> data = Arrays.asList(
                new Object[]{2019, true, true, 3},
                new Object[]{2020, false, true, 2},
                new Object[]{2021, false, false, 1},
                new Object[]{2022, false, false, 1},
                new Object[]{2023, true, false, 2},
                new Object[]{2024, true, true, 3},
                new Object[]{2025, true, true, 3},
                new Object[]{2026, false, true, 2},
                new Object[]{2027, false, false, 1}
        );
        return data.iterator();
    }

    @Test(dataProvider = "usEarlyCloseFixture")
    public void testEarlyCloseCountForYear(int year, boolean july3Present, boolean dec24Present, int expectedCount) {
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(year);
        assertNotNull(earlyCloses);
        assertEquals(earlyCloses.size(), expectedCount, "US " + year + ": unexpected early-close count");
    }

    @Test(dataProvider = "usEarlyCloseFixture")
    public void testJulyThirdPresenceForYear(int year, boolean july3Present, boolean dec24Present, int expectedCount) {
        Set<String> names = earlyCloseNames(year);
        assertEquals(names.contains("July 3rd"), july3Present,
                "US " + year + ": July 3rd presence must match July 4 day-of-week rule");
    }

    @Test(dataProvider = "usEarlyCloseFixture")
    public void testChristmasEveEarlyClosePresenceForYear(int year, boolean july3Present, boolean dec24Present, int expectedCount) {
        Set<String> names = earlyCloseNames(year);
        assertEquals(names.contains("Christmas Eve"), dec24Present,
                "US " + year + ": Christmas Eve presence must match December 25 day-of-week rule");
    }

    @Test(dataProvider = "usEarlyCloseFixture")
    public void testDayAfterThanksgivingAlwaysPresent(int year, boolean july3Present, boolean dec24Present, int expectedCount) {
        assertTrue(earlyCloseNames(year).contains("Day After Thanksgiving"),
                "US " + year + ": Day After Thanksgiving must always be present");
    }

    @Test(dataProvider = "usEarlyCloseFixture")
    public void testDayAfterThanksgivingCloseIsAlwaysFriday(int year, boolean july3Present, boolean dec24Present, int expectedCount) {
        HolidayDate matched = service.getHolidayCalendar().calculateEarlyCloses(year).stream()
                .filter(hd -> "Day After Thanksgiving".equals(hd.getHoliday().getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Day After Thanksgiving not found in " + year));
        assertEquals(matched.getDate().getDayOfWeek(), DayOfWeek.FRIDAY);
    }

    private Set<String> earlyCloseNames(int year) {
        return service.getHolidayCalendar().calculateEarlyCloses(year).stream()
                .map(hd -> hd.getHoliday().getName())
                .collect(Collectors.toSet());
    }

    // -------------------------------------------------------------------------
    // Monday-exclusion regression cases (issue text incorrectly said "Monday-Friday")
    // -------------------------------------------------------------------------

    @Test
    public void testJulyThirdSuppressedWhenJuly4IsMonday() {
        // 2022: July 4 = Monday.
        Set<String> names = earlyCloseNames(2022);
        assertFalse(names.contains("July 3rd"));
    }

    @Test
    public void testChristmasEveSuppressedWhenDecember25IsMonday() {
        // 2023: December 25 = Monday.
        Set<String> names = earlyCloseNames(2023);
        assertFalse(names.contains("Christmas Eve"));
    }

    // -------------------------------------------------------------------------
    // Close time / zone / rollability
    // -------------------------------------------------------------------------

    @Test
    public void testAllEarlyCloses_CloseTimeIs13_00() {
        for (int year : List.of(2021, 2024)) { // one 1-count year, one 3-count year
            for (HolidayDate hd : service.getHolidayCalendar().calculateEarlyCloses(year)) {
                assertTrue(hd.getHoliday() instanceof EarlyCloseHoliday);
                EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) hd.getHoliday();
                assertEquals(earlyClose.getCloseTime(), EXPECTED_CLOSE_TIME,
                        earlyClose.getName() + " must close at 13:00");
            }
        }
    }

    @Test
    public void testAllEarlyCloses_ZoneId_IsNewYork() {
        for (int year : List.of(2021, 2024)) {
            for (HolidayDate hd : service.getHolidayCalendar().calculateEarlyCloses(year)) {
                EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) hd.getHoliday();
                assertEquals(earlyClose.getZoneId(), EXPECTED_ZONE,
                        earlyClose.getName() + " must be expressed in America/New_York");
            }
        }
    }

    @Test
    public void testEarlyCloses_NotRollable() {
        for (int year : List.of(2021, 2024)) {
            for (HolidayDate hd : service.getHolidayCalendar().calculateEarlyCloses(year)) {
                assertFalse(hd.getHoliday().isRollable(), hd.getHoliday().getName() + " must not be rollable");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Full-day holiday count unchanged / no leakage into calculate()
    // -------------------------------------------------------------------------

    @Test
    public void testFullDayHolidayCount_Unchanged() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        assertNotNull(holidays);
        assertEquals(holidays.size(), FULL_DAY_HOLIDAY_COUNT);
    }

    // -------------------------------------------------------------------------
    // No date appears in both calculate() and calculateEarlyCloses() for the same year
    // -------------------------------------------------------------------------

    @Test(dataProvider = "usEarlyCloseFixture")
    public void testNoDateCollisionBetweenFullDayAndEarlyClose(int year, boolean july3Present, boolean dec24Present, int expectedCount) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        Set<LocalDate> fullDayDates = calendar.calculate(year).stream()
                .map(HolidayDate::getDate)
                .collect(Collectors.toSet());
        Set<LocalDate> earlyCloseDates = calendar.calculateEarlyCloses(year).stream()
                .map(HolidayDate::getDate)
                .collect(Collectors.toSet());
        Set<LocalDate> intersection = fullDayDates.stream()
                .filter(earlyCloseDates::contains)
                .collect(Collectors.toSet());
        assertTrue(intersection.isEmpty(),
                "US " + year + ": dates must not appear in both calculate() and calculateEarlyCloses(): " + intersection);
    }

    // -------------------------------------------------------------------------
    // Cross-check calculateEarlyCloses() dates against raw Observance output
    // -------------------------------------------------------------------------

    @DataProvider(name = "earlyCloseDates")
    public Iterator<Object[]> earlyCloseDates() {
        List<Object[]> data = Arrays.asList(
                new Object[]{"Day After Thanksgiving", 2021},
                new Object[]{"Day After Thanksgiving", 2024},
                new Object[]{"July 3rd", 2019},
                new Object[]{"July 3rd", 2023},
                new Object[]{"July 3rd", 2024},
                new Object[]{"Christmas Eve", 2019},
                new Object[]{"Christmas Eve", 2024},
                new Object[]{"Christmas Eve", 2025}
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
            case "Day After Thanksgiving" -> new DayAfterThanksgiving().apply(year);
            case "July 3rd" -> new JulyThirdEarlyClose().apply(year);
            case "Christmas Eve" -> new ChristmasEveEarlyClose().apply(year);
            default -> throw new IllegalArgumentException("Unknown holiday: " + holidayName);
        };
    }

}
