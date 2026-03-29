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

/**
 * Holiday Calendar APAC module.
 *
 * <p>Provides holiday calendar implementations and observances for Asia-Pacific
 * countries, including lunar, Islamic, and Hindu calendar-based holidays.
 * Currently supports Singapore (SG).
 */
module holiday.calendar.apac {
    requires holiday.calendar.core;
    requires holiday.calendar.western;
    requires net.time4j.base;

    exports com.github.davejoyce.calendar.observance.lunar;
    exports com.github.davejoyce.calendar.observance.islamic;
    exports com.github.davejoyce.calendar.observance.hindu;

    provides com.github.davejoyce.calendar.HolidayCalendarService with
        com.github.davejoyce.calendar.impl.HolidayCalendarServiceSG;
}
