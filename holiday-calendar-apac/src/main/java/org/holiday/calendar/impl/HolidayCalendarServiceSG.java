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
import org.holiday.calendar.observance.christian.GoodFriday;
import org.holiday.calendar.observance.christian.WesternEaster;
import org.holiday.calendar.observance.hindu.Deepavali;
import org.holiday.calendar.observance.islamic.HariRayaHaji;
import org.holiday.calendar.observance.islamic.HariRayaPuasa;
import org.holiday.calendar.observance.lunar.ChineseNewYearFirstDay;
import org.holiday.calendar.observance.lunar.ChineseNewYearSecondDay;
import org.holiday.calendar.observance.lunar.VesakDay;

import java.time.Month;
import java.util.OptionalInt;

/**
 * Service for provision of Singapore Exchange (SGX) holiday calendar.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceSG extends AbstractHolidayCalendarService {

    private static final String CODE = "SG";
    private static final String NAME = "Singapore (SGX) Holidays";

    // Boundary year through which all four gazetted lookup-table observances
    // (VesakDay, HariRayaHaji, HariRayaPuasa, Deepavali) are populated.
    private static final int DATA_VALID_THROUGH_YEAR = 2055;

    public HolidayCalendarServiceSG() {
        super(CODE, NAME);
    }

    @Override
    public OptionalInt dataValidThrough() {
        return OptionalInt.of(DATA_VALID_THROUGH_YEAR);
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
        final Holiday chineseNewYearFirstDay = Holiday.builder()
                .name("Chinese New Year (1st Day)")
                .description("First day of the Chinese lunisolar new year")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new ChineseNewYearFirstDay())
                .build();
        final Holiday chineseNewYearSecondDay = Holiday.builder()
                .name("Chinese New Year (2nd Day)")
                .description("Second day of the Chinese lunisolar new year")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new ChineseNewYearSecondDay())
                .build();
        final Holiday goodFriday = Holiday.builder()
                .name("Good Friday")
                .description("Commemoration of the crucifixion of Jesus Christ")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new GoodFriday(new WesternEaster()))
                .build();
        final Holiday labourDay = Holiday.builder()
                .name("Labour Day")
                .description("International Workers' Day")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.MAY, 1)
                .build();
        final Holiday vesakDay = Holiday.builder()
                .name("Vesak Day")
                .description("Commemoration of the birth, enlightenment, and death of Gautama Buddha")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new VesakDay())
                .build();
        final Holiday hariRayaPuasa = Holiday.builder()
                .name("Hari Raya Puasa")
                .description("Eid al-Fitr, marking the end of Ramadan")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new HariRayaPuasa())
                .build();
        final Holiday nationalDay = Holiday.builder()
                .name("National Day")
                .description("Commemoration of Singapore's independence on 9 August 1965")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.AUGUST, 9)
                .build();
        final Holiday hariRayaHaji = Holiday.builder()
                .name("Hari Raya Haji")
                .description("Eid al-Adha, the Feast of Sacrifice")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new HariRayaHaji())
                .build();
        final Holiday deepavali = Holiday.builder()
                .name("Deepavali")
                .description("Hindu Festival of Lights")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new Deepavali())
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
                .dateRoll(DateRolls.followingMonday())
                .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                .holiday(newYearsDay)
                .holiday(chineseNewYearFirstDay)
                .holiday(chineseNewYearSecondDay)
                .holiday(goodFriday)
                .holiday(labourDay)
                .holiday(vesakDay)
                .holiday(hariRayaPuasa)
                .holiday(nationalDay)
                .holiday(hariRayaHaji)
                .holiday(deepavali)
                .holiday(christmasDay)
                .build();
    }

}
