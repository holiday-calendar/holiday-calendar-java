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

public class HolidayCalendarServiceEGPTest {

    // EG holidays (17) minus Arafat Day = 16
    private static final int EGP_HOLIDAY_COUNT = 16;

    // Beyond CSV ceiling: 7 fixed Gregorian + Sham El-Nessim (algorithm-based) = 8
    private static final int EGP_FIXED_HOLIDAY_COUNT = 8;

    private final HolidayCalendarServiceEGP service = new HolidayCalendarServiceEGP();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // ── service identity ──────────────────────────────────────────────────────

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("EGP"));
        assertFalse(service.isProvided("EG"));
        assertFalse(service.isProvided("KWD"));
        assertFalse(service.isProvided("SAR"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "EGP");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Egypt (EGX/CBE) Holidays");
    }

    // ── factory integration ───────────────────────────────────────────────────

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("EGP");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "EGP");
    }

    // ── weekend configuration ─────────────────────────────────────────────────

    @Test
    public void testWeekendDaysFridayAndSaturday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2, "Egypt weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY), "Friday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY), "Saturday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY), "Sunday must not be a weekend day in Egypt");
    }

    // ── total count ───────────────────────────────────────────────────────────

    @Test
    public void testCalculate2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        assertNotNull(holidays);
        assertEquals(holidays.size(), EGP_HOLIDAY_COUNT,
                "Expected " + EGP_HOLIDAY_COUNT + " holidays for 2024, got: " + holidays.size());
    }

    @Test
    public void testCalculate2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), EGP_HOLIDAY_COUNT,
                "Expected " + EGP_HOLIDAY_COUNT + " holidays for 2025, got: " + holidays.size());
    }

    // ── chronological order ───────────────────────────────────────────────────

    @Test
    public void testChronologicalOrder2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).date().isBefore(holidays.get(i - 1).date()),
                    "Holidays must be in chronological order");
        }
    }

    // ── no-roll convention — fixed holidays stay on their natural dates ────────

    // Jan 7, 2028 is Friday → EGP must NOT roll (stays Jan 7)
    @Test
    public void testCopticChristmasOnFridayNotRolled2028() {
        assertEquals(LocalDate.of(2028, Month.JANUARY, 7).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> cc = findFirst("Coptic Christmas", 2028);
        assertTrue(cc.isPresent());
        assertEquals(cc.get().date(), LocalDate.of(2028, Month.JANUARY, 7),
                "EGP must not roll Coptic Christmas 2028 (Friday) — CBE uses no-roll convention");
    }

    // May 1, 2026 is Friday → EGP must NOT roll (stays May 1)
    @Test
    public void testLabourDayOnFridayNotRolled2026() {
        assertEquals(LocalDate.of(2026, Month.MAY, 1).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ld = findFirst("Labour Day", 2026);
        assertTrue(ld.isPresent());
        assertEquals(ld.get().date(), LocalDate.of(2026, Month.MAY, 1),
                "EGP must not roll Labour Day 2026 (Friday) — CBE uses no-roll convention");
    }

    // Oct 6, 2029 is Saturday → EGP must NOT roll (stays Oct 6)
    @Test
    public void testArmedForcesDayOnSaturdayNotRolled2029() {
        assertEquals(LocalDate.of(2029, Month.OCTOBER, 6).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> afd = findFirst("Armed Forces Day", 2029);
        assertTrue(afd.isPresent());
        assertEquals(afd.get().date(), LocalDate.of(2029, Month.OCTOBER, 6),
                "EGP must not roll Armed Forces Day 2029 (Saturday) — CBE uses no-roll convention");
    }

    // Jul 23, 2027 is Friday → EGP must NOT roll (stays Jul 23)
    @Test
    public void testRevolutionDayJulyOnFridayNotRolled2027() {
        assertEquals(LocalDate.of(2027, Month.JULY, 23).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> rd2 = findFirst("Revolution Day (July 23)", 2027);
        assertTrue(rd2.isPresent());
        assertEquals(rd2.get().date(), LocalDate.of(2027, Month.JULY, 23),
                "EGP must not roll Revolution Day (July 23) 2027 (Friday) — CBE uses no-roll convention");
    }

    // ── Arafat Day must be absent from EGP ───────────────────────────────────

    @Test
    public void testArafatDayAbsent() {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> "Arafat Day".equals(h.getName()));
        assertFalse(found, "Arafat Day is a national holiday only; it must not appear in the EGP settlement calendar");
    }

    @Test
    public void testArafatDayAbsent2024() {
        Optional<HolidayDate> ad = findFirst("Arafat Day", 2024);
        assertFalse(ad.isPresent(), "Arafat Day must not be present in EGP 2024");
    }

    // ── Islamic holiday spot-checks (same CSV as EG) ──────────────────────────

    @Test
    public void testEidAlFitr2024() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2024);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2024, Month.APRIL, 10),
                "EGP Eid al-Fitr 2024 must match CBE official date");
    }

    @Test
    public void testEidAlAdha2024() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2024);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2024, Month.JUNE, 16),
                "EGP Eid al-Adha 2024 must match CBE official date");
    }

    @Test
    public void testEidAlFitr2025() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2025);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2025, Month.MARCH, 30),
                "EGP Eid al-Fitr 2025 must match CBE official date");
    }

    @Test
    public void testEidAlAdha2025() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2025);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2025, Month.JUNE, 6),
                "EGP Eid al-Adha 2025 must match CBE official date");
    }

    // ── EGP Islamic dates match EG ────────────────────────────────────────────

    @Test
    public void testEgpIslamicDatesMatchEg() {
        HolidayCalendarServiceEG egService = new HolidayCalendarServiceEG();
        for (String name : new String[]{"Eid al-Fitr", "Eid al-Adha", "Islamic New Year", "Prophet's Birthday"}) {
            for (int year : new int[]{2024, 2025}) {
                Optional<HolidayDate> egp = findFirst(name, year);
                Optional<HolidayDate> eg  = egService.getHolidayCalendar().calculate(year).stream()
                        .filter(hd -> name.equals(hd.holiday().getName()))
                        .findFirst();
                assertTrue(egp.isPresent(), year + " EGP must contain " + name);
                assertTrue(eg.isPresent(),  year + " EG must contain " + name);
                assertEquals(egp.get().date(), eg.get().date(),
                        year + " EGP and EG must share the same " + name + " date");
            }
        }
    }

    // ── Sham El-Nessim spot-check ─────────────────────────────────────────────

    @Test
    public void testShamElNessim2024() {
        Optional<HolidayDate> sen = findFirst("Sham El-Nessim", 2024);
        assertTrue(sen.isPresent());
        assertEquals(sen.get().date(), LocalDate.of(2024, Month.MAY, 6),
                "EGP Sham El-Nessim 2024 must be May 6 (day after Coptic Easter May 5)");
    }

    // ── dataValidThrough ──────────────────────────────────────────────────────

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "EGP calendar has CSV-backed holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("EGP");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"EGP\") must delegate to the service");
    }

    // ── CSV boundary behaviour ────────────────────────────────────────────────

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundary);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundary + ") must return holidays — within covered range");
        assertEquals(holidays.size(), EGP_HOLIDAY_COUNT,
                "Expected all " + EGP_HOLIDAY_COUNT + " EGP holidays for boundary year " + boundary);
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
    public void testFixedAndCopticHolidaysPresentBeyondCeiling() {
        List<HolidayDate> holidays2056 = service.getHolidayCalendar().calculate(2056);
        assertEquals(holidays2056.size(), EGP_FIXED_HOLIDAY_COUNT,
                "Beyond CSV ceiling only the " + EGP_FIXED_HOLIDAY_COUNT +
                " fixed and algorithmic EGP holidays must remain (7 Gregorian + Sham El-Nessim)");
    }

    // ── holiday name registry ─────────────────────────────────────────────────

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"Coptic Christmas"},
            new Object[]{"Revolution Day"},
            new Object[]{"Sinai Liberation Day"},
            new Object[]{"Sham El-Nessim"},
            new Object[]{"Labour Day"},
            new Object[]{"June 30 Revolution"},
            new Object[]{"Revolution Day (July 23)"},
            new Object[]{"Armed Forces Day"},
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Eid al-Fitr (3rd Day)"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Eid al-Adha (3rd Day)"},
            new Object[]{"Islamic New Year"},
            new Object[]{"Prophet's Birthday"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName()));
        assertTrue(found, "EGP calendar must contain holiday: " + holidayName);
    }

    // ── helper ────────────────────────────────────────────────────────────────

    private Optional<HolidayDate> findFirst(String name, int year) {
        return service.getHolidayCalendar().calculate(year).stream()
                .filter(hd -> name.equals(hd.holiday().getName()))
                .findFirst();
    }

}
