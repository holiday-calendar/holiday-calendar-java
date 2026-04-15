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
import java.time.Year;
import java.time.temporal.TemporalAdjusters;

/**
 * Observance of Family Day as a federal Bank Act statutory holiday - the third
 * Monday in February. This observance reflects the federal adoption of Family
 * Day, which was first observed by federally regulated institutions (including
 * the Bank of Canada) on <strong>11 February 2013</strong>.
 *
 * <p>This class is distinct from {@link FamilyDay}, which models the earlier
 * provincial Alberta adoption (1990). For the Bank of Canada Lynx settlement
 * calendar, the 2013 federal start date is authoritative.</p>
 *
 * @see FamilyDay
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class FamilyDayCAD implements Observance {

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        return Year.of(year)
                   .atMonth(Month.FEBRUARY)
                   .atDay(1)
                   .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY));
    }

    @Override
    public boolean test(Integer year) {
        return year != null && year >= 2013;
    }

}