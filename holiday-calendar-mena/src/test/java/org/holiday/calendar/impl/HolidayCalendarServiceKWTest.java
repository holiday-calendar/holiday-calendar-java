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

public class HolidayCalendarServiceKWTest {

    // 3 fixed (New Year's, National Day, Liberation Day)
    // + Isra and Mi'raj + 3 Eid al-Fitr + 3 Eid al-Adha + Arafat Day
    // + Islamic New Year + Prophet's Birthday = 13
    private static final int KW_HOLIDAY_COUNT = 13;

    // Fixed holidays survive beyond the CSV ceiling (New Year's, National Day, Liberation Day)
    private static final int KW_FIXED_HOLIDAY_COUNT = 3;

    private final HolidayCalendarServiceKW service = new HolidayCalendarServiceKW();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // ── service identity ──────────────────────────────────────────────────────

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("KW"));
        assertFalse(service.isProvided("KWD"));
        assertFalse(service.isProvided("QA"));
        assertFalse(service.isProvided("SA"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "KW");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Kuwait (National) Holidays");
    }

    // ── factory integration ───────────────────────────────────────────────────

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("KW");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "KW");
    }

    // ── weekend configuration ─────────────────────────────────────────────────

    @Test
    public void testWeekendDaysFridayAndSaturday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2, "Kuwait weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY), "Friday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY), "Saturday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY), "Sunday must not be a weekend day");
    }

    // ── total count ───────────────────────────────────────────────────────────

    @Test
    public void testCalculate2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        assertNotNull(holidays);
        assertEquals(holidays.size(), KW_HOLIDAY_COUNT,
                "Expected " + KW_HOLIDAY_COUNT + " holidays for 2024, got: " + holidays.size());
    }

    @Test
    public void testCalculate2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), KW_HOLIDAY_COUNT,
                "Expected " + KW_HOLIDAY_COUNT + " holidays for 2025, got: " + holidays.size());
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

    // Feb 25, 2024 is Sunday — must not roll
    @Test
    public void testNationalDay2024NoRoll() {
        assertEquals(LocalDate.of(2024, Month.FEBRUARY, 25).getDayOfWeek(), DayOfWeek.SUNDAY);
        Optional<HolidayDate> nd = findFirst("National Day", 2024);
        assertTrue(nd.isPresent());
        assertEquals(nd.get().date(), LocalDate.of(2024, Month.FEBRUARY, 25),
                "National Day 2024 (Sunday) must not roll");
    }

    // Feb 26, 2024 is Monday — must not roll
    @Test
    public void testLiberationDay2024NoRoll() {
        assertEquals(LocalDate.of(2024, Month.FEBRUARY, 26).getDayOfWeek(), DayOfWeek.MONDAY);
        Optional<HolidayDate> ld = findFirst("Liberation Day", 2024);
        assertTrue(ld.isPresent());
        assertEquals(ld.get().date(), LocalDate.of(2024, Month.FEBRUARY, 26),
                "Liberation Day 2024 (Monday) must not roll");
    }

    // Jan 1, 2025 is Wednesday — must not roll
    @Test
    public void testNewYearsDay2025NoRoll() {
        assertEquals(LocalDate.of(2025, Month.JANUARY, 1).getDayOfWeek(), DayOfWeek.WEDNESDAY);
        Optional<HolidayDate> ny = findFirst("New Year's Day", 2025);
        assertTrue(ny.isPresent());
        assertEquals(ny.get().date(), LocalDate.of(2025, Month.JANUARY, 1),
                "New Year's Day 2025 (Wednesday) must not roll");
    }

    // ── fixed holidays — Friday/Saturday roll to following Sunday ─────────────

    // Feb 25, 2028 is Friday → rolls to Sunday Feb 27
    @Test
    public void testNationalDayRollFromFriday2028() {
        assertEquals(LocalDate.of(2028, Month.FEBRUARY, 25).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> nd = findFirst("National Day", 2028);
        assertTrue(nd.isPresent());
        assertEquals(nd.get().date(), LocalDate.of(2028, Month.FEBRUARY, 27),
                "National Day 2028 (Friday) must roll to Sunday Feb 27");
    }

    // Feb 26, 2028 is Saturday → rolls to Sunday Feb 27; combined with National Day roll,
    // both fixed holidays land on the same Sunday — assert total count is unchanged
    @Test
    public void testNoDuplicateDatesWhenNationalAndLiberationDayBothRoll2028() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2028);
        // Rolling does not deduplicate — all 11 holidays must still be returned even when
        // multiple land on the same date (Eid al-Fitr Day 2 also falls on 2028-02-27)
        assertEquals(holidays.size(), KW_HOLIDAY_COUNT,
                "2028 must still return " + KW_HOLIDAY_COUNT + " holidays even when multiple holidays share a date");
        // Both fixed holidays must resolve to the same Sunday
        Optional<HolidayDate> nd = findFirst("National Day", 2028);
        Optional<HolidayDate> ld = findFirst("Liberation Day", 2028);
        assertTrue(nd.isPresent());
        assertTrue(ld.isPresent());
        assertEquals(nd.get().date(), LocalDate.of(2028, Month.FEBRUARY, 27),
                "National Day 2028 (Friday) must roll to Sunday Feb 27");
        assertEquals(ld.get().date(), LocalDate.of(2028, Month.FEBRUARY, 27),
                "Liberation Day 2028 (Saturday) must roll to Sunday Feb 27");
    }

    // Feb 26, 2027 is Friday → rolls to Sunday Feb 28
    @Test
    public void testLiberationDayRollFromFriday2027() {
        assertEquals(LocalDate.of(2027, Month.FEBRUARY, 26).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> ld = findFirst("Liberation Day", 2027);
        assertTrue(ld.isPresent());
        assertEquals(ld.get().date(), LocalDate.of(2027, Month.FEBRUARY, 28),
                "Liberation Day 2027 (Friday) must roll to Sunday Feb 28");
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
                "Eid al-Fitr 2024 must be 2024-04-10 per CBK official");
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
                "Eid al-Adha 2024 must be 2024-06-16 per CBK official");
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
                "Eid al-Fitr 2025 must be 2025-03-30 per CBK official");
    }

    @Test
    public void testEidAlAdha2025() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2025);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2025, Month.JUNE, 6),
                "Eid al-Adha 2025 must be 2025-06-06 per CBK official");
    }

    // ── Isra and Mi'raj spot-checks ───────────────────────────────────────────

    @Test
    public void testIsraMiraj2024() {
        Optional<HolidayDate> im = findFirst("Isra and Mi'raj", 2024);
        assertTrue(im.isPresent());
        assertEquals(im.get().date(), LocalDate.of(2024, Month.FEBRUARY, 8),
                "Isra and Mi'raj 2024 must be 2024-02-08 per CBK official");
    }

    @Test
    public void testIsraMiraj2025() {
        Optional<HolidayDate> im = findFirst("Isra and Mi'raj", 2025);
        assertTrue(im.isPresent());
        assertEquals(im.get().date(), LocalDate.of(2025, Month.JANUARY, 27),
                "Isra and Mi'raj 2025 must be 2025-01-27 per CBK official");
    }

    // ── Arafat Day spot-checks ────────────────────────────────────────────────

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

    // ── Islamic New Year and Prophet's Birthday are gazetted in Kuwait ────────

    @Test
    public void testIslamicNewYearIncluded() {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> "Islamic New Year".equals(h.getName()));
        assertTrue(found, "Islamic New Year is a gazetted Kuwait public holiday and must be included");
    }

    @Test
    public void testProphetsBirthdayIncluded() {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> "Prophet's Birthday".equals(h.getName()));
        assertTrue(found, "Prophet's Birthday is a gazetted Kuwait public holiday and must be included");
    }

    @Test
    public void testIslamicNewYear2024() {
        Optional<HolidayDate> iny = findFirst("Islamic New Year", 2024);
        assertTrue(iny.isPresent());
        assertEquals(iny.get().date(), LocalDate.of(2024, Month.JULY, 7),
                "Islamic New Year 2024 must be 2024-07-07 per CBK official");
    }

    @Test
    public void testProphetsBirthday2024() {
        Optional<HolidayDate> pb = findFirst("Prophet's Birthday", 2024);
        assertTrue(pb.isPresent());
        assertEquals(pb.get().date(), LocalDate.of(2024, Month.SEPTEMBER, 15),
                "Prophet's Birthday 2024 must be 2024-09-15 per CBK official");
    }

    // ── dataValidThrough ──────────────────────────────────────────────────────

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "KW calendar has CSV-backed holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("KW");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"KW\") must delegate to the service");
    }

    // ── CSV boundary behaviour ────────────────────────────────────────────────

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundary);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundary + ") must return holidays — within covered range");
        assertEquals(holidays.size(), KW_HOLIDAY_COUNT,
                "Expected all " + KW_HOLIDAY_COUNT + " KW holidays for boundary year " + boundary);
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
        assertEquals(holidays2056.size(), KW_FIXED_HOLIDAY_COUNT,
                "Beyond CSV ceiling only the " + KW_FIXED_HOLIDAY_COUNT +
                " fixed Kuwait holidays must remain");
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
            new Object[]{"National Day"},
            new Object[]{"Liberation Day"},
            new Object[]{"Isra and Mi'raj"},
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Eid al-Fitr (3rd Day)"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Eid al-Adha (3rd Day)"},
            new Object[]{"Arafat Day"},
            new Object[]{"Islamic New Year"},
            new Object[]{"Prophet's Birthday"}
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
