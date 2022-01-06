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

import com.github.davejoyce.calendar.function.Observance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;

/**
 * Observance of Labor Day - a federal holiday in the United States that occurs
 * on the first Monday in September. One of several Labour Day celebrations that
 * occur worldwide, the American observance occurs on the same day as Labour Day
 * in Canada.
 *
 * @see com.github.davejoyce.calendar.observance.MayDay
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class LaborDay implements Observance {

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        return Year.of(year)
                   .atMonth(Month.SEPTEMBER)
                   .atDay(1)
                   .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    @Override
    public boolean test(Integer year) {
        return 1894 <= year;
    }

}
