package com.github.davejoyce.calendar;

import java.time.LocalDate;

/**
 * Defines date adjustment behavior for holiday observance when the calculated
 * date falls on a weekend day. Date roll behavior is a defined attribute of a
 * published holiday calendar.
 */
public interface DateRoll {

    /**
     * Roll the calculated date for the specified holiday in the given year to
     * the nearest valid date.
     * @param dateToRoll calculated holiday date to be rolled
     * @return holiday date (adjusted for valid observance)
     */
    LocalDate rollToObservedDate(LocalDate dateToRoll);

}
