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
 * Service for provision of Turkey BIST / TCMB settlement holiday calendar.
 *
 * <p>Calendar code: {@code TRY}. Covers settlement closure days for Borsa Istanbul
 * (BIST) and the Central Bank of the Republic of Turkey (TCMB). The settlement
 * holiday schedule mirrors the Turkey national calendar ({@code TR}), differing
 * only in roll strategy.
 *
 * <p>Roll strategy: {@link DateRolls#noRoll()} — settlement requires both
 * counterparties to be available on the same calendar date; there is no
 * roll-forward convention. All holidays are {@code rollable(false)}.
 *
 * <p>Weekend: Saturday + Sunday (standard Western weekend).
 *
 * <p>Law No. 2429 (Ulusal Bayram ve Genel Tatiller Hakkında Kanun) declares
 * Republic Day Eve (28 October) a nationwide half-day holiday, in effect from
 * 13:00 {@code Europe/Istanbul}. This is modelled as an {@code EARLY_CLOSE}
 * holiday, non-rollable, and reported separately via {@link
 * HolidayCalendar#calculateEarlyCloses(int)} rather than {@link
 * HolidayCalendar#calculate(int)}. It does not shift when 28 October falls on
 * a Saturday or Sunday.
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
                .holidays(TurkeyHolidays.baseHolidays(false))
                .holidays(TurkeyHolidays.earlyCloseHolidays())
                .build();
    }

}
