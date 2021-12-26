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

import com.github.davejoyce.calendar.function.Observance;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.davejoyce.calendar.TestObjects.*;
import static org.testng.Assert.*;

@Slf4j
public class FloatingHolidayTest {

    @Test(dataProvider = "data")
    public void testDateForYear(String name, Observance observance, int yearToCalculate, LocalDate expected) {
        FloatingHoliday holiday = new FloatingHoliday(name, "", observance);
        Optional<LocalDate> actual = holiday.dateForYear(yearToCalculate);
        assertTrue(actual.isPresent());
        assertEquals(actual.get(), expected);

        log.debug("{} {}: {}", name, yearToCalculate, actual.get());
    }

    @Test
    public void testGetObservance() {
        final Observance thanksGiving = (year) -> Year.of(year)
                                                      .atMonth(Month.NOVEMBER)
                                                      .atDay(1)
                                                      .with(TemporalAdjusters.lastInMonth(DayOfWeek.THURSDAY));
        FloatingHoliday holiday = new FloatingHoliday("Thanksgiving", "Thanksgiving (US)", thanksGiving);
        assertEquals(holiday.getObservance(), thanksGiving);
    }

    @Test
    public void testEquals() {
        final Observance observance = createObservanceThanksgiving();
        final Observance justNewYear = (year) -> LocalDate.of(year, Month.JANUARY, 1);
        FloatingHoliday holiday1 = new FloatingHoliday("Thanksgiving Day", "Day of giving thanks", observance);
        FloatingHoliday holiday2 = new FloatingHoliday("Thanksgiving Day", "Day of giving thanks", observance);
        FloatingHoliday holiday3 = new FloatingHoliday("Thanksgiving Day", "Day of giving thanks", justNewYear);
        FloatingHoliday holiday4 = new FloatingHoliday("Thanksgiving Day", "Friday before Easter Sunday", observance);
        Object notAHoliday = new Object();

        assertEquals(holiday1, holiday1);
        assertEquals(holiday2, holiday1);
        assertNotEquals(holiday1, null);
        assertNotEquals(notAHoliday, holiday1);
        assertNotEquals(holiday3, holiday1);
        assertNotEquals(holiday4, holiday1);
    }

    @Test
    public void testHashCode() {
        final Observance observance = createObservanceThanksgiving();
        FloatingHoliday holiday1 = new FloatingHoliday("Thanksgiving Day", "Day of giving thanks", observance);
        FloatingHoliday holiday2 = new FloatingHoliday("Thanksgiving Day", "Day of giving thanks", observance);
        assertEquals(holiday2.hashCode(), holiday1.hashCode());
    }

    @Test
    public void testToString() {
        final Observance observance = createObservanceThanksgiving();
        FloatingHoliday holiday = new FloatingHoliday("Thanksgiving Day", "Day of giving thanks", observance);
        String expected = "Holiday\\[name='Thanksgiving Day', description='Day of giving thanks', observance=.+]";
        Pattern pattern = Pattern.compile(expected);
        Matcher matcher = pattern.matcher(holiday.toString());
        assertTrue(matcher.matches());
        log.debug("FloatingHoliday (string): {}", holiday);
    }

    @DataProvider
    public Iterator<Object[]> data() {
        final Observance memorialDay = createObservanceMemorialDay();
        final Observance laborDay = createObservanceLaborDay();
        final Observance thanksGiving = createObservanceThanksgiving();

        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{ "Memorial Day", memorialDay, 1918, LocalDate.of(1918, Month.MAY, 30)});
        data.add(new Object[]{ "Labor Day", laborDay, 1918, LocalDate.of(1918, Month.SEPTEMBER, 2)});
        data.add(new Object[]{ "Thanksgiving", thanksGiving, 1918, LocalDate.of(1918, Month.NOVEMBER, 28)});
        data.add(new Object[]{ "Memorial Day", memorialDay, 2021, LocalDate.of(2021, Month.MAY, 31)});
        data.add(new Object[]{ "Labor Day", laborDay, 2021, LocalDate.of(2021, Month.SEPTEMBER, 6)});
        data.add(new Object[]{ "Thanksgiving", thanksGiving, 2021, LocalDate.of(2021, Month.NOVEMBER, 25)});
        return data.iterator();
    }

}