package com.github.davejoyce.calendar.impl;

import org.testng.annotations.DataProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class HolidayCalendarServiceUSTest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "US";

    public HolidayCalendarServiceUSTest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        final Object[] presidentsDay = {"Presidents' Day"};
        final Object[] juneteenth = {"Juneteenth"};
        return Arrays.asList(presidentsDay, juneteenth).listIterator();
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayOccurrences() {
        return Collections.emptyIterator();
    }

}