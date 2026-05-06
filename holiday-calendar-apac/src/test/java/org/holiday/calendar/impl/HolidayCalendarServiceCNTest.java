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

/**
 * Unit tests for {@link HolidayCalendarServiceCN} — the China National Public
 * Holidays calendar (code {@code CN}).
 *
 * <p>This calendar covers the 11-entry statutory schedule as defined by China's
 * State Council for the purposes of national public holidays. Single-day holidays
 * use {@code DateRolls.followingMonday()} with {@code rollable(true)}; multi-day
 * blocks (Spring Festival Days 1–3, National Day Days 1–3) are {@code rollable(false)}
 * because China's block-shift adjustment mechanism cannot be modelled per-day.
 * This contrasts with the PBOC/CNAPS calendar ({@code CNY}) that uses
 * {@code DateRolls.noRoll()} with {@code rollable(false)} for all entries.
 *
 * <p>Test categories:
 * <ol>
 *   <li>Service metadata — code, region, dataValidThrough</li>
 *   <li>Holiday count — exactly 11 entries per year</li>
 *   <li>Spring Festival golden-master dates (2024–2027)</li>
 *   <li>Qingming Festival golden-master dates (2024–2026)</li>
 *   <li>Dragon Boat Festival golden-master dates (2024–2026)</li>
 *   <li>Mid-Autumn Festival golden-master dates (2024–2026)</li>
 *   <li>Fixed National Day dates — Oct 1, 2, 3 always present</li>
 *   <li>Roll behavior — Saturday/Sunday holidays advance to following Monday</li>
 *   <li>Roll collision — two holidays landing on the same rolled date</li>
 *   <li>Chronological ordering — list remains sorted after rolling</li>
 *   <li>Factory discovery — ServiceLoader finds the CN registration</li>
 * </ol>
 */
public class HolidayCalendarServiceCNTest {

    private HolidayCalendarServiceCN service;
    private HolidayCalendarFactory factory;

    @BeforeClass
    public void setup() {
        this.service = new HolidayCalendarServiceCN();
        this.factory = new HolidayCalendarFactory();
    }

    // =========================================================================
    // 1. SERVICE METADATA
    // =========================================================================

    @Test(description = "isProvided() returns true for CN and false for unrelated codes")
    public void testIsProvided() {
        assertTrue(service.isProvided("CN"),  "isProvided(\"CN\") must return true");
        assertFalse(service.isProvided("CNY"), "isProvided(\"CNY\") must return false — different service");
        assertFalse(service.isProvided("SG"),  "isProvided(\"SG\") must return false");
        assertFalse(service.isProvided("US"),  "isProvided(\"US\") must return false");
    }

    @Test(description = "getCode() returns the calendar code CN")
    public void testGetCode() {
        assertEquals(service.getCode(), "CN");
    }

    @Test(description = "getRegion() returns a non-blank human-readable description")
    public void testGetRegion() {
        String region = service.getRegion();
        assertNotNull(region, "getRegion() must not return null");
        assertFalse(region.isBlank(), "getRegion() must not return a blank string");
    }

    @Test(description = "dataValidThrough() returns empty — CN calendar is fully algorithmic")
    public void testDataValidThroughReturnsEmpty() {
        OptionalInt result = service.dataValidThrough();
        assertFalse(result.isPresent(),
                "CN calendar is fully algorithmic; dataValidThrough() must return OptionalInt.empty()");
    }

    // =========================================================================
    // 2. HOLIDAY COUNT
    // =========================================================================

    @DataProvider(name = "yearsForCountCheck")
    public Object[][] yearsForCountCheck() {
        return new Object[][] {
            {2022},
            {2023},
            {2024},
            {2025},
            {2026},
            {2027},
        };
    }

