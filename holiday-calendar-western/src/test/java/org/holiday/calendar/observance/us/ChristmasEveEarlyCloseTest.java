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

package org.holiday.calendar.observance.us;

import org.holiday.calendar.western.test.AbstractObservanceTest;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertNull;

public class ChristmasEveEarlyCloseTest extends AbstractObservanceTest {

    public ChristmasEveEarlyCloseTest() {
        super(new ChristmasEveEarlyClose());
    }

    @Override
    protected List<Object[]> createData() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 2019, LocalDate.of(2019, Month.DECEMBER, 24) }); // Dec 25 Wed -> eligible
        data.add(new Object[]{ 2020, LocalDate.of(2020, Month.DECEMBER, 24) }); // Dec 25 Fri -> eligible
        data.add(new Object[]{ 2021, null });                                   // Dec 25 Sat -> ineligible
        data.add(new Object[]{ 2022, null });                                   // Dec 25 Sun -> ineligible
        data.add(new Object[]{ 2023, null });                                   // Dec 25 Mon -> ineligible (Monday-exclusion case)
        data.add(new Object[]{ 2024, LocalDate.of(2024, Month.DECEMBER, 24) }); // Dec 25 Wed -> eligible
        data.add(new Object[]{ 2025, LocalDate.of(2025, Month.DECEMBER, 24) }); // Dec 25 Thu -> eligible
        data.add(new Object[]{ 2026, LocalDate.of(2026, Month.DECEMBER, 24) }); // Dec 25 Fri -> eligible
        data.add(new Object[]{ 2027, null });                                   // Dec 25 Sat -> ineligible
        data.add(new Object[]{ 2000, null });                                   // Dec 25 2000 Mon -> ineligible, edge year
        data.add(new Object[]{ 2099, LocalDate.of(2099, Month.DECEMBER, 24) }); // Dec 25 2099 Fri -> eligible, edge year
        return data;
    }

    @Test
    public void testSuppressedWhenChristmasDayIsMonday() {
        // 2023: December 25 falls on a Monday. Must be ineligible — the issue text's
        // "Monday through Friday" claim was incorrect; the verified NYSE rule excludes Monday.
        assertNull(observance.apply(2023));
    }

}
