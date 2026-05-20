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

package org.holiday.calendar.observance.qa;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

/**
 * Observance of Qatar National Sports Day — the second Tuesday of February.
 *
 * <p>Established by Emiri Decree No. 80 of 2011; first observed on
 * 14 February 2012. Falls on a Tuesday by definition and never requires a
 * substitute holiday.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class QatarNationalSportsDay extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        return LocalDate.of(year, Month.FEBRUARY, 1)
                        .with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.TUESDAY));
    }

    @Override
    protected boolean isValidYear(int year) {
        return year >= 2012;
    }

}
