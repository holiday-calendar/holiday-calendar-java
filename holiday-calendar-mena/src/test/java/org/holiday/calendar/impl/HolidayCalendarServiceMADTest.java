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

public class HolidayCalendarServiceMADTest {

    // Same 17 holidays as MA — settlement calendar uses the full national holiday set, no-roll
    private static final int MAD_HOLIDAY_COUNT = 17;

    // Fixed holidays survive beyond the CSV ceiling
    private static final int MAD_FIXED_HOLIDAY_COUNT = 10;

    private final HolidayCalendarServiceMAD service = new HolidayCalendarServiceMAD();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // ── service identity ──────────────────────────────────────────────────────

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("MAD"));
        assertFalse(service.isProvided("MA"));
        assertFalse(service.isProvided("EGP"));
        assertFalse(service.isProvided("BHD"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "MAD");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Morocco (CSE/BAM) Holidays");
    }

    // ── factory integration ───────────────────────────────────────────────────

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("MAD");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "MAD");
    }

    // ── weekend configuration ─────────────────────────────────────────────────

    @Test
    public void testWeekendDaysSaturdayAndSunday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2,
                "Morocco weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY),
                "Saturday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY),
                "Sunday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY),
                "Friday must NOT be a weekend day — Morocco uses Sat/Sun weekend");
    }

    // ── total count ───────────────────────────────────────────────────────────

    @Test
    public void testCalculate2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        assertNotNull(holidays);
        assertEquals(holidays.size(), MAD_HOLIDAY_COUNT,
                "Expected " + MAD_HOLIDAY_COUNT + " holidays for 2024, got: " + holidays.size());
    }

    @Test
    public void testCalculate2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), MAD_HOLIDAY_COUNT,
                "Expected " + MAD_HOLIDAY_COUNT + " holidays for 2025, got: " + holidays.size());
    }

    // ── chronological order ───────────────────────────────────────────────────

    @Test
    public void testChronologicalOrder2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).date().isBefore(holidays.get(i - 1).date()),
                    "Holidays must be in chronological order");
        }
    }

    // ── no-roll: fixed holidays stay on natural dates ─────────────────────────

    // Jan 11, 2025 is Saturday — MAD must NOT roll; MA rolls this to Monday Jan 13
    @Test
    public void testManifestoDayOnSaturdayNotRolled2025() {
        assertEquals(LocalDate.of(2025, Month.JANUARY, 11).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> md = findFirst("Manifesto of Independence Day", 2025);
        assertTrue(md.isPresent());
        assertEquals(md.get().date(), LocalDate.of(2025, Month.JANUARY, 11),
                "MAD must not roll Manifesto of Independence Day 2025 (Saturday) — settlement uses no-roll convention");
    }

    // Jan 14, 2024 is Sunday — MAD must NOT roll; MA rolls this to Monday Jan 15
    @Test
    public void testAmazighNewYear2024OnSundayNotRolled() {
        assertEquals(LocalDate.of(2024, Month.JANUARY, 14).getDayOfWeek(), DayOfWeek.SUNDAY);
        Optional<HolidayDate> any = findFirst("Amazigh New Year", 2024);
        assertTrue(any.isPresent());
        assertEquals(any.get().date(), LocalDate.of(2024, Month.JANUARY, 14),
                "MAD must not roll Amazigh New Year 2024 (Sunday) — settlement uses no-roll convention");
    }

    // Jan 1, 2028 is Saturday — MAD must NOT roll; MA rolls this to Monday Jan 3
    @Test
    public void testNewYearsDay2028OnSaturdayNotRolled() {
        assertEquals(LocalDate.of(2028, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2028);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2028, Month.JANUARY, 1),
                "MAD must not roll New Year's Day 2028 (Saturday) — settlement uses no-roll convention");
    }

    // Aug 14, 2027 is Saturday — MAD must NOT roll; MA rolls this to Monday Aug 16
    @Test
    public void testOuedEdDahab2027OnSaturdayNotRolled() {
        assertEquals(LocalDate.of(2027, Month.AUGUST, 14).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> odd = findFirst("Oued Ed-Dahab Allegiance Day", 2027);
        assertTrue(odd.isPresent());
        assertEquals(odd.get().date(), LocalDate.of(2027, Month.AUGUST, 14),
                "MAD must not roll Oued Ed-Dahab Allegiance Day 2027 (Saturday)");
    }

    // Nov 18, 2028 is Saturday — MAD must NOT roll; MA rolls this to Monday Nov 20
    @Test
    public void testIndependenceDay2028OnSaturdayNotRolled() {
        assertEquals(LocalDate.of(2028, Month.NOVEMBER, 18).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> id = findFirst("Independence Day", 2028);
        assertTrue(id.isPresent());
        assertEquals(id.get().date(), LocalDate.of(2028, Month.NOVEMBER, 18),
                "MAD must not roll Independence Day 2028 (Saturday)");
    }

    // ── MAD differs from MA on fixed holidays that fall on weekends ───────────

    @Test
    public void testManifestoDayDiffersBetweenMaAndMad2025() {
        // MA rolls Jan 11 (Saturday) to Jan 13 (Monday); MAD stays Jan 11
        HolidayCalendar maCalendar = new HolidayCalendarServiceMA().getHolidayCalendar();
        Optional<HolidayDate> madMd = findFirst("Manifesto of Independence Day", 2025);
        Optional<HolidayDate> maMd  = maCalendar.calculate(2025).stream()
                .filter(hd -> "Manifesto of Independence Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(madMd.isPresent());
        assertTrue(maMd.isPresent());
        assertNotEquals(madMd.get().date(), maMd.get().date(),
                "MAD Manifesto of Independence Day 2025 must differ from MA (no-roll vs followingMonday)");
        assertEquals(madMd.get().date(), LocalDate.of(2025, Month.JANUARY, 11));
        assertEquals(maMd.get().date(), LocalDate.of(2025, Month.JANUARY, 13));
    }

    @Test
    public void testAmazighNewYearDiffersBetweenMaAndMad2024() {
        // MA rolls Jan 14, 2024 (Sunday) to Jan 15; MAD stays Jan 14
        HolidayCalendar maCalendar = new HolidayCalendarServiceMA().getHolidayCalendar();
        Optional<HolidayDate> madAny = findFirst("Amazigh New Year", 2024);
        Optional<HolidayDate> maAny  = maCalendar.calculate(2024).stream()
                .filter(hd -> "Amazigh New Year".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(madAny.isPresent());
        assertTrue(maAny.isPresent());
        assertNotEquals(madAny.get().date(), maAny.get().date(),
                "MAD Amazigh New Year 2024 must differ from MA (no-roll vs followingMonday)");
        assertEquals(madAny.get().date(), LocalDate.of(2024, Month.JANUARY, 14));
        assertEquals(maAny.get().date(), LocalDate.of(2024, Month.JANUARY, 15));
    }

    // ── KEY EDGE CASE: Islamic holidays on weekend must not roll in MAD ───────

    // Eid al-Adha 2025 = Saturday Jun 7 — must stay Jun 7
    @Test
    public void testEidAlAdha2025OnSaturdayNotRolled() {
        assertEquals(LocalDate.of(2025, Month.JUNE, 7).getDayOfWeek(), DayOfWeek.SATURDAY,
                "Pre-condition: June 7, 2025 must be a Saturday");
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2025);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2025, Month.JUNE, 7),
                "MAD Eid al-Adha 2025 must be 2025-06-07 per CSE AV-2025-078 — no-roll preserves Saturday date");
    }

    // Eid al-Adha Day 2, 2025 = Sunday Jun 8 — must stay Jun 8
    @Test
    public void testEidAlAdhaDay2_2025OnSundayNotRolled() {
        assertEquals(LocalDate.of(2025, Month.JUNE, 8).getDayOfWeek(), DayOfWeek.SUNDAY,
                "Pre-condition: June 8, 2025 must be a Sunday");
        Optional<HolidayDate> adha2 = findFirst("Eid al-Adha (2nd Day)", 2025);
        assertTrue(adha2.isPresent());
        assertEquals(adha2.get().date(), LocalDate.of(2025, Month.JUNE, 8),
                "MAD Eid al-Adha (2nd Day) 2025 must be 2025-06-08 (Sunday) — no-roll preserves date");
    }

    // Mawlid Day 2, 2025 = Saturday Sep 6 — must stay Sep 6
    @Test
    public void testProphetsBirthdayDay2_2025OnSaturdayNotRolled() {
        assertEquals(LocalDate.of(2025, Month.SEPTEMBER, 6).getDayOfWeek(), DayOfWeek.SATURDAY,
                "Pre-condition: September 6, 2025 must be a Saturday");
        Optional<HolidayDate> pb2 = findFirst("Prophet's Birthday (2nd Day)", 2025);
        assertTrue(pb2.isPresent());
        assertEquals(pb2.get().date(), LocalDate.of(2025, Month.SEPTEMBER, 6),
                "MAD Prophet's Birthday (2nd Day) 2025 must be 2025-09-06 (Saturday) — no-roll preserves date");
    }

    // ── Islamic dates match MA (same CSV source) ──────────────────────────────

    @Test
    public void testMadIslamicDatesMatchMa() {
        HolidayCalendarServiceMA maService = new HolidayCalendarServiceMA();
        for (String name : new String[]{
                "Eid al-Fitr", "Eid al-Fitr (2nd Day)",
                "Eid al-Adha", "Eid al-Adha (2nd Day)",
                "Islamic New Year",
                "Prophet's Birthday", "Prophet's Birthday (2nd Day)"}) {
            for (int year : new int[]{2024, 2025}) {
                Optional<HolidayDate> mad = findFirst(name, year);
                Optional<HolidayDate> ma  = maService.getHolidayCalendar().calculate(year).stream()
                        .filter(hd -> name.equals(hd.holiday().getName()))
                        .findFirst();
                assertTrue(mad.isPresent(), year + " MAD must contain " + name);
                assertTrue(ma.isPresent(),  year + " MA must contain " + name);
                assertEquals(mad.get().date(), ma.get().date(),
                        year + " MAD and MA must share the same " + name + " date (same CSV source, both rollable=false)");
            }
        }
    }

    // ── Amazigh New Year ──────────────────────────────────────────────────────

    @Test
    public void testAmazighNewYearIncluded() {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> "Amazigh New Year".equals(h.getName()));
        assertTrue(found, "Amazigh New Year must be included in the MAD calendar");
    }

    @Test
    public void testAmazighNewYear2025() {
        Optional<HolidayDate> any = findFirst("Amazigh New Year", 2025);
        assertTrue(any.isPresent());
        assertEquals(any.get().date(), LocalDate.of(2025, Month.JANUARY, 14),
                "MAD Amazigh New Year 2025 must be Jan 14 (Tuesday, no roll needed)");
    }

    // ── dataValidThrough ──────────────────────────────────────────────────────

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "MAD calendar has CSV-backed Islamic holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("MAD");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"MAD\") must delegate to the service");
    }

    // ── CSV boundary behaviour ────────────────────────────────────────────────

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundary);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundary + ") must return holidays — within covered range");
        assertEquals(holidays.size(), MAD_HOLIDAY_COUNT,
                "Expected all " + MAD_HOLIDAY_COUNT + " MAD holidays for boundary year " + boundary);
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
        assertEquals(holidays2056.size(), MAD_FIXED_HOLIDAY_COUNT,
                "Beyond CSV ceiling only the " + MAD_FIXED_HOLIDAY_COUNT +
                " fixed Morocco holidays must remain (10 Gregorian)");
    }

    // ── holiday name registry ─────────────────────────────────────────────────

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"New Year's Day"},
            new Object[]{"Manifesto of Independence Day"},
            new Object[]{"Amazigh New Year"},
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Labour Day"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Throne Day"},
            new Object[]{"Islamic New Year"},
            new Object[]{"Oued Ed-Dahab Allegiance Day"},
            new Object[]{"Revolution of the King and the People"},
            new Object[]{"Youth Day"},
            new Object[]{"Prophet's Birthday"},
            new Object[]{"Prophet's Birthday (2nd Day)"},
            new Object[]{"Green March Anniversary"},
            new Object[]{"Independence Day"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName()));
        assertTrue(found, "MAD calendar must contain holiday: " + holidayName);
    }

    // ── helper ────────────────────────────────────────────────────────────────

    private Optional<HolidayDate> findFirst(String name, int year) {
        return service.getHolidayCalendar().calculate(year).stream()
                .filter(hd -> name.equals(hd.holiday().getName()))
                .findFirst();
    }

}
