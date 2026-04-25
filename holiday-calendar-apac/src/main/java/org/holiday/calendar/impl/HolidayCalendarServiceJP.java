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
 * Service for provision of the Japan (Tokyo Stock Exchange / TSE) holiday calendar.
 *
 * <p>Calendar code: {@code JP}. The TSE calendar is based on Japanese national
 * public holidays with the substitute-holiday rule (振替休日): when a holiday falls
 * on Sunday, the following Monday is observed instead. The sandwiched-day rule
 * (国民の休日) is also applied: a non-holiday weekday between two consecutive
 * national holidays becomes a holiday.
 */
public class HolidayCalendarServiceJP extends AbstractHolidayCalendarService {

    private static final String CODE = "JP";
    private static final String NAME = "Japan (TSE) Holidays";

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
