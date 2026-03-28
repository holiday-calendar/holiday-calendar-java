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

import com.github.davejoyce.calendar.AbstractHolidayCalendarService;
import com.github.davejoyce.calendar.Holiday;
import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.function.DateRolls;
import com.github.davejoyce.calendar.observance.christian.AscensionDay;
import com.github.davejoyce.calendar.observance.christian.EasterMonday;
import com.github.davejoyce.calendar.observance.christian.EasterObservance;
import com.github.davejoyce.calendar.observance.christian.GoodFriday;
import com.github.davejoyce.calendar.observance.christian.WesternEaster;
import com.github.davejoyce.calendar.observance.christian.WhitMonday;

import java.time.Month;

import static com.github.davejoyce.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of Germany (Xetra/FSE) holiday calendar.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceDE extends AbstractHolidayCalendarService {

    private static final String CODE = "DE";
    private static final String NAME = "Germany (Xetra) Holidays";

    public HolidayCalendarServiceDE() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final EasterObservance easter = new WesternEaster();

        final Holiday newYearsDay = Holiday.builder()
                .name("New Year's Day")
                .description("First day of new year in the Common Era (CE)")
                .type(Holiday.Type.FIXED)
                .rollable(true)
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
                .rollable(true)
                .monthDay(Month.MAY, 1)
                .build();
        final Holiday ascensionDay = Holiday.builder()
                .name("Ascension Day")
                .description("The 40th day of Easter; Jesus Christ's ascension into heaven")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new AscensionDay(easter))
                .build();
        final Holiday whitMonday = Holiday.builder()
                .name("Whit Monday")
                .description("Monday after Whit Sunday (Pentecost)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new WhitMonday(easter))
                .build();
        final Holiday germanUnityDay = Holiday.builder()
                .name("German Unity Day")
                .description("German Unity Day")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.OCTOBER, 3)
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
                .holiday(goodFriday)
                .holiday(easterMonday)
                .holiday(labourDay)
                .holiday(ascensionDay)
                .holiday(whitMonday)
                .holiday(germanUnityDay)
                .holiday(christmasDay)
                .holiday(boxingDay)
                .build();
    }

}
