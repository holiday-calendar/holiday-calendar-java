package com.github.davejoyce.calendar;

import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.Set;
import java.util.TimeZone;

import static org.testng.Assert.*;

public class HolidayCalendarTest {

    private static final ZoneId ZONE_ID_NEW_YORK = ZoneId.of("America/New_York");
    private static final ZoneId ZONE_ID_TOKYO = ZoneId.of("Asia/Tokyo");
    private static final TimeZone TZ_NEW_YORK = TimeZone.getTimeZone(ZONE_ID_NEW_YORK);

    @Test
    public void testBuilderWithNoHolidays() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        assertNotNull(holidayCalendar.getHolidays());
        assertTrue(holidayCalendar.getHolidays().isEmpty());
    }

    @Test
    public void testBuilderWithNoWeekendDays() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        assertEquals(holidayCalendar.getWeekendDays(), HolidayCalendar.STANDARD_WEEKEND);
    }

    @Test
    public void testGetCode() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        assertEquals(holidayCalendar.getCode(), "FRB");
    }

    @Test
    public void testGetName() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        assertEquals(holidayCalendar.getName(), "Federal Reserve Board");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testGetHolidays_Unmodifiable() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        Set<Holiday> view = holidayCalendar.getHolidays();
        assertNotNull(view, "Expected non-null holidays");

        view.add(null);
        fail("Holidays set view should be unmodifiable!");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testGetWeekendDays_Unmodifiable() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        Set<DayOfWeek> view = holidayCalendar.getWeekendDays();
        assertNotNull(view, "Expected non-null weekendDays");

        view.add(null);
        fail("WeekendDays set view should be unmodifiable!");
    }

    @Test
    public void testToString() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        String actual = holidayCalendar.toString();
        assertEquals(actual, "HolidayCalendar[code='FRB', name='Federal Reserve Board']");
    }

}