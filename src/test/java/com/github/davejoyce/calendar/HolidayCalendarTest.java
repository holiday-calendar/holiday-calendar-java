package com.github.davejoyce.calendar;

import org.testng.annotations.Test;

import java.time.ZoneId;
import java.util.Map;
import java.util.TimeZone;

import static org.testng.Assert.*;

public class HolidayCalendarTest {

    private static final ZoneId ZONE_ID_NEW_YORK = ZoneId.of("America/New_York");
    private static final ZoneId ZONE_ID_TOKYO = ZoneId.of("Asia/Tokyo");
    private static final TimeZone TZ_NEW_YORK = TimeZone.getTimeZone(ZONE_ID_NEW_YORK);

    @Test(expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "Argument 'holidays'.*")
    public void testFullConstructorWithNullHolidays() {
        new HolidayCalendar("FRB", "Federal Reserve Board", HolidayCalendar.STANDARD_WEEKEND, null);
    }

    @Test
    public void testBuilderWithNoHolidays() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                                                         .build();
        assertNotNull(holidayCalendar.getHolidays());
    }

    @Test
    public void testGetCode() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                                                         .build();
        assertEquals(holidayCalendar.getCode(), "FRB");
    }

    @Test
    public void testGetName() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                                                         .build();
        assertEquals(holidayCalendar.getName(), "Federal Reserve Board");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testGetHolidays_Unmodifiable() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                                                         .build();
        Map<String, Holiday> view = holidayCalendar.getHolidays();
        assertNotNull(view, "Expected non-null holidays");

        view.put("TEST", null);
        fail("Holidays map view should be unmodifiable!");
    }

    @Test
    public void testToString() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                                                         .build();
        String actual = holidayCalendar.toString();
        assertEquals(actual, "HolidayCalendar[code='FRB', name='Federal Reserve Board']");
    }

}