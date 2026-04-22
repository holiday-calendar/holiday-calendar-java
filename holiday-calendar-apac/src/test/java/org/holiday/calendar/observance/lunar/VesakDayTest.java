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

package org.holiday.calendar.observance.lunar;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public class VesakDayTest {

    private final VesakDay observance = new VesakDay();

    // -------------------------------------------------------------------------
    // DataProviders
    // -------------------------------------------------------------------------

    @DataProvider
    Iterator<Object[]> allCoveredYears() {
        List<Object[]> data = new ArrayList<>();
        // 2019–2030: Singapore MOM official gazette
        data.add(new Object[]{2019, LocalDate.of(2019, Month.MAY,    19)});
        data.add(new Object[]{2020, LocalDate.of(2020, Month.MAY,     7)});
        data.add(new Object[]{2021, LocalDate.of(2021, Month.MAY,    26)});
        data.add(new Object[]{2022, LocalDate.of(2022, Month.MAY,    15)});
        data.add(new Object[]{2023, LocalDate.of(2023, Month.JUNE,    2)});
        data.add(new Object[]{2024, LocalDate.of(2024, Month.MAY,    22)});
        data.add(new Object[]{2025, LocalDate.of(2025, Month.MAY,    12)});
        data.add(new Object[]{2026, LocalDate.of(2026, Month.MAY,    31)});
        data.add(new Object[]{2027, LocalDate.of(2027, Month.MAY,    20)});
        data.add(new Object[]{2028, LocalDate.of(2028, Month.MAY,     8)});
        data.add(new Object[]{2029, LocalDate.of(2029, Month.MAY,    27)});
        data.add(new Object[]{2030, LocalDate.of(2030, Month.MAY,    16)});
        // 2031–2055: HKO Gregorian-Lunar Conversion Tables
        data.add(new Object[]{2031, LocalDate.of(2031, Month.JUNE,    4)});
        data.add(new Object[]{2032, LocalDate.of(2032, Month.MAY,    23)});
        data.add(new Object[]{2033, LocalDate.of(2033, Month.MAY,    13)});
        data.add(new Object[]{2034, LocalDate.of(2034, Month.JUNE,    1)});
        data.add(new Object[]{2035, LocalDate.of(2035, Month.MAY,    22)});
        data.add(new Object[]{2036, LocalDate.of(2036, Month.MAY,    10)});
        data.add(new Object[]{2037, LocalDate.of(2037, Month.MAY,    29)});
        data.add(new Object[]{2038, LocalDate.of(2038, Month.MAY,    18)});
        data.add(new Object[]{2039, LocalDate.of(2039, Month.MAY,     7)});
        data.add(new Object[]{2040, LocalDate.of(2040, Month.MAY,    25)});
        data.add(new Object[]{2041, LocalDate.of(2041, Month.MAY,    14)});
        data.add(new Object[]{2042, LocalDate.of(2042, Month.JUNE,    2)});
        data.add(new Object[]{2043, LocalDate.of(2043, Month.MAY,    23)});
        data.add(new Object[]{2044, LocalDate.of(2044, Month.MAY,    12)});
        data.add(new Object[]{2045, LocalDate.of(2045, Month.MAY,    31)});
        data.add(new Object[]{2046, LocalDate.of(2046, Month.MAY,    20)});
        data.add(new Object[]{2047, LocalDate.of(2047, Month.MAY,     9)});
        data.add(new Object[]{2048, LocalDate.of(2048, Month.MAY,    27)});
        data.add(new Object[]{2049, LocalDate.of(2049, Month.MAY,    16)});
        data.add(new Object[]{2050, LocalDate.of(2050, Month.JUNE,    4)});
        data.add(new Object[]{2051, LocalDate.of(2051, Month.MAY,    24)});
        data.add(new Object[]{2052, LocalDate.of(2052, Month.MAY,    13)});
        data.add(new Object[]{2053, LocalDate.of(2053, Month.JUNE,    1)});
        data.add(new Object[]{2054, LocalDate.of(2054, Month.MAY,    22)});
        data.add(new Object[]{2055, LocalDate.of(2055, Month.MAY,    11)});
        return data.iterator();
    }

    @DataProvider
    Iterator<Object[]> outOfRangeYears() {
        return List.of(
            new Object[]{2018},
            new Object[]{1900},
            new Object[]{2056},
            new Object[]{2099}
        ).iterator();
    }

    // -------------------------------------------------------------------------
    // Group 1: apply() returns correct non-null dates for all covered years
    // -------------------------------------------------------------------------

    @Test(dataProvider = "allCoveredYears")
    public void testApplyReturnsExpectedDate(int year, LocalDate expected) {
        assertEquals(observance.apply(year), expected,
            "VesakDay.apply(" + year + ") should return " + expected);
    }

    // -------------------------------------------------------------------------
    // Group 2: test() (Predicate) returns true for all covered years
    // -------------------------------------------------------------------------

    @Test(dataProvider = "allCoveredYears")
    public void testPredicateReturnsTrueForCoveredYear(int year, LocalDate ignored) {
        assertTrue(observance.test(year),
            "VesakDay.test(" + year + ") should return true");
    }

    // -------------------------------------------------------------------------
    // Group 3: apply() returns null for out-of-range years
    // -------------------------------------------------------------------------

    @Test(dataProvider = "outOfRangeYears")
    public void testApplyReturnsNullOutOfRange(int year) {
        assertNull(observance.apply(year),
            "VesakDay.apply(" + year + ") should return null (outside coverage)");
    }

    // -------------------------------------------------------------------------
    // Group 4: test() (Predicate) returns false for out-of-range years
    // -------------------------------------------------------------------------

    @Test(dataProvider = "outOfRangeYears")
    public void testPredicateReturnsFalseOutOfRange(int year) {
        assertFalse(observance.test(year),
            "VesakDay.test(" + year + ") should return false (outside coverage)");
    }

    // -------------------------------------------------------------------------
    // Group 5: null-safety from AbstractObservance contract
    // -------------------------------------------------------------------------

    @Test
    public void testApplyNullYearReturnsNull() {
        assertNull(observance.apply(null),
            "VesakDay.apply(null) must return null per AbstractObservance contract");
    }

    @Test
    public void testPredicateNullYearReturnsFalse() {
        assertFalse(observance.test(null),
            "VesakDay.test(null) must return false per AbstractObservance contract");
    }

    // -------------------------------------------------------------------------
    // Group 6: Boundary and seam spot checks
    // -------------------------------------------------------------------------

    @Test
    public void testOriginalCeilingYear2030() {
        assertEquals(observance.apply(2030), LocalDate.of(2030, Month.MAY, 16),
            "2030 is the original table ceiling; must remain intact after extension");
    }

    @Test
    public void testFirstNewYear2031() {
        assertEquals(observance.apply(2031), LocalDate.of(2031, Month.JUNE, 4),
            "2031 is the first newly-added year");
    }

    @Test
    public void testMidpointYear2042() {
        assertEquals(observance.apply(2042), LocalDate.of(2042, Month.JUNE, 2),
            "2042 is near the midpoint of the extended range");
    }

    @Test
    public void testNewCeilingYear2055() {
        assertEquals(observance.apply(2055), LocalDate.of(2055, Month.MAY, 11),
            "2055 is the new upper bound; must be present and correct");
    }

    @Test
    public void testYearJustBeforeLowerBound() {
        assertNull(observance.apply(2018),
            "2018 is just before the lower bound; must return null");
    }

    @Test
    public void testYearJustAfterUpperBound() {
        assertNull(observance.apply(2056),
            "2056 is just after the new upper bound; must return null");
    }

    // -------------------------------------------------------------------------
    // Group 7: Month-range sanity — all Vesak dates must fall in May or June
    // -------------------------------------------------------------------------

    @Test(dataProvider = "allCoveredYears")
    public void testDateFallsInMayOrJune(int year, LocalDate expected) {
        Month month = expected.getMonth();
        assertTrue(month == Month.MAY || month == Month.JUNE,
            "VesakDay " + year + " (" + expected + ") must fall in May or June");
    }

    // -------------------------------------------------------------------------
    // Group 8: Structural guard — table covers exactly 37 years
    // -------------------------------------------------------------------------

    @Test
    public void testTableCoversExactly37Years() {
        long coveredCount = 0;
        for (int y = 2019; y <= 2055; y++) {
            if (observance.test(y)) coveredCount++;
        }
        assertEquals(coveredCount, 37L,
            "DATES map must have exactly 37 entries covering 2019–2055 inclusive");
    }

}