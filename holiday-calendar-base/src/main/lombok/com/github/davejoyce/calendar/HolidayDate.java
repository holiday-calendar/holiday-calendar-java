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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

/**
 * The observed date of a {@link Holiday} in a particular year of the Common
 * Era (CE). Instances of this class are immutable and thread safe.
 *
 * @see HolidayCalendar#calculate(int)
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
@Data
@AllArgsConstructor
public class HolidayDate {

    @NonNull
    private final Holiday holiday;

    @NonNull
    private final LocalDate date;

}
