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
import org.holiday.calendar.observance.christian.EasterMonday;
import org.holiday.calendar.observance.christian.EasterObservance;
import org.holiday.calendar.observance.christian.GoodFriday;
import org.holiday.calendar.observance.christian.WesternEaster;
import org.holiday.calendar.observance.uk.EarlyMayBankHoliday;
import org.holiday.calendar.observance.uk.SpringBankHoliday;
import org.holiday.calendar.observance.uk.SummerBankHoliday;
import org.holiday.calendar.observance.uk.UKDateRolls;

import java.time.Month;

/**
 * Service for provision of United Kingdom (CHAPS) holiday calendar.
 *
 * <p>CHAPS (Clearing House Automated Payment System) is operated by the Bank of
 * England and governs GBP high-value payment settlement. The system observes eight
 * recurring annual bank holidays aligned with the England and Wales statutory bank
 * holiday schedule.
 *
 * <p>Date-roll behaviour:
 * <ul>
 *   <li>Christmas Day and Boxing Day: when either falls on a weekend, both
 *       substitute days are observed on the following Monday and Tuesday (+2 days).
 *   <li>New Year's Day: when it falls on Saturday it is observed on the following
 *       Monday (+2 days); when it falls on Sunday it is observed on Monday (+1 day).
 * </ul>
 *
 * <p>NOTE: This calendar's {@code dateRoll} lambda is the authoritative roll
 * mechanism for all three rollable fixed holidays. Setting {@code rollable(true)}
 * alone is insufficient — each holiday's identity is checked in the lambda.
 * Any future rollable holiday added to this calendar must also be handled there.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 * @see <a href="https://www.bankofengland.co.uk/payment-and-settlement/chaps/chaps-settlement-calendar">Bank of England CHAPS Settlement Calendar</a>
 */
public class HolidayCalendarServiceGBP extends AbstractHolidayCalendarService {

    private static final String CODE = "GBP";
    private static final String NAME = "United Kingdom (CHAPS) Holidays";

    public HolidayCalendarServiceGBP() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final EasterObservance easter = new WesternEaster();

        final Holiday newYearsDay = Holiday.builder()
                .name("New Year's Day")
                .description("First day of new year in the Common Era (CE)")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.JANUARY, 1)
                .build();
        final Holiday goodFriday = Holiday.builder()
                .name("Good Friday")
                .description("Commemoration of the crucifixion of Jesus Christ")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new GoodFriday(easter))
                .build();
        final Holiday easterMonday = Holiday.builder()
                .name("Easter Monday")
                .description("Day after Easter Sunday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EasterMonday(easter))
                .build();
        final Holiday earlyMayBankHoliday = Holiday.builder()
                .name("Early May Bank Holiday")
                .description("Early May bank holiday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EarlyMayBankHoliday())
                .build();
        final Holiday springBankHoliday = Holiday.builder()
                .name("Spring Bank Holiday")
                .description("Late May bank holiday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new SpringBankHoliday())
                .build();
        final Holiday summerBankHoliday = Holiday.builder()
                .name("Summer Bank Holiday")
                .description("Summer bank holiday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new SummerBankHoliday())
                .build();
        final Holiday christmasDay = Holiday.builder()
                .name("Christmas Day")
                .description("Commemoration of the birth of Jesus Christ")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.DECEMBER, 25)
                .build();
        final Holiday boxingDay = Holiday.builder()
                .name("Boxing Day")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.DECEMBER, 26)
                .build();
        final Holiday queenFuneral = Holiday.builder()
                .name("State Funeral of Queen Elizabeth II")
                .description("Proclaimed bank holiday for the State Funeral of Queen Elizabeth II")
                .type(Holiday.Type.SPECIAL_ANNIVERSARY)
                .rollable(false)
                .anniversaryDate(LocalDate.of(2022, Month.SEPTEMBER, 19))
                .build();
        final Holiday coronationBankHoliday = Holiday.builder()
                .name("Coronation Bank Holiday")
                .description("Proclaimed bank holiday for the Coronation of King Charles III")
                .type(Holiday.Type.SPECIAL_ANNIVERSARY)
                .rollable(false)
                .anniversaryDate(LocalDate.of(2023, Month.MAY, 8))
                .build();

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(UKDateRolls.fixedHolidayRoll(newYearsDay, christmasDay, boxingDay))
                .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                .holiday(newYearsDay)
                .holiday(goodFriday)
                .holiday(easterMonday)
                .holiday(earlyMayBankHoliday)
                .holiday(springBankHoliday)
                .holiday(summerBankHoliday)
                .holiday(christmasDay)
                .holiday(boxingDay)
                .holiday(queenFuneral)
                .holiday(coronationBankHoliday)
                .build();
    }

}