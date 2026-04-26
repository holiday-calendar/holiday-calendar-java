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

public class HolidayCalendarServiceJPYTest {

    private final HolidayCalendarServiceJPY service = new HolidayCalendarServiceJPY();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("JPY"));
        assertFalse(service.isProvided("JP"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "JPY");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Japan (BOJ) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("JPY");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "JPY");
    }

    @Test
    public void testChronologicalOrder() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).getDate().isBefore(holidays.get(i - 1).getDate()), "Holidays should be in chronological order");
        }
    }

    // ── BOJ-specific operational closures ─────────────────────────────────────

    @Test
    public void testYearStartJan2() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        Optional<HolidayDate> h = findByDate(holidays, LocalDate.of(2025, Month.JANUARY, 2));
        assertTrue(h.isPresent(), "Jan 2 should be a BOJ Year-Start Holiday");
        assertEquals(h.get().getHoliday().getName(), "Year-Start Holiday");
    }

    @Test
    public void testYearStartJan3() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        Optional<HolidayDate> h = findByDate(holidays, LocalDate.of(2025, Month.JANUARY, 3));
        assertTrue(h.isPresent(), "Jan 3 should be a BOJ Year-Start Holiday");
        assertEquals(h.get().getHoliday().getName(), "Year-Start Holiday");
    }

    @Test
    public void testYearEnd() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        Optional<HolidayDate> h = findByDate(holidays, LocalDate.of(2025, Month.DECEMBER, 31));
        assertTrue(h.isPresent(), "Dec 31 should be a BOJ Year-End Holiday");
        assertEquals(h.get().getHoliday().getName(), "Year-End Holiday");
    }

    @Test
    public void testBOJClosuresAreNotRollable() {
        // Jan 2 falls on Saturday in 2021 — should NOT roll (rollable=false)
        List<HolidayDate> holidays2021 = service.getHolidayCalendar().calculate(2021);
        // Jan 2 2021 is a Saturday; verify it is NOT present (non-rollable fixed)
        // Actually: the holiday IS on Jan 2 even if it's a Saturday — rollable=false means no roll
        Optional<HolidayDate> h = findByDate(holidays2021, LocalDate.of(2021, Month.JANUARY, 2));
        assertTrue(h.isPresent(), "Jan 2 BOJ closure should appear on the Saturday itself (no roll)");
    }

    // ── Inherited base holidays are present ───────────────────────────────────

    @Test
    public void testBaseHolidaysPresent2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertTrue(findByName(holidays, "New Year's Day").isPresent());
        assertTrue(findByName(holidays, "Constitution Memorial Day").isPresent());
        assertTrue(findByName(holidays, "Children's Day").isPresent());
        assertTrue(findByName(holidays, "Culture Day").isPresent());
        assertTrue(findByName(holidays, "Labour Thanksgiving Day").isPresent());
    }

    // ── Sandwiched-day rule also applies to JPY ────────────────────────────────

    @Test
    public void testSilverWeek2009() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2009);
        Optional<HolidayDate> sandwiched = holidays.stream()
                .filter(hd -> "National Holiday".equals(hd.getHoliday().getName())
                           && hd.getDate().equals(LocalDate.of(2009, Month.SEPTEMBER, 22)))
                .findFirst();
        assertTrue(sandwiched.isPresent(), "Sep 22, 2009 should be a National Holiday in JPY calendar");
    }

    // ── 2021 Tokyo Olympics holiday relocations (issue #127) ─────────────────

    @DataProvider(name = "jpy2021OlympicsHolidays")
    public Object[][] jpy2021OlympicsHolidays() {
        return new Object[][] {
            { "Marine Day",   LocalDate.of(2021, Month.JULY,   22) },
            { "Sports Day",   LocalDate.of(2021, Month.JULY,   23) },
            { "Mountain Day", LocalDate.of(2021, Month.AUGUST,  9) },
        };
    }

    @Test(dataProvider = "jpy2021OlympicsHolidays")
    public void testJPY_2021_TokyoOlympicsRelocations(String name, LocalDate expected) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2021);
        Optional<HolidayDate> h = findByName(holidays, name);
        assertTrue(h.isPresent(), "2021: " + name + " must be present");
        assertEquals(h.get().getDate(), expected,
                "2021: " + name + " must be on relocated date, not formula date");
    }

    @DataProvider(name = "jpy2021FormulaDatesMustBeAbsent")
    public Object[][] jpy2021FormulaDatesMustBeAbsent() {
        return new Object[][] {
            { "Marine Day",   LocalDate.of(2021, Month.JULY,    19) },
            { "Sports Day",   LocalDate.of(2021, Month.OCTOBER, 11) },
            { "Mountain Day", LocalDate.of(2021, Month.AUGUST,  11) },
        };
    }

    @Test(dataProvider = "jpy2021FormulaDatesMustBeAbsent")
    public void testJPY_2021_FormulaDateAbsent(String name, LocalDate formulaDate) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2021);
        long count = holidays.stream()
                .filter(hd -> name.equals(hd.getHoliday().getName())
                           && formulaDate.equals(hd.getDate()))
                .count();
        assertEquals(count, 0L, "2021: " + name + " must NOT appear on formula date " + formulaDate);
    }

    // ── 2020 Tokyo Olympics holiday relocations (issue #132) ─────────────────

    @DataProvider(name = "jpy2020OlympicsHolidays")
    public Object[][] jpy2020OlympicsHolidays() {
        return new Object[][] {
            { "Marine Day",   LocalDate.of(2020, Month.JULY,    23) },
            { "Sports Day",   LocalDate.of(2020, Month.JULY,    24) },
            { "Mountain Day", LocalDate.of(2020, Month.AUGUST,  10) },
        };
    }

    @Test(dataProvider = "jpy2020OlympicsHolidays")
    public void testJPY_2020_TokyoOlympicsRelocations(String name, LocalDate expected) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2020);
        Optional<HolidayDate> h = findByName(holidays, name);
        assertTrue(h.isPresent(), "2020: " + name + " must be present");
        assertEquals(h.get().getDate(), expected,
                "2020: " + name + " must be on relocated date, not formula date");
    }

    @DataProvider(name = "jpy2020FormulaDatesMustBeAbsent")
    public Object[][] jpy2020FormulaDatesMustBeAbsent() {
        return new Object[][] {
            { "Marine Day",   LocalDate.of(2020, Month.JULY,    20) },
            { "Sports Day",   LocalDate.of(2020, Month.OCTOBER, 12) },
            { "Mountain Day", LocalDate.of(2020, Month.AUGUST,  11) },
        };
    }

    @Test(dataProvider = "jpy2020FormulaDatesMustBeAbsent")
    public void testJPY_2020_FormulaDateAbsent(String name, LocalDate formulaDate) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2020);
        long count = holidays.stream()
                .filter(hd -> name.equals(hd.getHoliday().getName())
                           && formulaDate.equals(hd.getDate()))
                .count();
        assertEquals(count, 0L, "2020: " + name + " must NOT appear on formula date " + formulaDate);
    }

    @DataProvider(name = "jpyBoundaryYears")
    public Object[][] jpyBoundaryYears() {
        return new Object[][] {
            { 2020, "Marine Day",   LocalDate.of(2020, Month.JULY,    23) },
            { 2020, "Sports Day",   LocalDate.of(2020, Month.JULY,    24) },
            { 2020, "Mountain Day", LocalDate.of(2020, Month.AUGUST,  10) },
            { 2022, "Marine Day",   LocalDate.of(2022, Month.JULY,    18) },
            { 2022, "Sports Day",   LocalDate.of(2022, Month.OCTOBER, 10) },
            { 2022, "Mountain Day", LocalDate.of(2022, Month.AUGUST,  11) },
        };
    }

    @Test(dataProvider = "jpyBoundaryYears")
    public void testJPY_BoundaryYears_OlympicAndFormulaYears(int year, String name, LocalDate expected) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
        Optional<HolidayDate> h = findByName(holidays, name);
        assertTrue(h.isPresent(), year + ": " + name + " must be present");
        assertEquals(h.get().getDate(), expected,
                year + ": " + name + " must be on the expected date");
    }

    @Test
    public void testJPY_NoDateDuplicatesIn2021() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2021);
        long uniqueDates = holidays.stream().map(HolidayDate::date).distinct().count();
        assertEquals(uniqueDates, holidays.size(),
                "2021: no two holidays should share a date");
    }

    // ── Saturday holidays must NOT roll (issue #125) ─────────────────────────

    @DataProvider(name = "jpySaturdayHolidays")
    public Object[][] jpySaturdayHolidays() {
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

    @Test(dataProvider = "jpySaturdayHolidays")
    public void testJPY_SaturdayHoliday_NoRoll(int year, String name, LocalDate expected) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
        Optional<HolidayDate> h = findByName(holidays, name);
        assertTrue(h.isPresent(), name + " should be present in JPY " + year);
        assertEquals(h.get().getDate(), expected,
                name + " on Saturday should remain at natural date in JPY calendar");
    }

    @Test
    public void testNewYearsDay2028_StaysJanFirst_NoClashWithBOJClosures() {
        // Jan 1 2028 is Saturday: New Year's Day stays Jan 1 (no roll).
        // BOJ Year-Start closures (Jan 2, Jan 3) are rollable=false and unaffected.
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2028);
        Optional<HolidayDate> newYearsDay = findByName(holidays, "New Year's Day");
        assertTrue(newYearsDay.isPresent());
        assertEquals(newYearsDay.get().getDate(), LocalDate.of(2028, Month.JANUARY, 1),
                "New Year's Day must stay on Jan 1 (Saturday) — no substitute");
        assertTrue(findByDate(holidays, LocalDate.of(2028, Month.JANUARY, 2)).isPresent(),
                "Jan 2 BOJ Year-Start Holiday must still be present");
        assertTrue(findByDate(holidays, LocalDate.of(2028, Month.JANUARY, 3)).isPresent(),
                "Jan 3 BOJ Year-Start Holiday must still be present");
    }

    // ── Sunday holidays still roll to Monday (regression guards) ─────────────

    @Test
    public void testNationalFoundationDaySunday2029_RollsToMonday_JPY() {
        // Feb 11 2029 is Sunday → Feb 12 (Monday)
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2029);
        Optional<HolidayDate> h = findByName(holidays, "National Foundation Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2029, Month.FEBRUARY, 12));
    }

    @Test
    public void testConstitutionMemorialDaySunday2026_CascadesToMaySixth_JPY() {
        // May 3 2026 is Sunday → naive roll to May 4 (Monday), but May 4 is already
        // Greenery Day → cascade to May 6 (Wednesday)
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2026);
        Optional<HolidayDate> h = findByName(holidays, "Constitution Memorial Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2026, Month.MAY, 6));
        // May 4 must have exactly one entry (Greenery Day only)
        long may4Count = holidays.stream()
                .filter(hd -> hd.getDate().equals(LocalDate.of(2026, Month.MAY, 4)))
                .count();
        assertEquals(may4Count, 1L, "May 4 2026 must have exactly 1 entry (Greenery Day only) in JPY");
    }

    // ── No spurious sandwiched-day entries (issue #125 secondary effect) ──────

    @Test
    public void testNoSpuriousSandwichedDay2028_JPY() {
        // Old code: Showa Day (Apr 29 Sat) rolled to May 1 → phantom National Holiday on May 2.
        // After fix: Apr 29 stays; no 2-day gap to Constitution (May 3).
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2028);
        boolean phantomMay2 = holidays.stream()
                .anyMatch(hd -> "National Holiday".equals(hd.getHoliday().getName())
                        && hd.getDate().equals(LocalDate.of(2028, Month.MAY, 2)));
        assertFalse(phantomMay2, "May 2, 2028 must not be a spurious National Holiday in JPY calendar");
    }

    @Test
    public void testNoSpuriousSandwichedDay2031_JPY() {
        // May 3=Sat (Constitution stays), May 4=Sun (middle), May 5=Mon (Children's).
        // Middle day is Sunday → detector must NOT inject a National Holiday on May 4.
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2031);
        boolean phantomMay4 = holidays.stream()
                .anyMatch(hd -> "National Holiday".equals(hd.getHoliday().getName())
                        && hd.getDate().equals(LocalDate.of(2031, Month.MAY, 4)));
        assertFalse(phantomMay4, "May 4, 2031 must not be a spurious National Holiday in JPY calendar");
    }

    // ── New Year's Day cascade when Jan 1=Sunday (JPY-specific, issue #126) ──────
    //
    // When New Year's Day (Jan 1) falls on Sunday its naive Monday substitute is
    // Jan 2, which is already a non-rollable BOJ Year-Start Holiday.  Jan 3 is
    // also a BOJ Year-Start Holiday.  The cascade advances New Year's Day to Jan 4
    // (the first free weekday), leaving Jan 2 and Jan 3 untouched.

    @DataProvider(name = "jpyNewYearCascadeYears")
    public Object[][] jpyNewYearCascadeYears() {
        return new Object[][] {
            { 2034, LocalDate.of(2034, Month.JANUARY, 4) },
            { 2040, LocalDate.of(2040, Month.JANUARY, 4) },
            { 2045, LocalDate.of(2045, Month.JANUARY, 4) },
            { 2051, LocalDate.of(2051, Month.JANUARY, 4) },
        };
    }

    @Test(dataProvider = "jpyNewYearCascadeYears")
    public void testJPY_NewYearCascade(int year, LocalDate cascaded) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
        // New Year's Day must be on the cascaded date
        Optional<HolidayDate> nyd = findByName(holidays, "New Year's Day");
        assertTrue(nyd.isPresent(), year + ": New Year's Day must be present");
        assertEquals(nyd.get().getDate(), cascaded,
                year + ": New Year's Day (Jan 1 Sun) must cascade to " + cascaded);
        // Jan 2 must have exactly one entry (BOJ Year-Start only, no duplicate)
        long jan2Count = holidays.stream()
                .filter(hd -> hd.getDate().equals(LocalDate.of(year, Month.JANUARY, 2)))
                .count();
        assertEquals(jan2Count, 1L, year + ": Jan 2 must have exactly 1 entry (BOJ Year-Start only)");
        // Jan 3 BOJ Year-Start Holiday must still be present
        assertTrue(findByDate(holidays, LocalDate.of(year, Month.JANUARY, 3)).isPresent(),
                year + ": Jan 3 BOJ Year-Start Holiday must still be present");
    }

    // ── Golden Week cascade years (inherited from JP, issue #126) ─────────────

    @DataProvider(name = "jpyGoldenWeekCascadeYears")
    public Object[][] jpyGoldenWeekCascadeYears() {
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

    @Test(dataProvider = "jpyGoldenWeekCascadeYears")
    public void testJPY_GoldenWeekCascade(int year, String name, LocalDate collision, LocalDate cascaded) {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
        // Cascaded date is present under the correct holiday name
        Optional<HolidayDate> hd = holidays.stream()
                .filter(h -> name.equals(h.getHoliday().getName()) && cascaded.equals(h.getDate()))
                .findFirst();
        assertTrue(hd.isPresent(), year + ": " + name + " must be observed on " + cascaded + " in JPY");
        // Holiday does not appear at the naive collision date
        long countAtCollision = holidays.stream()
                .filter(h -> name.equals(h.getHoliday().getName()) && collision.equals(h.getDate()))
                .count();
        assertEquals(countAtCollision, 0L, year + ": " + name + " must not appear at " + collision + " in JPY");
        // No date appears more than once in the full list
        long uniqueDates = holidays.stream().map(HolidayDate::date).distinct().count();
        assertEquals(uniqueDates, holidays.size(), year + ": no two JPY holidays should share a date");
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
