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
 * Holiday Calendar core API module.
 *
 * <p>Provides the core abstractions for defining, building, and calculating
 * holiday calendars: {@code Holiday}, {@code HolidayCalendar},
 * {@code HolidayCalendarService}, {@code HolidayCalendarFactory}, and
 * associated functional interfaces.
 */
module org.holiday.calendar.core {
    requires org.slf4j;

    exports org.holiday.calendar;
    exports org.holiday.calendar.function;
    exports org.holiday.calendar.observance;
    exports org.holiday.calendar.util;

    uses org.holiday.calendar.HolidayCalendarService;
}
