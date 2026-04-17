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

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.testng.Assert.*;

public class HolidayCalendarServiceCNYTest {

    private final HolidayCalendarServiceCNY service = new HolidayCalendarServiceCNY();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("CNY"));
        assertFalse(service.isProvided("SG"));
        assertFalse(service.isProvided("US"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "CNY");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "China (PBOC) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("CNY");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "CNY");
    }

    @Test
    public void testCalculate2024HasAllHolidays() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);
        assertNotNull(holidays);
        assertEquals(holidays.size(), 21, "Expected 21 holiday entries for 2024");
    }

    @Test
    public void testChronologicalOrder() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).getDate().isBefore(holidays.get(i - 1).getDate()), "Holidays must be in chronological order");
        }
    }

    // --- Spring Festival ---

    @DataProvider
    Iterator<Object[]> springFestivalDays2024() {
        // CNY 2024 begins Feb 10; Days 1-7 are Feb 10-16
        return Arrays.asList(
            new Object[]{"Spring Festival (Day 1)", LocalDate.of(2024, Month.FEBRUARY, 10)},
            new Object[]{"Spring Festival (Day 2)", LocalDate.of(2024, Month.FEBRUARY, 11)},
            new Object[]{"Spring Festival (Day 3)", LocalDate.of(2024, Month.FEBRUARY, 12)},
            new Object[]{"Spring Festival (Day 4)", LocalDate.of(2024, Month.FEBRUARY, 13)},
            new Object[]{"Spring Festival (Day 5)", LocalDate.of(2024, Month.FEBRUARY, 14)},
            new Object[]{"Spring Festival (Day 6)", LocalDate.of(2024, Month.FEBRUARY, 15)},
            new Object[]{"Spring Festival (Day 7)", LocalDate.of(2024, Month.FEBRUARY, 16)}
        ).iterator();
    }

    @Test(dataProvider = "springFestivalDays2024")
    public void testSpringFestivalDays2024(String holidayName, LocalDate expectedDate) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);
        Optional<HolidayDate> found = holidays.stream()
                .filter(hd -> holidayName.equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(found.isPresent(), holidayName + " should be present for 2024");
        assertEquals(found.get().getDate(), expectedDate, holidayName + " date mismatch for 2024");
    }

    @DataProvider
    Iterator<Object[]> springFestivalDays2025() {
        // CNY 2025 begins Jan 29; Days 1-7 are Jan 29 - Feb 4
        return Arrays.asList(
            new Object[]{"Spring Festival (Day 1)", LocalDate.of(2025, Month.JANUARY, 29)},
            new Object[]{"Spring Festival (Day 7)", LocalDate.of(2025, Month.FEBRUARY, 4)}
        ).iterator();
    }

    @Test(dataProvider = "springFestivalDays2025")
    public void testSpringFestivalDays2025(String holidayName, LocalDate expectedDate) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);
        Optional<HolidayDate> found = holidays.stream()
                .filter(hd -> holidayName.equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(found.isPresent(), holidayName + " should be present for 2025");
        assertEquals(found.get().getDate(), expectedDate, holidayName + " date mismatch for 2025");
    }

    // --- Floating lunisolar holidays ---

    @DataProvider
    Iterator<Object[]> floatingHolidayDates() {
        return Arrays.asList(
            new Object[]{"Qingming Festival",   2023, LocalDate.of(2023, Month.APRIL,     5)},
            new Object[]{"Qingming Festival",   2024, LocalDate.of(2024, Month.APRIL,     4)},
            new Object[]{"Qingming Festival",   2025, LocalDate.of(2025, Month.APRIL,     4)},
            new Object[]{"Dragon Boat Festival", 2023, LocalDate.of(2023, Month.JUNE,     22)},
            new Object[]{"Dragon Boat Festival", 2024, LocalDate.of(2024, Month.JUNE,     10)},
            new Object[]{"Dragon Boat Festival", 2025, LocalDate.of(2025, Month.MAY,      31)},
            new Object[]{"Mid-Autumn Festival", 2023, LocalDate.of(2023, Month.SEPTEMBER, 29)},
            new Object[]{"Mid-Autumn Festival", 2024, LocalDate.of(2024, Month.SEPTEMBER, 17)},
            new Object[]{"Mid-Autumn Festival", 2025, LocalDate.of(2025, Month.OCTOBER,    6)}
        ).iterator();
    }

    @Test(dataProvider = "floatingHolidayDates")
    public void testFloatingHolidayDate(String holidayName, int year, LocalDate expectedDate) {
        assertHolidayDate(service.getHolidayCalendar().calculate(year), holidayName, expectedDate);
    }

    // --- Labour Day ---

    @Test
    public void testLabourDay2024() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);

        assertHolidayDate(holidays, "Labour Day (Day 1)", LocalDate.of(2024, Month.MAY, 1));
        assertHolidayDate(holidays, "Labour Day (Day 2)", LocalDate.of(2024, Month.MAY, 2));
        assertHolidayDate(holidays, "Labour Day (Day 3)", LocalDate.of(2024, Month.MAY, 3));
    }

    // --- National Day Golden Week ---

    @Test
    public void testNationalDayGoldenWeek2024() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);
        for (int day = 1; day <= 7; day++) {
            String name = "National Day (Day " + day + ")";
            assertHolidayDate(holidays, name, LocalDate.of(2024, Month.OCTOBER, day));
        }
    }

    // --- No date rolling ---

    @Test
    public void testNewYearsDay2023NoRoll() {
        // Jan 1 2023 is Sunday — should NOT roll since CNY calendar uses noRoll
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2023);
        assertHolidayDate(holidays, "New Year's Day", LocalDate.of(2023, Month.JANUARY, 1));
    }

    // --- Compensatory working days ---

    @Test
    public void testCompensatoryWorkingDays2024() {
        List<LocalDate> makeUpDays = service.getCompensatoryWorkingDays(2024);
        assertFalse(makeUpDays.isEmpty(), "2024 compensatory working days should be available");
        assertTrue(makeUpDays.contains(LocalDate.of(2024, 2, 4)),
                "Feb 4 2024 (Spring Festival bridge) should be a make-up working day");
        assertTrue(makeUpDays.contains(LocalDate.of(2024, 9, 29)),
                "Sep 29 2024 (National Day bridge) should be a make-up working day");
    }

    @Test
    public void testCompensatoryWorkingDaysFutureYearReturnsEmpty() {
        // Year well beyond the lookup table — should return empty and log a warning
        List<LocalDate> makeUpDays = service.getCompensatoryWorkingDays(2099);
        assertNotNull(makeUpDays);
        assertTrue(makeUpDays.isEmpty(),
                "Compensatory working days for an unpublished future year should be empty");
    }

    @Test
    public void testCompensatoryWorkingDaysAreUnmodifiable() {
        List<LocalDate> makeUpDays = service.getCompensatoryWorkingDays(2024);
        assertThrows(UnsupportedOperationException.class, () -> makeUpDays.add(LocalDate.of(2024, 1, 1)));
    }

    // --- Expected holiday names present in the calendar definition ---

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"New Year's Day"},
            new Object[]{"Spring Festival (Day 1)"},
            new Object[]{"Spring Festival (Day 7)"},
            new Object[]{"Qingming Festival"},
            new Object[]{"Labour Day (Day 1)"},
            new Object[]{"Labour Day (Day 3)"},
            new Object[]{"Dragon Boat Festival"},
            new Object[]{"Mid-Autumn Festival"},
            new Object[]{"National Day (Day 1)"},
            new Object[]{"National Day (Day 7)"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        boolean found = calendar.getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName()));
        assertTrue(found, "Calendar should contain holiday: " + holidayName);
    }

    // --- parseLine error paths ---

    @Test
    public void testParseLineInvalidYearIsSkipped() {
        Map<Integer, List<LocalDate>> result = new HashMap<>();
        String[] parts = {"not-a-year", "2024-02-04"};
        HolidayCalendarServiceCNY.parseLine(parts, 1, "not-a-year,2024-02-04", result);
        assertTrue(result.isEmpty(), "Malformed year should be skipped");
    }

    @Test
    public void testParseLineInvalidDateIsSkipped() {
        Map<Integer, List<LocalDate>> result = new HashMap<>();
        String[] parts = {"2024", "not-a-date"};
        HolidayCalendarServiceCNY.parseLine(parts, 1, "2024,not-a-date", result);
        assertTrue(result.isEmpty(), "Malformed date should be skipped");
    }

    @Test
    public void testParseLineValidEntry() {
        Map<Integer, List<LocalDate>> result = new HashMap<>();
        String[] parts = {"2024", "2024-02-04", "Spring Festival bridge"};
        HolidayCalendarServiceCNY.parseLine(parts, 1, "2024,2024-02-04,Spring Festival bridge", result);
        assertEquals(result.get(2024), List.of(LocalDate.of(2024, 2, 4)));
    }

    // --- Helper ---

    private static void assertHolidayDate(List<HolidayDate> holidays, String name, LocalDate expected) {
        Optional<HolidayDate> found = holidays.stream()
                .filter(hd -> name.equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(found.isPresent(), name + " should be present");
        assertEquals(found.get().getDate(), expected, name + " date mismatch");
    }

}
