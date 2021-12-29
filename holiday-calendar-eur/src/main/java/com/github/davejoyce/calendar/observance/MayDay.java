/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2021 David Joyce
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

package com.github.davejoyce.calendar.observance;

import com.github.davejoyce.calendar.function.Observance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;

/**
 * Observance of May Day - an ancient festival marking the first day of summer.
 * It is a current traditional spring holiday in many European cultures that is
 * celebrated as a public holiday on 1 May or the 1st Monday of May.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class MayDay implements Observance {

    private final boolean onFirstMonday;

    public MayDay(boolean onFirstMonday) {
        this.onFirstMonday = onFirstMonday;
    }

    public MayDay() {
        this(false);
    }

    @Override
    public LocalDate apply(Integer year) {
        LocalDate actual = Year.of(year).atMonth(Month.MAY).atDay(1);
        return onFirstMonday ? actual.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)) : actual;
    }

}
