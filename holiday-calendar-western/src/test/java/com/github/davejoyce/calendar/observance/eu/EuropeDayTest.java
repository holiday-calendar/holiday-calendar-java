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

package com.github.davejoyce.calendar.observance.eu;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public class EuropeDayTest {

    @Test(dataProvider = "data")
    public void testApply(int yearToCalculate, boolean forEuropeanUnion, LocalDate expected) {
        final EuropeDay europeDay = new EuropeDay(forEuropeanUnion);
        LocalDate actual = europeDay.apply(yearToCalculate);
        assertEquals(actual, expected);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 1963, true, null });
        data.add(new Object[]{ 1963, false, null });
        data.add(new Object[]{ 1964, true, LocalDate.of(1964, Month.MAY, 9) });
        data.add(new Object[]{ 1964, false, LocalDate.of(1964, Month.MAY, 5) });
        data.add(new Object[]{ 2022, true, LocalDate.of(2022, Month.MAY, 9) });
        data.add(new Object[]{ 2022, false, LocalDate.of(2022, Month.MAY, 5) });

        return data.iterator();
    }

}