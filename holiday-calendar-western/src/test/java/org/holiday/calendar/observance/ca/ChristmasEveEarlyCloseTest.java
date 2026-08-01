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

package org.holiday.calendar.observance.ca;

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
        data.add(new Object[]{ 2017, null });                                   // Dec 24 Sun -> ineligible
        data.add(new Object[]{ 2018, LocalDate.of(2018, Month.DECEMBER, 24) }); // Dec 24 Mon -> eligible
        data.add(new Object[]{ 2019, LocalDate.of(2019, Month.DECEMBER, 24) }); // Dec 24 Tue -> eligible
        data.add(new Object[]{ 2021, LocalDate.of(2021, Month.DECEMBER, 24) }); // Dec 24 Fri -> eligible (NYSE-divergence year)
        data.add(new Object[]{ 2022, null });                                   // Dec 24 Sat -> ineligible
        data.add(new Object[]{ 2023, null });                                   // Dec 24 Sun -> ineligible
        data.add(new Object[]{ 2024, LocalDate.of(2024, Month.DECEMBER, 24) }); // Dec 24 Tue -> eligible
        data.add(new Object[]{ 2025, LocalDate.of(2025, Month.DECEMBER, 24) }); // Dec 24 Wed -> eligible
        data.add(new Object[]{ 2026, LocalDate.of(2026, Month.DECEMBER, 24) }); // Dec 24 Thu -> eligible
        // Edge years outside the sampled 2017-2026 cycle
        data.add(new Object[]{ 2033, null });                                   // Dec 24 Sat -> ineligible
        data.add(new Object[]{ 2034, null });                                   // Dec 24 Sun -> ineligible
        data.add(new Object[]{ 2099, LocalDate.of(2099, Month.DECEMBER, 24) }); // Dec 24 Thu -> eligible, far-future edge
        return data;
    }

    @Test
    public void testTsxClosesEarlyIn2021EvenThoughNyseRuleWouldSuppress() {
        // 2021: December 25 = Saturday, December 24 = Friday. NYSE's rule keys off
        // December 25 and would suppress when December 25 falls on Sat/Sun/Mon. TSX's
        // rule keys off December 24 directly: Friday is a valid weekday, so TSX still
        // closes early. This is the key behavioral divergence justifying a CA-specific
        // Observance rather than reusing the US one.
        assertEquals(observance.apply(2021), LocalDate.of(2021, Month.DECEMBER, 24));
    }

    @Test
    public void testDivergesFromUsRuleIn2021() {
        assertNull(new org.holiday.calendar.observance.us.ChristmasEveEarlyClose().apply(2021),
                "US rule (keyed off Dec 25) must suppress in 2021");
        assertEquals(observance.apply(2021), LocalDate.of(2021, Month.DECEMBER, 24),
                "CA/TSX rule (keyed off Dec 24) must NOT suppress in 2021");
    }

}
