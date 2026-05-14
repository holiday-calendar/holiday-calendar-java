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
import org.holiday.calendar.observance.christian.GoodFriday;
import org.holiday.calendar.observance.christian.WesternEaster;
import org.holiday.calendar.observance.hindu.Deepavali;
import org.holiday.calendar.observance.islamic.apac.HariRayaHaji;
import org.holiday.calendar.observance.islamic.apac.HariRayaPuasa;
import org.holiday.calendar.observance.lunar.ChineseNewYearFirstDay;
import org.holiday.calendar.observance.lunar.ChineseNewYearSecondDay;
import org.holiday.calendar.observance.lunar.VesakDay;

import java.time.Month;
import java.util.List;

/**
 * Package-private factory building the Singapore statutory public holiday list shared
 * by the SGX ({@code SG}) and MEPS+ ({@code SGD}) calendars.
 *
 * <p>The four gazetted lookup-table observances (VesakDay, HariRayaPuasa,
 * HariRayaHaji, Deepavali) are populated through {@value DATA_VALID_THROUGH}.
 * Chinese New Year is computed algorithmically via Time4J and has no upper bound.
 */
class SingaporeHolidays {

    // Boundary year through which all four gazetted lookup-table observances
    // (VesakDay, HariRayaPuasa, HariRayaHaji, Deepavali) are populated.
    static final int DATA_VALID_THROUGH = 2055;

    private SingaporeHolidays() {}

    /**
     * Returns the 11 Singapore statutory public holidays.
     *
     * @param rollableFixed whether fixed-date holidays (New Year's Day, Labour Day,
     *                      National Day, Christmas Day) are rollable; all floating
     *                      holidays are always {@code rollable(false)}
     */
    static List<Holiday> baseHolidays(boolean rollableFixed) {
        return List.of(
            Holiday.builder()
                    .name("New Year's Day")
                    .description("First day of new year in the Common Era (CE)")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.JANUARY, 1)
                    .build(),
            Holiday.builder()
                    .name("Chinese New Year (1st Day)")
                    .description("First day of the Chinese lunisolar new year")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new ChineseNewYearFirstDay())
                    .build(),
            Holiday.builder()
                    .name("Chinese New Year (2nd Day)")
                    .description("Second day of the Chinese lunisolar new year")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new ChineseNewYearSecondDay())
                    .build(),
            Holiday.builder()
                    .name("Good Friday")
                    .description("Commemoration of the crucifixion of Jesus Christ")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new GoodFriday(new WesternEaster()))
                    .build(),
            Holiday.builder()
                    .name("Labour Day")
                    .description("International Workers' Day")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.MAY, 1)
                    .build(),
            Holiday.builder()
                    .name("Vesak Day")
                    .description("Commemoration of the birth, enlightenment, and death of Gautama Buddha")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new VesakDay())
                    .build(),
            Holiday.builder()
                    .name("Hari Raya Puasa")
                    .description("Eid al-Fitr, marking the end of Ramadan")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new HariRayaPuasa())
                    .build(),
            Holiday.builder()
                    .name("National Day")
                    .description("Commemoration of Singapore's independence on 9 August 1965")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.AUGUST, 9)
                    .build(),
            Holiday.builder()
                    .name("Hari Raya Haji")
                    .description("Eid al-Adha, the Feast of Sacrifice")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new HariRayaHaji())
                    .build(),
            Holiday.builder()
                    .name("Deepavali")
                    .description("Hindu Festival of Lights")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new Deepavali())
                    .build(),
            Holiday.builder()
                    .name("Christmas Day")
                    .description("Celebration of traditional Christmas holiday")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.DECEMBER, 25)
                    .build()
        );
    }
}
