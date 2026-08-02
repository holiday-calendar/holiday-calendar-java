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
import org.holiday.calendar.observance.uk.UKDateRolls;

import java.util.List;

/**
 * Service for provision of the United Kingdom national/government bank
 * holiday calendar. Distinct from {@link HolidayCalendarServiceXLON}, the
 * London Stock Exchange (LSE) trading calendar: this calendar contains only
 * England-and-Wales bank holidays, and never includes early-close (half-day)
 * trading sessions.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceUK extends AbstractHolidayCalendarService {

    private static final String CODE = "UK";
    private static final String NAME = "United Kingdom National Holidays";

    public HolidayCalendarServiceUK() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final Holiday newYearsDay = UkHolidays.newYearsDay();
        final Holiday christmasDay = UkHolidays.christmasDay();
        final Holiday boxingDay = UkHolidays.boxingDay();
        final List<Holiday> holidays = UkHolidays.baseHolidays();

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(UKDateRolls.fixedHolidayRoll(newYearsDay, christmasDay, boxingDay))
                .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                .holidays(holidays)
                .build();
    }

}