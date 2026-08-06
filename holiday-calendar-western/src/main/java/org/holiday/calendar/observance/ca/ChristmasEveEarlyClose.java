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

package org.holiday.calendar.observance.ca;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

/**
 * Observance of the Toronto Stock Exchange's Christmas Eve half-day close
 * (December 24). Unlike {@code org.holiday.calendar.observance.us.ChristmasEveEarlyClose}
 * — whose eligibility is keyed off December 25's day of week — TSX's rule is
 * keyed directly off December 24 itself: the early close applies whenever
 * December 24 falls Monday through Friday, and is suppressed entirely (not
 * shifted, unlike {@code org.holiday.calendar.observance.uk.ChristmasEveEarlyClose})
 * when December 24 falls on a weekend. This is why {@link #isValidYear(int)}
 * is overridden here to signal absence in ineligible years, rather than
 * {@link #computeDate(int)} shifting to a different day.
 *
 * <p>Note this differs from the only other pre-existing use of
 * {@link #isValidYear(int)} in this codebase ({@code WesternEaster} /
 * {@code OrthodoxEaster}), where it bounds algorithm validity rather than
 * encoding a business-calendar exclusion — the same mechanism, two different
 * purposes.
 *
 * <p>Verified against TMX Group's official Holiday Operating Schedule
 * (tsx.com trading calendar / TMX PDF schedules), 2017-2026. Confirmed
 * divergence from NYSE convention in 2021: December 25, 2021 fell on a
 * Saturday (which would suppress NYSE's Dec 24 early close), yet December 24,
 * 2021 fell on a Friday and TSX held its early close that day.
 *
 * @author <a href="mailto:dave@holiday-calendar.org">Dave Joyce</a>
 */
public class ChristmasEveEarlyClose extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        return LocalDate.of(year, Month.DECEMBER, 24);
    }

    @Override
    protected boolean isValidYear(int year) {
        DayOfWeek christmasEve = LocalDate.of(year, Month.DECEMBER, 24).getDayOfWeek();
        return switch (christmasEve) {
            case SATURDAY, SUNDAY -> false;
            default -> true;
        };
    }

}
