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
 * Israel's Hebrew-calendar national holidays and their TASE/Bank of Israel
 * "erev" (eve) early-close counterparts:
 * {@link org.holiday.calendar.observance.hebrew.RoshHashanah} (and
 * {@code RoshHashanahDay2}),
 * {@link org.holiday.calendar.observance.hebrew.YomKippur},
 * {@link org.holiday.calendar.observance.hebrew.Sukkot},
 * {@link org.holiday.calendar.observance.hebrew.SheminiAtzeret},
 * {@link org.holiday.calendar.observance.hebrew.HoshanaRaba},
 * {@link org.holiday.calendar.observance.hebrew.Passover} (and
 * {@code PassoverEnd}),
 * {@link org.holiday.calendar.observance.hebrew.Shavuot},
 * {@link org.holiday.calendar.observance.hebrew.IndependenceDay} (with its
 * statutory postponement rule), and
 * {@link org.holiday.calendar.observance.hebrew.YomHazikaron} (Memorial Day,
 * coupled to the day before Independence Day). The
 * {@code Erev*} classes ({@code ErevRoshHashanah}, {@code ErevYomKippur},
 * {@code ErevSukkot}, {@code ErevPassover}, {@code ErevShavuot}) compute the
 * preceding day for {@code EARLY_CLOSE} modeling.
 *
 * <p>All dates are computed algorithmically via
 * <a href="https://www.time4j.net/">Time4J</a>'s
 * {@code net.time4j.calendar.HebrewCalendar}; no lookup tables or data
 * ceiling apply.
 *
 * @author <a href="mailto:dave@holiday-calendar.org">Dave Joyce</a>
 */
package org.holiday.calendar.observance.hebrew;
