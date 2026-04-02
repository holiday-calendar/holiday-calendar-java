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
import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.HolidayDate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A {@link HolidayCalendar} for Japanese holiday schedules that applies the
 * sandwiched-day rule (国民の休日): a non-holiday weekday falling between two
 * consecutive national holidays also becomes a holiday.
 *
 * <p>After the base holiday list is computed by the superclass, this class scans
 * consecutive pairs of holiday dates. Where exactly one weekday falls between
 * them, a synthetic "National Holiday" is injected into the result.
 */
class JapaneseHolidayCalendar extends HolidayCalendar {

    /**
     * Constructs a {@code JapaneseHolidayCalendar} by copying all state from
     * a {@link HolidayCalendar} built by the standard builder.
     */
    JapaneseHolidayCalendar(HolidayCalendar base) {
        super(base.getCode(), base.getName(), base.getDateRoll(),
              base.getWeekendDays(), base.getHolidays());
    }

    /**
     * Calculates holidays for the given year, then injects any sandwiched days
     * (国民の休日) that arise between consecutive holidays.
     */
    @Override
    public List<HolidayDate> calculate(int year) {
        List<HolidayDate> base = super.calculate(year);
        List<HolidayDate> result = new ArrayList<>(base);

        for (int i = 0; i < base.size() - 1; i++) {
            LocalDate d1 = base.get(i).date();
            LocalDate d2 = base.get(i + 1).date();
            if (ChronoUnit.DAYS.between(d1, d2) == 2) {
                LocalDate middle = d1.plusDays(1);
                DayOfWeek dow = middle.getDayOfWeek();
                if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                    Holiday sandwiched = Holiday.builder()
                            .name("National Holiday")
                            .description("国民の休日 — weekday sandwiched between two national holidays")
                            .type(Holiday.Type.FLOATING)
                            .rollable(false)
                            .observance(y -> y == middle.getYear() ? middle : null)
                            .build();
                    result.add(new HolidayDate(sandwiched, middle));
                }
            }
        }

        return result.stream()
                     .sorted(Comparator.comparing(HolidayDate::date))
                     .toList();
    }
}
