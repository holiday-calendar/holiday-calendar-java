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

import static org.holiday.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of the Australia national public holiday calendar.
 * Distinct from {@link HolidayCalendarServiceXASX}, the Australian
 * Securities Exchange (ASX) trading calendar: this calendar contains only
 * the 9 national public holidays, and never includes early-close (half-day)
 * trading sessions.
 *
 * @author <a href="mailto:dave@holiday-calendar.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceAU extends AbstractHolidayCalendarService {

    private static final String CODE = "AU";
    private static final String NAME = "Australia National Holidays";

    public HolidayCalendarServiceAU() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.previousFridayOrFollowingMonday())
                .weekendDays(STANDARD_WEEKEND)
                .holidays(AuHolidays.baseHolidays())
                .build();
    }

}
