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
 * Observance of Memorial Day - a federal holiday in the United States for
 * mourning all US military personnel who died while serving in the United
 * States armed forces. Originally called <em>Decoration Day</em>, this holiday
 * was celebrated on May 30 from 1868 to 1970. As of 1971, this holiday always
 * falls on the last Monday in February.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class MemorialDay implements Observance {

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        return (1971 > year)
                ? LocalDate.of(year, Month.MAY, 30)
                : Year.of(year)
                      .atMonth(Month.MAY)
                      .atDay(1)
                      .with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY));
    }

    @Override
    public boolean test(Integer year) {
        return 1868 <= year;
    }

}
