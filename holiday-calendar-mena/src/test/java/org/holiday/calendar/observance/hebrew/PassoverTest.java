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

package org.holiday.calendar.observance.hebrew;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public class PassoverTest {

    @DataProvider
    Iterator<Object[]> knownDates() {
        // 15 Nisan: hebrewYear = gregorianYear + 3760
        // Dates verified via net.time4j.calendar.HebrewCalendar
        return List.of(
            new Object[]{2024, LocalDate.of(2024,  4, 23)},  // 15 Nisan 5784 = Tuesday
            new Object[]{2025, LocalDate.of(2025,  4, 13)},  // 15 Nisan 5785 = Sunday
            new Object[]{2026, LocalDate.of(2026,  4,  2)},  // 15 Nisan 5786 = Thursday
            new Object[]{2027, LocalDate.of(2027,  4, 22)},  // 15 Nisan 5787 = Thursday
            new Object[]{2028, LocalDate.of(2028,  4, 11)}   // 15 Nisan 5788 = Tuesday
        ).iterator();
    }

    @Test(dataProvider = "knownDates")
    public void testKnownDatesDay1(int year, LocalDate expected) {
        assertEquals(new Passover().apply(year), expected,
                "Passover Day 1 (15 Nisan) for " + year + " must be " + expected);
    }

    @DataProvider
    Iterator<Object[]> knownDatesEnd() {
        // 21 Nisan: always 6 days after 15 Nisan
        return List.of(
            new Object[]{2024, LocalDate.of(2024,  4, 29)},  // 21 Nisan 5784 = Monday
            new Object[]{2025, LocalDate.of(2025,  4, 19)},  // 21 Nisan 5785 = Saturday
            new Object[]{2026, LocalDate.of(2026,  4,  8)},  // 21 Nisan 5786 = Wednesday
            new Object[]{2027, LocalDate.of(2027,  4, 28)},  // 21 Nisan 5787 = Wednesday
            new Object[]{2028, LocalDate.of(2028,  4, 17)}   // 21 Nisan 5788 = Monday
        ).iterator();
    }

    @Test(dataProvider = "knownDatesEnd")
    public void testKnownDatesDay7(int year, LocalDate expected) {
        assertEquals(new PassoverEnd().apply(year), expected,
                "Passover Day 7 (21 Nisan) for " + year + " must be " + expected);
    }

    @Test
    public void testDay7AlwaysSixDaysAfterDay1() {
        for (int year = 2024; year <= 2035; year++) {
            LocalDate day1 = new Passover().apply(year);
            LocalDate day7 = new PassoverEnd().apply(year);
            assertNotNull(day1);
            assertNotNull(day7);
            assertEquals(day7, day1.plusDays(6),
                    "Passover Day 7 must be exactly 6 days after Day 1 (year " + year + ")");
        }
    }

    @Test
    public void testTestReturnsTrueForValidYear() {
        assertTrue(new Passover().test(2025));
    }

}
