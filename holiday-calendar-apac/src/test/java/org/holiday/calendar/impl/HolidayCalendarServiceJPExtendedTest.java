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
import org.holiday.calendar.HolidayDate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;

/**
 * Extended integration tests for {@link HolidayCalendarServiceJP} covering the
 * 2026–2055 target range.  These tests complement the baseline coverage in
 * {@link HolidayCalendarServiceJPTest} and exercise:
 *
 * <ol>
 *   <li>Olympic year overrides for 2020 and 2021</li>
 *   <li>Vernal Equinox Day — observed dates after Sunday rolling (2026–2055)</li>
 *   <li>Autumnal Equinox Day — observed dates after Sunday rolling (2026–2055)</li>
 *   <li>Silver Week sandwich days (2026, 2032, 2037, 2043, 2049, 2054)</li>
 *   <li>Cascading substitute holidays in Golden Week (9 years in 2026–2055)</li>
 *   <li>Emperor's Birthday Sunday rolls and Saturday no-rolls (2026–2055)</li>
 *   <li>Full-year holiday counts for representative years (2026–2055)</li>
 * </ol>
 */
public class HolidayCalendarServiceJPExtendedTest {

    private HolidayCalendar calendar;

    @BeforeClass
    public void setup() {
        this.calendar = new HolidayCalendarServiceJP().getHolidayCalendar();
    }

    // =========================================================================
    // 1. OLYMPIC YEAR OVERRIDES (2020 AND 2021)
    // =========================================================================
    // The Tokyo 2020 Olympics special measures act relocated three holidays in
    // both 2020 and 2021.
    //
    // 2021 relocations (implemented, #127):
    //   Sports Day → Jul 23, Marine Day → Jul 22, Mountain Day → Aug 9
    //
    // 2020 relocations (NOT YET IMPLEMENTED — tracked as a separate defect):
    //   Sports Day → Jul 24, Marine Day → Jul 23, Mountain Day → Aug 10
    // The 2020 tests below are disabled and will be re-enabled once fixed.
    // =========================================================================

    // 2020 relocations implemented — issue #132
    @Test
    public void testSportsDayOlympic2020() {
        List<HolidayDate> holidays = calendar.calculate(2020);
        Optional<HolidayDate> h = findByName(holidays, "Sports Day");
        assertTrue(h.isPresent(), "Sports Day must be present in 2020");
        assertEquals(h.get().getDate(), LocalDate.of(2020, Month.JULY, 24),
                     "Sports Day 2020 should be Jul 24 (Olympic relocation)");
    }

    @Test
    public void testMarineDayOlympic2020() {
        List<HolidayDate> holidays = calendar.calculate(2020);
        Optional<HolidayDate> h = findByName(holidays, "Marine Day");
        assertTrue(h.isPresent(), "Marine Day must be present in 2020");
        assertEquals(h.get().getDate(), LocalDate.of(2020, Month.JULY, 23),
                     "Marine Day 2020 should be Jul 23 (Olympic relocation)");
    }

    @Test
    public void testMountainDayOlympic2020() {
        List<HolidayDate> holidays = calendar.calculate(2020);
        Optional<HolidayDate> h = findByName(holidays, "Mountain Day");
        assertTrue(h.isPresent(), "Mountain Day must be present in 2020");
        assertEquals(h.get().getDate(), LocalDate.of(2020, Month.AUGUST, 10),
                     "Mountain Day 2020 should be Aug 10 (Olympic relocation)");
    }

    /**
     * Sports Day 2021 was relocated from its normal 2nd-Monday-in-October
     * (Oct 11) to Jul 23 by special ordinance.
     */
    @Test
    public void testSportsDayOlympic2021() {
        List<HolidayDate> holidays = calendar.calculate(2021);
        Optional<HolidayDate> h = findByName(holidays, "Sports Day");
        assertTrue(h.isPresent(), "Sports Day must be present in 2021");
        assertEquals(h.get().getDate(), LocalDate.of(2021, Month.JULY, 23),
                     "Sports Day 2021 should be Jul 23 (Olympic relocation)");
    }

