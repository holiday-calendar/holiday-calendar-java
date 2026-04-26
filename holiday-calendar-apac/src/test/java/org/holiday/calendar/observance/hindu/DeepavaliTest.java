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

package org.holiday.calendar.observance.hindu;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public class DeepavaliTest {

    private final Deepavali observance = new Deepavali();

    // -------------------------------------------------------------------------
    // DataProviders
    // -------------------------------------------------------------------------

    @DataProvider
    Iterator<Object[]> allCoveredYears() {
        List<Object[]> data = new ArrayList<>();
        // 2019–2030: Singapore MOM official gazette
        data.add(new Object[]{2019, LocalDate.of(2019, Month.OCTOBER,  27)});
        data.add(new Object[]{2020, LocalDate.of(2020, Month.NOVEMBER, 14)});
        data.add(new Object[]{2021, LocalDate.of(2021, Month.NOVEMBER,  4)});
        data.add(new Object[]{2022, LocalDate.of(2022, Month.OCTOBER,  24)});
        data.add(new Object[]{2023, LocalDate.of(2023, Month.NOVEMBER, 12)});
        data.add(new Object[]{2024, LocalDate.of(2024, Month.OCTOBER,  31)});
        data.add(new Object[]{2025, LocalDate.of(2025, Month.OCTOBER,  20)});
        data.add(new Object[]{2026, LocalDate.of(2026, Month.NOVEMBER,  8)});
        data.add(new Object[]{2027, LocalDate.of(2027, Month.OCTOBER,  29)});
        data.add(new Object[]{2028, LocalDate.of(2028, Month.OCTOBER,  17)});
        data.add(new Object[]{2029, LocalDate.of(2029, Month.NOVEMBER,  5)});
        data.add(new Object[]{2030, LocalDate.of(2030, Month.OCTOBER,  26)});
        // 2031–2055: Tamil Panchang projection (1st day of Tamil month Karthigai, SGT);
        // verify against MOM gazette as each year is officially published.
        data.add(new Object[]{2031, LocalDate.of(2031, Month.NOVEMBER, 14)});
        data.add(new Object[]{2032, LocalDate.of(2032, Month.NOVEMBER,  2)});
        data.add(new Object[]{2033, LocalDate.of(2033, Month.OCTOBER,  23)});
        data.add(new Object[]{2034, LocalDate.of(2034, Month.NOVEMBER, 10)});
        data.add(new Object[]{2035, LocalDate.of(2035, Month.OCTOBER,  31)});
        data.add(new Object[]{2036, LocalDate.of(2036, Month.OCTOBER,  19)});
        data.add(new Object[]{2037, LocalDate.of(2037, Month.NOVEMBER,  7)});
        data.add(new Object[]{2038, LocalDate.of(2038, Month.OCTOBER,  27)});
        data.add(new Object[]{2039, LocalDate.of(2039, Month.OCTOBER,  16)});
        data.add(new Object[]{2040, LocalDate.of(2040, Month.NOVEMBER,  3)});
        data.add(new Object[]{2041, LocalDate.of(2041, Month.OCTOBER,  24)});
        data.add(new Object[]{2042, LocalDate.of(2042, Month.NOVEMBER, 11)});
        data.add(new Object[]{2043, LocalDate.of(2043, Month.NOVEMBER,  1)});
        data.add(new Object[]{2044, LocalDate.of(2044, Month.OCTOBER,  20)});
        data.add(new Object[]{2045, LocalDate.of(2045, Month.NOVEMBER,  8)});
        data.add(new Object[]{2046, LocalDate.of(2046, Month.OCTOBER,  29)});
        data.add(new Object[]{2047, LocalDate.of(2047, Month.OCTOBER,  19)});
        data.add(new Object[]{2048, LocalDate.of(2048, Month.NOVEMBER,  5)});
        data.add(new Object[]{2049, LocalDate.of(2049, Month.OCTOBER,  25)});
        data.add(new Object[]{2050, LocalDate.of(2050, Month.OCTOBER,  15)});
        data.add(new Object[]{2051, LocalDate.of(2051, Month.NOVEMBER,  3)});
        data.add(new Object[]{2052, LocalDate.of(2052, Month.OCTOBER,  22)});
        data.add(new Object[]{2053, LocalDate.of(2053, Month.NOVEMBER, 10)});
        data.add(new Object[]{2054, LocalDate.of(2054, Month.OCTOBER,  30)});
        data.add(new Object[]{2055, LocalDate.of(2055, Month.OCTOBER,  19)});
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
            "Deepavali.apply(" + year + ") should return " + expected);
    }

    // -------------------------------------------------------------------------
    // Group 2: test() (Predicate) returns true for all covered years
    // -------------------------------------------------------------------------

    @Test(dataProvider = "allCoveredYears")
    public void testPredicateReturnsTrueForCoveredYear(int year, LocalDate ignored) {
        assertTrue(observance.test(year),
            "Deepavali.test(" + year + ") should return true");
    }

    // -------------------------------------------------------------------------
    // Group 3: apply() returns null for out-of-range years
    // -------------------------------------------------------------------------

    @Test(dataProvider = "outOfRangeYears")
    public void testApplyReturnsNullOutOfRange(int year) {
        assertNull(observance.apply(year),
            "Deepavali.apply(" + year + ") should return null (outside coverage)");
    }

    // -------------------------------------------------------------------------
    // Group 4: test() (Predicate) returns false for out-of-range years
    // -------------------------------------------------------------------------

    @Test(dataProvider = "outOfRangeYears")
    public void testPredicateReturnsFalseOutOfRange(int year) {
        assertFalse(observance.test(year),
            "Deepavali.test(" + year + ") should return false (outside coverage)");
    }

    // -------------------------------------------------------------------------
    // Group 5: null-safety from AbstractObservance contract
    // -------------------------------------------------------------------------

    @Test
    public void testApplyNullYearReturnsNull() {
        assertNull(observance.apply(null),
            "Deepavali.apply(null) must return null per AbstractObservance contract");
    }

    @Test
    public void testPredicateNullYearReturnsFalse() {
        assertFalse(observance.test(null),
            "Deepavali.test(null) must return false per AbstractObservance contract");
    }

    // -------------------------------------------------------------------------
    // Group 6: Boundary and seam spot checks
    // -------------------------------------------------------------------------

    @Test
    public void testOriginalCeilingYear2030() {
        assertEquals(observance.apply(2030), LocalDate.of(2030, Month.OCTOBER, 26),
            "2030 is the original table ceiling; must remain intact after extension");
    }

    @Test
    public void testFirstNewYear2031() {
        assertEquals(observance.apply(2031), LocalDate.of(2031, Month.NOVEMBER, 14),
            "2031 is the first newly-added year");
    }

    @Test
    public void testMidpointYear2042() {
        assertEquals(observance.apply(2042), LocalDate.of(2042, Month.NOVEMBER, 11),
            "2042 is near the midpoint of the extended range");
    }

    @Test
    public void testNewCeilingYear2055() {
        assertEquals(observance.apply(2055), LocalDate.of(2055, Month.OCTOBER, 19),
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
    // Group 7: Structural guard — table covers exactly 37 years (2019–2055)
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
