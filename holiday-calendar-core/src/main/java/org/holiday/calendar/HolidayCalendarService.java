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

package org.holiday.calendar;

import java.util.OptionalInt;

/**
 * Required behavior of a service which provides the {@link HolidayCalendar}
 * object assigned to a unique <em>code</em> identifier.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface HolidayCalendarService {

    /**
     * Determine if this service provides the {@link HolidayCalendar} for the
     * specified code.
     *
     * @param code short code identifier of desired holiday calendar
     * @return true if this service provides the holiday calendar for the given
     *         code, false otherwise
     */
    boolean isProvided(String code);

    /**
     * Get the holiday calendar object provided by this service.
     *
     * @return holiday calendar object
     */
    HolidayCalendar getHolidayCalendar();

    /**
     * Get the short code identifier for the holiday calendar provided by this service.
     * Returns {@code null} if not implemented.
     *
     * @return short code identifier, e.g. "US", "UK"
     */
    default String getCode() {
        return null;
    }

    /**
     * Get the human-readable name of the region for this holiday calendar.
     * Returns {@code null} if not implemented.
     *
     * @return region name, e.g. "United States National Holidays"
     */
    default String getRegion() {
        return null;
    }

    /**
     * Returns the latest year for which this service provides authoritative
     * holiday data for <em>all</em> lookup-table-backed holidays in this calendar.
     *
     * <p>An empty result indicates that every holiday in this calendar is
     * computed algorithmically and therefore has no known upper bound &mdash; all
     * years are equally authoritative (e.g. Easter-based or nth-weekday rules).
     *
     * <p>A non-empty result indicates that one or more holidays are backed by a
     * finite, officially-gazetted lookup table. The returned value is the last
     * year for which all such tables have been populated. For years beyond this
     * value, {@link HolidayCalendar#calculate(int)} will silently omit those
     * holidays without throwing an exception. This method is an advisory signal
     * only &mdash; it does not alter {@code calculate()} behaviour. Callers that
     * require complete data should verify that the requested year does not
     * exceed this bound before invoking {@code calculate()}.
     *
     * <p>Implementations backed by lookup tables <strong>must</strong> override
     * this method and return {@code OptionalInt.of(lastTableYear)}, where
     * {@code lastTableYear} is the minimum of the last year covered by each
     * individual lookup table in the calendar. Subclasses of
     * {@link AbstractHolidayCalendarService} that use lookup tables should
     * override this method accordingly.
     *
     * @return the last year for which data is fully authoritative, or
     *         {@link OptionalInt#empty()} if the calendar is fully algorithmic
     *         with no known upper bound
     */
    default OptionalInt dataValidThrough() {
        return OptionalInt.empty();
    }

}
