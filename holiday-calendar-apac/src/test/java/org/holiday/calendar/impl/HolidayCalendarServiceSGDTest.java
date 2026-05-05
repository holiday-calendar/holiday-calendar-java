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

public class HolidayCalendarServiceSGDTest {

    private final HolidayCalendarServiceSGD service = new HolidayCalendarServiceSGD();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    // ── Identity ──────────────────────────────────────────────────────────────

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("SGD"));
        assertFalse(service.isProvided("SG"), "SGD service must not respond to the SG exchange code");
        assertFalse(service.isProvided("US"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "SGD");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Singapore (MAS/MEPS+) Holidays");
    }

    // ── ServiceLoader / factory path ──────────────────────────────────────────

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("SGD");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "SGD");
    }

    // ── noRoll() vs followingMonday() — the key behavioural difference ────────

    @Test
    public void testNoRoll_NewYearsDay2023() {
        // Jan 1, 2023 is Sunday.
        // SG (followingMonday) rolls to Jan 2.  SGD (noRoll) stays on Jan 1.
        HolidayCalendar sgCalendar  = new HolidayCalendarServiceSG().getHolidayCalendar();
        HolidayCalendar sgdCalendar = service.getHolidayCalendar();

        Optional<HolidayDate> sgNYD = findByName(sgCalendar.calculate(2023), "New Year's Day");
        Optional<HolidayDate> sgdNYD = findByName(sgdCalendar.calculate(2023), "New Year's Day");

        assertTrue(sgNYD.isPresent());
        assertTrue(sgdNYD.isPresent());
        assertEquals(sgNYD.get().getDate(),  LocalDate.of(2023, Month.JANUARY, 2),
                "SG must roll New Year's Day (Jan 1 Sun 2023) to Jan 2");
        assertEquals(sgdNYD.get().getDate(), LocalDate.of(2023, Month.JANUARY, 1),
                "SGD must NOT roll New Year's Day — noRoll() keeps it on Jan 1");
    }

    @Test
    public void testNoRoll_NationalDay2020() {
        // Aug 9, 2020 is Sunday.
        // SG rolls National Day to Aug 10.  SGD stays on Aug 9.
        HolidayCalendar sgCalendar  = new HolidayCalendarServiceSG().getHolidayCalendar();
        HolidayCalendar sgdCalendar = service.getHolidayCalendar();

        Optional<HolidayDate> sgND  = findByName(sgCalendar.calculate(2020), "National Day");
        Optional<HolidayDate> sgdND = findByName(sgdCalendar.calculate(2020), "National Day");

        assertTrue(sgND.isPresent());
        assertTrue(sgdND.isPresent());
        assertEquals(sgND.get().getDate(),  LocalDate.of(2020, Month.AUGUST, 10),
                "SG must roll National Day (Aug 9 Sun 2020) to Aug 10");
        assertEquals(sgdND.get().getDate(), LocalDate.of(2020, Month.AUGUST, 9),
                "SGD must NOT roll National Day — noRoll() keeps it on Aug 9");
    }

    // ── Holiday list completeness ─────────────────────────────────────────────

    @Test
    public void testCalculateAllHolidaysPresent2024() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);
        assertNotNull(holidays);
        assertEquals(holidays.size(), 11,
                "Expected all 11 SGD holidays for 2024, got: " + holidays.size());
    }

    @Test
    public void testChronologicalOrder() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2024);
        for (int i = 1; i < holidays.size(); i++) {
            assertFalse(holidays.get(i).getDate().isBefore(holidays.get(i - 1).getDate()),
                    "SGD holidays must be in chronological order");
        }
    }

    @DataProvider
    Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"New Year's Day"},
            new Object[]{"Chinese New Year (1st Day)"},
            new Object[]{"Chinese New Year (2nd Day)"},
            new Object[]{"Good Friday"},
            new Object[]{"Labour Day"},
            new Object[]{"Vesak Day"},
            new Object[]{"Hari Raya Puasa"},
            new Object[]{"National Day"},
            new Object[]{"Hari Raya Haji"},
            new Object[]{"Deepavali"},
            new Object[]{"Christmas Day"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames")
    public void testHolidayCalendarContains(String holidayName) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        boolean found = calendar.getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName()));
        assertTrue(found, "SGD calendar must contain holiday: " + holidayName);
    }

    // ── dataValidThrough ──────────────────────────────────────────────────────

    @Test
    public void testDataValidThroughReturnsPresent() {
        OptionalInt result = service.dataValidThrough();
        assertTrue(result.isPresent(),
                "SGD calendar has lookup-table holidays; must return a bounded year from dataValidThrough()");
    }

    @Test
    public void testDataValidThroughReturnedYear() {
        assertEquals(
                service.dataValidThrough().orElseThrow(() -> new RuntimeException("Expected present boundary year")),
                2055,
                "dataValidThrough() must return 2055 — ceiling of all four lookup-table observances");
    }

    @Test
    public void testDataValidThroughMatchesSG() {
        OptionalInt sgd = service.dataValidThrough();
        OptionalInt sg  = new HolidayCalendarServiceSG().dataValidThrough();
        assertEquals(sgd.getAsInt(), sg.getAsInt(),
                "SGD and SG must share the same dataValidThrough boundary year");
    }

    @Test
    public void testDataValidThroughViaFactory() {
        OptionalInt result = factory.dataValidThrough("SGD");
        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(),
                service.dataValidThrough().orElseThrow(() -> new RuntimeException("Expected present boundary year")),
                "factory.dataValidThrough(\"SGD\") must delegate to the service");
    }

    @Test
    public void testCalculateAtDataValidThroughReturnsAllHolidays() {
        int boundaryYear = service.dataValidThrough().orElseThrow(() -> new RuntimeException("Expected present boundary year"));
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(boundaryYear);
        assertFalse(holidays.isEmpty(),
                "calculate(" + boundaryYear + ") must return holidays — it is within the covered range");
        assertEquals(holidays.size(), 11,
                "Expected all 11 SGD holidays for boundary year " + boundaryYear);
    }

    @Test
    public void testCalculateBeyondDataValidThroughDropsLookupTableHolidays() {
        int boundaryYear = service.dataValidThrough().orElseThrow(() -> new RuntimeException("Expected present boundary year"));
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidaysAtBoundary = calendar.calculate(boundaryYear);
        List<HolidayDate> holidaysBeyond = calendar.calculate(boundaryYear + 1);
        assertTrue(holidaysBeyond.size() < holidaysAtBoundary.size(),
                "Year beyond dataValidThrough must produce fewer holidays (lookup tables exhausted); " +
                "at boundary: " + holidaysAtBoundary.size() + ", beyond: " + holidaysBeyond.size());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Optional<HolidayDate> findByName(List<HolidayDate> holidays, String name) {
        return holidays.stream()
                       .filter(hd -> name.equals(hd.getHoliday().getName()))
                       .findFirst();
    }
}
