package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.Holiday;
import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.HolidayCalendarService;

import java.time.Month;

import static com.github.davejoyce.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of Example holiday calendar.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceEXAMPLE implements HolidayCalendarService {

    private static final String CODE = "EXAMPLE";
    private static final String NAME = "Example Holidays";

    @Override
    public boolean isProvided(String code) {
        return CODE.equalsIgnoreCase(code);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final Holiday newYearsDay = Holiday.builder()
                                           .name("New Year's Day")
                                           .description("First day of new year in the Common Era (CE)")
                                           .type(Holiday.Type.FIXED)
                                           .rollable(true)
                                           .monthDay(Month.JANUARY, 1)
                                           .build();
        final Holiday newYearsEve = Holiday.builder()
                                           .name("New Year's Eve")
                                           .description("Last day of the current year in the Common Era (CE)")
                                           .type(Holiday.Type.FIXED)
                                           .rollable(false)
                                           .monthDay(Month.DECEMBER, 31)
                                           .build();
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .weekendDays(STANDARD_WEEKEND)
                .holiday(newYearsDay)
                .holiday(newYearsEve)
                .build();
    }

}
