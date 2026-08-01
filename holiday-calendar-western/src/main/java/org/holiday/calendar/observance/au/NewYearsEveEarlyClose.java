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

import java.time.Month;

/**
 * Observance of the Australian Securities Exchange's New Year's Eve half-day
 * close (December 31). ASX runs a shortened trading session (normal trading
 * ceasing at 14:10 Sydney time) on December 31 whenever that date falls on a
 * weekday; when December 31 falls on a Saturday or Sunday, the half-day close
 * is not observed that year at all — unlike the London Stock Exchange (see
 * {@code org.holiday.calendar.observance.uk.NewYearsEveEarlyClose}), ASX does
 * not shift the half-day session to the preceding Friday. See
 * {@link AsxEveEarlyClose} for the shared eligibility logic (a different
 * use of {@code isValidYear} than its algorithm-validity use in
 * {@code WesternEaster}/{@code OrthodoxEaster} — see
 * {@code org.holiday.calendar.observance.ca.ChristmasEveEarlyClose}'s javadoc
 * for the same note).
 *
 * <p>Verified against ASX's own "ASX Trade trading hours for Christmas and
 * New Year" notices (asxonline.com): the 2025/2026 notice confirms the same
 * 14:10 Sydney close applies to both December 24 and December 31.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class NewYearsEveEarlyClose extends AsxEveEarlyClose {

    public NewYearsEveEarlyClose() {
        super(Month.DECEMBER, 31);
    }

}
