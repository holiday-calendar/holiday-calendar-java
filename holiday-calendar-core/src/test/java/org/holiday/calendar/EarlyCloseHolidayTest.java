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

package org.holiday.calendar;

import org.holiday.calendar.function.Observance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.*;

public class EarlyCloseHolidayTest {

    private static final Logger log = LoggerFactory.getLogger(EarlyCloseHolidayTest.class);

    private static final LocalTime CLOSE_TIME = LocalTime.of(13, 0);
    private static final ZoneId ZONE_JERUSALEM = ZoneId.of("Asia/Jerusalem");

    // A simple synthetic observance: eve of a fixed Dec 25 "holiday" — Dec 24 each year.
    private static Observance createObservanceEve() {
        return year -> LocalDate.of(year, Month.DECEMBER, 25).minusDays(1);
    }

    // -------------------------------------------------------------------------
    // Construction
    // -------------------------------------------------------------------------

    @Test
    public void testConstructor_ValidArgs() {
        Observance observance = createObservanceEve();
        EarlyCloseHoliday holiday = new EarlyCloseHoliday(
                "Erev Christmas", "Eve of Christmas", observance, CLOSE_TIME, ZONE_JERUSALEM);
        assertEquals(holiday.getName(), "Erev Christmas");
        assertEquals(holiday.getDescription(), "Eve of Christmas");
        assertEquals(holiday.getObservance(), observance);
        assertEquals(holiday.getCloseTime(), CLOSE_TIME);
        assertEquals(holiday.getZoneId(), ZONE_JERUSALEM);
        assertFalse(holiday.isRollable());
    }

