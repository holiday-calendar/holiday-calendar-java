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
import org.holiday.calendar.Holiday;
import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.function.DateRolls;
import org.holiday.calendar.observance.ca.NationalDayForTruthAndReconciliation;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.holiday.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of the Canada national/federal holiday calendar.
 * Distinct from {@link HolidayCalendarServiceXTSE}, the Toronto Stock
 * Exchange (TSX) trading calendar: this calendar contains only holidays
 * observed by the Government of Canada, and never includes early-close
 * (half-day) trading sessions.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceCA extends AbstractHolidayCalendarService {

    private static final String CODE = "CA";
    private static final String NAME = "Canada National Holidays";

    public HolidayCalendarServiceCA() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final Holiday nationalDayForTruthAndReconciliation = Holiday.builder()
                .name("National Day For Truth and Reconciliation")
                .description("Recognition of the legacy of the Canadian Indian residential school system; "
                             + "federal statutory holiday first observed 30 September 2021")
                .type(Holiday.Type.FLOATING)
                .rollable(true)
                .observance(new NationalDayForTruthAndReconciliation())
                .build();
        final Holiday christmas = Holiday.builder()
                .name("Christmas Day")
                .description("Christmas Day")
                .type(Holiday.Type.FIXED)
                .monthDay(Month.DECEMBER, 25)
                .rollable(false)
                .build();
        final Holiday boxingDay = Holiday.builder()
                .name("Boxing Day")
                .description("Day after Christmas")
                .type(Holiday.Type.FIXED)
                .monthDay(Month.DECEMBER, 26)
                .rollable(false)
                .build();

        final List<Holiday> holidays = new ArrayList<>(CanadaHolidays.baseHolidays());
        holidays.add(nationalDayForTruthAndReconciliation);
        holidays.add(christmas);
        holidays.add(boxingDay);

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.followingMonday())
                .weekendDays(STANDARD_WEEKEND)
                .holidays(holidays)
                .build();
    }

}
