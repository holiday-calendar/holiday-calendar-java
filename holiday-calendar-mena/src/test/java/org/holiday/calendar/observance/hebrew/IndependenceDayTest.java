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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Tests for the Yom Ha'atzmaut (Israeli Independence Day) statutory postponement rule.
 *
 * <p>The rule (based on natural Iyar 5 day of week):
 * <ul>
 *   <li>Sunday  → observed Monday  (+1) — Iyar 4 would be Shabbat</li>
 *   <li>Monday  → observed Tuesday (+1) — Iyar 4 would be Sunday</li>
 *   <li>Tue/Wed/Thu → no shift</li>
 *   <li>Friday  → observed Thursday (−1) — Erev Shabbat</li>
 *   <li>Saturday → observed Thursday (−2) — Shabbat itself</li>
 * </ul>
 *
 * <p>All natural dates and official observed dates verified against
 * net.time4j.calendar.HebrewCalendar and confirmed against official Israeli
 * government calendar announcements.
 */
public class IndependenceDayTest {

    private final IndependenceDay observance = new IndependenceDay();

    // -------------------------------------------------------------------------
    // No-shift cases (Tuesday, Wednesday, Thursday)
    // -------------------------------------------------------------------------

    @Test
    public void testNoShiftWednesday2023() {
        // Iyar 5, 5783 = 2023-04-26 (Wednesday) → no shift
        assertEquals(LocalDate.of(2023, 4, 26).getDayOfWeek(), DayOfWeek.WEDNESDAY);
        assertEquals(observance.apply(2023), LocalDate.of(2023, 4, 26));
    }

    @Test
    public void testNoShiftWednesday2026() {
        // Iyar 5, 5786 = 2026-04-22 (Wednesday) → no shift
        assertEquals(LocalDate.of(2026, 4, 22).getDayOfWeek(), DayOfWeek.WEDNESDAY);
        assertEquals(observance.apply(2026), LocalDate.of(2026, 4, 22));
    }

    // -------------------------------------------------------------------------
    // Monday → Tuesday (+1): Iyar 4 would fall on Sunday
    // -------------------------------------------------------------------------

    @Test
    public void testShiftFromMonday2024() {
        // Iyar 5, 5784 = 2024-05-13 (Monday) → observed 2024-05-14 (Tuesday)
        // Confirmed: official Yom Ha'atzmaut 2024 = May 14
        assertEquals(LocalDate.of(2024, 5, 13).getDayOfWeek(), DayOfWeek.MONDAY,
                "Natural Iyar 5 5784 must be Monday");
        assertEquals(observance.apply(2024), LocalDate.of(2024, 5, 14),
                "Yom Ha'atzmaut 2024 (Iyar 5 = Monday) must shift to Tuesday May 14");
    }

    @Test
    public void testShiftFromMonday2028() {
        // Iyar 5, 5788 = 2028-05-01 (Monday) → observed 2028-05-02 (Tuesday)
        assertEquals(LocalDate.of(2028, 5, 1).getDayOfWeek(), DayOfWeek.MONDAY,
                "Natural Iyar 5 5788 must be Monday");
        assertEquals(observance.apply(2028), LocalDate.of(2028, 5, 2),
                "Yom Ha'atzmaut 2028 (Iyar 5 = Monday) must shift to Tuesday May 2");
    }

    // -------------------------------------------------------------------------
    // Friday → Thursday (−1)
    // -------------------------------------------------------------------------

    @Test
    public void testShiftFromFriday2022() {
        // Iyar 5, 5782 = 2022-05-06 (Friday) → observed 2022-05-05 (Thursday)
        // Confirmed: official Yom Ha'atzmaut 2022 = May 5
        assertEquals(LocalDate.of(2022, 5, 6).getDayOfWeek(), DayOfWeek.FRIDAY,
                "Natural Iyar 5 5782 must be Friday");
        assertEquals(observance.apply(2022), LocalDate.of(2022, 5, 5),
                "Yom Ha'atzmaut 2022 (Iyar 5 = Friday) must shift to Thursday May 5");
    }

