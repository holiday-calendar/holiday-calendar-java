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

package org.holiday.calendar.observance.us;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

/**
 * Observance of the NYSE's Christmas Eve half-day close (December 24). Unlike
 * a weekend-shifting observance, this early close does not occur at all in
 * years where December 25 falls on a Monday, Saturday, or Sunday — it is
 * suppressed for that year rather than moved to an adjacent date. This is why
 * {@link #isValidYear(int)} is overridden here to signal absence, rather than
 * {@link #computeDate(int)} shifting to a different day as
 * {@code org.holiday.calendar.observance.uk.ChristmasEveEarlyClose} does.
 *
 * <p>Verified against NYSE Group's official Holiday and Early Closings
 * Calendar press releases (ir.theice.com): December 24 is an early close only
 * when December 25 falls Tuesday through Friday.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class ChristmasEveEarlyClose extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        return LocalDate.of(year, Month.DECEMBER, 24);
    }

    @Override
    protected boolean isValidYear(int year) {
        DayOfWeek christmasDay = LocalDate.of(year, Month.DECEMBER, 25).getDayOfWeek();
        return switch (christmasDay) {
            case MONDAY, SATURDAY, SUNDAY -> false;
            default -> true;
        };
    }

}
