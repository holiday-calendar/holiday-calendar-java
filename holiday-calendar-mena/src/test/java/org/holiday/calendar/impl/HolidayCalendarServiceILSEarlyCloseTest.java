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
import org.holiday.calendar.observance.hebrew.ErevPassover;
import org.holiday.calendar.observance.hebrew.ErevRoshHashanah;
import org.holiday.calendar.observance.hebrew.ErevShavuot;
import org.holiday.calendar.observance.hebrew.ErevSukkot;
import org.holiday.calendar.observance.hebrew.ErevYomKippur;
import org.holiday.calendar.observance.hebrew.Passover;
import org.holiday.calendar.observance.hebrew.RoshHashanah;
import org.holiday.calendar.observance.hebrew.Shavuot;
import org.holiday.calendar.observance.hebrew.Sukkot;
import org.holiday.calendar.observance.hebrew.YomKippur;
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
 * Tests for the {@code ILS} calendar's early-close (TASE half-day closure /
 * Bank of Israel reduced-hours) holidays, provided by
 * {@link IsraelHolidays#earlyCloseHolidays()}.
 */
public class HolidayCalendarServiceILSEarlyCloseTest {

    private static final int EARLY_CLOSE_COUNT = 6;
    private static final int FULL_DAY_HOLIDAY_COUNT = 9;
    private static final LocalTime EXPECTED_CLOSE_TIME = LocalTime.of(13, 0);
    private static final LocalTime HOSHANA_RABA_CLOSE_TIME = LocalTime.of(13, 15);
    private static final ZoneId EXPECTED_ZONE = ZoneId.of("Asia/Jerusalem");

    private final HolidayCalendarServiceILS service = new HolidayCalendarServiceILS();

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
        Set<String> expectedNames = Set.of(
                "Erev Rosh Hashanah",
                "Erev Yom Kippur",
                "Erev Passover",
                "Erev Shavuot",
                "Erev Sukkot",
                "Hoshana Raba"
        );
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
    public void testAllEarlyCloses_CloseTimeIs13_00() {
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(2025);
        assertEquals(earlyCloses.size(), EARLY_CLOSE_COUNT);
        for (HolidayDate hd : earlyCloses) {
            assertTrue(hd.getHoliday() instanceof EarlyCloseHoliday);
            EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) hd.getHoliday();
            if ("Hoshana Raba".equals(earlyClose.getName())) {
                // Hoshana Raba has its own distinct close time; verified separately below.
                continue;
            }
            assertEquals(earlyClose.getCloseTime(), EXPECTED_CLOSE_TIME,
                    earlyClose.getName() + " must close at 13:00");
        }
    }

    @Test
    public void testHoshanaRaba_CloseTimeIs13_15() {
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(2025);
        HolidayDate hoshanaRaba = earlyCloses.stream()
                .filter(hd -> "Hoshana Raba".equals(hd.getHoliday().getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Hoshana Raba not found in calculateEarlyCloses(2025)"));
        assertTrue(hoshanaRaba.getHoliday() instanceof EarlyCloseHoliday);
        EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) hoshanaRaba.getHoliday();
        assertEquals(earlyClose.getCloseTime(), HOSHANA_RABA_CLOSE_TIME,
                "Hoshana Raba must close at 13:15, distinct from the 13:00 holiday-eve early closes");
    }

    @Test
    public void testAllEarlyCloses_ZoneId_IsJerusalem() {
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(2025);
        assertEquals(earlyCloses.size(), EARLY_CLOSE_COUNT);
        for (HolidayDate hd : earlyCloses) {
            EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) hd.getHoliday();
            assertEquals(earlyClose.getZoneId(), EXPECTED_ZONE,
                    earlyClose.getName() + " must be expressed in Asia/Jerusalem");
        }
    }

    // -------------------------------------------------------------------------
    // Erev date == main holiday date minus 1 day, cross-checked against
    // calculateEarlyCloses() output, across multiple years
    // -------------------------------------------------------------------------

    @DataProvider(name = "erevDates")
    public Iterator<Object[]> erevDates() {
        List<Object[]> data = Arrays.asList(
                new Object[]{"Erev Rosh Hashanah", "Rosh Hashanah", 2024},
                new Object[]{"Erev Rosh Hashanah", "Rosh Hashanah", 2025},
                new Object[]{"Erev Rosh Hashanah", "Rosh Hashanah", 2026},
                new Object[]{"Erev Yom Kippur", "Yom Kippur", 2024},
                new Object[]{"Erev Yom Kippur", "Yom Kippur", 2025},
                new Object[]{"Erev Yom Kippur", "Yom Kippur", 2026},
                new Object[]{"Erev Passover", "Passover", 2024},
                new Object[]{"Erev Passover", "Passover", 2025},
                new Object[]{"Erev Passover", "Passover", 2026},
                new Object[]{"Erev Shavuot", "Shavuot", 2024},
                new Object[]{"Erev Shavuot", "Shavuot", 2025},
                new Object[]{"Erev Shavuot", "Shavuot", 2026},
                new Object[]{"Erev Sukkot", "Sukkot", 2024},
                new Object[]{"Erev Sukkot", "Sukkot", 2025},
                new Object[]{"Erev Sukkot", "Sukkot", 2026}
        );
        return data.iterator();
    }

    @Test(dataProvider = "erevDates")
    public void testErevDateIsOneDayBeforeMainHoliday(String erevName, String mainHolidayName, int year) {
        LocalDate expected = mainHolidayDate(mainHolidayName, year).minusDays(1);
        LocalDate erevObservanceDate = erevObservanceDate(erevName, year);
        assertEquals(erevObservanceDate, expected,
                erevName + " " + year + " must be exactly 1 day before " + mainHolidayName);

        // Cross-check against calculateEarlyCloses() output
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
        HolidayDate matched = earlyCloses.stream()
                .filter(hd -> erevName.equals(hd.getHoliday().getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError(erevName + " not found in calculateEarlyCloses(" + year + ")"));
        assertEquals(matched.getDate(), expected,
                erevName + " via calculateEarlyCloses(" + year + ") must match production Observance");
    }

    private LocalDate mainHolidayDate(String mainHolidayName, int year) {
        return switch (mainHolidayName) {
            case "Rosh Hashanah" -> new RoshHashanah().apply(year);
            case "Yom Kippur" -> new YomKippur().apply(year);
            case "Passover" -> new Passover().apply(year);
            case "Shavuot" -> new Shavuot().apply(year);
            case "Sukkot" -> new Sukkot().apply(year);
            default -> throw new IllegalArgumentException("Unknown holiday: " + mainHolidayName);
        };
    }

    private LocalDate erevObservanceDate(String erevName, int year) {
        return switch (erevName) {
            case "Erev Rosh Hashanah" -> new ErevRoshHashanah().apply(year);
            case "Erev Yom Kippur" -> new ErevYomKippur().apply(year);
            case "Erev Passover" -> new ErevPassover().apply(year);
            case "Erev Shavuot" -> new ErevShavuot().apply(year);
            case "Erev Sukkot" -> new ErevSukkot().apply(year);
            default -> throw new IllegalArgumentException("Unknown erev: " + erevName);
        };
    }

}
