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
import org.holiday.calendar.observance.uk.ChristmasEveEarlyClose;
import org.holiday.calendar.observance.uk.NewYearsEveEarlyClose;
import org.holiday.calendar.observance.uk.UKDateRolls;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for provision of the London Stock Exchange (LSE) trading holiday
 * calendar. Distinct from {@link HolidayCalendarServiceUK}, the United
 * Kingdom national holiday calendar: this calendar additionally includes two
 * {@code EARLY_CLOSE} holidays representing LSE's Christmas Eve and New
 * Year's Eve half-day closes. LSE is closed on every bank holiday observed
 * by {@code UK}, including the one-off Jubilee bank holidays.
 *
 * @author <a href="mailto:dave@holiday-calendar.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceXLON extends AbstractHolidayCalendarService {

    private static final String CODE = "XLON";
    private static final String NAME = "United Kingdom (London Stock Exchange) Holidays";
    private static final ZoneId LSE_ZONE = ZoneId.of("Europe/London");

    public HolidayCalendarServiceXLON() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final Holiday newYearsDay = UkHolidays.newYearsDay();
        final Holiday christmasDay = UkHolidays.christmasDay();
        final Holiday boxingDay = UkHolidays.boxingDay();

        final Holiday christmasEveEarlyClose = Holiday.builder()
                .name("Christmas Eve")
                .description("LSE half-day close; shifts to the preceding Friday when " +
                             "December 24 falls on a Saturday or Sunday")
                .type(Holiday.Type.EARLY_CLOSE)
                .rollable(false)
                .observance(new ChristmasEveEarlyClose())
                .closeTime(LocalTime.of(12, 30))
                .zoneId(LSE_ZONE)
                .build();
        final Holiday newYearsEveEarlyClose = Holiday.builder()
                .name("New Year's Eve")
                .description("LSE half-day close; shifts to the preceding Friday when " +
                             "December 31 falls on a Saturday or Sunday")
                .type(Holiday.Type.EARLY_CLOSE)
                .rollable(false)
                .observance(new NewYearsEveEarlyClose())
                .closeTime(LocalTime.of(12, 30))
                .zoneId(LSE_ZONE)
                .build();

        final List<Holiday> holidays = new ArrayList<>(UkHolidays.baseHolidays());
        holidays.add(christmasEveEarlyClose);
        holidays.add(newYearsEveEarlyClose);

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(UKDateRolls.fixedHolidayRoll(newYearsDay, christmasDay, boxingDay))
                .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                .holidays(holidays)
                .build();
    }

}