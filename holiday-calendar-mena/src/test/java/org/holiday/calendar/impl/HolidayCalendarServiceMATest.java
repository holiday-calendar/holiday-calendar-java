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

public class HolidayCalendarServiceMATest {

    // 10 fixed Gregorian + 2 Eid al-Fitr + 2 Eid al-Adha
    // + Islamic New Year + Prophet's Birthday × 2 = 17
    private static final int MA_HOLIDAY_COUNT = 17;

    // Beyond CSV ceiling: 10 fixed Gregorian holidays survive
    private static final int MA_FIXED_HOLIDAY_COUNT = 10;

    private final HolidayCalendarServiceMA service = new HolidayCalendarServiceMA();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // ── service identity ──────────────────────────────────────────────────────

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("MA"));
        assertFalse(service.isProvided("MAD"));
        assertFalse(service.isProvided("EG"));
        assertFalse(service.isProvided("BH"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "MA");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Morocco (National) Holidays");
    }

    // ── factory integration ───────────────────────────────────────────────────

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("MA");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "MA");
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
        assertEquals(holidays.size(), MA_HOLIDAY_COUNT,
                "Expected " + MA_HOLIDAY_COUNT + " holidays for " + year +
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

    // ── fixed holidays — no roll on weekday ───────────────────────────────────

    // Jan 1, 2025 is Wednesday — must not roll
    @Test
    public void testNewYearsDay2025NoRoll() {
        assertEquals(LocalDate.of(2025, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.WEDNESDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2025);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2025, Month.JANUARY, 1),
                "New Year's Day 2025 (Wednesday) must not roll");
    }

    // May 1, 2025 is Thursday — must not roll
    @Test
    public void testLabourDay2025NoRoll() {
        assertEquals(LocalDate.of(2025, Month.MAY, 1).getDayOfWeek(), DayOfWeek.THURSDAY);
        Optional<HolidayDate> ld = findFirst("Labour Day", 2025);
        assertTrue(ld.isPresent());
        assertEquals(ld.get().date(), LocalDate.of(2025, Month.MAY, 1),
                "Labour Day 2025 (Thursday) must not roll");
    }

    // Jul 30, 2025 is Wednesday — must not roll
    @Test
    public void testThroneDay2025NoRoll() {
        assertEquals(LocalDate.of(2025, Month.JULY, 30).getDayOfWeek(), DayOfWeek.WEDNESDAY);
        Optional<HolidayDate> td = findFirst("Throne Day", 2025);
        assertTrue(td.isPresent());
        assertEquals(td.get().date(), LocalDate.of(2025, Month.JULY, 30),
                "Throne Day 2025 (Wednesday) must not roll");
    }

    // ── fixed holidays — Saturday/Sunday roll to following Monday ─────────────

    // Jan 11, 2025 is Saturday → rolls to Monday Jan 13
    @Test
    public void testManifestoDay2025OnSaturdayRollsToMonday() {
        assertEquals(LocalDate.of(2025, Month.JANUARY, 11).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> md = findFirst("Manifesto of Independence Day", 2025);
        assertTrue(md.isPresent());
        assertEquals(md.get().date(), LocalDate.of(2025, Month.JANUARY, 13),
                "Manifesto of Independence Day 2025 (Saturday) must roll to Monday Jan 13");
    }

    // Jan 14, 2024 is Sunday → rolls to Monday Jan 15
    @Test
    public void testAmazighNewYear2024OnSundayRollsToMonday() {
        assertEquals(LocalDate.of(2024, Month.JANUARY, 14).getDayOfWeek(), DayOfWeek.SUNDAY);
        Optional<HolidayDate> any = findFirst("Amazigh New Year", 2024);
        assertTrue(any.isPresent());
        assertEquals(any.get().date(), LocalDate.of(2024, Month.JANUARY, 15),
                "Amazigh New Year 2024 (Sunday) must roll to Monday Jan 15");
    }

    // Jan 14, 2025 is Tuesday — must not roll
    @Test
    public void testAmazighNewYear2025NoRoll() {
        assertEquals(LocalDate.of(2025, Month.JANUARY, 14).getDayOfWeek(), DayOfWeek.TUESDAY);
        Optional<HolidayDate> any = findFirst("Amazigh New Year", 2025);
        assertTrue(any.isPresent());
        assertEquals(any.get().date(), LocalDate.of(2025, Month.JANUARY, 14),
                "Amazigh New Year 2025 (Tuesday) must not roll");
    }

    // Jan 1, 2028 is Saturday → rolls to Monday Jan 3
    @Test
    public void testNewYearsDay2028OnSaturdayRollsToMonday() {
        assertEquals(LocalDate.of(2028, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2028);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2028, Month.JANUARY, 3),
                "New Year's Day 2028 (Saturday) must roll to Monday Jan 3");
    }

    // Aug 14, 2027 is Saturday → rolls to Monday Aug 16
    @Test
    public void testOuedEdDahab2027OnSaturdayRollsToMonday() {
        assertEquals(LocalDate.of(2027, Month.AUGUST, 14).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> odd = findFirst("Oued Ed-Dahab Allegiance Day", 2027);
        assertTrue(odd.isPresent());
        assertEquals(odd.get().date(), LocalDate.of(2027, Month.AUGUST, 16),
                "Oued Ed-Dahab Allegiance Day 2027 (Saturday) must roll to Monday Aug 16");
    }

    // Nov 18, 2028 is Saturday → rolls to Monday Nov 20
    @Test
    public void testIndependenceDay2028OnSaturdayRollsToMonday() {
        assertEquals(LocalDate.of(2028, Month.NOVEMBER, 18).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> id = findFirst("Independence Day", 2028);
        assertTrue(id.isPresent());
        assertEquals(id.get().date(), LocalDate.of(2028, Month.NOVEMBER, 20),
                "Independence Day 2028 (Saturday) must roll to Monday Nov 20");
    }

    // ── Islamic holidays — rollable(false): never roll even on weekend ─────────

    // KEY EDGE CASE: Eid al-Adha 2025 = Saturday Jun 7 — must NOT roll despite MA using followingMonday
    @Test
    public void testEidAlAdha2025OnSaturdayNotRolled() {
        assertEquals(LocalDate.of(2025, Month.JUNE, 7).getDayOfWeek(), DayOfWeek.SATURDAY,
                "Pre-condition: June 7, 2025 must be a Saturday");
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2025);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2025, Month.JUNE, 7),
                "Eid al-Adha 2025 (Saturday) must stay Jun 7 — Islamic holidays are rollable(false) in MA");
    }

    // Eid al-Adha Day 2, 2025 = Sunday Jun 8 — must NOT roll
    @Test
    public void testEidAlAdhaDay2_2025OnSundayNotRolled() {
        assertEquals(LocalDate.of(2025, Month.JUNE, 8).getDayOfWeek(), DayOfWeek.SUNDAY,
                "Pre-condition: June 8, 2025 must be a Sunday");
        Optional<HolidayDate> adha2 = findFirst("Eid al-Adha (2nd Day)", 2025);
        assertTrue(adha2.isPresent());
        assertEquals(adha2.get().date(), LocalDate.of(2025, Month.JUNE, 8),
                "Eid al-Adha (2nd Day) 2025 (Sunday) must stay Jun 8 — rollable(false)");
    }

    // Islamic New Year 2025 = Friday Jun 27 — must NOT roll (Friday is not a Morocco weekend)
    @Test
    public void testIslamicNewYear2025OnFriday() {
        assertEquals(LocalDate.of(2025, Month.JUNE, 27).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> iny = findFirst("Islamic New Year", 2025);
        assertTrue(iny.isPresent());
        assertEquals(iny.get().date(), LocalDate.of(2025, Month.JUNE, 27),
                "Islamic New Year 2025 must be 2025-06-27 per CSE AV-2025-078");
    }

    // ── Islamic holiday spot-checks (official 2024–2025 dates) ───────────────

    @Test
    public void testEidAlFitr2024() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2024);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2024, Month.APRIL, 10),
                "Eid al-Fitr 2024 must be 2024-04-10 per official announcement");
    }

    @Test
    public void testEidAlFitrDay2_2024() {
        Optional<HolidayDate> eid2 = findFirst("Eid al-Fitr (2nd Day)", 2024);
        assertTrue(eid2.isPresent());
        assertEquals(eid2.get().date(), LocalDate.of(2024, Month.APRIL, 11),
                "Eid al-Fitr (2nd Day) 2024 must be 2024-04-11");
    }

    @Test
    public void testEidAlFitr2025() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2025);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2025, Month.MARCH, 31),
                "Eid al-Fitr 2025 must be 2025-03-31 per CSE AV-2025-078 (one day after GCC consensus)");
    }

    @Test
    public void testEidAlFitrDay2_2025() {
        Optional<HolidayDate> eid2 = findFirst("Eid al-Fitr (2nd Day)", 2025);
        assertTrue(eid2.isPresent());
        assertEquals(eid2.get().date(), LocalDate.of(2025, Month.APRIL, 1),
                "Eid al-Fitr (2nd Day) 2025 must be 2025-04-01");
    }

    @Test
    public void testEidAlFitrDay2IsOneDayAfterDay1() {
        for (int year : new int[]{2024, 2025, 2026}) {
            Optional<HolidayDate> day1 = findFirst("Eid al-Fitr", year);
            Optional<HolidayDate> day2 = findFirst("Eid al-Fitr (2nd Day)", year);
            assertTrue(day1.isPresent(), year + ": Eid al-Fitr Day 1 must be present");
            assertTrue(day2.isPresent(), year + ": Eid al-Fitr Day 2 must be present");
            assertEquals(day2.get().date(), day1.get().date().plusDays(1),
                    year + ": Eid al-Fitr (2nd Day) must be exactly one day after Day 1");
        }
    }

    @Test
    public void testEidAlAdha2024() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2024);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2024, Month.JUNE, 16),
                "Eid al-Adha 2024 must be 2024-06-16 per official announcement");
    }

    @Test
    public void testEidAlAdha2025() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2025);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2025, Month.JUNE, 7),
                "Eid al-Adha 2025 must be 2025-06-07 per CSE AV-2025-078 (one day after GCC consensus)");
    }

    @Test
    public void testEidAlAdhaDay2IsOneDayAfterDay1() {
        for (int year : new int[]{2024, 2025, 2026}) {
            Optional<HolidayDate> day1 = findFirst("Eid al-Adha", year);
            Optional<HolidayDate> day2 = findFirst("Eid al-Adha (2nd Day)", year);
            assertTrue(day1.isPresent(), year + ": Eid al-Adha Day 1 must be present");
            assertTrue(day2.isPresent(), year + ": Eid al-Adha Day 2 must be present");
            assertEquals(day2.get().date(), day1.get().date().plusDays(1),
                    year + ": Eid al-Adha (2nd Day) must be exactly one day after Day 1");
        }
    }

    @Test
    public void testIslamicNewYear2024() {
        Optional<HolidayDate> iny = findFirst("Islamic New Year", 2024);
        assertTrue(iny.isPresent());
        assertEquals(iny.get().date(), LocalDate.of(2024, Month.JULY, 7),
                "Islamic New Year 2024 must be 2024-07-07 per official announcement");
    }

    @Test
    public void testIslamicNewYear2025() {
        Optional<HolidayDate> iny = findFirst("Islamic New Year", 2025);
        assertTrue(iny.isPresent());
        assertEquals(iny.get().date(), LocalDate.of(2025, Month.JUNE, 27),
                "Islamic New Year 2025 must be 2025-06-27 per CSE AV-2025-078");
    }

    @Test
    public void testProphetsBirthday2024() {
        Optional<HolidayDate> pb = findFirst("Prophet's Birthday", 2024);
        assertTrue(pb.isPresent());
        assertEquals(pb.get().date(), LocalDate.of(2024, Month.SEPTEMBER, 15),
                "Prophet's Birthday 2024 must be 2024-09-15 per official announcement");
    }

    // Mawlid 2025: Day 1 = Fri Sep 5 (natural date, observed as-is)
    @Test
    public void testProphetsBirthday2025OnFriday() {
        assertEquals(LocalDate.of(2025, Month.SEPTEMBER, 5).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> pb = findFirst("Prophet's Birthday", 2025);
        assertTrue(pb.isPresent());
        assertEquals(pb.get().date(), LocalDate.of(2025, Month.SEPTEMBER, 5),
                "Prophet's Birthday 2025 must be 2025-09-05 per CSE AV-2025-078");
    }

    // Mawlid Day 2, 2025 = Sat Sep 6 — must NOT roll (Islamic rollable=false)
    @Test
    public void testProphetsBirthdayDay2_2025OnSaturdayNotRolled() {
        assertEquals(LocalDate.of(2025, Month.SEPTEMBER, 6).getDayOfWeek(), DayOfWeek.SATURDAY,
                "Pre-condition: September 6, 2025 must be a Saturday");
        Optional<HolidayDate> pb2 = findFirst("Prophet's Birthday (2nd Day)", 2025);
        assertTrue(pb2.isPresent());
        assertEquals(pb2.get().date(), LocalDate.of(2025, Month.SEPTEMBER, 6),
                "Prophet's Birthday (2nd Day) 2025 (Saturday) must stay Sep 6 — rollable(false)");
    }

    @Test
    public void testProphetsBirthdayDay2IsOneDayAfterDay1() {
        for (int year : new int[]{2024, 2025, 2026}) {
            Optional<HolidayDate> day1 = findFirst("Prophet's Birthday", year);
            Optional<HolidayDate> day2 = findFirst("Prophet's Birthday (2nd Day)", year);
            assertTrue(day1.isPresent(), year + ": Prophet's Birthday Day 1 must be present");
            assertTrue(day2.isPresent(), year + ": Prophet's Birthday (2nd Day) must be present");
            assertEquals(day2.get().date(), day1.get().date().plusDays(1),
                    year + ": Prophet's Birthday (2nd Day) must be exactly one day after Day 1");
        }
    }

    // ── Amazigh New Year presence ─────────────────────────────────────────────

    @Test
    public void testAmazighNewYearIncluded() {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> "Amazigh New Year".equals(h.getName()));
        assertTrue(found,
                "Amazigh New Year (Jan 14, added 2024) must be included in the MA calendar");
    }

    // ── dataValidThrough ──────────────────────────────────────────────────────

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "MA calendar has CSV-backed Islamic holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("MA");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"MA\") must delegate to the service");
    }

    // ── CSV boundary behaviour ────────────────────────────────────────────────

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundary);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundary + ") must return holidays — within covered range");
        assertEquals(holidays.size(), MA_HOLIDAY_COUNT,
                "Expected all " + MA_HOLIDAY_COUNT + " MA holidays for boundary year " + boundary);
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
        assertEquals(holidays2056.size(), MA_FIXED_HOLIDAY_COUNT,
                "Beyond CSV ceiling only the " + MA_FIXED_HOLIDAY_COUNT +
                " fixed Morocco holidays must remain (10 Gregorian)");
    }

    // ── 2033 dual-Eid al-Fitr ────────────────────────────────────────────────

    @Test
    public void testEidAlFitr2033OnlyJanuaryOccurrence() {
        List<HolidayDate> eidOccurrences = service.getHolidayCalendar().calculate(2033).stream()
                .filter(hd -> "Eid al-Fitr".equals(hd.holiday().getName()))
                .toList();
        assertEquals(eidOccurrences.size(), 1,
                "2033 has two Eid al-Fitr occurrences but only January is recorded in the CSV");
        assertEquals(eidOccurrences.getFirst().date().getMonth(), Month.JANUARY,
                "The single 2033 Eid al-Fitr occurrence must be in January");
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
        assertTrue(found, "Calendar must contain holiday: " + holidayName);
    }

    // ── helper ────────────────────────────────────────────────────────────────

    private Optional<HolidayDate> findFirst(String name, int year) {
        return service.getHolidayCalendar().calculate(year).stream()
                .filter(hd -> name.equals(hd.holiday().getName()))
                .findFirst();
    }

}
