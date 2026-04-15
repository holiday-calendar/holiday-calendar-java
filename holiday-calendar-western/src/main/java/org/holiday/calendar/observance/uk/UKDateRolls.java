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

package org.holiday.calendar.observance.uk;

import org.holiday.calendar.Holiday;
import org.holiday.calendar.function.DateRoll;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Factory providing {@link DateRoll} strategies for United Kingdom holiday calendars.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class UKDateRolls {

    private UKDateRolls() {}

    /**
     * Returns the standard UK date roll strategy for New Year's Day, Christmas Day,
     * and Boxing Day.
     *
     * <ul>
     *   <li>Christmas Day / Boxing Day on Saturday or Sunday: both substitute days
     *       are observed on the following Monday and Tuesday (+2 days).</li>
     *   <li>New Year's Day on Saturday: observed on the following Monday (+2 days).</li>
     *   <li>New Year's Day on Sunday: observed on the following Monday (+1 day).</li>
     * </ul>
     *
     * @param newYearsDay  the New Year's Day holiday
     * @param christmasDay the Christmas Day holiday
     * @param boxingDay    the Boxing Day holiday
     * @return date roll strategy for these holidays
     */
    public static DateRoll fixedHolidayRoll(Holiday newYearsDay, Holiday christmasDay, Holiday boxingDay) {
        return dateToRoll -> {
            final Optional<LocalDate> nydDate       = newYearsDay.dateForYear(dateToRoll.getYear());
            final Optional<LocalDate> christmasDate = christmasDay.dateForYear(dateToRoll.getYear());
            final Optional<LocalDate> boxingDayDate = boxingDay.dateForYear(dateToRoll.getYear());
            final boolean isNewYearsDay = nydDate.isPresent()       && dateToRoll.equals(nydDate.get());
            final boolean isChristmas   = christmasDate.isPresent() && dateToRoll.equals(christmasDate.get());
            final boolean isBoxingDay   = boxingDayDate.isPresent() && dateToRoll.equals(boxingDayDate.get());
            return switch (dateToRoll.getDayOfWeek()) {
                case SATURDAY -> (isChristmas || isBoxingDay || isNewYearsDay)
                        ? dateToRoll.plusDays(2L) : dateToRoll;
                case SUNDAY -> {
                    if (isChristmas || isBoxingDay) yield dateToRoll.plusDays(2L);
                    if (isNewYearsDay)              yield dateToRoll.plusDays(1L);
                    yield dateToRoll;
                }
                default -> dateToRoll;
            };
        };
    }

}
