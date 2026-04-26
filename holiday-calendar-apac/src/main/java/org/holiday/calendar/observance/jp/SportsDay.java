/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2021-2026 The Holiday Calendar Project Contributors
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

package org.holiday.calendar.observance.jp;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;

/**
 * Observance of Sports Day (スポーツの日), formerly Health and Sports Day
 * (体育の日) — a Japanese national holiday promoting sporting activity.
 *
 * <p>Historical rule changes:
 * <ul>
 *   <li>1966–1999: fixed on October 10 (called Health and Sports Day)</li>
 *   <li>2000+: 2nd Monday in October (Happy Monday System; renamed Sports Day
 *       in 2020)</li>
 * </ul>
 */
public class SportsDay extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        if (year == 2020) {
            return LocalDate.of(2020, Month.JULY, 24);
        }
        if (year == 2021) {
            return LocalDate.of(2021, Month.JULY, 23);
        }
        if (year < 2000) {
            return LocalDate.of(year, Month.OCTOBER, 10);
        }
        return Year.of(year)
                   .atMonth(Month.OCTOBER)
                   .atDay(1)
                   .with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.MONDAY));
    }

    @Override
    protected boolean isValidYear(int year) {
        return year >= 1966;
    }
}
