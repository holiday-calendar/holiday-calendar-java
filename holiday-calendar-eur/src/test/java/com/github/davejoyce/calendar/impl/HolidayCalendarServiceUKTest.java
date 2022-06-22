package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.HolidayCalendarFactory;
import com.github.davejoyce.calendar.HolidayCalendarService;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class HolidayCalendarServiceUKTest {

    static final String CODE = "UK";

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        HolidayCalendar calendar = factory.create(CODE);
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), CODE);
    }

    @Test
    public void testGetHolidayCalendar() {
        HolidayCalendarService calendarService = new HolidayCalendarServiceUK();
        assertTrue(calendarService.isProvided(CODE));

        HolidayCalendar ukCalendar = calendarService.getHolidayCalendar();
        assertTrue(ukCalendar.getHolidays().stream().anyMatch(holiday -> "Boxing Day".equals(holiday.getName())));
    }

}