    /**
     * Marine Day 2021 was relocated from its normal 3rd-Monday-in-July
     * (Jul 19) to Jul 22 by special ordinance.
     */
    @Test
    public void testMarineDayOlympic2021() {
        List<HolidayDate> holidays = calendar.calculate(2021);
        Optional<HolidayDate> h = findByName(holidays, "Marine Day");
        assertTrue(h.isPresent(), "Marine Day must be present in 2021");
        assertEquals(h.get().getDate(), LocalDate.of(2021, Month.JULY, 22),
                     "Marine Day 2021 should be Jul 22 (Olympic relocation)");
    }

    /**
     * Mountain Day 2021 was relocated from Aug 11 (Wednesday) to Aug 8
     * (Sunday); the substitute was therefore observed on Aug 9 (Monday).
     */
    @Test
    public void testMountainDayOlympic2021() {
        List<HolidayDate> holidays = calendar.calculate(2021);
        Optional<HolidayDate> h = findByName(holidays, "Mountain Day");
        assertTrue(h.isPresent(), "Mountain Day must be present in 2021");
        assertEquals(h.get().getDate(), LocalDate.of(2021, Month.AUGUST, 9),
                     "Mountain Day 2021 should be Aug 9 (Aug 8 relocated date, Sunday → Monday substitute)");
    }

    // =========================================================================
    // 2. VERNAL EQUINOX DAY — OBSERVED DATES 2026–2055
    // =========================================================================
    // Tests the end-to-end observed date for Vernal Equinox Day across the
    // full 30-year range.
    //
    // The JP calendar uses {@code sundayToMonday()} (per issue #125): only
    // Sunday holidays roll to Monday (+1). Saturday holidays remain on their
    // natural date.
    //
    // Sun→Mon rolls: 2027 (Mar 21 Sun→Mar 22 Mon), 2033 (Mar 20 Sun→Mar 21 Mon),
    // 2044 (Mar 20 Sun→Mar 21 Mon), 2050 (Mar 20 Sun→Mar 21 Mon),
    // 2055 (Mar 21 Sun→Mar 22 Mon).
    // Saturday (no roll): 2032 (Mar 20 Sat), 2038 (Mar 20 Sat),
    // 2043 (Mar 21 Sat), 2049 (Mar 20 Sat).
    // =========================================================================

    /**
     * Vernal Equinox Day observed dates 2026–2055.
     *
     * <p>The JP calendar uses {@code sundayToMonday()}: Sunday rolls +1 to Monday;
     * Saturday stays on its natural date (no substitute under Japanese law).
     */
    @DataProvider(name = "vernalEquinoxObserved2026to2055")
    Iterator<Object[]> vernalEquinoxObserved2026to2055() {
        return List.of(
            new Object[]{2026, LocalDate.of(2026, Month.MARCH, 20)},  // Fri
            new Object[]{2027, LocalDate.of(2027, Month.MARCH, 22)},  // Sun raw → Mon observed (+1)
            new Object[]{2028, LocalDate.of(2028, Month.MARCH, 20)},  // Mon
            new Object[]{2029, LocalDate.of(2029, Month.MARCH, 20)},  // Tue
            new Object[]{2030, LocalDate.of(2030, Month.MARCH, 20)},  // Wed
            new Object[]{2031, LocalDate.of(2031, Month.MARCH, 21)},  // Fri
            new Object[]{2032, LocalDate.of(2032, Month.MARCH, 20)},  // Sat — stays (no roll per #125)
            new Object[]{2033, LocalDate.of(2033, Month.MARCH, 21)},  // Sun raw → Mon observed (+1)
            new Object[]{2034, LocalDate.of(2034, Month.MARCH, 20)},  // Mon
            new Object[]{2035, LocalDate.of(2035, Month.MARCH, 21)},  // Wed
            new Object[]{2036, LocalDate.of(2036, Month.MARCH, 20)},  // Thu
            new Object[]{2037, LocalDate.of(2037, Month.MARCH, 20)},  // Fri
            new Object[]{2038, LocalDate.of(2038, Month.MARCH, 20)},  // Sat — stays (no roll per #125)
            new Object[]{2039, LocalDate.of(2039, Month.MARCH, 21)},  // Mon
            new Object[]{2040, LocalDate.of(2040, Month.MARCH, 20)},  // Tue
            new Object[]{2041, LocalDate.of(2041, Month.MARCH, 20)},  // Wed
            new Object[]{2042, LocalDate.of(2042, Month.MARCH, 20)},  // Thu
            new Object[]{2043, LocalDate.of(2043, Month.MARCH, 21)},  // Sat — stays (no roll per #125)
            new Object[]{2044, LocalDate.of(2044, Month.MARCH, 21)},  // Sun raw → Mon observed (+1)
            new Object[]{2045, LocalDate.of(2045, Month.MARCH, 20)},  // Mon
            new Object[]{2046, LocalDate.of(2046, Month.MARCH, 20)},  // Tue
            new Object[]{2047, LocalDate.of(2047, Month.MARCH, 21)},  // Thu
            new Object[]{2048, LocalDate.of(2048, Month.MARCH, 20)},  // Fri
            new Object[]{2049, LocalDate.of(2049, Month.MARCH, 20)},  // Sat — stays (no roll per #125)
            new Object[]{2050, LocalDate.of(2050, Month.MARCH, 21)},  // Sun raw → Mon observed (+1)
            new Object[]{2051, LocalDate.of(2051, Month.MARCH, 21)},  // Tue
            new Object[]{2052, LocalDate.of(2052, Month.MARCH, 20)},  // Wed
            new Object[]{2053, LocalDate.of(2053, Month.MARCH, 20)},  // Thu
            new Object[]{2054, LocalDate.of(2054, Month.MARCH, 20)},  // Fri
            new Object[]{2055, LocalDate.of(2055, Month.MARCH, 22)}   // Sun raw → Mon observed (+1)
        ).iterator();
    }

