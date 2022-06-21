/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2021 David Joyce
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

package com.github.davejoyce.calendar;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;

@Slf4j
public class SpecialAnniversaryTest {

    @Test
    public void testConstructor_3Args() {
        LocalDate jubileeDate = LocalDate.of(2022, Month.FEBRUARY, 6);
        SpecialAnniversary holiday = new SpecialAnniversary("Platinum Jubilee of Elizabeth II",
                "70th anniversary of the accession of Queen Elizabeth II", jubileeDate);
        assertEquals(holiday.getAnniversaryDate(), jubileeDate);
        assertEquals(holiday.getMonth(), Month.FEBRUARY);
        assertEquals(holiday.getDayOfMonth(), 6);
        assertFalse(holiday.isRollable());
    }

    @Test(dataProvider = "data")
    public void testDateForYear(String name, LocalDate anniversaryDate, int yearToCalculate, LocalDate expected) {
        SpecialAnniversary holiday = new SpecialAnniversary(name, "", anniversaryDate);
        Optional<LocalDate> actual = holiday.dateForYear(yearToCalculate);
        final String dateToLog;
        if (null == expected) {
            assertFalse(actual.isPresent());
            dateToLog = "empty";
        } else {
            assertTrue(actual.isPresent());
            assertEquals(actual.get(), expected);
            assertEquals(holiday.getYear(), yearToCalculate);
            dateToLog = actual.get().toString();
        }
        log.debug("{} {}: {}", name, yearToCalculate, dateToLog);
    }

    @Test
    public void testEquals() {
        LocalDate jubileeDate = LocalDate.of(2022, Month.FEBRUARY, 6);
        LocalDate notJubileeDate = LocalDate.of(2021, Month.FEBRUARY, 6);
        SpecialAnniversary holiday1 = new SpecialAnniversary("Platinum Jubilee of Elizabeth II", "70th anniversary of the accession of Queen Elizabeth II", jubileeDate);
        SpecialAnniversary holiday2 = new SpecialAnniversary("Platinum Jubilee of Elizabeth II", "70th anniversary of the accession of Queen Elizabeth II", jubileeDate);
        SpecialAnniversary holiday3 = new SpecialAnniversary("Platinum Jubilee of Elizabeth II", "70th anniversary of the accession of Queen Elizabeth II", notJubileeDate);
        SpecialAnniversary holiday4 = new SpecialAnniversary("Platinum Jubilee of Elizabeth II", "70th anniversary", jubileeDate);
        Object notAHoliday = new Object();

        assertEquals(holiday1, holiday1);
        assertEquals(holiday2, holiday1);
        assertNotEquals(holiday1, null);
        assertNotEquals(notAHoliday, holiday1);
        assertNotEquals(holiday3, holiday1);
        assertNotEquals(holiday4, holiday1);
        assertFalse(holiday1.equals(null));
        assertFalse(holiday1.equals(notAHoliday));
    }

    @Test
    public void testHashCode() {
        LocalDate jubileeDate = LocalDate.of(2022, Month.FEBRUARY, 6);
        SpecialAnniversary holiday1 = new SpecialAnniversary("Platinum Jubilee of Elizabeth II", "70th anniversary of the accession of Queen Elizabeth II", jubileeDate);
        SpecialAnniversary holiday2 = new SpecialAnniversary("Platinum Jubilee of Elizabeth II", "70th anniversary of the accession of Queen Elizabeth II", jubileeDate);
        assertEquals(holiday2.hashCode(), holiday1.hashCode());
    }

    @Test
    public void testToString() {
        LocalDate jubileeDate = LocalDate.of(2022, Month.FEBRUARY, 6);
        SpecialAnniversary holiday = new SpecialAnniversary("Platinum Jubilee of Elizabeth II", "70th anniversary of the accession of Queen Elizabeth II", jubileeDate);
        String expected = "Holiday[name='Platinum Jubilee of Elizabeth II', description='70th anniversary of the accession of Queen Elizabeth II', anniversaryDate=2022-02-06]";
        assertEquals(holiday.toString(), expected);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ "Platinum Jubilee of Elizabeth II", LocalDate.of(2022, Month.FEBRUARY, 6), 2022, LocalDate.of(2022, Month.FEBRUARY, 6)});
        data.add(new Object[]{ "Platinum Jubilee of Elizabeth II", LocalDate.of(2022, Month.FEBRUARY, 6), 2021, null});
        data.add(new Object[]{ "Diamond Jubilee of Elizabeth II", LocalDate.of(2012, Month.FEBRUARY, 6), 2012, LocalDate.of(2012, Month.FEBRUARY, 6)});
        data.add(new Object[]{ "Diamond Jubilee of Elizabeth II", LocalDate.of(2012, Month.FEBRUARY, 6), 1977, null});
        return data.iterator();
    }

}