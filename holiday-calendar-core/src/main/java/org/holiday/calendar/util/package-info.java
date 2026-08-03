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
 * Shared runtime utilities used by {@link org.holiday.calendar.function.Observance}
 * implementations across the regional calendar modules.
 *
 * <p>{@link org.holiday.calendar.util.CsvObservanceLoader} loads
 * officially-gazetted holiday dates (moon-sighting-dependent Islamic holidays,
 * government-published Lunar New Year and Deepavali dates, etc.) from classpath
 * CSV resources of the form {@code year,YYYY-MM-DD[,comment]}, since these
 * dates cannot be computed algorithmically for the full supported year range:
 * <pre>{@code
 * private static final Map<Integer, LocalDate> DATES =
 *     CsvObservanceLoader.loadSingle(EidAlFitr.class, "eid-al-fitr.csv");
 * }</pre>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
package org.holiday.calendar.util;
