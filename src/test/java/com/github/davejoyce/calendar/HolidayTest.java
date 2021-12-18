package com.github.davejoyce.calendar;

import org.testng.annotations.Test;

import java.time.Month;

import static org.testng.Assert.*;

public class HolidayTest {

    @Test
    public void testGetName() {
        Holiday holiday = new FixedHoliday("New Year's Day", "", Month.JANUARY, 1);
        assertEquals(holiday.getName(), "New Year's Day");
    }

    @Test
    public void testGetDescription() {
        Holiday holiday = new FixedHoliday("New Year's Day", null, Month.JANUARY, 1);
        assertNotNull(holiday.getDescription());
        assertEquals(holiday.getDescription(), "");
    }

    @Test
    public void testIsRollable() {
        Holiday holiday = new FixedHoliday("New Year's Day", null, Month.JANUARY, 1);
        assertTrue(holiday.isRollable());
    }

}