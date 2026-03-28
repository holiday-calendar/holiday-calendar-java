/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2021 David Joyce
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA
 ******************************************************************************/

package org.holiday.calendar;

import org.holiday.calendar.function.DateRoll;
import org.testng.annotations.Test;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.holiday.calendar.TestObjects.*;
import static org.testng.Assert.*;

public class HolidayCalendarTest {

    private static final ZoneId ZONE_ID_NEW_YORK = ZoneId.of("America/New_York");

    @Test(groups = "core")
    public void testBuilder_NoHolidays() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        assertNotNull(holidayCalendar.getHolidays());
        assertTrue(holidayCalendar.getHolidays().isEmpty());
    }

    @Test(groups = "core")
    public void testBuilder_NoWeekendDays() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        assertEquals(holidayCalendar.getWeekendDays(), HolidayCalendar.STANDARD_WEEKEND);
    }

    @Test(groups = "core")
    public void testBuilder_NoDateRoll() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        assertEquals(holidayCalendar.getDateRoll(), HolidayCalendar.NO_ROLL);
    }

    @Test(groups = "core")
    public void testGetCode() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        assertEquals(holidayCalendar.getCode(), "FRB");
    }

    @Test(groups = "core")
    public void testGetName() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        assertEquals(holidayCalendar.getName(), "Federal Reserve Board");
    }

    @Test(groups = "core")
    public void testGetDateRoll() {
        final DateRoll usDateRoll = createDateRollUS();
        HolidayCalendar holidayCalendar = new HolidayCalendar("FRB", "Federal Reserve Board", usDateRoll, null, null);
        assertEquals(holidayCalendar.getDateRoll(), usDateRoll);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class, groups = "core")
    public void testGetHolidays_Unmodifiable() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        Set<Holiday> view = holidayCalendar.getHolidays();
        assertNotNull(view, "Expected non-null holidays");

        view.add(null);
        fail("Holidays set view should be unmodifiable!");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class, groups = "core")
    public void testGetWeekendDays_Unmodifiable() {
        HolidayCalendar holidayCalendar = HolidayCalendar.builder()
                                                         .code("FRB")
                                                         .name("Federal Reserve Board")
                                                         .build();
        Set<DayOfWeek> view = holidayCalendar.getWeekendDays();
        assertNotNull(view, "Expected non-null weekendDays");

        view.add(null);
        fail("WeekendDays set view should be unmodifiable!");
    }

    @Test(groups = "core")
    public void testToString() {
        HolidayCalendar holidayCalendar = createHolidayCalendarSifmaUS();
        String actual = holidayCalendar.toString();
        assertEquals(actual, "HolidayCalendar[code='SIFMA', name='SIFMA Holiday Recommendations (US)']");
    }

    @Test(groups = "core")
    public void testCalculate() {
        HolidayCalendar calendar = createHolidayCalendarSifmaUS();
        List<HolidayDate> dates = calendar.calculate(2021);

        assertEquals(dates.getFirst().getHoliday().getName(), "New Year's Day");
        assertEquals(dates.getFirst().getDate(), LocalDate.of(2021, Month.JANUARY, 1));
        assertEquals(dates.getLast().getHoliday().getName(), "Christmas Day");
        assertEquals(dates.getLast().getDate(), LocalDate.of(2021, Month.DECEMBER, 24)); // rolled back 1 day
    }

    @Test(groups = "core")
    public void testMerge() {
        HolidayCalendar sifmaCalendar = createHolidayCalendarSifmaUS();
        HolidayCalendar frbCalendar = HolidayCalendar.builder()
                                                     .code("FRB")
                                                     .name("Federal Reserve Board")
                                                     .build();
        assertEquals(sifmaCalendar.getHolidays().size(), 10);

        HolidayCalendar merged = sifmaCalendar.merge(frbCalendar);
        assertEquals(merged.getCode(), "SIFMA/FRB");
        assertEquals(merged.getWeekendDays(), HolidayCalendar.STANDARD_WEEKEND);
        assertEquals(merged.getHolidays().size(), 10);
    }

    @Test(groups = "core")
    public void testMergeNull() {
        HolidayCalendar sifmaCalendar = createHolidayCalendarSifmaUS();
        HolidayCalendar merged = sifmaCalendar.merge(null);
        assertSame(merged, sifmaCalendar);
    }

    @Test(groups = "core")
    public void testMergeThis() {
        HolidayCalendar sifmaCalendar = createHolidayCalendarSifmaUS();
        HolidayCalendar merged = sifmaCalendar.merge(sifmaCalendar);
        assertSame(merged, sifmaCalendar);
    }

    @Test(groups = "core")
    public void testIsWeekendUTC_Date() {
        HolidayCalendar calendar = createHolidayCalendarSifmaUS();

        LocalDate date = LocalDate.of(2021, Month.DECEMBER, 19);
        Date d = new Date(date.atStartOfDay(ZONE_ID_NEW_YORK).toInstant().toEpochMilli());
        boolean actual = calendar.isWeekendUTC(d);
        assertTrue(actual);
    }

    @Test(groups = "core")
    public void testIsWeekendUTC_Instant() {
        HolidayCalendar calendar = createHolidayCalendarSifmaUS();

        LocalDate date = LocalDate.of(2021, Month.DECEMBER, 19);
        Instant i = date.atStartOfDay(ZONE_ID_NEW_YORK).toInstant();
        boolean actual = calendar.isWeekendUTC(i);
        assertTrue(actual);
    }

    @Test(groups = "core")
    public void testCalculateIsChronologicallyOrdered() {
        HolidayCalendar calendar = HolidayCalendar.builder()
                                                  .code("TEST")
                                                  .name("Chronological Order Test Calendar")
                                                  .holiday(new FixedHoliday("Christmas Day", "", Month.DECEMBER, 25))
                                                  .holiday(new FixedHoliday("Fourth of July", "", Month.JULY, 4))
                                                  .holiday(new FixedHoliday("New Year's Day", "", Month.JANUARY, 1))
                                                  .build();

        List<HolidayDate> dates = calendar.calculate(2021);

        assertEquals(dates.size(), 3);
        assertTrue(dates.get(0).getDate().isBefore(dates.get(1).getDate()),
                   "First date should be before second date");
        assertTrue(dates.get(1).getDate().isBefore(dates.get(2).getDate()),
                   "Second date should be before third date");
        assertEquals(dates.get(0).getHoliday().getName(), "New Year's Day");
        assertEquals(dates.get(1).getHoliday().getName(), "Fourth of July");
        assertEquals(dates.get(2).getHoliday().getName(), "Christmas Day");
    }

    @Test(groups = "core")
    public void testIsWeekendUTC_LocalDate_Saturday() {
        HolidayCalendar calendar = createHolidayCalendarSifmaUS();

        // 2021-12-18 is a Saturday
        LocalDate saturday = LocalDate.of(2021, Month.DECEMBER, 18);
        Instant saturdayInstant = saturday.atStartOfDay(ZoneOffset.UTC).toInstant();
        assertTrue(calendar.isWeekendUTC(saturdayInstant));
    }

    @Test(groups = "core")
    public void testIsWeekendUTC_LocalDate_Sunday() {
        HolidayCalendar calendar = createHolidayCalendarSifmaUS();

        // 2021-12-19 is a Sunday
        LocalDate sunday = LocalDate.of(2021, Month.DECEMBER, 19);
        Instant sundayInstant = sunday.atStartOfDay(ZoneOffset.UTC).toInstant();
        assertTrue(calendar.isWeekendUTC(sundayInstant));
    }

    @Test(groups = "core")
    public void testIsWeekendUTC_LocalDate_Monday() {
        HolidayCalendar calendar = createHolidayCalendarSifmaUS();

        // 2021-12-20 is a Monday
        LocalDate monday = LocalDate.of(2021, Month.DECEMBER, 20);
        Instant mondayInstant = monday.atStartOfDay(ZoneOffset.UTC).toInstant();
        assertFalse(calendar.isWeekendUTC(mondayInstant));
    }

    private HolidayCalendar createHolidayCalendarSifmaUS() {
        return HolidayCalendar.builder()
                              .code("SIFMA")
                              .name("SIFMA Holiday Recommendations (US)")
                              .dateRoll(createDateRollUS())
                              .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                              .holiday(new FixedHoliday("New Year's Day", "", Month.JANUARY, 1))
                              .holiday(new FloatingHoliday("Martin Luther King Jr. Day", "Honor of Martin Luther King Jr's birthday", createObservanceMlkDay()))
                              .holiday(new FloatingHoliday("Presidents' Day", "Honor of George Washington's birthday", createObservancePresidentsDay()))
                              .holiday(new FloatingHoliday("Memorial Day", "Mourning of fallen US military personnel", createObservanceMemorialDay()))
                              .holiday(new FixedHoliday("Independence Day", "Fourth of July", Month.JULY, 4))
                              .holiday(new FloatingHoliday("Labor Day", "Recognition of American labor", createObservanceLaborDay()))
                              .holiday(new FloatingHoliday("Columbus Day", "Anniversary of arrival of Columbus in Americas", createObservanceColumbusDay()))
                              .holiday(new FixedHoliday("Veterans Day", "Honor of all US veterans", Month.NOVEMBER, 11))
                              .holiday(new FloatingHoliday("Thanksgiving Day", "Day of giving thanks", createObservanceThanksgiving()))
                              .holiday(new FixedHoliday("Christmas Day", "", Month.DECEMBER, 25))
                              .build();
    }

}