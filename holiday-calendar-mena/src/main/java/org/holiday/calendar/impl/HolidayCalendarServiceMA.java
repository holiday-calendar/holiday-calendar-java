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
 * Service for provision of Morocco national public holiday calendar.
 *
 * <p>Calendar code: {@code MA}. Covers public holidays observed in the Kingdom of
 * Morocco as declared by the Ministry of Interior.
 *
 * <p>Weekend: Saturday + Sunday. Morocco uses a standard Monday–Friday workweek
 * (established in 2004), unlike GCC countries that observe Friday + Saturday.
 *
 * <p>Roll strategy: {@link DateRolls#followingMonday()} for fixed Gregorian
 * holidays — when a fixed holiday falls on Saturday or Sunday it is observed on
 * the following Monday, consistent with Moroccan Labour Code practice. Islamic
 * holidays are {@code rollable(false)}: Islamic holiday substitutions are issued
 * by case-by-case government decree in Morocco rather than by a statutory
 * automatic roll rule.
 *
 * <p>Islamic holidays are sourced from {@code eid-al-fitr-ma.csv},
 * {@code eid-al-adha-ma.csv}, {@code islamic-new-year-ma.csv}, and
 * {@code mawlid-ma.csv}. Dates for 2024–2025 are official Moroccan government /
 * CSE holiday announcements. Dates for 2026–2055 are projected from the Umm
 * al-Qura tabular Islamic calendar. Morocco determines Islamic holiday dates by
 * moon sighting and may differ from GCC countries by ±1 day — verify projected
 * dates against official Moroccan announcements as each year is published.
 * Corrections require a new JAR release.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceMA extends AbstractHolidayCalendarService {

    private static final String CODE = "MA";
    private static final String NAME = "Morocco (National) Holidays";

    public HolidayCalendarServiceMA() {
        super(CODE, NAME);
    }

    @Override
    public OptionalInt dataValidThrough() {
        return OptionalInt.of(MoroccoHolidays.DATA_VALID_THROUGH);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.followingMonday())
                .weekendDays(MoroccoHolidays.MOROCCO_WEEKEND)
                .holidays(MoroccoHolidays.maHolidays())
                .build();
    }

}
