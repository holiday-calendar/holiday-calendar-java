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

public class WhitSundayTest {

    private final OrthodoxEaster orthodoxEaster = new OrthodoxEaster();
    private final WesternEaster westernEaster = new WesternEaster();

    @Test(dataProvider = "data", groups = "observance.christian")
    public void testApply(EasterObservance easterObservance, int yearToCalculate, LocalDate expected) {
        final WhitSunday whitSunday = new WhitSunday(easterObservance);
        LocalDate actual = whitSunday.apply(yearToCalculate);
        assertEquals(actual, expected);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        // Western Easter 2021 = April 4; +49 = May 23
        data.add(new Object[]{ westernEaster, 2021, LocalDate.of(2021, Month.MAY, 23) });
        // Western Easter 2022 = April 17; +49 = June 5
        data.add(new Object[]{ westernEaster, 2022, LocalDate.of(2022, Month.JUNE,  5) });
        // Western Easter 2023 = April 9; +49 = May 28
        data.add(new Object[]{ westernEaster, 2023, LocalDate.of(2023, Month.MAY, 28) });
        // Orthodox Easter 2021 = May 2; +49 = June 20
        data.add(new Object[]{ orthodoxEaster, 2021, LocalDate.of(2021, Month.JUNE, 20) });
        // Orthodox Easter 2022 = April 24; +49 = June 12
        data.add(new Object[]{ orthodoxEaster, 2022, LocalDate.of(2022, Month.JUNE, 12) });
        // Invalid year returns null
        data.add(new Object[]{ orthodoxEaster, 529, null });
        return data.iterator();
    }

}
