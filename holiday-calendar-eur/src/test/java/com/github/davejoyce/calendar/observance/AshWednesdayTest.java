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

public class AshWednesdayTest {

    private final WesternEaster westernEaster = new WesternEaster();

    @Test(dataProvider = "data")
    public void testApply(EasterObservance easterObservance, int yearToCalculate, LocalDate expected) {
        final AshWednesday ashWednesday = new AshWednesday(easterObservance);
        LocalDate actual = ashWednesday.apply(yearToCalculate);
        assertEquals(actual, expected);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ westernEaster, 1582, LocalDate.of(1582, Month.FEBRUARY, 28) }); // year before Gregorian calendar exists
        data.add(new Object[]{ westernEaster, 1583, LocalDate.of(1583, Month.FEBRUARY, 23) });
        data.add(new Object[]{ westernEaster, 1776, LocalDate.of(1776, Month.FEBRUARY, 21) });
        data.add(new Object[]{ westernEaster, 1918, LocalDate.of(1918, Month.FEBRUARY, 13) });
        data.add(new Object[]{ westernEaster, 2000, LocalDate.of(2000, Month.MARCH,     8) });
        data.add(new Object[]{ westernEaster, 2021, LocalDate.of(2021, Month.FEBRUARY, 17) });
        data.add(new Object[]{ westernEaster, 2022, LocalDate.of(2022, Month.MARCH,     2) });

        data.add(new Object[]{ westernEaster, 529, null });
        return data.iterator();
    }

}