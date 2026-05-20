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

public class EidAlFitrTest {

    private ListAppender<ILoggingEvent> listAppender;
    private Logger observanceLogger;

    @BeforeMethod
    public void attachLogAppender() {
        observanceLogger = (Logger) LoggerFactory.getLogger(EidAlFitr.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        observanceLogger.addAppender(listAppender);
    }

    @AfterMethod
    public void detachLogAppender() {
        observanceLogger.detachAppender(listAppender);
    }

    // -------------------------------------------------------------------------
    // Known dates — AE
    // -------------------------------------------------------------------------

    @DataProvider
    Iterator<Object[]> knownDatesAE() {
        return List.of(
            new Object[]{2024, LocalDate.of(2024, 4, 10)},
            new Object[]{2025, LocalDate.of(2025, 3, 30)},
            new Object[]{2055, LocalDate.of(2055, 4, 28)}
        ).iterator();
    }

    @Test(dataProvider = "knownDatesAE")
    public void testKnownDatesAE(int year, LocalDate expected) {
        assertEquals(new EidAlFitr("AE").apply(year), expected);
    }

    // -------------------------------------------------------------------------
    // Known dates — SA
    // -------------------------------------------------------------------------

    @DataProvider
    Iterator<Object[]> knownDatesSA() {
        return List.of(
            new Object[]{2024, LocalDate.of(2024, 4, 10)},
            new Object[]{2025, LocalDate.of(2025, 3, 30)},
            new Object[]{2055, LocalDate.of(2055, 4, 28)}
        ).iterator();
    }

    @Test(dataProvider = "knownDatesSA")
    public void testKnownDatesSA(int year, LocalDate expected) {
        assertEquals(new EidAlFitr("SA").apply(year), expected);
    }

    // -------------------------------------------------------------------------
    // Boundary year (2055) — last valid entry
    // -------------------------------------------------------------------------

    @Test
    public void testCeilingYearReturnsDate() {
        assertNotNull(new EidAlFitr("AE").apply(EidAlFitr.DATA_VALID_THROUGH));
    }

    // -------------------------------------------------------------------------
    // Beyond ceiling — returns null and logs WARN
    // -------------------------------------------------------------------------

    @Test
    public void testBeyondCeilingReturnsNull() {
        assertNull(new EidAlFitr("AE").apply(EidAlFitr.DATA_VALID_THROUGH + 1));
    }

    @Test
    public void testBeyondCeilingLogsWarning() {
        new EidAlFitr("AE").apply(EidAlFitr.DATA_VALID_THROUGH + 1);
        boolean hasWarn = listAppender.list.stream()
                .anyMatch(e -> e.getLevel() == Level.WARN);
        assertTrue(hasWarn, "Expected a WARN log for year beyond ceiling");
    }

    // -------------------------------------------------------------------------
    // Below floor — returns null silently (no WARN)
    // -------------------------------------------------------------------------

    @Test
    public void testBelowFloorReturnsNullSilently() {
        assertNull(new EidAlFitr("AE").apply(EidAlFitr.DATA_VALID_FROM - 1));
        boolean hasWarn = listAppender.list.stream()
                .anyMatch(e -> e.getLevel() == Level.WARN);
        assertFalse(hasWarn, "No WARN should be logged for year below floor");
    }

    // -------------------------------------------------------------------------
    // Case-insensitive country code
    // -------------------------------------------------------------------------

    @Test
    public void testCaseInsensitiveCountryCode() {
        assertEquals(new EidAlFitr("ae").apply(2025), new EidAlFitr("AE").apply(2025));
    }

    // -------------------------------------------------------------------------
    // test(year) mirrors apply(year) validity
    // -------------------------------------------------------------------------

    @Test
    public void testTestReturnsTrueForValidYear() {
        assertTrue(new EidAlFitr("AE").test(2025));
    }

    @Test
    public void testTestReturnsFalseForBeyondCeiling() {
        assertFalse(new EidAlFitr("AE").test(EidAlFitr.DATA_VALID_THROUGH + 1));
    }

    // -------------------------------------------------------------------------
    // Known dates — TR (Diyanet ilmi takvim)
    // -------------------------------------------------------------------------

    @DataProvider
    Iterator<Object[]> knownDatesTR() {
        return List.of(
            new Object[]{2024, LocalDate.of(2024, 4, 10)},
            new Object[]{2025, LocalDate.of(2025, 3, 30)},
            new Object[]{2026, LocalDate.of(2026, 3, 19)},
            new Object[]{2055, LocalDate.of(2055, 4, 28)}
        ).iterator();
    }

    @Test(dataProvider = "knownDatesTR")
    public void testKnownDatesTR(int year, LocalDate expected) {
        assertEquals(new EidAlFitr("TR").apply(year), expected,
                "Eid al-Fitr " + year + " per Diyanet must be " + expected);
    }

    // TR 2025 matches AE: both Diyanet and UAE SCA agree on March 30 for 1446 AH
    @Test
    public void testTR2025MatchesAE() {
        assertEquals(new EidAlFitr("TR").apply(2025), new EidAlFitr("AE").apply(2025),
                "Eid al-Fitr 2025: Diyanet (TR) and UAE SCA (AE) must agree on March 30");
    }

    // -------------------------------------------------------------------------
    // Known dates — QA (Qatar Central Bank official)
    // -------------------------------------------------------------------------

    @DataProvider
    Iterator<Object[]> knownDatesQA() {
        return List.of(
            new Object[]{2024, LocalDate.of(2024, 4, 10)},
            new Object[]{2025, LocalDate.of(2025, 3, 30)},
            new Object[]{2055, LocalDate.of(2055, 4, 28)}
        ).iterator();
    }

    @Test(dataProvider = "knownDatesQA")
    public void testKnownDatesQA(int year, LocalDate expected) {
        assertEquals(new EidAlFitr("QA").apply(year), expected,
                "Eid al-Fitr " + year + " per QCB must be " + expected);
    }

    // QA 2025 matches AE: both Qatar and UAE arrive at same date via Umm al-Qura
    @Test
    public void testQA2025MatchesAE() {
        assertEquals(new EidAlFitr("QA").apply(2025), new EidAlFitr("AE").apply(2025),
                "Eid al-Fitr 2025: Qatar (QCB) and UAE (SCA) must agree on March 30");
    }
}