    @Test(dataProvider = "vernalEquinoxObserved2026to2055")
    public void testVernalEquinoxObservedDate(int year, LocalDate expected) {
        List<HolidayDate> holidays = calendar.calculate(year);
        Optional<HolidayDate> h = findByName(holidays, "Vernal Equinox Day");
        assertTrue(h.isPresent(), "Vernal Equinox Day must be present in " + year);
        assertEquals(h.get().getDate(), expected,
                     "Vernal Equinox Day observed date for " + year);
    }

    // =========================================================================
    // 3. AUTUMNAL EQUINOX DAY — OBSERVED DATES 2026–2055
    // =========================================================================
    // The JP calendar uses {@code sundayToMonday()}: only Sunday rolls to Monday.
    // Saturday holidays remain on their natural date (no substitute per #125).
    //
    // Sun→Mon rolls: 2029 (Sep 23 Sun→Sep 24 Mon), 2035 (Sep 23 Sun→Sep 24 Mon),
    // 2046 (Sep 23 Sun→Sep 24 Mon), 2052 (Sep 22 Sun→Sep 23 Mon).
    // Saturday (no roll): 2034 (Sep 23 Sat), 2040 (Sep 22 Sat), 2051 (Sep 23 Sat).
    // =========================================================================

    /**
     * Autumnal Equinox Day observed dates 2026–2055.
     *
     * <p>The JP calendar uses {@code sundayToMonday()}: Sunday rolls +1 to Monday;
     * Saturday stays on its natural date (no substitute under Japanese law).
     */
    @DataProvider(name = "autumnalEquinoxObserved2026to2055")
    Iterator<Object[]> autumnalEquinoxObserved2026to2055() {
        return List.of(
            new Object[]{2026, LocalDate.of(2026, Month.SEPTEMBER, 23)},  // Wed
            new Object[]{2027, LocalDate.of(2027, Month.SEPTEMBER, 23)},  // Thu
            new Object[]{2028, LocalDate.of(2028, Month.SEPTEMBER, 22)},  // Fri
            new Object[]{2029, LocalDate.of(2029, Month.SEPTEMBER, 24)},  // Sun raw Sep 23 → Mon Sep 24
            new Object[]{2030, LocalDate.of(2030, Month.SEPTEMBER, 23)},  // Mon
            new Object[]{2031, LocalDate.of(2031, Month.SEPTEMBER, 23)},  // Tue
            new Object[]{2032, LocalDate.of(2032, Month.SEPTEMBER, 22)},  // Wed
            new Object[]{2033, LocalDate.of(2033, Month.SEPTEMBER, 23)},  // Fri
            new Object[]{2034, LocalDate.of(2034, Month.SEPTEMBER, 23)},  // Sat — stays (no roll per #125)
            new Object[]{2035, LocalDate.of(2035, Month.SEPTEMBER, 24)},  // Sun raw Sep 23 → Mon Sep 24
            new Object[]{2036, LocalDate.of(2036, Month.SEPTEMBER, 22)},  // Mon
            new Object[]{2037, LocalDate.of(2037, Month.SEPTEMBER, 23)},  // Wed
            new Object[]{2038, LocalDate.of(2038, Month.SEPTEMBER, 23)},  // Thu
            new Object[]{2039, LocalDate.of(2039, Month.SEPTEMBER, 23)},  // Fri
            new Object[]{2040, LocalDate.of(2040, Month.SEPTEMBER, 22)},  // Sat — stays (no roll per #125)
            new Object[]{2041, LocalDate.of(2041, Month.SEPTEMBER, 23)},  // Mon
            new Object[]{2042, LocalDate.of(2042, Month.SEPTEMBER, 23)},  // Tue
            new Object[]{2043, LocalDate.of(2043, Month.SEPTEMBER, 23)},  // Wed
            new Object[]{2044, LocalDate.of(2044, Month.SEPTEMBER, 22)},  // Thu
            new Object[]{2045, LocalDate.of(2045, Month.SEPTEMBER, 22)},  // Fri
            new Object[]{2046, LocalDate.of(2046, Month.SEPTEMBER, 24)},  // Sun raw Sep 23 → Mon Sep 24
            new Object[]{2047, LocalDate.of(2047, Month.SEPTEMBER, 23)},  // Mon
            new Object[]{2048, LocalDate.of(2048, Month.SEPTEMBER, 22)},  // Tue
            new Object[]{2049, LocalDate.of(2049, Month.SEPTEMBER, 22)},  // Wed
            new Object[]{2050, LocalDate.of(2050, Month.SEPTEMBER, 23)},  // Fri
            new Object[]{2051, LocalDate.of(2051, Month.SEPTEMBER, 23)},  // Sat — stays (no roll per #125)
            new Object[]{2052, LocalDate.of(2052, Month.SEPTEMBER, 23)},  // Sun raw Sep 22 → Mon Sep 23
            new Object[]{2053, LocalDate.of(2053, Month.SEPTEMBER, 22)},  // Mon
            new Object[]{2054, LocalDate.of(2054, Month.SEPTEMBER, 23)},  // Wed
            new Object[]{2055, LocalDate.of(2055, Month.SEPTEMBER, 23)}   // Thu
        ).iterator();
    }

