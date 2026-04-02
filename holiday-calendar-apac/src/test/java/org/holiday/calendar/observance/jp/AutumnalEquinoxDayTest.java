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

package org.holiday.calendar.observance.jp;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public class AutumnalEquinoxDayTest {

    private final AutumnalEquinoxDay observance = new AutumnalEquinoxDay();

    @DataProvider
    Iterator<Object[]> knownDates() {
        return List.of(
            new Object[]{1980, LocalDate.of(1980, Month.SEPTEMBER, 23)},
            new Object[]{2000, LocalDate.of(2000, Month.SEPTEMBER, 23)},
            new Object[]{2024, LocalDate.of(2024, Month.SEPTEMBER, 22)},
            new Object[]{2025, LocalDate.of(2025, Month.SEPTEMBER, 23)},
            new Object[]{2044, LocalDate.of(2044, Month.SEPTEMBER, 22)}
        ).iterator();
    }

    @Test(dataProvider = "knownDates")
    public void testKnownDates(int year, LocalDate expected) {
        assertEquals(observance.apply(year), expected,
                     "Autumnal Equinox Day " + year + " should be " + expected);
    }

    @Test
    public void testInvalidYearBeforeRange() {
        assertNull(observance.apply(1979), "Should return null for year before 1980");
    }

    @Test
    public void testInvalidYearAfterRange() {
        assertNull(observance.apply(2100), "Should return null for year after 2099");
    }

    @Test
    public void testValidYearPredicate() {
        assertTrue(observance.test(2025));
        assertFalse(observance.test(1979));
        assertFalse(observance.test(2100));
    }
}
