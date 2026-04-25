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

import java.time.Month;

/**
 * Service for provision of the Japan (Bank of Japan / BOJ) holiday calendar.
 *
 * <p>Calendar code: {@code JPY}. The BOJ calendar shares all Japanese national
 * public holidays with the TSE calendar ({@code JP}) and additionally closes on
 * January 2, January 3, and December 31 as BOJ operational closures. The
 * substitute-holiday rule (振替休日) and sandwiched-day rule (国民の休日) both apply.
 */
public class HolidayCalendarServiceJPY extends AbstractHolidayCalendarService {

    private static final String CODE = "JPY";
    private static final String NAME = "Japan (BOJ) Holidays";

    public HolidayCalendarServiceJPY() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final Holiday yearStartJan2 = Holiday.builder()
                .name("Year-Start Holiday")
                .description("Bank of Japan operational closure — January 2")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.JANUARY, 2)
                .build();
        final Holiday yearStartJan3 = Holiday.builder()
                .name("Year-Start Holiday")
                .description("Bank of Japan operational closure — January 3")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.JANUARY, 3)
                .build();
        final Holiday yearEnd = Holiday.builder()
                .name("Year-End Holiday")
                .description("Bank of Japan operational closure — December 31")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.DECEMBER, 31)
                .build();

        HolidayCalendar base = HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.sundayToMonday())
                .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                .holidays(JapaneseHolidays.baseHolidays())
                .holiday(yearStartJan2)
                .holiday(yearStartJan3)
                .holiday(yearEnd)
                .build();
        return new JapaneseHolidayCalendar(base);
    }
}
