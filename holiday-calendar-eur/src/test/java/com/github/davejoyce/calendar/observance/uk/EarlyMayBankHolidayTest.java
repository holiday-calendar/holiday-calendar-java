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

package com.github.davejoyce.calendar.observance.uk;

import com.github.davejoyce.calendar.observance.AbstractObservanceTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class EarlyMayBankHolidayTest extends AbstractObservanceTest<EarlyMayBankHoliday> {

    public EarlyMayBankHolidayTest() {
        super(new EarlyMayBankHoliday());
    }

    @Override
    protected List<Object[]> createData() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ 1977, null });
        data.add(new Object[]{ 1978, LocalDate.of(1978, Month.MAY, 1) });
        data.add(new Object[]{ 1990, LocalDate.of(1990, Month.MAY, 7) });
        data.add(new Object[]{ 2021, LocalDate.of(2021, Month.MAY, 3) });
        data.add(new Object[]{ 2022, LocalDate.of(2022, Month.MAY, 2) });

        return data;
    }

}