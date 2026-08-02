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
import java.util.ArrayList;
import java.util.List;

import static org.holiday.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of Switzerland (SIX Swiss Exchange) holiday
 * calendar. Distinct from {@link HolidayCalendarServiceCH}, the Switzerland
 * national public holiday calendar: this calendar additionally includes
 * Christmas Eve and New Year's Eve, both SIX-only market holidays per the
 * official SIX Trading Calendar, not cantonal-law public holidays. SIX is
 * closed on every public holiday observed by {@code CH}. All holidays here
 * are {@code FIXED}, {@code rollable(true)} — SIX has no {@code EARLY_CLOSE}
 * (half-day) sessions.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceXSWX extends AbstractHolidayCalendarService {

    private static final String CODE = "XSWX";
    private static final String NAME = "Switzerland (SIX) Holidays";

    public HolidayCalendarServiceXSWX() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final Holiday christmasEve = Holiday.builder()
                .name("Christmas Eve")
                .description("SIX Swiss Exchange market holiday per official Trading Calendar")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.DECEMBER, 24)
                .build();
        final Holiday newYearsEve = Holiday.builder()
                .name("New Year's Eve")
                .description("SIX Swiss Exchange market holiday per official Trading Calendar")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.DECEMBER, 31)
                .build();

        final List<Holiday> holidays = new ArrayList<>(ChHolidays.baseHolidays());
        holidays.add(christmasEve);
        holidays.add(newYearsEve);

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.previousFridayOrFollowingMonday())
                .weekendDays(STANDARD_WEEKEND)
                .holidays(holidays)
                .build();
    }

}
