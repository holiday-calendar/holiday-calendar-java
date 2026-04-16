package org.holiday.calendar.impl;

import org.holiday.calendar.Holiday;
import org.holiday.calendar.HolidayCalendar;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class HolidayCalendarServiceUSDTest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "USD";

    public HolidayCalendarServiceUSDTest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        // Columbus Day and Veterans Day distinguish USD (Federal Reserve) from US (NYSE)
        final Object[] columbusDay = {"Columbus Day"};
        final Object[] veteransDay = {"Veterans Day"};
        return Arrays.asList(columbusDay, veteransDay).listIterator();
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayOccurrences() {
        // Juneteenth 2021: Jun 19 is Saturday → rolls to Friday Jun 18
        final Object[] juneteenth21   = {2021, "Juneteenth",       LocalDate.of(2021, Month.JUNE,     18)};
        // Juneteenth 2022: Jun 19 is Sunday → rolls to Monday Jun 20
        final Object[] juneteenth22   = {2022, "Juneteenth",       LocalDate.of(2022, Month.JUNE,     20)};
        // Juneteenth 2023: Jun 19 is Monday → no roll
        final Object[] juneteenth23   = {2023, "Juneteenth",       LocalDate.of(2023, Month.JUNE,     19)};
        // Independence Day 2021: Jul 4 is Sunday → rolls to Monday Jul 5
        final Object[] independence21 = {2021, "Independence Day", LocalDate.of(2021, Month.JULY,      5)};
        // Independence Day 2020: Jul 4 is Saturday → rolls to Friday Jul 3
        final Object[] independence20 = {2020, "Independence Day", LocalDate.of(2020, Month.JULY,      3)};
        // Veterans Day 2018: Nov 11 is Sunday → rolls to Monday Nov 12
        final Object[] veteransDay18  = {2018, "Veterans Day",     LocalDate.of(2018, Month.NOVEMBER, 12)};
        // Veterans Day 2023: Nov 11 is Saturday → rolls to Friday Nov 10
        final Object[] veteransDay23  = {2023, "Veterans Day",     LocalDate.of(2023, Month.NOVEMBER, 10)};
        // Christmas 2021: Dec 25 is Saturday → rolls to Friday Dec 24
        final Object[] christmas21    = {2021, "Christmas Day",    LocalDate.of(2021, Month.DECEMBER, 24)};
        // Christmas 2022: Dec 25 is Sunday → rolls to Monday Dec 26
        final Object[] christmas22    = {2022, "Christmas Day",    LocalDate.of(2022, Month.DECEMBER, 26)};
        // New Year's 2021: Jan 1 is Friday → no roll
        final Object[] newYears21     = {2021, "New Year's Day",   LocalDate.of(2021, Month.JANUARY,   1)};
        // New Year's 2022: Jan 1 is Saturday → rolls to Friday Dec 31 of prior year
        final Object[] newYears22     = {2022, "New Year's Day",   LocalDate.of(2021, Month.DECEMBER, 31)};
        // Columbus Day 2021: Oct 11 is Monday → no roll
        final Object[] columbusDay21  = {2021, "Columbus Day",     LocalDate.of(2021, Month.OCTOBER,  11)};
        return Arrays.asList(
                juneteenth21, juneteenth22, juneteenth23,
                independence21, independence20,
                veteransDay18, veteransDay23,
                christmas21, christmas22,
                newYears21, newYears22,
                columbusDay21
        ).listIterator();
    }

    @Test(dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testHolidayCount() {
        final HolidayCalendar calendar = factory.create(CODE);
        assertEquals(calendar.getHolidays().size(), 11,
                "USD calendar must define exactly 11 Federal Reserve holidays");
    }

    @Test(dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testNYSEOnlyHolidaysAbsent() {
        final HolidayCalendar calendar = factory.create(CODE);
        final Set<String> names = calendar.getHolidays().stream()
                .map(Holiday::getName)
                .collect(Collectors.toSet());
        assertFalse(names.contains("Good Friday"),
                "USD calendar must not contain 'Good Friday' (NYSE-only closure)");
        assertFalse(names.contains("Day After Thanksgiving"),
                "USD calendar must not contain 'Day After Thanksgiving' (NYSE-only closure)");
    }

    @Test(dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testFederalReserveSpecificHolidaysPresent() {
        final HolidayCalendar calendar = factory.create(CODE);
        final Set<String> names = calendar.getHolidays().stream()
                .map(Holiday::getName)
                .collect(Collectors.toSet());
        assertTrue(names.contains("Columbus Day"),
                "USD calendar must contain 'Columbus Day'");
        assertTrue(names.contains("Veterans Day"),
                "USD calendar must contain 'Veterans Day'");
    }

    @Test(dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testJuneteenthYearBounds() {
        final HolidayCalendar calendar = factory.create(CODE);
        // Juneteenth was not a federal holiday before June 17, 2021
        assertEquals(calendar.calculate(2020).size(), 10,
                "2020 should yield 10 holidays (Juneteenth not yet in effect)");
        final long juneteenth2020 = calendar.calculate(2020).stream()
                .filter(hd -> "Juneteenth".equals(hd.getHoliday().getName()))
                .count();
        assertEquals(juneteenth2020, 0L,
                "Juneteenth must not appear in calculate(2020)");
        // Juneteenth became effective in 2021
        assertEquals(calendar.calculate(2021).size(), 11,
                "2021 should yield 11 holidays (Juneteenth now a federal holiday)");
        final long juneteenth2021 = calendar.calculate(2021).stream()
                .filter(hd -> "Juneteenth".equals(hd.getHoliday().getName()))
                .count();
        assertEquals(juneteenth2021, 1L,
                "Juneteenth must appear exactly once in calculate(2021)");
    }

}
