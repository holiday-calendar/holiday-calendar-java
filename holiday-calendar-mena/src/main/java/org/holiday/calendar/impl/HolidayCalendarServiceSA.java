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

import java.util.OptionalInt;

/**
 * Service for provision of Saudi Arabia national public holiday calendar.
 *
 * <p>Calendar code: {@code SA}. Covers public holidays observed in the Kingdom of
 * Saudi Arabia as declared by the Saudi Exchange (Tadawul) and the Saudi Central
 * Bank (SAMA).
 *
 * <p>Weekend: Friday + Saturday (GCC market convention). Fixed holidays that fall
 * on Friday or Saturday are observed on the following Sunday per
 * {@link DateRolls#followingSunday()}.
 *
 * <p>Islamic holidays are sourced from {@code eid-al-fitr-sa.csv},
 * {@code eid-al-adha-sa.csv}, {@code islamic-new-year-sa.csv}, and
 * {@code mawlid-sa.csv}. Dates for 2024–2026 are official Tadawul/SAMA
 * announcements; 2027–2055 are projected from the Umm al-Qura tabular Islamic
 * calendar. Saudi Arabia uses the Umm al-Qura calendar as its official state
 * calendar; note that moon sighting determinations may still differ from UAE
 * announcements by ±1 day.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceSA extends AbstractHolidayCalendarService {

    private static final String CODE = "SA";
    private static final String NAME = "Saudi Arabia (National) Holidays";

    public HolidayCalendarServiceSA() {
        super(CODE, NAME);
    }

    @Override
    public OptionalInt dataValidThrough() {
        return OptionalInt.of(SaudiHolidays.DATA_VALID_THROUGH);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                // GCC weekend is Friday + Saturday; holidays on these days roll to Sunday
                .dateRoll(DateRolls.followingSunday())
                .weekendDays(SaudiHolidays.SAUDI_WEEKEND)
                .holidays(SaudiHolidays.baseHolidays(true))
                .build();
    }

}
