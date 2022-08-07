package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.Holiday;
import com.github.davejoyce.calendar.HolidayCalendar;
import com.github.davejoyce.calendar.HolidayCalendarFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.function.Predicate;

import static org.testng.Assert.*;
import static org.testng.Assert.assertTrue;

public abstract class AbstractHolidayCalendarServiceTest {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    final String calendarCode;

    AbstractHolidayCalendarServiceTest(String calendarCode) {
        this.calendarCode = calendarCode;
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        final HolidayCalendarFactory factory = new HolidayCalendarFactory();
        final HolidayCalendar calendar = factory.create(calendarCode);
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), calendarCode);
        logger.debug("Factory created HolidayCalendar: {}", calendarCode);
    }

    @Test(dataProvider = "expectedHolidayNames",
          dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testGetHolidayCalendar(String expectedHolidayName) {
        final Predicate<Holiday> expectedHoliday = (holiday -> expectedHolidayName.equals(holiday.getName()));

        final HolidayCalendarFactory factory = new HolidayCalendarFactory();
        final HolidayCalendar calendar = factory.create(calendarCode);
        assertTrue(calendar.getHolidays().stream().anyMatch(expectedHoliday));
        logger.debug("HolidayCalendar contains expected Holiday: {}", expectedHolidayName);
    }

    @DataProvider
    abstract Iterator<Object[]> expectedHolidayNames();

}
