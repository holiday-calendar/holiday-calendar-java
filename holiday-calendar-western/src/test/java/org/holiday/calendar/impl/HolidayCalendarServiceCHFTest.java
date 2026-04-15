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

public class HolidayCalendarServiceCHFTest extends AbstractHolidayCalendarServiceTest {

    static final String CODE = "CHF";

    public HolidayCalendarServiceCHFTest() {
        super(CODE);
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayNames() {
        // Berchtoldstag is the primary CHF differentiator — absent from the CH calendar.
        final Object[] berchtoldstag    = new Object[]{"Berchtoldstag"};
        final Object[] swissNationalDay = new Object[]{"Swiss National Day"};
        final Object[] boxingDay        = new Object[]{"Boxing Day"};
        return Arrays.asList(berchtoldstag, swissNationalDay, boxingDay).listIterator();
    }

    @DataProvider
    @Override
    Iterator<Object[]> expectedHolidayOccurrences() {
        // SIC no-adjustment convention: all holidays observe on their natural calendar dates.

        // ---- New Year's Day (Jan 1) — no roll ----
        // 2021: Friday    — weekday, no adjustment needed
        final Object[] nyd2021 = {2021, "New Year's Day", LocalDate.of(2021, Month.JANUARY, 1)};
        // 2022: Saturday  — no roll (SIC no-adjustment)
        final Object[] nyd2022 = {2022, "New Year's Day", LocalDate.of(2022, Month.JANUARY, 1)};
        // 2023: Sunday    — no roll
        final Object[] nyd2023 = {2023, "New Year's Day", LocalDate.of(2023, Month.JANUARY, 1)};

        // ---- Berchtoldstag (Jan 2) — no roll — NOT in CH calendar ----
        // 2021: Saturday  — no roll
        final Object[] berchtold2021 = {2021, "Berchtoldstag", LocalDate.of(2021, Month.JANUARY, 2)};
        // 2022: Sunday    — no roll
        final Object[] berchtold2022 = {2022, "Berchtoldstag", LocalDate.of(2022, Month.JANUARY, 2)};
        // 2023: Monday    — weekday
        final Object[] berchtold2023 = {2023, "Berchtoldstag", LocalDate.of(2023, Month.JANUARY, 2)};

        // ---- Good Friday (floating, rollable=false — always a Friday) ----
        // 2021: Easter Apr 4  → Good Friday Apr 2
        final Object[] goodFriday2021 = {2021, "Good Friday", LocalDate.of(2021, Month.APRIL,  2)};
        // 2022: Easter Apr 17 → Good Friday Apr 15
        final Object[] goodFriday2022 = {2022, "Good Friday", LocalDate.of(2022, Month.APRIL, 15)};
        // 2024: Easter Mar 31 → Good Friday Mar 29
        final Object[] goodFriday2024 = {2024, "Good Friday", LocalDate.of(2024, Month.MARCH, 29)};

        // ---- Easter Monday (floating, rollable=false — always a Monday) ----
        // 2021: Easter Apr 4  → Easter Monday Apr 5
        final Object[] easterMonday2021 = {2021, "Easter Monday", LocalDate.of(2021, Month.APRIL,  5)};
        // 2022: Easter Apr 17 → Easter Monday Apr 18
        final Object[] easterMonday2022 = {2022, "Easter Monday", LocalDate.of(2022, Month.APRIL, 18)};
        // 2024: Easter Mar 31 → Easter Monday Apr 1
        final Object[] easterMonday2024 = {2024, "Easter Monday", LocalDate.of(2024, Month.APRIL,  1)};

        // ---- Labour Day (May 1) — no roll ----
        // 2021: Saturday  — no roll
        final Object[] labourDay2021 = {2021, "Labour Day", LocalDate.of(2021, Month.MAY, 1)};
        // 2022: Sunday    — no roll
        final Object[] labourDay2022 = {2022, "Labour Day", LocalDate.of(2022, Month.MAY, 1)};
        // 2023: Monday    — weekday
        final Object[] labourDay2023 = {2023, "Labour Day", LocalDate.of(2023, Month.MAY, 1)};

        // ---- Ascension Day (floating, rollable=false — always a Thursday, Easter+39) ----
        // 2021: Easter Apr 4  + 39 = May 13
        final Object[] ascension2021 = {2021, "Ascension Day", LocalDate.of(2021, Month.MAY, 13)};
        // 2022: Easter Apr 17 + 39 = May 26
        final Object[] ascension2022 = {2022, "Ascension Day", LocalDate.of(2022, Month.MAY, 26)};
        // 2023: Easter Apr 9  + 39 = May 18
        final Object[] ascension2023 = {2023, "Ascension Day", LocalDate.of(2023, Month.MAY, 18)};

        // ---- Whit Monday (floating, rollable=false — always a Monday, Easter+50) ----
        // 2021: Easter Apr 4  + 50 = May 24
        final Object[] whitMonday2021 = {2021, "Whit Monday", LocalDate.of(2021, Month.MAY, 24)};
        // 2022: Easter Apr 17 + 50 = Jun 6
        final Object[] whitMonday2022 = {2022, "Whit Monday", LocalDate.of(2022, Month.JUNE,  6)};
        // 2023: Easter Apr 9  + 50 = May 29
        final Object[] whitMonday2023 = {2023, "Whit Monday", LocalDate.of(2023, Month.MAY, 29)};

        // ---- Swiss National Day (Aug 1) — no roll ----
        // 2021: Sunday    — no roll (CH rolls this to Aug 2; CHF does not)
        final Object[] nationalDay2021 = {2021, "Swiss National Day", LocalDate.of(2021, Month.AUGUST, 1)};
        // 2022: Monday    — weekday
        final Object[] nationalDay2022 = {2022, "Swiss National Day", LocalDate.of(2022, Month.AUGUST, 1)};
        // 2026: Saturday  — no roll
        final Object[] nationalDay2026 = {2026, "Swiss National Day", LocalDate.of(2026, Month.AUGUST, 1)};

        // ---- Christmas Day (Dec 25) — no roll ----
        // 2021: Saturday  — no roll (CH rolls this to Dec 24; CHF does not)
        final Object[] christmas2021 = {2021, "Christmas Day", LocalDate.of(2021, Month.DECEMBER, 25)};
        // 2022: Sunday    — no roll (CH rolls this to Dec 26; CHF does not)
        final Object[] christmas2022 = {2022, "Christmas Day", LocalDate.of(2022, Month.DECEMBER, 25)};
        // 2023: Monday    — weekday
        final Object[] christmas2023 = {2023, "Christmas Day", LocalDate.of(2023, Month.DECEMBER, 25)};

        // ---- Boxing Day (Dec 26) — no roll ----
        // 2021: Sunday    — no roll
        final Object[] boxingDay2021 = {2021, "Boxing Day", LocalDate.of(2021, Month.DECEMBER, 26)};
        // 2022: Monday    — weekday
        final Object[] boxingDay2022 = {2022, "Boxing Day", LocalDate.of(2022, Month.DECEMBER, 26)};
        // 2023: Tuesday   — weekday
        final Object[] boxingDay2023 = {2023, "Boxing Day", LocalDate.of(2023, Month.DECEMBER, 26)};
        // 2026: Saturday  — no roll
        final Object[] boxingDay2026 = {2026, "Boxing Day", LocalDate.of(2026, Month.DECEMBER, 26)};

        return Arrays.asList(
                nyd2021, nyd2022, nyd2023,
                berchtold2021, berchtold2022, berchtold2023,
                goodFriday2021, goodFriday2022, goodFriday2024,
                easterMonday2021, easterMonday2022, easterMonday2024,
                labourDay2021, labourDay2022, labourDay2023,
                ascension2021, ascension2022, ascension2023,
                whitMonday2021, whitMonday2022, whitMonday2023,
                nationalDay2021, nationalDay2022, nationalDay2026,
                christmas2021, christmas2022, christmas2023,
                boxingDay2021, boxingDay2022, boxingDay2023, boxingDay2026
        ).listIterator();
    }

    @Test(dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testHolidayCount() {
        final HolidayCalendar calendar = factory.create(CODE);
        // New Year's Day, Berchtoldstag, Good Friday, Easter Monday, Labour Day,
        // Ascension Day, Whit Monday, Swiss National Day, Christmas Day, Boxing Day.
        assertEquals(calendar.getHolidays().size(), 10,
                "CHF calendar must define exactly 10 SIC closure days");
    }

    @Test(dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testBerchtoldTagAbsentFromCH() {
        // Berchtoldstag is the sole holiday present in CHF but absent from CH.
        // Asserting its absence from CH validates that the CHF presence assertion is meaningful.
        final HolidayCalendar chCalendar = factory.create("CH");
        final boolean defined = chCalendar.getHolidays()
                .stream()
                .anyMatch(h -> "Berchtoldstag".equals(h.getName()));
        assertFalse(defined, "CH calendar must NOT contain Berchtoldstag — it is a CHF-only holiday");
    }

    @Test(dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testNoAdjustmentForWeekendHoliday() {
        // Swiss National Day 2021 (Aug 1 = Sunday) must observe on Aug 1 — not rolled to Aug 2.
        // This is the sharpest proof that CHF uses noRoll() rather than any rolling strategy.
        // By contrast, the CH calendar (previousFridayOrFollowingMonday) would give Aug 2.
        final HolidayCalendar calendar = factory.create(CODE);
        final List<HolidayDate> results = calendar.calculate(2021);
        final LocalDate observed = results.stream()
                .filter(hd -> "Swiss National Day".equals(hd.getHoliday().getName()))
                .findFirst()
                .map(HolidayDate::getDate)
                .orElseThrow();
        assertEquals(observed, LocalDate.of(2021, Month.AUGUST, 1),
                "CHF must observe Swiss National Day on Aug 1 even when it falls on a Sunday");
    }

    @Test(dependsOnMethods = "testHolidayCalendarFactoryCreate")
    public void testChronologicalOrder() {
        final HolidayCalendar calendar = factory.create(CODE);
        for (final int year : new int[]{2021, 2022, 2023, 2024, 2025, 2026, 2027}) {
            final List<HolidayDate> holidays = calendar.calculate(year);
            assertFalse(holidays.isEmpty(), "Expected non-empty holiday list for " + year);
            for (int i = 1; i < holidays.size(); i++) {
                assertFalse(holidays.get(i).getDate().isBefore(holidays.get(i - 1).getDate()), "Holidays out of chronological order for " + year
                        + ": " + holidays.get(i - 1).getDate()
                        + " > " + holidays.get(i).getDate());
            }
        }
    }

}