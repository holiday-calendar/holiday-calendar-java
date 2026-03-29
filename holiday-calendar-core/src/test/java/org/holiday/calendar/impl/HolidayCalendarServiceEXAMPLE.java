package org.holiday.calendar.impl;

import org.holiday.calendar.AbstractHolidayCalendarService;
import org.holiday.calendar.Holiday;
import org.holiday.calendar.HolidayCalendar;

import java.time.Month;

import static org.holiday.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of Example holiday calendar.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceEXAMPLE extends AbstractHolidayCalendarService {

    private static final String CODE = "EXAMPLE";
    private static final String NAME = "Example Holidays";

    public HolidayCalendarServiceEXAMPLE() {
        super(CODE, NAME);
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
