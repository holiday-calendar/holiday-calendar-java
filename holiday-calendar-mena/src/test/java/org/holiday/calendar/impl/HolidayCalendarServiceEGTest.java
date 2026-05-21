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

public class HolidayCalendarServiceEGTest {

    // 7 fixed + Sham El-Nessim + Arafat Day + 3 Eid al-Fitr + 3 Eid al-Adha
    // + Islamic New Year + Prophet's Birthday = 17
    private static final int EG_HOLIDAY_COUNT = 17;

    // Beyond CSV ceiling: 7 fixed Gregorian + Sham El-Nessim (algorithm-based, never drops) = 8
    private static final int EG_FIXED_HOLIDAY_COUNT = 8;

    private final HolidayCalendarServiceEG service = new HolidayCalendarServiceEG();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // ── service identity ──────────────────────────────────────────────────────

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("EG"));
        assertFalse(service.isProvided("EGP"));
        assertFalse(service.isProvided("KW"));
        assertFalse(service.isProvided("SA"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "EG");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Egypt (National) Holidays");
    }

    // ── factory integration ───────────────────────────────────────────────────

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("EG");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "EG");
    }

    // ── weekend configuration ─────────────────────────────────────────────────

    @Test
    public void testWeekendDaysFridayAndSaturday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2, "Egypt weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY), "Friday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY), "Saturday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY), "Sunday must not be a weekend day in Egypt");
    }

    // ── total count ───────────────────────────────────────────────────────────

    @Test
    public void testCalculate2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        assertNotNull(holidays);
        assertEquals(holidays.size(), EG_HOLIDAY_COUNT,
                "Expected " + EG_HOLIDAY_COUNT + " holidays for 2024, got: " + holidays.size());
    }

    @Test
    public void testCalculate2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), EG_HOLIDAY_COUNT,
                "Expected " + EG_HOLIDAY_COUNT + " holidays for 2025, got: " + holidays.size());
    }

    @Test
    public void testCalculate2026() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2026);
        assertNotNull(holidays);
        assertEquals(holidays.size(), EG_HOLIDAY_COUNT,
                "Expected " + EG_HOLIDAY_COUNT + " holidays for 2026, got: " + holidays.size());
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

    // Jan 7, 2025 is Tuesday — must not roll
    @Test
    public void testCopticChristmas2025NoRoll() {
        assertEquals(LocalDate.of(2025, Month.JANUARY, 7).getDayOfWeek(), DayOfWeek.TUESDAY);
        Optional<HolidayDate> cc = findFirst("Coptic Christmas", 2025);
        assertTrue(cc.isPresent());
        assertEquals(cc.get().date(), LocalDate.of(2025, Month.JANUARY, 7),
                "Coptic Christmas 2025 (Tuesday) must not roll");
    }

    // Jun 30, 2025 is Monday — must not roll
    @Test
    public void testJune30Revolution2025NoRoll() {
        assertEquals(LocalDate.of(2025, Month.JUNE, 30).getDayOfWeek(), DayOfWeek.MONDAY);
        Optional<HolidayDate> j30 = findFirst("June 30 Revolution", 2025);
        assertTrue(j30.isPresent());
        assertEquals(j30.get().date(), LocalDate.of(2025, Month.JUNE, 30),
                "June 30 Revolution 2025 (Monday) must not roll");
    }

    // ── fixed holidays — Friday/Saturday roll to following Sunday ─────────────

    // Jan 7, 2028 is Friday → rolls to Sunday Jan 9
    @Test
    public void testCopticChristmasRollFromFriday2028() {
        assertEquals(LocalDate.of(2028, Month.JANUARY, 7).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> cc = findFirst("Coptic Christmas", 2028);
        assertTrue(cc.isPresent());
        assertEquals(cc.get().date(), LocalDate.of(2028, Month.JANUARY, 9),
                "Coptic Christmas 2028 (Friday) must roll to Sunday Jan 9");
    }

    // Jan 25, 2025 is Saturday → rolls to Sunday Jan 26
    @Test
    public void testRevolutionDayRollFromSaturday2025() {
        assertEquals(LocalDate.of(2025, Month.JANUARY, 25).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> rd = findFirst("Revolution Day", 2025);
        assertTrue(rd.isPresent());
        assertEquals(rd.get().date(), LocalDate.of(2025, Month.JANUARY, 26),
                "Revolution Day 2025 (Saturday) must roll to Sunday Jan 26");
    }

    // Apr 25, 2026 is Saturday → rolls to Sunday Apr 26
    @Test
    public void testSinaiLiberationDayRollFromSaturday2026() {
        assertEquals(LocalDate.of(2026, Month.APRIL, 25).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> sld = findFirst("Sinai Liberation Day", 2026);
        assertTrue(sld.isPresent());
        assertEquals(sld.get().date(), LocalDate.of(2026, Month.APRIL, 26),
                "Sinai Liberation Day 2026 (Saturday) must roll to Sunday Apr 26");
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

    // Jul 23, 2028 is Sunday — must not roll (Sunday is a business day in Egypt)
    @Test
    public void testRevolutionDayJuly2028NoRoll() {
        assertEquals(LocalDate.of(2028, Month.JULY, 23).getDayOfWeek(), DayOfWeek.SUNDAY);
        Optional<HolidayDate> rd2 = findFirst("Revolution Day (July 23)", 2028);
        assertTrue(rd2.isPresent());
        assertEquals(rd2.get().date(), LocalDate.of(2028, Month.JULY, 23),
                "Revolution Day (July 23) 2028 (Sunday) must not roll — Sunday is a business day in Egypt");
    }

    // Jul 23, 2027 is Friday → rolls to Sunday Jul 25
    @Test
    public void testRevolutionDayJulyRollFromFriday2027() {
        assertEquals(LocalDate.of(2027, Month.JULY, 23).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> rd2 = findFirst("Revolution Day (July 23)", 2027);
        assertTrue(rd2.isPresent());
        assertEquals(rd2.get().date(), LocalDate.of(2027, Month.JULY, 25),
                "Revolution Day (July 23) 2027 (Friday) must roll to Sunday Jul 25");
    }

    // Oct 6, 2029 is Saturday → rolls to Sunday Oct 7
    @Test
    public void testArmedForcesDayRollFromSaturday2029() {
        assertEquals(LocalDate.of(2029, Month.OCTOBER, 6).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> afd = findFirst("Armed Forces Day", 2029);
        assertTrue(afd.isPresent());
        assertEquals(afd.get().date(), LocalDate.of(2029, Month.OCTOBER, 7),
                "Armed Forces Day 2029 (Saturday) must roll to Sunday Oct 7");
    }

    // ── Sham El-Nessim spot-checks (day after Coptic/Orthodox Easter) ─────────

    @Test
    public void testShamElNessim2024() {
        // Coptic Easter 2024 = May 5 (Orthodox Easter algorithm); Sham El-Nessim = May 6
        Optional<HolidayDate> sen = findFirst("Sham El-Nessim", 2024);
        assertTrue(sen.isPresent());
        assertEquals(sen.get().date(), LocalDate.of(2024, Month.MAY, 6),
                "Sham El-Nessim 2024 must be May 6 (day after Coptic Easter May 5)");
    }

    @Test
    public void testShamElNessim2025() {
        // Coptic Easter 2025 = April 20; Sham El-Nessim = April 21
        Optional<HolidayDate> sen = findFirst("Sham El-Nessim", 2025);
        assertTrue(sen.isPresent());
        assertEquals(sen.get().date(), LocalDate.of(2025, Month.APRIL, 21),
                "Sham El-Nessim 2025 must be April 21 (day after Coptic Easter April 20)");
    }

    @Test
    public void testShamElNessim2026() {
        // Coptic Easter 2026 = April 12; Sham El-Nessim = April 13
        Optional<HolidayDate> sen = findFirst("Sham El-Nessim", 2026);
        assertTrue(sen.isPresent());
        assertEquals(sen.get().date(), LocalDate.of(2026, Month.APRIL, 13),
                "Sham El-Nessim 2026 must be April 13 (day after Coptic Easter April 12)");
    }

    // ── Islamic holiday spot-checks ───────────────────────────────────────────

    @Test
    public void testEidAlFitr2024() {
        Optional<HolidayDate> eid = findFirst("Eid al-Fitr", 2024);
        assertTrue(eid.isPresent());
        assertEquals(eid.get().date(), LocalDate.of(2024, Month.APRIL, 10),
                "Eid al-Fitr 2024 must be 2024-04-10 per CBE official");
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
                "Eid al-Fitr 2025 must be 2025-03-30 per CBE official");
    }

    @Test
    public void testEidAlAdha2024() {
        Optional<HolidayDate> adha = findFirst("Eid al-Adha", 2024);
        assertTrue(adha.isPresent());
        assertEquals(adha.get().date(), LocalDate.of(2024, Month.JUNE, 16),
                "Eid al-Adha 2024 must be 2024-06-16 per CBE official");
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
                "Eid al-Adha 2025 must be 2025-06-06 per CBE official");
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

    // ── Islamic New Year and Prophet's Birthday ───────────────────────────────

    @Test
    public void testIslamicNewYearIncluded() {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> "Islamic New Year".equals(h.getName()));
        assertTrue(found, "Islamic New Year is a gazetted Egypt public holiday and must be included");
    }

    @Test
    public void testProphetsBirthdayIncluded() {
        boolean found = service.getHolidayCalendar().getHolidays().stream()
                .anyMatch(h -> "Prophet's Birthday".equals(h.getName()));
        assertTrue(found, "Prophet's Birthday is a gazetted Egypt public holiday and must be included");
    }

    @Test
    public void testIslamicNewYear2024() {
        Optional<HolidayDate> iny = findFirst("Islamic New Year", 2024);
        assertTrue(iny.isPresent());
        assertEquals(iny.get().date(), LocalDate.of(2024, Month.JULY, 7),
                "Islamic New Year 2024 must be 2024-07-07 per CBE official");
    }

    @Test
    public void testProphetsBirthday2024() {
        Optional<HolidayDate> pb = findFirst("Prophet's Birthday", 2024);
        assertTrue(pb.isPresent());
        assertEquals(pb.get().date(), LocalDate.of(2024, Month.SEPTEMBER, 15),
                "Prophet's Birthday 2024 must be 2024-09-15 per CBE official");
    }

    // ── dataValidThrough ──────────────────────────────────────────────────────

    @Test
    public void testDataValidThroughReturnsPresent() {
        assertTrue(service.dataValidThrough().isPresent(),
                "EG calendar has CSV-backed holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("EG");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"EG\") must delegate to the service");
    }

    // ── CSV boundary behaviour ────────────────────────────────────────────────

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundary = service.dataValidThrough().orElseThrow();
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(boundary);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundary + ") must return holidays — within covered range");
        assertEquals(holidays.size(), EG_HOLIDAY_COUNT,
                "Expected all " + EG_HOLIDAY_COUNT + " EG holidays for boundary year " + boundary);
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
    public void testFixedAndCopticHolidaysPresentBeyondCeiling() {
        List<HolidayDate> holidays2056 = service.getHolidayCalendar().calculate(2056);
        assertEquals(holidays2056.size(), EG_FIXED_HOLIDAY_COUNT,
                "Beyond CSV ceiling only the " + EG_FIXED_HOLIDAY_COUNT +
                " fixed and algorithmic EG holidays must remain (7 Gregorian + Sham El-Nessim)");
    }

    @Test
    public void testShamElNessimPresentBeyondCeiling() {
        List<HolidayDate> holidays2056 = service.getHolidayCalendar().calculate(2056);
        boolean found = holidays2056.stream().anyMatch(hd -> "Sham El-Nessim".equals(hd.holiday().getName()));
        assertTrue(found, "Sham El-Nessim must be present beyond CSV ceiling — it is algorithm-based, not CSV-based");
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
            new Object[]{"Coptic Christmas"},
            new Object[]{"Revolution Day"},
            new Object[]{"Sinai Liberation Day"},
            new Object[]{"Sham El-Nessim"},
            new Object[]{"Labour Day"},
            new Object[]{"June 30 Revolution"},
            new Object[]{"Revolution Day (July 23)"},
            new Object[]{"Armed Forces Day"},
            new Object[]{"Arafat Day"},
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Eid al-Fitr (3rd Day)"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Eid al-Adha (3rd Day)"},
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
