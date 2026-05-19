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
 * Service for provision of Saudi Arabia SAMA / Tadawul settlement holiday calendar.
 *
 * <p>Calendar code: {@code SAR}. Covers settlement closure days for the Saudi Central
 * Bank (SAMA) and the Saudi Exchange (Tadawul / TASI). The settlement holiday schedule
 * mirrors the Saudi Arabia national calendar ({@code SA}).
 *
 * <p>Roll strategy: {@link DateRolls#noRoll()} — settlement requires both
 * counterparties to be available on the same calendar date; there is no
 * roll-forward convention. All holidays are {@code rollable(false)}.
 *
 * <p>Weekend: Friday + Saturday (GCC market convention).
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceSAR extends AbstractHolidayCalendarService {

    private static final String CODE = "SAR";
    private static final String NAME = "Saudi Arabia (Tadawul/SAMA) Holidays";

    public HolidayCalendarServiceSAR() {
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
                .dateRoll(DateRolls.noRoll())
                .weekendDays(SaudiHolidays.SAUDI_WEEKEND)
                .holidays(SaudiHolidays.baseHolidays(false))
                .build();
    }

}
