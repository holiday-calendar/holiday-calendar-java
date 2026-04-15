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

package org.holiday.calendar.observance.jp;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.LocalDate;
import java.time.Month;

/**
 * Observance of the Emperor's Birthday (天皇誕生日) — a Japanese national holiday
 * marking the birthday of the reigning Emperor.
 *
 * <p>The date changes with each new Emperor:
 * <ul>
 *   <li>1949–1988: April 29 (Emperor Hirohito, Showa era)</li>
 *   <li>1989–2018: December 23 (Emperor Akihito, Heisei era)</li>
 *   <li>2019: not observed (Emperor Akihito abdicated April 30; Emperor Naruhito
 *       acceded May 1 — no Emperor's Birthday was designated for 2019)</li>
 *   <li>2020+: February 23 (Emperor Naruhito, Reiwa era)</li>
 * </ul>
 *
 * <p>Note: April 29 continues as a public holiday under a different name (Showa Day
 * from 2007; Greenery Day 1989–2006) and is modelled separately in the calendar.
 */
public class EmperorsBirthday extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        if (year <= 1988) {
            return LocalDate.of(year, Month.APRIL, 29);
        } else if (year <= 2018) {
            return LocalDate.of(year, Month.DECEMBER, 23);
        } else {
            return LocalDate.of(year, Month.FEBRUARY, 23);
        }
    }

    @Override
    protected boolean isValidYear(int year) {
        return year >= 1949 && year != 2019;
    }
}
