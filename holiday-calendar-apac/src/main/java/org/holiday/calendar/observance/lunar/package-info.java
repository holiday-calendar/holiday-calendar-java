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
 * Implementations of {@link org.holiday.calendar.function.Observance} for
 * Chinese lunisolar-calendar holidays: Chinese New Year (
 * {@link org.holiday.calendar.observance.lunar.ChineseNewYearDay} parameterized
 * by day number 1&ndash;7, plus the convenience
 * {@link org.holiday.calendar.observance.lunar.ChineseNewYearFirstDay} and
 * {@link org.holiday.calendar.observance.lunar.ChineseNewYearSecondDay}),
 * {@link org.holiday.calendar.observance.lunar.QingmingFestival},
 * {@link org.holiday.calendar.observance.lunar.DragonBoatFestival},
 * {@link org.holiday.calendar.observance.lunar.MidAutumnFestival}, and
 * {@link org.holiday.calendar.observance.lunar.VesakDay}.
 *
 * <p>Chinese New Year's Day is computed algorithmically via Time4J's
 * {@code net.time4j.calendar.ChineseCalendar}; other festivals in this
 * package are similarly computed from the underlying lunisolar calendar
 * rather than lookup tables:
 * <pre>{@code
 * Observance cnyDay1 = new ChineseNewYearFirstDay();
 * Observance cnyDay3 = new ChineseNewYearDay(3);
 * }</pre>
 *
 * @author <a href="mailto:dave@holiday-calendar.org">Dave Joyce</a>
 */
package org.holiday.calendar.observance.lunar;
