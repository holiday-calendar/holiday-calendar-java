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

public class ShavuotTest {

    @DataProvider
    Iterator<Object[]> knownDates() {
        // 6 Sivan: hebrewYear = gregorianYear + 3760
        // Dates verified via net.time4j.calendar.HebrewCalendar
        return List.of(
            new Object[]{2024, LocalDate.of(2024,  6, 12)},  // 6 Sivan 5784 = Wednesday
            new Object[]{2025, LocalDate.of(2025,  6,  2)},  // 6 Sivan 5785 = Monday
            new Object[]{2026, LocalDate.of(2026,  5, 22)},  // 6 Sivan 5786 = Friday
            new Object[]{2027, LocalDate.of(2027,  6, 11)},  // 6 Sivan 5787 = Friday
            new Object[]{2028, LocalDate.of(2028,  5, 31)}   // 6 Sivan 5788 = Wednesday
        ).iterator();
    }

    @Test(dataProvider = "knownDates")
    public void testKnownDates(int year, LocalDate expected) {
        assertEquals(new Shavuot().apply(year), expected,
                "Shavuot (6 Sivan) for " + year + " must be " + expected);
    }

    @Test
    public void testTestReturnsTrueForValidYear() {
        assertTrue(new Shavuot().test(2025));
    }

}
