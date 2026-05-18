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
import org.holiday.calendar.HolidayCalendarFactory;
import org.holiday.calendar.HolidayDate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.testng.Assert.*;

public class HolidayCalendarServiceILSTest {

    private static final int ILS_HOLIDAY_COUNT = 9;

    private final HolidayCalendarServiceILS service = new HolidayCalendarServiceILS();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // -------------------------------------------------------------------------
    // Identity
    // -------------------------------------------------------------------------

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("ILS"));
        assertFalse(service.isProvided("IL"));
        assertFalse(service.isProvided("SAR"));
        assertFalse(service.isProvided("US"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "ILS");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Israel (TASE/Bank of Israel) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("ILS");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "ILS");
    }

    // -------------------------------------------------------------------------
    // Weekend configuration
    // -------------------------------------------------------------------------

    @Test
    public void testWeekendDaysFridayAndSaturday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2, "Israeli weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY), "Friday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY), "Saturday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY), "Sunday must not be a weekend day");
    }

    // -------------------------------------------------------------------------
    // dataValidThrough — algorithmic, no ceiling
    // -------------------------------------------------------------------------

    @Test
    public void testDataValidThroughReturnsEmpty() {
        OptionalInt result = service.dataValidThrough();
        assertTrue(result.isEmpty(),
                "ILS uses HebrewCalendar arithmetic; dataValidThrough() must return empty");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("ILS");
        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // Holiday count
    // -------------------------------------------------------------------------

    @Test
    public void testCalculate2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), ILS_HOLIDAY_COUNT);
    }

    @Test
    public void testChronologicalOrder2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).date().isBefore(holidays.get(i - 1).date()),
                    "Holidays must be in chronological order");
        }
    }

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"Rosh Hashanah"},
            new Object[]{"Rosh Hashanah (2nd Day)"},
            new Object[]{"Yom Kippur"},
            new Object[]{"Sukkot"},
            new Object[]{"Shemini Atzeret / Simchat Torah"},
            new Object[]{"Passover"},
            new Object[]{"Passover (7th Day)"},
            new Object[]{"Yom Ha'atzmaut"},
            new Object[]{"Shavuot"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertTrue(calendar.getHolidays().stream().anyMatch(h -> holidayName.equals(h.getName())),
                "Calendar must contain holiday: " + holidayName);
    }

    // -------------------------------------------------------------------------
    // ILS mirrors IL: all floating Hebrew dates must be identical
    // -------------------------------------------------------------------------

    @Test
    public void testRoshHashanah2025MatchesIL() {
        assertMatchesIL("Rosh Hashanah", 2025);
    }

    @Test
    public void testPassover2025MatchesIL() {
        assertMatchesIL("Passover", 2025);
    }

    @Test
    public void testYomHaatzmaut2025MatchesIL() {
        assertMatchesIL("Yom Ha'atzmaut", 2025);
    }

    @Test
    public void testShavuot2025MatchesIL() {
        assertMatchesIL("Shavuot", 2025);
    }

    @Test
    public void testYomKippur2025MatchesIL() {
        assertMatchesIL("Yom Kippur", 2025);
    }

    // -------------------------------------------------------------------------
    // Passover 7th Day falls on Saturday 2025 — ILS observes on natural date (no roll)
    // -------------------------------------------------------------------------

    @Test
    public void testPassoverEnd2025NotRolled() {
        // 21 Nisan 5785 = 2025-04-19 (Saturday — Israeli weekend)
        // ILS uses noRoll(); the date stays on Saturday
        assertEquals(LocalDate.of(2025, Month.APRIL, 19).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> hd = findFirst("Passover (7th Day)", 2025);
        assertTrue(hd.isPresent());
        assertEquals(hd.get().date(), LocalDate.of(2025, Month.APRIL, 19),
                "ILS Passover 7th Day 2025 (Saturday) must not be rolled — noRoll() convention");
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private Optional<HolidayDate> findFirst(String name, int year) {
        return service.getHolidayCalendar().calculate(year).stream()
                .filter(hd -> name.equals(hd.holiday().getName()))
                .findFirst();
    }

    private void assertMatchesIL(String holidayName, int year) {
        Optional<HolidayDate> ilsDate = findFirst(holidayName, year);
        Optional<HolidayDate> ilDate = new HolidayCalendarServiceIL().getHolidayCalendar()
                .calculate(year).stream()
                .filter(hd -> holidayName.equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(ilsDate.isPresent(), "ILS must contain: " + holidayName);
        assertTrue(ilDate.isPresent(), "IL must contain: " + holidayName);
        assertEquals(ilsDate.get().date(), ilDate.get().date(),
                "ILS " + holidayName + " " + year + " must match IL (all Hebrew holidays are rollable=false)");
    }

}
