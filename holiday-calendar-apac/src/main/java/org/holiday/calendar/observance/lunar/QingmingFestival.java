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

import java.time.LocalDate;

/**
 * Observance of Qingming Festival (清明, Tomb Sweeping Day), which falls on
 * the solar term at ecliptic longitude 15° — the 5th of the 24 Chinese solar
 * terms. It occurs on 4 or 5 April in the Gregorian calendar.
 *
 * <p>The date is computed via Time4J's {@code ChineseCalendar.ofQingMing(year)},
 * which applies the {@code SolarTerm.MINOR_03_QINGMING_015.sinceLichun()} operator
 * using precise astronomical solar-longitude calculations.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class QingmingFestival extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        ChineseCalendar qingming = ChineseCalendar.ofQingMing(year);
        PlainDate plain = qingming.transform(PlainDate.axis());
        return plain.toTemporalAccessor();
    }

}
