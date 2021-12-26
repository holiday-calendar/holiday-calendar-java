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

package com.github.davejoyce.calendar;

import com.github.davejoyce.calendar.function.DateRoll;
import com.github.davejoyce.calendar.function.Observance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;

public final class TestObjects {

    private TestObjects() {}

    public static DateRoll createDateRollUS() {
        return dateToRoll -> {
            if (DayOfWeek.SATURDAY.equals(dateToRoll.getDayOfWeek())) return dateToRoll.minusDays(1);
            if (DayOfWeek.SUNDAY.equals(dateToRoll.getDayOfWeek())) return dateToRoll.plusDays(1);
            return dateToRoll;
        };
    }

    public static Observance createObservanceMlkDay() {
        return year -> Year.of(year)
                .atMonth(Month.JANUARY)
                .atDay(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY));
    }

    public static Observance createObservancePresidentsDay() {
        return year -> Year.of(year)
                .atMonth(Month.FEBRUARY)
                .atDay(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY));
    }

    public static Observance createObservanceMemorialDay() {
        return year -> (1970 < year) ? Year.of(year)
                                           .atMonth(Month.MAY)
                                           .atDay(1)
                                           .with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY))
                                     : LocalDate.of(year, Month.MAY, 30);
    }

    public static Observance createObservanceLaborDay() {
        return year -> Year.of(year)
                .atMonth(Month.SEPTEMBER)
                .atDay(1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    public static Observance createObservanceColumbusDay() {
        return year -> Year.of(year)
                .atMonth(Month.OCTOBER)
                .atDay(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.MONDAY));
    }

    public static Observance createObservanceThanksgiving() {
        return year -> Year.of(year)
                .atMonth(Month.NOVEMBER)
                .atDay(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY));
    }

}
