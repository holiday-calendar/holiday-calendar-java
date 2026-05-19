/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2021-2026 The Holiday Calendar Project Contributors
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

package org.holiday.calendar.impl;

import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.HolidayCalendarFactory;
import org.holiday.calendar.HolidayDate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.testng.Assert.*;

public class HolidayCalendarServiceSARTest {

    // 2 fixed + 3 Eid al-Fitr + 3 Eid al-Adha + Islamic New Year + Mawlid = 10
    private static final int SA_HOLIDAY_COUNT = 10;

    private final HolidayCalendarServiceSAR service = new HolidayCalendarServiceSAR();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("SAR"));
        assertFalse(service.isProvided("SA"));
        assertFalse(service.isProvided("AED"));
        assertFalse(service.isProvided("US"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "SAR");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Saudi Arabia (Tadawul/SAMA) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("SAR");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "SAR");
    }

    @Test
    public void testWeekendDaysFridayAndSaturday() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(calendar.getWeekendDays().size(), 2, "Saudi weekend must be exactly 2 days");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.FRIDAY), "Friday must be a weekend day");
        assertTrue(calendar.getWeekendDays().contains(DayOfWeek.SATURDAY), "Saturday must be a weekend day");
        assertFalse(calendar.getWeekendDays().contains(DayOfWeek.SUNDAY), "Sunday must not be a weekend day");
    }

    @Test
    public void testCalculate2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);
        assertNotNull(holidays);
        assertEquals(holidays.size(), SA_HOLIDAY_COUNT,
                "Expected " + SA_HOLIDAY_COUNT + " holidays for 2025, got: " + holidays.size());
    }

    @Test
    public void testChronologicalOrder() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).date().isBefore(holidays.get(i - 1).date()),
                    "Holidays must be in chronological order");
        }
    }

    // SAR uses noRoll() — fixed holidays on Friday/Saturday are not rolled

    // Feb 22, 2025 is Saturday — SAR must not roll it
    @Test
    public void testFoundingDayOnSaturdayNotRolled2025() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(LocalDate.of(2025, Month.FEBRUARY, 22).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> foundingDay = calendar.calculate(2025).stream()
                .filter(hd -> "Saudi Founding Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(foundingDay.isPresent());
        assertEquals(foundingDay.get().date(), LocalDate.of(2025, Month.FEBRUARY, 22),
                "SAR must not roll Saudi Founding Day 2025 (Saturday) — settlement uses no-roll convention");
    }

    // Feb 22, 2030 is Friday — SAR must not roll it
    @Test
    public void testFoundingDayOnFridayNotRolled2030() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(LocalDate.of(2030, Month.FEBRUARY, 22).getDayOfWeek(), DayOfWeek.FRIDAY);
        Optional<HolidayDate> foundingDay = calendar.calculate(2030).stream()
                .filter(hd -> "Saudi Founding Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(foundingDay.isPresent());
        assertEquals(foundingDay.get().date(), LocalDate.of(2030, Month.FEBRUARY, 22),
                "SAR must not roll Saudi Founding Day 2030 (Friday)");
    }

    // Sep 23, 2028 is Saturday — SAR must not roll it
    @Test
    public void testNationalDayOnSaturdayNotRolled2028() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        assertEquals(LocalDate.of(2028, Month.SEPTEMBER, 23).getDayOfWeek(), DayOfWeek.SATURDAY);
        Optional<HolidayDate> nationalDay = calendar.calculate(2028).stream()
                .filter(hd -> "Saudi National Day".equals(hd.holiday().getName()))
                .findFirst();
        assertTrue(nationalDay.isPresent());
        assertEquals(nationalDay.get().date(), LocalDate.of(2028, Month.SEPTEMBER, 23),
                "SAR must not roll Saudi National Day 2028 (Saturday)");
    }

    // SAR mirrors SA: Islamic floating dates must be identical
    @Test
    public void testEidAlFitr2025MatchesSA() {
        HolidayCalendar sarCalendar = service.getHolidayCalendar();
        HolidayCalendar saCalendar = new HolidayCalendarServiceSA().getHolidayCalendar();

        Optional<HolidayDate> sarEid = sarCalendar.calculate(2025).stream()
                .filter(hd -> "Eid al-Fitr".equals(hd.holiday().getName()))
                .findFirst();
        Optional<HolidayDate> saEid = saCalendar.calculate(2025).stream()
                .filter(hd -> "Eid al-Fitr".equals(hd.holiday().getName()))
                .findFirst();

        assertTrue(sarEid.isPresent());
        assertTrue(saEid.isPresent());
        assertEquals(sarEid.get().date(), saEid.get().date(),
                "SAR Eid al-Fitr date must match SA");
    }

    @Test
    public void testEidAlAdha2025MatchesSA() {
        HolidayCalendar sarCalendar = service.getHolidayCalendar();
        HolidayCalendar saCalendar = new HolidayCalendarServiceSA().getHolidayCalendar();

        Optional<HolidayDate> sarAdha = sarCalendar.calculate(2025).stream()
                .filter(hd -> "Eid al-Adha".equals(hd.holiday().getName()))
                .findFirst();
        Optional<HolidayDate> saAdha = saCalendar.calculate(2025).stream()
                .filter(hd -> "Eid al-Adha".equals(hd.holiday().getName()))
                .findFirst();

        assertTrue(sarAdha.isPresent());
        assertTrue(saAdha.isPresent());
        assertEquals(sarAdha.get().date(), saAdha.get().date(),
                "SAR Eid al-Adha date must match SA");
    }

    @Test
    public void testDataValidThroughReturnsPresent() {
        OptionalInt result = service.dataValidThrough();
        assertTrue(result.isPresent(),
                "SAR calendar has lookup-table holidays; dataValidThrough() must be present");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(), 2055,
                "dataValidThrough() must return 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("SAR");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(),
                "factory.dataValidThrough(\"SAR\") must delegate to the service");
    }

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundaryYear = service.dataValidThrough().orElseThrow();
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(boundaryYear);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundaryYear + ") must return holidays — it is within the covered range");
        assertEquals(holidays.size(), SA_HOLIDAY_COUNT,
                "Expected all " + SA_HOLIDAY_COUNT + " SAR holidays for boundary year " + boundaryYear);
    }

    @Test
    public void testCalculateBeyondDataValidThroughDropsIslamicHolidays() {
        int boundaryYear = service.dataValidThrough().orElseThrow();
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> atBoundary = calendar.calculate(boundaryYear);
        List<HolidayDate> beyondBoundary = calendar.calculate(boundaryYear + 1);
        assertTrue(beyondBoundary.size() < atBoundary.size(),
                "Year beyond dataValidThrough must produce fewer holidays; " +
                "at boundary: " + atBoundary.size() + ", beyond: " + beyondBoundary.size());
    }

    @Test
    public void testFixedHolidaysPresentBeyondCeiling() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays2056 = calendar.calculate(2056);
        assertEquals(holidays2056.size(), 2,
                "Beyond CSV ceiling only the 2 fixed Saudi holidays must remain");
    }

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"Saudi Founding Day"},
            new Object[]{"Eid al-Fitr"},
            new Object[]{"Eid al-Fitr (2nd Day)"},
            new Object[]{"Eid al-Fitr (3rd Day)"},
            new Object[]{"Eid al-Adha"},
            new Object[]{"Eid al-Adha (2nd Day)"},
            new Object[]{"Eid al-Adha (3rd Day)"},
            new Object[]{"Islamic New Year"},
            new Object[]{"Prophet's Birthday"},
            new Object[]{"Saudi National Day"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        boolean found = calendar.getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName()));
        assertTrue(found, "Calendar must contain holiday: " + holidayName);
    }

}
