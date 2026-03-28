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
 * Observance of Thanksgiving - a day to give thanks for the things one has at
 * the close of the harvest season. This national holiday is observed on the
 * second Monday in October.
 * <p>Note that this public holiday is similar to, but distinct from, the
 * holiday of the same name in the United States.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class Thanksgiving implements Observance {

    @Override
    public LocalDate apply(Integer year) {
        return Year.of(year)
                   .atMonth(Month.OCTOBER)
                   .atDay(1)
                   .with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.MONDAY));
    }

}
