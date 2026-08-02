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
 * Service for provision of the Switzerland national public holiday
 * calendar. Distinct from {@link HolidayCalendarServiceXSWX}, the SIX Swiss
 * Exchange trading calendar: this calendar never includes early-close
 * (half-day) trading sessions.
 *
 * <p>Switzerland has only <strong>one</strong> federally-mandated nationwide
 * public holiday: Swiss National Day (August 1), enshrined in the Federal
 * Act on the Swiss National Holiday. Every other holiday in this calendar,
 * including New Year's Day and Christmas, is decided at the cantonal level
 * and is adopted by most-but-not-all of the 26 cantons — for example, Good
 * Friday is not observed in Ticino or Valais. There is no single
 * unambiguous "Swiss national holiday list" defined by federal law: the 8
 * non-federal holidays here represent the <strong>majority-cantonal
 * convention</strong> (holidays observed by most/all cantons), not uniform
 * federal law.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceCH extends AbstractHolidayCalendarService {

    private static final String CODE = "CH";
    private static final String NAME = "Switzerland National Holidays";

    public HolidayCalendarServiceCH() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.previousFridayOrFollowingMonday())
                .weekendDays(STANDARD_WEEKEND)
                .holidays(ChHolidays.baseHolidays())
                .build();
    }

}
