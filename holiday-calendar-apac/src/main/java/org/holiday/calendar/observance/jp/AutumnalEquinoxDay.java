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
 * Observance of Autumnal Equinox Day (秋分の日) — a Japanese national holiday
 * observed on the day of the astronomical autumnal equinox in Japan Standard Time.
 *
 * <p>The exact date (September 22 or 23) is computed using the formula published
 * by the National Astronomical Observatory of Japan (NAOJ) and is valid for years
 * 1980–2099.
 */
public class AutumnalEquinoxDay extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        int day = (int) (23.2488 + 0.242194 * (year - 1980) - Math.floor((year - 1980) / 4.0));
        return LocalDate.of(year, Month.SEPTEMBER, day);
    }

    @Override
    protected boolean isValidYear(int year) {
        return year >= 1980 && year <= 2099;
    }
}
