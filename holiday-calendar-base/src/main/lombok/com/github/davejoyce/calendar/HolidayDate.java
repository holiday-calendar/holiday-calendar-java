package com.github.davejoyce.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

/**
 * The observed date of a {@link Holiday} in a particular year of the Common
 * Era (CE). Instances of this class are immutable and thread safe.
 *
 * @see HolidayCalendar#calculate(int)
 */
@Data
@AllArgsConstructor
public class HolidayDate {

    @NonNull
    private final Holiday holiday;

    @NonNull
    private final LocalDate date;

}
