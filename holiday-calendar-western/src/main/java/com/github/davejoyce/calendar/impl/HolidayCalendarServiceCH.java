package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.Holiday;
import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.HolidayCalendarService;

import java.time.Month;

import static com.github.davejoyce.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of Switzerland national holiday calendar.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceCH implements HolidayCalendarService {

    private static final String CODE = "CH";
    private static final String NAME = "Switzerland National Holidays";

    @Override
    public boolean isProvided(String code) {
        return CODE.equalsIgnoreCase(code);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final Holiday swissNationalDay = Holiday.builder()
                                                .name("Swiss National Day")
                                                .description("Date of the Federal Charter of 1291")
                                                .type(Holiday.Type.FIXED)
                                                .rollable(false)
                                                .monthDay(Month.AUGUST, 1)
                                                .build();
        return HolidayCalendar.builder()
                              .code(CODE)
                              .name(NAME)
                              .weekendDays(STANDARD_WEEKEND)
                              .holiday(swissNationalDay)
                              .build();
    }

}
