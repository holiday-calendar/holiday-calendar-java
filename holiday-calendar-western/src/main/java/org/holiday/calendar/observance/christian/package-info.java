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
 * Christian moveable-feast observances shared across multiple Western (and
 * MENA) calendars.
 *
 * <p>{@link org.holiday.calendar.observance.christian.WesternEaster} and
 * {@link org.holiday.calendar.observance.christian.OrthodoxEaster} compute
 * Easter Sunday under the Gregorian and Julian-calendar algorithms
 * respectively; both implement
 * {@link org.holiday.calendar.observance.christian.EasterObservance}. The
 * remaining classes in this package (
 * {@link org.holiday.calendar.observance.christian.AshWednesday},
 * {@link org.holiday.calendar.observance.christian.PalmSunday},
 * {@link org.holiday.calendar.observance.christian.GoodFriday},
 * {@link org.holiday.calendar.observance.christian.EasterMonday},
 * {@link org.holiday.calendar.observance.christian.AscensionDay},
 * {@link org.holiday.calendar.observance.christian.WhitSunday},
 * {@link org.holiday.calendar.observance.christian.WhitMonday}, and
 * {@link org.holiday.calendar.observance.christian.CorpusChristi}) are
 * {@link org.holiday.calendar.observance.CompositeObservance}s computed as a
 * fixed offset from an {@code EasterObservance} base:
 * <pre>{@code
 * Observance goodFriday = new GoodFriday(new WesternEaster());
 * Observance easterMonday = new EasterMonday(new WesternEaster());
 * }</pre>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
package org.holiday.calendar.observance.christian;
