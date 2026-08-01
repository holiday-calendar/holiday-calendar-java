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

package org.holiday.calendar.observance.au;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

/**
 * Package-private template for the Australian Securities Exchange's
 * Christmas Eve / New Year's Eve half-day close Observances: the early close
 * occurs on the fixed date whenever that date falls on a weekday, and is
 * suppressed entirely (not shifted, unlike the London Stock Exchange) when
 * the date falls on a Saturday or Sunday.
 *
 * <p>Other calendars in this codebase (e.g. {@code observance.ca},
 * {@code observance.sg}) intentionally keep their Christmas Eve/New Year's
 * Eve Observances as separate, non-sharing classes despite near-identical
 * bodies. This package deviates from that convention with a package-private
 * shared base, scoped only to {@code observance.au}, purely to avoid
 * duplicating that same body a second time (SonarCloud's new-code
 * duplication gate flags the repeated {@code computeDate}/{@code isValidYear}
 * pattern, same as it did for {@code observance.fr}'s
 * {@code EuronextEveEarlyClose}).
 */
abstract class AsxEveEarlyClose extends AbstractObservance {

    private final Month month;
    private final int dayOfMonth;

    AsxEveEarlyClose(Month month, int dayOfMonth) {
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    @Override
    protected final LocalDate computeDate(int year) {
        return LocalDate.of(year, month, dayOfMonth);
    }

    @Override
    protected final boolean isValidYear(int year) {
        DayOfWeek dayOfWeek = computeDate(year).getDayOfWeek();
        return switch (dayOfWeek) {
            case SATURDAY, SUNDAY -> false;
            default -> true;
        };
    }

}
