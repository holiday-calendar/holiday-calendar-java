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
 * Service for provision of Israel TASE / Bank of Israel settlement holiday calendar.
 *
 * <p>Calendar code: {@code ILS}. Covers settlement closure days for the Tel Aviv
 * Stock Exchange (TASE) and the Bank of Israel. The settlement holiday schedule
 * mirrors the Israel national calendar ({@code IL}).
 *
 * <p>Roll strategy: {@link DateRolls#noRoll()} — settlement requires both
 * counterparties to be available on the same calendar date; there is no
 * roll-forward convention. All holidays are {@code rollable(false)}.
 *
 * <p>Weekend: Friday + Saturday (Israeli market convention).
 *
 * <p><strong>Scope limitation — half-day closures:</strong> TASE closes early
 * (typically 13:00 IST) on holiday eves: Erev Rosh Hashanah, Erev Yom Kippur,
 * Erev Passover, Erev Shavuot, and Erev Sukkot. The current {@code Holiday}
 * sealed interface models only full-day closures. These half-day eves are
 * <em>not</em> represented in this calendar. Users performing same-day or T+1
 * settlement date calculations for trades placed in the morning of an eve day
 * must apply additional TASE-specific adjustments outside this library.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceILS extends AbstractHolidayCalendarService {

    private static final String CODE = "ILS";
    private static final String NAME = "Israel (TASE/Bank of Israel) Holidays";

    public HolidayCalendarServiceILS() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.noRoll())
                .weekendDays(IsraelHolidays.ISRAEL_WEEKEND)
                .holidays(IsraelHolidays.baseHolidays())
                .build();
    }

}
