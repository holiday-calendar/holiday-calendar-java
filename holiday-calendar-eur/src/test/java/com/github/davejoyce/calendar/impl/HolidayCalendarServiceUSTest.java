package com.github.davejoyce.calendar.impl;

import org.testng.annotations.DataProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
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
        final Object[] veteransDay18 = {2018, "Veterans Day", LocalDate.of(2018, Month.NOVEMBER, 12)};
        final Object[] veteransDay19 = {2019, "Veterans Day", LocalDate.of(2019, Month.NOVEMBER, 11)};
        final Object[] veteransDay20 = {2020, "Veterans Day", LocalDate.of(2020, Month.NOVEMBER, 11)};
        final Object[] veteransDay21 = {2021, "Veterans Day", LocalDate.of(2021, Month.NOVEMBER, 11)};
        final Object[] veteransDay22 = {2022, "Veterans Day", LocalDate.of(2022, Month.NOVEMBER, 11)};
        final Object[] veteransDay23 = {2023, "Veterans Day", LocalDate.of(2023, Month.NOVEMBER, 10)};
        final Object[] christmas18 = {2018, "Christmas Day", LocalDate.of(2018, Month.DECEMBER, 25)};
        final Object[] christmas19 = {2019, "Christmas Day", LocalDate.of(2019, Month.DECEMBER, 25)};
        final Object[] christmas20 = {2020, "Christmas Day", LocalDate.of(2020, Month.DECEMBER, 25)};
        final Object[] christmas21 = {2021, "Christmas Day", LocalDate.of(2021, Month.DECEMBER, 24)};
        final Object[] christmas22 = {2022, "Christmas Day", LocalDate.of(2022, Month.DECEMBER, 26)};
        final Object[] christmas23 = {2023, "Christmas Day", LocalDate.of(2023, Month.DECEMBER, 25)};
        return Arrays.asList(veteransDay18, veteransDay19, veteransDay20, veteransDay21, veteransDay22, veteransDay23,
                             christmas18, christmas19, christmas20, christmas21, christmas22, christmas23).listIterator();
    }

}