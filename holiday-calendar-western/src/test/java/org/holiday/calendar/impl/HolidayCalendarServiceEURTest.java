package org.holiday.calendar.impl;

import org.holiday.calendar.HolidayCalendar;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;

import static org.testng.Assert.assertEquals;

public class HolidayCalendarServiceEURTest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "EUR";

    public HolidayCalendarServiceEURTest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        // Good Friday: absent from FR (Euronext Paris) — proves EUR ≠ FR
        // Boxing Day: absent from FR — confirms both TARGET2-specific holidays are present
        final Object[] goodFriday = {"Good Friday"};
        final Object[] boxingDay  = {"Boxing Day"};
        return Arrays.asList(goodFriday, boxingDay).listIterator();
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayOccurrences() {
        // Good Friday and Easter Monday always land on weekdays — noRoll() is irrelevant for these
        // 2022: Easter Sunday Apr 17 -> Good Friday Apr 15, Easter Monday Apr 18
        final Object[] goodFriday22   = {2022, "Good Friday",   LocalDate.of(2022, Month.APRIL,    15)};
        final Object[] easterMonday22 = {2022, "Easter Monday", LocalDate.of(2022, Month.APRIL,    18)};
        // 2024: Easter Sunday Mar 31 -> Good Friday Mar 29, Easter Monday Apr 1
        final Object[] goodFriday24   = {2024, "Good Friday",   LocalDate.of(2024, Month.MARCH,    29)};
        final Object[] easterMonday24 = {2024, "Easter Monday", LocalDate.of(2024, Month.APRIL,     1)};
        // Labour Day: weekend cases prove noRoll() — TARGET2 records May 1 as-is, no weekday displacement
        final Object[] labourDay21    = {2021, "Labour Day",    LocalDate.of(2021, Month.MAY,       1)}; // Saturday → stays May 1
        final Object[] labourDay22    = {2022, "Labour Day",    LocalDate.of(2022, Month.MAY,       1)}; // Sunday   → stays May 1
        final Object[] labourDay23    = {2023, "Labour Day",    LocalDate.of(2023, Month.MAY,       1)}; // Monday   → no roll
        // Christmas + Boxing Day 2021: Dec 25 Sat, Dec 26 Sun — both stay on their actual dates
        final Object[] christmas21    = {2021, "Christmas Day", LocalDate.of(2021, Month.DECEMBER, 25)}; // Saturday → stays Dec 25
        final Object[] boxingDay21    = {2021, "Boxing Day",    LocalDate.of(2021, Month.DECEMBER, 26)}; // Sunday   → stays Dec 26
        // Christmas + Boxing Day 2022: Dec 25 Sun, Dec 26 Mon — both stay on their actual dates
        final Object[] christmas22    = {2022, "Christmas Day", LocalDate.of(2022, Month.DECEMBER, 25)}; // Sunday   → stays Dec 25
        final Object[] boxingDay22    = {2022, "Boxing Day",    LocalDate.of(2022, Month.DECEMBER, 26)}; // Monday   → no roll
        return Arrays.asList(
                goodFriday22, easterMonday22,
                goodFriday24, easterMonday24,
                labourDay21, labourDay22, labourDay23,
                christmas21, boxingDay21,
                christmas22, boxingDay22
        ).listIterator();
    }

    @Test(dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testHolidayCount() {
        final HolidayCalendar calendar = factory.create(CODE);
        assertEquals(calendar.getHolidays().size(), 6,
                "EUR calendar must define exactly 6 TARGET2 closure days");
    }

}
