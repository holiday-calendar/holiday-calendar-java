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
import org.holiday.calendar.observance.de.ChristmasEve;
import org.holiday.calendar.observance.de.NewYearsEve;

import java.util.ArrayList;
import java.util.List;

import static org.holiday.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of Germany (Xetra) holiday calendar. Distinct from
 * {@link HolidayCalendarServiceDE}, the Germany national public holiday
 * calendar: this calendar additionally includes Christmas Eve and New
 * Year's Eve as full non-trading days (Erfüllungstage) at Xetra/Frankfurt
 * Stock Exchange — both are confirmed genuinely NOT German public holidays,
 * and are omitted (not shifted) when they fall on a weekend. Xetra/FWB is
 * closed on every public holiday observed by {@code DE}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceXETR extends AbstractHolidayCalendarService {

    private static final String CODE = "XETR";
    private static final String NAME = "Germany (Xetra) Holidays";

    public HolidayCalendarServiceXETR() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final Holiday christmasEve = Holiday.builder()
                .name("Christmas Eve")
                .description("Full non-trading day (Erfüllungstag) at Xetra/Frankfurt Stock "
                        + "Exchange; omitted (not shifted) when it falls on a weekend")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new ChristmasEve())
                .build();
        final Holiday newYearsEve = Holiday.builder()
                .name("New Year's Eve")
                .description("Full non-trading day (Erfüllungstag) at Xetra/Frankfurt Stock "
                        + "Exchange; omitted (not shifted) when it falls on a weekend")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new NewYearsEve())
                .build();

        final List<Holiday> holidays = new ArrayList<>(DeHolidays.baseHolidays());
        holidays.add(christmasEve);
        holidays.add(newYearsEve);

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.previousFridayOrFollowingMonday())
                .weekendDays(STANDARD_WEEKEND)
                .holidays(holidays)
                .build();
    }

}
