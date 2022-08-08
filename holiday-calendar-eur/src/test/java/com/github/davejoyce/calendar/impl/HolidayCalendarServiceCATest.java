package com.github.davejoyce.calendar.impl;

import org.testng.annotations.DataProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;

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
        final Object[] canadaDay18 = {2018, "Canada Day", LocalDate.of(2018, Month.JULY, 2)};
        final Object[] canadaDay19 = {2019, "Canada Day", LocalDate.of(2019, Month.JULY, 1)};
        final Object[] canadaDay20 = {2020, "Canada Day", LocalDate.of(2020, Month.JULY, 1)};
        final Object[] canadaDay21 = {2021, "Canada Day", LocalDate.of(2021, Month.JULY, 1)};
        final Object[] canadaDay22 = {2022, "Canada Day", LocalDate.of(2022, Month.JULY, 1)};
        final Object[] canadaDay23 = {2023, "Canada Day", LocalDate.of(2023, Month.JULY, 1)};
        final Object[] remembranceDay18 = {2018, "Remembrance Day", LocalDate.of(2018, Month.NOVEMBER, 12)};
        final Object[] remembranceDay19 = {2019, "Remembrance Day", LocalDate.of(2019, Month.NOVEMBER, 11)};
        final Object[] remembranceDay20 = {2020, "Remembrance Day", LocalDate.of(2020, Month.NOVEMBER, 11)};
        final Object[] remembranceDay21 = {2021, "Remembrance Day", LocalDate.of(2021, Month.NOVEMBER, 11)};
        final Object[] remembranceDay22 = {2022, "Remembrance Day", LocalDate.of(2022, Month.NOVEMBER, 11)};
        final Object[] remembranceDay23 = {2023, "Remembrance Day", LocalDate.of(2023, Month.NOVEMBER, 11)};
        return Arrays.asList(canadaDay18, canadaDay19, canadaDay20, canadaDay21, canadaDay22, canadaDay23,
                remembranceDay18, remembranceDay19, remembranceDay20, remembranceDay21, remembranceDay22, remembranceDay23).listIterator();
    }

}