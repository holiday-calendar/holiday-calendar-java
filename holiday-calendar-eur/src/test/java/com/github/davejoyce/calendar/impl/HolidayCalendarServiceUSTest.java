package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.HolidayCalendarFactory;
import com.github.davejoyce.calendar.HolidayCalendarService;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class HolidayCalendarServiceUSTest {

    static final String CODE = "US";

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        HolidayCalendar calendar = factory.create(CODE);
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), CODE);
    }

    @Test
    public void testGetHolidayCalendar() {
        HolidayCalendarService calendarService = new HolidayCalendarServiceUS();
        assertTrue(calendarService.isProvided(CODE));

        HolidayCalendar usCalendar = calendarService.getHolidayCalendar();
        assertTrue(usCalendar.getHolidays().stream().anyMatch(holiday -> "Juneteenth".equals(holiday.getName())));
    }

}