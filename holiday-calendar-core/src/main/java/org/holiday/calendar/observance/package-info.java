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
 * Base classes for {@link org.holiday.calendar.function.Observance}
 * implementations, used throughout the regional calendar modules.
 *
 * <p>{@link org.holiday.calendar.observance.AbstractObservance} supplies
 * standard null-guard handling and a year-validity hook, leaving subclasses
 * to implement only {@code computeDate(int)}:
 * <pre>{@code
 * public class Juneteenth extends AbstractObservance {
 *     protected LocalDate computeDate(int year) {
 *         return LocalDate.of(year, Month.JUNE, 19);
 *     }
 *     protected boolean isValidYear(int year) {
 *         return year >= 2021;
 *     }
 * }
 * }</pre>
 *
 * <p>{@link org.holiday.calendar.observance.CompositeObservance} extends
 * {@code AbstractObservance} for holidays computed relative to another
 * observance (most commonly an Easter algorithm), delegating year validity to
 * that base observance — see the {@code observance.christian} package in the
 * western module for examples such as Good Friday and Easter Monday.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
package org.holiday.calendar.observance;