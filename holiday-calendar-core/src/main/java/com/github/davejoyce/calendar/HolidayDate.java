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

package com.github.davejoyce.calendar;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

/**
 * The observed date of a {@link Holiday} in a particular year of the Common
 * Era (CE). Instances of this class are immutable and thread safe.
 *
 * @see HolidayCalendar#calculate(int)
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public record HolidayDate(Holiday holiday, LocalDate date) {

    public HolidayDate {
        requireNonNull(holiday, "Argument 'holiday' cannot be null");
        requireNonNull(date, "Argument 'date' cannot be null");
    }

    /** Backward-compatible accessor. */
    public Holiday getHoliday() { return holiday; }

    /** Backward-compatible accessor. */
    public LocalDate getDate() { return date; }

}
