package org.holiday.calendar.impl;

import org.testng.annotations.DataProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Fixtures verify TSX's actual weekend substitution rule, per TMX Group's
 * official Holiday Operating Schedule: both Saturday and Sunday roll
 * <strong>forward</strong> to the next available business day, never
 * backward. This is identical to the Bank of Canada (Lynx) / {@code CAD}
 * convention, and to the corrected {@code CA} national calendar's rule.
 */
public class HolidayCalendarServiceXTSETest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "XTSE";

    public HolidayCalendarServiceXTSETest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        final Object[] familyDay = {"Family Day"};
        final Object[] victoriaDay = {"Victoria Day"};
        final Object[] christmasEve = {"Christmas Eve"};
        return Arrays.asList(familyDay, victoriaDay, christmasEve).listIterator();
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayOccurrences() {
        final Object[] canadaDay18 = {2018, "Canada Day", LocalDate.of(2018, Month.JULY, 2)}; // Sun -> following Monday
        final Object[] canadaDay19 = {2019, "Canada Day", LocalDate.of(2019, Month.JULY, 1)};
        final Object[] canadaDay20 = {2020, "Canada Day", LocalDate.of(2020, Month.JULY, 1)};
        final Object[] canadaDay21 = {2021, "Canada Day", LocalDate.of(2021, Month.JULY, 1)};
        final Object[] canadaDay22 = {2022, "Canada Day", LocalDate.of(2022, Month.JULY, 1)};
        final Object[] canadaDay23 = {2023, "Canada Day", LocalDate.of(2023, Month.JULY, 3)}; // Sat -> following Monday
        final Object[] remembranceDay18 = {2018, "Remembrance Day", LocalDate.of(2018, Month.NOVEMBER, 12)}; // Sun -> following Monday
        final Object[] remembranceDay19 = {2019, "Remembrance Day", LocalDate.of(2019, Month.NOVEMBER, 11)};
        final Object[] remembranceDay20 = {2020, "Remembrance Day", LocalDate.of(2020, Month.NOVEMBER, 11)};
        final Object[] remembranceDay21 = {2021, "Remembrance Day", LocalDate.of(2021, Month.NOVEMBER, 11)};
        final Object[] remembranceDay22 = {2022, "Remembrance Day", LocalDate.of(2022, Month.NOVEMBER, 11)};
        final Object[] remembranceDay23 = {2023, "Remembrance Day", LocalDate.of(2023, Month.NOVEMBER, 13)}; // Sat -> following Monday
        final Object[] newYearsDay22 = {2022, "New Year's Day", LocalDate.of(2022, Month.JANUARY, 3)}; // Sat -> following Monday
        final Object[] christmas21 = {2021, "Christmas Day", LocalDate.of(2021, Month.DECEMBER, 27)}; // Sat -> following Monday (TMX 2021 schedule confirmed)
        final Object[] boxingDay21 = {2021, "Boxing Day", LocalDate.of(2021, Month.DECEMBER, 28)}; // Christmas=Saturday cascade, displaced to Tuesday (TMX 2021 schedule confirmed)
        final Object[] christmas22 = {2022, "Christmas Day", LocalDate.of(2022, Month.DECEMBER, 26)}; // Sun -> following Monday, consumes Boxing Day's natural date
        final Object[] boxingDay22 = {2022, "Boxing Day", LocalDate.of(2022, Month.DECEMBER, 27)}; // displaced to Tuesday by Christmas collision
        final Object[] christmas23 = {2023, "Christmas Day", LocalDate.of(2023, Month.DECEMBER, 25)}; // Mon, no roll
        final Object[] boxingDay23 = {2023, "Boxing Day", LocalDate.of(2023, Month.DECEMBER, 26)}; // Tue, no roll
        final Object[] christmas26 = {2026, "Christmas Day", LocalDate.of(2026, Month.DECEMBER, 25)}; // Fri, no roll
        final Object[] boxingDay26 = {2026, "Boxing Day", LocalDate.of(2026, Month.DECEMBER, 28)}; // Sat -> following Monday, no collision with Friday Christmas
        return Arrays.asList(canadaDay18, canadaDay19, canadaDay20, canadaDay21, canadaDay22, canadaDay23,
                             remembranceDay18, remembranceDay19, remembranceDay20, remembranceDay21, remembranceDay22, remembranceDay23,
                             newYearsDay22,
                             christmas21, boxingDay21,
                             christmas22, boxingDay22,
                             christmas23, boxingDay23,
                             christmas26, boxingDay26).listIterator();
    }

}