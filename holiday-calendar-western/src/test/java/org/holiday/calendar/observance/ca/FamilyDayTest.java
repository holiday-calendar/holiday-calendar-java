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

package org.holiday.calendar.observance.ca;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class FamilyDayTest {

    private final FamilyDay familyDay = new FamilyDay();

    @Test(dataProvider = "data")
    public void testApply(Integer yearToCalculate, LocalDate expected) {
        LocalDate actual = familyDay.apply(yearToCalculate);
        assertEquals(actual, expected);
    }

    @Test
    public void testApply_NullYear() {
        assertNull(familyDay.apply(null));
    }

    @Test
    public void testApply_YearBefore1990() {
        // FamilyDay was not observed before 1990
        assertNull(familyDay.apply(1989));
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        // Third Monday in February 1990 = February 19
        data.add(new Object[]{ 1990, LocalDate.of(1990, Month.FEBRUARY, 19) });
        // Third Monday in February 2021 = February 15
        data.add(new Object[]{ 2021, LocalDate.of(2021, Month.FEBRUARY, 15) });
        // Third Monday in February 2022 = February 21
        data.add(new Object[]{ 2022, LocalDate.of(2022, Month.FEBRUARY, 21) });
        return data.iterator();
    }

}
