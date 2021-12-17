package com.github.davejoyce.calendar.holiday;

import com.github.davejoyce.calendar.function.EasterObservance;

import java.time.LocalDate;
import java.time.Month;

/**
 * Observance of <em>Western</em> Easter as recognized by Roman Catholic and
 * Protestant Christian denominations. This class implements the algorithm for
 * Easter calculation as published by Butcher / Jones / Meeus.
 */
public class WesternEaster implements EasterObservance {

    private static final OrthodoxEaster PRE_1583 = new OrthodoxEaster();

    @Override
    public LocalDate apply(Integer year) {
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

    private int monthDividend(int h, int l, int m) {
        return (h + l - 7 * m + 114);
    }

}
