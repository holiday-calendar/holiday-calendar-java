package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.*;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

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

    @Test(dependsOnMethods = "testGetHolidayCalendar")
    public void testBoxingDayRoll() {
        final HolidayCalendarService calendarService = new HolidayCalendarServiceCA();
        final HolidayCalendar caCalendar = calendarService.getHolidayCalendar();

        final Optional<HolidayDate> boxingDay21 = caCalendar.calculate(2021)
                                                            .stream()
                                                            .filter(hd -> "Boxing Day".equals(hd.getHoliday().getName()))
                                                            .findFirst();
        assertTrue(boxingDay21.isPresent());
        assertEquals(boxingDay21.get().getDate(), LocalDate.of(2021, Month.DECEMBER, 28));
        assertEquals(boxingDay21.get().getDate().getDayOfWeek(), DayOfWeek.TUESDAY);

        final Optional<HolidayDate> boxingDay22 = caCalendar.calculate(2022)
                                                            .stream()
                                                            .filter(hd -> "Boxing Day".equals(hd.getHoliday().getName()))
                                                            .findFirst();
        assertTrue(boxingDay22.isPresent());
        assertEquals(boxingDay22.get().getDate(), LocalDate.of(2022, Month.DECEMBER, 26));
    }

}