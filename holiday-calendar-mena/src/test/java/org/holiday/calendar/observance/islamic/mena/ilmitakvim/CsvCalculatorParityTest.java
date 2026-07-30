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

import org.holiday.calendar.observance.islamic.mena.EidAlAdha;
import org.holiday.calendar.observance.islamic.mena.EidAlFitr;
import org.holiday.calendar.util.CsvObservanceLoader;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * Guards against drift between the checked-in {@code eid-al-fitr-tr.csv} /
 * {@code eid-al-adha-tr.csv} rows for 2036-2055 and {@link IlmiTakvimCalculator}'s
 * own live output — both currently encode the same values (the CSV rows were
 * generated from the calculator), but nothing prevents them from silently
 * diverging if either is edited independently in the future.
 */
public class CsvCalculatorParityTest {

    // 2039 has a documented double-occurrence in Gregorian terms (see
    // eid-al-adha-tr.csv comments); the CSV records only the earlier occurrence,
    // exactly as the calculator does, so no exclusion is actually needed — but
    // years with day1 exactly on 31 December / 1 January carry a small extra risk
    // of calendar-boundary edge cases, so this suite does not special-case any
    // year and instead lets a real mismatch fail loudly.

    @DataProvider
    Object[][] projectedYears() {
        Object[][] years = new Object[20][1];
        for (int i = 0; i < 20; i++) {
            years[i][0] = 2036 + i;
        }
        return years;
    }

    @Test(dataProvider = "projectedYears")
    public void fitrCsvRowMatchesCalculatorOutput(int year) {
        Map<Integer, LocalDate> csv = CsvObservanceLoader.loadSingle(EidAlFitr.class, "eid-al-fitr-tr.csv");
        assertEquals(csv.get(year), IlmiTakvimCalculator.eidAlFitr(year),
                "eid-al-fitr-tr.csv row for " + year + " has drifted from IlmiTakvimCalculator's own output");
    }

    @Test(dataProvider = "projectedYears")
    public void adhaCsvRowMatchesCalculatorOutput(int year) {
        Map<Integer, LocalDate> csv = CsvObservanceLoader.loadSingle(EidAlAdha.class, "eid-al-adha-tr.csv");
        assertEquals(csv.get(year), IlmiTakvimCalculator.eidAlAdha(year),
                "eid-al-adha-tr.csv row for " + year + " has drifted from IlmiTakvimCalculator's own output");
    }

}
