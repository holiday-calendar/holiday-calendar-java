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

import java.util.List;
import java.util.OptionalInt;

/**
 * Service for provision of Turkey BIST / TCMB settlement holiday calendar.
 *
 * <p>Calendar code: {@code TRY}. Covers settlement closure days for Borsa Istanbul
 * (BIST) and the Central Bank of the Republic of Turkey (TCMB). The settlement
 * holiday schedule mirrors the Turkey national calendar ({@code TR}).
 *
 * <p>Roll strategy: {@link DateRolls#noRoll()} — settlement requires both
 * counterparties to be available on the same calendar date; there is no
 * roll-forward convention. All holidays are {@code rollable(false)}.
 *
 * <p>Weekend: Saturday + Sunday (standard Western weekend).
 *
 * <p><strong>Half-day closure not modelled:</strong>
 * Borsa Istanbul (BIST) and TCMB observe a partial closure on 28 October
 * (Republic Day Eve): BIST closes at 12:30 local time and TCMB suspends TRY
 * settlement from midday. This calendar does not include 28 October as a holiday.
 * Callers relying on afternoon liquidity or same-day settlement on this date must
 * apply their own adjustment.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceTRY extends AbstractHolidayCalendarService {

    private static final String CODE = "TRY";
    private static final String NAME = "Turkey (BIST/TCMB) Holidays";

    public HolidayCalendarServiceTRY() {
        super(CODE, NAME);
    }

    @Override
    public OptionalInt dataValidThrough() {
        return OptionalInt.of(TurkeyHolidays.DATA_VALID_THROUGH);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.noRoll())
                .weekendDays(TurkeyHolidays.STANDARD_WEEKEND)
                .holidays(TurkeyHolidays.baseHolidays(false, List.of()))
                .build();
    }

}
