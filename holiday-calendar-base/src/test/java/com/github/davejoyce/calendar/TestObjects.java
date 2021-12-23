package com.github.davejoyce.calendar;

import com.github.davejoyce.calendar.function.Observance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;

public final class TestObjects {

    private TestObjects() {}

    public static DateRoll createDateRollUS() {
        return dateToRoll -> {
            if (DayOfWeek.SATURDAY.equals(dateToRoll.getDayOfWeek())) return dateToRoll.minusDays(1);
            if (DayOfWeek.SUNDAY.equals(dateToRoll.getDayOfWeek())) return dateToRoll.plusDays(1);
            return dateToRoll;
        };
    }

    public static Observance createObservanceMlkDay() {
        return year -> Year.of(year)
                .atMonth(Month.JANUARY)
                .atDay(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY));
    }

    public static Observance createObservancePresidentsDay() {
        return year -> Year.of(year)
                .atMonth(Month.FEBRUARY)
                .atDay(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY));
    }

    public static Observance createObservanceMemorialDay() {
        return year -> (1970 < year) ? Year.of(year)
                                           .atMonth(Month.MAY)
                                           .atDay(1)
                                           .with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY))
                                     : LocalDate.of(year, Month.MAY, 30);
    }

    public static Observance createObservanceLaborDay() {
        return year -> Year.of(year)
                .atMonth(Month.SEPTEMBER)
                .atDay(1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    public static Observance createObservanceColumbusDay() {
        return year -> Year.of(year)
                .atMonth(Month.OCTOBER)
                .atDay(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.MONDAY));
    }

    public static Observance createObservanceThanksgiving() {
        return year -> Year.of(year)
                .atMonth(Month.NOVEMBER)
                .atDay(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY));
    }

}
