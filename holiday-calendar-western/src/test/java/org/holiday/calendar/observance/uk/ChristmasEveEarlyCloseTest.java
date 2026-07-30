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

package org.holiday.calendar.observance.uk;

import org.holiday.calendar.western.test.AbstractObservanceTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class ChristmasEveEarlyCloseTest extends AbstractObservanceTest {

    public ChristmasEveEarlyCloseTest() {
        super(new ChristmasEveEarlyClose());
    }

    @Override
    protected List<Object[]> createData() {
        List<Object[]> data = new ArrayList<>();
        // Weekday years: December 24 falls Mon-Fri, date unchanged.
        data.add(new Object[]{ 2024, LocalDate.of(2024, Month.DECEMBER, 24) }); // Tuesday
        data.add(new Object[]{ 2025, LocalDate.of(2025, Month.DECEMBER, 24) }); // Wednesday
        data.add(new Object[]{ 2026, LocalDate.of(2026, Month.DECEMBER, 24) }); // Thursday
        // Weekend years: shift to the preceding Friday.
        data.add(new Object[]{ 2022, LocalDate.of(2022, Month.DECEMBER, 23) }); // Dec 24 is Saturday
        data.add(new Object[]{ 2023, LocalDate.of(2023, Month.DECEMBER, 22) }); // Dec 24 is Sunday
        // 2028: directly confirmed against LSE's official business days notice
        // (londonstockexchange.com/equities-trading/business-days), which lists
        // Friday 22 December 2028 as a "Christmas Holiday half day."
        data.add(new Object[]{ 2028, LocalDate.of(2028, Month.DECEMBER, 22) }); // Dec 24 is Sunday

        return data;
    }
}
