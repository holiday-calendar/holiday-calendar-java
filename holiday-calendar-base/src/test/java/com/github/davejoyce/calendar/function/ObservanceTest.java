/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2022 David Joyce
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

package com.github.davejoyce.calendar.function;

import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;

import static org.testng.Assert.*;

public class ObservanceTest {

    @Test
    public void testDefaultTest() {
        Observance o = (year) -> Year.of(year).atMonth(Month.JANUARY).atDay(1).with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY));
        assertTrue(o.test(2021));
    }

    @Test
    public void testMinValueTest() {
        Observance o = new Observance() {
            @Override
            public LocalDate apply(Integer year) {
                return test(year)
                        ? Year.of(year).atMonth(Month.JANUARY).atDay(1).with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY))
                        : null;
            }

            @Override
            public boolean test(Integer year) {
                return 1986 <= year;
            }
        };
        assertTrue(o.test(2021));
        assertFalse(o.test(1985));
    }

}