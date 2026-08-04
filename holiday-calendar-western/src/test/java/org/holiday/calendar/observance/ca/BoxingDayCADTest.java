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

import static org.testng.Assert.assertNull;

public class BoxingDayCADTest extends AbstractObservanceTest {

    public BoxingDayCADTest() {
        super(new BoxingDayCAD());
    }

    @Test
    public void testApply_NullYear() {
        assertNull(observance.apply(null));
    }

    @Override
    protected List<Object[]> createData() {
        List<Object[]> data = new ArrayList<>();
        // Dec 25 = Friday, Dec 26 = Saturday -> Boxing Day observed Mon 28
        data.add(new Object[]{ 2020, LocalDate.of(2020, Month.DECEMBER, 28) });
        // Dec 25 = Saturday -> Christmas observed Mon 27; Boxing Day observed Tue 28
        data.add(new Object[]{ 2021, LocalDate.of(2021, Month.DECEMBER, 28) });
        // Dec 25 = Sunday -> Christmas observed Mon 26; Boxing Day observed Tue 27
        data.add(new Object[]{ 2022, LocalDate.of(2022, Month.DECEMBER, 27) });
        // Dec 25 = Monday -> no collision, Boxing Day observed on natural date
        data.add(new Object[]{ 2023, LocalDate.of(2023, Month.DECEMBER, 26) });
        // Dec 25 = Wednesday -> no collision, Boxing Day observed on natural date
        data.add(new Object[]{ 2024, LocalDate.of(2024, Month.DECEMBER, 26) });

        return data;
    }

}
