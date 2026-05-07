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
 * Observance of a specific day within the Chinese New Year (Spring Festival)
 * holiday window, numbered from Day 1 (the first day of the first lunar month)
 * through Day 7.
 *
 * <p>Days are consecutive Gregorian calendar days beginning from Day 1
 * (Chinese New Year's Day). The valid range {@code 1–7} supports both the
 * three-day statutory minimum window (national {@code CN} calendar, Days 1–3)
 * and the seven-day PBOC operational window ({@code CNY} calendar, Days 1–7).</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class ChineseNewYearDay extends AbstractObservance {

    private final int dayNumber;

    /**
     * Constructs an observance for the given day of the Spring Festival window.
     *
     * @param dayNumber day within the Spring Festival window, in range {@code 1–7};
     *                  values 1–3 model the statutory national ({@code CN}) calendar,
     *                  values 1–7 model the PBOC operational ({@code CNY}) calendar
     * @throws IllegalArgumentException if {@code dayNumber} is not in range 1–7
     */
    public ChineseNewYearDay(int dayNumber) {
        if (dayNumber < 1 || dayNumber > 7) {
            throw new IllegalArgumentException("dayNumber must be between 1 and 7, got: " + dayNumber);
        }
        this.dayNumber = dayNumber;
    }

    @Override
    protected LocalDate computeDate(int year) {
        ChineseCalendar cny = ChineseCalendar.ofNewYear(year);
        PlainDate plain = cny.transform(PlainDate.axis());
        return plain.toTemporalAccessor().plusDays((long) dayNumber - 1);
    }

}
