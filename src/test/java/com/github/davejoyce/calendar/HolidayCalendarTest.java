package com.github.davejoyce.calendar;

import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;
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

    @Test
    public void testCalculate() {
        HolidayCalendar calendar = HolidayCalendar.builder()
                .code("SIFMA")
                .name("SIFMA Holiday Calendar")
                .dateRoll(dateToRoll -> {
                    if (DayOfWeek.SATURDAY.equals(dateToRoll.getDayOfWeek())) return dateToRoll.minusDays(1);
                    if (DayOfWeek.SUNDAY.equals(dateToRoll.getDayOfWeek())) return dateToRoll.plusDays(1);
                    return dateToRoll;
                })
                .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                .holiday(new FixedHoliday("New Year's Day", "", Month.JANUARY, 1))
                .holiday(new FixedHoliday("Christmas Day", "", Month.DECEMBER, 25))
                .build();
        List<HolidayDate> dates = calendar.calculate(2021);

        assertEquals(dates.get(0).getHoliday().getName(), "New Year's Day");
        assertEquals(dates.get(0).getDate(), LocalDate.of(2021, Month.JANUARY, 1));
        assertEquals(dates.get(1).getHoliday().getName(), "Christmas Day");
        assertEquals(dates.get(1).getDate(), LocalDate.of(2021, Month.DECEMBER, 24)); // rolled back 1 day
    }

}