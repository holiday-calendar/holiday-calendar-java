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
import org.holiday.calendar.observance.ca.BoxingDayCAD;
import org.holiday.calendar.observance.ca.ChristmasEveEarlyClose;

import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.holiday.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of the Toronto Stock Exchange (TSX) trading holiday
 * calendar. Distinct from {@link HolidayCalendarServiceCA}, the Canada
 * national holiday calendar: this calendar additionally includes a TSX
 * Christmas Eve early close.
 *
 * <p>TSX's weekend substitution rule, per TMX Group's official Holiday
 * Operating Schedule (verified against the 2021 and 2016 published
 * schedules, and cross-checked against the 2017 Canada Day schedule): a
 * statutory holiday falling on Saturday or Sunday rolls <strong>forward</strong>
 * to the next available business day — never backward. This is identical
 * to the Bank of Canada (Lynx) settlement convention used by {@code CAD}.
 * Boxing Day requires collision-aware handling — see {@link BoxingDayCAD},
 * reused here — since its own roll can collide with Christmas Day's rolled
 * observance, and vice versa (e.g. in 2021, Christmas Day and Boxing Day
 * both rolled forward to Monday Dec 27 and Tuesday Dec 28 respectively,
 * while the ordinary Dec 24 early close proceeded unaffected).</p>
 *
 * @author <a href="mailto:dave@holiday-calendar.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceXTSE extends AbstractHolidayCalendarService {

    private static final String CODE = "XTSE";
    private static final String NAME = "Canada (Toronto Stock Exchange) Holidays";
    private static final ZoneId TSX_ZONE = ZoneId.of("America/Toronto");

    public HolidayCalendarServiceXTSE() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final Holiday nationalDayForTruthAndReconciliation = Holiday.builder()
                .name("National Day For Truth and Reconciliation")
                .description("Recognition of the legacy of the Canadian Indian residential school system")
                .type(Holiday.Type.FIXED)
                .monthDay(Month.SEPTEMBER, 30)
                .rollable(false)
                .build();
        final Holiday christmasEveEarlyClose = Holiday.builder()
                .name("Christmas Eve")
                .description("TSX 1:00pm local early close; occurs whenever "
                             + "December 24 falls Monday through Friday")
                .type(Holiday.Type.EARLY_CLOSE)
                .rollable(false)
                .observance(new ChristmasEveEarlyClose())
                .closeTime(LocalTime.of(13, 0))
                .zoneId(TSX_ZONE)
                .build();
        final Holiday christmas = Holiday.builder()
                .name("Christmas Day")
                .description("Christmas Day")
                .type(Holiday.Type.FIXED)
                .monthDay(Month.DECEMBER, 25)
                .rollable(true)
                .build();
        final Holiday boxingDay = Holiday.builder()
                .name("Boxing Day")
                .description("Day after Christmas (observed; collision-aware with Christmas Day)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new BoxingDayCAD())
                .build();

        final List<Holiday> holidays = new ArrayList<>(CanadaHolidays.baseHolidays());
        holidays.add(nationalDayForTruthAndReconciliation);
        holidays.add(christmasEveEarlyClose);
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