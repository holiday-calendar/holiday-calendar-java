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

public class HolidayCalendarServiceKWDTest {

    // Same 13 holidays as KW — exchange uses the national holiday set, no-roll only
    private static final int KWD_HOLIDAY_COUNT = 13;

    // Fixed holidays survive beyond the CSV ceiling
    private static final int KWD_FIXED_HOLIDAY_COUNT = 3;

    private final HolidayCalendarServiceKWD service = new HolidayCalendarServiceKWD();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // ── service identity ──────────────────────────────────────────────────────

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("KWD"));
        assertFalse(service.isProvided("KW"));
        assertFalse(service.isProvided("QAR"));
        assertFalse(service.isProvided("SAR"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "KWD");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Kuwait (Boursa Kuwait/CBK) Holidays");
    }

    // ── factory integration ───────────────────────────────────────────────────

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("KWD");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "KWD");
    }

    // ── weekend configuration ─────────────────────────────────────────────────

    @Test
    public void testWeekendDaysFridayAndSaturday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2, "Kuwait weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY), "Friday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY), "Saturday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY), "Sunday must not be a weekend day");
    }

    // ── total count ───────────────────────────────────────────────────────────

    @Test
    public void testCalculate2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        assertNotNull(holidays);
        assertEquals(holidays.size(), KWD_HOLIDAY_COUNT,
                "Expected " + KWD_HOLIDAY_COUNT + " holidays for 2024, got: " + holidays.size());
    }

    @Test
    public void testCalculate2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), KWD_HOLIDAY_COUNT,
                "Expected " + KWD_HOLIDAY_COUNT + " holidays for 2025, got: " + holidays.size());
    }

    // ── no-roll: Liberation Day stays on natural date (Friday 2027) ───────────

    // Feb 26, 2027 is Friday — KWD must NOT roll (noRoll convention)
    // KW rolls this to Sunday Feb 28; KWD stays on Feb 26
    @Test
    public void testLiberationDayOnFridayNotRolled2027() {
        assertEquals(LocalDate.of(2027, Month.FEBRUARY, 26).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ld = findFirst("Liberation Day", 2027);
        assertTrue(ld.isPresent());
        assertEquals(ld.get().date(), LocalDate.of(2027, Month.FEBRUARY, 26),
                "KWD must not roll Liberation Day 2027 (Friday) — settlement uses no-roll convention");
    }

    // ── no-roll: National Day stays on natural date (Friday 2028) ────────────

    // Feb 25, 2028 is Friday — KWD must NOT roll
    @Test
    public void testNationalDayOnFridayNotRolled2028() {
        assertEquals(LocalDate.of(2028, Month.FEBRUARY, 25).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> nd = findFirst("National Day", 2028);
        assertTrue(nd.isPresent());
        assertEquals(nd.get().date(), LocalDate.of(2028, Month.FEBRUARY, 25),
                "KWD must not roll National Day 2028 (Friday) — settlement uses no-roll convention");
    }

    // Feb 26, 2028 is Saturday — KWD must NOT roll
    @Test
    public void testLiberationDayOnSaturdayNotRolled2028() {
        assertEquals(LocalDate.of(2028, Month.FEBRUARY, 26).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> ld = findFirst("Liberation Day", 2028);
        assertTrue(ld.isPresent());
        assertEquals(ld.get().date(), LocalDate.of(2028, Month.FEBRUARY, 26),
                "KWD must not roll Liberation Day 2028 (Saturday) — settlement uses no-roll convention");
    }

    // Jan 1, 2027 is Friday — KWD must NOT roll
    @Test
    public void testNewYearsDayOnFridayNotRolled2027() {
        assertEquals(LocalDate.of(2027, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2027);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2027, Month.JANUARY, 1),
                "KWD must not roll New Year's Day 2027 (Friday) — settlement uses no-roll convention");
    }

    // ── Islamic dates match KW (same CSV source) ──────────────────────────────

    @Test
    public void testEidAlFitr2025MatchesKW() {
        HolidayCalendar kwCalendar = new HolidayCalendarServiceKW().getHolidayCalendar();
        Optional<HolidayDate> kwdEid = findFirst("Eid al-Fitr", 2025);
        Optional<HolidayDate> kwEid  = kwCalendar.calculate(2025).stream()
                .filter(hd -> "Eid al-Fitr".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(kwdEid.isPresent());
        assertTrue(kwEid.isPresent());
        assertEquals(kwdEid.get().date(), kwEid.get().date(),
                "KWD Eid al-Fitr 2025 must match KW (same CSV source)");
    }

    @Test
    public void testEidAlAdha2025MatchesKW() {
        HolidayCalendar kwCalendar = new HolidayCalendarServiceKW().getHolidayCalendar();
        Optional<HolidayDate> kwdAdha = findFirst("Eid al-Adha", 2025);
        Optional<HolidayDate> kwAdha  = kwCalendar.calculate(2025).stream()
                .filter(hd -> "Eid al-Adha".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(kwdAdha.isPresent());
        assertTrue(kwAdha.isPresent());
        assertEquals(kwdAdha.get().date(), kwAdha.get().date(),
                "KWD Eid al-Adha 2025 must match KW (same CSV source)");
    }

    // ── dataValidThrough ──────────────────────────────────────────────────────

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "KWD calendar has CSV-backed holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("KWD");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"KWD\") must delegate to the service");
    }

    // ── CSV boundary behaviour ────────────────────────────────────────────────

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundary);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundary + ") must return holidays — within covered range");
        assertEquals(holidays.size(), KWD_HOLIDAY_COUNT,
                "Expected all " + KWD_HOLIDAY_COUNT + " KWD holidays for boundary year " + boundary);
    }

    @Test
    public void testCalculateBeyondDataValidThroughDropsIslamicHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> atBoundary     = calendar.calculate(boundary);
        List<HolidayDate> beyondBoundary = calendar.calculate(boundary + 1);
        assertTrue(beyondBoundary.size() < atBoundary.size(),
                "Year beyond dataValidThrough must produce fewer holidays (Islamic tables exhausted); " +
                "at boundary: " + atBoundary.size() + ", beyond: " + beyondBoundary.size());
    }

    @Test
    public void testFixedHolidaysPresentBeyondCeiling() {
        List<HolidayDate> holidays2056 = service.getHolidayCalendar().calculate(2056);
        assertEquals(holidays2056.size(), KWD_FIXED_HOLIDAY_COUNT,
                "Beyond CSV ceiling only the " + KWD_FIXED_HOLIDAY_COUNT +
                " fixed KWD holidays must remain");
    }

    // ── holiday name registry ─────────────────────────────────────────────────

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"New Year's Day"},
            new Object[]{"National Day"},
            new Object[]{"Liberation Day"},
            new Object[]{"Isra and Mi'raj"},
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Eid al-Fitr (3rd Day)"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Eid al-Adha (3rd Day)"},
            new Object[]{"Arafat Day"},
            new Object[]{"Islamic New Year"},
            new Object[]{"Prophet's Birthday"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName()));
        assertTrue(found, "Calendar must contain holiday: " + holidayName);
    }

    // ── helper ────────────────────────────────────────────────────────────────

    private Optional<HolidayDate> findFirst(String name, int year) {
        return service.getHolidayCalendar().calculate(year).stream()
                .filter(hd -> name.equals(hd.holiday().getName()))
                .findFirst();
    }

}
