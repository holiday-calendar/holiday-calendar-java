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
import org.holiday.calendar.HolidayDate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public class HolidayCalendarServiceCADTest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "CAD";

    public HolidayCalendarServiceCADTest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        final Object[] civicHoliday   = {"Civic Holiday"};
        final Object[] goodFriday     = {"Good Friday"};
        final Object[] ndtr           = {"National Day For Truth and Reconciliation"};
        final Object[] remembranceDay = {"Remembrance Day"};
        final Object[] boxingDay      = {"Boxing Day"};
        return Arrays.asList(civicHoliday, goodFriday, ndtr, remembranceDay, boxingDay).listIterator();
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayOccurrences() {
        // New Year's Day — following-Monday roll
        final Object[] newYear22   = {2022, "New Year's Day", LocalDate.of(2022, Month.JANUARY,  3)}; // Sat → Mon
        final Object[] newYear23   = {2023, "New Year's Day", LocalDate.of(2023, Month.JANUARY,  2)}; // Sun → Mon
        final Object[] newYear25   = {2025, "New Year's Day", LocalDate.of(2025, Month.JANUARY,  1)}; // Wed → no roll

        // Canada Day — following-Monday roll (note: 2023 Sat roll is absent from the CA calendar — this is correct)
        final Object[] canadaDay18 = {2018, "Canada Day", LocalDate.of(2018, Month.JULY,  2)}; // Sun → Mon
        final Object[] canadaDay23 = {2023, "Canada Day", LocalDate.of(2023, Month.JULY,  3)}; // Sat → Mon
        final Object[] canadaDay24 = {2024, "Canada Day", LocalDate.of(2024, Month.JULY,  1)}; // Mon → no roll

        // Remembrance Day — following-Monday roll for both Saturday and Sunday
        final Object[] remembrance18 = {2018, "Remembrance Day", LocalDate.of(2018, Month.NOVEMBER, 12)}; // Sun → Mon
        final Object[] remembrance23 = {2023, "Remembrance Day", LocalDate.of(2023, Month.NOVEMBER, 13)}; // Sat → Mon
        final Object[] remembrance24 = {2024, "Remembrance Day", LocalDate.of(2024, Month.NOVEMBER, 11)}; // Mon → no roll

        // Christmas / Boxing Day — 2021: Dec 25 = Sat, Dec 26 = Sun
        final Object[] christmas21 = {2021, "Christmas Day", LocalDate.of(2021, Month.DECEMBER, 27)}; // Sat → Mon
        final Object[] boxingDay21 = {2021, "Boxing Day",    LocalDate.of(2021, Month.DECEMBER, 28)}; // Sun (displaced by Christmas) → Tue

        // Christmas / Boxing Day — 2022: Dec 25 = Sun, Dec 26 = Mon (consumed by Christmas roll)
        final Object[] christmas22 = {2022, "Christmas Day", LocalDate.of(2022, Month.DECEMBER, 26)}; // Sun → Mon
        final Object[] boxingDay22 = {2022, "Boxing Day",    LocalDate.of(2022, Month.DECEMBER, 27)}; // Mon taken by Christmas → Tue

        // Christmas / Boxing Day — 2023: Dec 25 = Mon, Dec 26 = Tue (no roll)
        final Object[] christmas23 = {2023, "Christmas Day", LocalDate.of(2023, Month.DECEMBER, 25)};
        final Object[] boxingDay23 = {2023, "Boxing Day",    LocalDate.of(2023, Month.DECEMBER, 26)};

        // Christmas / Boxing Day — 2026: Dec 25 = Fri, Dec 26 = Sat
        final Object[] christmas26 = {2026, "Christmas Day", LocalDate.of(2026, Month.DECEMBER, 25)}; // Fri → no roll
        final Object[] boxingDay26 = {2026, "Boxing Day",    LocalDate.of(2026, Month.DECEMBER, 28)}; // Sat → Mon

        // Good Friday (always a Friday — no roll ever applies)
        final Object[] goodFriday22 = {2022, "Good Friday", LocalDate.of(2022, Month.APRIL, 15)};
        final Object[] goodFriday24 = {2024, "Good Friday", LocalDate.of(2024, Month.MARCH, 29)};

        // Family Day (3rd Monday in February — no roll ever applies)
        final Object[] familyDay21  = {2021, "Family Day", LocalDate.of(2021, Month.FEBRUARY, 15)};
        final Object[] familyDay22  = {2022, "Family Day", LocalDate.of(2022, Month.FEBRUARY, 21)};

        // Victoria Day (last Monday on or before May 25 — no roll ever applies)
        final Object[] victoriaDay21 = {2021, "Victoria Day", LocalDate.of(2021, Month.MAY, 24)};
        final Object[] victoriaDay22 = {2022, "Victoria Day", LocalDate.of(2022, Month.MAY, 23)};
        // Edge case: May 25, 2026 is a Monday — VictoriaDay must be the week before (May 18)
        final Object[] victoriaDay26 = {2026, "Victoria Day", LocalDate.of(2026, Month.MAY, 18)};

        // Civic Holiday (1st Monday in August — no roll ever applies)
        final Object[] civicHoliday21 = {2021, "Civic Holiday", LocalDate.of(2021, Month.AUGUST, 2)};
        final Object[] civicHoliday22 = {2022, "Civic Holiday", LocalDate.of(2022, Month.AUGUST, 1)};

        // Labour Day (1st Monday in September — no roll ever applies)
        final Object[] labourDay21  = {2021, "Labour Day", LocalDate.of(2021, Month.SEPTEMBER,  6)};
        final Object[] labourDay25  = {2025, "Labour Day", LocalDate.of(2025, Month.SEPTEMBER,  1)}; // Sep 1 is itself Monday

        // Thanksgiving Day (2nd Monday in October — no roll ever applies)
        final Object[] thanksgiving21 = {2021, "Thanksgiving Day", LocalDate.of(2021, Month.OCTOBER, 11)};
        final Object[] thanksgiving22 = {2022, "Thanksgiving Day", LocalDate.of(2022, Month.OCTOBER, 10)};

        // National Day for Truth and Reconciliation — rollable
        final Object[] ndtr21 = {2021, "National Day For Truth and Reconciliation",
                                  LocalDate.of(2021, Month.SEPTEMBER, 30)}; // Thu → no roll (first observed year)
        final Object[] ndtr23 = {2023, "National Day For Truth and Reconciliation",
                                  LocalDate.of(2023, Month.OCTOBER,    2)}; // Sat → Mon (confirmed Bank of Canada schedule)

        return Arrays.asList(
                newYear22, newYear23, newYear25,
                canadaDay18, canadaDay23, canadaDay24,
                remembrance18, remembrance23, remembrance24,
                christmas21, boxingDay21,
                christmas22, boxingDay22,
                christmas23, boxingDay23,
                christmas26, boxingDay26,
                goodFriday22, goodFriday24,
                familyDay21, familyDay22,
                victoriaDay21, victoriaDay22, victoriaDay26,
                civicHoliday21, civicHoliday22,
                labourDay21, labourDay25,
                thanksgiving21, thanksgiving22,
                ndtr21, ndtr23
        ).listIterator();
    }

    @Test(dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testHolidayCount() {
        final HolidayCalendar calendar = factory.create(CODE);
        assertEquals(calendar.getHolidays().size(), 12,
                "CAD calendar must define exactly 12 Bank of Canada Lynx closure days");
    }

    @Test(dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testEasterMondayAbsent() {
        final HolidayCalendar calendar = factory.create(CODE);
        final boolean defined = calendar.getHolidays().stream()
                .anyMatch(h -> "Easter Monday".equals(h.getName()));
        assertFalse(defined,
                "CAD calendar must not contain Easter Monday — it is not a Bank Act statutory holiday");
    }

    @Test
    public void testNdtrAbsentBefore2021() {
        final HolidayCalendar calendar = factory.create(CODE);
        for (final int year : new int[]{2019, 2020}) {
            final boolean present = calendar.calculate(year).stream()
                    .anyMatch(hd -> "National Day For Truth and Reconciliation"
                            .equals(hd.getHoliday().getName()));
            assertFalse(present,
                    "NDTR must not appear in the CAD calendar for " + year + " (first observed 2021)");
        }
    }

    @Test
    public void testNdtrPresentFrom2021() {
        final HolidayCalendar calendar = factory.create(CODE);
        for (final int year : new int[]{2021, 2022, 2023, 2024}) {
            final boolean present = calendar.calculate(year).stream()
                    .anyMatch(hd -> "National Day For Truth and Reconciliation"
                            .equals(hd.getHoliday().getName()));
            assertTrue(present,
                    "NDTR must appear in the CAD calendar for year " + year);
        }
    }

    @Test
    public void testCalculatedHolidayCount() {
        final HolidayCalendar calendar = factory.create(CODE);
        // Pre-2021: NDTR absent → 11 holidays
        assertEquals(calendar.calculate(2019).size(), 11,
                "CAD calendar should produce 11 holidays for 2019 (NDTR absent)");
        assertEquals(calendar.calculate(2020).size(), 11,
                "CAD calendar should produce 11 holidays for 2020 (NDTR absent)");
        // 2021+: NDTR present → 12 holidays
        assertEquals(calendar.calculate(2021).size(), 12,
                "CAD calendar should produce 12 holidays for 2021");
        assertEquals(calendar.calculate(2022).size(), 12,
                "CAD calendar should produce 12 holidays for 2022");
        assertEquals(calendar.calculate(2023).size(), 12,
                "CAD calendar should produce 12 holidays for 2023");
    }

    @Test
    public void testChronologicalOrder() {
        final HolidayCalendar calendar = factory.create(CODE);
        for (final int year : new int[]{2018, 2021, 2022, 2023, 2024, 2025, 2026}) {
            final List<HolidayDate> holidays = calendar.calculate(year);
            assertFalse(holidays.isEmpty(), "Expected non-empty holiday list for " + year);
            for (int i = 1; i < holidays.size(); i++) {
                assertTrue(
                        holidays.get(i).getDate().compareTo(holidays.get(i - 1).getDate()) >= 0,
                        "Holidays out of chronological order for " + year
                                + ": " + holidays.get(i - 1).getDate()
                                + " > " + holidays.get(i).getDate());
            }
        }
    }

}