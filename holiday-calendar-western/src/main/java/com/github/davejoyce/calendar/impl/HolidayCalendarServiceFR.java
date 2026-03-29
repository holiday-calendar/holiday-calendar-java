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

package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.AbstractHolidayCalendarService;
import com.github.davejoyce.calendar.Holiday;
import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.function.DateRolls;
import com.github.davejoyce.calendar.observance.christian.AscensionDay;
import com.github.davejoyce.calendar.observance.christian.EasterMonday;
import com.github.davejoyce.calendar.observance.christian.EasterObservance;
import com.github.davejoyce.calendar.observance.christian.WesternEaster;
import com.github.davejoyce.calendar.observance.christian.WhitMonday;

import java.time.Month;

import static com.github.davejoyce.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of France (Euronext Paris) holiday calendar.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceFR extends AbstractHolidayCalendarService {

    private static final String CODE = "FR";
    private static final String NAME = "France (Euronext Paris) Holidays";

    public HolidayCalendarServiceFR() {
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
        final Holiday victoryInEuropeDay = Holiday.builder()
                .name("Victory in Europe Day")
                .description("Victory in Europe Day")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.MAY, 8)
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
        final Holiday bastilleDay = Holiday.builder()
                .name("Bastille Day")
                .description("Bastille Day (French National Day)")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.JULY, 14)
                .build();
        final Holiday assumptionDay = Holiday.builder()
                .name("Assumption Day")
                .description("Assumption of the Blessed Virgin Mary")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.AUGUST, 15)
                .build();
        final Holiday allSaintsDay = Holiday.builder()
                .name("All Saints' Day")
                .description("All Saints' Day")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.NOVEMBER, 1)
                .build();
        final Holiday armisticeDay = Holiday.builder()
                .name("Armistice Day")
                .description("Armistice Day")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.NOVEMBER, 11)
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
                .dateRoll(DateRolls.previousFridayOrFollowingMonday())
                .weekendDays(STANDARD_WEEKEND)
                .holiday(newYearsDay)
                .holiday(easterMonday)
                .holiday(labourDay)
                .holiday(victoryInEuropeDay)
                .holiday(ascensionDay)
                .holiday(whitMonday)
                .holiday(bastilleDay)
                .holiday(assumptionDay)
                .holiday(allSaintsDay)
                .holiday(armisticeDay)
                .holiday(christmasDay)
                .build();
    }

}
