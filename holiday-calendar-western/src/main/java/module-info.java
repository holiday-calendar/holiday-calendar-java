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

/**
 * Holiday Calendar western module.
 *
 * <p>Provides holiday calendar implementations and observances for Western
 * countries: Australia, Canada, France, Germany, Switzerland, the United
 * Kingdom, and the United States.
 */
module holiday.calendar.western {
    requires holiday.calendar.core;

    exports com.github.davejoyce.calendar.observance;
    exports com.github.davejoyce.calendar.observance.christian;
    exports com.github.davejoyce.calendar.observance.au;
    exports com.github.davejoyce.calendar.observance.ca;
    exports com.github.davejoyce.calendar.observance.eu;
    exports com.github.davejoyce.calendar.observance.uk;
    exports com.github.davejoyce.calendar.observance.us;

    provides com.github.davejoyce.calendar.HolidayCalendarService with
        com.github.davejoyce.calendar.impl.HolidayCalendarServiceAU,
        com.github.davejoyce.calendar.impl.HolidayCalendarServiceCA,
        com.github.davejoyce.calendar.impl.HolidayCalendarServiceCH,
        com.github.davejoyce.calendar.impl.HolidayCalendarServiceDE,
        com.github.davejoyce.calendar.impl.HolidayCalendarServiceFR,
        com.github.davejoyce.calendar.impl.HolidayCalendarServiceUK,
        com.github.davejoyce.calendar.impl.HolidayCalendarServiceUS;
}