    @Test(dataProvider = "autumnalEquinoxObserved2026to2055")
    public void testAutumnalEquinoxObservedDate(int year, LocalDate expected) {
        List<HolidayDate> holidays = calendar.calculate(year);
        Optional<HolidayDate> h = findByName(holidays, "Autumnal Equinox Day");
        assertTrue(h.isPresent(), "Autumnal Equinox Day must be present in " + year);
        assertEquals(h.get().getDate(), expected,
                     "Autumnal Equinox Day observed date for " + year);
    }

    // =========================================================================
    // 4. SILVER WEEK — SEPTEMBER SANDWICH DAYS 2026–2055
    // =========================================================================
    // Silver Week occurs when Respect for the Aged Day (3rd Monday in September)
    // and Autumnal Equinox Day (observed) are exactly two days apart, leaving a
    // weekday sandwiched between them.  Years in 2026–2055: 2026, 2032, 2037,
    // 2043, 2049, 2054.
    // =========================================================================

    /**
     * Years in 2026–2055 where a Silver Week "National Holiday" sandwich occurs.
     * Each row: year, Respect for the Aged Day date, sandwich date,
     * Autumnal Equinox observed date.
     */
    @DataProvider(name = "silverWeekYears")
    Iterator<Object[]> silverWeekYears() {
        return List.of(
            // year, RespectForAgedDay, sandwichDate, AutumnalEquinox
            new Object[]{2026,
                LocalDate.of(2026, Month.SEPTEMBER, 21),  // Mon
                LocalDate.of(2026, Month.SEPTEMBER, 22),  // Tue sandwich
                LocalDate.of(2026, Month.SEPTEMBER, 23)}, // Wed
            new Object[]{2032,
                LocalDate.of(2032, Month.SEPTEMBER, 20),  // Mon
                LocalDate.of(2032, Month.SEPTEMBER, 21),  // Tue sandwich
                LocalDate.of(2032, Month.SEPTEMBER, 22)}, // Wed
            new Object[]{2037,
                LocalDate.of(2037, Month.SEPTEMBER, 21),  // Mon
                LocalDate.of(2037, Month.SEPTEMBER, 22),  // Tue sandwich
                LocalDate.of(2037, Month.SEPTEMBER, 23)}, // Wed
            new Object[]{2043,
                LocalDate.of(2043, Month.SEPTEMBER, 21),  // Mon
                LocalDate.of(2043, Month.SEPTEMBER, 22),  // Tue sandwich
                LocalDate.of(2043, Month.SEPTEMBER, 23)}, // Wed
            new Object[]{2049,
                LocalDate.of(2049, Month.SEPTEMBER, 20),  // Mon
                LocalDate.of(2049, Month.SEPTEMBER, 21),  // Tue sandwich
                LocalDate.of(2049, Month.SEPTEMBER, 22)}, // Wed
            new Object[]{2054,
                LocalDate.of(2054, Month.SEPTEMBER, 21),  // Mon
                LocalDate.of(2054, Month.SEPTEMBER, 22),  // Tue sandwich
                LocalDate.of(2054, Month.SEPTEMBER, 23)}  // Wed
        ).iterator();
    }

