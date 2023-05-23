package com.github.davejoyce.calendar;

import org.testng.annotations.Test;

import java.util.NoSuchElementException;

import static org.testng.Assert.*;

public class HolidayCalendarFactoryTest {

    private static final String CODE = "EXAMPLE";
    private static final String INVALID_CODE = "BAD";

    @Test
    public void testCreate() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        HolidayCalendar calendar = factory.create(CODE);
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), CODE);
        assertEquals(calendar.getHolidays().size(), 2);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testCreateInvalidCodeThrowsException() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        factory.create(INVALID_CODE);
        fail("Expected NoSuchElementException to be thrown for code " + INVALID_CODE);
    }

}