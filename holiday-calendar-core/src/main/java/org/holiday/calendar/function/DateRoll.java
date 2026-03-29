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

package org.holiday.calendar.function;

import org.holiday.calendar.HolidayCalendar;

import java.time.LocalDate;

/**
 * Defines date adjustment behavior for holiday observance when the calculated
 * date falls on a weekend day. Date roll behavior is a defined attribute of a
 * published holiday calendar.
 * <p>This is a functional interface, supporting definition of date roll
 * behavior for a {@link HolidayCalendar} as a
 * lambda expression.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
@FunctionalInterface
public interface DateRoll {

    /**
     * Roll the calculated date for the specified holiday in the given year to
     * the nearest valid date.
     * @param dateToRoll calculated holiday date to be rolled
     * @return holiday date (adjusted for valid observance)
     */
    LocalDate rollToObservedDate(LocalDate dateToRoll);

    /**
     * Returns a composed {@code DateRoll} that first applies this roll, then
     * applies {@code after}.
     *
     * @param after the roll to apply after this one
     * @return composed date roll
     */
    default DateRoll andThen(DateRoll after) {
        return date -> after.rollToObservedDate(this.rollToObservedDate(date));
    }

}
