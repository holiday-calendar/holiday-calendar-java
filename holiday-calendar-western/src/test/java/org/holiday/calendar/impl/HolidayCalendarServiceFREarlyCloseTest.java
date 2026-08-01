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

import org.holiday.calendar.EarlyCloseHoliday;
import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.HolidayDate;
import org.holiday.calendar.observance.fr.ChristmasEveEarlyClose;
import org.holiday.calendar.observance.fr.NewYearsEveEarlyClose;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

/**
 * Tests for the {@code FR} calendar's early-close (Euronext Paris half-day
 * closure) holidays: Christmas Eve and New Year's Eve. Because December 24
 * and December 31 are always exactly 7 days apart, they always share the
 * same day-of-week within a given year — both early closes are always
 * present together or absent together, never one without the other, same
 * shape as SG.
 *
 * <p>Unlike CA (Christmas Day {@code rollable(false)}) and SG (no rollable
 * holiday ever lands on Dec 24/31), FR's Christmas Day and New Year's Day are
 * both {@code rollable(true)} under {@code previousFridayOrFollowingMonday}.
 * This produces two genuine, independently-verified date collisions with the
 * early closes, both covered explicitly below rather than relying on a naive
 * "always empty intersection" assertion:
 * <ul>
 *     <li>Christmas Day rolls back onto December 24 whenever December 25
 *     falls on a Saturday (e.g. 2027) — the same date Christmas Eve's early
 *     close independently occupies that year.</li>
 *     <li>New Year's Day rolls back onto December 31 of the <em>prior</em>
 *     year whenever January 1 falls on a Saturday (e.g. January 1, 2028 rolls
 *     to December 31, 2027) — the same date New Year's Eve's early close
 *     independently occupies in that prior year. This crosses a
 *     {@code calculate(year)} year boundary, so it is not caught by a
 *     same-year intersection check alone.</li>
 * </ul>
 */
public class HolidayCalendarServiceFREarlyCloseTest {

    private static final LocalTime EXPECTED_CLOSE_TIME = LocalTime.of(14, 5);
    private static final ZoneId EXPECTED_ZONE = ZoneId.of("Europe/Paris");

    // 11 full-day holidays registered in HolidayCalendarServiceFR; the two Eve
    // holidays are EARLY_CLOSE and excluded by calculate(), so calculate() sees 11.
    private static final int FULL_DAY_HOLIDAY_COUNT = 11;

    private final HolidayCalendarServiceFR service = new HolidayCalendarServiceFR();

    // -------------------------------------------------------------------------
    // Count / presence per year (verified 2020-2029 Euronext cycle)
    // -------------------------------------------------------------------------

    @DataProvider(name = "frEarlyCloseFixture")
    public Iterator<Object[]> frEarlyCloseFixture() {
        List<Object[]> data = Arrays.asList(
                new Object[]{2020, true},
                new Object[]{2021, true},
                new Object[]{2022, false},
                new Object[]{2023, false},
                new Object[]{2024, true},
                new Object[]{2025, true},
                new Object[]{2026, true},
                new Object[]{2027, true},
                new Object[]{2028, false},
                new Object[]{2029, true}
        );
        return data.iterator();
    }

    @Test(dataProvider = "frEarlyCloseFixture")
    public void testEarlyCloseCountForYear(int year, boolean present) {
        List<HolidayDate> earlyCloses = service.getHolidayCalendar().calculateEarlyCloses(year);
        assertNotNull(earlyCloses);
        assertEquals(earlyCloses.size(), present ? 2 : 0, "FR " + year + ": unexpected early-close count");
    }

    @Test(dataProvider = "frEarlyCloseFixture")
    public void testChristmasEvePresenceForYear(int year, boolean present) {
        assertEquals(earlyCloseNames(year).contains("Christmas Eve"), present,
                "FR " + year + ": Christmas Eve presence must match December 24 day-of-week rule");
    }

    @Test(dataProvider = "frEarlyCloseFixture")
    public void testNewYearsEvePresenceForYear(int year, boolean present) {
        assertEquals(earlyCloseNames(year).contains("New Year's Eve"), present,
                "FR " + year + ": New Year's Eve presence must match December 31 day-of-week rule");
    }

    private Set<String> earlyCloseNames(int year) {
        return service.getHolidayCalendar().calculateEarlyCloses(year).stream()
                .map(hd -> hd.getHoliday().getName())
                .collect(Collectors.toSet());
    }

