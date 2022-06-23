package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.HolidayCalendarFactory;
import com.github.davejoyce.calendar.HolidayCalendarService;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class HolidayCalendarServiceCATest {

    static final String CODE = "CA";

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        HolidayCalendar calendar = factory.create(CODE);
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), CODE);
    }

    @Test
    public void testGetHolidayCalendar() {
        HolidayCalendarService calendarService = new HolidayCalendarServiceCA();
        assertTrue(calendarService.isProvided(CODE));

        HolidayCalendar caCalendar = calendarService.getHolidayCalendar();
        assertTrue(caCalendar.getHolidays().stream().anyMatch(holiday -> "Victoria Day".equals(holiday.getName())));
    }

}