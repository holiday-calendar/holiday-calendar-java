package com.github.davejoyce.calendar.function;

import java.time.LocalDate;
import java.time.Month;

/**
 * Observance of <em>Western</em> Easter as recognized by Roman Catholic and
 * Protestant Christian denominations. This class implements the algorithm for
 * Easter calculation as developed by Carl Friedrich Gauss.
 */
public class WesternEaster implements EasterObservance {

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        final int y = year;
        final float a = y % 19;
        final float b = y % 4;
        final float c = y % 7;
        final float p = (float)Math.floor((double) y / 100);
        final float q = (float)Math.floor((double) (13 + 8 * p) / 25);
        final float m = (15 - q + p - p / 4) % 30;
        final float n = (4 + p - p / 4) % 7;
        final float d = (19 * a + m) % 30;
        final float e = (2 * b + 4 * c + 6 * d + n) % 7;
        int days = (int)(22 + d + e);

        // Corner cases
        if (28 == d && 6 == e) return LocalDate.of(y, Month.APRIL, 18);
        if (29 == d && 6 == e) return LocalDate.of(y, Month.APRIL, 19);

        return (31 < days) ? LocalDate.of(y, Month.APRIL, (days - 31))
                           : LocalDate.of(y, Month.MARCH, days);
    }

}
