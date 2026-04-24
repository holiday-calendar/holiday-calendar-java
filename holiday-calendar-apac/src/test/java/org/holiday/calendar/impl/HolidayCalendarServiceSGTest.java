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

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.testng.Assert.*;

public class HolidayCalendarServiceSGTest {

    private final HolidayCalendarServiceSG service = new HolidayCalendarServiceSG();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("SG"));
        assertFalse(service.isProvided("US"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "SG");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Singapore (SGX) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("SG");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "SG");
    }

    @Test
    public void testCalculate2024() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);
        assertNotNull(holidays);
        // 11 holidays defined; all lookup-table ones cover 2024, Time4J covers CNY
        assertTrue(holidays.size() >= 7, "Expected at least 7 holidays for 2024, got: " + holidays.size());
    }

    @Test
    public void testChronologicalOrder() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).getDate().isBefore(holidays.get(i - 1).getDate()), "Holidays should be in chronological order");
        }
    }

    @Test
    public void testChineseNewYear2024() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);

        Optional<HolidayDate> cny1 = holidays.stream()
                .filter(hd -> "Chinese New Year (1st Day)".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(cny1.isPresent(), "Chinese New Year 1st Day should be present for 2024");
        assertEquals(cny1.get().getDate(), LocalDate.of(2024, Month.FEBRUARY, 10),
                "Chinese New Year 1st Day 2024 should be Feb 10");

        Optional<HolidayDate> cny2 = holidays.stream()
                .filter(hd -> "Chinese New Year (2nd Day)".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(cny2.isPresent(), "Chinese New Year 2nd Day should be present for 2024");
        assertEquals(cny2.get().getDate(), LocalDate.of(2024, Month.FEBRUARY, 11),
                "Chinese New Year 2nd Day 2024 should be Feb 11");
    }

    @Test
    public void testGoodFriday2024() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);

        Optional<HolidayDate> goodFriday = holidays.stream()
                .filter(hd -> "Good Friday".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(goodFriday.isPresent(), "Good Friday should be present for 2024");
        assertEquals(goodFriday.get().getDate(), LocalDate.of(2024, Month.MARCH, 29),
                "Good Friday 2024 should be March 29");
    }

    @Test
    public void testDeepavali2024() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);

        Optional<HolidayDate> deepavali = holidays.stream()
                .filter(hd -> "Deepavali".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(deepavali.isPresent(), "Deepavali should be present for 2024");
        assertEquals(deepavali.get().getDate(), LocalDate.of(2024, Month.OCTOBER, 31),
                "Deepavali 2024 should be October 31");
    }

    @Test
    public void testNationalDayRoll() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        // National Day 2020: Aug 9 is Sunday -> rolls to Monday Aug 10
        List<HolidayDate> holidays2020 = calendar.calculate(2020);
        Optional<HolidayDate> nationalDay2020 = holidays2020.stream()
                .filter(hd -> "National Day".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(nationalDay2020.isPresent());
        assertEquals(nationalDay2020.get().getDate(), LocalDate.of(2020, Month.AUGUST, 10),
                "National Day 2020 (Aug 9, Sunday) should roll to Monday Aug 10");

        // National Day 2023: Aug 9 is Wednesday -> no roll
        List<HolidayDate> holidays2023 = calendar.calculate(2023);
        Optional<HolidayDate> nationalDay2023 = holidays2023.stream()
                .filter(hd -> "National Day".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(nationalDay2023.isPresent());
        assertEquals(nationalDay2023.get().getDate(), LocalDate.of(2023, Month.AUGUST, 9),
                "National Day 2023 (Aug 9, Wednesday) should not roll");
    }

    @Test
    public void testDataValidThroughReturnsPresent() {
        OptionalInt result = service.dataValidThrough();
        assertTrue(result.isPresent(),
            "SG calendar has lookup-table holidays; must return a bounded year from dataValidThrough()");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(service.dataValidThrough().orElseThrow(() -> new RuntimeException("Expected present boundary year")), 2055,
            "dataValidThrough() returns the minimum ceiling across all four lookup-table observances; " +
            "all four (VesakDay, HariRayaPuasa, HariRayaHaji, Deepavali) " +
            "now extend through 2055");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("SG");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), service.dataValidThrough().orElseThrow(() -> new RuntimeException("Expected present boundary year")),
            "factory.dataValidThrough(\"SG\") must delegate to the service and return the same year");
    }

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundaryYear = service.dataValidThrough().orElseThrow(() -> new RuntimeException("Expected present boundary year"));
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(boundaryYear);
        assertFalse(holidays.isEmpty(),
            "calculate(" + boundaryYear + ") must return holidays — it is within the covered range");
        assertEquals(holidays.size(), 11,
            "Expected all 11 SG holidays for boundary year " + boundaryYear);
    }

    @Test
    public void testCalculateBeyondDataValidThroughDropsLookupTableHolidays() {
        int boundaryYear = service.dataValidThrough().orElseThrow(() -> new RuntimeException("Expected present boundary year"));
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidaysAtBoundary = calendar.calculate(boundaryYear);
        // Must not throw — silent omission is the documented calculate() contract
        List<HolidayDate> holidaysBeyond = calendar.calculate(boundaryYear + 1);
        assertTrue(holidaysBeyond.size() < holidaysAtBoundary.size(),
            "Year beyond dataValidThrough should produce fewer holidays (lookup tables exhausted); " +
            "at boundary: " + holidaysAtBoundary.size() + ", beyond: " + holidaysBeyond.size());
    }

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"New Year's Day"},
            new Object[]{"Chinese New Year (1st Day)"},
            new Object[]{"Good Friday"},
            new Object[]{"Labour Day"},
            new Object[]{"National Day"},
            new Object[]{"Christmas Day"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        boolean found = calendar.getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName()));
        assertTrue(found, "Calendar should contain holiday: " + holidayName);
    }

    @Test
    public void testVesakDay2055PresentAndCorrect() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2055);
        Optional<HolidayDate> vesak = holidays.stream()
                .filter(hd -> "Vesak Day".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(vesak.isPresent(), "Vesak Day must be present for 2055");
        assertEquals(vesak.get().getDate(), LocalDate.of(2055, Month.MAY, 11),
                "Vesak Day 2055 should be May 11");
    }

    @Test
    public void testVesakDay2056AbsentSilently() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays2056 = calendar.calculate(2056);
        Optional<HolidayDate> vesak = holidays2056.stream()
                .filter(hd -> "Vesak Day".equals(hd.getHoliday().getName()))
                .findFirst();
        assertFalse(vesak.isPresent(),
                "Vesak Day must be silently absent for 2056 — beyond the table ceiling");
    }

    @Test
    public void testHariRayaHaji2055PresentAndCorrect() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2055);
        Optional<HolidayDate> hariRaya = holidays.stream()
                .filter(hd -> "Hari Raya Haji".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(hariRaya.isPresent(), "Hari Raya Haji must be present for 2055");
        assertEquals(hariRaya.get().getDate(), LocalDate.of(2055, Month.JULY, 4),
                "Hari Raya Haji 2055 should be July 4");
    }

    @Test
    public void testHariRayaHaji2056AbsentSilently() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays2056 = calendar.calculate(2056);
        Optional<HolidayDate> hariRaya = holidays2056.stream()
                .filter(hd -> "Hari Raya Haji".equals(hd.getHoliday().getName()))
                .findFirst();
        assertFalse(hariRaya.isPresent(),
                "Hari Raya Haji must be silently absent for 2056 — beyond the table ceiling");
    }

    @Test
    public void testHariRayaPuasa2055PresentAndCorrect() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2055);
        Optional<HolidayDate> hrp = holidays.stream()
                .filter(hd -> "Hari Raya Puasa".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(hrp.isPresent(), "Hari Raya Puasa must be present for 2055");
        assertEquals(hrp.get().getDate(), LocalDate.of(2055, Month.APRIL, 29),
                "Hari Raya Puasa 2055 should be April 29");
    }

    @Test
    public void testHariRayaPuasa2056AbsentSilently() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        Optional<HolidayDate> hrp = calendar.calculate(2056).stream()
                .filter(hd -> "Hari Raya Puasa".equals(hd.getHoliday().getName()))
                .findFirst();
        assertFalse(hrp.isPresent(),
                "Hari Raya Puasa must be silently absent for 2056 — beyond the table ceiling");
    }

    @Test
    public void testDeepavali2055PresentAndCorrect() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2055);
        Optional<HolidayDate> deepavali = holidays.stream()
                .filter(hd -> "Deepavali".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(deepavali.isPresent(), "Deepavali must be present for 2055");
        assertEquals(deepavali.get().getDate(), LocalDate.of(2055, Month.OCTOBER, 19),
                "Deepavali 2055 should be October 19");
    }

    @Test
    public void testDeepavali2056AbsentSilently() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        Optional<HolidayDate> deepavali = calendar.calculate(2056).stream()
                .filter(hd -> "Deepavali".equals(hd.getHoliday().getName()))
                .findFirst();
        assertFalse(deepavali.isPresent(),
                "Deepavali must be silently absent for 2056 — beyond the table ceiling");
    }

}
