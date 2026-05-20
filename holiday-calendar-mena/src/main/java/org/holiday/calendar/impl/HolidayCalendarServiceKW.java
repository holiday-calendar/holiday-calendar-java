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
 * Service for provision of Kuwait national public holiday calendar.
 *
 * <p>Calendar code: {@code KW}. Covers public holidays observed in the State of
 * Kuwait as declared by the Amiri Diwan and the Central Bank of Kuwait (CBK).
 *
 * <p>Weekend: Friday + Saturday (GCC market convention). Fixed holidays that fall
 * on Friday or Saturday are observed on the following Sunday per
 * {@link DateRolls#followingSunday()}, consistent with the GCC market rule.
 *
 * <p>Islamic holidays are sourced from {@code eid-al-fitr-kw.csv},
 * {@code eid-al-adha-kw.csv}, {@code islamic-new-year-kw.csv}, and
 * {@code mawlid-kw.csv}. Dates for 2024–2026 are official CBK / Boursa Kuwait
 * announcements; 2027–2055 are projected from the Umm al-Qura tabular Islamic
 * calendar. Kuwait determines Islamic holiday dates by moon sighting and may
 * differ from Saudi Arabia by ±1 day.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceKW extends AbstractHolidayCalendarService {

    private static final String CODE = "KW";
    private static final String NAME = "Kuwait (National) Holidays";

    public HolidayCalendarServiceKW() {
        super(CODE, NAME);
    }

    @Override
    public OptionalInt dataValidThrough() {
        return OptionalInt.of(KuwaitHolidays.DATA_VALID_THROUGH);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                // GCC weekend is Friday + Saturday; holidays on these days roll to Sunday
                .dateRoll(DateRolls.followingSunday())
                .weekendDays(KuwaitHolidays.KUWAIT_WEEKEND)
                .holidays(KuwaitHolidays.baseHolidays(true))
                .build();
    }

}