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
 * Service for provision of Bahrain national public holiday calendar.
 *
 * <p>Calendar code: {@code BH}. Covers public holidays observed in the Kingdom of
 * Bahrain as declared by the Crown Prince and Prime Minister.
 *
 * <p>Weekend: Friday + Saturday (GCC market convention). Fixed holidays that fall
 * on Friday or Saturday are observed on the following Sunday per
 * {@link DateRolls#followingSunday()}, consistent with the GCC market rule.
 *
 * <p>Islamic holidays are sourced from {@code eid-al-fitr-bh.csv},
 * {@code eid-al-adha-bh.csv}, {@code arafat-day-bh.csv},
 * {@code islamic-new-year-bh.csv}, {@code ashura-bh.csv}, and
 * {@code mawlid-bh.csv}. Dates for 2024–2025 are official CBB / Boursa Bahrain
 * announcements; 2026–2055 are projected from the Umm al-Qura tabular Islamic
 * calendar. Bahrain determines Islamic holiday dates by moon sighting and may
 * differ from Saudi Arabia by ±1 day.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceBH extends AbstractHolidayCalendarService {

    private static final String CODE = "BH";
    private static final String NAME = "Bahrain (National) Holidays";

    public HolidayCalendarServiceBH() {
        super(CODE, NAME);
    }

    @Override
    public OptionalInt dataValidThrough() {
        return OptionalInt.of(BahrainHolidays.DATA_VALID_THROUGH);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                // GCC weekend is Friday + Saturday; holidays on these days roll to Sunday
                .dateRoll(DateRolls.followingSunday())
                .weekendDays(BahrainHolidays.BAHRAIN_WEEKEND)
                .holidays(BahrainHolidays.baseHolidays(true))
                .build();
    }

}
