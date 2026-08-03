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
 * Implementations of {@link org.holiday.calendar.HolidayCalendarService} for
 * Western national and market/exchange holiday calendars, one national/market
 * pair per country: Australia ({@code AU}/{@code XASX}), Canada
 * ({@code CA}/{@code XTSE}), Germany ({@code DE}/{@code XETR}), France
 * ({@code FR}/{@code XPAR}), Switzerland ({@code CH}/{@code XSWX}), the United
 * Kingdom ({@code UK}/{@code XLON}), and the United States
 * ({@code US}/{@code XNYS}); plus central-bank/settlement calendars
 * ({@code AUD}, {@code CAD}, {@code CHF}, {@code EUR}, {@code GBP},
 * {@code USD}) and the shared {@code *Holidays} helper classes each service
 * delegates to. Each type is discovered at runtime via
 * {@link java.util.ServiceLoader}; see {@code module-info.java} for the
 * registered {@code provides} list.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
package org.holiday.calendar.impl;