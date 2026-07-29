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

package org.holiday.calendar.observance.islamic.mena.ilmitakvim;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Verifies {@link IlmiTakvimCalculator} against all 24 dates (2024-2035, Eid
 * al-Fitr and Eid al-Adha) fetched directly from Diyanet's own published
 * "Dini Günler" tables at vakithesaplama.diyanet.gov.tr (icerik.php?icerik=N /
 * dinigunler.php?yil=N for the respective years) — not the CSV files, so this
 * is an independent check of the calculator's logic in isolation.
 */
public class IlmiTakvimCalculatorTest {

    // -------------------------------------------------------------------------
    // Verified Eid al-Fitr dates (1 Shawwal), 2024-2035, Diyanet official
    // -------------------------------------------------------------------------

    @DataProvider
    Iterator<Object[]> verifiedEidAlFitrDates() {
        return List.of(
            new Object[]{2024, LocalDate.of(2024, 4, 10)},
            new Object[]{2025, LocalDate.of(2025, 3, 30)},
            new Object[]{2026, LocalDate.of(2026, 3, 20)},
            new Object[]{2027, LocalDate.of(2027, 3, 9)},
            new Object[]{2028, LocalDate.of(2028, 2, 26)},
            new Object[]{2029, LocalDate.of(2029, 2, 14)},
            new Object[]{2030, LocalDate.of(2030, 2, 4)},
            new Object[]{2031, LocalDate.of(2031, 1, 24)},
            new Object[]{2032, LocalDate.of(2032, 1, 14)},
            new Object[]{2033, LocalDate.of(2033, 1, 2)}, // double-occurrence year; January recorded
            new Object[]{2034, LocalDate.of(2034, 12, 12)},
            new Object[]{2035, LocalDate.of(2035, 12, 1)}
        ).iterator();
    }

    @Test(dataProvider = "verifiedEidAlFitrDates")
    public void computesEidAlFitrAgainstDiyanetPublishedDate(int year, LocalDate expected) {
        assertEquals(IlmiTakvimCalculator.eidAlFitr(year), expected,
                "Eid al-Fitr " + year + " must match Diyanet's published ilmi takvim date");
    }

    // -------------------------------------------------------------------------
    // Verified Eid al-Adha dates (10 Dhu al-Hijjah), 2024-2035, Diyanet official
    // -------------------------------------------------------------------------

    @DataProvider
    Iterator<Object[]> verifiedEidAlAdhaDates() {
        return List.of(
            new Object[]{2024, LocalDate.of(2024, 6, 16)},
            new Object[]{2025, LocalDate.of(2025, 6, 6)},
            new Object[]{2026, LocalDate.of(2026, 5, 27)},
            new Object[]{2027, LocalDate.of(2027, 5, 16)},
            new Object[]{2028, LocalDate.of(2028, 5, 5)},
            new Object[]{2029, LocalDate.of(2029, 4, 24)},
            new Object[]{2030, LocalDate.of(2030, 4, 13)},
            new Object[]{2031, LocalDate.of(2031, 4, 2)},
            new Object[]{2032, LocalDate.of(2032, 3, 22)},
            new Object[]{2033, LocalDate.of(2033, 3, 11)},
            new Object[]{2034, LocalDate.of(2034, 3, 1)},
            new Object[]{2035, LocalDate.of(2035, 2, 18)}
        ).iterator();
    }

    @Test(dataProvider = "verifiedEidAlAdhaDates")
    public void computesEidAlAdhaAgainstDiyanetPublishedDate(int year, LocalDate expected) {
        assertEquals(IlmiTakvimCalculator.eidAlAdha(year), expected,
                "Eid al-Adha " + year + " must match Diyanet's published ilmi takvim date");
    }

    // Canonical divergence: Diyanet vs UAE SCA / Umm al-Qura, 2026 Eid al-Adha
    @Test
    public void reproducesCanonical2026AdhaDivergence() {
        assertEquals(IlmiTakvimCalculator.eidAlAdha(2026), LocalDate.of(2026, 5, 27));
        assertNotEquals(IlmiTakvimCalculator.eidAlAdha(2026), LocalDate.of(2026, 5, 26),
                "Diyanet and UAE SCA Eid al-Adha 2026 must differ by one day");
    }

    // -------------------------------------------------------------------------
    // Residual projection range (2036-2055) — internal consistency checks only;
    // Diyanet has not yet published these, so there is no independent source to
    // verify against. These checks guard against gross errors (wrong direction,
    // duplicate dates, out-of-range results), not exact-date correctness.
    // -------------------------------------------------------------------------

    @Test
    public void projectedFitrDatesRegressPlausibly() {
        for (int year = 2036; year <= 2054; year++) {
            LocalDate current = IlmiTakvimCalculator.eidAlFitr(year);
            LocalDate next = IlmiTakvimCalculator.eidAlFitr(year + 1);
            assertPlausibleGap("Eid al-Fitr", year, current, next);
        }
    }

    @Test
    public void projectedAdhaDatesRegressPlausibly() {
        for (int year = 2036; year <= 2054; year++) {
            LocalDate current = IlmiTakvimCalculator.eidAlAdha(year);
            LocalDate next = IlmiTakvimCalculator.eidAlAdha(year + 1);
            assertPlausibleGap("Eid al-Adha", year, current, next);
        }
    }

    /**
     * A Hijri year is ~354-355 days; against a ~365-366 day Gregorian year, the
     * anniversary regresses by ~10-12 days most years. When a double-occurrence
     * year (two anniversaries in one Gregorian year, only the first recorded per
     * the documented convention) is involved, the recorded gap to the following
     * year can look like roughly two lunar years instead of one.
     */
    private static void assertPlausibleGap(String label, int year, LocalDate current, LocalDate next) {
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(current, next);
        boolean oneLunarYear = daysBetween >= 353 && daysBetween <= 357;
        boolean twoLunarYears = daysBetween >= 700 && daysBetween <= 712;
        assertTrue(oneLunarYear || twoLunarYears,
                label + " " + year + "->" + (year + 1) + " gap of " + daysBetween
                        + " days is outside the plausible range");
    }

    @Test
    public void adhaIsAlwaysComputableAcrossTheResidualProjectionRange() {
        for (int year = 2036; year <= 2055; year++) {
            assertNotNull(IlmiTakvimCalculator.eidAlAdha(year), "Eid al-Adha " + year + " must resolve to a date");
        }
    }

}
