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
import org.holiday.calendar.observance.au.KingsBirthday;
import org.holiday.calendar.observance.christian.EasterMonday;
import org.holiday.calendar.observance.christian.EasterObservance;
import org.holiday.calendar.observance.christian.GoodFriday;
import org.holiday.calendar.observance.christian.WesternEaster;

import java.time.Month;
import java.util.List;

/**
 * Package-private factory building the 9 holidays common to both the
 * Australia national ({@code AU}) and Australian Securities Exchange
 * ({@code XASX}) calendars: New Year's Day, Australia Day, Good Friday,
 * Easter Saturday, Easter Monday, ANZAC Day, King's Birthday, Christmas Day,
 * and Boxing Day. ASX is closed on all of these, so the full list is shared
 * verbatim — only {@code XASX} adds the Christmas Eve/New Year's Eve early
 * closes on top.
 *
 * <p>Easter Saturday is not observed in Western Australia or Tasmania, and
 * King's Birthday uses different dates in Queensland (1st Monday in October)
 * and Western Australia (a late-September date) than the 2nd-Monday-in-June
 * convention used elsewhere — both are genuine, state-backed public holidays
 * somewhere in the country, so both are retained here with these caveats.
 */
class AuHolidays {

    private AuHolidays() {}

    static List<Holiday> baseHolidays() {
        final EasterObservance easter = new WesternEaster();
        final GoodFriday goodFridayObs = new GoodFriday(easter);

        return List.of(
            Holiday.builder()
                    .name("New Year's Day")
                    .description("First day of new year in the Common Era (CE)")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.JANUARY, 1)
                    .build(),
            Holiday.builder()
                    .name("Australia Day")
                    .description("Australia Day")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.JANUARY, 26)
                    .build(),
            Holiday.builder()
                    .name("Good Friday")
                    .description("Friday before Easter Sunday")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(goodFridayObs)
                    .build(),
            Holiday.builder()
                    .name("Easter Saturday")
                    .description("Day after Good Friday; not observed in Western Australia or Tasmania")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(year -> goodFridayObs.apply(year).plusDays(1))
                    .build(),
            Holiday.builder()
                    .name("Easter Monday")
                    .description("Monday after Easter Sunday")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EasterMonday(easter))
                    .build(),
            Holiday.builder()
                    .name("ANZAC Day")
                    .description("ANZAC Day")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.APRIL, 25)
                    .build(),
            Holiday.builder()
                    .name("King's Birthday")
                    .description("King's Birthday (2nd Monday in June); Queensland uses the 1st "
                                 + "Monday in October and Western Australia uses a late-September date")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new KingsBirthday())
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
