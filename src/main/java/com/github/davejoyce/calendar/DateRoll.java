package com.github.davejoyce.calendar;

import java.time.Instant;
import java.time.LocalDate;

public interface DateRoll {

    LocalDate rollToObservedDate(Holiday holiday, int year);

}
