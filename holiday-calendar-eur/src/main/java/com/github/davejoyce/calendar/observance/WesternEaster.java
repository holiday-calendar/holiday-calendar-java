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

/**
 * Observance of <em>Western</em> Easter as recognized by Roman Catholic and
 * Protestant Christian denominations. This class implements the algorithm for
 * Easter calculation as published by Butcher / Jones / Meeus.
 * <p>While all Easter dates calculated by this class are Gregorian dates, it
 * does not support a proleptic Gregorian calendar. Dates for years prior to
 * 1583 CE will be calculated the same as Orthodox Easter and simply returned as
 * the Gregorian date.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class WesternEaster implements EasterObservance {

    private static final OrthodoxEaster PRE_1583 = new OrthodoxEaster();

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        if (1583 > year) return PRE_1583.apply(year);
        final int a = year % 19;
        final int b = year / 100;
        final int c = year % 100;
        final int d = b / 4;
        final int e = b % 4;
        final int f = (b + 8) / 25;
        final int g = (b - f + 1) / 3;
        final int h = (19 * a + b - d - g + 15) % 30;
        final int i = c / 4;
        final int k = c % 4;
        final int l = (32 + 2 * e + 2 * i - h - k) % 7;
        final int m = (a + 11 * h + 22 * l) / 451;
        final int month = monthDividend(h, l, m) / 31;
        final int p = monthDividend(h, l, m) % 31;
        final int day = p + 1;
        return LocalDate.of(year, Month.of(month), day);
    }

    @Override
    public boolean test(Integer year) {
        return OrthodoxEaster.MIN_VALID_YEAR <= year;
    }

    private int monthDividend(int h, int l, int m) {
        return (h + l - 7 * m + 114);
    }

}
