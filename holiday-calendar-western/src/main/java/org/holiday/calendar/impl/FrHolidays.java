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
import org.holiday.calendar.observance.christian.WesternEaster;
import org.holiday.calendar.observance.christian.WhitMonday;

import java.time.Month;
import java.util.List;

/**
 * Package-private factory building the 11 holidays common to both the
 * France national ({@code FR}) and Euronext Paris ({@code XPAR}) calendars:
 * New Year's Day, Easter Monday, Labour Day, Victory in Europe Day,
 * Ascension Day, Whit Monday, Bastille Day, Assumption Day, All Saints'
 * Day, Armistice Day, and Christmas Day. Euronext Paris is closed on all of
 * these, so the full list is shared verbatim — only {@code XPAR} adds Good
 * Friday and the Christmas Eve/New Year's Eve early closes on top.
 */
class FrHolidays {

    private FrHolidays() {}

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
                    .name("Victory in Europe Day")
                    .description("Victory in Europe Day")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.MAY, 8)
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
                    .name("Bastille Day")
                    .description("Bastille Day (French National Day)")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.JULY, 14)
                    .build(),
            Holiday.builder()
                    .name("Assumption Day")
                    .description("Assumption of the Blessed Virgin Mary")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.AUGUST, 15)
                    .build(),
            Holiday.builder()
                    .name("All Saints' Day")
                    .description("All Saints' Day")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.NOVEMBER, 1)
                    .build(),
            Holiday.builder()
                    .name("Armistice Day")
                    .description("Armistice Day")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.NOVEMBER, 11)
                    .build(),
            Holiday.builder()
                    .name("Christmas Day")
                    .description("Celebration of traditional Christmas holiday")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.DECEMBER, 25)
                    .build()
        );
    }

}
