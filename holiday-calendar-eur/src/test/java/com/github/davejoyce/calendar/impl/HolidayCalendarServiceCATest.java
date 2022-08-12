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
        final Object[] newYearsDay12 = {2012, "New Year's Day", LocalDate.of(2012, Month.JANUARY, 2)};
        final Object[] canadaDay12 = {2012, "Canada Day", LocalDate.of(2012, Month.JULY, 2)};
        final Object[] remembranceDay12 = {2012, "Remembrance Day", LocalDate.of(2012, Month.NOVEMBER, 12)};
        final Object[] newYearsDay13 = {2013, "New Year's Day", LocalDate.of(2013, Month.JANUARY, 1)};
        final Object[] canadaDay13 = {2013, "Canada Day", LocalDate.of(2013, Month.JULY, 1)};
        final Object[] remembranceDay13 = {2013, "Remembrance Day", LocalDate.of(2013, Month.NOVEMBER, 11)};
        final Object[] christmas20 = {2020, "Christmas Day", LocalDate.of(2020, Month.DECEMBER, 25)};
        final Object[] christmas21 = {2021, "Christmas Day", LocalDate.of(2021, Month.DECEMBER, 25)};
        return Arrays.asList(newYearsDay12, canadaDay12, remembranceDay12,
                             newYearsDay13, canadaDay13, remembranceDay13,
                             christmas20, christmas21).listIterator();
    }

}