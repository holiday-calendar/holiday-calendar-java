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
import org.holiday.calendar.observance.au.KingsBirthday;
import org.holiday.calendar.observance.christian.EasterMonday;
import org.holiday.calendar.observance.christian.EasterObservance;
import org.holiday.calendar.observance.christian.GoodFriday;
import org.holiday.calendar.observance.christian.WesternEaster;

import java.time.Month;

import static org.holiday.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of Australia (ASX) holiday calendar.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceAU extends AbstractHolidayCalendarService {

    private static final String CODE = "AU";
    private static final String NAME = "Australia (ASX) Holidays";

    public HolidayCalendarServiceAU() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final EasterObservance easter = new WesternEaster();
        final GoodFriday goodFridayObs = new GoodFriday(easter);

        final Holiday newYearsDay = Holiday.builder()
                .name("New Year's Day")
                .description("First day of new year in the Common Era (CE)")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.JANUARY, 1)
                .build();
        final Holiday australiaDay = Holiday.builder()
                .name("Australia Day")
                .description("Australia Day")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.JANUARY, 26)
                .build();
        final Holiday goodFriday = Holiday.builder()
                .name("Good Friday")
                .description("Friday before Easter Sunday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(goodFridayObs)
                .build();
        final Holiday easterSaturday = Holiday.builder()
                .name("Easter Saturday")
                .description("Day after Good Friday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(year -> goodFridayObs.apply(year).plusDays(1))
                .build();
        final Holiday easterMonday = Holiday.builder()
                .name("Easter Monday")
                .description("Monday after Easter Sunday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EasterMonday(easter))
                .build();
        final Holiday anzacDay = Holiday.builder()
                .name("ANZAC Day")
                .description("ANZAC Day")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.APRIL, 25)
                .build();
        final Holiday kingsBirthday = Holiday.builder()
                .name("King's Birthday")
                .description("King's Birthday (ASX; 2nd Monday in June)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new KingsBirthday())
                .build();
        final Holiday christmasDay = Holiday.builder()
                .name("Christmas Day")
                .description("Celebration of traditional Christmas holiday")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.DECEMBER, 25)
                .build();
        final Holiday boxingDay = Holiday.builder()
                .name("Boxing Day")
                .description("Day after Christmas")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.DECEMBER, 26)
                .build();

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.previousFridayOrFollowingMonday())
                .weekendDays(STANDARD_WEEKEND)
                .holiday(newYearsDay)
                .holiday(australiaDay)
                .holiday(goodFriday)
                .holiday(easterSaturday)
                .holiday(easterMonday)
                .holiday(anzacDay)
                .holiday(kingsBirthday)
                .holiday(christmasDay)
                .holiday(boxingDay)
                .build();
    }

}
