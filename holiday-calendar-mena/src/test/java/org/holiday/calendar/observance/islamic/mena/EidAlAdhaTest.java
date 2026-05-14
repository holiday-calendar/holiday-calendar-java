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
}
