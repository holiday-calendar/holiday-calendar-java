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
import org.holiday.calendar.observance.christian.EasterObservance;
import org.holiday.calendar.observance.christian.GoodFriday;
import org.holiday.calendar.observance.christian.WesternEaster;
import org.holiday.calendar.observance.fr.ChristmasEveEarlyClose;
import org.holiday.calendar.observance.fr.NewYearsEveEarlyClose;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.holiday.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of France (Euronext Paris) holiday calendar.
 * Distinct from {@link HolidayCalendarServiceFR}, the France national
 * public holiday calendar: this calendar additionally includes Good
 * Friday — a confirmed Euronext Paris market closure that is not a French
 * national holiday — and two {@code EARLY_CLOSE} holidays representing
 * Euronext Paris's Christmas Eve and New Year's Eve half-day closes.
 * Euronext Paris is closed on every public holiday observed by {@code FR}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceXPAR extends AbstractHolidayCalendarService {

    private static final String CODE = "XPAR";
    private static final String NAME = "France (Euronext Paris) Holidays";
    private static final ZoneId EURONEXT_PARIS_ZONE = ZoneId.of("Europe/Paris");

    public HolidayCalendarServiceXPAR() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final EasterObservance easter = new WesternEaster();

        final Holiday goodFriday = Holiday.builder()
                .name("Good Friday")
                .description("Friday before Easter Sunday; Euronext Paris market closure, "
                             + "not a French national holiday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new GoodFriday(easter))
                .build();
        final Holiday christmasEveEarlyClose = Holiday.builder()
                .name("Christmas Eve")
                .description("Euronext Paris half-day close; suppressed entirely (not shifted) " +
                             "when December 24 falls on a Saturday or Sunday")
                .type(Holiday.Type.EARLY_CLOSE)
                .rollable(false)
                .observance(new ChristmasEveEarlyClose())
                .closeTime(LocalTime.of(14, 5))
                .zoneId(EURONEXT_PARIS_ZONE)
                .build();
        final Holiday newYearsEveEarlyClose = Holiday.builder()
                .name("New Year's Eve")
                .description("Euronext Paris half-day close; suppressed entirely (not shifted) " +
                             "when December 31 falls on a Saturday or Sunday")
                .type(Holiday.Type.EARLY_CLOSE)
                .rollable(false)
                .observance(new NewYearsEveEarlyClose())
                .closeTime(LocalTime.of(14, 5))
                .zoneId(EURONEXT_PARIS_ZONE)
                .build();

        final List<Holiday> holidays = new ArrayList<>(FrHolidays.baseHolidays());
        holidays.add(goodFriday);
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
