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

public class HolidayCalendarServiceJOTest {

    // 4 fixed (New Year's Day, Labour Day, Independence Day, Christmas Day)
    // + 4 Eid al-Fitr + Arafat Day + 4 Eid al-Adha
    // + Islamic New Year + Prophet's Birthday = 15
    private static final int JO_HOLIDAY_COUNT = 15;

    // Beyond CSV ceiling: 4 fixed Gregorian holidays survive
    private static final int JO_FIXED_HOLIDAY_COUNT = 4;

    private final HolidayCalendarServiceJO service = new HolidayCalendarServiceJO();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // ── service identity ──────────────────────────────────────────────────────

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("JO"));
        assertFalse(service.isProvided("JOD"));
        assertFalse(service.isProvided("BH"));
        assertFalse(service.isProvided("EG"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "JO");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Jordan (National) Holidays");
    }

    // ── factory integration ───────────────────────────────────────────────────

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("JO");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "JO");
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
        assertEquals(holidays.size(), JO_HOLIDAY_COUNT,
                "Expected " + JO_HOLIDAY_COUNT + " holidays for " + year +
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

    @Test
    public void testChronologicalOrder2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).date().isBefore(holidays.get(i - 1).date()),
                    "Holidays must be in chronological order");
        }
    }

    // ── fixed holidays — no roll on weekday or Sunday ─────────────────────────
    //
    // In Jordan, Sunday is the first business day; it must NEVER trigger a roll.

    // Jan 1, 2025 is Wednesday — must not roll
    @Test
    public void testNewYearsDay2025NoRoll() {
        assertEquals(LocalDate.of(2025, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.WEDNESDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2025);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2025, Month.JANUARY, 1),
                "New Year's Day 2025 (Wednesday) must not roll");
    }

    // May 25, 2025 is Sunday — must NOT roll (Sunday is the first business day in Jordan)
    @Test
    public void testIndependenceDayOnSundayNotRolled2025() {
        assertEquals(LocalDate.of(2025, Month.MAY, 25).getDayOfWeek(), DayOfWeek.SUNDAY);
        Optional<HolidayDate> id = findFirst("Independence Day", 2025);
        assertTrue(id.isPresent());
        assertEquals(id.get().date(), LocalDate.of(2025, Month.MAY, 25),
                "Independence Day 2025 (Sunday) must not roll — Sunday is first business day in Jordan");
    }

    // Jun 10, 2025 is Tuesday — unused (Army Day not in Jordan calendar; verifies no such holiday exists)
    @Test
    public void testNoArmyDayHoliday() {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> h.getName().contains("Army Day"));
        assertFalse(found, "Jordan calendar must not contain Army Day — not a gazetted public holiday");
    }

    // ── fixed holidays — Friday/Saturday roll to following Sunday ─────────────

    // May 25, 2024 is Saturday → rolls to Sunday May 26
    @Test
    public void testIndependenceDayRollFromSaturday2024() {
        assertEquals(LocalDate.of(2024, Month.MAY, 25).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> id = findFirst("Independence Day", 2024);
        assertTrue(id.isPresent());
        assertEquals(id.get().date(), LocalDate.of(2024, Month.MAY, 26),
                "Independence Day 2024 (Saturday) must roll to Sunday May 26");
    }

    // May 1, 2026 is Friday → rolls to Sunday May 3
    @Test
    public void testLabourDayRollFromFriday2026() {
        assertEquals(LocalDate.of(2026, Month.MAY, 1).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ld = findFirst("Labour Day", 2026);
        assertTrue(ld.isPresent());
        assertEquals(ld.get().date(), LocalDate.of(2026, Month.MAY, 3),
                "Labour Day 2026 (Friday) must roll to Sunday May 3");
    }

    // Dec 25, 2026 is Friday → rolls to Sunday Dec 27
    @Test
    public void testChristmasDayRollFromFriday2026() {
        assertEquals(LocalDate.of(2026, Month.DECEMBER, 25).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> cd = findFirst("Christmas Day", 2026);
        assertTrue(cd.isPresent());
        assertEquals(cd.get().date(), LocalDate.of(2026, Month.DECEMBER, 27),
                "Christmas Day 2026 (Friday) must roll to Sunday Dec 27");
    }

    // Jan 1, 2027 is Friday → rolls to Sunday Jan 3
    @Test
    public void testNewYearsDayRollFromFriday2027() {
        assertEquals(LocalDate.of(2027, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2027);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2027, Month.JANUARY, 3),
                "New Year's Day 2027 (Friday) must roll to Sunday Jan 3");
    }

    // May 1, 2027 is Saturday → rolls to Sunday May 2
    @Test
    public void testLabourDayRollFromSaturday2027() {
        assertEquals(LocalDate.of(2027, Month.MAY, 1).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> ld = findFirst("Labour Day", 2027);
        assertTrue(ld.isPresent());
        assertEquals(ld.get().date(), LocalDate.of(2027, Month.MAY, 2),
                "Labour Day 2027 (Saturday) must roll to Sunday May 2");
    }

    // Dec 25, 2027 is Saturday → rolls to Sunday Dec 26
    @Test
    public void testChristmasDayRollFromSaturday2027() {
        assertEquals(LocalDate.of(2027, Month.DECEMBER, 25).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> cd = findFirst("Christmas Day", 2027);
        assertTrue(cd.isPresent());
        assertEquals(cd.get().date(), LocalDate.of(2027, Month.DECEMBER, 26),
                "Christmas Day 2027 (Saturday) must roll to Sunday Dec 26");
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

    // May 25, 2029 is Friday → rolls to Sunday May 27
    @Test
    public void testIndependenceDayRollFromFriday2029() {
        assertEquals(LocalDate.of(2029, Month.MAY, 25).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> id = findFirst("Independence Day", 2029);
        assertTrue(id.isPresent());
        assertEquals(id.get().date(), LocalDate.of(2029, Month.MAY, 27),
                "Independence Day 2029 (Friday) must roll to Sunday May 27");
    }

    // May 25, 2030 is Saturday → rolls to Sunday May 26
    @Test
    public void testIndependenceDayRollFromSaturday2030() {
        assertEquals(LocalDate.of(2030, Month.MAY, 25).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> id = findFirst("Independence Day", 2030);
        assertTrue(id.isPresent());
        assertEquals(id.get().date(), LocalDate.of(2030, Month.MAY, 26),
                "Independence Day 2030 (Saturday) must roll to Sunday May 26");
    }

    // ── Islamic holiday spot-checks ───────────────────────────────────────────
    //
    // 2024: sourced from CBJ / ASE official announcements.
    // 2025–2026: sourced from ASE official announcements.
    // Jordan determines Islamic holiday dates by moon sighting (Ministry of Awqaf /
    // Higher Judiciary Council) and may differ from Saudi Arabia by ±1 day.

    @Test
    public void testEidAlFitr2024() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2024);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2024, Month.APRIL, 9),
                "Eid al-Fitr 2024 must be 2024-04-09 per CBJ official");
    }

    @Test
    public void testEidAlFitrDay2_2024() {
        Optional<HolidayDate> eid2 = findFirst("Eid al-Fitr (2nd Day)", 2024);
        assertTrue(eid2.isPresent());
        assertEquals(eid2.get().date(), LocalDate.of(2024, Month.APRIL, 10));
    }

    @Test
    public void testEidAlFitrDay3_2024() {
        Optional<HolidayDate> eid3 = findFirst("Eid al-Fitr (3rd Day)", 2024);
        assertTrue(eid3.isPresent());
        assertEquals(eid3.get().date(), LocalDate.of(2024, Month.APRIL, 11));
    }

    @Test
    public void testEidAlFitrDay4_2024() {
        Optional<HolidayDate> eid4 = findFirst("Eid al-Fitr (4th Day)", 2024);
        assertTrue(eid4.isPresent());
        assertEquals(eid4.get().date(), LocalDate.of(2024, Month.APRIL, 12));
    }

    @Test
    public void testEidAlFitr2025() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2025);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2025, Month.MARCH, 30),
                "Eid al-Fitr 2025 must be 2025-03-30 per ASE official");
    }

    @Test
    public void testEidAlFitr2026() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2026);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2026, Month.MARCH, 20),
                "Eid al-Fitr 2026 must be 2026-03-20 per ASE official");
    }

    @Test
    public void testEidAlFitrConsecutiveDays() {
        for (int year : new int[]{2024, 2025, 2026}) {
            Optional<HolidayDate> day1 = findFirst("Eid al-Fitr", year);
            Optional<HolidayDate> day2 = findFirst("Eid al-Fitr (2nd Day)", year);
            Optional<HolidayDate> day3 = findFirst("Eid al-Fitr (3rd Day)", year);
            Optional<HolidayDate> day4 = findFirst("Eid al-Fitr (4th Day)", year);
            assertTrue(day1.isPresent(), year + ": Eid al-Fitr must be present");
            assertTrue(day2.isPresent(), year + ": Eid al-Fitr (2nd Day) must be present");
            assertTrue(day3.isPresent(), year + ": Eid al-Fitr (3rd Day) must be present");
            assertTrue(day4.isPresent(), year + ": Eid al-Fitr (4th Day) must be present");
            assertEquals(day2.get().date(), day1.get().date().plusDays(1),
                    year + ": Eid al-Fitr (2nd Day) must be Day 1 + 1");
            assertEquals(day3.get().date(), day1.get().date().plusDays(2),
                    year + ": Eid al-Fitr (3rd Day) must be Day 1 + 2");
            assertEquals(day4.get().date(), day1.get().date().plusDays(3),
                    year + ": Eid al-Fitr (4th Day) must be Day 1 + 3");
        }
    }

    @Test
    public void testArafatDay2024() {
        Optional<HolidayDate> ad = findFirst("Arafat Day", 2024);
        assertTrue(ad.isPresent());
        assertEquals(ad.get().date(), LocalDate.of(2024, Month.JUNE, 15),
                "Arafat Day 2024 must be 2024-06-15 per ASE official (falls on Saturday)");
    }

    @Test
    public void testArafatDay2025() {
        Optional<HolidayDate> ad = findFirst("Arafat Day", 2025);
        assertTrue(ad.isPresent());
        assertEquals(ad.get().date(), LocalDate.of(2025, Month.JUNE, 5),
                "Arafat Day 2025 must be 2025-06-05 per ASE official");
    }

    @Test
    public void testArafatDayPrecedesEidAlAdha() {
        for (int year : new int[]{2024, 2025, 2026}) {
            Optional<HolidayDate> ad  = findFirst("Arafat Day", year);
            Optional<HolidayDate> eid = findFirst("Eid al-Adha", year);
            assertTrue(ad.isPresent(),  year + ": Arafat Day must be present");
            assertTrue(eid.isPresent(), year + ": Eid al-Adha must be present");
            assertEquals(ad.get().date(), eid.get().date().minusDays(1),
                    year + ": Arafat Day must be exactly one day before Eid al-Adha");
        }
    }

    @Test
    public void testEidAlAdha2024() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2024);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2024, Month.JUNE, 16),
                "Eid al-Adha 2024 must be 2024-06-16 per CBJ official");
    }

    @Test
    public void testEidAlAdha2025() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2025);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2025, Month.JUNE, 6),
                "Eid al-Adha 2025 must be 2025-06-06 per ASE official");
    }

    @Test
    public void testEidAlAdha2026() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2026);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2026, Month.MAY, 27),
                "Eid al-Adha 2026 must be 2026-05-27 per ASE official");
    }

    @Test
    public void testEidAlAdhaConsecutiveDays() {
        for (int year : new int[]{2024, 2025, 2026}) {
            Optional<HolidayDate> day1 = findFirst("Eid al-Adha", year);
            Optional<HolidayDate> day2 = findFirst("Eid al-Adha (2nd Day)", year);
            Optional<HolidayDate> day3 = findFirst("Eid al-Adha (3rd Day)", year);
            Optional<HolidayDate> day4 = findFirst("Eid al-Adha (4th Day)", year);
            assertTrue(day1.isPresent(), year + ": Eid al-Adha must be present");
            assertTrue(day2.isPresent(), year + ": Eid al-Adha (2nd Day) must be present");
            assertTrue(day3.isPresent(), year + ": Eid al-Adha (3rd Day) must be present");
            assertTrue(day4.isPresent(), year + ": Eid al-Adha (4th Day) must be present");
            assertEquals(day2.get().date(), day1.get().date().plusDays(1),
                    year + ": Eid al-Adha (2nd Day) must be Day 1 + 1");
            assertEquals(day3.get().date(), day1.get().date().plusDays(2),
                    year + ": Eid al-Adha (3rd Day) must be Day 1 + 2");
            assertEquals(day4.get().date(), day1.get().date().plusDays(3),
                    year + ": Eid al-Adha (4th Day) must be Day 1 + 3");
        }
    }

    @Test
    public void testIslamicNewYear2024() {
        Optional<HolidayDate> iny = findFirst("Islamic New Year", 2024);
        assertTrue(iny.isPresent());
        assertEquals(iny.get().date(), LocalDate.of(2024, Month.JULY, 7),
                "Islamic New Year 2024 must be 2024-07-07 per CBJ official");
    }

    @Test
    public void testIslamicNewYear2025() {
        Optional<HolidayDate> iny = findFirst("Islamic New Year", 2025);
        assertTrue(iny.isPresent());
        assertEquals(iny.get().date(), LocalDate.of(2025, Month.JUNE, 26),
                "Islamic New Year 2025 must be 2025-06-26 per ASE official");
    }

    @Test
    public void testProphetsBirthday2024() {
        Optional<HolidayDate> pb = findFirst("Prophet's Birthday", 2024);
        assertTrue(pb.isPresent());
        assertEquals(pb.get().date(), LocalDate.of(2024, Month.SEPTEMBER, 16),
                "Prophet's Birthday 2024 must be 2024-09-16 per CBJ official");
    }

    @Test
    public void testProphetsBirthday2025() {
        Optional<HolidayDate> pb = findFirst("Prophet's Birthday", 2025);
        assertTrue(pb.isPresent());
        assertEquals(pb.get().date(), LocalDate.of(2025, Month.SEPTEMBER, 4),
                "Prophet's Birthday 2025 must be 2025-09-04 per ASE official");
    }

    // ── dataValidThrough ──────────────────────────────────────────────────────

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "JO calendar has CSV-backed Islamic holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("JO");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"JO\") must delegate to the service");
    }

    // ── CSV boundary behaviour ────────────────────────────────────────────────

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundary);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundary + ") must return holidays — within covered range");
        assertEquals(holidays.size(), JO_HOLIDAY_COUNT,
                "Expected all " + JO_HOLIDAY_COUNT + " JO holidays for boundary year " + boundary);
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
        assertEquals(holidays2056.size(), JO_FIXED_HOLIDAY_COUNT,
                "Beyond CSV ceiling only the " + JO_FIXED_HOLIDAY_COUNT +
                " fixed Jordan holidays must remain");
    }

    // ── 2033 dual Eid al-Fitr ────────────────────────────────────────────────

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
        assertTrue(found, "Calendar must contain holiday: " + holidayName);
    }

    // ── helper ────────────────────────────────────────────────────────────────

    private Optional<HolidayDate> findFirst(String name, int year) {
        return service.getHolidayCalendar().calculate(year).stream()
                .filter(hd -> name.equals(hd.holiday().getName()))
                .findFirst();
    }

}
