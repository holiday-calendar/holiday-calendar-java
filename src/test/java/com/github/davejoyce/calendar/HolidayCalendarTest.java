package com.github.davejoyce.calendar;

import org.testng.annotations.*;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import static org.testng.Assert.*;

public class HolidayCalendarTest {

    private static final ZoneId ZONE_ID_NEW_YORK = ZoneId.of("America/New_York");
    private static final ZoneId ZONE_ID_TOKYO = ZoneId.of("Asia/Tokyo");
    private static final TimeZone TZ_NEW_YORK = TimeZone.getTimeZone(ZONE_ID_NEW_YORK);

    private HolidayCalendar standardEmptyHolidayCalendar;

    @Test(expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "Argument 'holidays'.*")
    public void testFullConstructorWithNullHolidays() {
        new HolidayCalendar("FRB", "Federal Reserve Board", HolidayCalendar.STANDARD_WEEKEND, (Holiday[]) null);
    }

    @Test
    public void testPartialConstructorInitializesEmptyHolidays() {
        HolidayCalendar holidayCalendar = new HolidayCalendar("FRB", "Federal Reserve Board", HolidayCalendar.STANDARD_WEEKEND);
        Map<String, Holiday> view = holidayCalendar.getHolidays();
        assertNotNull(view, "Expected non-null holidays");
        assertTrue(view.isEmpty(), "Expected empty holidays");
    }

    @Test
    public void testMinimalConstructorInitializesEmptyHolidays() {
        HolidayCalendar holidayCalendar = new HolidayCalendar("FRB", "Federal Reserve Board");
        Map<String, Holiday> view = holidayCalendar.getHolidays();
        assertNotNull(view, "Expected non-null holidays");
        assertTrue(view.isEmpty(), "Expected empty holidays");
    }

    @Test
    public void testGetCode() {
        HolidayCalendar holidayCalendar = new HolidayCalendar("FRB", "Federal Reserve Board");
        assertEquals(holidayCalendar.getCode(), "FRB");
    }

    @Test
    public void testGetName() {
        HolidayCalendar holidayCalendar = new HolidayCalendar("FRB", "Federal Reserve Board");
        assertEquals(holidayCalendar.getName(), "Federal Reserve Board");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testGetHolidays_Unmodifiable() {
        HolidayCalendar holidayCalendar = new HolidayCalendar("FRB", "Federal Reserve Board");
        Map<String, Holiday> view = holidayCalendar.getHolidays();
        assertNotNull(view, "Expected non-null holidays");

        view.put("TEST", null);
        fail("Holidays map view should be unmodifiable!");
    }

    @Test
    public void testToString() {
        HolidayCalendar holidayCalendar = new HolidayCalendar("FRB", "Federal Reserve Board");
        String actual = holidayCalendar.toString();
        assertEquals(actual, "HolidayCalendar[code='FRB', name='Federal Reserve Board']");
    }

    @BeforeMethod(onlyForGroups = "weekend")
    public void setupStandardEmptyHolidayCalendar() {
        this.standardEmptyHolidayCalendar = new HolidayCalendar("FRB", "Federal Reserve Board", HolidayCalendar.STANDARD_WEEKEND);
    }

    @AfterMethod(onlyForGroups = "weekend")
    public void teardownStandardEmptyHolidayCalendar() {
        this.standardEmptyHolidayCalendar = null;
    }

    @Test(groups = "weekend")
    public void testGetWeekendDays() {
        assertNotNull(standardEmptyHolidayCalendar, "Expected non-null standard HolidayCalendar");

        assertEquals(standardEmptyHolidayCalendar.getWeekendDays(), HolidayCalendar.STANDARD_WEEKEND);
    }

    @Test(groups = "weekend",
          expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "Argument 'instant'.*")
    public void testIsWeekend_NullInstantThrowsException() {
        assertNotNull(standardEmptyHolidayCalendar, "Expected non-null standard HolidayCalendar");

        standardEmptyHolidayCalendar.isWeekend(null, ZONE_ID_NEW_YORK);
    }

    @Test(groups = "weekend",
          expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "Argument 'zone[Ii][Dd]'.*")
    public void testIsWeekend_NullZoneIdThrowsException() {
        assertNotNull(standardEmptyHolidayCalendar, "Expected non-null standard HolidayCalendar");

        standardEmptyHolidayCalendar.isWeekend(Instant.now(), null);
    }

    @Test(groups = "weekend",
          expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "Argument 'date'.*")
    public void testTestIsWeekend_NullDateThrowsException() {
        assertNotNull(standardEmptyHolidayCalendar, "Expected non-null standard HolidayCalendar");

        standardEmptyHolidayCalendar.isWeekend(null, TZ_NEW_YORK);
    }

    @Test(groups = "weekend",
          expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "Argument 'timeZone'.*")
    public void testTestIsWeekend_NullTimeZoneThrowsException() {
        assertNotNull(standardEmptyHolidayCalendar, "Expected non-null standard HolidayCalendar");

        standardEmptyHolidayCalendar.isWeekend(new Date(), null);
    }

    @Test(groups = "weekend")
    public void testIsWeekend() {
        assertNotNull(standardEmptyHolidayCalendar, "Expected non-null standard HolidayCalendar");

        Instant instant = getLastSundayInNovemberAtNewYork();
        assertTrue(standardEmptyHolidayCalendar.isWeekend(instant, ZONE_ID_NEW_YORK));
        assertFalse(standardEmptyHolidayCalendar.isWeekend(instant, ZONE_ID_TOKYO));
    }

    @Test(groups = "weekend")
    public void testIsWeekendUTC_Instant() {
        assertNotNull(standardEmptyHolidayCalendar, "Expected non-null standard HolidayCalendar");

        Instant instant = getLastSundayInNovemberAtNewYork();
        assertTrue(standardEmptyHolidayCalendar.isWeekendUTC(instant));

        instant = instant.plus(8, ChronoUnit.HOURS);
        assertFalse(standardEmptyHolidayCalendar.isWeekendUTC(instant));
    }

    @Test(groups = "weekend")
    public void testIsWeekendUTC_Date() {
        assertNotNull(standardEmptyHolidayCalendar, "Expected non-null standard HolidayCalendar");

        Instant instant = getLastSundayInNovemberAtNewYork();
        Date date = Date.from(instant);
        assertTrue(standardEmptyHolidayCalendar.isWeekendUTC(date));

        instant = instant.plus(8, ChronoUnit.HOURS);
        date = Date.from(instant);
        assertFalse(standardEmptyHolidayCalendar.isWeekendUTC(date));
    }

    @Test
    public void testMerge() {
    }

    private Instant getLastSundayInNovemberAtNewYork() {
        LocalDate lastSundayInNovember = LocalDate.of(2021, Month.NOVEMBER, 28);
        return LocalDateTime.of(lastSundayInNovember, LocalTime.NOON)
                            .atZone(ZONE_ID_NEW_YORK)
                            .toInstant();
    }

}