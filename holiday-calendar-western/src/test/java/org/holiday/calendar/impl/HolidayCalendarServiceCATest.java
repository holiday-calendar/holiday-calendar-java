package org.holiday.calendar.impl;

import org.holiday.calendar.HolidayCalendar;
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
        final Object[] canadaDay23 = {2023, "Canada Day", LocalDate.of(2023, Month.JULY, 3)}; // Sat -> following Monday
        final Object[] remembranceDay18 = {2018, "Remembrance Day", LocalDate.of(2018, Month.NOVEMBER, 12)};
        final Object[] remembranceDay19 = {2019, "Remembrance Day", LocalDate.of(2019, Month.NOVEMBER, 11)};
        final Object[] remembranceDay20 = {2020, "Remembrance Day", LocalDate.of(2020, Month.NOVEMBER, 11)};
        final Object[] remembranceDay21 = {2021, "Remembrance Day", LocalDate.of(2021, Month.NOVEMBER, 11)};
        final Object[] remembranceDay22 = {2022, "Remembrance Day", LocalDate.of(2022, Month.NOVEMBER, 11)};
        final Object[] remembranceDay23 = {2023, "Remembrance Day", LocalDate.of(2023, Month.NOVEMBER, 13)}; // Sat -> following Monday
        final Object[] ndtr21 = {2021, "National Day For Truth and Reconciliation", LocalDate.of(2021, Month.SEPTEMBER, 30)}; // Thu, no roll, first observed year
        final Object[] ndtr23 = {2023, "National Day For Truth and Reconciliation", LocalDate.of(2023, Month.OCTOBER, 2)}; // Sat -> following Monday
        return Arrays.asList(canadaDay18, canadaDay19, canadaDay20, canadaDay21, canadaDay22, canadaDay23,
                             remembranceDay18, remembranceDay19, remembranceDay20, remembranceDay21, remembranceDay22, remembranceDay23,
                             ndtr21, ndtr23).listIterator();
    }

    @Test
    public void testNdtrAbsentBefore2021() {
        HolidayCalendarService service = factory.getService(CODE);
        for (int year : List.of(2019, 2020)) {
            List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
            assertFalse(holidays.stream().anyMatch(hd -> "National Day For Truth and Reconciliation".equals(hd.getHoliday().getName())),
                    "CA: NDTR must not appear for " + year + " (first observed 2021)");
        }
    }

    @Test
    public void testEarlyCloseHolidaysAbsentFromCalculate() {
        HolidayCalendarService service = factory.getService(CODE);
        for (int year : List.of(2021, 2023, 2024, 2025)) {
            List<HolidayDate> holidays = service.getHolidayCalendar().calculate(year);
            Set<String> actualNames = holidays.stream()
                    .map(hd -> hd.getHoliday().getName())
                    .collect(Collectors.toSet());
            assertFalse(actualNames.contains("Christmas Eve"),
                    "Christmas Eve must not appear in calculate(" + year + ") — moved to XTSE");
        }
    }

    @Test
    public void testCaHasNoEarlyCloses() {
        HolidayCalendarService service = factory.getService(CODE);
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertFalse(calendar.hasEarlyCloses(), "CA: national calendar must have zero early closes");
    }

}