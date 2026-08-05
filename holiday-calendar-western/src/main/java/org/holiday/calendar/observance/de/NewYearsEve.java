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
 * Observance of Xetra/Frankfurt Stock Exchange's New Year's Eve full non-trading
 * day (December 31, an "Erfüllungstag"/settlement day). Mirrors {@link ChristmasEve}'s
 * suppression semantics: omitted entirely (not shifted) in years December 31 falls
 * on a Saturday or Sunday, since Xetra/FWB is simply not a trading day at all on
 * weekends.
 *
 * <p>Verified against Deutsche Börse's official Xetra-Handelskalender
 * (cashmarket.deutsche-boerse.com), 2023-2026.
 *
 * @author <a href="mailto:dave@holiday-calendar.org">Dave Joyce</a>
 */
public class NewYearsEve extends AbstractObservance {

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
