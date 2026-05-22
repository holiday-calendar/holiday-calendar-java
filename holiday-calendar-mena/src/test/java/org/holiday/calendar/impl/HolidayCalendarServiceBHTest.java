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

public class HolidayCalendarServiceBHTest {

    // 4 fixed (New Year's Day, Labour Day, National Day, Accession Day)
    // + 3 Eid al-Fitr + Arafat Day + 3 Eid al-Adha
    // + Islamic New Year + 2 Ashura + Prophet's Birthday = 15
    private static final int BH_HOLIDAY_COUNT = 15;

    // Beyond CSV ceiling: 4 fixed Gregorian holidays survive
    private static final int BH_FIXED_HOLIDAY_COUNT = 4;

    private final HolidayCalendarServiceBH service = new HolidayCalendarServiceBH();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // ── service identity ──────────────────────────────────────────────────────

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("BH"));
        assertFalse(service.isProvided("BHD"));
        assertFalse(service.isProvided("KW"));
        assertFalse(service.isProvided("AE"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "BH");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Bahrain (National) Holidays");
    }

    // ── factory integration ───────────────────────────────────────────────────

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("BH");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "BH");
    }

    // ── weekend configuration ─────────────────────────────────────────────────

    @Test
    public void testWeekendDaysFridayAndSaturday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2, "Bahrain weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY), "Friday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY), "Saturday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY), "Sunday must not be a weekend day");
    }

    // ── total count ───────────────────────────────────────────────────────────

    @Test
    public void testCalculate2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        assertNotNull(holidays);
        assertEquals(holidays.size(), BH_HOLIDAY_COUNT,
                "Expected " + BH_HOLIDAY_COUNT + " holidays for 2024, got: " + holidays.size());
    }

    @Test
    public void testCalculate2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), BH_HOLIDAY_COUNT,
                "Expected " + BH_HOLIDAY_COUNT + " holidays for 2025, got: " + holidays.size());
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

    // ── fixed holidays — no roll on weekday ──────────────────────────────────

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

    // Dec 16, 2025 is Tuesday — must not roll
    @Test
    public void testNationalDay2025NoRoll() {
        assertEquals(LocalDate.of(2025, Month.DECEMBER, 16).getDayOfWeek(), DayOfWeek.TUESDAY);
        Optional<HolidayDate> nd = findFirst("National Day", 2025);
        assertTrue(nd.isPresent());
        assertEquals(nd.get().date(), LocalDate.of(2025, Month.DECEMBER, 16),
                "National Day 2025 (Tuesday) must not roll");
    }

    // Dec 17, 2025 is Wednesday — must not roll
    @Test
    public void testAccessionDay2025NoRoll() {
        assertEquals(LocalDate.of(2025, Month.DECEMBER, 17).getDayOfWeek(), DayOfWeek.WEDNESDAY);
        Optional<HolidayDate> ad = findFirst("Accession Day", 2025);
        assertTrue(ad.isPresent());
        assertEquals(ad.get().date(), LocalDate.of(2025, Month.DECEMBER, 17),
                "Accession Day 2025 (Wednesday) must not roll");
    }

    // ── fixed holidays — Friday/Saturday roll to following Sunday ─────────────

    // Jan 1, 2027 is Friday → rolls to Sunday Jan 3
    @Test
    public void testNewYearsDayRollFromFriday2027() {
        assertEquals(LocalDate.of(2027, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2027);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2027, Month.JANUARY, 3),
                "New Year's Day 2027 (Friday) must roll to Sunday Jan 3");
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

    // May 1, 2026 is Friday → rolls to Sunday May 3
    @Test
    public void testLabourDayRollFromFriday2026() {
        assertEquals(LocalDate.of(2026, Month.MAY, 1).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ld = findFirst("Labour Day", 2026);
        assertTrue(ld.isPresent());
        assertEquals(ld.get().date(), LocalDate.of(2026, Month.MAY, 3),
                "Labour Day 2026 (Friday) must roll to Sunday May 3");
    }

    // Dec 16, 2028 is Saturday → rolls to Sunday Dec 17
    // Note: Dec 17 (Accession Day) also lands on Sunday 2028 — calendars permit duplicate dates
    @Test
    public void testNationalDayRollFromSaturday2028() {
        assertEquals(LocalDate.of(2028, Month.DECEMBER, 16).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> nd = findFirst("National Day", 2028);
        assertTrue(nd.isPresent());
        assertEquals(nd.get().date(), LocalDate.of(2028, Month.DECEMBER, 17),
                "National Day 2028 (Saturday) must roll to Sunday Dec 17");
    }

    // Dec 17, 2028 is Sunday — must NOT roll (Sunday is first workday in Bahrain)
    @Test
    public void testAccessionDayOnSundayNotRolled2028() {
        assertEquals(LocalDate.of(2028, Month.DECEMBER, 17).getDayOfWeek(), DayOfWeek.SUNDAY);
        Optional<HolidayDate> ad = findFirst("Accession Day", 2028);
        assertTrue(ad.isPresent());
        assertEquals(ad.get().date(), LocalDate.of(2028, Month.DECEMBER, 17),
                "Accession Day 2028 (Sunday) must not roll — Sunday is first workday in Bahrain");
    }

    // ── Islamic holiday spot-checks ───────────────────────────────────────────

    @Test
    public void testEidAlFitr2024() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2024);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2024, Month.APRIL, 10),
                "Eid al-Fitr 2024 must be 2024-04-10 per CBB official");
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
    public void testEidAlFitr2025() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2025);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2025, Month.MARCH, 30),
                "Eid al-Fitr 2025 must be 2025-03-30 per CBB official");
    }

    @Test
    public void testArafatDay2024() {
        Optional<HolidayDate> ad = findFirst("Arafat Day", 2024);
        assertTrue(ad.isPresent());
        assertEquals(ad.get().date(), LocalDate.of(2024, Month.JUNE, 15),
                "Arafat Day 2024 must be 2024-06-15 (one day before Eid al-Adha June 16)");
    }

    @Test
    public void testArafatDay2025() {
        Optional<HolidayDate> ad = findFirst("Arafat Day", 2025);
        assertTrue(ad.isPresent());
        assertEquals(ad.get().date(), LocalDate.of(2025, Month.JUNE, 5),
                "Arafat Day 2025 must be 2025-06-05 (one day before Eid al-Adha June 6)");
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
                "Eid al-Adha 2024 must be 2024-06-16 per CBB official");
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
    public void testEidAlAdha2025() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2025);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2025, Month.JUNE, 6),
                "Eid al-Adha 2025 must be 2025-06-06 per CBB official");
    }

    @Test
    public void testIslamicNewYear2024() {
        Optional<HolidayDate> iny = findFirst("Islamic New Year", 2024);
        assertTrue(iny.isPresent());
        assertEquals(iny.get().date(), LocalDate.of(2024, Month.JULY, 7),
                "Islamic New Year 2024 must be 2024-07-07 per CBB official");
    }

    @Test
    public void testIslamicNewYear2025() {
        Optional<HolidayDate> iny = findFirst("Islamic New Year", 2025);
        assertTrue(iny.isPresent());
        assertEquals(iny.get().date(), LocalDate.of(2025, Month.JUNE, 26),
                "Islamic New Year 2025 must be 2025-06-26 per CBB official");
    }

    // ── Ashura spot-checks ────────────────────────────────────────────────────

    @Test
    public void testAshura2024() {
        // Islamic New Year 2024 = Jul 7; Ashura (10 Muharram) = Jul 7 + 9 = Jul 16
        Optional<HolidayDate> ashura = findFirst("Ashura", 2024);
        assertTrue(ashura.isPresent());
        assertEquals(ashura.get().date(), LocalDate.of(2024, Month.JULY, 16),
                "Ashura 2024 must be 2024-07-16 per CBB official (Islamic New Year Jul 7 + 9 days)");
    }

    @Test
    public void testAshuraDay2_2024() {
        // Ashura Day 2 (11 Muharram) = Jul 17
        Optional<HolidayDate> ashura2 = findFirst("Ashura (2nd Day)", 2024);
        assertTrue(ashura2.isPresent());
        assertEquals(ashura2.get().date(), LocalDate.of(2024, Month.JULY, 17),
                "Ashura (2nd Day) 2024 must be 2024-07-17");
    }

    @Test
    public void testAshura2025() {
        // Islamic New Year 2025 = Jun 26; Ashura = Jun 26 + 9 = Jul 5
        Optional<HolidayDate> ashura = findFirst("Ashura", 2025);
        assertTrue(ashura.isPresent());
        assertEquals(ashura.get().date(), LocalDate.of(2025, Month.JULY, 5),
                "Ashura 2025 must be 2025-07-05 per CBB official (Islamic New Year Jun 26 + 9 days)");
    }

    @Test
    public void testAshuraDay2_2025() {
        Optional<HolidayDate> ashura2 = findFirst("Ashura (2nd Day)", 2025);
        assertTrue(ashura2.isPresent());
        assertEquals(ashura2.get().date(), LocalDate.of(2025, Month.JULY, 6),
                "Ashura (2nd Day) 2025 must be 2025-07-06");
    }

    @Test
    public void testAshuraDay2IsDayAfterAshura() {
        for (int year : new int[]{2024, 2025, 2026}) {
            Optional<HolidayDate> ashura  = findFirst("Ashura", year);
            Optional<HolidayDate> ashura2 = findFirst("Ashura (2nd Day)", year);
            assertTrue(ashura.isPresent(),  year + ": Ashura must be present");
            assertTrue(ashura2.isPresent(), year + ": Ashura (2nd Day) must be present");
            assertEquals(ashura2.get().date(), ashura.get().date().plusDays(1),
                    year + ": Ashura (2nd Day) must be exactly one day after Ashura");
        }
    }

    // ── Prophet's Birthday ────────────────────────────────────────────────────

    @Test
    public void testProphetsBirthday2024() {
        Optional<HolidayDate> pb = findFirst("Prophet's Birthday", 2024);
        assertTrue(pb.isPresent());
        assertEquals(pb.get().date(), LocalDate.of(2024, Month.SEPTEMBER, 15),
                "Prophet's Birthday 2024 must be 2024-09-15 per CBB official");
    }

    @Test
    public void testProphetsBirthday2025() {
        // Actual 12 Rabi' al-Awwal = Fri Sep 5; compensatory day announced as Thu Sep 4
        Optional<HolidayDate> pb = findFirst("Prophet's Birthday", 2025);
        assertTrue(pb.isPresent());
        assertEquals(pb.get().date(), LocalDate.of(2025, Month.SEPTEMBER, 4),
                "Prophet's Birthday 2025 must be 2025-09-04 (compensatory for Fri Sep 5) per CBB official");
    }

    // ── dataValidThrough ──────────────────────────────────────────────────────

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "BH calendar has CSV-backed holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("BH");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"BH\") must delegate to the service");
    }

    // ── CSV boundary behaviour ────────────────────────────────────────────────

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundary);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundary + ") must return holidays — within covered range");
        assertEquals(holidays.size(), BH_HOLIDAY_COUNT,
                "Expected all " + BH_HOLIDAY_COUNT + " BH holidays for boundary year " + boundary);
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
        assertEquals(holidays2056.size(), BH_FIXED_HOLIDAY_COUNT,
                "Beyond CSV ceiling only the " + BH_FIXED_HOLIDAY_COUNT +
                " fixed Bahrain holidays must remain (New Year's Day, Labour Day, National Day, Accession Day)");
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
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Eid al-Fitr (3rd Day)"},
            new Object[]{"Labour Day"},
            new Object[]{"Arafat Day"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Eid al-Adha (3rd Day)"},
            new Object[]{"Islamic New Year"},
            new Object[]{"Ashura"},
            new Object[]{"Ashura (2nd Day)"},
            new Object[]{"Prophet's Birthday"},
            new Object[]{"National Day"},
            new Object[]{"Accession Day"}
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
