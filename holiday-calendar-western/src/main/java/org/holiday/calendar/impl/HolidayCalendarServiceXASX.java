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
import org.holiday.calendar.observance.au.ChristmasEveEarlyClose;
import org.holiday.calendar.observance.au.NewYearsEveEarlyClose;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.holiday.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of Australia (ASX) holiday calendar. Distinct from
 * {@link HolidayCalendarServiceAU}, the Australia national public holiday
 * calendar: this calendar additionally includes two {@code EARLY_CLOSE}
 * holidays representing ASX's Christmas Eve and New Year's Eve half-day
 * closes. ASX is closed on every public holiday observed by {@code AU}.
 *
 * @author <a href="mailto:dave@holiday-calendar.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceXASX extends AbstractHolidayCalendarService {

    private static final String CODE = "XASX";
    private static final String NAME = "Australia (ASX) Holidays";
    private static final ZoneId ASX_ZONE = ZoneId.of("Australia/Sydney");

    public HolidayCalendarServiceXASX() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final Holiday christmasEveEarlyClose = Holiday.builder()
                .name("Christmas Eve")
                .description("ASX half-day close; suppressed entirely (not shifted) " +
                             "when December 24 falls on a Saturday or Sunday")
                .type(Holiday.Type.EARLY_CLOSE)
                .rollable(false)
                .observance(new ChristmasEveEarlyClose())
                .closeTime(LocalTime.of(14, 10))
                .zoneId(ASX_ZONE)
                .build();
        final Holiday newYearsEveEarlyClose = Holiday.builder()
                .name("New Year's Eve")
                .description("ASX half-day close; suppressed entirely (not shifted) " +
                             "when December 31 falls on a Saturday or Sunday")
                .type(Holiday.Type.EARLY_CLOSE)
                .rollable(false)
                .observance(new NewYearsEveEarlyClose())
                .closeTime(LocalTime.of(14, 10))
                .zoneId(ASX_ZONE)
                .build();

        final List<Holiday> holidays = new ArrayList<>(AuHolidays.baseHolidays());
        holidays.add(christmasEveEarlyClose);
        holidays.add(newYearsEveEarlyClose);

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.previousFridayOrFollowingMonday())
                .weekendDays(STANDARD_WEEKEND)
                .holidays(holidays)
                .build();
    }

}
