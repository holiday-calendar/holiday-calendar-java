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
import com.github.davejoyce.calendar.function.EasterObservance;
import com.github.davejoyce.calendar.observance.EasterMonday;
import com.github.davejoyce.calendar.observance.GoodFriday;
import com.github.davejoyce.calendar.observance.WesternEaster;
import com.github.davejoyce.calendar.observance.uk.EarlyMayBankHoliday;
import com.github.davejoyce.calendar.observance.uk.SpringBankHoliday;
import com.github.davejoyce.calendar.observance.uk.SummerBankHoliday;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

public class HolidayCalendarServiceUK implements HolidayCalendarService {

    private static final String CODE = "UK";
    private static final String NAME = "United Kingdom National Holidays";

    @Override
    public boolean isProvided(String code) {
        return CODE.equalsIgnoreCase(code);
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
                .description("Commemoration of the crucifixion of Jesus Christ")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new GoodFriday(easter))
                .build();
        final Holiday easterMonday = Holiday.builder()
                .name("Easter Monday")
                .description("Day after Easter Sunday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EasterMonday(easter))
                .build();
        final Holiday earlyMayBankHoliday = Holiday.builder()
                .name("Early May Bank Holiday")
                .description("Early May bank holiday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EarlyMayBankHoliday())
                .build();
        final Holiday springBankHoliday = Holiday.builder()
                .name("Spring Bank Holiday")
                .description("Late May bank holiday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new SpringBankHoliday())
                .build();
        final Holiday silverJubileeBankHoliday = Holiday.builder()
                .name("Silver Jubilee Bank Holiday")
                .description("Silver Jubilee of Queen Elizabeth II")
                .type(Holiday.Type.SPECIAL_ANNIVERSARY)
                .rollable(false)
                .anniversaryDate(LocalDate.of(1977, Month.JUNE, 7))
                .build();
        final Holiday goldenJubileeBankHoliday = Holiday.builder()
                .name("Golden Jubilee Bank Holiday")
                .description("Golden Jubilee of Queen Elizabeth II")
                .type(Holiday.Type.SPECIAL_ANNIVERSARY)
                .rollable(false)
                .anniversaryDate(LocalDate.of(2002, Month.JUNE, 3))
                .build();
        final Holiday diamondJubileeBankHoliday = Holiday.builder()
                .name("Diamond Jubilee Bank Holiday")
                .description("Diamond Jubilee of Queen Elizabeth II")
                .type(Holiday.Type.SPECIAL_ANNIVERSARY)
                .rollable(false)
                .anniversaryDate(LocalDate.of(2012, Month.JUNE, 5))
                .build();
        final Holiday platinumJubileeBankHoliday = Holiday.builder()
                .name("Platinum Jubilee Bank Holiday")
                .description("Platinum Jubilee of Queen Elizabeth II")
                .type(Holiday.Type.SPECIAL_ANNIVERSARY)
                .rollable(false)
                .anniversaryDate(LocalDate.of(2022, Month.JUNE, 3))
                .build();
        final Holiday summerBankHoliday = Holiday.builder()
                .name("Summer Bank Holiday")
                .description("Summer bank holiday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new SummerBankHoliday())
                .build();
        final Holiday christmasDay = Holiday.builder()
                .name("Christmas Day")
                .description("Commemoration of the birth of Jesus Christ")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.DECEMBER, 25)
                .build();
        final Holiday boxingDay = Holiday.builder()
                .name("Boxing Day")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.DECEMBER, 26)
                .build();

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(dateToRoll -> {
                    final Optional<LocalDate> boxingDayDate = boxingDay.dateForYear(dateToRoll.getYear());
                    final boolean isBoxingDay = boxingDayDate.isPresent() && dateToRoll.equals(boxingDayDate.get());
                    switch (dateToRoll.getDayOfWeek()) {
                        case SATURDAY:
                            return dateToRoll.plusDays(2L);
                        case SUNDAY:
                            return dateToRoll.plusDays(isBoxingDay ? 2L : 1L);
                        case MONDAY:
                            return dateToRoll.plusDays(isBoxingDay ? 1L : 0L);
                        default:
                            return dateToRoll;
                    }
                })
                .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                .holiday(newYearsDay)
                .holiday(goodFriday)
                .holiday(easterMonday)
                .holiday(earlyMayBankHoliday)
                .holiday(springBankHoliday)
                .holiday(silverJubileeBankHoliday)
                .holiday(goldenJubileeBankHoliday)
                .holiday(diamondJubileeBankHoliday)
                .holiday(platinumJubileeBankHoliday)
                .holiday(summerBankHoliday)
                .holiday(christmasDay)
                .holiday(boxingDay)
                .build();
    }

}