    @Test(dataProvider = "silverWeekYears")
    public void testSilverWeekSandwich(int year, LocalDate rfad, LocalDate sandwich, LocalDate aeq) {
        List<HolidayDate> holidays = calendar.calculate(year);

        // Verify anchor holidays are present
        assertTrue(findByDate(holidays, rfad).isPresent(),
                   year + ": Respect for the Aged Day expected on " + rfad);
        assertTrue(findByDate(holidays, aeq).isPresent(),
                   year + ": Autumnal Equinox Day expected on " + aeq);

        // Verify sandwich "National Holiday"
        Optional<HolidayDate> sw = holidays.stream()
                .filter(hd -> "National Holiday".equals(hd.getHoliday().getName())
                           && hd.getDate().equals(sandwich))
                .findFirst();
        assertTrue(sw.isPresent(),
                   year + ": Silver Week sandwich expected on " + sandwich);
    }

    /**
     * Confirms no Silver Week sandwich appears in representative non-Silver-Week
     * years where Respect for the Aged Day and Autumnal Equinox are more than
     * two days apart.
     */
    @DataProvider(name = "nonSilverWeekYears")
    Iterator<Object[]> nonSilverWeekYears() {
        return List.of(
            // diff = 3 or more days between RFAD and AEQ observed date
            new Object[]{2027},  // diff=3
            new Object[]{2028},  // diff=4
            new Object[]{2038},  // diff=3
            new Object[]{2044},  // diff=3
            new Object[]{2050},  // diff=4
            new Object[]{2055}   // diff=3
        ).iterator();
    }

    @Test(dataProvider = "nonSilverWeekYears")
    public void testNoSilverWeekSandwich(int year) {
        List<HolidayDate> holidays = calendar.calculate(year);
        long septemberSandwiches = holidays.stream()
                .filter(hd -> "National Holiday".equals(hd.getHoliday().getName())
                           && hd.getDate().getMonth() == Month.SEPTEMBER)
                .count();
        assertEquals(septemberSandwiches, 0L,
                     year + ": no September National Holiday sandwich expected");
    }

    // =========================================================================
    // 4b. SHOWA DAY 2045 — SATURDAY, NO ROLL
    // =========================================================================
    // In 2045, Apr 29 (Showa Day) falls on Saturday.  Per the Japanese holiday
    // law (振替休日 applies only to Sunday), Saturday holidays have no substitute
    // (issue #125).  Showa Day therefore remains on Apr 29, and no Golden Week
    // sandwich is generated on May 2.
    // =========================================================================

