package org.holiday.calendar;

import org.testng.annotations.Test;

import java.util.List;
import java.util.OptionalInt;

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

    @Test(expectedExceptions = HolidayCalendarNotFoundException.class)
    public void testCreateInvalidCodeThrowsException() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        factory.create(INVALID_CODE);
        fail("Expected HolidayCalendarNotFoundException to be thrown for code " + INVALID_CODE);
    }

    @Test
    public void testListAvailableCodes() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        List<String> codes = factory.listAvailableCodes();
        assertNotNull(codes);
        assertTrue(codes.contains(CODE));
    }

    @Test
    public void testDataValidThroughAlgorithmicCalendarReturnsEmpty() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        OptionalInt result = factory.dataValidThrough(CODE);
        assertFalse(result.isPresent(),
            "Fully algorithmic calendar should return OptionalInt.empty() from dataValidThrough()");
    }

    @Test(expectedExceptions = HolidayCalendarNotFoundException.class)
    public void testDataValidThroughInvalidCodeThrowsException() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        factory.dataValidThrough(INVALID_CODE);
        fail("Expected HolidayCalendarNotFoundException to be thrown for code " + INVALID_CODE);
    }

}