    @Test
    public void testShiftFromFriday2029() {
        // Iyar 5, 5789 = 2029-04-20 (Friday) → observed 2029-04-19 (Thursday)
        assertEquals(LocalDate.of(2029, 4, 20).getDayOfWeek(), DayOfWeek.FRIDAY,
                "Natural Iyar 5 5789 must be Friday");
        assertEquals(observance.apply(2029), LocalDate.of(2029, 4, 19),
                "Yom Ha'atzmaut 2029 (Iyar 5 = Friday) must shift to Thursday April 19");
    }

    // -------------------------------------------------------------------------
    // Saturday → Thursday (−2)
    // -------------------------------------------------------------------------

    @Test
    public void testShiftFromSaturday2021() {
        // Iyar 5, 5781 = 2021-04-17 (Saturday) → observed 2021-04-15 (Thursday)
        // Confirmed: official Yom Ha'atzmaut 2021 = April 15
        assertEquals(LocalDate.of(2021, 4, 17).getDayOfWeek(), DayOfWeek.SATURDAY,
                "Natural Iyar 5 5781 must be Saturday");
        assertEquals(observance.apply(2021), LocalDate.of(2021, 4, 15),
                "Yom Ha'atzmaut 2021 (Iyar 5 = Saturday) must shift to Thursday April 15");
    }

    @Test
    public void testShiftFromSaturday2025() {
        // Iyar 5, 5785 = 2025-05-03 (Saturday) → observed 2025-05-01 (Thursday)
        assertEquals(LocalDate.of(2025, 5, 3).getDayOfWeek(), DayOfWeek.SATURDAY,
                "Natural Iyar 5 5785 must be Saturday");
        assertEquals(observance.apply(2025), LocalDate.of(2025, 5, 1),
                "Yom Ha'atzmaut 2025 (Iyar 5 = Saturday) must shift to Thursday May 1");
    }

    // -------------------------------------------------------------------------
    // Invariant: observed date is never Friday or Saturday (2020–2055)
    // -------------------------------------------------------------------------

    @Test
    public void testObservedDateNeverFallsOnWeekend() {
        for (int year = 2020; year <= 2055; year++) {
            LocalDate observed = observance.apply(year);
            assertNotNull(observed, "Independence Day must be non-null for year " + year);
            assertNotEquals(observed.getDayOfWeek(), DayOfWeek.FRIDAY,
                    "Independence Day must not fall on Friday in year " + year + " (got " + observed + ")");
            assertNotEquals(observed.getDayOfWeek(), DayOfWeek.SATURDAY,
                    "Independence Day must not fall on Saturday in year " + year + " (got " + observed + ")");
        }
    }

    // -------------------------------------------------------------------------
    // DataProvider-driven spot checks
    // -------------------------------------------------------------------------

    @DataProvider
    Iterator<Object[]> knownDates() {
        // natural Iyar 5 → official observed date (all confirmed against official Israeli records)
        return List.of(
            new Object[]{2019, LocalDate.of(2019, 5,  9)},  // Fri → Thu (-1); official confirmed
            new Object[]{2020, LocalDate.of(2020, 4, 29)},  // Wed → no shift
            new Object[]{2021, LocalDate.of(2021, 4, 15)},  // Sat → Thu (-2); official confirmed
            new Object[]{2022, LocalDate.of(2022, 5,  5)},  // Fri → Thu (-1); official confirmed
            new Object[]{2023, LocalDate.of(2023, 4, 26)},  // Wed → no shift
            new Object[]{2024, LocalDate.of(2024, 5, 14)},  // Mon → Tue (+1); official confirmed
            new Object[]{2025, LocalDate.of(2025, 5,  1)},  // Sat → Thu (-2)
            new Object[]{2026, LocalDate.of(2026, 4, 22)},  // Wed → no shift
            new Object[]{2028, LocalDate.of(2028, 5,  2)},  // Mon → Tue (+1)
            new Object[]{2029, LocalDate.of(2029, 4, 19)}   // Fri → Thu (-1)
        ).iterator();
    }

    @Test(dataProvider = "knownDates")
    public void testKnownDates(int year, LocalDate expected) {
        assertEquals(observance.apply(year), expected,
                "Independence Day " + year + " must be " + expected);
    }

    @Test
    public void testTestReturnsTrueForValidYear() {
        assertTrue(observance.test(2025));
    }

}
