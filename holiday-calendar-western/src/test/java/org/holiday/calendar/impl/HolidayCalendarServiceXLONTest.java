package org.holiday.calendar.impl;

import org.testng.annotations.DataProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Fixtures mirror {@link HolidayCalendarServiceUKTest} — {@code XLON} shares
 * the exact same holiday set and {@code UKDateRolls.fixedHolidayRoll}
 * behavior as {@code UK}, differing only by the addition of the Christmas
 * Eve/New Year's Eve early closes (see {@link HolidayCalendarServiceXLONEarlyCloseTest}).
 */
public class HolidayCalendarServiceXLONTest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "XLON";

    public HolidayCalendarServiceXLONTest() {
        super(CODE);
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