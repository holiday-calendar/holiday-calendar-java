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
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;

public class HolidayCalendarServiceJPYTest {

    private final HolidayCalendarServiceJPY service = new HolidayCalendarServiceJPY();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("JPY"));
        assertFalse(service.isProvided("JP"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "JPY");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Japan (BOJ) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("JPY");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "JPY");
    }

    @Test
    public void testChronologicalOrder() {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(2025);
        for (int i = 1; i < holidays.size(); i++) {
            assertTrue(
                holidays.get(i).getDate().compareTo(holidays.get(i - 1).getDate()) >= 0,
                "Holidays should be in chronological order"
            );
        }
    }

    // ── BOJ-specific operational closures ─────────────────────────────────────

    @Test
    public void testYearStartJan2() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        Optional<HolidayDate> h = findByDate(holidays, LocalDate.of(2025, Month.JANUARY, 2));
        assertTrue(h.isPresent(), "Jan 2 should be a BOJ Year-Start Holiday");
        assertEquals(h.get().getHoliday().getName(), "Year-Start Holiday");
    }

    @Test
    public void testYearStartJan3() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        Optional<HolidayDate> h = findByDate(holidays, LocalDate.of(2025, Month.JANUARY, 3));
        assertTrue(h.isPresent(), "Jan 3 should be a BOJ Year-Start Holiday");
        assertEquals(h.get().getHoliday().getName(), "Year-Start Holiday");
    }

    @Test
    public void testYearEnd() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        Optional<HolidayDate> h = findByDate(holidays, LocalDate.of(2025, Month.DECEMBER, 31));
        assertTrue(h.isPresent(), "Dec 31 should be a BOJ Year-End Holiday");
        assertEquals(h.get().getHoliday().getName(), "Year-End Holiday");
    }

    @Test
    public void testBOJClosuresAreNotRollable() {
        // Jan 2 falls on Saturday in 2021 — should NOT roll (rollable=false)
        List<HolidayDate> holidays2021 = service.getHolidayCalendar().calculate(2021);
        // Jan 2 2021 is a Saturday; verify it is NOT present (non-rollable fixed)
        // Actually: the holiday IS on Jan 2 even if it's a Saturday — rollable=false means no roll
        Optional<HolidayDate> h = findByDate(holidays2021, LocalDate.of(2021, Month.JANUARY, 2));
        assertTrue(h.isPresent(), "Jan 2 BOJ closure should appear on the Saturday itself (no roll)");
    }

    // ── Inherited base holidays are present ───────────────────────────────────

    @Test
    public void testBaseHolidaysPresent2025() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertTrue(findByName(holidays, "New Year's Day").isPresent());
        assertTrue(findByName(holidays, "Constitution Memorial Day").isPresent());
        assertTrue(findByName(holidays, "Children's Day").isPresent());
        assertTrue(findByName(holidays, "Culture Day").isPresent());
        assertTrue(findByName(holidays, "Labour Thanksgiving Day").isPresent());
    }

    // ── Sandwiched-day rule also applies to JPY ────────────────────────────────

    @Test
    public void testSilverWeek2009() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2009);
        Optional<HolidayDate> sandwiched = holidays.stream()
                .filter(hd -> "National Holiday".equals(hd.getHoliday().getName())
                           && hd.getDate().equals(LocalDate.of(2009, Month.SEPTEMBER, 22)))
                .findFirst();
        assertTrue(sandwiched.isPresent(), "Sep 22, 2009 should be a National Holiday in JPY calendar");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Optional<HolidayDate> findByName(List<HolidayDate> holidays, String name) {
        return holidays.stream()
                       .filter(hd -> name.equals(hd.getHoliday().getName()))
                       .findFirst();
    }

    private Optional<HolidayDate> findByDate(List<HolidayDate> holidays, LocalDate date) {
        return holidays.stream()
                       .filter(hd -> date.equals(hd.getDate()))
                       .findFirst();
    }
}
