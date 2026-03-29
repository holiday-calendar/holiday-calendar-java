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

package com.github.davejoyce.calendar.observance.christian;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class ShroveTuesdayTest {

    private final OrthodoxEaster orthodoxEaster = new OrthodoxEaster();
    private final WesternEaster westernEaster = new WesternEaster();

    @Test(dataProvider = "data", groups = "observance.christian")
    public void testApply(EasterObservance easterObservance, int yearToCalculate, LocalDate expected) {
        final ShroveTuesday shroveTuesday = new ShroveTuesday(easterObservance);
        LocalDate actual = shroveTuesday.apply(yearToCalculate);
        assertEquals(actual, expected);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        // Western Easter 2021 = April 4; -47 = February 16
        data.add(new Object[]{ westernEaster, 2021, LocalDate.of(2021, Month.FEBRUARY, 16) });
        // Western Easter 2022 = April 17; -47 = March 1
        data.add(new Object[]{ westernEaster, 2022, LocalDate.of(2022, Month.MARCH,  1) });
        // Western Easter 2023 = April 9; -47 = February 21
        data.add(new Object[]{ westernEaster, 2023, LocalDate.of(2023, Month.FEBRUARY, 21) });
        // Orthodox Easter 2021 = May 2; -47 = March 16
        data.add(new Object[]{ orthodoxEaster, 2021, LocalDate.of(2021, Month.MARCH, 16) });
        // Orthodox Easter 2022 = April 24; -47 = March 8
        data.add(new Object[]{ orthodoxEaster, 2022, LocalDate.of(2022, Month.MARCH,  8) });
        // Invalid year returns null
        data.add(new Object[]{ orthodoxEaster, 529, null });
        return data.iterator();
    }

}
