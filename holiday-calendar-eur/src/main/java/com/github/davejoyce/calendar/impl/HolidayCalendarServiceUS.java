/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2022 David Joyce
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

package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.Holiday;
import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.HolidayCalendarService;
import com.github.davejoyce.calendar.observance.us.*;

import java.time.DayOfWeek;
import java.time.Month;

/**
 * Service for provision of US national holiday calendar.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceUS implements HolidayCalendarService {

    private static final String CODE = "US";
    private static final String NAME = "United States National Holidays";

    @Override
    public boolean isProvided(String code) {
        return CODE.equalsIgnoreCase(code);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final Holiday newYearsDay = Holiday.builder()
                                           .name("New Year's Day")
                                           .description("First day of new year in the Common Era (CE)")
                                           .type(Holiday.Type.FIXED)
                                           .rollable(true)
                                           .monthDay(Month.JANUARY, 1)
                                           .build();
        final Holiday mlkDay = Holiday.builder()
                                      .name("Martin Luther King Jr. Day")
                                      .description("Observed birthday of Martin Luther King, Jr.")
                                      .type(Holiday.Type.FLOATING)
                                      .rollable(false)
                                      .observance(new MartinLutherKingJrDay())
                                      .build();
        final Holiday presidentsDay = Holiday.builder()
                                             .name("Presidents' Day")
                                             .description("Commemoration of Presidents of the United States")
                                             .type(Holiday.Type.FLOATING)
                                             .rollable(false)
                                             .observance(new PresidentsDay())
                                             .build();
        final Holiday memorialDay = Holiday.builder()
                                           .name("Memorial Day")
                                           .description("Commemoration of fallen service members of US armed forces")
                                           .type(Holiday.Type.FLOATING)
                                           .rollable(false)
                                           .observance(new MemorialDay())
                                           .build();
        final Holiday juneteenth = Holiday.builder()
                                          .name("Juneteenth")
                                          .description("Commemoration of emancipation of African-American slaves")
                                          .type(Holiday.Type.FIXED)
                                          .rollable(true)
                                          .monthDay(Month.JUNE, 19)
                                          .build();
        final Holiday independenceDay = Holiday.builder()
                                               .name("Independence Day")
                                               .description("Celebration of US Declaration of Independence")
                                               .type(Holiday.Type.FIXED)
                                               .rollable(true)
                                               .monthDay(Month.JULY, 4)
                                               .build();
        final Holiday laborDay = Holiday.builder()
                                        .name("Labor Day")
                                        .description("US Labor Day")
                                        .type(Holiday.Type.FLOATING)
                                        .rollable(false)
                                        .observance(new LaborDay())
                                        .build();
        final Holiday columbusDay = Holiday.builder()
                                           .name("Columbus Day")
                                           .description("Anniversary of the arrival of Christopher Columbus in the Americas")
                                           .type(Holiday.Type.FLOATING)
                                           .rollable(false)
                                           .observance(new ColumbusDay())
                                           .build();
        final Holiday veteransDay = Holiday.builder()
                                           .name("Veterans Day")
                                           .description("Commemoration of all US veterans of foreign wars")
                                           .type(Holiday.Type.FIXED)
                                           .rollable(true)
                                           .monthDay(Month.NOVEMBER, 11)
                                           .build();
        final Holiday thanksgiving = Holiday.builder()
                                            .name("Thanksgiving")
                                            .description("Day to give thanks")
                                            .type(Holiday.Type.FLOATING)
                                            .rollable(false)
                                            .observance(new Thanksgiving())
                                            .build();
        final Holiday christmasDay = Holiday.builder()
                                            .name("Christmas Day")
                                            .description("Celebration of traditional Christmas holiday")
                                            .type(Holiday.Type.FIXED)
                                            .rollable(true)
                                            .monthDay(Month.DECEMBER, 25)
                                            .build();
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(dateToRoll -> {
                    if (DayOfWeek.SATURDAY.equals(dateToRoll.getDayOfWeek())) return dateToRoll.minusDays(1L);
                    if (DayOfWeek.SUNDAY.equals(dateToRoll.getDayOfWeek())) return dateToRoll.plusDays(1L);
                    return dateToRoll;
                })
                .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                .holiday(newYearsDay)
                .holiday(mlkDay)
                .holiday(presidentsDay)
                .holiday(memorialDay)
                .holiday(juneteenth)
                .holiday(independenceDay)
                .holiday(laborDay)
                .holiday(columbusDay)
                .holiday(veteransDay)
                .holiday(thanksgiving)
                .holiday(christmasDay)
                .build();
    }

}
