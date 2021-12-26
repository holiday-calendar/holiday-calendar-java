/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2021 David Joyce
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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.assertEquals;


public class OrthodoxEasterTest {

    private final OrthodoxEaster orthodoxEaster = new OrthodoxEaster();

    @Test(dataProvider = "data")
    public void testApply(int yearToCalculate, LocalDate expected) {
        LocalDate actual = orthodoxEaster.apply(yearToCalculate);
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "predicateData")
    public void testTest(int yearToCheck, boolean expected) {
        boolean actual = orthodoxEaster.test(yearToCheck);
        assertEquals(actual, expected);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 1582, LocalDate.of(1582, Month.APRIL, 15) }); // year before Gregorian calendar exists
        data.add(new Object[]{ 1583, LocalDate.of(1583, Month.APRIL, 10) }); // Same as Western
        data.add(new Object[]{ 1776, LocalDate.of(1776, Month.APRIL, 14) });
        data.add(new Object[]{ 1918, LocalDate.of(1918, Month.MAY,    5) });
        data.add(new Object[]{ 1919, LocalDate.of(1919, Month.APRIL, 20) }); // Same as Western
        data.add(new Object[]{ 2000, LocalDate.of(2000, Month.APRIL, 30) });
        data.add(new Object[]{ 2021, LocalDate.of(2021, Month.MAY,    2) });
        data.add(new Object[]{ 2022, LocalDate.of(2022, Month.APRIL, 24) });

        data.add(new Object[]{ 529, null });
        data.add(new Object[]{ 3400, null });

        return data.iterator();
    }

    @DataProvider
    public Iterator<Object[]> predicateData() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 529, false });
        data.add(new Object[]{ 530, true });
        data.add(new Object[]{ 3399, true });
        data.add(new Object[]{ 3400, false });

        return data.iterator();
    }

}