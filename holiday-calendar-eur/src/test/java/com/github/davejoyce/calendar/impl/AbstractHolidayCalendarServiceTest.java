package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

import static org.testng.Assert.*;
import static org.testng.Assert.assertTrue;

public abstract class AbstractHolidayCalendarServiceTest {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    final String calendarCode;

    HolidayCalendarFactory factory;

    AbstractHolidayCalendarServiceTest(String calendarCode) {
        this.calendarCode = calendarCode;
    }

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        final HolidayCalendar calendar = factory.create(calendarCode);
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), calendarCode);
        logger.debug("Factory created HolidayCalendar: {}", calendarCode);
    }

    @Test(dataProvider = "expectedHolidayNames",
          dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testGetHolidayCalendar(String expectedHolidayName) {
        final Predicate<Holiday> expectedHoliday = (holiday -> expectedHolidayName.equals(holiday.getName()));
        final HolidayCalendar calendar = factory.create(calendarCode);
        assertTrue(calendar.getHolidays().stream().anyMatch(expectedHoliday));
        logger.debug("HolidayCalendar contains expected Holiday: {}", expectedHolidayName);
    }

    @Test(dataProvider = "expectedHolidayOccurrences")
    public void testDateRoll(int year, String holidayName, LocalDate expectedHolidayOccurrence) {
        final HolidayCalendarService calendarService = factory.getService(calendarCode);
        final HolidayCalendar calendar = calendarService.getHolidayCalendar();

        final Optional<HolidayDate> holidayDate = calendar.calculate(year)
                .stream()
                .filter(hd -> holidayName.equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(holidayDate.isPresent());
        assertEquals(holidayDate.get().getDate(), expectedHolidayOccurrence);
    }

    @DataProvider
    abstract Iterator<Object[]> expectedHolidayNames();

    @DataProvider
    abstract Iterator<Object[]> expectedHolidayOccurrences();

}
