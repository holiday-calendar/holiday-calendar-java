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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public class EidAlAdhaTest {

    private ListAppender<ILoggingEvent> listAppender;
    private Logger observanceLogger;

    @BeforeMethod
    public void attachLogAppender() {
        observanceLogger = (Logger) LoggerFactory.getLogger(EidAlAdha.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        observanceLogger.addAppender(listAppender);
    }

    @AfterMethod
    public void detachLogAppender() {
        observanceLogger.detachAppender(listAppender);
    }

    @DataProvider
    Iterator<Object[]> knownDatesAE() {
        return List.of(
            new Object[]{2024, LocalDate.of(2024, 6, 16)},
            new Object[]{2025, LocalDate.of(2025, 6, 6)},
            new Object[]{2055, LocalDate.of(2055, 7, 3)}
        ).iterator();
    }

    @Test(dataProvider = "knownDatesAE")
    public void testKnownDatesAE(int year, LocalDate expected) {
        assertEquals(new EidAlAdha("AE").apply(year), expected);
    }

    @Test
    public void testCeilingYearReturnsDate() {
        assertNotNull(new EidAlAdha("AE").apply(EidAlAdha.DATA_VALID_THROUGH));
    }

    @Test
    public void testBeyondCeilingReturnsNull() {
        assertNull(new EidAlAdha("AE").apply(EidAlAdha.DATA_VALID_THROUGH + 1));
    }

    @Test
    public void testBeyondCeilingLogsWarning() {
        new EidAlAdha("AE").apply(EidAlAdha.DATA_VALID_THROUGH + 1);
        assertTrue(listAppender.list.stream().anyMatch(e -> e.getLevel() == Level.WARN),
                "Expected a WARN log for year beyond ceiling");
    }

    @Test
    public void testBelowFloorReturnsNullSilently() {
        assertNull(new EidAlAdha("AE").apply(EidAlAdha.DATA_VALID_FROM - 1));
        assertFalse(listAppender.list.stream().anyMatch(e -> e.getLevel() == Level.WARN),
                "No WARN should be logged for year below floor");
    }

    @Test
    public void testCaseInsensitiveCountryCode() {
        assertEquals(new EidAlAdha("ae").apply(2025), new EidAlAdha("AE").apply(2025));
    }

    // -------------------------------------------------------------------------
    // Known dates — TR (Diyanet ilmi takvim, official published dates 2024-2035)
    // -------------------------------------------------------------------------

    @DataProvider
    Iterator<Object[]> knownDatesTR() {
        return List.of(
            new Object[]{2024, LocalDate.of(2024, 6, 16)},
            new Object[]{2025, LocalDate.of(2025, 6, 6)},
            new Object[]{2026, LocalDate.of(2026, 5, 27)},  // one day later than AE/SA — see below
            new Object[]{2027, LocalDate.of(2027, 5, 16)},
            new Object[]{2035, LocalDate.of(2035, 2, 18)},
            // 2036-2055 are IlmiTakvimCalculator projections (Diyanet has not yet
            // published this far ahead) — not independently verified against a
            // Diyanet source; see CsvCalculatorParityTest for CSV/calculator consistency.
            new Object[]{2040, LocalDate.of(2040, 12, 14)},
            new Object[]{2050, LocalDate.of(2050, 8, 27)},
            new Object[]{2055, LocalDate.of(2055, 7, 5)}
        ).iterator();
    }

    @Test(dataProvider = "knownDatesTR")
    public void testKnownDatesTR(int year, LocalDate expected) {
        assertEquals(new EidAlAdha("TR").apply(year), expected,
                "Eid al-Adha " + year + " per Diyanet must be " + expected);
    }

    // Canonical Diyanet vs. Umm al-Qura/UAE divergence: 2026 Eid al-Adha.
    // (A previously-recorded 2025 divergence was a data error: 2025-06-05 is Arefe,
    // the eve of Bayram, not the actual 1st day — Diyanet's published 1st day is
    // 2025-06-06, matching AE/SA. No divergence exists in 2025.)
    @Test
    public void testTR2026DiffersFromAE() {
        LocalDate trDate = new EidAlAdha("TR").apply(2026);
        LocalDate aeDate = new EidAlAdha("AE").apply(2026);
        assertEquals(trDate, LocalDate.of(2026, 5, 27),
                "Diyanet Eid al-Adha 2026 must be May 27");
        assertEquals(aeDate, LocalDate.of(2026, 5, 26),
                "UAE SCA Eid al-Adha 2026 must be May 26");
        assertNotEquals(trDate, aeDate,
                "Diyanet and UAE SCA Eid al-Adha 2026 must differ by one day");
    }

    // 2025 no longer diverges once the Arefe/Bayram-day data error is corrected
    @Test
    public void testTR2025MatchesAE() {
        assertEquals(new EidAlAdha("TR").apply(2025), new EidAlAdha("AE").apply(2025),
                "Eid al-Adha 2025: Diyanet (TR) and UAE SCA (AE) must agree on June 6");
    }

    // -------------------------------------------------------------------------
    // Known dates — QA (Qatar Central Bank official)
    // -------------------------------------------------------------------------

    @DataProvider
    Iterator<Object[]> knownDatesQA() {
        return List.of(
            new Object[]{2024, LocalDate.of(2024, 6, 16)},
            new Object[]{2025, LocalDate.of(2025, 6, 6)},
            new Object[]{2055, LocalDate.of(2055, 7, 3)}
        ).iterator();
    }

    @Test(dataProvider = "knownDatesQA")
    public void testKnownDatesQA(int year, LocalDate expected) {
        assertEquals(new EidAlAdha("QA").apply(year), expected,
                "Eid al-Adha " + year + " per QCB must be " + expected);
    }

    // QA 2025 matches AE: Qatar and UAE both use Umm al-Qura; Jun 6 confirmed QCB official
    @Test
    public void testQA2025MatchesAE() {
        assertEquals(new EidAlAdha("QA").apply(2025), new EidAlAdha("AE").apply(2025),
                "Eid al-Adha 2025: Qatar (QCB) and UAE (SCA) must agree on June 6");
    }
}
