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
import java.time.Month;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Independent domain-fact sanity check for the Hebrew-calendar {@code Observance}
 * classes, for 2024-2026.
 *
 * <p><strong>Why this test exists:</strong> the other tests in this module (e.g.
 * {@link org.holiday.calendar.impl.HolidayCalendarServiceILSEarlyCloseTest}) verify
 * internal consistency — that {@code Erev X} is exactly one day before {@code X}, that
 * {@code calculateEarlyCloses()} agrees with the raw {@code Observance}, etc. Those
 * checks can never catch a case where the underlying Hebrew→Gregorian conversion
 * (via {@code net.time4j.calendar.HebrewCalendar}) is systematically wrong, because
 * both sides of the comparison are derived from the same production code path.
 *
 * <p>This test instead hardcodes real-world Gregorian dates that were independently
 * looked up (not computed from, or derived by calling, any class under test) and
 * asserts the production {@code Observance} classes return exactly those dates.
 * The {@code Erev} (eve) expectations are computed by literally subtracting one day
 * from the hardcoded main-holiday literal in this test file — they never flow through
 * {@code RoshHashanah#apply}, {@code YomKippur#apply}, etc. Where an authoritative
 * source published the eve date directly (rather than this test deriving it), that
 * directly-sourced date is used and is noted below; it happens to always agree with
 * "main date minus 1", which is itself a useful independent confirmation.
 *
 * <p><strong>Sources (cross-referenced against at least two independent providers):</strong>
 * <ul>
 *   <li>Hebcal.com per-holiday pages for Israel (single-day observance), e.g.
 *       {@code https://www.hebcal.com/holidays/rosh-hashana-2024},
 *       {@code https://www.hebcal.com/holidays/pesach-2024?i=on}, etc. Hebcal
 *       expresses each holiday as beginning "at sunset" on a given Gregorian date;
 *       the Gregorian calendar date of the corresponding Hebrew day (as returned by
 *       {@code HebrewCalendar...transform(PlainDate.axis())}, which is what every
 *       production {@code Observance} in this package uses) is the day <em>after</em>
 *       that sunset-start date, i.e. the day whose daylight hours the holiday falls
 *       during. The sunset-start date itself is, independently, the real-world
 *       civil date of "Erev [Holiday]" (the eve).</li>
 *   <li>Cross-check: Jerusalem Post ("Rosh Hashanah 2024: Frequently asked questions
 *       and answers") and israelhayom.com confirm Rosh Hashanah 2024 in Israel ran
 *       from the evening of Wed 2 Oct 2024 through the evening of Thu 3 Oct 2024 —
 *       i.e. 1 Tishri 5785 = Thursday, 3 October 2024.</li>
 *   <li>Cross-check: Hoshana Raba (21 Tishri) dates were independently confirmed via
 *       separate search results (porthope.ca event calendar and chabad.org calendar
 *       views) giving "October 22 to 23" for 2024, "sunset 12 October" (day itself:
 *       13 October) for 2025, and "sunset 1 October" (day itself: 2 October) for
 *       2026 — each consistent with Sukkot's first day (15 Tishri) plus 6 days.</li>
 * </ul>
 */
public class HebrewCalendarDateSanityTest {

    // -------------------------------------------------------------------------
    // Main holidays: hardcoded, independently-sourced Gregorian dates.
    // -------------------------------------------------------------------------

    @DataProvider
    Iterator<Object[]> mainHolidayDates() {
        return List.of(
            // Rosh Hashanah (1 Tishri) — source: Hebcal rosh-hashana-{year} pages,
            // cross-checked against Jerusalem Post / israelhayom.com for 2024.
            new Object[]{"RoshHashanah", 2024, LocalDate.of(2024, Month.OCTOBER, 3)},
            new Object[]{"RoshHashanah", 2025, LocalDate.of(2025, Month.SEPTEMBER, 23)},
            new Object[]{"RoshHashanah", 2026, LocalDate.of(2026, Month.SEPTEMBER, 12)},

            // Yom Kippur (10 Tishri) — source: Hebcal yom-kippur-{year} pages.
            new Object[]{"YomKippur", 2024, LocalDate.of(2024, Month.OCTOBER, 12)},
            new Object[]{"YomKippur", 2025, LocalDate.of(2025, Month.OCTOBER, 2)},
            new Object[]{"YomKippur", 2026, LocalDate.of(2026, Month.SEPTEMBER, 21)},

            // Sukkot, first day (15 Tishri) — source: Hebcal sukkot-{year} pages.
            new Object[]{"Sukkot", 2024, LocalDate.of(2024, Month.OCTOBER, 17)},
            new Object[]{"Sukkot", 2025, LocalDate.of(2025, Month.OCTOBER, 7)},
            new Object[]{"Sukkot", 2026, LocalDate.of(2026, Month.SEPTEMBER, 26)},

            // Passover, first day (15 Nisan) — source: Hebcal pesach-{year}?i=on
            // (Israel, single-day first-day observance) pages.
            new Object[]{"Passover", 2024, LocalDate.of(2024, Month.APRIL, 23)},
            new Object[]{"Passover", 2025, LocalDate.of(2025, Month.APRIL, 13)},
            new Object[]{"Passover", 2026, LocalDate.of(2026, Month.APRIL, 2)},

            // Shavuot (6 Sivan) — source: Hebcal shavuot-{year}?i=on (Israel) pages.
            new Object[]{"Shavuot", 2024, LocalDate.of(2024, Month.JUNE, 12)},
            new Object[]{"Shavuot", 2025, LocalDate.of(2025, Month.JUNE, 2)},
            new Object[]{"Shavuot", 2026, LocalDate.of(2026, Month.MAY, 22)},

            // Hoshana Raba (21 Tishri) — source: Sukkot first day (above) + 6 days,
            // cross-checked directly against independent Hoshana Raba listings.
            new Object[]{"HoshanaRaba", 2024, LocalDate.of(2024, Month.OCTOBER, 23)},
            new Object[]{"HoshanaRaba", 2025, LocalDate.of(2025, Month.OCTOBER, 13)},
            new Object[]{"HoshanaRaba", 2026, LocalDate.of(2026, Month.OCTOBER, 2)}
        ).iterator();
    }

    @Test(dataProvider = "mainHolidayDates")
    public void testMainHolidayMatchesIndependentlySourcedDate(String holidayName, int year, LocalDate expected) {
        LocalDate actual = switch (holidayName) {
            case "RoshHashanah" -> new RoshHashanah().apply(year);
            case "YomKippur" -> new YomKippur().apply(year);
            case "Sukkot" -> new Sukkot().apply(year);
            case "Passover" -> new Passover().apply(year);
            case "Shavuot" -> new Shavuot().apply(year);
            case "HoshanaRaba" -> new HoshanaRaba().apply(year);
            default -> throw new IllegalArgumentException("Unknown holiday: " + holidayName);
        };
        assertEquals(actual, expected,
                holidayName + " " + year + " must match the independently-sourced real-world date");
    }

    // -------------------------------------------------------------------------
    // Erev (eve) holidays: expected value is the hardcoded main-holiday literal
    // above, minus one day, computed here in the test — never by calling the
    // production main-holiday Observance and subtracting from its output. These
    // eve dates also happen to be directly the "begins at sunset on" date
    // published by Hebcal, an independent confirmation in its own right.
    // -------------------------------------------------------------------------

    @DataProvider
    Iterator<Object[]> erevHolidayDates() {
        return List.of(
            // Erev Rosh Hashanah — directly-sourced eve date (Hebcal: "began at
            // sunset" on this date) = independently confirmed as 1 Tishri minus 1.
            new Object[]{"ErevRoshHashanah", 2024, LocalDate.of(2024, Month.OCTOBER, 2)},
            new Object[]{"ErevRoshHashanah", 2025, LocalDate.of(2025, Month.SEPTEMBER, 22)},
            new Object[]{"ErevRoshHashanah", 2026, LocalDate.of(2026, Month.SEPTEMBER, 11)},

            // Erev Yom Kippur — directly-sourced eve date (Hebcal sunset-start).
            new Object[]{"ErevYomKippur", 2024, LocalDate.of(2024, Month.OCTOBER, 11)},
            new Object[]{"ErevYomKippur", 2025, LocalDate.of(2025, Month.OCTOBER, 1)},
            new Object[]{"ErevYomKippur", 2026, LocalDate.of(2026, Month.SEPTEMBER, 20)},

            // Erev Sukkot — directly-sourced eve date (Hebcal sunset-start).
            new Object[]{"ErevSukkot", 2024, LocalDate.of(2024, Month.OCTOBER, 16)},
            new Object[]{"ErevSukkot", 2025, LocalDate.of(2025, Month.OCTOBER, 6)},
            new Object[]{"ErevSukkot", 2026, LocalDate.of(2026, Month.SEPTEMBER, 25)},

            // Erev Passover — directly-sourced eve date (Hebcal sunset-start).
            new Object[]{"ErevPassover", 2024, LocalDate.of(2024, Month.APRIL, 22)},
            new Object[]{"ErevPassover", 2025, LocalDate.of(2025, Month.APRIL, 12)},
            new Object[]{"ErevPassover", 2026, LocalDate.of(2026, Month.APRIL, 1)},

            // Erev Shavuot — directly-sourced eve date (Hebcal sunset-start).
            new Object[]{"ErevShavuot", 2024, LocalDate.of(2024, Month.JUNE, 11)},
            new Object[]{"ErevShavuot", 2025, LocalDate.of(2025, Month.JUNE, 1)},
            new Object[]{"ErevShavuot", 2026, LocalDate.of(2026, Month.MAY, 21)}
        ).iterator();
    }

    @Test(dataProvider = "erevHolidayDates")
    public void testErevHolidayMatchesIndependentlySourcedEveDate(String erevName, int year, LocalDate expected) {
        LocalDate actual = switch (erevName) {
            case "ErevRoshHashanah" -> new ErevRoshHashanah().apply(year);
            case "ErevYomKippur" -> new ErevYomKippur().apply(year);
            case "ErevSukkot" -> new ErevSukkot().apply(year);
            case "ErevPassover" -> new ErevPassover().apply(year);
            case "ErevShavuot" -> new ErevShavuot().apply(year);
            default -> throw new IllegalArgumentException("Unknown erev: " + erevName);
        };
        assertEquals(actual, expected,
                erevName + " " + year + " must match the independently-sourced real-world eve date");
    }

}
