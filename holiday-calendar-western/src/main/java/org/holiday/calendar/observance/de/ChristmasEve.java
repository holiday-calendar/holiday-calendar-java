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

package org.holiday.calendar.observance.de;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

/**
 * Observance of Xetra/Frankfurt Stock Exchange's Christmas Eve full non-trading
 * day (December 24, an "Erfüllungstag"/settlement day). Unlike
 * {@code org.holiday.calendar.observance.uk.ChristmasEveEarlyClose} — which
 * always occurs, shifting to the preceding Friday on a weekend — Xetra/FWB is
 * simply not a trading day at all on weekends, so December 24 is omitted
 * entirely (not shifted) in years it falls on a Saturday or Sunday. This
 * mirrors {@code org.holiday.calendar.observance.ca.ChristmasEveEarlyClose}'s
 * suppression semantics, applied here to a {@code FLOATING} full-day holiday
 * rather than an {@code EARLY_CLOSE}.
 *
 * <p>Verified against Deutsche Börse's official Xetra-Handelskalender
 * (cashmarket.deutsche-boerse.com), 2023-2026.
 *
 * @author <a href="mailto:dave@holiday-calendar.org">Dave Joyce</a>
 */
public class ChristmasEve extends AbstractObservance {

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
