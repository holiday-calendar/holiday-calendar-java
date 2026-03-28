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

package com.github.davejoyce.calendar;

/**
 * Required behavior of a service which provides the {@link HolidayCalendar}
 * object assigned to a unique <em>code</em> identifier.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface HolidayCalendarService {

    /**
     * Determine if this service provides the {@link HolidayCalendar} for the
     * specified code.
     *
     * @param code short code identifier of desired holiday calendar
     * @return true if this service provides the holiday calendar for the given
     *         code, false otherwise
     */
    boolean isProvided(String code);

    /**
     * Get the holiday calendar object provided by this service.
     *
     * @return holiday calendar object
     */
    HolidayCalendar getHolidayCalendar();

}
