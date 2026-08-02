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
import org.holiday.calendar.observance.christian.EasterMonday;
import org.holiday.calendar.observance.christian.EasterObservance;
import org.holiday.calendar.observance.christian.GoodFriday;
import org.holiday.calendar.observance.christian.WesternEaster;
import org.holiday.calendar.observance.ca.CivicHoliday;
import org.holiday.calendar.observance.ca.FamilyDay;
import org.holiday.calendar.observance.ca.LabourDay;
import org.holiday.calendar.observance.ca.Thanksgiving;
import org.holiday.calendar.observance.ca.VictoriaDay;

import java.time.Month;
import java.util.List;

/**
 * Package-private factory building the 10 holidays common to both the
 * Canada national ({@code CA}) and Toronto Stock Exchange ({@code XTSE})
 * calendars: New Year's Day, Family Day, Good Friday, Easter Monday,
 * Victoria Day, Canada Day, Civic Holiday, Labour Day, Thanksgiving Day, and
 * Remembrance Day. Each calling calendar adds its own remaining holidays
 * (National Day For Truth and Reconciliation, Christmas Day, Boxing Day,
 * and — for {@code XTSE} only — the Christmas Eve early close), since those
 * differ between the two calendars.
 */
class CanadaHolidays {

    private CanadaHolidays() {}

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
                    .name("Family Day")
                    .description("Day to spend time with the family")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new FamilyDay())
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
                    .name("Victoria Day")
                    .description("Official celebration of birthday of Canada's Sovereign")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new VictoriaDay())
                    .build(),
            Holiday.builder()
                    .name("Canada Day")
                    .description("Anniversary of Canadian Confederation")
                    .type(Holiday.Type.FIXED)
                    .monthDay(Month.JULY, 1)
                    .rollable(true)
                    .build(),
            Holiday.builder()
                    .name("Civic Holiday")
                    .description("Civic Holiday (observed; varies by province)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new CivicHoliday())
                    .build(),
            Holiday.builder()
                    .name("Labour Day")
                    .description("Celebration of workers in Canada")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new LabourDay())
                    .build(),
            Holiday.builder()
                    .name("Thanksgiving Day")
                    .description("National day for giving thanks")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new Thanksgiving())
                    .build(),
            Holiday.builder()
                    .name("Remembrance Day")
                    .description("Commemoration of armed forces members who have died in the line of duty")
                    .type(Holiday.Type.FIXED)
                    .monthDay(Month.NOVEMBER, 11)
                    .rollable(true)
                    .build()
        );
    }

}