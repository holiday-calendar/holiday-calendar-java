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

public class HolidayCalendarServiceSATest {

    // 2 fixed + 3 Eid al-Fitr + 3 Eid al-Adha + Islamic New Year + Mawlid = 10
    private static final int SA_HOLIDAY_COUNT = 10;

    private final HolidayCalendarServiceSA service = new HolidayCalendarServiceSA();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("SA"));
        assertFalse(service.isProvided("SAR"));
        assertFalse(service.isProvided("AE"));
        assertFalse(service.isProvided("US"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "SA");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Saudi Arabia (National) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("SA");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "SA");
    }

    @Test
    public void testWeekendDaysFridayAndSaturday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2, "Saudi weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY), "Friday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY), "Saturday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY), "Sunday must not be a weekend day");
    }

    @Test
    public void testCalculate2024() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);
        assertNotNull(holidays);
        assertEquals(holidays.size(), SA_HOLIDAY_COUNT,
                "Expected " + SA_HOLIDAY_COUNT + " holidays for 2024, got: " + holidays.size());
    }

    @Test
    public void testCalculate2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), SA_HOLIDAY_COUNT,
                "Expected " + SA_HOLIDAY_COUNT + " holidays for 2025, got: " + holidays.size());
    }

    @Test
    public void testChronologicalOrder2024() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).date().isBefore(holidays.get(i - 1).date()),
                    "Holidays must be in chronological order");
        }
    }

    @Test
    public void testChronologicalOrder2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).date().isBefore(holidays.get(i - 1).date()),
                    "Holidays must be in chronological order");
        }
    }

    // Feb 22, 2025 is Saturday → rolls to Sunday Feb 23
    @Test
    public void testFoundingDayRoll2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(LocalDate.of(2025, Month.FEBRUARY, 22).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> foundingDay = calendar.calculate(2025).stream()
                .filter(hd -> "Saudi Founding Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(foundingDay.isPresent());
        assertEquals(foundingDay.get().date(), LocalDate.of(2025, Month.FEBRUARY, 23),
                "Saudi Founding Day 2025 (Saturday) must roll to Sunday Feb 23");
    }

    // Sep 23, 2025 is Tuesday → no roll
    @Test
    public void testNationalDay2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(LocalDate.of(2025, Month.SEPTEMBER, 23).getDayOfWeek(), DayOfWeek.TUESDAY);
        Optional<HolidayDate> nationalDay = calendar.calculate(2025).stream()
                .filter(hd -> "Saudi National Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(nationalDay.isPresent());
        assertEquals(nationalDay.get().date(), LocalDate.of(2025, Month.SEPTEMBER, 23),
                "Saudi National Day 2025 (Tuesday) must not roll");
    }

    // Feb 22, 2030 is Friday → rolls to Sunday Feb 24
    @Test
    public void testFoundingDayRollFromFriday2030() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(LocalDate.of(2030, Month.FEBRUARY, 22).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> foundingDay = calendar.calculate(2030).stream()
                .filter(hd -> "Saudi Founding Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(foundingDay.isPresent());
        assertEquals(foundingDay.get().date(), LocalDate.of(2030, Month.FEBRUARY, 24),
                "Saudi Founding Day 2030 (Friday) must roll to Sunday Feb 24");
    }

    // Sep 23, 2028 is Saturday → rolls to Sunday Sep 24
    @Test
    public void testNationalDayRollFromSaturday2028() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(LocalDate.of(2028, Month.SEPTEMBER, 23).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> nationalDay = calendar.calculate(2028).stream()
                .filter(hd -> "Saudi National Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(nationalDay.isPresent());
        assertEquals(nationalDay.get().date(), LocalDate.of(2028, Month.SEPTEMBER, 24),
                "Saudi National Day 2028 (Saturday) must roll to Sunday Sep 24");
    }

    @Test
    public void testEidAlFitr2024() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2024);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2024, Month.APRIL, 10),
                "Eid al-Fitr 2024 must be 2024-04-10");
    }

    @Test
    public void testEidAlFitrDay2_2024() {
        Optional<HolidayDate> eid2 = findFirst("Eid al-Fitr (2nd Day)", 2024);
        assertTrue(eid2.isPresent());
        assertEquals(eid2.get().date(), LocalDate.of(2024, Month.APRIL, 11),
                "Eid al-Fitr (2nd Day) 2024 must be 2024-04-11");
    }

    @Test
    public void testEidAlFitrDay3_2024() {
        Optional<HolidayDate> eid3 = findFirst("Eid al-Fitr (3rd Day)", 2024);
        assertTrue(eid3.isPresent());
        assertEquals(eid3.get().date(), LocalDate.of(2024, Month.APRIL, 12),
                "Eid al-Fitr (3rd Day) 2024 must be 2024-04-12");
    }

    @Test
    public void testEidAlAdha2024() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2024);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2024, Month.JUNE, 16),
                "Eid al-Adha 2024 must be 2024-06-16");
    }

    @Test
    public void testEidAlAdhaDay2_2024() {
        Optional<HolidayDate> adha2 = findFirst("Eid al-Adha (2nd Day)", 2024);
        assertTrue(adha2.isPresent());
        assertEquals(adha2.get().date(), LocalDate.of(2024, Month.JUNE, 17),
                "Eid al-Adha (2nd Day) 2024 must be 2024-06-17");
    }

    @Test
    public void testEidAlAdhaDay3_2024() {
        Optional<HolidayDate> adha3 = findFirst("Eid al-Adha (3rd Day)", 2024);
        assertTrue(adha3.isPresent());
        assertEquals(adha3.get().date(), LocalDate.of(2024, Month.JUNE, 18),
                "Eid al-Adha (3rd Day) 2024 must be 2024-06-18");
    }

    @Test
    public void testIslamicNewYear2024() {
        Optional<HolidayDate> ny = findFirst("Islamic New Year", 2024);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2024, Month.JULY, 7),
                "Islamic New Year 2024 must be 2024-07-07");
    }

    @Test
    public void testProphetsBirthday2024() {
        Optional<HolidayDate> mawlid = findFirst("Prophet's Birthday", 2024);
        assertTrue(mawlid.isPresent());
        assertEquals(mawlid.get().date(), LocalDate.of(2024, Month.SEPTEMBER, 15),
                "Prophet's Birthday 2024 must be 2024-09-15");
    }

    @Test
    public void testEidAlFitr2025() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2025);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2025, Month.MARCH, 30),
                "Eid al-Fitr 2025 must be 2025-03-30");
    }

    @Test
    public void testEidAlFitrDay2_2025() {
        Optional<HolidayDate> eid2 = findFirst("Eid al-Fitr (2nd Day)", 2025);
        assertTrue(eid2.isPresent());
        assertEquals(eid2.get().date(), LocalDate.of(2025, Month.MARCH, 31),
                "Eid al-Fitr (2nd Day) 2025 must be 2025-03-31");
    }

    @Test
    public void testEidAlFitrDay3_2025() {
        Optional<HolidayDate> eid3 = findFirst("Eid al-Fitr (3rd Day)", 2025);
        assertTrue(eid3.isPresent());
        assertEquals(eid3.get().date(), LocalDate.of(2025, Month.APRIL, 1),
                "Eid al-Fitr (3rd Day) 2025 must be 2025-04-01");
    }

    @Test
    public void testEidAlAdha2025() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2025);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2025, Month.JUNE, 6),
                "Eid al-Adha 2025 must be 2025-06-06 per Tadawul official announcement");
    }

    @Test
    public void testEidAlAdhaDay2_2025() {
        Optional<HolidayDate> adha2 = findFirst("Eid al-Adha (2nd Day)", 2025);
        assertTrue(adha2.isPresent());
        assertEquals(adha2.get().date(), LocalDate.of(2025, Month.JUNE, 7),
                "Eid al-Adha (2nd Day) 2025 must be 2025-06-07");
    }

    @Test
    public void testEidAlAdhaDay3_2025() {
        Optional<HolidayDate> adha3 = findFirst("Eid al-Adha (3rd Day)", 2025);
        assertTrue(adha3.isPresent());
        assertEquals(adha3.get().date(), LocalDate.of(2025, Month.JUNE, 8),
                "Eid al-Adha (3rd Day) 2025 must be 2025-06-08");
    }

    @Test
    public void testDataValidThroughReturnsPresent() {
        OptionalInt result = service.dataValidThrough();
        assertTrue(result.isPresent(),
                "SA calendar has lookup-table holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("SA");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"SA\") must delegate to the service");
    }

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundaryYear = service.dataValidThrough().orElseThrow();
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(boundaryYear);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundaryYear + ") must return holidays — it is within the covered range");
        assertEquals(holidays.size(), SA_HOLIDAY_COUNT,
                "Expected all " + SA_HOLIDAY_COUNT + " SA holidays for boundary year " + boundaryYear);
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
        assertEquals(holidays2056.size(), 2,
                "Beyond CSV ceiling only the 2 fixed Saudi holidays must remain");
    }

    // Year 2033 has two Eid al-Fitr occurrences; only January is recorded in the CSV
    @Test
    public void testEidAlFitr2033OnlyJanuaryOccurrence() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> eidOccurrences = calendar.calculate(2033).stream()
                .filter(hd -> "Eid al-Fitr".equals(hd.holiday().getName()))
                .toList();
        assertEquals(eidOccurrences.size(), 1,
                "2033 has two Eid al-Fitr occurrences but only January is recorded in the CSV");
        assertEquals(eidOccurrences.getFirst().date(), LocalDate.of(2033, Month.JANUARY, 3),
                "The single 2033 Eid al-Fitr occurrence must be January 3");
    }

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"Saudi Founding Day"},
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Eid al-Fitr (3rd Day)"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Eid al-Adha (3rd Day)"},
            new Object[]{"Islamic New Year"},
            new Object[]{"Prophet's Birthday"},
            new Object[]{"Saudi National Day"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        boolean found = calendar.getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName()));
        assertTrue(found, "Calendar must contain holiday: " + holidayName);
    }

    private Optional<HolidayDate> findFirst(String name, int year) {
        return service.getHolidayCalendar().calculate(year).stream()
                .filter(hd -> name.equals(hd.holiday().getName()))
                .findFirst();
    }

}
