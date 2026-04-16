package org.holiday.calendar.impl;

import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.HolidayCalendarFactory;
import org.holiday.calendar.HolidayDate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;

public class HolidayCalendarServiceUKTest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "UK";

    public HolidayCalendarServiceUKTest() {
        super(CODE);
    }

    @Test
    public void testDateRoll_BoxingDayCase() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        HolidayCalendar calendar = factory.create(CODE);
        assertNotNull(calendar);

        List<HolidayDate> ukHolidays2021 = calendar.calculate(2021);
        Optional<HolidayDate> foundBoxingDay = ukHolidays2021.stream()
                                                             .filter(hd -> "Boxing Day".equals(hd.getHoliday().getName()))
                                                             .findFirst();
        assertTrue(foundBoxingDay.isPresent());
        assertEquals(foundBoxingDay.get().getDate(), LocalDate.of(2021, Month.DECEMBER, 28));
    }

    @Test
    public void testDateRoll_NewYearsDaySaturday() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        HolidayCalendar calendar = factory.create(CODE);
        assertNotNull(calendar);

        List<HolidayDate> ukHolidays2022 = calendar.calculate(2022);
        Optional<HolidayDate> foundNewYearsDay = ukHolidays2022.stream()
                                                                .filter(hd -> "New Year's Day".equals(hd.getHoliday().getName()))
                                                                .findFirst();
        assertTrue(foundNewYearsDay.isPresent());
        assertEquals(foundNewYearsDay.get().getDate(), LocalDate.of(2022, Month.JANUARY, 3));
    }

    @Test
    public void testDateRoll_NewYearsDaySunday() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        HolidayCalendar calendar = factory.create(CODE);
        assertNotNull(calendar);

        List<HolidayDate> ukHolidays2023 = calendar.calculate(2023);
        Optional<HolidayDate> foundNewYearsDay = ukHolidays2023.stream()
                                                                .filter(hd -> "New Year's Day".equals(hd.getHoliday().getName()))
                                                                .findFirst();
        assertTrue(foundNewYearsDay.isPresent());
        assertEquals(foundNewYearsDay.get().getDate(), LocalDate.of(2023, Month.JANUARY, 2));
    }

    @Test
    public void testDateRoll_DefaultCase() {
        HolidayCalendarFactory factory = new HolidayCalendarFactory();
        HolidayCalendar calendar = factory.create(CODE);
        assertNotNull(calendar);

        List<HolidayDate> ukHolidays2021 = calendar.calculate(2020);
        Optional<HolidayDate> foundNewYearsDay = ukHolidays2021.stream()
                                                               .filter(hd -> "New Year's Day".equals(hd.getHoliday().getName()))
                                                               .findFirst();
        assertTrue(foundNewYearsDay.isPresent());
        assertEquals(foundNewYearsDay.get().getDate(), LocalDate.of(2020, Month.JANUARY, 1));
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        final Object[] summerBankHoliday = {"Summer Bank Holiday"};
        final Object[] boxingDay = {"Boxing Day"};
        return Arrays.asList(summerBankHoliday, boxingDay).listIterator();
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayOccurrences() {
        final Object[] newYearsDay22 = {2022, "New Year's Day", LocalDate.of(2022, Month.JANUARY, 3)};
        final Object[] newYearsDay23 = {2023, "New Year's Day", LocalDate.of(2023, Month.JANUARY, 2)};
        final Object[] christmas18 = {2018, "Christmas Day", LocalDate.of(2018, Month.DECEMBER, 25)};
        final Object[] christmas19 = {2019, "Christmas Day", LocalDate.of(2019, Month.DECEMBER, 25)};
        final Object[] christmas20 = {2020, "Christmas Day", LocalDate.of(2020, Month.DECEMBER, 25)};
        final Object[] christmas21 = {2021, "Christmas Day", LocalDate.of(2021, Month.DECEMBER, 27)};
        final Object[] christmas22 = {2022, "Christmas Day", LocalDate.of(2022, Month.DECEMBER, 27)};
        final Object[] christmas23 = {2023, "Christmas Day", LocalDate.of(2023, Month.DECEMBER, 25)};
        final Object[] boxingDay18 = {2018, "Boxing Day", LocalDate.of(2018, Month.DECEMBER, 26)};
        final Object[] boxingDay19 = {2019, "Boxing Day", LocalDate.of(2019, Month.DECEMBER, 26)};
        final Object[] boxingDay20 = {2020, "Boxing Day", LocalDate.of(2020, Month.DECEMBER, 28)};
        final Object[] boxingDay21 = {2021, "Boxing Day", LocalDate.of(2021, Month.DECEMBER, 28)};
        final Object[] boxingDay22 = {2022, "Boxing Day", LocalDate.of(2022, Month.DECEMBER, 26)};
        final Object[] boxingDay23 = {2023, "Boxing Day", LocalDate.of(2023, Month.DECEMBER, 26)};
        return Arrays.asList(newYearsDay22, newYearsDay23,
                             christmas18, christmas19, christmas20, christmas21, christmas22, christmas23,
                             boxingDay18, boxingDay19, boxingDay20, boxingDay21, boxingDay22, boxingDay23).listIterator();
    }

}