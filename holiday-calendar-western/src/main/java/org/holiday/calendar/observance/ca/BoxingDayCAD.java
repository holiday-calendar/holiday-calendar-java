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

package org.holiday.calendar.observance.ca;

import org.holiday.calendar.function.Observance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

/**
 * Observance of Boxing Day for the Bank of Canada (Lynx) settlement calendar.
 *
 * <p>Boxing Day (26 December) must be computed in tandem with Christmas Day
 * (25 December) because the two holidays can collide when Christmas falls on a
 * weekend. The framework's {@code DateRoll} mechanism operates independently per
 * holiday and cannot detect when Christmas Day has already consumed 26 December
 * as its observed Monday substitute. This observance therefore encapsulates the
 * full substitution logic for the Christmas/Boxing Day pair:</p>
 *
 * <ul>
 *   <li><strong>Dec 25 = Saturday</strong> &rarr; Christmas observed Mon 27;
 *       Boxing Day (Dec 26 = Sunday) observed Tue 28.</li>
 *   <li><strong>Dec 25 = Sunday</strong> &rarr; Christmas observed Mon 26;
 *       Boxing Day displaced to Tue 27.</li>
 *   <li><strong>Dec 26 = Saturday</strong> (Christmas is Friday) &rarr;
 *       Christmas not rolled; Boxing Day observed Mon 28.</li>
 *   <li><strong>Dec 26 = Sunday</strong> (Christmas is Saturday &mdash; covered
 *       by first case above; or Christmas is a different weekday) &rarr;
 *       Boxing Day observed Mon 27.</li>
 *   <li>All other cases &rarr; Boxing Day observed on its natural date, 26
 *       December.</li>
 * </ul>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class BoxingDayCAD implements Observance {

    @Override
    public LocalDate apply(Integer year) {
        final LocalDate christmas = LocalDate.of(year, Month.DECEMBER, 25);
        final LocalDate boxingDay = LocalDate.of(year, Month.DECEMBER, 26);
        final DayOfWeek christmasDow = christmas.getDayOfWeek();
        final DayOfWeek boxingDow    = boxingDay.getDayOfWeek();

        if (christmasDow == DayOfWeek.SATURDAY) return boxingDay.plusDays(2L); // Dec 28 (Tue)
        if (christmasDow == DayOfWeek.SUNDAY)   return boxingDay.plusDays(1L); // Dec 27 (Tue)
        if (boxingDow    == DayOfWeek.SATURDAY) return boxingDay.plusDays(2L); // Dec 28 (Mon)
        if (boxingDow    == DayOfWeek.SUNDAY)   return boxingDay.plusDays(1L); // Dec 27 (Mon)
        return boxingDay;
    }

}