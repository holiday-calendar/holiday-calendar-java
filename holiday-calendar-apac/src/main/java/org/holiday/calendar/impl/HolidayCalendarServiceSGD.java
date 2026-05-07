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
 * Service for provision of the Singapore MAS MEPS+ holiday calendar.
 *
 * <p>Calendar code: {@code SGD}. MEPS+ (MAS Electronic Payment System Plus) is
 * Singapore's real-time gross settlement (RTGS) system for SGD interbank payments.
 * Settlement holidays follow Singapore's statutory public holiday schedule.
 *
 * <p>Roll strategy: {@link DateRolls#noRoll()} — RTGS settlement requires both
 * counterparties to be available on the same calendar date; there is no roll-forward
 * convention. All holidays are {@code rollable(false)}.
 *
 * <p>Reference: MAS MEPS+ Service Agreement (effective 1 Oct 2025), which defines
 * "business day" as any Monday–Friday excluding Singapore public holidays.
 * No additional bank-specific closure days (analogous to BOJ Jan 2, Jan 3, Dec 31)
 * are published by MAS for MEPS+.
 */
public class HolidayCalendarServiceSGD extends AbstractHolidayCalendarService {

    private static final String CODE = "SGD";
    private static final String NAME = "Singapore (MAS/MEPS+) Holidays";

    public HolidayCalendarServiceSGD() {
        super(CODE, NAME);
    }

    @Override
    public OptionalInt dataValidThrough() {
        return OptionalInt.of(SingaporeHolidays.DATA_VALID_THROUGH);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.noRoll())
                .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                .holidays(SingaporeHolidays.baseHolidays(false))
                .build();
    }
}
