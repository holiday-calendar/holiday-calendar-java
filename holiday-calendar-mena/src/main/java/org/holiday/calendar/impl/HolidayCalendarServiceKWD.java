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
 * Service for provision of Kuwait exchange and central bank holiday calendar.
 *
 * <p>Calendar code: {@code KWD}. Covers market closure days for Boursa Kuwait
 * and Central Bank of Kuwait (CBK) financial institutions.
 *
 * <p>Weekend: Friday + Saturday (GCC market convention). Fixed holidays are not
 * rolled — the settlement calendar uses natural calendar dates per
 * {@link DateRolls#noRoll()}, consistent with the AED, SAR, and QAR convention.
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
public class HolidayCalendarServiceKWD extends AbstractHolidayCalendarService {

    private static final String CODE = "KWD";
    private static final String NAME = "Kuwait (Boursa Kuwait/CBK) Holidays";

    public HolidayCalendarServiceKWD() {
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
                .dateRoll(DateRolls.noRoll())
                .weekendDays(KuwaitHolidays.KUWAIT_WEEKEND)
                .holidays(KuwaitHolidays.kwdHolidays())
                .build();
    }

}
