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

package org.holiday.calendar.observance.au;

import org.holiday.calendar.western.test.AbstractObservanceTest;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class ChristmasEveEarlyCloseTest extends AbstractObservanceTest {

    public ChristmasEveEarlyCloseTest() {
        super(new ChristmasEveEarlyClose());
    }

    @Override
    protected List<Object[]> createData() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 2011, null });                                   // Dec 24 Sat -> suppressed, pre-cycle edge year
        data.add(new Object[]{ 2020, LocalDate.of(2020, Month.DECEMBER, 24) }); // Dec 24 Thu -> present
        data.add(new Object[]{ 2021, LocalDate.of(2021, Month.DECEMBER, 24) }); // Dec 24 Fri -> present
        data.add(new Object[]{ 2022, null });                                   // Dec 24 Sat -> suppressed
        data.add(new Object[]{ 2023, null });                                   // Dec 24 Sun -> suppressed
        data.add(new Object[]{ 2024, LocalDate.of(2024, Month.DECEMBER, 24) }); // Dec 24 Tue -> present
        data.add(new Object[]{ 2025, LocalDate.of(2025, Month.DECEMBER, 24) }); // Dec 24 Wed -> present
        data.add(new Object[]{ 2026, LocalDate.of(2026, Month.DECEMBER, 24) }); // Dec 24 Thu -> present, confirmed by ASX's 2025/2026 notice
        data.add(new Object[]{ 2027, LocalDate.of(2027, Month.DECEMBER, 24) }); // Dec 24 Fri -> present
        data.add(new Object[]{ 2028, null });                                   // Dec 24 Sun -> suppressed
        data.add(new Object[]{ 2029, LocalDate.of(2029, Month.DECEMBER, 24) }); // Dec 24 Mon -> present, boundary check adjacent to 2028
        data.add(new Object[]{ 2033, null });                                   // Dec 24 Sat -> suppressed
        return data;
    }

    @Test
    public void testApplyNullYear() {
        assertNull(observance.apply(null));
    }

    @Test
    public void testTestNullYear() {
        assertEquals(observance.test(null), false);
    }

    @Test
    public void testDivergesFromUkShiftRuleIn2022() {
        LocalDate ukShifted = new org.holiday.calendar.observance.uk.ChristmasEveEarlyClose().apply(2022);
        assertEquals(ukShifted, LocalDate.of(2022, Month.DECEMBER, 23),
                "UK rule shifts to the preceding Friday and must not be null");
        assertNull(observance.apply(2022), "ASX rule must suppress entirely, not shift like UK");
    }

}
