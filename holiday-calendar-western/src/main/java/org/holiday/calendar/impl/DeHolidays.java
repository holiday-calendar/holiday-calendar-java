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

package org.holiday.calendar.impl;

import org.holiday.calendar.Holiday;
import org.holiday.calendar.observance.christian.AscensionDay;
import org.holiday.calendar.observance.christian.EasterMonday;
import org.holiday.calendar.observance.christian.EasterObservance;
import org.holiday.calendar.observance.christian.GoodFriday;
import org.holiday.calendar.observance.christian.WesternEaster;
import org.holiday.calendar.observance.christian.WhitMonday;

import java.time.Month;
import java.util.List;

/**
 * Package-private factory building the 9 holidays common to both the
 * Germany national ({@code DE}) and Xetra ({@code XETR}) calendars: New
 * Year's Day, Good Friday, Easter Monday, Labour Day, Ascension Day, Whit
 * Monday, German Unity Day, Christmas Day, and Boxing Day. These are
 * exactly Germany's 9 official nationwide (bundesweite) public holidays,
 * and Xetra/Frankfurt is closed on all of them, so the full list is shared
 * verbatim — only {@code XETR} adds the Christmas Eve/New Year's Eve
 * market-only closures on top.
 */
class DeHolidays {

    private DeHolidays() {}

    static List<Holiday> baseHolidays() {
        final EasterObservance easter = new WesternEaster();

        return List.of(
            Holiday.builder()
                    .name("New Year's Day")
                    .description("First day of new year in the Common Era (CE)")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.JANUARY, 1)
                    .build(),
            Holiday.builder()
                    .name("Good Friday")
                    .description("Friday before Easter Sunday")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new GoodFriday(easter))
                    .build(),
            Holiday.builder()
                    .name("Easter Monday")
                    .description("Monday after Easter Sunday")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EasterMonday(easter))
                    .build(),
            Holiday.builder()
                    .name("Labour Day")
                    .description("International Workers' Day")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.MAY, 1)
                    .build(),
            Holiday.builder()
                    .name("Ascension Day")
                    .description("The 40th day of Easter; Jesus Christ's ascension into heaven")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new AscensionDay(easter))
                    .build(),
            Holiday.builder()
                    .name("Whit Monday")
                    .description("Monday after Whit Sunday (Pentecost)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new WhitMonday(easter))
                    .build(),
            Holiday.builder()
                    .name("German Unity Day")
                    .description("German Unity Day")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.OCTOBER, 3)
                    .build(),
            Holiday.builder()
                    .name("Christmas Day")
                    .description("Celebration of traditional Christmas holiday")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.DECEMBER, 25)
                    .build(),
            Holiday.builder()
                    .name("Boxing Day")
                    .description("Day after Christmas")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.DECEMBER, 26)
                    .build()
        );
    }

}
