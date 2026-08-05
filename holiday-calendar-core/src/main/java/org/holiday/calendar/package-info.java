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
 * Base classes and interfaces of the Holiday Calendar API.
 *
 * <p>{@link org.holiday.calendar.Holiday} is a sealed interface describing a
 * single holiday's identity and date-calculation method, with four permitted
 * subtypes: {@link org.holiday.calendar.FixedHoliday} (same {@code MonthDay}
 * every year), {@link org.holiday.calendar.FloatingHoliday} (date computed via
 * an {@link org.holiday.calendar.function.Observance}),
 * {@link org.holiday.calendar.SpecialAnniversary} (anniversary of a fixed
 * date), and {@link org.holiday.calendar.EarlyCloseHoliday} (a non-rollable
 * partial trading day, carrying a local close time and zone).
 * {@link org.holiday.calendar.HolidayCalendar} is a named, immutable
 * collection of holidays, plus a {@link org.holiday.calendar.function.DateRoll}
 * weekend-adjustment strategy, that calculates the observed
 * {@link org.holiday.calendar.HolidayDate}s for a given year.
 *
 * <p>{@link org.holiday.calendar.HolidayCalendarService} is the extension
 * point implemented by each regional module and discovered at runtime by
 * {@link org.holiday.calendar.HolidayCalendarFactory} via
 * {@link java.util.ServiceLoader}:
 * <pre>{@code
 * HolidayCalendarFactory factory = new HolidayCalendarFactory();
 * HolidayCalendar calendar = factory.create("XNYS");
 *
 * List<HolidayDate> fullClosures = calendar.calculate(2026);
 * if (calendar.hasEarlyCloses()) {
 *     List<HolidayDate> earlyCloses = calendar.calculateEarlyCloses(2026);
 * }
 * }</pre>
 *
 * @author <a href="mailto:dave@holiday-calendar.org">Dave Joyce</a>
 */
package org.holiday.calendar;