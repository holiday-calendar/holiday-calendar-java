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

public class HolidayCalendarServiceJODTest {

    // Same holiday set as JO — settlement calendar uses the national holiday set with no-roll.
    // Keep in sync with JO_HOLIDAY_COUNT in HolidayCalendarServiceJOTest.
    private static final int JOD_HOLIDAY_COUNT = 15;

    // Fixed holidays survive beyond the CSV ceiling
    private static final int JOD_FIXED_HOLIDAY_COUNT = 4;

    private final HolidayCalendarServiceJOD service = new HolidayCalendarServiceJOD();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // ── service identity ──────────────────────────────────────────────────────

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("JOD"));
        assertFalse(service.isProvided("JO"));
        assertFalse(service.isProvided("BHD"));
        assertFalse(service.isProvided("EGP"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "JOD");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Jordan (ASE/CBJ) Holidays");
    }

    // ── factory integration ───────────────────────────────────────────────────

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("JOD");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "JOD");
    }

    // ── weekend configuration ─────────────────────────────────────────────────

    @Test
    public void testWeekendDaysFridayAndSaturday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2,
                "Jordan weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY),
                "Friday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY),
                "Saturday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY),
                "Sunday must not be a weekend day in Jordan");
    }

    // ── total count (S5976-compliant parameterized test) ──────────────────────

    @DataProvider
    Iterator<Object[]> years() {
        return Arrays.asList(
            new Object[]{2024},
            new Object[]{2025},
            new Object[]{2026}
        ).iterator();
    }

    @Test(dataProvider = "years")
    public void testCalculate(int year) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
        assertNotNull(holidays);
        assertEquals(holidays.size(), JOD_HOLIDAY_COUNT,
                "Expected " + JOD_HOLIDAY_COUNT + " holidays for " + year +
                ", got: " + holidays.size());
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
    //
    // JOD uses DateRolls.noRoll(); all holidays have rollable(false).
    // The same Fri/Sat collisions used in JO roll tests are the ideal no-roll
    // verification points: JOD must return the NATURAL date, JO returns the rolled Sunday.

    // May 25, 2024 is Saturday → JOD must NOT roll; JO rolls this to Sunday May 26
    @Test
    public void testIndependenceDayOnSaturdayNotRolled2024() {
        assertEquals(LocalDate.of(2024, Month.MAY, 25).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> id = findFirst("Independence Day", 2024);
        assertTrue(id.isPresent());
        assertEquals(id.get().date(), LocalDate.of(2024, Month.MAY, 25),
                "JOD must not roll Independence Day 2024 (Saturday) — settlement uses no-roll");
    }

    // May 1, 2026 is Friday → JOD must NOT roll; JO rolls this to Sunday May 3
    @Test
    public void testLabourDayOnFridayNotRolled2026() {
        assertEquals(LocalDate.of(2026, Month.MAY, 1).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ld = findFirst("Labour Day", 2026);
        assertTrue(ld.isPresent());
        assertEquals(ld.get().date(), LocalDate.of(2026, Month.MAY, 1),
                "JOD must not roll Labour Day 2026 (Friday) — settlement uses no-roll");
    }

    // Dec 25, 2026 is Friday → JOD must NOT roll; JO rolls this to Sunday Dec 27
    @Test
    public void testChristmasDayOnFridayNotRolled2026() {
        assertEquals(LocalDate.of(2026, Month.DECEMBER, 25).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> cd = findFirst("Christmas Day", 2026);
        assertTrue(cd.isPresent());
        assertEquals(cd.get().date(), LocalDate.of(2026, Month.DECEMBER, 25),
                "JOD must not roll Christmas Day 2026 (Friday) — settlement uses no-roll");
    }

    // Jan 1, 2027 is Friday → JOD must NOT roll; JO rolls this to Sunday Jan 3
    @Test
    public void testNewYearsDayOnFridayNotRolled2027() {
        assertEquals(LocalDate.of(2027, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2027);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2027, Month.JANUARY, 1),
                "JOD must not roll New Year's Day 2027 (Friday) — settlement uses no-roll");
    }

    // Jan 1, 2028 is Saturday → JOD must NOT roll; JO rolls this to Sunday Jan 2
    @Test
    public void testNewYearsDayOnSaturdayNotRolled2028() {
        assertEquals(LocalDate.of(2028, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2028);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2028, Month.JANUARY, 1),
                "JOD must not roll New Year's Day 2028 (Saturday) — settlement uses no-roll");
    }

    // ── JOD differs from JO on rolled fixed holidays ──────────────────────────

    @Test
    public void testIndependenceDay2024DiffersBetweenJoAndJod() {
        HolidayCalendar joCalendar = new HolidayCalendarServiceJO().getHolidayCalendar();
        Optional<HolidayDate> jodDate = findFirst("Independence Day", 2024);
        Optional<HolidayDate> joDate  = joCalendar.calculate(2024).stream()
                .filter(hd -> "Independence Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(jodDate.isPresent());
        assertTrue(joDate.isPresent());
        assertNotEquals(jodDate.get().date(), joDate.get().date(),
                "JOD Independence Day 2024 must differ from JO (no-roll vs roll-to-Sunday)");
        assertEquals(jodDate.get().date(), LocalDate.of(2024, Month.MAY, 25));
        assertEquals(joDate.get().date(),  LocalDate.of(2024, Month.MAY, 26));
    }

    @Test
    public void testLabourDay2026DiffersBetweenJoAndJod() {
        HolidayCalendar joCalendar = new HolidayCalendarServiceJO().getHolidayCalendar();
        Optional<HolidayDate> jodDate = findFirst("Labour Day", 2026);
        Optional<HolidayDate> joDate  = joCalendar.calculate(2026).stream()
                .filter(hd -> "Labour Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(jodDate.isPresent());
        assertTrue(joDate.isPresent());
        assertNotEquals(jodDate.get().date(), joDate.get().date(),
                "JOD Labour Day 2026 must differ from JO (no-roll vs roll-to-Sunday)");
        assertEquals(jodDate.get().date(), LocalDate.of(2026, Month.MAY, 1));
        assertEquals(joDate.get().date(),  LocalDate.of(2026, Month.MAY, 3));
    }

    // ── Islamic dates match JO (same CSV source) ──────────────────────────────

    @Test
    public void testJodIslamicDatesMatchJo() {
        HolidayCalendarServiceJO joService = new HolidayCalendarServiceJO();
        for (String name : new String[]{
                "Eid al-Fitr", "Eid al-Fitr (2nd Day)", "Eid al-Fitr (3rd Day)", "Eid al-Fitr (4th Day)",
                "Arafat Day",
                "Eid al-Adha", "Eid al-Adha (2nd Day)", "Eid al-Adha (3rd Day)", "Eid al-Adha (4th Day)",
                "Islamic New Year", "Prophet's Birthday"}) {
            for (int year : new int[]{2024, 2025}) {
                Optional<HolidayDate> jod = findFirst(name, year);
                Optional<HolidayDate> jo  = joService.getHolidayCalendar().calculate(year).stream()
                        .filter(hd -> name.equals(hd.holiday().getName()))
                        .findFirst();
                assertTrue(jod.isPresent(), year + " JOD must contain " + name);
                assertTrue(jo.isPresent(),  year + " JO must contain " + name);
                assertEquals(jod.get().date(), jo.get().date(),
                        year + " JOD and JO must share the same " + name + " date (same CSV source)");
            }
        }
    }

    @Test
    public void testEidAlFitr2024() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2024);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2024, Month.APRIL, 9),
                "JOD Eid al-Fitr 2024 must match CBJ official date");
    }

    @Test
    public void testEidAlAdha2024() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2024);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2024, Month.JUNE, 16),
                "JOD Eid al-Adha 2024 must match CBJ official date");
    }

    // ── JOD holiday count equals JO (same holiday set, only rolling differs) ──

    @Test
    public void testJodHolidayCountEqualsJo() {
        HolidayCalendarServiceJO joService = new HolidayCalendarServiceJO();
        for (int year : new int[]{2024, 2025, 2026}) {
            int joCount  = joService.getHolidayCalendar().calculate(year).size();
            int jodCount = service.getHolidayCalendar().calculate(year).size();
            assertEquals(jodCount, joCount,
                    year + ": JOD holiday count must equal JO count");
        }
    }

    // ── dataValidThrough ──────────────────────────────────────────────────────

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "JOD calendar has CSV-backed Islamic holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("JOD");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"JOD\") must delegate to the service");
    }

    // ── CSV boundary behaviour ────────────────────────────────────────────────

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundary);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundary + ") must return holidays — within covered range");
        assertEquals(holidays.size(), JOD_HOLIDAY_COUNT,
                "Expected all " + JOD_HOLIDAY_COUNT + " JOD holidays for boundary year " + boundary);
    }

    @Test
    public void testCalculateBeyondDataValidThroughDropsIslamicHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> atBoundary     = calendar.calculate(boundary);
        List<HolidayDate> beyondBoundary = calendar.calculate(boundary + 1);
        assertTrue(beyondBoundary.size() < atBoundary.size(),
                "Year beyond dataValidThrough must produce fewer holidays (Islamic tables exhausted)");
    }

    @Test
    public void testFixedHolidaysPresentBeyondCeiling() {
        List<HolidayDate> holidays2056 = service.getHolidayCalendar().calculate(2056);
        assertEquals(holidays2056.size(), JOD_FIXED_HOLIDAY_COUNT,
                "Beyond CSV ceiling only the " + JOD_FIXED_HOLIDAY_COUNT +
                " fixed JOD holidays must remain");
    }

    // ── holiday name registry ─────────────────────────────────────────────────

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"New Year's Day"},
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Eid al-Fitr (3rd Day)"},
            new Object[]{"Eid al-Fitr (4th Day)"},
            new Object[]{"Labour Day"},
            new Object[]{"Independence Day"},
            new Object[]{"Arafat Day"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Eid al-Adha (3rd Day)"},
            new Object[]{"Eid al-Adha (4th Day)"},
            new Object[]{"Islamic New Year"},
            new Object[]{"Prophet's Birthday"},
            new Object[]{"Christmas Day"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName()));
        assertTrue(found, "JOD calendar must contain holiday: " + holidayName);
    }

    // ── helper ────────────────────────────────────────────────────────────────

    private Optional<HolidayDate> findFirst(String name, int year) {
        return service.getHolidayCalendar().calculate(year).stream()
                .filter(hd -> name.equals(hd.holiday().getName()))
                .findFirst();
    }

}
