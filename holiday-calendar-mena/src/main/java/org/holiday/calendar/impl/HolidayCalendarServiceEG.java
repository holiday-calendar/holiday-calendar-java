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
 * Service for provision of Egypt national public holiday calendar.
 *
 * <p>Calendar code: {@code EG}. Covers public holidays observed in the Arab
 * Republic of Egypt as declared by the Egyptian Government.
 *
 * <p>Weekend: Friday + Saturday (GCC market convention). Fixed holidays that fall
 * on Friday or Saturday are observed on the following Sunday per
 * {@link DateRolls#followingSunday()}, consistent with the GCC market rule.
 *
 * <p>Islamic holidays are sourced from {@code eid-al-fitr-eg.csv},
 * {@code eid-al-adha-eg.csv}, {@code arafat-day-eg.csv},
 * {@code islamic-new-year-eg.csv}, and {@code mawlid-eg.csv}. Dates for 2024–2025
 * are official CBE / EGX announcements; 2026–2055 are projected from the Umm
 * al-Qura tabular Islamic calendar. Egypt determines Islamic holiday dates by moon
 * sighting and may differ from Saudi Arabia by ±1 day.
 *
 * <p>Sham El-Nessim is computed algorithmically as the day after Coptic Easter
 * and is available for all years within the valid range of the Orthodox Easter
 * algorithm (530–3399 AD).
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceEG extends AbstractHolidayCalendarService {

    private static final String CODE = "EG";
    private static final String NAME = "Egypt (National) Holidays";

    public HolidayCalendarServiceEG() {
        super(CODE, NAME);
    }

    @Override
    public OptionalInt dataValidThrough() {
        return OptionalInt.of(EgyptHolidays.DATA_VALID_THROUGH);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                // GCC weekend is Friday + Saturday; holidays on these days roll to Sunday
                .dateRoll(DateRolls.followingSunday())
                .weekendDays(EgyptHolidays.EGYPT_WEEKEND)
                .holidays(EgyptHolidays.baseHolidays(true))
                .build();
    }

}