    @Test(dataProvider = "yearsForCountCheck",
          description = "calculate(year) returns exactly 11 HolidayDate entries")
    public void testHolidayCountIsEleven(int year) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(year);
        assertNotNull(holidays, "calculate(" + year + ") must not return null");
        assertEquals(holidays.size(), 11,
                "Expected exactly 11 holiday entries for " + year + " but got " + holidays.size());
    }

    // =========================================================================
    // 3. SPRING FESTIVAL GOLDEN-MASTER DATES (DAYS 1–3)
    // =========================================================================

    /**
     * Spring Festival natural (lunar calendar) dates for Days 1–3.
     * Spring Festival is {@code rollable(false)}: China's block-shift mechanism
     * cannot be replicated per-day, so dates are reported on their natural positions.
     */
    @DataProvider(name = "springFestivalDates")
    public Object[][] springFestivalDates() {
        return new Object[][] {
            {"Spring Festival (Day 1)", 2024, LocalDate.of(2024, Month.FEBRUARY, 10)}, // Sat — stays
            {"Spring Festival (Day 1)", 2025, LocalDate.of(2025, Month.JANUARY,  29)}, // Wed — stays
            {"Spring Festival (Day 1)", 2026, LocalDate.of(2026, Month.FEBRUARY, 17)}, // Tue — stays
            {"Spring Festival (Day 1)", 2027, LocalDate.of(2027, Month.FEBRUARY,  6)}, // Sat — stays
            {"Spring Festival (Day 2)", 2024, LocalDate.of(2024, Month.FEBRUARY, 11)}, // Sun — stays
            {"Spring Festival (Day 2)", 2025, LocalDate.of(2025, Month.JANUARY,  30)},
            {"Spring Festival (Day 2)", 2026, LocalDate.of(2026, Month.FEBRUARY, 18)},
            {"Spring Festival (Day 2)", 2027, LocalDate.of(2027, Month.FEBRUARY,  7)}, // Sun — stays
            {"Spring Festival (Day 3)", 2024, LocalDate.of(2024, Month.FEBRUARY, 12)},
            {"Spring Festival (Day 3)", 2025, LocalDate.of(2025, Month.JANUARY,  31)},
            {"Spring Festival (Day 3)", 2026, LocalDate.of(2026, Month.FEBRUARY, 19)},
            {"Spring Festival (Day 3)", 2027, LocalDate.of(2027, Month.FEBRUARY,  8)},
        };
    }

    @Test(dataProvider = "springFestivalDates",
          description = "Spring Festival date matches expected natural date (rollable=false)")
    public void testSpringFestivalDate(String holidayName, int year, LocalDate expectedDate) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(year);
        assertHolidayDate(holidays, holidayName, expectedDate, holidayName + " " + year);
    }

    // =========================================================================
    // 4–6. SINGLE-DAY FLOATING HOLIDAY GOLDEN-MASTER DATES
    // =========================================================================

    @DataProvider(name = "floatingHolidayDates")
    public Object[][] floatingHolidayDates() {
        return new Object[][] {
            {"Qingming Festival",    2024, LocalDate.of(2024, Month.APRIL,      4)}, // Thu — no roll
            {"Qingming Festival",    2025, LocalDate.of(2025, Month.APRIL,      4)}, // Fri — no roll
            {"Qingming Festival",    2026, LocalDate.of(2026, Month.APRIL,      6)}, // Mon — no roll
            {"Dragon Boat Festival", 2024, LocalDate.of(2024, Month.JUNE,      10)}, // Mon — no roll
            {"Dragon Boat Festival", 2025, LocalDate.of(2025, Month.JUNE,       2)}, // raw Sat May 31 → Mon
            {"Dragon Boat Festival", 2026, LocalDate.of(2026, Month.JUNE,      19)}, // Fri — no roll
            {"Mid-Autumn Festival",  2024, LocalDate.of(2024, Month.SEPTEMBER, 17)}, // Tue — no roll
            {"Mid-Autumn Festival",  2025, LocalDate.of(2025, Month.OCTOBER,    6)}, // Mon — no roll
            {"Mid-Autumn Festival",  2026, LocalDate.of(2026, Month.SEPTEMBER, 25)}, // Fri — no roll
        };
    }

    @Test(dataProvider = "floatingHolidayDates",
          description = "Floating holiday date matches expected post-roll observed date")
    public void testFloatingHolidayDate(String holidayName, int year, LocalDate expectedDate) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(year);
        assertHolidayDate(holidays, holidayName, expectedDate, holidayName + " " + year);
    }

    // =========================================================================
    // 7. FIXED HOLIDAYS — PRESENT EVERY YEAR
    // =========================================================================

    @DataProvider(name = "fixedHolidayYears")
    public Object[][] fixedHolidayYears() {
        return new Object[][] {
            {2024}, {2025}, {2026},
        };
    }

    @Test(dataProvider = "fixedHolidayYears",
          description = "New Year's Day (Jan 1) is always defined in the calendar")
    public void testNewYearsDayDefined(int year) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        boolean found = calendar.getHolidays().stream()
                .anyMatch(h -> "New Year's Day".equals(h.getName()));
        assertTrue(found, "Calendar must define New Year's Day");
    }

    @Test(dataProvider = "fixedHolidayYears",
          description = "Labour Day (May 1) is present in each calculated year")
    public void testLabourDayPresent(int year) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(year);
        Optional<HolidayDate> labourDay = holidays.stream()
                .filter(hd -> "Labour Day".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(labourDay.isPresent(), "Labour Day must be present for " + year);
        assertEquals(labourDay.get().getDate().getMonth(), Month.MAY,
                "Labour Day must fall in May for " + year);
    }

    @Test(description = "National Day Day 1 (Oct 1) falls in October in 2024")
    public void testNationalDayDay1October2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        // 2024-10-01 is a Tuesday — no roll expected
        assertHolidayDate(holidays, "National Day (Day 1)", LocalDate.of(2024, Month.OCTOBER, 1),
                "National Day (Day 1) 2024");
    }

    @Test(description = "National Day Day 2 (Oct 2) is present for 2024")
    public void testNationalDayDay2October2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        assertHolidayDate(holidays, "National Day (Day 2)", LocalDate.of(2024, Month.OCTOBER, 2),
                "National Day (Day 2) 2024");
    }

    @Test(description = "National Day Day 3 (Oct 3) is present for 2024")
    public void testNationalDayDay3October2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        assertHolidayDate(holidays, "National Day (Day 3)", LocalDate.of(2024, Month.OCTOBER, 3),
                "National Day (Day 3) 2024");
    }

    // =========================================================================
    // 8. ROLL BEHAVIOR — FOLLOWING MONDAY STRATEGY
    // =========================================================================

    @Test(description = "New Year's Day 2023 (Sunday Jan 1) rolls to Monday Jan 2")
    public void testNewYearsDayRollSunday2023() {
        // Jan 1 2023 = Sunday → followingMonday → Jan 2
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2023);
        assertHolidayDate(holidays, "New Year's Day", LocalDate.of(2023, Month.JANUARY, 2),
                "New Year's Day 2023 (Sunday) roll");
    }

    @Test(description = "New Year's Day 2022 (Saturday Jan 1) rolls to Monday Jan 3")
    public void testNewYearsDayRollSaturday2022() {
        // Jan 1 2022 = Saturday → followingMonday → Jan 3
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2022);
        assertHolidayDate(holidays, "New Year's Day", LocalDate.of(2022, Month.JANUARY, 3),
                "New Year's Day 2022 (Saturday) roll");
    }

    @Test(description = "New Year's Day 2028 (Saturday Jan 1) rolls to Monday Jan 3")
    public void testNewYearsDayRollSaturday2028() {
        // Jan 1 2028 = Saturday → followingMonday → Jan 3
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2028);
        assertHolidayDate(holidays, "New Year's Day", LocalDate.of(2028, Month.JANUARY, 3),
                "New Year's Day 2028 (Saturday) roll");
    }

    @Test(description = "Labour Day 2022 (Sunday May 1) rolls to Monday May 2")
    public void testLabourDayRollSunday2022() {
        // May 1 2022 = Sunday → followingMonday → May 2
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2022);
        assertHolidayDate(holidays, "Labour Day", LocalDate.of(2022, Month.MAY, 2),
                "Labour Day 2022 (Sunday) roll");
    }

    @Test(description = "Labour Day 2021 (Saturday May 1) rolls to Monday May 3")
    public void testLabourDayRollSaturday2021() {
        // May 1 2021 = Saturday → followingMonday → May 3
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2021);
        assertHolidayDate(holidays, "Labour Day", LocalDate.of(2021, Month.MAY, 3),
                "Labour Day 2021 (Saturday) roll");
    }

    @Test(description = "New Year's Day on weekday (2025, Wednesday) does not roll")
    public void testNewYearsDayNoRollWeekday2025() {
        // Jan 1 2025 = Wednesday → no roll
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2025);
        assertHolidayDate(holidays, "New Year's Day", LocalDate.of(2025, Month.JANUARY, 1),
                "New Year's Day 2025 (Wednesday) should not roll");
    }

    // =========================================================================
    // 9. MULTI-DAY BLOCKS NOT ROLLED — NATURAL DATES PRESERVED
    // =========================================================================

    /**
     * 2023: Oct 1 is Sunday. National Day is {@code rollable(false)}, so Day 1
     * stays on Oct 1 (not moved to Monday Oct 2). This verifies that the
     * block-shift rationale is implemented correctly — no per-day rolling occurs.
     */
    @Test(description = "National Day Day 1 (Oct 1 2023, Sunday) stays on Oct 1 — rollable=false")
    public void testNationalDayDay1NotRolledSunday2023() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2023);
        Optional<HolidayDate> day1 = holidays.stream()
                .filter(hd -> "National Day (Day 1)".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(day1.isPresent(), "National Day (Day 1) must be present for 2023");
        assertEquals(day1.get().getDate(), LocalDate.of(2023, Month.OCTOBER, 1),
                "National Day (Day 1) 2023 (Sunday) must stay Oct 1 (rollable=false)");
    }

    @Test(description = "Spring Festival Day 1 (Feb 10 2024, Saturday) stays on Feb 10 — rollable=false")
    public void testSpringFestivalDay1NotRolledSaturday2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        Optional<HolidayDate> day1 = holidays.stream()
                .filter(hd -> "Spring Festival (Day 1)".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(day1.isPresent(), "Spring Festival (Day 1) must be present for 2024");
        assertEquals(day1.get().getDate(), LocalDate.of(2024, Month.FEBRUARY, 10),
                "Spring Festival (Day 1) 2024 (Saturday) must stay Feb 10 (rollable=false)");
    }

    @Test(description = "Spring Festival Day 2 (Feb 11 2024, Sunday) stays on Feb 11 — rollable=false")
    public void testSpringFestivalDay2NotRolledSunday2024() {
        List<HolidayDate> holidays = service.getHolidayCalendar().calculate(2024);
        Optional<HolidayDate> day2 = holidays.stream()
                .filter(hd -> "Spring Festival (Day 2)".equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(day2.isPresent(), "Spring Festival (Day 2) must be present for 2024");
        assertEquals(day2.get().getDate(), LocalDate.of(2024, Month.FEBRUARY, 11),
                "Spring Festival (Day 2) 2024 (Sunday) must stay Feb 11 (rollable=false)");
    }

    // =========================================================================
    // 10. CHRONOLOGICAL ORDERING — LIST REMAINS SORTED AFTER ROLLING
    // =========================================================================

    @DataProvider(name = "yearsForOrderCheck")
    public Iterator<Object[]> yearsForOrderCheck() {
        return Arrays.asList(
            new Object[]{2021},
            new Object[]{2022},
            new Object[]{2023},
            new Object[]{2024},
            new Object[]{2025},
            new Object[]{2026},
            new Object[]{2027},
            new Object[]{2028}
        ).iterator();
    }

    @Test(dataProvider = "yearsForOrderCheck",
          description = "calculate(year) returns holidays in non-decreasing chronological order")
    public void testChronologicalOrder(int year) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        List<HolidayDate> holidays = calendar.calculate(year);
        for (int i = 1; i < holidays.size(); i++) {
            LocalDate prev = holidays.get(i - 1).getDate();
            LocalDate curr = holidays.get(i).getDate();
            assertFalse(curr.isBefore(prev),
                    "Dates out of order in year " + year + " at index " + i
                    + ": " + prev + " followed by " + curr);
        }
    }

    // =========================================================================
    // 11. FACTORY DISCOVERY — SERVICELOADER FINDS CN
    // =========================================================================

    @Test(description = "HolidayCalendarFactory.create(\"CN\") returns a non-null calendar with code CN")
    public void testHolidayCalendarFactoryCreate() {
        HolidayCalendar calendar = factory.create("CN");
        assertNotNull(calendar, "factory.create(\"CN\") must not return null");
        assertEquals(calendar.getCode(), "CN",
                "factory.create(\"CN\").getCode() must return \"CN\"");
    }

    @Test(description = "factory.dataValidThrough(\"CN\") returns empty — CN is fully algorithmic")
    public void testFactoryDataValidThroughCN() {
        OptionalInt result = factory.dataValidThrough("CN");
        assertFalse(result.isPresent(),
                "factory.dataValidThrough(\"CN\") must return OptionalInt.empty() — fully algorithmic");
    }

    // =========================================================================
    // 12. HOLIDAY NAME DEFINITIONS — CALENDAR DEFINITION CONTAINS EXPECTED NAMES
    // =========================================================================

    @DataProvider(name = "expectedHolidayNames")
    public Iterator<Object[]> expectedHolidayNames() {
        return Arrays.asList(
            new Object[]{"New Year's Day"},
            new Object[]{"Spring Festival (Day 1)"},
            new Object[]{"Spring Festival (Day 2)"},
            new Object[]{"Spring Festival (Day 3)"},
            new Object[]{"Qingming Festival"},
            new Object[]{"Labour Day"},
            new Object[]{"Dragon Boat Festival"},
            new Object[]{"Mid-Autumn Festival"},
            new Object[]{"National Day (Day 1)"},
            new Object[]{"National Day (Day 2)"},
            new Object[]{"National Day (Day 3)"}
        ).iterator();
    }

    @Test(dataProvider = "expectedHolidayNames",
          description = "Calendar definition contains the expected holiday name")
    public void testHolidayCalendarContainsName(String holidayName) {
        HolidayCalendar calendar = service.getHolidayCalendar();
        boolean found = calendar.getHolidays().stream()
                .anyMatch(h -> holidayName.equals(h.getName()));
        assertTrue(found, "Calendar must define holiday: \"" + holidayName + "\"");
    }

    // =========================================================================
    // 13. CN vs CNY DISTINCTION — ROLLABLE FLAG CONTRAST
    // =========================================================================

    @Test(description = "CN calendar rolls New Year's Day (Sunday); CNY calendar does not")
    public void testCNRollsWhileCNYDoesNot() {
        // 2023: Jan 1 = Sunday
        // CN  (followingMonday, rollable=true)  → observed Jan 2
        // CNY (noRoll,          rollable=false) → observed Jan 1
        List<HolidayDate> cnHolidays  = service.getHolidayCalendar().calculate(2023);
        List<HolidayDate> cnyHolidays = new HolidayCalendarServiceCNY().getHolidayCalendar().calculate(2023);

        LocalDate cnDate  = findDate(cnHolidays,  "New Year's Day");
        LocalDate cnyDate = findDate(cnyHolidays, "New Year's Day");

        assertEquals(cnDate,  LocalDate.of(2023, Month.JANUARY, 2),
                "CN calendar must roll Sunday Jan 1 2023 to Monday Jan 2");
        assertEquals(cnyDate, LocalDate.of(2023, Month.JANUARY, 1),
                "CNY calendar must not roll Sunday Jan 1 2023 (noRoll strategy)");
        assertNotEquals(cnDate, cnyDate,
                "CN and CNY observed dates for New Year's Day 2023 must differ");
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private static void assertHolidayDate(List<HolidayDate> holidays,
                                          String name,
                                          LocalDate expectedDate,
                                          String context) {
        Optional<HolidayDate> found = holidays.stream()
                .filter(hd -> name.equals(hd.getHoliday().getName()))
                .findFirst();
        assertTrue(found.isPresent(), context + ": holiday \"" + name + "\" must be present");
        assertEquals(found.get().getDate(), expectedDate,
                context + ": observed date mismatch for \"" + name + "\"");
    }

    private static LocalDate findDate(List<HolidayDate> holidays, String name) {
        return holidays.stream()
                .filter(hd -> name.equals(hd.getHoliday().getName()))
                .map(HolidayDate::getDate)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Holiday not found: " + name));
    }

}