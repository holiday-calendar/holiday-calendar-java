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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * Calculates holidays for the given year, applies the cascading 振替休日 rule
     * (2007 Holiday Act amendment, Article 3 §3), then injects any sandwiched days
     * (国民の休日) that arise between consecutive holidays.
     *
     * <p>Cascade rule: when a national holiday falls on Sunday and its naive Monday
     * substitute is already occupied by another holiday, the substitute advances
     * day-by-day to the next weekday that is not itself a holiday.  The scan runs
     * before sandwich detection so that cascaded dates are visible to the sandwich
     * detector.  Only Sunday→Monday rolls trigger a cascade; Saturday holidays are
     * unaffected (they stay on their natural date per issue #125).
     */
    @Override
    public List<HolidayDate> calculate(int year) {
        List<HolidayDate> base = super.calculate(year);
        List<HolidayDate> cascaded = applyCascade(base, year);

        List<HolidayDate> result = new ArrayList<>(cascaded);
        for (int i = 0; i < cascaded.size() - 1; i++) {
            LocalDate d1 = cascaded.get(i).date();
            LocalDate d2 = cascaded.get(i + 1).date();
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

    private List<HolidayDate> applyCascade(List<HolidayDate> base, int year) {
        List<HolidayDate> result = new ArrayList<>(base);
        Set<LocalDate> occupiedDates = new HashSet<>();
        for (HolidayDate hd : base) {
            occupiedDates.add(hd.date());
        }

        for (int i = 0; i < result.size(); i++) {
            LocalDate cascaded = findCascadeTarget(result, i, year, occupiedDates);
            if (cascaded != null) {
                result.set(i, new HolidayDate(result.get(i).holiday(), cascaded));
                occupiedDates.add(cascaded);
            }
        }

        return result.stream().sorted(Comparator.comparing(HolidayDate::date)).toList();
    }

    private LocalDate findCascadeTarget(List<HolidayDate> result, int i, int year,
                                        Set<LocalDate> occupiedDates) {
        HolidayDate hd = result.get(i);
        Holiday h = hd.holiday();

        if (!h.isRollable()) return null;

        LocalDate rawDate = h.dateForYear(year).orElse(null);
        if (rawDate == null || rawDate.getDayOfWeek() != DayOfWeek.SUNDAY) return null;

        LocalDate expectedMonday = rawDate.plusDays(1);
        if (!hd.date().equals(expectedMonday)) return null;

        // Scan the list rather than occupiedDates: the Set stores each date once, so
        // two holidays sharing the same date look identical in the Set — we need to
        // know whether a *different* holiday occupies expectedMonday.
        if (!hasOtherHolidayOn(result, i, expectedMonday)) return null;

        // expectedMonday stays in occupiedDates (the other holiday still occupies it).
        return firstFreeWeekday(expectedMonday, year, occupiedDates);
    }

    private boolean hasOtherHolidayOn(List<HolidayDate> result, int excludeIndex, LocalDate date) {
        for (int j = 0; j < result.size(); j++) {
            if (j != excludeIndex
                    && date.equals(result.get(j).date())
                    && result.get(j).holiday().isRollable()) {   // only rollable national holidays block cascade
                return true;
            }
        }
        return false;
    }

    private LocalDate firstFreeWeekday(LocalDate after, int year, Set<LocalDate> occupiedDates) {
        LocalDate candidate = after.plusDays(1);
        while (candidate.getYear() == year
                && (occupiedDates.contains(candidate)
                    || candidate.getDayOfWeek() == DayOfWeek.SATURDAY
                    || candidate.getDayOfWeek() == DayOfWeek.SUNDAY)) {
            candidate = candidate.plusDays(1);
        }
        return candidate.getYear() == year ? candidate : null;
    }
}
