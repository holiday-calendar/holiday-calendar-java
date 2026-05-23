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

public class HolidayCalendarServiceQATest {

    // 2 fixed (New Year's, National Day) + 1 floating Gregorian (Sports Day)
    // + 3 Eid al-Fitr + 3 Eid al-Adha = 9
    private static final int QA_HOLIDAY_COUNT = 9;

    private final HolidayCalendarServiceQA service = new HolidayCalendarServiceQA();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // ── service identity ──────────────────────────────────────────────────────

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("QA"));
        assertFalse(service.isProvided("QAR"));
        assertFalse(service.isProvided("SA"));
        assertFalse(service.isProvided("US"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "QA");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Qatar (National) Holidays");
    }

    // ── factory integration ───────────────────────────────────────────────────

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("QA");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "QA");
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
        assertEquals(holidays.size(), QA_HOLIDAY_COUNT,
                "Expected " + QA_HOLIDAY_COUNT + " holidays for 2024, got: " + holidays.size());
    }

    @Test
    public void testCalculate2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), QA_HOLIDAY_COUNT,
                "Expected " + QA_HOLIDAY_COUNT + " holidays for 2025, got: " + holidays.size());
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

    @Test
    public void testChronologicalOrder2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).date().isBefore(holidays.get(i - 1).date()),
                    "Holidays must be in chronological order");
        }
    }

    // ── Sports Day (second Tuesday of February) ───────────────────────────────

    @Test
    public void testSportsDay2024() {
        assertEquals(LocalDate.of(2024, Month.FEBRUARY, 13).getDayOfWeek(), DayOfWeek.TUESDAY);
        Optional<HolidayDate> sd = findFirst("Qatar National Sports Day", 2024);
        assertTrue(sd.isPresent());
        assertEquals(sd.get().date(), LocalDate.of(2024, Month.FEBRUARY, 13),
                "Sports Day 2024 must be 2024-02-13");
    }

    @Test
    public void testSportsDay2025() {
        assertEquals(LocalDate.of(2025, Month.FEBRUARY, 11).getDayOfWeek(), DayOfWeek.TUESDAY);
        Optional<HolidayDate> sd = findFirst("Qatar National Sports Day", 2025);
        assertTrue(sd.isPresent());
        assertEquals(sd.get().date(), LocalDate.of(2025, Month.FEBRUARY, 11),
                "Sports Day 2025 must be 2025-02-11");
    }

    // ── National Day (Dec 18) — no roll when weekday ──────────────────────────

    // Dec 18, 2024 is Wednesday — must not roll
    @Test
    public void testNationalDay2024NoRoll() {
        assertEquals(LocalDate.of(2024, Month.DECEMBER, 18).getDayOfWeek(), DayOfWeek.WEDNESDAY);
        Optional<HolidayDate> nd = findFirst("National Day", 2024);
        assertTrue(nd.isPresent());
        assertEquals(nd.get().date(), LocalDate.of(2024, Month.DECEMBER, 18),
                "National Day 2024 (Wednesday) must not roll");
    }

    // ── National Day — Friday rolls to preceding Thursday ─────────────────────

    // Dec 18, 2026 is Friday → rolls to Thursday Dec 17 (Amiri Diwan rule; confirmed 2020)
    @Test
    public void testNationalDayRollFromFriday2026() {
        assertEquals(LocalDate.of(2026, Month.DECEMBER, 18).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> nd = findFirst("National Day", 2026);
        assertTrue(nd.isPresent());
        assertEquals(nd.get().date(), LocalDate.of(2026, Month.DECEMBER, 17),
                "National Day 2026 (Friday) must roll to Thursday Dec 17");
    }

    // ── National Day — Saturday rolls to following Sunday ─────────────────────

    // Dec 18, 2027 is Saturday → rolls to Sunday Dec 19 (Amiri Diwan rule; confirmed 2021)
    @Test
    public void testNationalDayRollFromSaturday2027() {
        assertEquals(LocalDate.of(2027, Month.DECEMBER, 18).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> nd = findFirst("National Day", 2027);
        assertTrue(nd.isPresent());
        assertEquals(nd.get().date(), LocalDate.of(2027, Month.DECEMBER, 19),
                "National Day 2027 (Saturday) must roll to Sunday Dec 19");
    }

    // ── New Year's Day rolls ──────────────────────────────────────────────────

    // Jan 1, 2027 is Friday → rolls to Thursday Dec 31, 2026
    @Test
    public void testNewYearsDayRollFromFriday2027() {
        assertEquals(LocalDate.of(2027, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2027);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2026, Month.DECEMBER, 31),
                "New Year's Day 2027 (Friday) must roll to Thursday Dec 31, 2026");
    }

    // Jan 1, 2028 is Saturday → rolls to Sunday Jan 2
    @Test
    public void testNewYearsDayRollFromSaturday2028() {
        assertEquals(LocalDate.of(2028, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2028);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2028, Month.JANUARY, 2),
                "New Year's Day 2028 (Saturday) must roll to Sunday Jan 2");
    }

    // ── Islamic holiday spot-checks ───────────────────────────────────────────

    @Test
    public void testEidAlFitr2024() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2024);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2024, Month.APRIL, 10),
                "Eid al-Fitr 2024 must be 2024-04-10 per QCB official");
    }

    @Test
    public void testEidAlFitrDay2_2024() {
        Optional<HolidayDate> eid2 = findFirst("Eid al-Fitr (2nd Day)", 2024);
        assertTrue(eid2.isPresent());
        assertEquals(eid2.get().date(), LocalDate.of(2024, Month.APRIL, 11));
    }

    @Test
    public void testEidAlFitrDay3_2024() {
        Optional<HolidayDate> eid3 = findFirst("Eid al-Fitr (3rd Day)", 2024);
        assertTrue(eid3.isPresent());
        assertEquals(eid3.get().date(), LocalDate.of(2024, Month.APRIL, 12));
    }

    @Test
    public void testEidAlAdha2024() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2024);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2024, Month.JUNE, 16),
                "Eid al-Adha 2024 must be 2024-06-16 per QCB official");
    }

    @Test
    public void testEidAlAdhaDay2_2024() {
        Optional<HolidayDate> adha2 = findFirst("Eid al-Adha (2nd Day)", 2024);
        assertTrue(adha2.isPresent());
        assertEquals(adha2.get().date(), LocalDate.of(2024, Month.JUNE, 17));
    }

    @Test
    public void testEidAlAdhaDay3_2024() {
        Optional<HolidayDate> adha3 = findFirst("Eid al-Adha (3rd Day)", 2024);
        assertTrue(adha3.isPresent());
        assertEquals(adha3.get().date(), LocalDate.of(2024, Month.JUNE, 18));
    }

    @Test
    public void testEidAlFitr2025() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2025);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2025, Month.MARCH, 30),
                "Eid al-Fitr 2025 must be 2025-03-30 per QCB official");
    }

    @Test
    public void testEidAlAdha2025() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2025);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2025, Month.JUNE, 6),
                "Eid al-Adha 2025 must be 2025-06-06 per QCB official");
    }

    // ── Islamic holidays not included ─────────────────────────────────────────

    @Test
    public void testIslamicNewYearNotIncluded() {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> "Islamic New Year".equals(h.getName()));
        assertFalse(found, "Islamic New Year is not a gazetted Qatar public holiday");
    }

    @Test
    public void testProphetsBirthdayNotIncluded() {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> "Prophet's Birthday".equals(h.getName()));
        assertFalse(found, "Prophet's Birthday is not a gazetted Qatar public holiday");
    }

    // ── dataValidThrough ──────────────────────────────────────────────────────

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "QA calendar has CSV-backed holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("QA");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"QA\") must delegate to the service");
    }

    // ── CSV boundary behaviour ────────────────────────────────────────────────

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundary);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundary + ") must return holidays — within covered range");
        assertEquals(holidays.size(), QA_HOLIDAY_COUNT,
                "Expected all " + QA_HOLIDAY_COUNT + " QA holidays for boundary year " + boundary);
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
    public void testEidAlFitr2056AbsentSilently() {
        Optional<HolidayDate> eid = service.getHolidayCalendar().calculate(2056).stream()
                .filter(hd -> "Eid al-Fitr".equals(hd.holiday().getName()))
                .findFirst();
        assertFalse(eid.isPresent(),
                "Eid al-Fitr must be silently absent for 2056 — beyond the table ceiling");
    }

    @Test
    public void testFixedHolidaysPresentBeyondCeiling() {
        // New Year's Day, Sports Day, and National Day remain after CSV data is exhausted
        List<HolidayDate> holidays2056 = service.getHolidayCalendar().calculate(2056);
        assertEquals(holidays2056.size(), 3,
                "Beyond CSV ceiling only the 3 fixed/computed Qatar holidays must remain");
    }

    // ── 2033 dual-Eid al-Fitr ────────────────────────────────────────────────

    @Test
    public void testEidAlFitr2033OnlyJanuaryOccurrence() {
        List<HolidayDate> eidOccurrences = service.getHolidayCalendar().calculate(2033).stream()
                .filter(hd -> "Eid al-Fitr".equals(hd.holiday().getName()))
                .toList();
        assertEquals(eidOccurrences.size(), 1,
                "2033 has two Eid al-Fitr occurrences but only January is recorded in the CSV");
        assertEquals(eidOccurrences.getFirst().date(), LocalDate.of(2033, Month.JANUARY, 3),
                "The single 2033 Eid al-Fitr occurrence must be January 3");
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
            new Object[]{"National Day"}
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
