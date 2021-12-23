package com.github.davejoyce.calendar;

import com.github.davejoyce.calendar.function.Observance;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;

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
    public void testBuilderFixedHoliday_StringMonthDay() {
        Holiday holiday = Holiday.builder()
                                 .type(Holiday.Type.FIXED)
                                 .name("New Year's Day")
                                 .description("First day of a new year")
                                 .rollable(true)
                                 .monthDay("--01-01")
                                 .build();
        assertEquals(holiday.getClass(), FixedHoliday.class);
    }

    @Test
    public void testBuilderFloatingHoliday() {
        Holiday holiday = Holiday.builder()
                                 .type(Holiday.Type.FLOATING)
                                 .name("Thanksgiving Day")
                                 .description("Day of giving thanks")
                                 .rollable(false)
                                 .observance(createObservanceThanksgiving())
                                 .build();
        assertEquals(holiday.getClass(), FloatingHoliday.class);
    }

    @Test
    public void testBuilderFloatingHoliday_StringType() {
        Holiday holiday = Holiday.builder()
                                 .type("floating")
                                 .name("Thanksgiving Day")
                                 .description("Day of giving thanks")
                                 .rollable(false)
                                 .observance(createObservanceThanksgiving())
                                 .build();
        assertEquals(holiday.getClass(), FloatingHoliday.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBuilder_NoType() {
        Holiday.builder()
               .type((Holiday.Type)null)
               .name("Thanksgiving Day")
               .description("Day of giving thanks")
               .rollable(false)
               .observance(createObservanceThanksgiving())
               .build();
        fail("Expected IllegalArgumentException");
    }

    private Observance createObservanceThanksgiving() {
        return year -> Year.of(year)
                .atMonth(Month.NOVEMBER)
                .atDay(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY));
    }

}
