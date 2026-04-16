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
import org.holiday.calendar.observance.christian.AscensionDay;
import org.holiday.calendar.observance.christian.EasterMonday;
import org.holiday.calendar.observance.christian.EasterObservance;
import org.holiday.calendar.observance.christian.GoodFriday;
import org.holiday.calendar.observance.christian.WesternEaster;
import org.holiday.calendar.observance.christian.WhitMonday;

import java.time.Month;

import static org.holiday.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of Switzerland (SIC/Swiss National Bank) holiday calendar.
 *
 * <p>The SIC (Swiss Interbank Clearing) system, operated by SIX Interbank Clearing AG
 * on behalf of the Swiss National Bank, follows a no-adjustment convention: holidays
 * are observed on their published calendar dates regardless of day of week. If a
 * closure date falls on a weekend, no compensatory weekday closure is designated.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 * @see <a href="https://www.six-group.com/en/products-services/banking-services/interbank-clearing/settlement-services/sic.html">SIX SIC Settlement Services</a>
 */
public class HolidayCalendarServiceCHF extends AbstractHolidayCalendarService {

    private static final String CODE = "CHF";
    private static final String NAME = "Switzerland (SIC/SNB) Holidays";

    public HolidayCalendarServiceCHF() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final EasterObservance easter = new WesternEaster();

        final Holiday newYearsDay = Holiday.builder()
                                           .name("New Year's Day")
                                           .description("First day of new year in the Common Era (CE)")
                                           .type(Holiday.Type.FIXED)
                                           .rollable(false)
                                           .monthDay(Month.JANUARY, 1)
                                           .build();
        final Holiday berchtoldstag = Holiday.builder()
                                             .name("Berchtoldstag")
                                             .description("Day after New Year's Day; SIC settlement closure day "
                                                     + "observed as a cantonal holiday across most Swiss cantons")
                                             .type(Holiday.Type.FIXED)
                                             .rollable(false)
                                             .monthDay(Month.JANUARY, 2)
                                             .build();
        final Holiday goodFriday = Holiday.builder()
                                          .name("Good Friday")
                                          .description("Friday before Easter Sunday")
                                          .type(Holiday.Type.FLOATING)
                                          .rollable(false)
                                          .observance(new GoodFriday(easter))
                                          .build();
        final Holiday easterMonday = Holiday.builder()
                                            .name("Easter Monday")
                                            .description("Monday after Easter Sunday")
                                            .type(Holiday.Type.FLOATING)
                                            .rollable(false)
                                            .observance(new EasterMonday(easter))
                                            .build();
        final Holiday labourDay = Holiday.builder()
                                         .name("Labour Day")
                                         .description("International Workers' Day")
                                         .type(Holiday.Type.FIXED)
                                         .rollable(false)
                                         .monthDay(Month.MAY, 1)
                                         .build();
        // AscensionDay offset 39: Easter Sunday + 39 days = Thursday (the "40th day"
        // counting Easter Sunday as day 1). Do not change to 40.
        final Holiday ascensionDay = Holiday.builder()
                                            .name("Ascension Day")
                                            .description("The 40th day of Easter; Jesus Christ's ascension into heaven")
                                            .type(Holiday.Type.FLOATING)
                                            .rollable(false)
                                            .observance(new AscensionDay(easter))
                                            .build();
        final Holiday whitMonday = Holiday.builder()
                                          .name("Whit Monday")
                                          .description("Monday after Whit Sunday (Pentecost)")
                                          .type(Holiday.Type.FLOATING)
                                          .rollable(false)
                                          .observance(new WhitMonday(easter))
                                          .build();
        final Holiday swissNationalDay = Holiday.builder()
                                                .name("Swiss National Day")
                                                .description("Date of the Federal Charter of 1291")
                                                .type(Holiday.Type.FIXED)
                                                .rollable(false)
                                                .monthDay(Month.AUGUST, 1)
                                                .build();
        final Holiday christmasDay = Holiday.builder()
                                            .name("Christmas Day")
                                            .description("Celebration of traditional Christmas holiday")
                                            .type(Holiday.Type.FIXED)
                                            .rollable(false)
                                            .monthDay(Month.DECEMBER, 25)
                                            .build();
        final Holiday boxingDay = Holiday.builder()
                                         .name("St. Stephen's Day")
                                         .description("Day after Christmas")
                                         .type(Holiday.Type.FIXED)
                                         .rollable(false)
                                         .monthDay(Month.DECEMBER, 26)
                                         .build();

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.noRoll())
                .weekendDays(STANDARD_WEEKEND)
                .holiday(newYearsDay)
                .holiday(berchtoldstag)
                .holiday(goodFriday)
                .holiday(easterMonday)
                .holiday(labourDay)
                .holiday(ascensionDay)
                .holiday(whitMonday)
                .holiday(swissNationalDay)
                .holiday(christmasDay)
                .holiday(boxingDay)
                .build();
    }

}