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

package org.holiday.calendar.observance.sg;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

/**
 * Observance of the Singapore Exchange's (SGX) New Year's Eve half-day close
 * (December 31). SGX's securities/equities market runs a half-day trading
 * session (09:00-12:00 SGT) on December 31 whenever that date falls on a
 * trading day; when December 31 falls on a Saturday or Sunday, the half-day
 * close is simply not observed that year — it is not shifted to another
 * date. {@link #isValidYear(int)} is overridden here to signal that
 * business-rule absence, while {@link #computeDate(int)} unconditionally
 * returns December 31.
 *
 * <p>Verified against SGX's Rulebook Regulatory/Practice Notice 8.2.1
 * ("Trading Hours, Market Phases...") and cross-checked against SGX's
 * published trading calendars, 2018-2026.
 *
 * @author <a href="mailto:dave@holiday-calendar.org">Dave Joyce</a>
 */
public class NewYearsEveEarlyClose extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        return LocalDate.of(year, Month.DECEMBER, 31);
    }

    @Override
    protected boolean isValidYear(int year) {
        DayOfWeek newYearsEve = LocalDate.of(year, Month.DECEMBER, 31).getDayOfWeek();
        return switch (newYearsEve) {
            case SATURDAY, SUNDAY -> false;
            default -> true;
        };
    }

}
