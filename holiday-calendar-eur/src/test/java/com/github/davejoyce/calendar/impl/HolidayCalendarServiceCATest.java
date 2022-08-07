package com.github.davejoyce.calendar.impl;

import com.github.davejoyce.calendar.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

import static org.testng.Assert.*;

public class HolidayCalendarServiceCATest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "CA";

    public HolidayCalendarServiceCATest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        final Object[] familyDay = {"Family Day"};
        final Object[] victoriaDay = {"Victoria Day"};
        return Arrays.asList(familyDay, victoriaDay).listIterator();
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayOccurrences() {
        final Object[] christmas20 = {2020, "Christmas Day", LocalDate.of(2020, Month.DECEMBER, 25)};
        final Object[] christmas21 = {2021, "Christmas Day", LocalDate.of(2021, Month.DECEMBER, 25)};
        return Arrays.asList(christmas20, christmas21).listIterator();
    }

}