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
import org.holiday.calendar.observance.sg.ChristmasEveEarlyClose;
import org.holiday.calendar.observance.sg.NewYearsEveEarlyClose;
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
 * Tests for the {@code XSES} calendar's early-close (SGX half-day closure)
 * holidays: Christmas Eve and New Year's Eve. Because December 24 and
 * December 31 are always exactly 7 days apart, they always share the same
 * day-of-week within a given year — both early closes are always present
 * together or absent together, never one without the other. This is a real
 * difference from CA (single early close) and worth its own dedicated
 * assertions below, beyond the standard count/presence/closeTime/zone checks.
 */
public class HolidayCalendarServiceXSESEarlyCloseTest {

    private static final LocalTime EXPECTED_CLOSE_TIME = LocalTime.of(12, 0);
    private static final ZoneId EXPECTED_ZONE = ZoneId.of("Asia/Singapore");

    // 11 full-day holidays registered in HolidayCalendarServiceXSES; the two Eve
    // holidays are EARLY_CLOSE and excluded by calculate(), so calculate() sees 11.
    private static final int FULL_DAY_HOLIDAY_COUNT = 11;

    private final HolidayCalendarServiceXSES service = new HolidayCalendarServiceXSES();

    // -------------------------------------------------------------------------
    // Count / presence per year (verified 2018-2026 SGX cycle)
    // -------------------------------------------------------------------------

    @DataProvider(name = "xsesEarlyCloseFixture")
    public Iterator<Object[]> xsesEarlyCloseFixture() {
        List<Object[]> data = Arrays.asList(
                new Object[]{2018, true},
                new Object[]{2019, true},
                new Object[]{2020, true},
                new Object[]{2021, true},
                new Object[]{2022, false},
                new Object[]{2023, false},
                new Object[]{2024, true},
                new Object[]{2025, true},
                new Object[]{2026, true}
        );
        return data.iterator();
    }

    @Test(dataProvider = "xsesEarlyCloseFixture")
    public void testEarlyCloseCountForYear(int year, boolean present) {
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(year);
        assertNotNull(earlyCloses);
        assertEquals(earlyCloses.size(), present ? 2 : 0, "XSES " + year + ": unexpected early-close count");
    }

    @Test(dataProvider = "xsesEarlyCloseFixture")
    public void testChristmasEvePresenceForYear(int year, boolean present) {
        assertEquals(earlyCloseNames(year).contains("Christmas Eve"), present,
                "XSES " + year + ": Christmas Eve presence must match December 24 day-of-week rule");
    }

    @Test(dataProvider = "xsesEarlyCloseFixture")
    public void testNewYearsEvePresenceForYear(int year, boolean present) {
        assertEquals(earlyCloseNames(year).contains("New Year's Eve"), present,
                "XSES " + year + ": New Year's Eve presence must match December 31 day-of-week rule");
    }

    private Set<String> earlyCloseNames(int year) {
        return service.getHolidayCalendar().calculateEarlyCloses(year).stream()
                .map(hd -> hd.getHoliday().getName())
                .collect(Collectors.toSet());
    }

    // -------------------------------------------------------------------------
    // Dual-suppression / dual-presence boundary (the XSES-specific difference)
    // -------------------------------------------------------------------------

    @Test
    public void testBothEarlyClosesSuppressedTogetherIn2022() {
        // 2022: December 24 and December 31 both fall on Saturday.
        assertTrue(service.getHolidayCalendar().calculateEarlyCloses(2022).isEmpty());
    }

    @Test
    public void testBothEarlyClosesSuppressedTogetherIn2023() {
        // 2023: December 24 and December 31 both fall on Sunday.
        assertTrue(service.getHolidayCalendar().calculateEarlyCloses(2023).isEmpty());
    }

    @Test
    public void testBothEarlyClosesPresentTogetherIn2024() {
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(2024);
        assertEquals(earlyCloses.size(), 2);
        Set<String> names = earlyCloses.stream()
                .map(hd -> hd.getHoliday().getName())
                .collect(Collectors.toSet());
        assertEquals(names, Set.of("Christmas Eve", "New Year's Eve"));
    }

    @Test(dataProvider = "xsesEarlyCloseFixture")
    public void testCountIsNeverExactlyOne(int year, boolean present) {
        int count = service.getHolidayCalendar().calculateEarlyCloses(year).size();
        assertNotEquals(count, 1, "XSES " + year + ": Christmas Eve and New Year's Eve must always co-occur, never appear alone");
    }

    // -------------------------------------------------------------------------
    // Close time / zone / rollability
    // -------------------------------------------------------------------------

    @Test
    public void testCloseTimeIs12_00() {
        for (int year : List.of(2018, 2024)) {
            for (HolidayDate hd : service.getHolidayCalendar().calculateEarlyCloses(year)) {
                assertTrue(hd.getHoliday() instanceof EarlyCloseHoliday);
                EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) hd.getHoliday();
                assertEquals(earlyClose.getCloseTime(), EXPECTED_CLOSE_TIME,
                        earlyClose.getName() + " must close at 12:00");
            }
        }
    }

    @Test
    public void testZoneIdIsAsiaSingapore() {
        for (int year : List.of(2018, 2024)) {
            for (HolidayDate hd : service.getHolidayCalendar().calculateEarlyCloses(year)) {
                EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) hd.getHoliday();
                assertEquals(earlyClose.getZoneId(), EXPECTED_ZONE,
                        earlyClose.getName() + " must be expressed in Asia/Singapore");
            }
        }
    }

    @Test
    public void testEarlyClosesNotRollable() {
        for (int year : List.of(2018, 2024)) {
            for (HolidayDate hd : service.getHolidayCalendar().calculateEarlyCloses(year)) {
                assertFalse(hd.getHoliday().isRollable(), hd.getHoliday().getName() + " must not be rollable");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Full-day holiday count unchanged / no leakage into calculate()
    // -------------------------------------------------------------------------

    @Test
    public void testFullDayHolidayCountUnchanged() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        assertNotNull(holidays);
        assertEquals(holidays.size(), FULL_DAY_HOLIDAY_COUNT);
    }

    // -------------------------------------------------------------------------
    // No date appears in both calculate() and calculateEarlyCloses() for the same year
    // -------------------------------------------------------------------------

    @Test(dataProvider = "xsesEarlyCloseFixture")
    public void testNoDateCollisionBetweenFullDayAndEarlyClose(int year, boolean present) {
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
                "XSES " + year + ": dates must not appear in both calculate() and calculateEarlyCloses(): " + intersection);
    }

    // -------------------------------------------------------------------------
    // Cross-check calculateEarlyCloses() dates against raw Observance output
    // -------------------------------------------------------------------------

    @DataProvider(name = "earlyCloseDates")
    public Iterator<Object[]> earlyCloseDates() {
        List<Object[]> data = Arrays.asList(
                new Object[]{"Christmas Eve", 2018},
                new Object[]{"Christmas Eve", 2024},
                new Object[]{"Christmas Eve", 2025},
                new Object[]{"Christmas Eve", 2026},
                new Object[]{"New Year's Eve", 2018},
                new Object[]{"New Year's Eve", 2024},
                new Object[]{"New Year's Eve", 2025},
                new Object[]{"New Year's Eve", 2026}
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
