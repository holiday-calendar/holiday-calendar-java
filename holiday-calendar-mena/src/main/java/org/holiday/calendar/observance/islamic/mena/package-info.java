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
 * Islamic (Hijri) calendar holidays observed across the MENA module:
 * {@link org.holiday.calendar.observance.islamic.mena.EidAlFitr} (and its
 * multi-day variants {@code EidAlFitrDay2}&ndash;{@code Day4}),
 * {@link org.holiday.calendar.observance.islamic.mena.EidAlAdha} (and
 * {@code EidAlAdhaDay2}&ndash;{@code Day4}),
 * {@link org.holiday.calendar.observance.islamic.mena.ArafatDay},
 * {@link org.holiday.calendar.observance.islamic.mena.IslamicNewYear},
 * {@link org.holiday.calendar.observance.islamic.mena.Ashura} (and
 * {@code AshuraDay2}),
 * {@link org.holiday.calendar.observance.islamic.mena.ProphetsBirthday} (and
 * {@code ProphetsBirthdayDay2}), and
 * {@link org.holiday.calendar.observance.islamic.mena.IsraMiraj}.
 *
 * <p>Moon-sighting-dependent dates cannot be computed by formula: each
 * observance loads officially-published dates from a classpath CSV lookup
 * table via {@link org.holiday.calendar.util.CsvObservanceLoader}, per
 * country/authority (e.g. Diyanet for Turkey, UAE SCA / Saudi Tadawul for the
 * Gulf states). Beyond the data ceiling, dates fall back to the Umm al-Qura
 * tabular Islamic calendar via the internal
 * {@code observance.islamic.mena.ilmitakvim} calculator, pending official
 * publication.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
package org.holiday.calendar.observance.islamic.mena;