    /**
     * Verifies that in 2045 Showa Day (Apr 29 Sat) stays on its natural date
     * and does NOT produce a Golden Week sandwich on May 2.
     */
    @Test
    public void testShowaDaySaturday2045_StaysApril29() {
        List<HolidayDate> holidays = calendar.calculate(2045);

        // Showa Day (Apr 29 Sat) must stay on Apr 29 — no substitute
        Optional<HolidayDate> showa = holidays.stream()
                .filter(hd -> "Showa Day".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(showa.isPresent(), "Showa Day must be present in 2045");
        assertEquals(showa.get().getDate(), LocalDate.of(2045, Month.APRIL, 29),
                     "Showa Day 2045 (Saturday Apr 29) must stay on Apr 29 — no substitute per #125");

        // No National Holiday sandwich on May 2 (Showa Day did not roll to May 1)
        long may2sandwiches = holidays.stream()
                .filter(hd -> "National Holiday".equals(hd.getHoliday().getName())
                           && hd.getDate().equals(LocalDate.of(2045, Month.MAY, 2)))
                .count();
        assertEquals(may2sandwiches, 0L,
                     "2045: no Golden Week sandwich on May 2 (Showa Day did not roll)");
    }

    // =========================================================================
    // 5. CASCADING SUBSTITUTE HOLIDAYS IN GOLDEN WEEK
    // =========================================================================
    // When a Golden Week holiday falls on Sunday and the following Monday is
    // already occupied by another GW holiday, the substitute must cascade to
    // the next available weekday.  This is a KNOWN DEFECT in the current
    // implementation — the tests below pin the CORRECT expected behaviour.
    //
    // Two collision patterns occur in 2026–2055:
    //   (A) May 3 (Constitution Memorial Day) on Sunday → Mon May 4 blocked by
    //       Greenery Day → Tue May 5 blocked by Children's Day → substitute: May 6
    //       Years: 2026, 2037, 2043, 2048, 2054
    //
    //   (B) May 4 (Greenery Day) on Sunday → Mon May 5 blocked by Children's Day
    //       → substitute: May 6
    //       Years: 2031, 2036, 2042, 2053
    // =========================================================================

    /**
     * Golden Week cascade cases where Constitution Memorial Day (May 3, Sunday)
     * has its substitute cascade past May 4 and May 5 to May 6.
     */
    @DataProvider(name = "constitutionCascadeYears")
    Iterator<Object[]> constitutionCascadeYears() {
        return List.of(
            new Object[]{2026},  // May 3 Sun, May 4 Mon (Greenery), May 5 Tue (Children's) → sub May 6 Wed
            new Object[]{2037},  // same pattern
            new Object[]{2043},  // same pattern
            new Object[]{2048},  // same pattern
            new Object[]{2054}   // same pattern
        ).iterator();
    }

    @Test(dataProvider = "constitutionCascadeYears")
    public void testConstitutionMemorialDayCascadeSubstitute(int year) {
        List<HolidayDate> holidays = calendar.calculate(year);

        // May 3 (natural date) should NOT appear in the output as May 3 —
        // it falls on Sunday and its substitute cascades to May 6.
        assertFalse(findByDate(holidays, LocalDate.of(year, Month.MAY, 3)).isPresent(),
                    year + ": May 3 (Sunday) should not appear as an observed date");

        // May 6 should be the cascaded substitute for Constitution Memorial Day
        Optional<HolidayDate> sub = findByDate(holidays, LocalDate.of(year, Month.MAY, 6));
        assertTrue(sub.isPresent(),
                   year + ": Constitution Memorial Day substitute should be observed on May 6");
        assertEquals(sub.get().getHoliday().getName(), "Constitution Memorial Day",
                     year + ": May 6 holiday name should be Constitution Memorial Day");

        // Greenery Day and Children's Day remain on their natural dates
        assertTrue(findByDate(holidays, LocalDate.of(year, Month.MAY, 4)).isPresent(),
                   year + ": Greenery Day should still be on May 4");
        assertTrue(findByDate(holidays, LocalDate.of(year, Month.MAY, 5)).isPresent(),
                   year + ": Children's Day should still be on May 5");
    }

    /**
     * Golden Week cascade cases where Greenery Day (May 4, Sunday) has its
     * substitute cascade past May 5 to May 6.
     */
    @DataProvider(name = "greeneryCascadeYears")
    Iterator<Object[]> greeneryCascadeYears() {
        return List.of(
            new Object[]{2031},  // May 4 Sun, May 5 Mon (Children's Day) → sub May 6 Tue
            new Object[]{2036},  // same pattern
            new Object[]{2042},  // same pattern
            new Object[]{2053}   // same pattern
        ).iterator();
    }

    @Test(dataProvider = "greeneryCascadeYears")
    public void testGreeneryDayCascadeSubstitute(int year) {
        List<HolidayDate> holidays = calendar.calculate(year);

        // May 4 (Sunday) should not appear as observed date directly
        assertFalse(findByDate(holidays, LocalDate.of(year, Month.MAY, 4)).isPresent(),
                    year + ": May 4 (Sunday) should not appear as an observed date");

        // May 6 should be the cascaded substitute for Greenery Day
        Optional<HolidayDate> sub = findByDate(holidays, LocalDate.of(year, Month.MAY, 6));
        assertTrue(sub.isPresent(),
                   year + ": Greenery Day substitute should be observed on May 6");
        assertEquals(sub.get().getHoliday().getName(), "Greenery Day",
                     year + ": May 6 holiday name should be Greenery Day");

        // Children's Day remains on May 5
        assertTrue(findByDate(holidays, LocalDate.of(year, Month.MAY, 5)).isPresent(),
                   year + ": Children's Day should still be on May 5");
    }

    // =========================================================================
    // 6. EMPEROR'S BIRTHDAY WEEKEND BEHAVIOUR (2026–2055)
    // =========================================================================
    // Emperor Naruhito's birthday is Feb 23.  The JP calendar uses
    // {@code sundayToMonday()}: Sunday rolls +1 to Monday; Saturday stays on
    // its natural date (no substitute under Japanese law, issue #125).
    //
    // Sunday years (→ Mon Feb 24): 2031, 2042, 2048, 2053
    // Saturday years (stays Feb 23): 2030, 2036, 2041, 2047
    // =========================================================================

    /**
     * Years where Feb 23 falls on Sunday: Emperor's Birthday rolls to the
     * following Monday (Feb 24).
     */
    @DataProvider(name = "emperorsBirthdaySundayRollYears")
    Iterator<Object[]> emperorsBirthdaySundayRollYears() {
        return List.of(
            new Object[]{2031, LocalDate.of(2031, Month.FEBRUARY, 24)},
            new Object[]{2042, LocalDate.of(2042, Month.FEBRUARY, 24)},
            new Object[]{2048, LocalDate.of(2048, Month.FEBRUARY, 24)},
            new Object[]{2053, LocalDate.of(2053, Month.FEBRUARY, 24)}
        ).iterator();
    }

    @Test(dataProvider = "emperorsBirthdaySundayRollYears")
    public void testEmperorsBirthdaySundayRoll(int year, LocalDate expected) {
        List<HolidayDate> holidays = calendar.calculate(year);
        Optional<HolidayDate> h = findByName(holidays, "Emperor's Birthday");
        assertTrue(h.isPresent(), year + ": Emperor's Birthday must be present");
        assertEquals(h.get().getDate(), expected,
                     year + ": Emperor's Birthday should roll from Sunday Feb 23 to Monday Feb 24");
    }

    /**
     * Years where Feb 23 falls on Saturday: Emperor's Birthday stays on its
     * natural date (Feb 23).  Saturday holidays have no substitute under
     * Japanese law (振替休日 applies only to Sunday, per issue #125).
     */
    @DataProvider(name = "emperorsBirthdaySaturdayYears")
    Iterator<Object[]> emperorsBirthdaySaturdayYears() {
        return List.of(
            new Object[]{2030, LocalDate.of(2030, Month.FEBRUARY, 23)},
            new Object[]{2036, LocalDate.of(2036, Month.FEBRUARY, 23)},
            new Object[]{2041, LocalDate.of(2041, Month.FEBRUARY, 23)},
            new Object[]{2047, LocalDate.of(2047, Month.FEBRUARY, 23)}
        ).iterator();
    }

    @Test(dataProvider = "emperorsBirthdaySaturdayYears")
    public void testEmperorsBirthdaySaturday_StaysOnSaturday(int year, LocalDate expected) {
        List<HolidayDate> holidays = calendar.calculate(year);
        Optional<HolidayDate> h = findByName(holidays, "Emperor's Birthday");
        assertTrue(h.isPresent(), year + ": Emperor's Birthday must be present");
        assertEquals(h.get().getDate(), expected,
                     year + ": Emperor's Birthday (Saturday Feb 23) must stay on Feb 23 — no substitute");
    }

    // =========================================================================
    // 7. FULL-YEAR HOLIDAY COUNTS FOR REPRESENTATIVE YEARS
    // =========================================================================
    // Validates that the total observed holiday count matches expectation,
    // guarding against missing entries or spurious sandwich days.
    //
    // Counts reflect the CORRECT expected behaviour after both known defects are
    // fixed (Olympic overrides and cascading substitutes).  Duplicate-date
    // detection is handled separately by testNoDuplicateDates.
    //
    //   2026: 17  (16 base + Silver Week sandwich Sep 22;
    //               cascade sub May 6 for Constitution Memorial Day)
    //   2030: 16  (no sandwich; Saturday holidays stay on natural date per #125)
    //   2032: 17  (16 base + Silver Week sandwich Sep 21)
    //   2035: 16  (various Sun rolls; no sandwich)
    //   2040: 16  (New Year's Day Jan 1 Sun rolls Mon Jan 2)
    //   2045: 16  (Showa Day Apr 29 Sat stays Apr 29 — no sandwich;
    //               New Year's Day Jan 1 Sun rolls to Jan 2)
    //   2050: 16  (New Year's Day Jan 1 Sat stays Jan 1 per #125;
    //               Vernal Equinox Mar 20 Sun rolls Mon Mar 21)
    //   2055: 16  (Vernal Equinox Mar 21 Sun rolls Mon Mar 22)
    // =========================================================================

    @DataProvider(name = "fullYearHolidayCount")
    Iterator<Object[]> fullYearHolidayCount() {
        return List.of(
            new Object[]{2026, 17},
            new Object[]{2030, 16},
            new Object[]{2032, 17},
            new Object[]{2035, 16},
            new Object[]{2040, 16},
            new Object[]{2045, 16},
            new Object[]{2050, 16},
            new Object[]{2055, 16}
        ).iterator();
    }

    @Test(dataProvider = "fullYearHolidayCount")
    public void testFullYearHolidayCount(int year, int expected) {
        List<HolidayDate> holidays = calendar.calculate(year);
        assertEquals(holidays.size(), expected,
                     year + ": expected " + expected + " observed holidays, got " + holidays.size()
                     + " | dates: " + holidays.stream().map(h -> h.getDate().toString()).toList());
    }

    /**
     * Validates that all observed dates within a given year are unique (no
     * duplicate entries for the same date, which would indicate a defective
     * roll producing two holidays on the same day).
     */
    @DataProvider(name = "yearsForDuplicateCheck")
    Iterator<Object[]> yearsForDuplicateCheck() {
        return List.of(
            new Object[]{2026},
            new Object[]{2031},
            new Object[]{2036},
            new Object[]{2037},
            new Object[]{2042},
            new Object[]{2043},
            new Object[]{2048},
            new Object[]{2053},
            new Object[]{2054}
        ).iterator();
    }

    @Test(dataProvider = "yearsForDuplicateCheck")
    public void testNoDuplicateDates(int year) {
        List<HolidayDate> holidays = calendar.calculate(year);
        long distinctCount = holidays.stream()
                .map(HolidayDate::getDate)
                .distinct()
                .count();
        assertEquals(distinctCount, (long) holidays.size(),
                     year + ": holiday list must not contain duplicate observed dates");
    }

    // =========================================================================
    // Helpers
    // =========================================================================

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
