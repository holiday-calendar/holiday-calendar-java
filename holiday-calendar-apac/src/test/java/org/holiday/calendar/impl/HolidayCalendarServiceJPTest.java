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

public class HolidayCalendarServiceJPTest {

    private final HolidayCalendarServiceJP service = new HolidayCalendarServiceJP();
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setupFactory() {
        this.factory = new HolidayCalendarFactory();
    }

    @Test
    public void testIsProvided() {
        assertTrue(service.isProvided("JP"));
        assertFalse(service.isProvided("SG"));
    }

    @Test
    public void testGetCode() {
        assertEquals(service.getCode(), "JP");
    }

    @Test
    public void testGetRegion() {
        assertEquals(service.getRegion(), "Japan (TSE) Holidays");
    }

    @Test
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("JP");
        assertNotNull(calendar);
        assertEquals(calendar.getCode(), "JP");
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

    // ── Equinox dates ─────────────────────────────────────────────────────────

    @Test
    public void testVernalEquinox2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        Optional<HolidayDate> h = findByName(holidays, "Vernal Equinox Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2024, Month.MARCH, 20));
    }

    @Test
    public void testAutumnalEquinox2024() {
        // Sep 22, 2024 is Sunday → rolls to Monday Sep 23
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        Optional<HolidayDate> h = findByName(holidays, "Autumnal Equinox Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2024, Month.SEPTEMBER, 23));
    }

    // ── Emperor's Birthday era changes ────────────────────────────────────────

    @Test
    public void testEmperorsBirthdayShowa() {
        // Showa era: Emperor Hirohito, April 29
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(1985);
        Optional<HolidayDate> h = findByName(holidays, "Emperor's Birthday");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(1985, Month.APRIL, 29));
    }

    @Test
    public void testEmperorsBirthdayHeisei() {
        // Heisei era: Emperor Akihito, December 23
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2010);
        Optional<HolidayDate> h = findByName(holidays, "Emperor's Birthday");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2010, Month.DECEMBER, 23));
    }

    @Test
    public void testEmperorsBirthday2019Absent() {
        // 2019: no Emperor's Birthday (transition year)
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2019);
        Optional<HolidayDate> h = findByName(holidays, "Emperor's Birthday");
        assertFalse(h.isPresent(), "Emperor's Birthday should not appear in 2019");
    }

    @Test
    public void testEmperorsBirthdayReiwa() {
        // Feb 23, 2025 is Sunday → rolls to Monday Feb 24
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        Optional<HolidayDate> h = findByName(holidays, "Emperor's Birthday");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2025, Month.FEBRUARY, 24));
    }

    // ── Substitute holiday roll ────────────────────────────────────────────────

    @Test
    public void testNewYearsDayRoll2023() {
        // Jan 1 2023 is Sunday → rolls to Monday Jan 2
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2023);
        Optional<HolidayDate> h = findByName(holidays, "New Year's Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2023, Month.JANUARY, 2));
    }

    // ── Happy Monday floating holidays ────────────────────────────────────────

    @Test
    public void testComingOfAgeDayPreReform() {
        // 1999: fixed January 15
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(1999);
        Optional<HolidayDate> h = findByName(holidays, "Coming of Age Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(1999, Month.JANUARY, 15));
    }

    @Test
    public void testComingOfAgeDay2025() {
        // 2025: 2nd Monday in January = Jan 13
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        Optional<HolidayDate> h = findByName(holidays, "Coming of Age Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2025, Month.JANUARY, 13));
    }

    // ── Sandwiched-day rule ────────────────────────────────────────────────────

    @Test
    public void testSilverWeek2009() {
        // Sep 21 (Respect for the Aged Day) and Sep 23 (Autumnal Equinox) → Sep 22 is sandwiched
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2009);
        Optional<HolidayDate> sandwiched = holidays.stream()
                .filter(hd -> "National Holiday".equals(hd.getHoliday().getName())
                           && hd.getDate().equals(LocalDate.of(2009, Month.SEPTEMBER, 22)))
                .findFirst();
        assertTrue(sandwiched.isPresent(), "Sep 22, 2009 should be a National Holiday (Silver Week)");
    }

    @Test
    public void test2019ImperialTransition() {
        // Apr 30 (Abdication) and May 2 (sandwiched between Apr 30 and May 3) and May 1 (Enthronement)
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2019);
        assertTrue(findByDate(holidays, LocalDate.of(2019, Month.APRIL, 30)).isPresent(),
                   "Apr 30, 2019 (Abdication Day) should be a holiday");
        assertTrue(findByDate(holidays, LocalDate.of(2019, Month.MAY, 1)).isPresent(),
                   "May 1, 2019 (Enthronement Day) should be a holiday");
        assertTrue(findByDate(holidays, LocalDate.of(2019, Month.MAY, 2)).isPresent(),
                   "May 2, 2019 should be a sandwiched National Holiday");
    }

    // ── Year-bounded holidays ─────────────────────────────────────────────────

    @Test
    public void testMountainDayAbsentBefore2016() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2015);
        assertFalse(findByName(holidays, "Mountain Day").isPresent(),
                    "Mountain Day should not appear before 2016");
    }

    @Test
    public void testMountainDay2016() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2016);
        Optional<HolidayDate> h = findByName(holidays, "Mountain Day");
        assertTrue(h.isPresent());
        assertEquals(h.get().getDate(), LocalDate.of(2016, Month.AUGUST, 11));
    }

    @Test
    public void testMarineDayAbsentBefore1996() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(1995);
        assertFalse(findByName(holidays, "Marine Day").isPresent(),
                    "Marine Day should not appear before 1996");
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
