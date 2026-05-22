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

public class HolidayCalendarServiceBHDTest {

    // Same 15 holidays as BH — settlement calendar uses national holiday set, no-roll only
    private static final int BHD_HOLIDAY_COUNT = 15;

    // Fixed holidays survive beyond the CSV ceiling
    private static final int BHD_FIXED_HOLIDAY_COUNT = 4;

    private final HolidayCalendarServiceBHD service = new HolidayCalendarServiceBHD();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // ── service identity ──────────────────────────────────────────────────────

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("BHD"));
        assertFalse(service.isProvided("BH"));
        assertFalse(service.isProvided("KWD"));
        assertFalse(service.isProvided("SAR"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "BHD");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Bahrain (Boursa Bahrain/CBB) Holidays");
    }

    // ── factory integration ───────────────────────────────────────────────────

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("BHD");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "BHD");
    }

    // ── weekend configuration ─────────────────────────────────────────────────

    @Test
    public void testWeekendDaysFridayAndSaturday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2, "Bahrain weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY), "Friday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY), "Saturday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY), "Sunday must not be a weekend day");
    }

    // ── total count ───────────────────────────────────────────────────────────

    @Test
    public void testCalculate2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        assertNotNull(holidays);
        assertEquals(holidays.size(), BHD_HOLIDAY_COUNT,
                "Expected " + BHD_HOLIDAY_COUNT + " holidays for 2024, got: " + holidays.size());
    }

    @Test
    public void testCalculate2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), BHD_HOLIDAY_COUNT,
                "Expected " + BHD_HOLIDAY_COUNT + " holidays for 2025, got: " + holidays.size());
    }

    // ── no-roll: New Year's Day stays on natural date (Friday 2027) ───────────

    // Jan 1, 2027 is Friday — BHD must NOT roll; BH rolls this to Sunday Jan 3
    @Test
    public void testNewYearsDayOnFridayNotRolled2027() {
        assertEquals(LocalDate.of(2027, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2027);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2027, Month.JANUARY, 1),
                "BHD must not roll New Year's Day 2027 (Friday) — settlement uses no-roll convention");
    }

    // Jan 1, 2028 is Saturday — BHD must NOT roll; BH rolls this to Sunday Jan 2
    @Test
    public void testNewYearsDayOnSaturdayNotRolled2028() {
        assertEquals(LocalDate.of(2028, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2028);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2028, Month.JANUARY, 1),
                "BHD must not roll New Year's Day 2028 (Saturday) — settlement uses no-roll convention");
    }

    // ── no-roll: National Day stays on natural date (Saturday 2028) ──────────

    // Dec 16, 2028 is Saturday — BHD must NOT roll; BH rolls this to Sunday Dec 17
    @Test
    public void testNationalDayOnSaturdayNotRolled2028() {
        assertEquals(LocalDate.of(2028, Month.DECEMBER, 16).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> nd = findFirst("National Day", 2028);
        assertTrue(nd.isPresent());
        assertEquals(nd.get().date(), LocalDate.of(2028, Month.DECEMBER, 16),
                "BHD must not roll National Day 2028 (Saturday) — settlement uses no-roll convention");
    }

    // ── no-roll: Labour Day stays on natural date ─────────────────────────────

    // May 1, 2026 is Friday — BHD must NOT roll; BH rolls this to Sunday May 3
    @Test
    public void testLabourDayOnFridayNotRolled2026() {
        assertEquals(LocalDate.of(2026, Month.MAY, 1).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ld = findFirst("Labour Day", 2026);
        assertTrue(ld.isPresent());
        assertEquals(ld.get().date(), LocalDate.of(2026, Month.MAY, 1),
                "BHD must not roll Labour Day 2026 (Friday) — settlement uses no-roll convention");
    }

    // ── BHD differs from BH on rolled fixed holidays ──────────────────────────

    @Test
    public void testNewYearsDay2027DiffersBetweenBhAndBhd() {
        // BH rolls Jan 1 (Friday) to Jan 3 (Sunday); BHD stays Jan 1
        HolidayCalendar bhCalendar = new HolidayCalendarServiceBH().getHolidayCalendar();
        Optional<HolidayDate> bhdNy = findFirst("New Year's Day", 2027);
        Optional<HolidayDate> bhNy  = bhCalendar.calculate(2027).stream()
                .filter(hd -> "New Year's Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(bhdNy.isPresent());
        assertTrue(bhNy.isPresent());
        assertNotEquals(bhdNy.get().date(), bhNy.get().date(),
                "BHD New Year's Day 2027 must differ from BH (no-roll vs roll-to-Sunday)");
        assertEquals(bhdNy.get().date(), LocalDate.of(2027, Month.JANUARY, 1));
        assertEquals(bhNy.get().date(), LocalDate.of(2027, Month.JANUARY, 3));
    }

    // ── Islamic dates match BH (same CSV source) ──────────────────────────────

    @Test
    public void testEidAlFitr2025MatchesBH() {
        HolidayCalendar bhCalendar = new HolidayCalendarServiceBH().getHolidayCalendar();
        Optional<HolidayDate> bhdEid = findFirst("Eid al-Fitr", 2025);
        Optional<HolidayDate> bhEid  = bhCalendar.calculate(2025).stream()
                .filter(hd -> "Eid al-Fitr".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(bhdEid.isPresent());
        assertTrue(bhEid.isPresent());
        assertEquals(bhdEid.get().date(), bhEid.get().date(),
                "BHD Eid al-Fitr 2025 must match BH (same CSV source)");
    }

    @Test
    public void testEidAlAdha2025MatchesBH() {
        HolidayCalendar bhCalendar = new HolidayCalendarServiceBH().getHolidayCalendar();
        Optional<HolidayDate> bhdAdha = findFirst("Eid al-Adha", 2025);
        Optional<HolidayDate> bhAdha  = bhCalendar.calculate(2025).stream()
                .filter(hd -> "Eid al-Adha".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(bhdAdha.isPresent());
        assertTrue(bhAdha.isPresent());
        assertEquals(bhdAdha.get().date(), bhAdha.get().date(),
                "BHD Eid al-Adha 2025 must match BH (same CSV source)");
    }

    @Test
    public void testAshura2025MatchesBH() {
        HolidayCalendar bhCalendar = new HolidayCalendarServiceBH().getHolidayCalendar();
        Optional<HolidayDate> bhdAshura = findFirst("Ashura", 2025);
        Optional<HolidayDate> bhAshura  = bhCalendar.calculate(2025).stream()
                .filter(hd -> "Ashura".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(bhdAshura.isPresent());
        assertTrue(bhAshura.isPresent());
        assertEquals(bhdAshura.get().date(), bhAshura.get().date(),
                "BHD Ashura 2025 must match BH (same CSV source)");
    }

    // ── dataValidThrough ──────────────────────────────────────────────────────

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "BHD calendar has CSV-backed holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("BHD");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"BHD\") must delegate to the service");
    }

    // ── CSV boundary behaviour ────────────────────────────────────────────────

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundary);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundary + ") must return holidays — within covered range");
        assertEquals(holidays.size(), BHD_HOLIDAY_COUNT,
                "Expected all " + BHD_HOLIDAY_COUNT + " BHD holidays for boundary year " + boundary);
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
        assertEquals(holidays2056.size(), BHD_FIXED_HOLIDAY_COUNT,
                "Beyond CSV ceiling only the " + BHD_FIXED_HOLIDAY_COUNT +
                " fixed BHD holidays must remain");
    }

    // ── holiday name registry ─────────────────────────────────────────────────

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"New Year's Day"},
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Eid al-Fitr (3rd Day)"},
            new Object[]{"Labour Day"},
            new Object[]{"Arafat Day"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Eid al-Adha (3rd Day)"},
            new Object[]{"Islamic New Year"},
            new Object[]{"Ashura"},
            new Object[]{"Ashura (2nd Day)"},
            new Object[]{"Prophet's Birthday"},
            new Object[]{"National Day"},
            new Object[]{"Accession Day"}
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
