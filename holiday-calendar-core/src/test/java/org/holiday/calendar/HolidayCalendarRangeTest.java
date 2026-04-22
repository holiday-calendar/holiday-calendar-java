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

package org.holiday.calendar;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.holiday.calendar.TestObjects.createDateRollUS;
import static org.holiday.calendar.TestObjects.createObservanceLaborDay;
import static org.testng.Assert.*;

/**
 * Tests for {@link HolidayCalendar#calculate(int, int)} and
 * {@link HolidayCalendar#calculateByYear(int, int)} introduced in GitHub #109.
 */
public class HolidayCalendarRangeTest {

    /**
     * Minimal calendar: FixedHoliday New Year's Day (Jan 1, rollable) + FloatingHoliday Labor Day.
     * Both always produce a date for any year → exactly 2 holidays per year, making count assertions exact.
     */
    private HolidayCalendar calendar;

    /** Empty calendar (no holidays) for edge-case tests. */
    private HolidayCalendar emptyCalendar;

    @BeforeClass
    public void setupCalendars() {
        calendar = HolidayCalendar.builder()
            .code("TEST")
            .name("Range Test Calendar")
            .dateRoll(createDateRollUS())
            .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
            .holiday(new FixedHoliday("New Year's Day", "", Month.JANUARY, 1))
            .holiday(new FloatingHoliday("Labor Day", "", createObservanceLaborDay()))
            .build();

        emptyCalendar = HolidayCalendar.builder()
            .code("EMPTY")
            .name("Empty Test Calendar")
            .build();
    }

    // =========================================================================
    // A — Validation: fromYear > toYear throws IllegalArgumentException
    // =========================================================================

    @Test(expectedExceptions = IllegalArgumentException.class, groups = "core")
    public void testCalculateRange_InvalidRange_ThrowsIllegalArgumentException() {
        calendar.calculate(2026, 2025);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, groups = "core")
    public void testCalculateByYear_InvalidRange_ThrowsIllegalArgumentException() {
        calendar.calculateByYear(2026, 2025);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, groups = "core")
    public void testCalculateRange_LargeInvertedRange_ThrowsIllegalArgumentException() {
        calendar.calculate(2055, 2025);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, groups = "core")
    public void testCalculateByYear_LargeInvertedRange_ThrowsIllegalArgumentException() {
        calendar.calculateByYear(2055, 2025);
    }

    // =========================================================================
    // B — Single-year range (fromYear == toYear) equals calculate(year)
    // =========================================================================

    @Test(groups = "core")
    public void testCalculateRange_SingleYear_MatchesSingleYearCalculate() {
        List<HolidayDate> expected = calendar.calculate(2025);
        List<HolidayDate> actual = calendar.calculate(2025, 2025);

        assertNotNull(actual);
        assertEquals(actual, expected);
    }

    @Test(groups = "core")
    public void testCalculateByYear_SingleYear_MatchesSingleYearCalculate() {
        List<HolidayDate> expected = calendar.calculate(2025);
        Map<Integer, List<HolidayDate>> byYear = calendar.calculateByYear(2025, 2025);

        assertNotNull(byYear);
        assertEquals(byYear.size(), 1);
        assertTrue(byYear.containsKey(2025));
        assertEquals(byYear.get(2025), expected);
    }

    // =========================================================================
    // C — Flat list: count and globally chronological order
    // =========================================================================

    @Test(dataProvider = "rangeCountData", groups = "core")
    public void testCalculateRange_Count(int fromYear, int toYear, int expectedCount) {
        List<HolidayDate> result = calendar.calculate(fromYear, toYear);

        assertNotNull(result);
        assertEquals(result.size(), expectedCount,
            String.format("Expected %d holidays for range [%d, %d]", expectedCount, fromYear, toYear));
    }

    @Test(dataProvider = "rangeCountData", groups = "core")
    public void testCalculateRange_GloballySortedByDate(int fromYear, int toYear, int expectedCount) {
        List<HolidayDate> result = calendar.calculate(fromYear, toYear);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        for (int i = 0; i < result.size() - 1; i++) {
            assertFalse(result.get(i).getDate().isAfter(result.get(i + 1).getDate()),
                String.format("Global sort violated at index %d: %s > %s",
                    i, result.get(i).getDate(), result.get(i + 1).getDate()));
        }
    }

