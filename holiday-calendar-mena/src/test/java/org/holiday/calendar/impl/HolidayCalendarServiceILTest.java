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

public class HolidayCalendarServiceILTest {

    // 10 Hebrew-calendar holidays per year (9 shared with ILS + Yom Hazikaron)
    private static final int IL_HOLIDAY_COUNT = 10;

    private final HolidayCalendarServiceIL service = new HolidayCalendarServiceIL();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // -------------------------------------------------------------------------
    // Identity
    // -------------------------------------------------------------------------

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("IL"));
        assertFalse(service.isProvided("ILS"));
        assertFalse(service.isProvided("SA"));
        assertFalse(service.isProvided("US"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "IL");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Israel (National) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("IL");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "IL");
    }

    // -------------------------------------------------------------------------
    // Weekend configuration
    // -------------------------------------------------------------------------

    @Test
    public void testWeekendDaysFridayAndSaturday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2, "Israeli weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY), "Friday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY), "Saturday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY), "Sunday must not be a weekend day");
    }

    // -------------------------------------------------------------------------
    // dataValidThrough — algorithmic, no ceiling
    // -------------------------------------------------------------------------

    @Test
    public void testDataValidThroughReturnsEmpty() {
        OptionalInt result = service.dataValidThrough();
        assertTrue(result.isEmpty(),
                "IL uses HebrewCalendar arithmetic; dataValidThrough() must return empty");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("IL");
        assertTrue(result.isEmpty(),
                "factory.dataValidThrough(\"IL\") must delegate to service and return empty");
    }

    // -------------------------------------------------------------------------
    // Holiday count and composition
    // -------------------------------------------------------------------------

    @Test
    public void testCalculate2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), IL_HOLIDAY_COUNT,
                "Expected " + IL_HOLIDAY_COUNT + " holidays for 2025, got: " + holidays.size());
    }

    @Test
    public void testCalculate2026() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2026);
        assertNotNull(holidays);
        assertEquals(holidays.size(), IL_HOLIDAY_COUNT,
                "Expected " + IL_HOLIDAY_COUNT + " holidays for 2026, got: " + holidays.size());
    }

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"Rosh Hashanah"},
            new Object[]{"Rosh Hashanah (2nd Day)"},
            new Object[]{"Yom Kippur"},
            new Object[]{"Sukkot"},
            new Object[]{"Shemini Atzeret / Simchat Torah"},
            new Object[]{"Passover"},
            new Object[]{"Passover (7th Day)"},
            new Object[]{"Yom Hazikaron"},
            new Object[]{"Yom Ha'atzmaut"},
            new Object[]{"Shavuot"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        boolean found = calendar.getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName()));
        assertTrue(found, "Calendar must contain holiday: " + holidayName);
    }

    // -------------------------------------------------------------------------
    // Chronological order
    // -------------------------------------------------------------------------

    @Test
    public void testChronologicalOrder2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).date().isBefore(holidays.get(i - 1).date()),
                    "Holidays must be in chronological order");
        }
    }

    @Test
    public void testChronologicalOrder2026() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2026);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).date().isBefore(holidays.get(i - 1).date()),
                    "Holidays must be in chronological order");
        }
    }

    // -------------------------------------------------------------------------
    // 2025 spot-checks
    // All dates verified via net.time4j.calendar.HebrewCalendar computation.
    // Note: the issue's Rosh Hashanah/Yom Kippur spot-checks (Sep 22-23, Oct 1)
    // use the sunset-based start convention; Time4J returns the Gregorian
    // calendar day of the Hebrew date, which is one day later.
    // -------------------------------------------------------------------------

    // Passover 5785 (spring 2025, offset +3760)
    @Test
    public void testPassover2025() {
        // 15 Nisan 5785 = 2025-04-13 (Sunday)
        assertEquals(LocalDate.of(2025, Month.APRIL, 13).getDayOfWeek(), DayOfWeek.SUNDAY);
        assertEquals(findFirst("Passover", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.APRIL, 13),
                "Passover Day 1 2025 must be 2025-04-13");
    }

    @Test
    public void testPassoverEnd2025() {
        // 21 Nisan 5785 = 2025-04-19 (Saturday)
        assertEquals(LocalDate.of(2025, Month.APRIL, 19).getDayOfWeek(), DayOfWeek.SATURDAY);
        assertEquals(findFirst("Passover (7th Day)", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.APRIL, 19),
                "Passover 7th Day 2025 must be 2025-04-19");
    }

    // Yom Ha'atzmaut 5785: natural Iyar 5 = May 3 (Saturday) → shift -2 → Thursday May 1
    @Test
    public void testYomHaatzmaut2025ShiftFromSaturday() {
        assertEquals(LocalDate.of(2025, Month.MAY, 3).getDayOfWeek(), DayOfWeek.SATURDAY,
                "Natural Iyar 5 5785 must be Saturday May 3");
        assertEquals(findFirst("Yom Ha'atzmaut", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.MAY, 1),
                "Yom Ha'atzmaut 2025 (Iyar 5 = Saturday) must shift back to Thursday May 1");
    }

    @Test
    public void testShavuot2025() {
        // 6 Sivan 5785 = 2025-06-02 (Monday)
        assertEquals(LocalDate.of(2025, Month.JUNE, 2).getDayOfWeek(), DayOfWeek.MONDAY);
        assertEquals(findFirst("Shavuot", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.JUNE, 2),
                "Shavuot 2025 must be 2025-06-02");
    }

    // Rosh Hashanah 5786 (autumn 2025, offset +3761)
    @Test
    public void testRoshHashanah2025() {
        // 1 Tishri 5786 = 2025-09-23 (Tuesday)
        assertEquals(LocalDate.of(2025, Month.SEPTEMBER, 23).getDayOfWeek(), DayOfWeek.TUESDAY);
        assertEquals(findFirst("Rosh Hashanah", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.SEPTEMBER, 23),
                "Rosh Hashanah Day 1 2025 must be 2025-09-23");
    }

    @Test
    public void testRoshHashanahDay2_2025() {
        // 2 Tishri 5786 = 2025-09-24 (Wednesday)
        assertEquals(LocalDate.of(2025, Month.SEPTEMBER, 24).getDayOfWeek(), DayOfWeek.WEDNESDAY);
        assertEquals(findFirst("Rosh Hashanah (2nd Day)", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.SEPTEMBER, 24),
                "Rosh Hashanah Day 2 2025 must be 2025-09-24");
    }

    @Test
    public void testYomKippur2025() {
        // 10 Tishri 5786 = 2025-10-02 (Thursday)
        assertEquals(LocalDate.of(2025, Month.OCTOBER, 2).getDayOfWeek(), DayOfWeek.THURSDAY);
        assertEquals(findFirst("Yom Kippur", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.OCTOBER, 2),
                "Yom Kippur 2025 must be 2025-10-02");
    }

    @Test
    public void testSukkot2025() {
        // 15 Tishri 5786 = 2025-10-07 (Tuesday)
        assertEquals(LocalDate.of(2025, Month.OCTOBER, 7).getDayOfWeek(), DayOfWeek.TUESDAY);
        assertEquals(findFirst("Sukkot", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.OCTOBER, 7),
                "Sukkot 2025 must be 2025-10-07");
    }

    @Test
    public void testSheminiAtzeret2025() {
        // 22 Tishri 5786 = 2025-10-14 (Tuesday)
        assertEquals(LocalDate.of(2025, Month.OCTOBER, 14).getDayOfWeek(), DayOfWeek.TUESDAY);
        assertEquals(findFirst("Shemini Atzeret / Simchat Torah", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.OCTOBER, 14),
                "Shemini Atzeret 2025 must be 2025-10-14");
    }

    // -------------------------------------------------------------------------
    // 2026 cross-year coverage
    // -------------------------------------------------------------------------

    @Test
    public void testPassover2026() {
        // 15 Nisan 5786 = 2026-04-02 (Thursday)
        assertEquals(LocalDate.of(2026, Month.APRIL, 2).getDayOfWeek(), DayOfWeek.THURSDAY);
        assertEquals(findFirst("Passover", 2026).orElseThrow().date(),
                LocalDate.of(2026, Month.APRIL, 2),
                "Passover Day 1 2026 must be 2026-04-02");
    }

    @Test
    public void testPassoverEnd2026() {
        // 21 Nisan 5786 = 2026-04-08 (Wednesday)
        assertEquals(LocalDate.of(2026, Month.APRIL, 8).getDayOfWeek(), DayOfWeek.WEDNESDAY);
        assertEquals(findFirst("Passover (7th Day)", 2026).orElseThrow().date(),
                LocalDate.of(2026, Month.APRIL, 8),
                "Passover 7th Day 2026 must be 2026-04-08");
    }

    // Yom Ha'atzmaut 2026: natural Iyar 5 = April 22 (Wednesday) → no shift
    @Test
    public void testYomHaatzmaut2026NoShift() {
        assertEquals(LocalDate.of(2026, Month.APRIL, 22).getDayOfWeek(), DayOfWeek.WEDNESDAY,
                "Natural Iyar 5 5786 must be Wednesday April 22");
        assertEquals(findFirst("Yom Ha'atzmaut", 2026).orElseThrow().date(),
                LocalDate.of(2026, Month.APRIL, 22),
                "Yom Ha'atzmaut 2026 (Wednesday) must not shift");
    }

    @Test
    public void testRoshHashanah2026() {
        // 1 Tishri 5787 = 2026-09-12 (Saturday — Israeli weekend, but still listed)
        assertEquals(LocalDate.of(2026, Month.SEPTEMBER, 12).getDayOfWeek(), DayOfWeek.SATURDAY);
        assertEquals(findFirst("Rosh Hashanah", 2026).orElseThrow().date(),
                LocalDate.of(2026, Month.SEPTEMBER, 12),
                "Rosh Hashanah Day 1 2026 must be 2026-09-12");
    }

    // -------------------------------------------------------------------------
    // Independence Day shift rule — additional years
    // -------------------------------------------------------------------------

    // 2024: natural Iyar 5 = May 13 (Monday) → shift +1 → Tuesday May 14
    @Test
    public void testYomHaatzmaut2024ShiftFromMonday() {
        assertEquals(LocalDate.of(2024, Month.MAY, 13).getDayOfWeek(), DayOfWeek.MONDAY,
                "Natural Iyar 5 5784 must be Monday May 13");
        assertEquals(findFirst("Yom Ha'atzmaut", 2024).orElseThrow().date(),
                LocalDate.of(2024, Month.MAY, 14),
                "Yom Ha'atzmaut 2024 (Iyar 5 = Monday) must shift to Tuesday May 14");
    }

    // 2029: natural Iyar 5 = April 20 (Friday) → shift -1 → Thursday April 19
    @Test
    public void testYomHaatzmaut2029ShiftFromFriday() {
        assertEquals(LocalDate.of(2029, Month.APRIL, 20).getDayOfWeek(), DayOfWeek.FRIDAY,
                "Natural Iyar 5 5789 must be Friday April 20");
        assertEquals(findFirst("Yom Ha'atzmaut", 2029).orElseThrow().date(),
                LocalDate.of(2029, Month.APRIL, 19),
                "Yom Ha'atzmaut 2029 (Iyar 5 = Friday) must shift to Thursday April 19");
    }

    // 2028: natural Iyar 5 = May 1 (Monday) → shift +1 → Tuesday May 2
    @Test
    public void testYomHaatzmaut2028ShiftFromMonday() {
        assertEquals(LocalDate.of(2028, Month.MAY, 1).getDayOfWeek(), DayOfWeek.MONDAY,
                "Natural Iyar 5 5788 must be Monday May 1");
        assertEquals(findFirst("Yom Ha'atzmaut", 2028).orElseThrow().date(),
                LocalDate.of(2028, Month.MAY, 2),
                "Yom Ha'atzmaut 2028 (Iyar 5 = Monday) must shift to Tuesday May 2");
    }

    // -------------------------------------------------------------------------
    // Yom Hazikaron — always one day before observed Yom Ha'atzmaut
    // -------------------------------------------------------------------------

    // 2025: Yom Ha'atzmaut = May 1 (Thu, shifted from Sat) → Yom Hazikaron = Apr 30 (Wed)
    @Test
    public void testYomHazikaron2025() {
        assertEquals(findFirst("Yom Hazikaron", 2025).orElseThrow().date(),
                LocalDate.of(2025, Month.APRIL, 30),
                "Yom Hazikaron 2025 must be 2025-04-30");
    }

    // 2026: Yom Ha'atzmaut = Apr 22 (Wed, no shift) → Yom Hazikaron = Apr 21 (Tue)
    @Test
    public void testYomHazikaron2026() {
        assertEquals(findFirst("Yom Hazikaron", 2026).orElseThrow().date(),
                LocalDate.of(2026, Month.APRIL, 21),
                "Yom Hazikaron 2026 must be 2026-04-21");
    }

    // 2024: Yom Ha'atzmaut = May 14 (Tue, shifted from Mon) → Yom Hazikaron = May 13 (Mon)
    @Test
    public void testYomHazikaron2024() {
        assertEquals(findFirst("Yom Hazikaron", 2024).orElseThrow().date(),
                LocalDate.of(2024, Month.MAY, 13),
                "Yom Hazikaron 2024 must be 2024-05-13");
    }

    // Invariant: Yom Hazikaron must always be exactly one day before Yom Ha'atzmaut
    @Test
    public void testYomHazikaron_AlwaysOneDayBeforeIndependenceDay() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        for (int year = 2020; year <= 2055; year++) {
            final int y = year;
            LocalDate memorial = calendar.calculate(year).stream()
                    .filter(h -> "Yom Hazikaron".equals(h.holiday().getName()))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("Yom Hazikaron missing for year " + y))
                    .date();
            LocalDate independence = calendar.calculate(year).stream()
                    .filter(h -> "Yom Ha'atzmaut".equals(h.holiday().getName()))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("Yom Ha'atzmaut missing for year " + y))
                    .date();
            assertEquals(memorial, independence.minusDays(1),
                    "Yom Hazikaron must be exactly one day before Yom Ha'atzmaut in year " + year);
        }
    }

    // Invariant: after shift rule, observed date must never be Friday or Saturday
    @Test
    public void testYomHaatzmautNeverFallsOnWeekend() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        for (int year = 2020; year <= 2055; year++) {
            Optional<HolidayDate> hd = calendar.calculate(year).stream()
                    .filter(h -> "Yom Ha'atzmaut".equals(h.holiday().getName()))
                    .findFirst();
            assertTrue(hd.isPresent(), "Yom Ha'atzmaut must be present for year " + year);
            DayOfWeek dow = hd.get().date().getDayOfWeek();
            assertNotEquals(dow, DayOfWeek.FRIDAY,
                    "Yom Ha'atzmaut must not fall on Friday in year " + year + " (got " + hd.get().date() + ")");
            assertNotEquals(dow, DayOfWeek.SATURDAY,
                    "Yom Ha'atzmaut must not fall on Saturday in year " + year + " (got " + hd.get().date() + ")");
        }
    }

    // -------------------------------------------------------------------------
    // Rosh Hashanah Day 1 and Day 2 are always consecutive
    // -------------------------------------------------------------------------

    @Test
    public void testRoshHashanahDaysAreConsecutive2025() {
        LocalDate day1 = findFirst("Rosh Hashanah", 2025).orElseThrow().date();
        LocalDate day2 = findFirst("Rosh Hashanah (2nd Day)", 2025).orElseThrow().date();
        assertEquals(day2, day1.plusDays(1), "Rosh Hashanah Day 2 must be the day after Day 1");
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private Optional<HolidayDate> findFirst(String name, int year) {
        return service.getHolidayCalendar().calculate(year).stream()
                .filter(hd -> name.equals(hd.holiday().getName()))
                .findFirst();
    }

}