    @Test(dataProvider = "frEarlyCloseFixture")
    public void testEarlyCloseCountAlwaysZeroOrTwo(int year, boolean present) {
        int count = service.getHolidayCalendar().calculateEarlyCloses(year).size();
        assertNotEquals(count, 1, "FR " + year + ": Christmas Eve and New Year's Eve must always co-occur, never appear alone");
    }

    // -------------------------------------------------------------------------
    // Suppressed-year sanity checks
    // -------------------------------------------------------------------------

    @Test
    public void testChristmasEveSuppressedIn2022() {
        // 2022: December 24 = Saturday.
        assertFalse(earlyCloseNames(2022).contains("Christmas Eve"));
    }

    @Test
    public void testChristmasEveSuppressedIn2023() {
        // 2023: December 24 = Sunday.
        assertFalse(earlyCloseNames(2023).contains("Christmas Eve"));
    }

    // -------------------------------------------------------------------------
    // Close time / zone / rollability
    // -------------------------------------------------------------------------

    @Test
    public void testCloseTimeIs14_05() {
        for (int year : List.of(2024, 2027)) {
            for (HolidayDate hd : service.getHolidayCalendar().calculateEarlyCloses(year)) {
                assertTrue(hd.getHoliday() instanceof EarlyCloseHoliday);
                EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) hd.getHoliday();
                assertEquals(earlyClose.getCloseTime(), EXPECTED_CLOSE_TIME,
                        earlyClose.getName() + " must close at 14:05");
            }
        }
    }

    @Test
    public void testZoneIdIsEuropeParis() {
        for (int year : List.of(2024, 2027)) {
            for (HolidayDate hd : service.getHolidayCalendar().calculateEarlyCloses(year)) {
                EarlyCloseHoliday earlyClose = (EarlyCloseHoliday) hd.getHoliday();
                assertEquals(earlyClose.getZoneId(), EXPECTED_ZONE,
                        earlyClose.getName() + " must be expressed in Europe/Paris");
            }
        }
    }

    @Test
    public void testEarlyClosesNotRollable() {
        for (int year : List.of(2024, 2027)) {
            for (HolidayDate hd : service.getHolidayCalendar().calculateEarlyCloses(year)) {
                assertFalse(hd.getHoliday().isRollable(), hd.getHoliday().getName() + " must not be rollable");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Full-day holiday count unchanged / no leakage into calculate() -- checked
    // in both a non-collision year and a Dec-24-collision year
    // -------------------------------------------------------------------------

    @Test
    public void testFullDayHolidayCountUnchanged() {
        for (int year : List.of(2024, 2027)) {
            List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
            assertNotNull(holidays);
            assertEquals(holidays.size(), FULL_DAY_HOLIDAY_COUNT, "FR " + year + ": unexpected full-day holiday count");
        }
    }

    // -------------------------------------------------------------------------
    // Cross-list date collision: empty in ordinary years, exactly one date
    // (December 24) in Dec-25-Saturday years, when Christmas Day rolls back
    // onto Christmas Eve
    // -------------------------------------------------------------------------

    @Test(dataProvider = "frEarlyCloseFixture")
    public void testCrossListDateOverlapOnlyOnKnownChristmasDayRollYears(int year, boolean present) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        Set<LocalDate> fullDayDates = calendar.calculate(year).stream()
                .map(HolidayDate::getDate)
                .collect(Collectors.toSet());
        Set<LocalDate> earlyCloseDates = calendar.calculateEarlyCloses(year).stream()
                .map(HolidayDate::getDate)
                .collect(Collectors.toSet());
        Set<LocalDate> intersection = fullDayDates.stream()
                .filter(earlyCloseDates::contains)
                .collect(Collectors.toSet());

        boolean isChristmasDaySaturdayRollYear =
                DayOfWeek.SATURDAY.equals(LocalDate.of(year, Month.DECEMBER, 25).getDayOfWeek());
        if (isChristmasDaySaturdayRollYear) {
            assertEquals(intersection, Set.of(LocalDate.of(year, Month.DECEMBER, 24)),
                    "FR " + year + ": expected exactly the known Christmas Day/Christmas Eve collision");
        } else {
            assertTrue(intersection.isEmpty(), "FR " + year + ": unexpected cross-list date collision: " + intersection);
        }
    }

    // -------------------------------------------------------------------------
    // Named regression: Christmas Day (rolled) and Christmas Eve (early close)
    // both legitimately appear on the same date in a Dec-25-Saturday year
    // -------------------------------------------------------------------------

    @Test
    public void testChristmasDayAndChristmasEveCoexistOn24Dec2027() {
        // December 25, 2027 is a Saturday and rolls to Friday December 24 under FR's
        // previousFridayOrFollowingMonday roll rule -- the same date the non-rolling
        // Christmas Eve EARLY_CLOSE independently occupies (December 24, 2027 is a
        // Friday, a valid early-close weekday). Both facts are true simultaneously and
        // must both surface, across calculate() and calculateEarlyCloses() respectively.
        HolidayCalendar calendar = service.getHolidayCalendar();
        LocalDate dec24 = LocalDate.of(2027, Month.DECEMBER, 24);

        boolean christmasDayPresent = calendar.calculate(2027).stream()
                .anyMatch(hd -> "Christmas Day".equals(hd.getHoliday().getName()) && dec24.equals(hd.getDate()));
        boolean christmasEvePresent = calendar.calculateEarlyCloses(2027).stream()
                .anyMatch(hd -> "Christmas Eve".equals(hd.getHoliday().getName()) && dec24.equals(hd.getDate()));

        assertTrue(christmasDayPresent, "Christmas Day (rolled) must appear on Dec 24, 2027");
        assertTrue(christmasEvePresent, "Christmas Eve early close must independently appear on Dec 24, 2027");
    }

    // -------------------------------------------------------------------------
    // Named regression: New Year's Day (rolled back onto the prior Dec 31) and
    // New Year's Eve (early close) both legitimately appear on the same date --
    // this crosses a calculate(year) year boundary and is not caught by the
    // same-year cross-list check above
    // -------------------------------------------------------------------------

    @Test
    public void testNewYearsDayAndNewYearsEveCoexistOn31Dec2027() {
        // January 1, 2028 is a Saturday and rolls back to Friday December 31, 2027
        // under FR's previousFridayOrFollowingMonday roll rule -- the same date the
        // non-rolling New Year's Eve EARLY_CLOSE independently occupies in 2027
        // (December 31, 2027 is a Friday, a valid early-close weekday). This is a
        // genuine same-date coexistence spanning two different year arguments:
        // calculate(2028) and calculateEarlyCloses(2027).
        HolidayCalendar calendar = service.getHolidayCalendar();
        LocalDate dec31 = LocalDate.of(2027, Month.DECEMBER, 31);

        boolean newYearsDayPresent = calendar.calculate(2028).stream()
                .anyMatch(hd -> "New Year's Day".equals(hd.getHoliday().getName()) && dec31.equals(hd.getDate()));
        boolean newYearsEvePresent = calendar.calculateEarlyCloses(2027).stream()
                .anyMatch(hd -> "New Year's Eve".equals(hd.getHoliday().getName()) && dec31.equals(hd.getDate()));

        assertTrue(newYearsDayPresent, "New Year's Day (rolled back from Jan 1, 2028) must appear on Dec 31, 2027");
        assertTrue(newYearsEvePresent, "New Year's Eve early close must independently appear on Dec 31, 2027");
    }

    // -------------------------------------------------------------------------
    // Cross-check calculateEarlyCloses() dates against raw Observance output
    // -------------------------------------------------------------------------

    @DataProvider(name = "earlyCloseDates")
    public Iterator<Object[]> earlyCloseDates() {
        List<Object[]> data = Arrays.asList(
                new Object[]{"Christmas Eve", 2024},
                new Object[]{"Christmas Eve", 2026},
                new Object[]{"Christmas Eve", 2027},
                new Object[]{"New Year's Eve", 2024},
                new Object[]{"New Year's Eve", 2026},
                new Object[]{"New Year's Eve", 2027}
        );
        return data.iterator();
    }

    @Test(dataProvider = "earlyCloseDates")
    public void testEarlyCloseDateMatchesRawObservance(String holidayName, int year) {
        LocalDate expected = rawObservanceDate(holidayName, year);

        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(year);
        HolidayDate matched = earlyCloses.stream()
                .filter(hd -> holidayName.equals(hd.getHoliday().getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError(holidayName + " not found in calculateEarlyCloses(" + year + ")"));
        assertEquals(matched.getDate(), expected,
                holidayName + " via calculateEarlyCloses(" + year + ") must match production Observance");
    }

    private LocalDate rawObservanceDate(String holidayName, int year) {
        return switch (holidayName) {
            case "Christmas Eve" -> new ChristmasEveEarlyClose().apply(year);
            case "New Year's Eve" -> new NewYearsEveEarlyClose().apply(year);
            default -> throw new IllegalArgumentException("Unknown holiday: " + holidayName);
        };
    }

}
