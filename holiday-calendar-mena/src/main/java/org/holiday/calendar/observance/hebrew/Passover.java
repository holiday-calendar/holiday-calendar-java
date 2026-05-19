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

package org.holiday.calendar.observance.hebrew;

import net.time4j.PlainDate;
import net.time4j.calendar.HebrewCalendar;
import net.time4j.calendar.HebrewMonth;
import org.holiday.calendar.observance.AbstractObservance;

import java.time.LocalDate;

/**
 * Observance of Passover (15 Nisan) — the first day of Pesach.
 *
 * <p>Israel observes a 7-day Passover (Nisan 15–21); only the first and last
 * days are public holidays. The Hebrew year for Nisan in a given Gregorian
 * year is {@code gregorianYear + 3760}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class Passover extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        return HebrewCalendar.of(year + 3760, HebrewMonth.NISAN, 15)
                .transform(PlainDate.axis())
                .toTemporalAccessor();
    }

}
