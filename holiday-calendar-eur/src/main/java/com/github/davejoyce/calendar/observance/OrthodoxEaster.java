/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2021 David Joyce
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

package com.github.davejoyce.calendar.observance;

import com.github.davejoyce.calendar.function.EasterObservance;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Observance of <em>Orthodox</em> Easter as recognized by the Orthodox church.
 * This class implements the algorithm for Easter calculation as published by
 * Carl Friedrich Gauss.
 * <p>Due to the evolution of the computation of Easter Sunday during the
 * early-to-medieval period of the Christian church, this implementation of the
 * Gauss algorithm is only valid for years 530 - 3399 AD.</p>
 */
public class OrthodoxEaster implements EasterObservance {

    public static final int MIN_VALID_YEAR = 530;
    public static final int MAX_VALID_YEAR = 3399;

    public static final int[][] YEAR_RANGE_ADJUSTMENT_MATRIX = {
            {1583, 1699, 10},
            {1700, 1799, 11},
            {1700, 1799, 11},
            {1800, 1899, 12},
            {1900, 2099, 13},
            {2100, 2199, 14},
            {2200, 2299, 15},
            {2300, 2499, 16},
            {2500, 2599, 17},
            {2600, 2699, 18},
            {2700, 2899, 19},
            {2900, 2999, 20},
            {3000, 3099, 21},
            {3100, 3299, 22},
            {3300, 3399, 23},
    };
    private static final Set<Integer> validYearRange = new HashSet<>();
    private static final Map<Set<Integer>, Integer> dayAdjustments = new HashMap<>();

    static {
        validYearRange.addAll(IntStream.rangeClosed(MIN_VALID_YEAR, MAX_VALID_YEAR).boxed().collect(Collectors.toSet()));
        for (int[] row : YEAR_RANGE_ADJUSTMENT_MATRIX) {
            dayAdjustments.put(IntStream.rangeClosed(row[0], row[1]).boxed().collect(Collectors.toSet()), row[2]);
        }
    }

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;

        final int Y = year;
        final float yearMod19 = Y % 19;
        final float yearMod4 = Y % 4;
        final float yearMod7 = Y % 7;

        final float d = 19 * yearMod19 + 16;
        final float e = d % 30;

        final float f = 2 * yearMod4 + 4 * yearMod7 + 6 * e;
        final float g = f % 7;

        final int h = (int)(e + g);

        int day = h + 21;
        int month = 3;
        if (31 < day) {
            day = day - 31;
            month = 4;
        }
        final LocalDate julianEaster = LocalDate.of(year, Month.of(month), day);
        final int daysToAdd = dayAdjustments.entrySet()
                                            .stream()
                                            .filter(entry -> entry.getKey().contains(year))
                                            .mapToInt(Map.Entry::getValue)
                                            .findFirst()
                                            .orElse(0);
        return julianEaster.plusDays(daysToAdd);
    }

    @Override
    public boolean test(Integer year) {
        return validYearRange.contains(year);
    }

}
