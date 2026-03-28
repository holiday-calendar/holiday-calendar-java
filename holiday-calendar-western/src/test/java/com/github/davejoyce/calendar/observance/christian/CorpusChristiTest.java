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

package com.github.davejoyce.calendar.observance.christian;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class CorpusChristiTest {

    private final OrthodoxEaster orthodoxEaster = new OrthodoxEaster();
    private final WesternEaster westernEaster = new WesternEaster();

    @Test(dataProvider = "data", groups = "observance.christian")
    public void testApply(EasterObservance easterObservance, boolean adjustToSunday, int yearToCalculate, LocalDate expected) {
        final CorpusChristi corpusChristi = new CorpusChristi(easterObservance, adjustToSunday);
        LocalDate actual = corpusChristi.apply(yearToCalculate);
        assertEquals(actual, expected);
    }

    @Test(groups = "observance.christian")
    public void testApply_DefaultConstructor() {
        // 1-arg constructor defaults adjustToSunday=false; 60 days after Western Easter 2021 (April 4)
        final CorpusChristi corpusChristi = new CorpusChristi(westernEaster);
        LocalDate actual = corpusChristi.apply(2021);
        // Easter 2021 = April 4, +60 days = June 3
        assertEquals(actual, LocalDate.of(2021, Month.JUNE, 3));
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        // Western Easter 2021 = April 4; +60 = June 3 (Thursday), +63 = June 6 (Sunday)
        data.add(new Object[]{ westernEaster, false, 2021, LocalDate.of(2021, Month.JUNE,  3) });
        data.add(new Object[]{ westernEaster, true,  2021, LocalDate.of(2021, Month.JUNE,  6) });
        // Western Easter 2022 = April 17; +60 = June 16, +63 = June 19
        data.add(new Object[]{ westernEaster, false, 2022, LocalDate.of(2022, Month.JUNE, 16) });
        data.add(new Object[]{ westernEaster, true,  2022, LocalDate.of(2022, Month.JUNE, 19) });
        // Western Easter 2023 = April 9; +60 = June 8, +63 = June 11
        data.add(new Object[]{ westernEaster, false, 2023, LocalDate.of(2023, Month.JUNE,  8) });
        data.add(new Object[]{ westernEaster, true,  2023, LocalDate.of(2023, Month.JUNE, 11) });
        // Orthodox Easter 2021 = May 2; +60 = July 1, +63 = July 4
        data.add(new Object[]{ orthodoxEaster, false, 2021, LocalDate.of(2021, Month.JULY,  1) });
        data.add(new Object[]{ orthodoxEaster, true,  2021, LocalDate.of(2021, Month.JULY,  4) });
        // Orthodox Easter 2022 = April 24; +60 = June 23, +63 = June 26
        data.add(new Object[]{ orthodoxEaster, false, 2022, LocalDate.of(2022, Month.JUNE, 23) });
        data.add(new Object[]{ orthodoxEaster, true,  2022, LocalDate.of(2022, Month.JUNE, 26) });
        // Invalid year returns null
        data.add(new Object[]{ orthodoxEaster, false, 529, null });
        return data.iterator();
    }

}
