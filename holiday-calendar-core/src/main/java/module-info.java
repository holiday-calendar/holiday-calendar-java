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
 * holiday calendars: {@code Holiday} (and its permitted subtypes, including
 * the {@code EarlyCloseHoliday} half-day close), {@code HolidayCalendar},
 * {@code HolidayCalendarService}, {@code HolidayCalendarFactory}, and the
 * {@code Observance}/{@code DateRoll} functional interfaces used to plug in
 * per-region date calculation and weekend-rolling behavior. Regional
 * implementations (the {@code western}, {@code apac}, and {@code mena}
 * modules) depend on this module and register their
 * {@code HolidayCalendarService} providers via {@link java.util.ServiceLoader}.
 *
 * <p>Typical read-only usage — locate a calendar by code and calculate a
 * year's holidays without depending on which module provides it:
 * <pre>{@code
 * HolidayCalendarFactory factory = new HolidayCalendarFactory();
 * HolidayCalendar xnys = factory.create("XNYS");
 * List<HolidayDate> holidays = xnys.calculate(2026);
 * List<HolidayDate> earlyCloses = xnys.calculateEarlyCloses(2026);
 * }</pre>
 */
module org.holiday.calendar.core {
    requires org.slf4j;

    exports org.holiday.calendar;
    exports org.holiday.calendar.function;
    exports org.holiday.calendar.observance;
    exports org.holiday.calendar.util;

    uses org.holiday.calendar.HolidayCalendarService;
}