    @Test
    public void testConstructor_NullDescription_DefaultsToEmptyString() {
        EarlyCloseHoliday holiday = new EarlyCloseHoliday(
                "Erev Christmas", null, createObservanceEve(), CLOSE_TIME, ZONE_JERUSALEM);
        assertNotNull(holiday.getDescription());
        assertEquals(holiday.getDescription(), "");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructor_NullName() {
        new EarlyCloseHoliday(null, "desc", createObservanceEve(), CLOSE_TIME, ZONE_JERUSALEM);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructor_NullObservance() {
        new EarlyCloseHoliday("Erev Christmas", "desc", null, CLOSE_TIME, ZONE_JERUSALEM);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructor_NullCloseTime() {
        new EarlyCloseHoliday("Erev Christmas", "desc", createObservanceEve(), null, ZONE_JERUSALEM);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructor_NullZoneId() {
        new EarlyCloseHoliday("Erev Christmas", "desc", createObservanceEve(), CLOSE_TIME, null);
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    @Test
    public void testGetObservance() {
        Observance observance = createObservanceEve();
        EarlyCloseHoliday holiday = new EarlyCloseHoliday(
                "Erev Christmas", "desc", observance, CLOSE_TIME, ZONE_JERUSALEM);
        assertEquals(holiday.getObservance(), observance);
    }

    @Test
    public void testGetCloseTime() {
        EarlyCloseHoliday holiday = new EarlyCloseHoliday(
                "Erev Christmas", "desc", createObservanceEve(), CLOSE_TIME, ZONE_JERUSALEM);
        assertEquals(holiday.getCloseTime(), CLOSE_TIME);
    }

    @Test
    public void testGetZoneId() {
        EarlyCloseHoliday holiday = new EarlyCloseHoliday(
                "Erev Christmas", "desc", createObservanceEve(), CLOSE_TIME, ZONE_JERUSALEM);
        assertEquals(holiday.getZoneId(), ZONE_JERUSALEM);
    }

    // -------------------------------------------------------------------------
    // isRollable
    // -------------------------------------------------------------------------

    @Test
    public void testIsRollable_AlwaysFalse() {
        EarlyCloseHoliday holiday = new EarlyCloseHoliday(
                "Erev Christmas", "desc", createObservanceEve(), CLOSE_TIME, ZONE_JERUSALEM);
        assertFalse(holiday.isRollable());
    }

    // -------------------------------------------------------------------------
    // dateForYear
    // -------------------------------------------------------------------------

    @Test(dataProvider = "data")
    public void testDateForYear(String name, Observance observance, int yearToCalculate, LocalDate expected) {
        EarlyCloseHoliday holiday = new EarlyCloseHoliday(name, "", observance, CLOSE_TIME, ZONE_JERUSALEM);
        Optional<LocalDate> actual = holiday.dateForYear(yearToCalculate);
        assertTrue(actual.isPresent());
        assertEquals(actual.get(), expected);

        log.debug("{} {}: {}", name, yearToCalculate, actual.get());
    }

    @DataProvider
    public Iterator<Object[]> data() {
        final Observance eve = createObservanceEve();
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ "Erev Christmas", eve, 2021, LocalDate.of(2021, Month.DECEMBER, 24)});
        data.add(new Object[]{ "Erev Christmas", eve, 2024, LocalDate.of(2024, Month.DECEMBER, 24)});
        data.add(new Object[]{ "Erev Christmas", eve, 2025, LocalDate.of(2025, Month.DECEMBER, 24)});
        return data.iterator();
    }

    // -------------------------------------------------------------------------
    // Equality / hashCode / toString
    // -------------------------------------------------------------------------

    @Test
    public void testEquals() {
        final Observance observance = createObservanceEve();
        final Observance otherObservance = year -> LocalDate.of(year, Month.JANUARY, 1);
        EarlyCloseHoliday holiday1 = new EarlyCloseHoliday("Erev Christmas", "Eve of Christmas", observance, CLOSE_TIME, ZONE_JERUSALEM);
        EarlyCloseHoliday holiday2 = new EarlyCloseHoliday("Erev Christmas", "Eve of Christmas", observance, CLOSE_TIME, ZONE_JERUSALEM);
        EarlyCloseHoliday holiday3 = new EarlyCloseHoliday("Erev Christmas", "Eve of Christmas", otherObservance, CLOSE_TIME, ZONE_JERUSALEM);
        EarlyCloseHoliday holiday4 = new EarlyCloseHoliday("Erev Christmas", "Different description", observance, CLOSE_TIME, ZONE_JERUSALEM);
        EarlyCloseHoliday holiday5 = new EarlyCloseHoliday("Erev Christmas", "Eve of Christmas", observance, LocalTime.of(9, 0), ZONE_JERUSALEM);
        EarlyCloseHoliday holiday6 = new EarlyCloseHoliday("Erev Christmas", "Eve of Christmas", observance, CLOSE_TIME, ZoneId.of("America/New_York"));
        Object notAHoliday = new Object();

        assertEquals(holiday1, holiday1);
        assertEquals(holiday2, holiday1);
        assertNotEquals(holiday1, null);
        assertNotEquals(notAHoliday, holiday1);
        assertNotEquals(holiday3, holiday1);
        assertNotEquals(holiday4, holiday1);
        assertNotEquals(holiday5, holiday1);
        assertNotEquals(holiday6, holiday1);
    }

    @Test
    public void testHashCode() {
        final Observance observance = createObservanceEve();
        EarlyCloseHoliday holiday1 = new EarlyCloseHoliday("Erev Christmas", "Eve of Christmas", observance, CLOSE_TIME, ZONE_JERUSALEM);
        EarlyCloseHoliday holiday2 = new EarlyCloseHoliday("Erev Christmas", "Eve of Christmas", observance, CLOSE_TIME, ZONE_JERUSALEM);
        assertEquals(holiday2.hashCode(), holiday1.hashCode());
    }

    @Test
    public void testToString() {
        EarlyCloseHoliday holiday = new EarlyCloseHoliday(
                "Erev Christmas", "Eve of Christmas", createObservanceEve(), CLOSE_TIME, ZONE_JERUSALEM);
        String expected = "Holiday\\[name='Erev Christmas', description='Eve of Christmas', observance=.+, closeTime=13:00, zoneId=Asia/Jerusalem]";
        Pattern pattern = Pattern.compile(expected);
        Matcher matcher = pattern.matcher(holiday.toString());
        assertTrue(matcher.matches());
        log.debug("EarlyCloseHoliday (string): {}", holiday);
    }

    // -------------------------------------------------------------------------
    // Sealed-type pattern matching / switch exhaustiveness
    // -------------------------------------------------------------------------

    @Test
    public void testSwitchExhaustiveness_EarlyCloseArm() {
        Holiday holiday = new EarlyCloseHoliday(
                "Erev Christmas", "Eve of Christmas", createObservanceEve(), CLOSE_TIME, ZONE_JERUSALEM);
        String result = describe(holiday);
        assertEquals(result, "early-close");
    }

    private String describe(Holiday holiday) {
        return switch (holiday) {
            case FixedHoliday fixed -> "fixed";
            case FloatingHoliday floating -> "floating";
            case SpecialAnniversary anniversary -> "special-anniversary";
            case EarlyCloseHoliday earlyClose -> "early-close";
        };
    }

}
