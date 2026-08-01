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
import org.holiday.calendar.observance.ca.ChristmasEveEarlyClose;
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
 * Tests for the {@code CA} calendar's early-close (TSX half-day closure)
 * holiday: Christmas Eve. Unlike US's Christmas Eve early close (keyed off
 * December 25's day of week), CA registers exactly one EARLY_CLOSE holiday,
 * so the count per year is either 0 or 1 — never more than one.
 */
public class HolidayCalendarServiceCAEarlyCloseTest {

    private static final LocalTime EXPECTED_CLOSE_TIME = LocalTime.of(13, 0);
    private static final ZoneId EXPECTED_ZONE = ZoneId.of("America/Toronto");

    // 13 full-day holidays registered in HolidayCalendarServiceCA; Christmas Eve is the
    // only EARLY_CLOSE holiday and is excluded by calculate(), so calculate() sees 13.
    private static final int FULL_DAY_HOLIDAY_COUNT = 13;

    private final HolidayCalendarServiceCA service = new HolidayCalendarServiceCA();

    // -------------------------------------------------------------------------
    // Count / presence per year (verified 2017-2026 TSX cycle)
    // -------------------------------------------------------------------------

    @DataProvider(name = "caEarlyCloseFixture")
    public Iterator<Object[]> caEarlyCloseFixture() {
        List<Object[]> data = Arrays.asList(
                new Object[]{2017, false},
                new Object[]{2018, true},
                new Object[]{2019, true},
                new Object[]{2021, true},
                new Object[]{2022, false},
                new Object[]{2023, false},
                new Object[]{2024, true},
                new Object[]{2025, true},
                new Object[]{2026, true}
        );
        return data.iterator();
    }

    @Test(dataProvider = "caEarlyCloseFixture")
    public void testEarlyCloseCountForYear(int year, boolean present) {
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(year);
        assertNotNull(earlyCloses);
        assertEquals(earlyCloses.size(), present ? 1 : 0, "CA " + year + ": unexpected early-close count");
    }

    @Test(dataProvider = "caEarlyCloseFixture")
    public void testChristmasEvePresenceForYear(int year, boolean present) {
        assertEquals(earlyCloseNames(year).contains("Christmas Eve"), present,
                "CA " + year + ": Christmas Eve presence must match December 24 day-of-week rule");
    }

    private Set<String> earlyCloseNames(int year) {
        return service.getHolidayCalendar().calculateEarlyCloses(year).stream()
                .map(hd -> hd.getHoliday().getName())
                .collect(Collectors.toSet());
    }

    // -------------------------------------------------------------------------
    // Suppressed-year sanity checks
    // -------------------------------------------------------------------------

    @Test
    public void testChristmasEveSuppressedIn2022() {
        // 2022: December 25 = Sunday, December 24 = Saturday.
        assertFalse(earlyCloseNames(2022).contains("Christmas Eve"));
    }

    @Test
    public void testChristmasEveSuppressedIn2023() {
        // 2023: December 25 = Monday, December 24 = Sunday.
        assertFalse(earlyCloseNames(2023).contains("Christmas Eve"));
    }

    // -------------------------------------------------------------------------
    // NYSE-divergence regression case
    // -------------------------------------------------------------------------

    @Test
    public void testTsxClosesEarlyIn2021DespiteNyseStyleRuleSuppressing() {
        // 2021: December 25 = Saturday, December 24 = Friday. NYSE's rule (keyed off
        // Dec 25) would suppress this year; TSX's rule (keyed off Dec 24) does not.
        assertTrue(earlyCloseNames(2021).contains("Christmas Eve"));
    }

    // -------------------------------------------------------------------------
    // Close time / zone / rollability
    // -------------------------------------------------------------------------

    @Test
    public void testCloseTimeIs13_00() {
        for (int year : List.of(2018, 2024)) {
            for (HolidayDate hd : service.getHolidayCalendar().calculateEarlyCloses(year)) {
                assertTrue(hd.getHoliday() instanceof EarlyCloseHoliday);
                EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) hd.getHoliday();
                assertEquals(earlyClose.getCloseTime(), EXPECTED_CLOSE_TIME,
                        earlyClose.getName() + " must close at 13:00");
            }
        }
    }

    @Test
    public void testZoneIdIsAmericaToronto() {
        for (int year : List.of(2018, 2024)) {
            for (HolidayDate hd : service.getHolidayCalendar().calculateEarlyCloses(year)) {
                EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) hd.getHoliday();
                assertEquals(earlyClose.getZoneId(), EXPECTED_ZONE,
                        earlyClose.getName() + " must be expressed in America/Toronto");
            }
        }
    }

    @Test
    public void testEarlyCloseNotRollable() {
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

    @Test(dataProvider = "caEarlyCloseFixture")
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
                "CA " + year + ": dates must not appear in both calculate() and calculateEarlyCloses(): " + intersection);
    }

    // -------------------------------------------------------------------------
    // Cross-check calculateEarlyCloses() dates against raw Observance output
    // -------------------------------------------------------------------------

    @Test(dataProvider = "caEarlyCloseFixture")
    public void testEarlyCloseDateMatchesRawObservance(int year, boolean present) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
        if (!present) {
            assertTrue(earlyCloses.isEmpty(), "CA " + year + ": expected no early closes");
            return;
        }
        LocalDate expected = new ChristmasEveEarlyClose().apply(year);
        HolidayDate matched = earlyCloses.stream()
                .filter(hd -> "Christmas Eve".equals(hd.getHoliday().getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Christmas Eve not found in calculateEarlyCloses(" + year + ")"));
        assertEquals(matched.getDate(), expected,
                "Christmas Eve via calculateEarlyCloses(" + year + ") must match production Observance");
    }

}
