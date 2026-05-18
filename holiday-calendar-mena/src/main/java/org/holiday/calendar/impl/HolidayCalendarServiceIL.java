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
 * Service for provision of Israel national public holiday calendar.
 *
 * <p>Calendar code: {@code IL}. Covers public holidays observed in Israel as
 * published by the Israeli government and confirmed against TASE market
 * closure announcements.
 *
 * <p>Weekend: Friday + Saturday (Israeli work week; Sunday is the first
 * business day). Fixed holidays that fall on Friday or Saturday roll to the
 * following Sunday per {@link DateRolls#followingSunday()}.
 *
 * <p>All nine holidays are computed algorithmically via
 * {@code net.time4j.calendar.HebrewCalendar}; no lookup tables or data
 * ceiling applies. Independence Day (Yom Ha'atzmaut) applies a statutory
 * postponement rule; see {@link org.holiday.calendar.observance.hebrew.IndependenceDay}.
 *
 * <p>MSCI classification: Developed Market (DM) — the only Developed Market
 * in the MENA module.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceIL extends AbstractHolidayCalendarService {

    private static final String CODE = "IL";
    private static final String NAME = "Israel (National) Holidays";

    public HolidayCalendarServiceIL() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                // Israeli weekend is Fri+Sat; fixed holidays rolling to following Sunday
                .dateRoll(DateRolls.followingSunday())
                .weekendDays(IsraelHolidays.ISRAEL_WEEKEND)
                .holidays(IsraelHolidays.baseHolidays())
                .build();
    }

}
