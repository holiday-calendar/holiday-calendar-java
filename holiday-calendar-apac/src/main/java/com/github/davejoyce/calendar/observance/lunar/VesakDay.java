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

package com.github.davejoyce.calendar.observance.lunar;

import com.github.davejoyce.calendar.observance.AbstractObservance;

import java.time.LocalDate;
import java.util.Map;

/**
 * Observance of Vesak Day (also known as Buddha's Birthday), commemorating
 * the birth, enlightenment, and death of Gautama Buddha. In Singapore, Vesak
 * Day falls on the full moon of the fourth month of the Chinese lunisolar
 * calendar, observed as a public holiday on the date officially gazetted by
 * the Singapore government.
 *
 * <p>Dates are sourced from official Singapore public holiday announcements.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class VesakDay extends AbstractObservance {

    private static final Map<Integer, LocalDate> DATES = Map.ofEntries(
        Map.entry(2019, LocalDate.of(2019, 5, 19)),
        Map.entry(2020, LocalDate.of(2020, 5, 7)),
        Map.entry(2021, LocalDate.of(2021, 5, 26)),
        Map.entry(2022, LocalDate.of(2022, 5, 15)),
        Map.entry(2023, LocalDate.of(2023, 6, 2)),
        Map.entry(2024, LocalDate.of(2024, 5, 22)),
        Map.entry(2025, LocalDate.of(2025, 5, 12)),
        Map.entry(2026, LocalDate.of(2026, 5, 31)),
        Map.entry(2027, LocalDate.of(2027, 5, 20)),
        Map.entry(2028, LocalDate.of(2028, 5, 8)),
        Map.entry(2029, LocalDate.of(2029, 5, 27)),
        Map.entry(2030, LocalDate.of(2030, 5, 16))
    );

    @Override
    protected LocalDate computeDate(int year) {
        return DATES.get(year);
    }

    @Override
    protected boolean isValidYear(int year) {
        return DATES.containsKey(year);
    }

}
