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

package org.holiday.calendar.observance.fr;

import java.time.Month;

/**
 * Observance of Euronext Paris's New Year's Eve half-day close (December 31).
 * Euronext runs a shortened trading session (closing 14:05 CET) on December 31
 * whenever that date falls on a weekday; when December 31 falls on a Saturday
 * or Sunday, the half-day close is not observed that year at all — unlike the
 * London Stock Exchange (see {@code org.holiday.calendar.observance.uk.NewYearsEveEarlyClose}),
 * Euronext does not shift the half-day session to the preceding Friday. See
 * {@link EuronextEveEarlyClose} for the shared eligibility logic (a different
 * use of {@code isValidYear} than its algorithm-validity use in
 * {@code WesternEaster}/{@code OrthodoxEaster} — see
 * {@code org.holiday.calendar.observance.ca.ChristmasEveEarlyClose}'s javadoc
 * for the same note).
 *
 * <p>Verified against Euronext's official trading calendar / half-day-close
 * notices, including the 2016 and 2017 press releases confirming the
 * preceding Friday traded full hours in both weekend years, cross-checked
 * 2014-2026.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class NewYearsEveEarlyClose extends EuronextEveEarlyClose {

    public NewYearsEveEarlyClose() {
        super(Month.DECEMBER, 31);
    }

}
