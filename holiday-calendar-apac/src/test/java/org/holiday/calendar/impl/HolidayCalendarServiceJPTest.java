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
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;

public class HolidayCalendarServiceJPTest {

    private final HolidayCalendarServiceJP service = new HolidayCalendarServiceJP();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("JP"));
        assertFalse(service.isProvided("SG"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "JP");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Japan (TSE) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("JP");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "JP");
    }

    @Test
    public void testChronologicalOrder() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).getDate().isBefore(holidays.get(i - 1).getDate()), "Holidays should be in chronological order");
        }
    }

    // ── Equinox dates ─────────────────────────────────────────────────────────

    @Test
    public void testVernalEquinox2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        Optional<HolidayDate> h = findByName(holidays, "Vernal Equinox Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2024, Month.MARCH, 20));
    }

    @Test
    public void testAutumnalEquinox2024() {
        // Sep 22, 2024 is Sunday → rolls to Monday Sep 23
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        Optional<HolidayDate> h = findByName(holidays, "Autumnal Equinox Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2024, Month.SEPTEMBER, 23));
    }

    // ── Emperor's Birthday era changes ────────────────────────────────────────

    @Test
    public void testEmperorsBirthdayShowa() {
        // Showa era: Emperor Hirohito, April 29
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(1985);
        Optional<HolidayDate> h = findByName(holidays, "Emperor's Birthday");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(1985, Month.APRIL, 29));
    }

    @Test
    public void testEmperorsBirthdayHeisei() {
        // Heisei era: Emperor Akihito, December 23
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2010);
        Optional<HolidayDate> h = findByName(holidays, "Emperor's Birthday");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2010, Month.DECEMBER, 23));
    }

    @Test
    public void testEmperorsBirthday2019Absent() {
        // 2019: no Emperor's Birthday (transition year)
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2019);
        Optional<HolidayDate> h = findByName(holidays, "Emperor's Birthday");
        assertFalse(h.isPresent(), "Emperor's Birthday should not appear in 2019");
    }

    @Test
    public void testEmperorsBirthdayReiwa() {
        // Feb 23, 2025 is Sunday → rolls to Monday Feb 24
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        Optional<HolidayDate> h = findByName(holidays, "Emperor's Birthday");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2025, Month.FEBRUARY, 24));
    }

    // ── Substitute holiday roll ────────────────────────────────────────────────

    @Test
    public void testNewYearsDayRoll2023() {
        // Jan 1 2023 is Sunday → rolls to Monday Jan 2
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2023);
        Optional<HolidayDate> h = findByName(holidays, "New Year's Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2023, Month.JANUARY, 2));
    }

    // ── Happy Monday floating holidays ────────────────────────────────────────

    @Test
    public void testComingOfAgeDayPreReform() {
        // 1999: fixed January 15
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(1999);
        Optional<HolidayDate> h = findByName(holidays, "Coming of Age Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(1999, Month.JANUARY, 15));
    }

    @Test
    public void testComingOfAgeDay2025() {
        // 2025: 2nd Monday in January = Jan 13
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        Optional<HolidayDate> h = findByName(holidays, "Coming of Age Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2025, Month.JANUARY, 13));
    }

    // ── Sandwiched-day rule ────────────────────────────────────────────────────

    @Test
    public void testSilverWeek2009() {
        // Sep 21 (Respect for the Aged Day) and Sep 23 (Autumnal Equinox) → Sep 22 is sandwiched
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2009);
        Optional<HolidayDate> sandwiched = holidays.stream()
                .filter(hd -> "National Holiday".equals(hd.getHoliday().getName())
                           && hd.getDate().equals(LocalDate.of(2009, Month.SEPTEMBER, 22)))
                .findFirst();
        assertTrue(sandwiched.isPresent(), "Sep 22, 2009 should be a National Holiday (Silver Week)");
    }

    @Test
    public void test2019ImperialTransition() {
        // Apr 30 (Abdication) and May 2 (sandwiched between Apr 30 and May 3) and May 1 (Enthronement)
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2019);
        assertTrue(findByDate(holidays, LocalDate.of(2019, Month.APRIL, 30)).isPresent(),
                   "Apr 30, 2019 (Abdication Day) should be a holiday");
        assertTrue(findByDate(holidays, LocalDate.of(2019, Month.MAY, 1)).isPresent(),
                   "May 1, 2019 (Enthronement Day) should be a holiday");
        assertTrue(findByDate(holidays, LocalDate.of(2019, Month.MAY, 2)).isPresent(),
                   "May 2, 2019 should be a sandwiched National Holiday");
    }

    // ── Year-bounded holidays ─────────────────────────────────────────────────

    @Test
    public void testMountainDayAbsentBefore2016() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2015);
        assertFalse(findByName(holidays, "Mountain Day").isPresent(),
                    "Mountain Day should not appear before 2016");
    }

    @Test
    public void testMountainDay2016() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2016);
        Optional<HolidayDate> h = findByName(holidays, "Mountain Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2016, Month.AUGUST, 11));
    }

    @Test
    public void testMarineDayAbsentBefore1996() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(1995);
        assertFalse(findByName(holidays, "Marine Day").isPresent(),
                    "Marine Day should not appear before 1996");
    }

    // ── 2021 Tokyo Olympics holiday relocations (issue #127) ─────────────────

    @DataProvider(name = "jp2021OlympicsHolidays")
    public Object[][] jp2021OlympicsHolidays() {
        return new Object[][] {
            { "Marine Day",   LocalDate.of(2021, Month.JULY,    22) },
            { "Sports Day",   LocalDate.of(2021, Month.JULY,    23) },
            { "Mountain Day", LocalDate.of(2021, Month.AUGUST,   9) },
        };
    }

    @Test(dataProvider = "jp2021OlympicsHolidays")
    public void testJP_2021_TokyoOlympicsRelocations(String name, LocalDate expected) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2021);
        Optional<HolidayDate> h = findByName(holidays, name);
        assertTrue(h.isPresent(), "2021: " + name + " must be present");
        assertEquals(h.get().getDate(), expected,
                "2021: " + name + " must be on relocated date, not formula date");
    }

    @DataProvider(name = "jp2021FormulaDatesMustBeAbsent")
    public Object[][] jp2021FormulaDatesMustBeAbsent() {
        return new Object[][] {
            { "Marine Day",   LocalDate.of(2021, Month.JULY,    19) },
            { "Sports Day",   LocalDate.of(2021, Month.OCTOBER, 11) },
            { "Mountain Day", LocalDate.of(2021, Month.AUGUST,  11) },
        };
    }

    @Test(dataProvider = "jp2021FormulaDatesMustBeAbsent")
    public void testJP_2021_FormulaDateAbsent(String name, LocalDate formulaDate) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2021);
        long count = holidays.stream()
                .filter(hd -> name.equals(hd.getHoliday().getName())
                           && formulaDate.equals(hd.getDate()))
                .count();
        assertEquals(count, 0L, "2021: " + name + " must NOT appear on formula date " + formulaDate);
    }

    @DataProvider(name = "jpBoundaryYears")
    public Object[][] jpBoundaryYears() {
        return new Object[][] {
            { 2020, "Marine Day",   LocalDate.of(2020, Month.JULY,    20) },
            { 2020, "Sports Day",   LocalDate.of(2020, Month.OCTOBER, 12) },
            { 2020, "Mountain Day", LocalDate.of(2020, Month.AUGUST,  11) },
            { 2022, "Marine Day",   LocalDate.of(2022, Month.JULY,    18) },
            { 2022, "Sports Day",   LocalDate.of(2022, Month.OCTOBER, 10) },
            { 2022, "Mountain Day", LocalDate.of(2022, Month.AUGUST,  11) },
        };
    }

    @Test(dataProvider = "jpBoundaryYears")
    public void testJP_BoundaryYears_FormulaUnchanged(int year, String name, LocalDate expected) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
        Optional<HolidayDate> h = findByName(holidays, name);
        assertTrue(h.isPresent(), year + ": " + name + " must be present");
        assertEquals(h.get().getDate(), expected,
                year + ": " + name + " must use formula, not 2021 override");
    }

    @Test
    public void testJP_NoDateDuplicatesIn2021() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2021);
        long uniqueDates = holidays.stream().map(HolidayDate::date).distinct().count();
        assertEquals(uniqueDates, (long) holidays.size(),
                "2021: no two holidays should share a date");
    }

    // ── Saturday holidays must NOT roll (issue #125) ─────────────────────────

    @Test
    public void testNewYearsDaySaturday2028_NoRoll() {
        // Jan 1 2028 is Saturday — no substitute under Japanese law
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2028);
        Optional<HolidayDate> h = findByName(holidays, "New Year's Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2028, Month.JANUARY, 1));
    }

    @Test
    public void testShowaDaySaturday2028_NoRoll() {
        // Apr 29 2028 is Saturday
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2028);
        Optional<HolidayDate> h = findByName(holidays, "Showa Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2028, Month.APRIL, 29));
    }

    @Test
    public void testChildrensDaySaturday2029_NoRoll() {
        // May 5 2029 is Saturday
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2029);
        Optional<HolidayDate> h = findByName(holidays, "Children's Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2029, Month.MAY, 5));
    }

    @Test
    public void testEmperorsBirthdaySaturday2030_NoRoll() {
        // Feb 23 2030 is Saturday
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2030);
        Optional<HolidayDate> h = findByName(holidays, "Emperor's Birthday");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2030, Month.FEBRUARY, 23));
    }

    @Test
    public void testConstitutionMemorialDaySaturday2031_NoRoll() {
        // May 3 2031 is Saturday
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2031);
        Optional<HolidayDate> h = findByName(holidays, "Constitution Memorial Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2031, Month.MAY, 3));
    }

    @Test
    public void testNewYearsDaySaturday2033_NoRoll() {
        // Jan 1 2033 is Saturday
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2033);
        Optional<HolidayDate> h = findByName(holidays, "New Year's Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2033, Month.JANUARY, 1));
    }

    @Test
    public void testAutumnalEquinox2034_Saturday_NoRoll() {
        // Sep 23 2034 is Saturday — equinox dates computed via astronomical formula
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2034);
        Optional<HolidayDate> h = findByName(holidays, "Autumnal Equinox Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2034, Month.SEPTEMBER, 23));
    }

    @DataProvider(name = "jpSaturdayHolidays")
    public Object[][] jpSaturdayHolidays() {
        return new Object[][] {
            { 2028, "New Year's Day",             LocalDate.of(2028, 1,  1)  },
            { 2028, "Showa Day",                  LocalDate.of(2028, 4,  29) },
            { 2029, "Children's Day",             LocalDate.of(2029, 5,  5)  },
            { 2030, "Emperor's Birthday",         LocalDate.of(2030, 2,  23) },
            { 2031, "Constitution Memorial Day",  LocalDate.of(2031, 5,  3)  },
            { 2033, "New Year's Day",             LocalDate.of(2033, 1,  1)  },
            { 2034, "National Foundation Day",    LocalDate.of(2034, 2,  11) },
            { 2034, "Autumnal Equinox Day",       LocalDate.of(2034, 9,  23) },
        };
    }

    @Test(dataProvider = "jpSaturdayHolidays")
    public void testJP_SaturdayHoliday_NoRoll(int year, String name, LocalDate expected) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
        Optional<HolidayDate> h = findByName(holidays, name);
        assertTrue(h.isPresent(), name + " should be present in " + year);
        assertEquals(h.get().getDate(), expected,
                name + " on Saturday should remain at natural date");
    }

    // ── Sunday holidays still roll to Monday (regression guards) ─────────────

    @Test
    public void testNationalFoundationDaySunday2029_RollsToMonday() {
        // Feb 11 2029 is Sunday → Feb 12 (Monday)
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2029);
        Optional<HolidayDate> h = findByName(holidays, "National Foundation Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2029, Month.FEBRUARY, 12));
    }

    @Test
    public void testShowaDaySunday2029_RollsToMonday() {
        // Apr 29 2029 is Sunday → Apr 30 (Monday)
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2029);
        Optional<HolidayDate> h = findByName(holidays, "Showa Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2029, Month.APRIL, 30));
    }

    @Test
    public void testConstitutionMemorialDaySunday2026_CascadesToMayFifth() {
        // May 3 2026 is Sunday → naive roll to May 4 (Monday), but May 4 is already
        // Greenery Day → cascade to May 6 (Wednesday)
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2026);
        Optional<HolidayDate> h = findByName(holidays, "Constitution Memorial Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2026, Month.MAY, 6));
        // Greenery Day stays on May 4; May 4 must have exactly one entry
        long may4Count = holidays.stream()
                .filter(hd -> hd.getDate().equals(LocalDate.of(2026, Month.MAY, 4)))
                .count();
        assertEquals(may4Count, 1L, "May 4 2026 must have exactly 1 entry (Greenery Day only)");
    }

    // ── No spurious sandwiched-day entries (issue #125 secondary effect) ──────

    @Test
    public void testNoSpuriousSandwichedDay2028() {
        // Old code: Showa Day (Apr 29 Sat) rolled to May 1; gap to Constitution (May 3) = 2 days
        // → phantom National Holiday on May 2 (Tuesday). After fix: Apr 29 stays; no 2-day gap.
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2028);
        boolean phantomMay2 = holidays.stream()
                .anyMatch(hd -> "National Holiday".equals(hd.getHoliday().getName())
                        && hd.getDate().equals(LocalDate.of(2028, Month.MAY, 2)));
        assertFalse(phantomMay2, "May 2, 2028 must not be a spurious National Holiday");
    }

    @Test
    public void testNoSpuriousSandwichedDay2031() {
        // May 3 (Sat) stays, May 4 (Sun) is the middle, May 5 (Mon) is Children's Day.
        // Detector sees Sunday in the gap → must NOT inject a National Holiday on May 4.
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2031);
        boolean phantomMay4 = holidays.stream()
                .anyMatch(hd -> "National Holiday".equals(hd.getHoliday().getName())
                        && hd.getDate().equals(LocalDate.of(2031, Month.MAY, 4)));
        assertFalse(phantomMay4, "May 4, 2031 must not be a spurious National Holiday (middle day is Sunday)");
    }

    @Test
    public void testGoldenWeek2031_GreeneryDayCascadesToMaySixth() {
        // May 3=Sat (Constitution stays), May 4=Sun (Greenery naive roll → May 5), May 5=Mon (Children's)
        // → collision: Greenery cascades to May 6 (Wednesday)
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2031);
        Optional<HolidayDate> greenery = findByName(holidays, "Greenery Day");
        assertTrue(greenery.isPresent());
        assertEquals(greenery.get().getDate(), LocalDate.of(2031, Month.MAY, 6),
                "Greenery Day (May 4 Sun) must cascade to May 6 — May 5 is already Children's Day");
        // May 5 must have exactly one entry (Children's Day only)
        long may5Count = holidays.stream()
                .filter(hd -> hd.getDate().equals(LocalDate.of(2031, Month.MAY, 5)))
                .count();
        assertEquals(may5Count, 1L, "May 5 2031 must have exactly 1 entry (Children's Day only)");
    }

    // ── Cascading substitute holiday rule — issue #126 ────────────────────────

    @DataProvider(name = "jpGoldenWeekCascadeYears")
    public Object[][] jpGoldenWeekCascadeYears() {
        return new Object[][] {
            { 2026, "Constitution Memorial Day", LocalDate.of(2026, Month.MAY, 4), LocalDate.of(2026, Month.MAY, 6) },
            { 2031, "Greenery Day",              LocalDate.of(2031, Month.MAY, 5), LocalDate.of(2031, Month.MAY, 6) },
            { 2036, "Greenery Day",              LocalDate.of(2036, Month.MAY, 5), LocalDate.of(2036, Month.MAY, 6) },
            { 2037, "Constitution Memorial Day", LocalDate.of(2037, Month.MAY, 4), LocalDate.of(2037, Month.MAY, 6) },
            { 2042, "Greenery Day",              LocalDate.of(2042, Month.MAY, 5), LocalDate.of(2042, Month.MAY, 6) },
            { 2043, "Constitution Memorial Day", LocalDate.of(2043, Month.MAY, 4), LocalDate.of(2043, Month.MAY, 6) },
            { 2048, "Constitution Memorial Day", LocalDate.of(2048, Month.MAY, 4), LocalDate.of(2048, Month.MAY, 6) },
            { 2053, "Greenery Day",              LocalDate.of(2053, Month.MAY, 5), LocalDate.of(2053, Month.MAY, 6) },
            { 2054, "Constitution Memorial Day", LocalDate.of(2054, Month.MAY, 4), LocalDate.of(2054, Month.MAY, 6) },
        };
    }

    @Test(dataProvider = "jpGoldenWeekCascadeYears")
    public void testJP_GoldenWeekCascade(int year, String name, LocalDate collision, LocalDate cascaded) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
        // Cascaded date is present under the correct holiday name
        Optional<HolidayDate> hd = holidays.stream()
                .filter(h -> name.equals(h.getHoliday().getName()) && cascaded.equals(h.getDate()))
                .findFirst();
        assertTrue(hd.isPresent(), year + ": " + name + " must be observed on " + cascaded);
        // Holiday does not appear at the naive collision date
        long countAtCollision = holidays.stream()
                .filter(h -> name.equals(h.getHoliday().getName()) && collision.equals(h.getDate()))
                .count();
        assertEquals(countAtCollision, 0L, year + ": " + name + " must not appear at " + collision);
        // No date appears more than once in the full list
        long uniqueDates = holidays.stream().map(HolidayDate::date).distinct().count();
        assertEquals(uniqueDates, holidays.size(), year + ": no two holidays should share a date");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Optional<HolidayDate> findByName(List<HolidayDate> holidays, String name) {
        return holidays.stream()
                       .filter(hd -> name.equals(hd.getHoliday().getName()))
                       .findFirst();
    }

    private Optional<HolidayDate> findByDate(List<HolidayDate> holidays, LocalDate date) {
        return holidays.stream()
                       .filter(hd -> date.equals(hd.getDate()))
                       .findFirst();
    }
}
