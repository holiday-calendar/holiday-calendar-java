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

package com.github.davejoyce.calendar.observance.ca;

import com.github.davejoyce.calendar.function.Observance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;

/**
 * Observance of Victoria Day - a federal Canadian public holiday on the last
 * Monday in May preceding May 25, celebrated as the official birthday of the
 * sovereign of Canada. The holiday, a distinctly Canadian observance (as
 * opposed to other realms of the Crown), originally fell on Queen Victoria's
 * actual birthday (May 24).
 * <p>This holiday has been observed in Canada since 1845. It falls on the
 * Monday between the 18th and the 24th (inclusive), and it is therefore always
 * the next to last Monday of May.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class VictoriaDay implements Observance {

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        return Year.of(year)
                   .atMonth(Month.MAY)
                   .atDay(1)
                   .with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY))
                   .minusWeeks(1);
    }

    @Override
    public boolean test(Integer year) {
        return 1845 <= year;
    }

}
