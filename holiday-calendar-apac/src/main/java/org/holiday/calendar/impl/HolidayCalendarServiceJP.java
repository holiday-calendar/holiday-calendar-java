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
import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.function.DateRolls;

/**
 * Service for provision of the Japan national public holiday calendar.
 *
 * <p>Calendar code: {@code JP}. Based on Japan's national Holiday Act, with the
 * substitute-holiday rule (振替休日): when a holiday falls on Sunday, the
 * following Monday is observed instead. The sandwiched-day rule (国民の休日) is
 * also applied: a non-holiday weekday between two consecutive national
 * holidays becomes a holiday. This is a pure national calendar with no
 * exchange- or bank-specific content; see {@link HolidayCalendarServiceJPY}
 * for the Bank of Japan's settlement calendar, which adds genuine
 * BOJ-specific closures (Jan 2, Jan 3, Dec 31) on top of this list.
 */
public class HolidayCalendarServiceJP extends AbstractHolidayCalendarService {

    private static final String CODE = "JP";
    private static final String NAME = "Japan National Holidays";

    public HolidayCalendarServiceJP() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        HolidayCalendar base = HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.sundayToMonday())
                .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                .holidays(JapaneseHolidays.baseHolidays())
                .build();
        return new JapaneseHolidayCalendar(base);
    }
}
