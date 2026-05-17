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

public class HolidayCalendarServiceAETest {

    private final HolidayCalendarServiceAE service = new HolidayCalendarServiceAE();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("AE"));
        assertFalse(service.isProvided("AED"));
        assertFalse(service.isProvided("US"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "AE");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "UAE (National) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("AE");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "AE");
    }

    @Test
    public void testWeekendDaysFridayAndSaturday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2, "UAE weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY), "Friday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY), "Saturday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY), "Sunday must not be a weekend day");
    }

    @Test
    public void testCalculate2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);
        assertNotNull(holidays);
        // 4 fixed + 6 floating (all Islamic holidays cover 2025)
        assertEquals(holidays.size(), 10, "Expected 10 holidays for 2025, got: " + holidays.size());
    }

    @Test
    public void testChronologicalOrder() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).date().isBefore(holidays.get(i - 1).date()),
                    "Holidays must be in chronological order");
        }
    }

    // Issue #158 acceptance criterion: UAE 2025 must include Eid al-Fitr on 2025-03-30
    @Test
    public void testEidAlFitr2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);

        Optional<HolidayDate> eid = holidays.stream()
                .filter(hd -> "Eid al-Fitr".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(eid.isPresent(), "Eid al-Fitr must be present for 2025");
        assertEquals(eid.get().date(), LocalDate.of(2025, Month.MARCH, 30),
                "Eid al-Fitr 2025 must be 2025-03-30");
    }

    @Test
    public void testEidAlFitrDay2_2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);

        Optional<HolidayDate> eid2 = holidays.stream()
                .filter(hd -> "Eid al-Fitr (2nd Day)".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(eid2.isPresent(), "Eid al-Fitr (2nd Day) must be present for 2025");
        assertEquals(eid2.get().date(), LocalDate.of(2025, Month.MARCH, 31),
                "Eid al-Fitr (2nd Day) 2025 must be 2025-03-31");
    }

    @Test
    public void testEidAlAdha2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);

        Optional<HolidayDate> adha = holidays.stream()
                .filter(hd -> "Eid al-Adha".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(adha.isPresent(), "Eid al-Adha must be present for 2025");
        assertEquals(adha.get().date(), LocalDate.of(2025, Month.JUNE, 6),
                "Eid al-Adha 2025 must be 2025-06-06");
    }

    @Test
    public void testEidAlAdhaDay2_2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);

        Optional<HolidayDate> adha2 = holidays.stream()
                .filter(hd -> "Eid al-Adha (2nd Day)".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(adha2.isPresent(), "Eid al-Adha (2nd Day) must be present for 2025");
        assertEquals(adha2.get().date(), LocalDate.of(2025, Month.JUNE, 7),
                "Eid al-Adha (2nd Day) 2025 must be 2025-06-07");
    }

    @Test
    public void testIslamicNewYear2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);

        Optional<HolidayDate> ny = holidays.stream()
                .filter(hd -> "Islamic New Year".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(ny.isPresent(), "Islamic New Year must be present for 2025");
        assertEquals(ny.get().date(), LocalDate.of(2025, Month.JUNE, 26),
                "Islamic New Year 2025 must be 2025-06-26");
    }

    @Test
    public void testProphetsBirthday2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);

        Optional<HolidayDate> mawlid = holidays.stream()
                .filter(hd -> "Prophet's Birthday".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(mawlid.isPresent(), "Prophet's Birthday must be present for 2025");
        assertEquals(mawlid.get().date(), LocalDate.of(2025, Month.SEPTEMBER, 4),
                "Prophet's Birthday 2025 must be 2025-09-04");
    }

    // Jan 1, 2027 falls on Friday → rolls to Sunday Jan 3 under followingSunday()
    @Test
    public void testNewYearsDayRoll2027() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2027);

        Optional<HolidayDate> newYear = holidays.stream()
                .filter(hd -> "New Year's Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(newYear.isPresent());
        assertEquals(newYear.get().date(), LocalDate.of(2027, Month.JANUARY, 3),
                "New Year's Day 2027 (Friday) must roll to Sunday Jan 3");
    }

    // Jan 1, 2022 falls on Saturday → rolls to Sunday Jan 2 under followingSunday()
    @Test
    public void testNewYearsDayRoll2022() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2022);

        Optional<HolidayDate> newYear = holidays.stream()
                .filter(hd -> "New Year's Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(newYear.isPresent());
        assertEquals(newYear.get().date(), LocalDate.of(2022, Month.JANUARY, 2),
                "New Year's Day 2022 (Saturday) must roll to Sunday Jan 2");
    }

    // Jan 1, 2025 falls on Wednesday → no roll
    @Test
    public void testNewYearsDayNoRoll2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        Optional<HolidayDate> newYear = calendar.calculate(2025).stream()
                .filter(hd -> "New Year's Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(newYear.isPresent());
        assertEquals(newYear.get().date(), LocalDate.of(2025, Month.JANUARY, 1),
                "New Year's Day 2025 (Wednesday) must not roll");
    }

    @Test
    public void testDataValidThroughReturnsPresent() {
        OptionalInt result = service.dataValidThrough();
        assertTrue(result.isPresent(),
                "AE calendar has lookup-table holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("AE");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"AE\") must delegate to the service");
    }

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundaryYear = service.dataValidThrough().orElseThrow();
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(boundaryYear);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundaryYear + ") must return holidays — it is within the covered range");
        assertEquals(holidays.size(), 10,
                "Expected all 10 AE holidays for boundary year " + boundaryYear);
    }

    @Test
    public void testCalculateBeyondDataValidThroughDropsIslamicHolidays() {
        int boundaryYear = service.dataValidThrough().orElseThrow();
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> atBoundary = calendar.calculate(boundaryYear);
        List<HolidayDate> beyondBoundary = calendar.calculate(boundaryYear + 1);
        assertTrue(beyondBoundary.size() < atBoundary.size(),
                "Year beyond dataValidThrough must produce fewer holidays (Islamic tables exhausted); " +
                "at boundary: " + atBoundary.size() + ", beyond: " + beyondBoundary.size());
    }

    @Test
    public void testEidAlFitr2056AbsentSilently() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        Optional<HolidayDate> eid = calendar.calculate(2056).stream()
                .filter(hd -> "Eid al-Fitr".equals(hd.holiday().getName()))
                .findFirst();
        assertFalse(eid.isPresent(),
                "Eid al-Fitr must be silently absent for 2056 — beyond the table ceiling");
    }

    @Test
    public void testFixedHolidaysPresentBeyondCeiling() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays2056 = calendar.calculate(2056);
        // Only the 4 fixed Gregorian holidays remain
        assertEquals(holidays2056.size(), 4,
                "Beyond CSV ceiling only the 4 fixed Gregorian holidays must remain");
    }

    // Year 2033 has two Eid al-Fitr occurrences; only January is recorded in the CSV
    @Test
    public void testEidAlFitr2033OnlyJanuaryOccurrence() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2033);
        List<HolidayDate> eidOccurrences = holidays.stream()
                .filter(hd -> "Eid al-Fitr".equals(hd.holiday().getName()))
                .toList();
        assertEquals(eidOccurrences.size(), 1,
                "2033 has two Eid al-Fitr occurrences but only January is recorded");
        assertEquals(eidOccurrences.getFirst().date(), LocalDate.of(2033, Month.JANUARY, 3),
                "The single 2033 Eid al-Fitr occurrence must be January 3");
    }

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"New Year's Day"},
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Islamic New Year"},
            new Object[]{"Prophet's Birthday"},
            new Object[]{"Commemoration Day"},
            new Object[]{"National Day"},
            new Object[]{"National Day (2nd Day)"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        boolean found = calendar.getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName()));
        assertTrue(found, "Calendar must contain holiday: " + holidayName);
    }

}
