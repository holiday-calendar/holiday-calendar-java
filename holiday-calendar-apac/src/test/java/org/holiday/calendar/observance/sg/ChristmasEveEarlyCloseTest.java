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

package org.holiday.calendar.observance.sg;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public class ChristmasEveEarlyCloseTest {

    private final ChristmasEveEarlyClose observance = new ChristmasEveEarlyClose();

    @DataProvider
    Iterator<Object[]> yearFixture() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 2017, null });                                   // Dec 24 Sun -> ineligible
        data.add(new Object[]{ 2018, LocalDate.of(2018, Month.DECEMBER, 24) }); // Dec 24 Mon -> eligible
        data.add(new Object[]{ 2019, LocalDate.of(2019, Month.DECEMBER, 24) }); // Dec 24 Tue -> eligible
        data.add(new Object[]{ 2020, LocalDate.of(2020, Month.DECEMBER, 24) }); // Dec 24 Thu -> eligible
        data.add(new Object[]{ 2021, LocalDate.of(2021, Month.DECEMBER, 24) }); // Dec 24 Fri -> eligible
        data.add(new Object[]{ 2022, null });                                   // Dec 24 Sat -> ineligible
        data.add(new Object[]{ 2023, null });                                   // Dec 24 Sun -> ineligible
        data.add(new Object[]{ 2024, LocalDate.of(2024, Month.DECEMBER, 24) }); // Dec 24 Tue -> eligible
        data.add(new Object[]{ 2025, LocalDate.of(2025, Month.DECEMBER, 24) }); // Dec 24 Wed -> eligible
        data.add(new Object[]{ 2026, LocalDate.of(2026, Month.DECEMBER, 24) }); // Dec 24 Thu -> eligible
        // Edge years outside the sampled 2018-2026 cycle
        data.add(new Object[]{ 2027, LocalDate.of(2027, Month.DECEMBER, 24) }); // Dec 24 Fri -> eligible
        data.add(new Object[]{ 2028, null });                                   // Dec 24 Sun -> ineligible
        data.add(new Object[]{ 2029, LocalDate.of(2029, Month.DECEMBER, 24) }); // Dec 24 Mon -> eligible
        data.add(new Object[]{ 2033, null });                                   // Dec 24 Sat -> ineligible
        return data.iterator();
    }

    @Test(dataProvider = "yearFixture")
    public void testApplyMatchesFixture(int year, LocalDate expected) {
        assertEquals(observance.apply(year), expected,
                "ChristmasEveEarlyClose.apply(" + year + ") mismatch");
    }

    @Test(dataProvider = "yearFixture")
    public void testPredicateMatchesFixture(int year, LocalDate expected) {
        assertEquals(observance.test(year), expected != null,
                "ChristmasEveEarlyClose.test(" + year + ") mismatch");
    }

    @Test
    public void testIsValidYearFalseWhenSaturday() {
        // 2033: December 24 is a Saturday.
        assertNull(observance.apply(2033));
    }

    @Test
    public void testIsValidYearFalseWhenSunday() {
        // 2028: December 24 is a Sunday.
        assertNull(observance.apply(2028));
    }

    @Test
    public void testApplyNullYearReturnsNull() {
        assertNull(observance.apply(null),
                "ChristmasEveEarlyClose.apply(null) must return null per AbstractObservance contract");
    }

    @Test
    public void testPredicateNullYearReturnsFalse() {
        assertFalse(observance.test(null),
                "ChristmasEveEarlyClose.test(null) must return false per AbstractObservance contract");
    }

}
