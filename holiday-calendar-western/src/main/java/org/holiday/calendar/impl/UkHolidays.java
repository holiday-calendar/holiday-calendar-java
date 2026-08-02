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

import org.holiday.calendar.Holiday;
import org.holiday.calendar.observance.christian.EasterMonday;
import org.holiday.calendar.observance.christian.EasterObservance;
import org.holiday.calendar.observance.christian.GoodFriday;
import org.holiday.calendar.observance.christian.WesternEaster;
import org.holiday.calendar.observance.uk.EarlyMayBankHoliday;
import org.holiday.calendar.observance.uk.SpringBankHoliday;
import org.holiday.calendar.observance.uk.SummerBankHoliday;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

/**
 * Package-private factory building the 12 holidays common to both the United
 * Kingdom national ({@code UK}) and London Stock Exchange ({@code XLON})
 * calendars: New Year's Day, Good Friday, Easter Monday, Early May Bank
 * Holiday, Spring Bank Holiday, the 4 Jubilee anniversaries, Summer Bank
 * Holiday, Christmas Day, and Boxing Day. LSE is closed on all of these,
 * including the one-off Jubilee bank holidays, so the full list is shared
 * verbatim — only {@code XLON} adds the Christmas Eve/New Year's Eve early
 * closes on top.
 *
 * <p>{@link #newYearsDay()}, {@link #christmasDay()}, and {@link #boxingDay()}
 * are exposed individually because {@code UKDateRolls.fixedHolidayRoll}
 * requires direct references to these three holidays to compute its
 * substitution logic.
 */
class UkHolidays {

    private UkHolidays() {}

    static Holiday newYearsDay() {
        return Holiday.builder()
                .name("New Year's Day")
                .description("First day of new year in the Common Era (CE)")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.JANUARY, 1)
                .build();
    }

    static Holiday christmasDay() {
        return Holiday.builder()
                .name("Christmas Day")
                .description("Commemoration of the birth of Jesus Christ")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.DECEMBER, 25)
                .build();
    }

    static Holiday boxingDay() {
        return Holiday.builder()
                .name("Boxing Day")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.DECEMBER, 26)
                .build();
    }

    static List<Holiday> baseHolidays() {
        final EasterObservance easter = new WesternEaster();

        return List.of(
            newYearsDay(),
            Holiday.builder()
                    .name("Good Friday")
                    .description("Commemoration of the crucifixion of Jesus Christ")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new GoodFriday(easter))
                    .build(),
            Holiday.builder()
                    .name("Easter Monday")
                    .description("Day after Easter Sunday")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EasterMonday(easter))
                    .build(),
            Holiday.builder()
                    .name("Early May Bank Holiday")
                    .description("Early May bank holiday")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EarlyMayBankHoliday())
                    .build(),
            Holiday.builder()
                    .name("Spring Bank Holiday")
                    .description("Late May bank holiday")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new SpringBankHoliday())
                    .build(),
            Holiday.builder()
                    .name("Silver Jubilee Bank Holiday")
                    .description("Silver Jubilee of Queen Elizabeth II")
                    .type(Holiday.Type.SPECIAL_ANNIVERSARY)
                    .rollable(false)
                    .anniversaryDate(LocalDate.of(1977, Month.JUNE, 7))
                    .build(),
            Holiday.builder()
                    .name("Golden Jubilee Bank Holiday")
                    .description("Golden Jubilee of Queen Elizabeth II")
                    .type(Holiday.Type.SPECIAL_ANNIVERSARY)
                    .rollable(false)
                    .anniversaryDate(LocalDate.of(2002, Month.JUNE, 3))
                    .build(),
            Holiday.builder()
                    .name("Diamond Jubilee Bank Holiday")
                    .description("Diamond Jubilee of Queen Elizabeth II")
                    .type(Holiday.Type.SPECIAL_ANNIVERSARY)
                    .rollable(false)
                    .anniversaryDate(LocalDate.of(2012, Month.JUNE, 5))
                    .build(),
            Holiday.builder()
                    .name("Platinum Jubilee Bank Holiday")
                    .description("Platinum Jubilee of Queen Elizabeth II")
                    .type(Holiday.Type.SPECIAL_ANNIVERSARY)
                    .rollable(false)
                    .anniversaryDate(LocalDate.of(2022, Month.JUNE, 3))
                    .build(),
            Holiday.builder()
                    .name("Summer Bank Holiday")
                    .description("Summer bank holiday")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new SummerBankHoliday())
                    .build(),
            christmasDay(),
            boxingDay()
        );
    }

}