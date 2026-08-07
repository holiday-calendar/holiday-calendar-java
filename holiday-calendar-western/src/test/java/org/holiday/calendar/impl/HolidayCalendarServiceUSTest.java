package org.holiday.calendar.impl;

import org.holiday.calendar.HolidayCalendarService;
import org.holiday.calendar.HolidayDate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.testng.Assert.assertFalse;

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

    @Test
    public void testGoodFridayAbsentFromCalculate() {
        HolidayCalendarService service = factory.getService(CODE);
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        assertFalse(holidays.stream().anyMatch(hd -> "Good Friday".equals(hd.getHoliday().getName())),
                "US: Good Friday is a NYSE-only closure, not a US federal holiday, and must not appear in calculate()");
    }

    @Test
    public void testEarlyCloseHolidaysAbsentFromCalculate() {
        HolidayCalendarService service = factory.getService(CODE);
        Set<String> earlyCloseNames = Set.of("Day After Thanksgiving", "Christmas Eve", "July 3rd");
        for (int year : List.of(2021, 2023, 2024, 2025)) { // mix of 1/2/3-count early-close years
            List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
            Set<String> actualNames = holidays.stream()
                    .map(hd -> hd.getHoliday().getName())
                    .collect(Collectors.toSet());
            for (String earlyCloseName : earlyCloseNames) {
                assertFalse(actualNames.contains(earlyCloseName),
                        earlyCloseName + " must not appear in calculate(" + year + ")");
            }
        }
    }

}