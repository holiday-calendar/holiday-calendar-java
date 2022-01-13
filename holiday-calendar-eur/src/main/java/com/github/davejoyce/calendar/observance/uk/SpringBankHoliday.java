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

import com.github.davejoyce.calendar.function.Observance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

/**
 * Observance of Spring or Late May bank holiday - a statutory bank holiday in
 * the United Kingdom since 1971. In years when a Jubilee is celebrated for the
 * anniversary of the accession of the sovereign, this holiday's observed date
 * is adjusted to adjoin the Jubilee bank holiday to create a 4-day weekend.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class SpringBankHoliday implements Observance {

    private final Map<Integer, LocalDate> jubileeYearDates;

    public SpringBankHoliday() {
        jubileeYearDates = new HashMap<>();
        jubileeYearDates.put(2002, LocalDate.of(2002, Month.JUNE, 4));
        jubileeYearDates.put(2012, LocalDate.of(2012, Month.JUNE, 4));
        jubileeYearDates.put(2022, LocalDate.of(2022, Month.JUNE, 2));
    }

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        return jubileeYearDates.getOrDefault(year, Year.of(year)
                                                       .atMonth(Month.MAY)
                                                       .atDay(1)
                                                       .with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY)));
    }

    @Override
    public boolean test(Integer year) {
        return 1971 <= year;
    }

}
