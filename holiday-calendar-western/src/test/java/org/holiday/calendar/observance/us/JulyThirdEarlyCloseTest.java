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

public class JulyThirdEarlyCloseTest extends AbstractObservanceTest {

    public JulyThirdEarlyCloseTest() {
        super(new JulyThirdEarlyClose());
    }

    @Override
    protected List<Object[]> createData() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 2019, LocalDate.of(2019, Month.JULY, 3) });  // Jul 4 Thu -> eligible
        data.add(new Object[]{ 2020, null });                                // Jul 4 Sat -> ineligible
        data.add(new Object[]{ 2021, null });                                // Jul 4 Sun -> ineligible
        data.add(new Object[]{ 2022, null });                                // Jul 4 Mon -> ineligible (Monday-exclusion case)
        data.add(new Object[]{ 2023, LocalDate.of(2023, Month.JULY, 3) });   // Jul 4 Tue -> eligible
        data.add(new Object[]{ 2024, LocalDate.of(2024, Month.JULY, 3) });   // Jul 4 Thu -> eligible
        data.add(new Object[]{ 2025, LocalDate.of(2025, Month.JULY, 3) });   // Jul 4 Fri -> eligible
        data.add(new Object[]{ 2026, null });                                // Jul 4 Sat -> ineligible
        data.add(new Object[]{ 2027, null });                                // Jul 4 Sun -> ineligible
        data.add(new Object[]{ 2000, LocalDate.of(2000, Month.JULY, 3) });   // Jul 4 2000 Tue -> eligible, edge year
        data.add(new Object[]{ 2099, null });                                // Jul 4 2099 Sat -> ineligible, edge year
        return data;
    }

    @Test
    public void testSuppressedWhenIndependenceDayIsMonday() {
        // 2022: July 4 falls on a Monday. Must be ineligible — the issue text's
        // "Monday through Friday" claim was incorrect; the verified NYSE rule excludes Monday.
        assertNull(observance.apply(2022));
    }

}
