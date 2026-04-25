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
    public void testConstitutionMemorialDaySunday2026_RollsToMonday_JPY() {
        // May 3 2026 is Sunday → May 4 (Monday)
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2026);
        Optional<HolidayDate> h = findByName(holidays, "Constitution Memorial Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2026, Month.MAY, 4));
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

    // ── Known pre-existing behavior: Jan 1=Sunday produces duplicate Jan 2 in JPY ──
    //
    // When New Year's Day (Jan 1) falls on Sunday it rolls to Jan 2, which is
    // already occupied by the non-rollable BOJ Year-Start Holiday.  The JPY
    // output contains two HolidayDate entries for Jan 2 in those years (e.g. 2034).
    // This is pre-existing behaviour, unaffected by the issue-#125 fix.

    @Test
    public void testJPY_NewYearsDay2034_DuplicateJan2_KnownBehavior() {
        // Jan 1 2034 is Sunday → New Year's Day rolls to Jan 2.
        // BOJ Year-Start Holiday is fixed, rollable=false, also on Jan 2.
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2034);
        long jan2Count = holidays.stream()
                .filter(hd -> hd.getDate().equals(LocalDate.of(2034, Month.JANUARY, 2)))
                .count();
        assertEquals(jan2Count, 2L,
                "Jan 2 2034 should have 2 entries (New Year's Day rolled + BOJ Year-Start)");
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
