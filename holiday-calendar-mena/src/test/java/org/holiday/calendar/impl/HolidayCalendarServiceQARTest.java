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

public class HolidayCalendarServiceQARTest {

    // 9 national holidays shared with QA + Qatar Banks Holiday (first Sunday of March) = 10
    private static final int QAR_HOLIDAY_COUNT = 10;

    private final HolidayCalendarServiceQAR service = new HolidayCalendarServiceQAR();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // ── service identity ──────────────────────────────────────────────────────

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("QAR"));
        assertFalse(service.isProvided("QA"));
        assertFalse(service.isProvided("SAR"));
        assertFalse(service.isProvided("USD"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "QAR");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Qatar (QSE/QCB) Holidays");
    }

    // ── factory integration ───────────────────────────────────────────────────

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("QAR");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "QAR");
    }

    // ── weekend configuration ─────────────────────────────────────────────────

    @Test
    public void testWeekendDaysFridayAndSaturday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2, "Qatar weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY), "Friday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY), "Saturday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY), "Sunday must not be a weekend day");
    }

    // ── total count ───────────────────────────────────────────────────────────

    @Test
    public void testCalculate2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        assertNotNull(holidays);
        assertEquals(holidays.size(), QAR_HOLIDAY_COUNT,
                "Expected " + QAR_HOLIDAY_COUNT + " holidays for 2024, got: " + holidays.size());
    }

    @Test
    public void testCalculate2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), QAR_HOLIDAY_COUNT,
                "Expected " + QAR_HOLIDAY_COUNT + " holidays for 2025, got: " + holidays.size());
    }

    // ── no-roll: National Day stays on natural date ───────────────────────────

    // Dec 18, 2026 is Friday — QAR must NOT roll (noRoll convention)
    @Test
    public void testNationalDayOnFridayNotRolled2026() {
        assertEquals(LocalDate.of(2026, Month.DECEMBER, 18).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> nd = findFirst("National Day", 2026);
        assertTrue(nd.isPresent());
        assertEquals(nd.get().date(), LocalDate.of(2026, Month.DECEMBER, 18),
                "QAR must not roll National Day 2026 (Friday) — settlement uses no-roll convention");
    }

    // Dec 18, 2027 is Saturday — QAR must NOT roll
    @Test
    public void testNationalDayOnSaturdayNotRolled2027() {
        assertEquals(LocalDate.of(2027, Month.DECEMBER, 18).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> nd = findFirst("National Day", 2027);
        assertTrue(nd.isPresent());
        assertEquals(nd.get().date(), LocalDate.of(2027, Month.DECEMBER, 18),
                "QAR must not roll National Day 2027 (Saturday) — settlement uses no-roll convention");
    }

    // ── no-roll: New Year's Day stays on natural date ─────────────────────────

    // Jan 1, 2027 is Friday — QAR must NOT roll
    @Test
    public void testNewYearsDayOnFridayNotRolled2027() {
        assertEquals(LocalDate.of(2027, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2027);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2027, Month.JANUARY, 1),
                "QAR must not roll New Year's Day 2027 (Friday) — settlement uses no-roll convention");
    }

    // ── Qatar Banks Holiday (first Sunday of March, QAR-only) ────────────────

    // Mar 3, 2024 is the first Sunday of March — confirmed QCB announcement
    @Test
    public void testQatarBanksHoliday2024() {
        assertEquals(LocalDate.of(2024, Month.MARCH, 3).getDayOfWeek(), DayOfWeek.SUNDAY);
        Optional<HolidayDate> bh = findFirst("Qatar Banks Holiday", 2024);
        assertTrue(bh.isPresent());
        assertEquals(bh.get().date(), LocalDate.of(2024, Month.MARCH, 3),
                "Qatar Banks Holiday 2024 must be 2024-03-03 per QCB announcement");
    }

    @Test
    public void testQatarBanksHoliday2025() {
        assertEquals(LocalDate.of(2025, Month.MARCH, 2).getDayOfWeek(), DayOfWeek.SUNDAY);
        Optional<HolidayDate> bh = findFirst("Qatar Banks Holiday", 2025);
        assertTrue(bh.isPresent());
        assertEquals(bh.get().date(), LocalDate.of(2025, Month.MARCH, 2),
                "Qatar Banks Holiday 2025 must be 2025-03-02");
    }

    @Test
    public void testQatarBanksHolidayNotInQA() {
        boolean inQA = new HolidayCalendarServiceQA().getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> "Qatar Banks Holiday".equals(h.getName()));
        assertFalse(inQA, "Qatar Banks Holiday is QAR-only — not a national public holiday");
    }

    // ── Islamic dates match QA (same CSV source) ──────────────────────────────

    @Test
    public void testEidAlFitr2025MatchesQA() {
        HolidayCalendar qaCalendar  = new HolidayCalendarServiceQA().getHolidayCalendar();
        Optional<HolidayDate> qarEid = findFirst("Eid al-Fitr", 2025);
        Optional<HolidayDate> qaEid  = qaCalendar.calculate(2025).stream()
                .filter(hd -> "Eid al-Fitr".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(qarEid.isPresent());
        assertTrue(qaEid.isPresent());
        assertEquals(qarEid.get().date(), qaEid.get().date(),
                "QAR Eid al-Fitr 2025 must match QA (same CSV source)");
    }

    @Test
    public void testEidAlAdha2025MatchesQA() {
        HolidayCalendar qaCalendar  = new HolidayCalendarServiceQA().getHolidayCalendar();
        Optional<HolidayDate> qarAdha = findFirst("Eid al-Adha", 2025);
        Optional<HolidayDate> qaAdha  = qaCalendar.calculate(2025).stream()
                .filter(hd -> "Eid al-Adha".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(qarAdha.isPresent());
        assertTrue(qaAdha.isPresent());
        assertEquals(qarAdha.get().date(), qaAdha.get().date(),
                "QAR Eid al-Adha 2025 must match QA (same CSV source)");
    }

    // ── dataValidThrough ──────────────────────────────────────────────────────

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "QAR calendar has CSV-backed holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("QAR");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"QAR\") must delegate to the service");
    }

    // ── CSV boundary behaviour ────────────────────────────────────────────────

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundary);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundary + ") must return holidays — within covered range");
        assertEquals(holidays.size(), QAR_HOLIDAY_COUNT,
                "Expected all " + QAR_HOLIDAY_COUNT + " QAR holidays for boundary year " + boundary);
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
        // New Year's Day, Sports Day, National Day, and Qatar Banks Holiday are all
        // algorithmic/fixed — they remain after the Eid CSV data is exhausted
        List<HolidayDate> holidays2056 = service.getHolidayCalendar().calculate(2056);
        assertEquals(holidays2056.size(), 4,
                "Beyond CSV ceiling the 4 fixed/computed QAR holidays must remain");
    }

    // ── holiday name registry ─────────────────────────────────────────────────

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"New Year's Day"},
            new Object[]{"Qatar National Sports Day"},
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Eid al-Fitr (3rd Day)"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Eid al-Adha (3rd Day)"},
            new Object[]{"National Day"},
            new Object[]{"Qatar Banks Holiday"}
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
