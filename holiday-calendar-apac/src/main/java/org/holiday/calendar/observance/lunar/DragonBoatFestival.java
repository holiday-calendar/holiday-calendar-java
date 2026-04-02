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

package org.holiday.calendar.observance.lunar;

import org.holiday.calendar.observance.AbstractObservance;
import net.time4j.PlainDate;
import net.time4j.calendar.ChineseCalendar;
import net.time4j.calendar.EastAsianMonth;
import net.time4j.calendar.EastAsianYear;

import java.time.LocalDate;

/**
 * Observance of Dragon Boat Festival (端午节, Duanwu), which falls on the
 * 5th day of the 5th month of the Chinese lunisolar calendar.
 *
 * <p>The date is computed algorithmically via Time4J's {@code ChineseCalendar},
 * using the Chinese year that begins in the given Gregorian year.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class DragonBoatFestival extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        ChineseCalendar date = ChineseCalendar.of(
                EastAsianYear.forGregorian(year),
                EastAsianMonth.valueOf(5),
                5);
        PlainDate plain = date.transform(PlainDate.axis());
        return plain.toTemporalAccessor();
    }

}
