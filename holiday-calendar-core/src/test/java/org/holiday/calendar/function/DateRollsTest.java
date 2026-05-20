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

package org.holiday.calendar.function;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;

public class DateRollsTest {

    // ── sundayToMonday ────────────────────────────────────────────────────────

    @Test
    public void testSundayToMonday_Saturday_NoRoll() {
        // Jan 1 2028 is Saturday — must stay unchanged
        LocalDate sat = LocalDate.of(2028, 1, 1);
        assertEquals(DateRolls.sundayToMonday().rollToObservedDate(sat), sat);
    }

    @Test
    public void testSundayToMonday_Sunday_RollsToMonday() {
        // Feb 11 2029 is Sunday → Feb 12 2029 (Monday)
        LocalDate sun = LocalDate.of(2029, 2, 11);
        assertEquals(DateRolls.sundayToMonday().rollToObservedDate(sun), LocalDate.of(2029, 2, 12));
    }

    @DataProvider(name = "weekdays")
    public Object[][] weekdays() {
        return new Object[][] {
            { LocalDate.of(2025, 1, 6)  },  // Monday
            { LocalDate.of(2025, 1, 7)  },  // Tuesday
            { LocalDate.of(2025, 1, 8)  },  // Wednesday
            { LocalDate.of(2025, 1, 9)  },  // Thursday
            { LocalDate.of(2025, 1, 10) },  // Friday
        };
    }

    @Test(dataProvider = "weekdays")
    public void testSundayToMonday_Weekday_NoRoll(LocalDate weekday) {
        assertEquals(DateRolls.sundayToMonday().rollToObservedDate(weekday), weekday);
    }

    // ── regression guards for existing strategies ─────────────────────────────

    @Test
    public void testFollowingMonday_Saturday_RollsToMonday() {
        // Jan 1 2028 (Sat) → Jan 3 2028 (Mon)
        LocalDate sat = LocalDate.of(2028, 1, 1);
        assertEquals(DateRolls.followingMonday().rollToObservedDate(sat), LocalDate.of(2028, 1, 3));
    }

    @Test
    public void testFollowingMonday_Sunday_RollsToMonday() {
        // Feb 11 2029 (Sun) → Feb 12 2029 (Mon)
        LocalDate sun = LocalDate.of(2029, 2, 11);
        assertEquals(DateRolls.followingMonday().rollToObservedDate(sun), LocalDate.of(2029, 2, 12));
    }

    @Test
    public void testPreviousFridayOrFollowingMonday_Saturday_RollsToFriday() {
        // Jan 1 2028 (Sat) → Dec 31 2027 (Fri)
        LocalDate sat = LocalDate.of(2028, 1, 1);
        assertEquals(DateRolls.previousFridayOrFollowingMonday().rollToObservedDate(sat), LocalDate.of(2027, 12, 31));
    }

    @Test
    public void testPreviousFridayOrFollowingMonday_Sunday_RollsToMonday() {
        // Feb 11 2029 (Sun) → Feb 12 2029 (Mon)
        LocalDate sun = LocalDate.of(2029, 2, 11);
        assertEquals(DateRolls.previousFridayOrFollowingMonday().rollToObservedDate(sun), LocalDate.of(2029, 2, 12));
    }

    @Test
    public void testNoRoll_SaturdayUnchanged() {
        LocalDate sat = LocalDate.of(2028, 1, 1);
        assertEquals(DateRolls.noRoll().rollToObservedDate(sat), sat);
    }

    // ── followingSunday ───────────────────────────────────────────────────────

    @Test
    public void testFollowingSunday_Friday_RollsToSunday() {
        // Jan 3 2025 is Friday → Jan 5 2025 (Sunday)
        LocalDate fri = LocalDate.of(2025, 1, 3);
        assertEquals(DateRolls.followingSunday().rollToObservedDate(fri), LocalDate.of(2025, 1, 5));
    }

    @Test
    public void testFollowingSunday_Saturday_RollsToSunday() {
        // Jan 4 2025 is Saturday → Jan 5 2025 (Sunday)
        LocalDate sat = LocalDate.of(2025, 1, 4);
        assertEquals(DateRolls.followingSunday().rollToObservedDate(sat), LocalDate.of(2025, 1, 5));
    }

    @DataProvider(name = "gccWorkdays")
    public Object[][] gccWorkdays() {
        return new Object[][] {
            { LocalDate.of(2025, 1, 5)  },  // Sunday
            { LocalDate.of(2025, 1, 6)  },  // Monday
            { LocalDate.of(2025, 1, 7)  },  // Tuesday
            { LocalDate.of(2025, 1, 8)  },  // Wednesday
            { LocalDate.of(2025, 1, 9)  },  // Thursday
        };
    }

    @Test(dataProvider = "gccWorkdays")
    public void testFollowingSunday_Workday_NoRoll(LocalDate workday) {
        assertEquals(DateRolls.followingSunday().rollToObservedDate(workday), workday);
    }

    // ── previousThursdayOrFollowingSunday ─────────────────────────────────────
    // Qatar Amiri Diwan rule: Friday → preceding Thursday; Saturday → following Sunday

    @Test
    public void testPreviousThursdayOrFollowingSunday_Friday_RollsToThursday() {
        // Dec 18, 2020 is Friday → Dec 17, 2020 (Thursday); confirmed Amiri Diwan
        LocalDate fri = LocalDate.of(2020, 12, 18);
        assertEquals(DateRolls.previousThursdayOrFollowingSunday().rollToObservedDate(fri),
                LocalDate.of(2020, 12, 17));
    }

    @Test
    public void testPreviousThursdayOrFollowingSunday_Saturday_RollsToSunday() {
        // Dec 18, 2021 is Saturday → Dec 19, 2021 (Sunday); confirmed Amiri Diwan
        LocalDate sat = LocalDate.of(2021, 12, 18);
        assertEquals(DateRolls.previousThursdayOrFollowingSunday().rollToObservedDate(sat),
                LocalDate.of(2021, 12, 19));
    }

    @DataProvider(name = "qatarWorkdays")
    public Object[][] qatarWorkdays() {
        return new Object[][] {
            { LocalDate.of(2025, 1, 5)  },  // Sunday
            { LocalDate.of(2025, 1, 6)  },  // Monday
            { LocalDate.of(2025, 1, 7)  },  // Tuesday
            { LocalDate.of(2025, 1, 8)  },  // Wednesday
            { LocalDate.of(2025, 1, 9)  },  // Thursday
        };
    }

    @Test(dataProvider = "qatarWorkdays")
    public void testPreviousThursdayOrFollowingSunday_Workday_NoRoll(LocalDate workday) {
        assertEquals(DateRolls.previousThursdayOrFollowingSunday().rollToObservedDate(workday), workday);
    }
}
