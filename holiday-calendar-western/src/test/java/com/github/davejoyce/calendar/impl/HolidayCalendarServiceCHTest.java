package com.github.davejoyce.calendar.impl;

import org.testng.annotations.DataProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class HolidayCalendarServiceCHTest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "CH";

    public HolidayCalendarServiceCHTest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        final Object[] swissNationalDay = new Object[] { "Swiss National Day" };
        return Collections.singletonList(swissNationalDay).listIterator();
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayOccurrences() {
        final Object[] swissNationalDay18 = {2018, "Swiss National Day", LocalDate.of(2018, Month.AUGUST, 1)};
        final Object[] swissNationalDay19 = {2019, "Swiss National Day", LocalDate.of(2019, Month.AUGUST, 1)};
        final Object[] swissNationalDay20 = {2020, "Swiss National Day", LocalDate.of(2020, Month.AUGUST, 1)};
        final Object[] swissNationalDay21 = {2021, "Swiss National Day", LocalDate.of(2021, Month.AUGUST, 1)};
        final Object[] swissNationalDay22 = {2022, "Swiss National Day", LocalDate.of(2022, Month.AUGUST, 1)};
        final Object[] swissNationalDay23 = {2023, "Swiss National Day", LocalDate.of(2023, Month.AUGUST, 1)};
        return Arrays.asList(swissNationalDay18, swissNationalDay19, swissNationalDay20,
                             swissNationalDay21, swissNationalDay22, swissNationalDay23).listIterator();
    }

}