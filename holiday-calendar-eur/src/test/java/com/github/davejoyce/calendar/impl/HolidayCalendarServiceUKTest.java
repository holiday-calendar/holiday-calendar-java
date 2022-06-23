package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.HolidayCalendarFactory;
import com.github.davejoyce.calendar.HolidayCalendarService;
import com.github.davejoyce.calendar.HolidayDate;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

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

    @Test
    public void testDateRoll_BoxingDayCase() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        HolidayCalendar calendar = factory.create(CODE);
        assertNotNull(calendar);

        List<HolidayDate> ukHolidays2021 = calendar.calculate(2021);
        Optional<HolidayDate> foundBoxingDay = ukHolidays2021.stream()
                                                             .filter(hd -> "Boxing Day".equals(hd.getHoliday().getName()))
                                                             .findFirst();
        assertTrue(foundBoxingDay.isPresent());
        assertEquals(foundBoxingDay.get().getDate(), LocalDate.of(2021, Month.DECEMBER, 28));
    }

    @Test
    public void testDateRoll_DefaultCase() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        HolidayCalendar calendar = factory.create(CODE);
        assertNotNull(calendar);

        List<HolidayDate> ukHolidays2021 = calendar.calculate(2020);
        Optional<HolidayDate> foundNewYearsDay = ukHolidays2021.stream()
                                                               .filter(hd -> "New Year's Day".equals(hd.getHoliday().getName()))
                                                               .findFirst();
        assertTrue(foundNewYearsDay.isPresent());
        assertEquals(foundNewYearsDay.get().getDate(), LocalDate.of(2020, Month.JANUARY, 1));
    }

}