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

package org.holiday.calendar.observance.tr;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

/**
 * Observance of Turkey's Republic Day Eve nationwide half-day (October 28),
 * declared by Law No. 2429 (Ulusal Bayram ve Genel Tatiller Hakkında Kanun)
 * and observed alike by public institutions, Borsa İstanbul (BIST), and the
 * Central Bank of the Republic of Turkey (TCMB). Unlike the LSE's Christmas
 * Eve convention, it does not shift to the preceding Friday when October 28
 * falls on a Saturday or Sunday — institutions are simply closed for the
 * weekend as usual, with no early-close adjustment that year (confirmed
 * against BIST's 2023 holiday schedule, where 28-29 October fell on
 * Saturday-Sunday with no preceding half-day session).
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class RepublicDayEveEarlyClose extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        return LocalDate.of(year, Month.OCTOBER, 28);
    }

    @Override
    protected boolean isValidYear(int year) {
        DayOfWeek dayOfWeek = LocalDate.of(year, Month.OCTOBER, 28).getDayOfWeek();
        return !dayOfWeek.equals(DayOfWeek.SATURDAY) && !dayOfWeek.equals(DayOfWeek.SUNDAY);
    }

}
