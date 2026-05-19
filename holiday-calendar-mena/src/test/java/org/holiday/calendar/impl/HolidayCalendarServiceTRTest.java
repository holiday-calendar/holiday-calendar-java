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

public class HolidayCalendarServiceTRTest {

    // 7 fixed + 3 Eid al-Fitr + 4 Eid al-Adha = 14 (Republic Day Eve omitted; half-day not modelled)
    private static final int TR_HOLIDAY_COUNT = 14;

    private final HolidayCalendarServiceTR service = new HolidayCalendarServiceTR();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // =========================================================================
    // Identity — SPI discovery
    // =========================================================================

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("TR"));
        assertFalse(service.isProvided("TRY"));
        assertFalse(service.isProvided("SA"));
        assertFalse(service.isProvided("US"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "TR");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Turkey (National) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("TR");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "TR");
    }

    // =========================================================================
    // Weekend configuration — Saturday + Sunday
    // =========================================================================

    @Test
    public void testWeekendDaysSaturdayAndSunday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2,
                "Turkish weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY),
                "Saturday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY),
                "Sunday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY),
                "Friday must not be a weekend day");
    }

    // =========================================================================
    // Holiday count
    // =========================================================================

    @DataProvider
    Iterator<Object[]> calculateYears() {
        return Arrays.asList(
            new Object[]{2024},
            new Object[]{2025},
            new Object[]{2026}
        ).iterator();
    }

    @Test(dataProvider = "calculateYears")
    public void testCalculate(int year) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
        assertNotNull(holidays);
        assertEquals(holidays.size(), TR_HOLIDAY_COUNT,
                "Expected " + TR_HOLIDAY_COUNT + " holidays for " + year + ", got: " + holidays.size());
    }

    // =========================================================================
    // Chronological order
    // =========================================================================

    @DataProvider
    Iterator<Object[]> chronologicalOrderYears() {
        return Arrays.asList(
            new Object[]{2024},
            new Object[]{2025},
            new Object[]{2026}
        ).iterator();
    }

    @Test(dataProvider = "chronologicalOrderYears")
    public void testChronologicalOrder(int year) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).date().isBefore(holidays.get(i - 1).date()),
                    "Holidays must be in chronological order");
        }
    }

    // =========================================================================
    // Holiday name membership
    // =========================================================================

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"New Year's Day"},
            new Object[]{"National Sovereignty and Children's Day"},
            new Object[]{"Labour and Solidarity Day"},
            new Object[]{"Commemoration of Atatürk, Youth and Sports Day"},
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Eid al-Fitr (3rd Day)"},
            new Object[]{"Democracy and National Unity Day"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Eid al-Adha (3rd Day)"},
            new Object[]{"Eid al-Adha (4th Day)"},
            new Object[]{"Victory Day"},
            new Object[]{"Republic Day"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        assertTrue(service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName())),
                "Calendar must contain holiday: " + holidayName);
    }

    // =========================================================================
    // Fixed holiday spot-checks — no roll (weekday years)
    // =========================================================================

    @Test
    public void testNewYearsDayNoRoll2025() {
        // Jan 1, 2025 = Wednesday; no roll
        assertEquals(LocalDate.of(2025, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.WEDNESDAY);
        assertEquals(findFirst("New Year's Day", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.JANUARY, 1),
                "New Year's Day 2025 (Wednesday) must not roll");
    }

    @Test
    public void testRepublicDayNoRoll2025() {
        // Oct 29, 2025 = Wednesday; no roll
        assertEquals(LocalDate.of(2025, Month.OCTOBER, 29).getDayOfWeek(), DayOfWeek.WEDNESDAY);
        assertEquals(findFirst("Republic Day", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.OCTOBER, 29),
                "Republic Day 2025 (Wednesday) must not roll");
    }

    // =========================================================================
    // Fixed holiday spot-checks — roll cases
    // =========================================================================

    @Test
    public void testAtaturkDayRollFromSunday2024() {
        // May 19, 2024 = Sunday; rolls to Monday May 20
        assertEquals(LocalDate.of(2024, Month.MAY, 19).getDayOfWeek(), DayOfWeek.SUNDAY);
        assertEquals(findFirst("Commemoration of Atatürk, Youth and Sports Day", 2024).orElseThrow().date(),
                LocalDate.of(2024, Month.MAY, 20),
                "Atatürk Day 2024 (Sunday) must roll to Monday May 20");
    }

    @Test
    public void testVictoryDayRollFromSaturday2025() {
        // Aug 30, 2025 = Saturday; rolls to Monday Sep 1
        assertEquals(LocalDate.of(2025, Month.AUGUST, 30).getDayOfWeek(), DayOfWeek.SATURDAY);
        assertEquals(findFirst("Victory Day", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.SEPTEMBER, 1),
                "Victory Day 2025 (Saturday) must roll to Monday Sep 1");
    }

    @Test
    public void testNewYearsDayNoRollOnFriday2027() {
        // Jan 1, 2027 = Friday (a workday); followingMonday() only rolls Sat/Sun, not Fri
        assertEquals(LocalDate.of(2027, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.FRIDAY);
        assertEquals(findFirst("New Year's Day", 2027).orElseThrow().date(),
                LocalDate.of(2027, Month.JANUARY, 1),
                "New Year's Day 2027 (Friday) must not roll — Friday is a workday");
    }

    @Test
    public void testNewYearsDayRollFromSaturday2028() {
        // Jan 1, 2028 = Saturday; rolls to Monday Jan 3
        assertEquals(LocalDate.of(2028, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.SATURDAY);
        assertEquals(findFirst("New Year's Day", 2028).orElseThrow().date(),
                LocalDate.of(2028, Month.JANUARY, 3),
                "New Year's Day 2028 (Saturday) must roll to Monday Jan 3");
    }

    @Test
    public void testLabourDayRollFromSaturday2027() {
        // May 1, 2027 = Saturday; rolls to Monday May 3
        assertEquals(LocalDate.of(2027, Month.MAY, 1).getDayOfWeek(), DayOfWeek.SATURDAY);
        assertEquals(findFirst("Labour and Solidarity Day", 2027).orElseThrow().date(),
                LocalDate.of(2027, Month.MAY, 3),
                "Labour Day 2027 (Saturday) must roll to Monday May 3");
    }

    @Test
    public void testRepublicDayNoRollOnFriday2027() {
        // Oct 29, 2027 = Friday (a workday); followingMonday() only rolls Sat/Sun, not Fri
        assertEquals(LocalDate.of(2027, Month.OCTOBER, 29).getDayOfWeek(), DayOfWeek.FRIDAY);
        assertEquals(findFirst("Republic Day", 2027).orElseThrow().date(),
                LocalDate.of(2027, Month.OCTOBER, 29),
                "Republic Day 2027 (Friday) must not roll — Friday is a workday");
    }

    @Test
    public void testNationalSovereigntyDayRollFromSunday2028() {
        // Apr 23, 2028 = Sunday; rolls to Monday Apr 24
        assertEquals(LocalDate.of(2028, Month.APRIL, 23).getDayOfWeek(), DayOfWeek.SUNDAY);
        assertEquals(findFirst("National Sovereignty and Children's Day", 2028).orElseThrow().date(),
                LocalDate.of(2028, Month.APRIL, 24),
                "National Sovereignty Day 2028 (Sunday) must roll to Monday Apr 24");
    }

    @Test
    public void testDemocracyDayRollFromSaturday2028() {
        // Jul 15, 2028 = Saturday; rolls to Monday Jul 17
        assertEquals(LocalDate.of(2028, Month.JULY, 15).getDayOfWeek(), DayOfWeek.SATURDAY);
        assertEquals(findFirst("Democracy and National Unity Day", 2028).orElseThrow().date(),
                LocalDate.of(2028, Month.JULY, 17),
                "Democracy Day 2028 (Saturday) must roll to Monday Jul 17");
    }

    // =========================================================================
    // Islamic spot-checks — Eid al-Fitr (Diyanet, rollable=false)
    // =========================================================================

    @Test
    public void testEidAlFitr2024() {
        assertEquals(findFirst("Eid al-Fitr", 2024).orElseThrow().date(),
                LocalDate.of(2024, Month.APRIL, 10),
                "Eid al-Fitr 2024 must be 2024-04-10 per Diyanet");
    }

    @Test
    public void testEidAlFitrDay2_2024() {
        assertEquals(findFirst("Eid al-Fitr (2nd Day)", 2024).orElseThrow().date(),
                LocalDate.of(2024, Month.APRIL, 11));
    }

    @Test
    public void testEidAlFitrDay3_2024() {
        assertEquals(findFirst("Eid al-Fitr (3rd Day)", 2024).orElseThrow().date(),
                LocalDate.of(2024, Month.APRIL, 12));
    }

    @Test
    public void testEidAlFitr2025() {
        // Mar 30, 2025 = Sunday — must not roll (rollable=false)
        assertEquals(LocalDate.of(2025, Month.MARCH, 30).getDayOfWeek(), DayOfWeek.SUNDAY);
        assertEquals(findFirst("Eid al-Fitr", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.MARCH, 30),
                "Eid al-Fitr 2025 (Sunday) must not be rolled — rollable(false)");
    }

    @Test
    public void testEidAlFitrDay2_2025() {
        assertEquals(findFirst("Eid al-Fitr (2nd Day)", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.MARCH, 31));
    }

    @Test
    public void testEidAlFitrDay3_2025() {
        assertEquals(findFirst("Eid al-Fitr (3rd Day)", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.APRIL, 1));
    }

    @Test
    public void testEidAlFitr2026() {
        assertEquals(findFirst("Eid al-Fitr", 2026).orElseThrow().date(),
                LocalDate.of(2026, Month.MARCH, 19),
                "Eid al-Fitr 2026 must be 2026-03-19 per Diyanet");
    }

    @Test
    public void testEidAlFitrDay2_2026() {
        assertEquals(findFirst("Eid al-Fitr (2nd Day)", 2026).orElseThrow().date(),
                LocalDate.of(2026, Month.MARCH, 20));
    }

    @Test
    public void testEidAlFitrDay3_2026() {
        assertEquals(findFirst("Eid al-Fitr (3rd Day)", 2026).orElseThrow().date(),
                LocalDate.of(2026, Month.MARCH, 21));
    }

    // =========================================================================
    // Islamic spot-checks — Eid al-Adha (Diyanet, 4 days, rollable=false)
    // =========================================================================

    @Test
    public void testEidAlAdha2024() {
        assertEquals(findFirst("Eid al-Adha", 2024).orElseThrow().date(),
                LocalDate.of(2024, Month.JUNE, 16),
                "Eid al-Adha 2024 must be 2024-06-16 per Diyanet");
    }

    @Test
    public void testEidAlAdhaDay2_2024() {
        assertEquals(findFirst("Eid al-Adha (2nd Day)", 2024).orElseThrow().date(),
                LocalDate.of(2024, Month.JUNE, 17));
    }

    @Test
    public void testEidAlAdhaDay3_2024() {
        assertEquals(findFirst("Eid al-Adha (3rd Day)", 2024).orElseThrow().date(),
                LocalDate.of(2024, Month.JUNE, 18));
    }

    @Test
    public void testEidAlAdhaDay4_2024() {
        assertEquals(findFirst("Eid al-Adha (4th Day)", 2024).orElseThrow().date(),
                LocalDate.of(2024, Month.JUNE, 19));
    }

    @Test
    public void testEidAlAdha2025() {
        // Turkey Diyanet = June 5; one day earlier than Saudi/UAE (June 6)
        assertEquals(findFirst("Eid al-Adha", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.JUNE, 5),
                "Eid al-Adha 2025 must be 2025-06-05 per Diyanet (one day earlier than Saudi/UAE)");
    }

    @Test
    public void testEidAlAdhaDay2_2025() {
        assertEquals(findFirst("Eid al-Adha (2nd Day)", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.JUNE, 6));
    }

    @Test
    public void testEidAlAdhaDay3_2025() {
        assertEquals(findFirst("Eid al-Adha (3rd Day)", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.JUNE, 7));
    }

    @Test
    public void testEidAlAdhaDay4_2025() {
        assertEquals(findFirst("Eid al-Adha (4th Day)", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.JUNE, 8));
    }

    @Test
    public void testEidAlAdha2026() {
        assertEquals(findFirst("Eid al-Adha", 2026).orElseThrow().date(),
                LocalDate.of(2026, Month.MAY, 26),
                "Eid al-Adha 2026 must be 2026-05-26 per Diyanet");
    }

    @Test
    public void testEidAlAdhaDay2_2026() {
        assertEquals(findFirst("Eid al-Adha (2nd Day)", 2026).orElseThrow().date(),
                LocalDate.of(2026, Month.MAY, 27));
    }

    @Test
    public void testEidAlAdhaDay3_2026() {
        assertEquals(findFirst("Eid al-Adha (3rd Day)", 2026).orElseThrow().date(),
                LocalDate.of(2026, Month.MAY, 28));
    }

    @Test
    public void testEidAlAdhaDay4_2026() {
        assertEquals(findFirst("Eid al-Adha (4th Day)", 2026).orElseThrow().date(),
                LocalDate.of(2026, Month.MAY, 29));
    }

    // =========================================================================
    // Consecutiveness invariants
    // =========================================================================

    @Test
    public void testEidAlFitrDaysAreConsecutive2025() {
        LocalDate day1 = findFirst("Eid al-Fitr", 2025).orElseThrow().date();
        LocalDate day2 = findFirst("Eid al-Fitr (2nd Day)", 2025).orElseThrow().date();
        LocalDate day3 = findFirst("Eid al-Fitr (3rd Day)", 2025).orElseThrow().date();
        assertEquals(day2, day1.plusDays(1), "Eid al-Fitr Day 2 must be the day after Day 1");
        assertEquals(day3, day1.plusDays(2), "Eid al-Fitr Day 3 must be two days after Day 1");
    }

    @Test
    public void testEidAlAdhaDaysAreConsecutive2025() {
        LocalDate day1 = findFirst("Eid al-Adha", 2025).orElseThrow().date();
        LocalDate day2 = findFirst("Eid al-Adha (2nd Day)", 2025).orElseThrow().date();
        LocalDate day3 = findFirst("Eid al-Adha (3rd Day)", 2025).orElseThrow().date();
        LocalDate day4 = findFirst("Eid al-Adha (4th Day)", 2025).orElseThrow().date();
        assertEquals(day2, day1.plusDays(1), "Eid al-Adha Day 2 must be the day after Day 1");
        assertEquals(day3, day1.plusDays(2), "Eid al-Adha Day 3 must be two days after Day 1");
        assertEquals(day4, day1.plusDays(3), "Eid al-Adha Day 4 must be three days after Day 1");
    }

    // =========================================================================
    // dataValidThrough
    // =========================================================================

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "TR calendar has lookup-table Islamic holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("TR");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), 2055,
                "factory.dataValidThrough(\"TR\") must return 2055");
    }

    // =========================================================================
    // Boundary: 2024 (first valid year)
    // =========================================================================

    @Test
    public void testCalculate2024ContainsEidAlFitr() {
        assertTrue(findFirst("Eid al-Fitr", 2024).isPresent(),
                "Eid al-Fitr must be present for boundary year 2024");
    }

    @Test
    public void testCalculate2024ContainsEidAlAdha() {
        assertTrue(findFirst("Eid al-Adha", 2024).isPresent(),
                "Eid al-Adha must be present for boundary year 2024");
    }

    // =========================================================================
    // Boundary: 2055 (last valid year)
    // =========================================================================

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundaryYear = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundaryYear);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundaryYear + ") must return holidays — within covered range");
        assertEquals(holidays.size(), TR_HOLIDAY_COUNT,
                "Expected " + TR_HOLIDAY_COUNT + " holidays for boundary year " + boundaryYear);
    }

    @Test
    public void testEidAlFitr2055Present() {
        assertTrue(findFirst("Eid al-Fitr", 2055).isPresent(),
                "Eid al-Fitr must be present at the data ceiling year 2055");
    }

    @Test
    public void testEidAlAdha2055Present() {
        assertTrue(findFirst("Eid al-Adha", 2055).isPresent(),
                "Eid al-Adha must be present at the data ceiling year 2055");
    }

    // =========================================================================
    // Beyond ceiling: 2056 — Islamic holidays silently absent
    // =========================================================================

    @Test
    public void testCalculateBeyondDataValidThroughDropsIslamicHolidays() {
        int boundaryYear = service.dataValidThrough().orElseThrow();
        List<HolidayDate> atBoundary = service.getHolidayCalendar().calculate(boundaryYear);
        List<HolidayDate> beyondBoundary = service.getHolidayCalendar().calculate(boundaryYear + 1);
        assertTrue(beyondBoundary.size() < atBoundary.size(),
                "Year beyond dataValidThrough must produce fewer holidays (Islamic tables exhausted); "
                + "at boundary: " + atBoundary.size() + ", beyond: " + beyondBoundary.size());
    }

    @Test
    public void testEidAlFitr2056AbsentSilently() {
        assertFalse(findFirst("Eid al-Fitr", 2056).isPresent(),
                "Eid al-Fitr must be silently absent for 2056 — beyond the table ceiling");
    }

    @Test
    public void testEidAlAdha2056AbsentSilently() {
        assertFalse(findFirst("Eid al-Adha", 2056).isPresent(),
                "Eid al-Adha must be silently absent for 2056 — beyond the table ceiling");
    }

    @Test
    public void testFixedHolidaysPresentBeyondCeiling() {
        List<HolidayDate> holidays2056 = service.getHolidayCalendar().calculate(2056);
        assertEquals(holidays2056.size(), 7,
                "Beyond CSV ceiling only the 7 fixed Gregorian holidays must remain");
    }

    // =========================================================================
    // Before floor: 2023 — Islamic holidays absent
    // =========================================================================

    @Test
    public void testEidAlFitr2023AbsentSilently() {
        assertFalse(findFirst("Eid al-Fitr", 2023).isPresent(),
                "Eid al-Fitr must be silently absent for 2023 — before the table floor");
    }

    // =========================================================================
    // Double Eid al-Fitr in 2033 — only January occurrence recorded
    // =========================================================================

    @Test
    public void testEidAlFitr2033OnlyOneOccurrence() {
        List<HolidayDate> eidOccurrences = service.getHolidayCalendar().calculate(2033).stream()
                .filter(hd -> "Eid al-Fitr".equals(hd.holiday().getName()))
                .toList();
        assertEquals(eidOccurrences.size(), 1,
                "2033 has two Eid al-Fitr occurrences but only the first is recorded in the CSV");
        assertEquals(eidOccurrences.getFirst().date(), LocalDate.of(2033, Month.JANUARY, 3),
                "The single 2033 Eid al-Fitr occurrence must be January 3");
    }

    // =========================================================================
    // Republic Day Eve (Oct 28) — confirmed absent; half-day not modelled
    // =========================================================================

    @Test
    public void testRepublicDayEveAbsent2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        boolean oct28Present = holidays.stream()
                .anyMatch(hd -> hd.date().equals(LocalDate.of(2025, Month.OCTOBER, 28)));
        assertFalse(oct28Present,
                "Oct 28 (Republic Day Eve) must not be present — half-day closures are not modelled");
    }

    // =========================================================================
    // Helper
    // =========================================================================

    private Optional<HolidayDate> findFirst(String name, int year) {
        return service.getHolidayCalendar().calculate(year).stream()
                .filter(hd -> name.equals(hd.holiday().getName()))
                .findFirst();
    }
}
