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

import org.holiday.calendar.AbstractHolidayCalendarService;
import org.holiday.calendar.Holiday;
import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.function.DateRolls;
import org.holiday.calendar.observance.christian.EasterMonday;
import org.holiday.calendar.observance.christian.EasterObservance;
import org.holiday.calendar.observance.christian.GoodFriday;
import org.holiday.calendar.observance.christian.WesternEaster;

import java.time.Month;

import static org.holiday.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of Euro (TARGET2) holiday calendar.
 *
 * <p>TARGET2 (Trans-European Automated Real-time Gross settlement Express
 * Transfer) publishes six fixed closure dates per year. No date-substitution
 * convention applies; closures are observed on the stated calendar date only.
 * If a closure date falls on a weekend, no compensatory weekday closure is
 * designated — the system is already non-operating on weekends.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 * @see <a href="https://www.ecb.europa.eu/paym/target/target2/profuse/calendar/html/index.en.html">ECB TARGET2 Calendar</a>
 */
public class HolidayCalendarServiceEUR extends AbstractHolidayCalendarService {

    private static final String CODE = "EUR";
    private static final String NAME = "Euro (TARGET2) Holidays";

    public HolidayCalendarServiceEUR() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final EasterObservance easter = new WesternEaster();

        final Holiday newYearsDay = Holiday.builder()
                                           .name("New Year's Day")
                                           .description("First day of new year in the Common Era (CE)")
                                           .type(Holiday.Type.FIXED)
                                           .rollable(false)
                                           .monthDay(Month.JANUARY, 1)
                                           .build();
        final Holiday goodFriday = Holiday.builder()
                                          .name("Good Friday")
                                          .description("Friday before Easter Sunday")
                                          .type(Holiday.Type.FLOATING)
                                          .rollable(false)
                                          .observance(new GoodFriday(easter))
                                          .build();
        final Holiday easterMonday = Holiday.builder()
                                            .name("Easter Monday")
                                            .description("Monday after Easter Sunday")
                                            .type(Holiday.Type.FLOATING)
                                            .rollable(false)
                                            .observance(new EasterMonday(easter))
                                            .build();
        final Holiday labourDay = Holiday.builder()
                                         .name("Labour Day")
                                         .description("International Workers' Day")
                                         .type(Holiday.Type.FIXED)
                                         .rollable(false)
                                         .monthDay(Month.MAY, 1)
                                         .build();
        final Holiday christmasDay = Holiday.builder()
                                            .name("Christmas Day")
                                            .description("Celebration of traditional Christmas holiday")
                                            .type(Holiday.Type.FIXED)
                                            .rollable(false)
                                            .monthDay(Month.DECEMBER, 25)
                                            .build();
        final Holiday boxingDay = Holiday.builder()
                                         .name("Boxing Day")
                                         .description("Second day of Christmas")
                                         .type(Holiday.Type.FIXED)
                                         .rollable(false)
                                         .monthDay(Month.DECEMBER, 26)
                                         .build();
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.noRoll())
                .weekendDays(STANDARD_WEEKEND)
                .holiday(newYearsDay)
                .holiday(goodFriday)
                .holiday(easterMonday)
                .holiday(labourDay)
                .holiday(christmasDay)
                .holiday(boxingDay)
                .build();
    }

}
