/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2022 David Joyce
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

package com.github.davejoyce.calendar.observance;

import com.github.davejoyce.calendar.function.EasterObservance;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public class PalmSundayTest {

    private final OrthodoxEaster orthodoxEaster = new OrthodoxEaster();
    private final WesternEaster westernEaster = new WesternEaster();

    @Test(dataProvider = "data")
    public void testApply(EasterObservance easterObservance, int yearToCalculate, LocalDate expected) {
        final PalmSunday palmSunday = new PalmSunday(easterObservance);
        LocalDate actual = palmSunday.apply(yearToCalculate);
        assertEquals(actual, expected);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ westernEaster, 1582, LocalDate.of(1582, Month.APRIL,  8) }); // year before Gregorian calendar exists
        data.add(new Object[]{ westernEaster, 1583, LocalDate.of(1583, Month.APRIL,  3) });
        data.add(new Object[]{ westernEaster, 1776, LocalDate.of(1776, Month.MARCH, 31) });
        data.add(new Object[]{ westernEaster, 1918, LocalDate.of(1918, Month.MARCH, 24) });
        data.add(new Object[]{ westernEaster, 2000, LocalDate.of(2000, Month.APRIL, 16) });
        data.add(new Object[]{ westernEaster, 2021, LocalDate.of(2021, Month.MARCH, 28) });
        data.add(new Object[]{ westernEaster, 2022, LocalDate.of(2022, Month.APRIL, 10) });

        data.add(new Object[]{ orthodoxEaster, 1582, LocalDate.of(1582, Month.APRIL,  8) }); // year before Gregorian calendar exists
        data.add(new Object[]{ orthodoxEaster, 1583, LocalDate.of(1583, Month.APRIL,  3) }); // Same as Western
        data.add(new Object[]{ orthodoxEaster, 1776, LocalDate.of(1776, Month.APRIL,  7) });
        data.add(new Object[]{ orthodoxEaster, 1918, LocalDate.of(1918, Month.APRIL, 28) });
        data.add(new Object[]{ orthodoxEaster, 1919, LocalDate.of(1919, Month.APRIL, 13) }); // Same as Western
        data.add(new Object[]{ orthodoxEaster, 2000, LocalDate.of(2000, Month.APRIL, 23) });
        data.add(new Object[]{ orthodoxEaster, 2021, LocalDate.of(2021, Month.APRIL, 25) });
        data.add(new Object[]{ orthodoxEaster, 2022, LocalDate.of(2022, Month.APRIL, 17) });

        data.add(new Object[]{ orthodoxEaster, 529, null });
        data.add(new Object[]{ orthodoxEaster, 3400, null });
        return data.iterator();
    }

}