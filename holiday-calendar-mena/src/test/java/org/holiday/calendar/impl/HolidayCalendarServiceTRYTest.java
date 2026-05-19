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

public class HolidayCalendarServiceTRYTest {

    // 7 fixed + 3 Eid al-Fitr + 4 Eid al-Adha = 14 (Republic Day Eve omitted; half-day not modelled)
    private static final int TRY_HOLIDAY_COUNT = 14;

    private final HolidayCalendarServiceTRY service = new HolidayCalendarServiceTRY();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // =========================================================================
    // Identity — SPI discovery
    // =========================================================================

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("TRY"));
        assertFalse(service.isProvided("TR"));
        assertFalse(service.isProvided("SAR"));
        assertFalse(service.isProvided("US"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "TRY");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Turkey (BIST/TCMB) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("TRY");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "TRY");
    }

    // =========================================================================
    // Weekend configuration — Saturday + Sunday
    // =========================================================================

    @Test
    public void testWeekendDaysSaturdayAndSunday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2);
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY));
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY));
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY));
    }

    // =========================================================================
    // Holiday count
    // =========================================================================

    @Test
    public void testCalculate2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), TRY_HOLIDAY_COUNT,
                "Expected " + TRY_HOLIDAY_COUNT + " holidays for 2025, got: " + holidays.size());
    }

    // =========================================================================
    // Chronological order
    // =========================================================================

    @Test
    public void testChronologicalOrder2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).date().isBefore(holidays.get(i - 1).date()),
                    "Holidays must be in chronological order");
        }
    }

    // =========================================================================
    // Holiday name membership
    // =========================================================================

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"New Year's Day"},
            new Object[]{"National Sovereignty and Children's Day"},
            new Object[]{"Labour and Solidarity Day"},
            new Object[]{"Commemoration of Atatürk, Youth and Sports Day"},
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Eid al-Fitr (3rd Day)"},
            new Object[]{"Democracy and National Unity Day"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Eid al-Adha (3rd Day)"},
            new Object[]{"Eid al-Adha (4th Day)"},
            new Object[]{"Victory Day"},
            new Object[]{"Republic Day"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        assertTrue(service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName())),
                "Calendar must contain holiday: " + holidayName);
    }

    // =========================================================================
    // No-roll behavior — fixed holidays stay on natural date
    // =========================================================================

    @Test
    public void testVictoryDay2025NotRolled() {
        // Aug 30, 2025 = Saturday; TR rolls to Sep 1 but TRY stays on Aug 30
        assertEquals(LocalDate.of(2025, Month.AUGUST, 30).getDayOfWeek(), DayOfWeek.SATURDAY);
        HolidayCalendar trCalendar = new HolidayCalendarServiceTR().getHolidayCalendar();
        assertEquals(findFirst("Victory Day", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.AUGUST, 30),
                "TRY must not roll Victory Day 2025 (Saturday) — noRoll() convention");
        assertEquals(trCalendar.calculate(2025).stream()
                .filter(hd -> "Victory Day".equals(hd.holiday().getName()))
                .findFirst().orElseThrow().date(),
                LocalDate.of(2025, Month.SEPTEMBER, 1),
                "TR must roll Victory Day 2025 (Saturday) to Sep 1");
    }

    @Test
    public void testNewYearsDay2028NotRolled() {
        // Jan 1, 2028 = Saturday; TR rolls to Jan 3 (Monday) but TRY stays on Jan 1
        assertEquals(LocalDate.of(2028, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.SATURDAY);
        assertEquals(findFirst("New Year's Day", 2028).orElseThrow().date(),
                LocalDate.of(2028, Month.JANUARY, 1),
                "TRY must not roll New Year's Day 2028 (Saturday) — noRoll() convention");
    }

    // =========================================================================
    // TRY mirrors TR — identical dates for any given year
    // =========================================================================

    @Test
    public void testTRYSameHolidayCountAsTR2025() {
        HolidayCalendar trCalendar = new HolidayCalendarServiceTR().getHolidayCalendar();
        assertEquals(service.getHolidayCalendar().calculate(2025).size(),
                trCalendar.calculate(2025).size(),
                "TRY and TR must produce the same number of holidays for 2025");
    }

    @Test
    public void testTRYSameHolidayCountAsTR2026() {
        HolidayCalendar trCalendar = new HolidayCalendarServiceTR().getHolidayCalendar();
        assertEquals(service.getHolidayCalendar().calculate(2026).size(),
                trCalendar.calculate(2026).size(),
                "TRY and TR must produce the same number of holidays for 2026");
    }

    @Test
    public void testEidAlFitr2025MatchesTR() {
        assertMatchesTR("Eid al-Fitr", 2025);
    }

    @Test
    public void testEidAlAdha2025MatchesTR() {
        assertMatchesTR("Eid al-Adha", 2025);
    }

    @Test
    public void testEidAlFitr2026MatchesTR() {
        assertMatchesTR("Eid al-Fitr", 2026);
    }

    @Test
    public void testEidAlAdha2026MatchesTR() {
        assertMatchesTR("Eid al-Adha", 2026);
    }

    // =========================================================================
    // dataValidThrough
    // =========================================================================

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "TRY calendar has lookup-table Islamic holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("TRY");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), 2055,
                "factory.dataValidThrough(\"TRY\") must return 2055");
    }

    // =========================================================================
    // Boundary: 2055 / beyond ceiling: 2056
    // =========================================================================

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundaryYear = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundaryYear);
        assertFalse(holidays.isEmpty(),
                "calculate(2055) must return holidays — within the covered range");
        assertEquals(holidays.size(), TRY_HOLIDAY_COUNT,
                "Expected " + TRY_HOLIDAY_COUNT + " holidays for boundary year " + boundaryYear);
    }

    @Test
    public void testCalculateBeyondDataValidThroughDropsIslamicHolidays() {
        int boundaryYear = service.dataValidThrough().orElseThrow();
        List<HolidayDate> atBoundary = service.getHolidayCalendar().calculate(boundaryYear);
        List<HolidayDate> beyondBoundary = service.getHolidayCalendar().calculate(boundaryYear + 1);
        assertTrue(beyondBoundary.size() < atBoundary.size(),
                "Year beyond dataValidThrough must produce fewer holidays; "
                + "at boundary: " + atBoundary.size() + ", beyond: " + beyondBoundary.size());
    }

    // =========================================================================
    // Republic Day Eve (Oct 28) — confirmed absent
    // =========================================================================

    @Test
    public void testRepublicDayEveAbsent2025() {
        boolean oct28Present = service.getHolidayCalendar().calculate(2025).stream()
                .anyMatch(hd -> hd.date().equals(LocalDate.of(2025, Month.OCTOBER, 28)));
        assertFalse(oct28Present,
                "Oct 28 (Republic Day Eve) must not be present — half-day closures are not modelled");
    }

    // =========================================================================
    // Helper
    // =========================================================================

    private Optional<HolidayDate> findFirst(String name, int year) {
        return service.getHolidayCalendar().calculate(year).stream()
                .filter(hd -> name.equals(hd.holiday().getName()))
                .findFirst();
    }

    private void assertMatchesTR(String holidayName, int year) {
        Optional<HolidayDate> tryDate = findFirst(holidayName, year);
        Optional<HolidayDate> trDate = new HolidayCalendarServiceTR().getHolidayCalendar()
                .calculate(year).stream()
                .filter(hd -> holidayName.equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(tryDate.isPresent(), "TRY must contain: " + holidayName);
        assertTrue(trDate.isPresent(), "TR must contain: " + holidayName);
        assertEquals(tryDate.get().date(), trDate.get().date(),
                "TRY " + holidayName + " " + year + " must match TR");
    }
}