    /**
     * 2 holidays per year (New Year's Day + Labor Day), so count = 2 * (toYear - fromYear + 1).
     */
    @DataProvider(name = "rangeCountData")
    public Iterator<Object[]> rangeCountData() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 2025, 2025,   2 });
        data.add(new Object[]{ 2024, 2025,   4 });
        data.add(new Object[]{ 2020, 2024,  10 });
        data.add(new Object[]{ 2000, 2029,  60 });
        data.add(new Object[]{ 2000, 2049, 100 });
        return data.iterator();
    }

    // =========================================================================
    // D — Explicit 30-year range
    // =========================================================================

    @Test(groups = "core")
    public void testCalculateRange_ThirtyYears_CountAndOrder() {
        List<HolidayDate> result = calendar.calculate(2000, 2029);

        assertNotNull(result);
        assertEquals(result.size(), 60, "30-year range with 2 holidays/year should produce 60 entries");

        for (int i = 0; i < result.size() - 1; i++) {
            assertFalse(result.get(i).getDate().isAfter(result.get(i + 1).getDate()),
                String.format("Global sort violated at index %d: %s > %s",
                    i, result.get(i).getDate(), result.get(i + 1).getDate()));
        }

        // 2000-01-01 is a Saturday; US roll → 1999-12-31; first date in range must be on or before 2000-01-02
        assertFalse(result.getFirst().getDate().isAfter(LocalDate.of(2000, Month.JANUARY, 2)));
    }

    // =========================================================================
    // E — calculateByYear map contract
    // =========================================================================

    @Test(dataProvider = "mapKeyRangeData", groups = "core")
    public void testCalculateByYear_MapKeysMatchRange(int fromYear, int toYear) {
        Map<Integer, List<HolidayDate>> byYear = calendar.calculateByYear(fromYear, toYear);

        assertNotNull(byYear);
        assertEquals(byYear.size(), toYear - fromYear + 1,
            "Map must have one key per year in range [" + fromYear + ", " + toYear + "]");

        for (int year = fromYear; year <= toYear; year++) {
            assertTrue(byYear.containsKey(year), "Map must contain key for year " + year);
        }
    }

    @Test(dataProvider = "mapKeyRangeData", groups = "core")
    public void testCalculateByYear_MapKeysAreOrdered(int fromYear, int toYear) {
        Map<Integer, List<HolidayDate>> byYear = calendar.calculateByYear(fromYear, toYear);

        assertNotNull(byYear);

        List<Integer> keys = new ArrayList<>(byYear.keySet());
        for (int i = 0; i < keys.size() - 1; i++) {
            assertTrue(keys.get(i) < keys.get(i + 1),
                String.format("Key at index %d (%d) must be less than key at index %d (%d)",
                    i, keys.get(i), i + 1, keys.get(i + 1)));
        }
    }

    @Test(dataProvider = "mapKeyRangeData", groups = "core")
    public void testCalculateByYear_PerYearValuesMatchSingleYearCalculate(int fromYear, int toYear) {
        Map<Integer, List<HolidayDate>> byYear = calendar.calculateByYear(fromYear, toYear);

        for (int year = fromYear; year <= toYear; year++) {
            List<HolidayDate> expected = calendar.calculate(year);
            List<HolidayDate> actual = byYear.get(year);

            assertNotNull(actual, "Map value for year " + year + " must not be null");
            assertEquals(actual, expected,
                "Map value for year " + year + " must equal calculate(" + year + ")");
        }
    }

    @DataProvider(name = "mapKeyRangeData")
    public Iterator<Object[]> mapKeyRangeData() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 2025, 2025 });
        data.add(new Object[]{ 2024, 2025 });
        data.add(new Object[]{ 2020, 2024 });
        data.add(new Object[]{ 2000, 2029 });
        data.add(new Object[]{ 2000, 2049 });
        return data.iterator();
    }

    // =========================================================================
    // F — Empty calendar
    // =========================================================================

    @Test(groups = "core")
    public void testCalculateRange_EmptyCalendar_ReturnsEmptyList() {
        List<HolidayDate> result = emptyCalendar.calculate(2025, 2030);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test(groups = "core")
    public void testCalculateByYear_EmptyCalendar_DenseMapWithEmptyLists() {
        Map<Integer, List<HolidayDate>> byYear = emptyCalendar.calculateByYear(2025, 2030);

        assertNotNull(byYear);
        assertEquals(byYear.size(), 6, "Empty calendar must still produce one key per year");

        for (int year = 2025; year <= 2030; year++) {
            assertTrue(byYear.containsKey(year));
            assertNotNull(byYear.get(year));
            assertTrue(byYear.get(year).isEmpty(),
                "Each year's value should be empty for an empty calendar");
        }
    }

    // =========================================================================
    // G — Date roll non-regression (2023: Jan 1 is Sunday → rolls to Jan 2)
    // =========================================================================

    @Test(groups = "core")
    public void testCalculateRange_DateRollApplied_NewYearsDay2023() {
        List<HolidayDate> result = calendar.calculate(2023, 2023);

        assertNotNull(result);
        boolean found = result.stream()
            .anyMatch(hd -> "New Year's Day".equals(hd.getHoliday().getName())
                         && hd.getDate().equals(LocalDate.of(2023, Month.JANUARY, 2)));
        assertTrue(found, "New Year's Day 2023 (Sunday) must roll to Monday 2023-01-02");
    }

    @Test(groups = "core")
    public void testCalculateByYear_DateRollApplied_NewYearsDay2023() {
        Map<Integer, List<HolidayDate>> byYear = calendar.calculateByYear(2023, 2023);

        List<HolidayDate> year2023 = byYear.get(2023);
        assertNotNull(year2023);

        boolean found = year2023.stream()
            .anyMatch(hd -> "New Year's Day".equals(hd.getHoliday().getName())
                         && hd.getDate().equals(LocalDate.of(2023, Month.JANUARY, 2)));
        assertTrue(found, "calculateByYear 2023 value must include New Year's Day rolled to 2023-01-02");
    }

    // =========================================================================
    // H — Cross-year roll: 2022 New Year's Day (Saturday) rolls to 2021-12-31
    // =========================================================================

    @Test(groups = "core")
    public void testCalculateRange_CrossYearRoll_GlobalSortCorrect() {
        // 2022-01-01 is Saturday → US roll → 2021-12-31
        // The flat list for [2021, 2022] must still be globally sorted:
        //   2021 entries: [2021-01-01, 2021-09-06]
        //   2022 entries (nominal): [2021-12-31 (rolled NYD), 2022-09-05]
        // Global sort: 2021-01-01, 2021-09-06, 2021-12-31, 2022-09-05
        List<HolidayDate> result = calendar.calculate(2021, 2022);

        assertNotNull(result);
        assertEquals(result.size(), 4);

        for (int i = 0; i < result.size() - 1; i++) {
            assertFalse(result.get(i).getDate().isAfter(result.get(i + 1).getDate()),
                String.format("Global sort violated at index %d: %s > %s",
                    i, result.get(i).getDate(), result.get(i + 1).getDate()));
        }

        // The rolled 2022 New Year's Day must appear as 2021-12-31
        boolean rolledNYD = result.stream()
            .anyMatch(hd -> "New Year's Day".equals(hd.getHoliday().getName())
                         && hd.getDate().equals(LocalDate.of(2021, Month.DECEMBER, 31)));
        assertTrue(rolledNYD, "2022 New Year's Day (Saturday) must appear as 2021-12-31 in the flat list");
    }

    // =========================================================================
    // I — Known values for 2025–2026 range
    // =========================================================================

    @Test(groups = "core")
    public void testCalculateRange_KnownValues_2025To2026() {
        // 2025-01-01 Wed (no roll), 2025-09-01 Labor Day
        // 2026-01-01 Thu (no roll), 2026-09-07 Labor Day
        List<HolidayDate> result = calendar.calculate(2025, 2026);

        assertNotNull(result);
        assertEquals(result.size(), 4);
        assertEquals(result.get(0).getDate(), LocalDate.of(2025, Month.JANUARY, 1));
        assertEquals(result.get(0).getHoliday().getName(), "New Year's Day");
        assertEquals(result.get(1).getDate(), LocalDate.of(2025, Month.SEPTEMBER, 1));
        assertEquals(result.get(1).getHoliday().getName(), "Labor Day");
        assertEquals(result.get(2).getDate(), LocalDate.of(2026, Month.JANUARY, 1));
        assertEquals(result.get(2).getHoliday().getName(), "New Year's Day");
        assertEquals(result.get(3).getDate(), LocalDate.of(2026, Month.SEPTEMBER, 7));
        assertEquals(result.get(3).getHoliday().getName(), "Labor Day");
    }

}
