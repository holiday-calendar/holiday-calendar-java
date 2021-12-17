package com.github.davejoyce.calendar;

import com.github.davejoyce.calendar.holiday.GoodFriday;
import com.github.davejoyce.calendar.holiday.WesternEaster;
import org.testng.annotations.Test;

import java.time.Month;

import static org.testng.Assert.*;

public class HolidayBuilderTest {

    @Test
    public void testBuilderInstantiation() {
        assertNotNull(Holiday.builder());
        assertNotEquals(Holiday.builder(), Holiday.builder());
    }

    @Test
    public void testBuilderFixedHoliday() {
        Holiday holiday = Holiday.builder()
                .type(Holiday.Type.FIXED)
                .name("New Year's Day")
                .description("First day of a new year")
                .rollable(true)
                .monthDay(Month.JANUARY, 1)
                .build();
        assertEquals(holiday.getClass(), FixedHoliday.class);
    }

    @Test
    public void testBuilderFloatingHoliday() {
        Holiday holiday = Holiday.builder()
                .type(Holiday.Type.FLOATING)
                .name("Good Friday")
                .description("Date of crucifixion of Jesus Christ")
                .rollable(false)
                .observance(new GoodFriday(new WesternEaster()))
                .build();
        assertEquals(holiday.getClass(), FloatingHoliday.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBuilder_NoType() {
        Holiday holiday = Holiday.builder()
                .type((Holiday.Type)null)
                .name("Good Friday")
                .description("Date of crucifixion of Jesus Christ")
                .rollable(false)
                .observance(new GoodFriday(new WesternEaster()))
                .build();
        fail("Expected IllegalArgumentException");
    }

}
