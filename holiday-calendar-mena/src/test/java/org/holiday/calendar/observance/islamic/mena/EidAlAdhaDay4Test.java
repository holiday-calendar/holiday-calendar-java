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

package org.holiday.calendar.observance.islamic.mena;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public class EidAlAdhaDay4Test {

    @DataProvider
    Iterator<Object[]> knownDatesTR() {
        return List.of(
            new Object[]{2024, LocalDate.of(2024, 6, 19)},
            new Object[]{2025, LocalDate.of(2025, 6, 8)},
            new Object[]{2026, LocalDate.of(2026, 5, 29)},
            new Object[]{2055, LocalDate.of(2055, 7, 6)}
        ).iterator();
    }

    @Test(dataProvider = "knownDatesTR")
    public void testKnownDatesTR(int year, LocalDate expected) {
        assertEquals(new EidAlAdhaDay4("TR").apply(year), expected,
                "Eid al-Adha (4th Day) " + year + " must be Day 1 + 3 days");
    }

    @Test
    public void testDay4IsThreeDaysAfterDay1() {
        LocalDate day1 = new EidAlAdha("TR").apply(2025);
        LocalDate day4 = new EidAlAdhaDay4("TR").apply(2025);
        assertEquals(day4, day1.plusDays(3),
                "Eid al-Adha Day 4 must be exactly 3 days after Day 1");
    }

    @Test
    public void testCeilingYearReturnsDate() {
        assertNotNull(new EidAlAdhaDay4("TR").apply(EidAlAdha.DATA_VALID_THROUGH),
                "EidAlAdhaDay4 must return a date for the ceiling year 2055");
    }

    @Test
    public void testBeyondCeilingReturnsNull() {
        assertNull(new EidAlAdhaDay4("TR").apply(EidAlAdha.DATA_VALID_THROUGH + 1),
                "EidAlAdhaDay4 must return null for year beyond ceiling");
    }

    @Test
    public void testBelowFloorReturnsNull() {
        assertNull(new EidAlAdhaDay4("TR").apply(EidAlAdha.DATA_VALID_FROM - 1),
                "EidAlAdhaDay4 must return null for year below floor");
    }

    @Test
    public void testIsValidYearFalseForBeyondCeiling() {
        assertFalse(new EidAlAdhaDay4("TR").test(EidAlAdha.DATA_VALID_THROUGH + 1));
    }

    @Test
    public void testCaseInsensitiveCountryCode() {
        assertEquals(new EidAlAdhaDay4("tr").apply(2025), new EidAlAdhaDay4("TR").apply(2025));
    }
}
