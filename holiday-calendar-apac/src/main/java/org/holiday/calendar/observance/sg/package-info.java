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
 * holidays observed exclusively on Singapore Exchange (SGX).
 *
 * <p>{@link org.holiday.calendar.observance.sg.ChristmasEveEarlyClose} and
 * {@link org.holiday.calendar.observance.sg.NewYearsEveEarlyClose} model
 * SGX's {@code EARLY_CLOSE} half-day securities sessions (09:00&ndash;12:00
 * SGT) on December 24 and 31; the close is simply not observed (not shifted)
 * in years those dates fall on a weekend.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
package org.holiday.calendar.observance.sg;
