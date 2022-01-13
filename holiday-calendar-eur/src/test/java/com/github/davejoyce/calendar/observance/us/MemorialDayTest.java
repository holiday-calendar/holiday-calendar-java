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

package com.github.davejoyce.calendar.observance.us;

import com.github.davejoyce.calendar.observance.AbstractObservanceTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class MemorialDayTest extends AbstractObservanceTest<MemorialDay> {

    public MemorialDayTest() {
        super(new MemorialDay());
    }

    @Override
    protected List<Object[]> createData() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 1867, null });
        data.add(new Object[]{ 1868, LocalDate.of(1868, Month.MAY, 30) });
        data.add(new Object[]{ 1970, LocalDate.of(1970, Month.MAY, 30) });
        data.add(new Object[]{ 1990, LocalDate.of(1990, Month.MAY, 28) });
        data.add(new Object[]{ 2021, LocalDate.of(2021, Month.MAY, 31) });
        data.add(new Object[]{ 2022, LocalDate.of(2022, Month.MAY, 30) });

        return data;
    }

}