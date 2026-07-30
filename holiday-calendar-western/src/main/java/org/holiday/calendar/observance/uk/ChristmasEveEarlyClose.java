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

package org.holiday.calendar.observance.uk;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.LocalDate;
import java.time.Month;

/**
 * Observance of the London Stock Exchange's Christmas Eve half-day close
 * (December 24). When December 24 falls on a Saturday or Sunday, the
 * half-day session is shifted to the preceding Friday, since the market is
 * already closed on the natural date.
 *
 * <p>Verified against the LSE's official business days notice
 * (londonstockexchange.com/equities-trading/business-days), which lists e.g.
 * Friday 22 December 2028 as a "Christmas Holiday half day" (markets closing
 * process commencing from 12:30 London time) — December 24, 2028 itself falls
 * on a Sunday, confirming the shift-to-preceding-Friday rule.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class ChristmasEveEarlyClose extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        LocalDate date = LocalDate.of(year, Month.DECEMBER, 24);
        return switch (date.getDayOfWeek()) {
            case SATURDAY -> date.minusDays(1);
            case SUNDAY -> date.minusDays(2);
            default -> date;
        };
    }

}
