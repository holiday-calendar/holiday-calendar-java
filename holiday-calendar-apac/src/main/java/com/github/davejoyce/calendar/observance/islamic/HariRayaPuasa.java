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

package com.github.davejoyce.calendar.observance.islamic;

import com.github.davejoyce.calendar.observance.AbstractObservance;

import java.time.LocalDate;
import java.util.Map;

/**
 * Observance of Hari Raya Puasa (Eid al-Fitr), marking the end of Ramadan.
 * In Singapore, the observed date is officially gazetted by the government
 * and follows the local lunar sighting, which may differ from tabular
 * Islamic calendar calculations.
 *
 * <p>Dates are sourced from official Singapore public holiday announcements.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HariRayaPuasa extends AbstractObservance {

    private static final Map<Integer, LocalDate> DATES = Map.ofEntries(
        Map.entry(2019, LocalDate.of(2019, 6, 5)),
        Map.entry(2020, LocalDate.of(2020, 5, 24)),
        Map.entry(2021, LocalDate.of(2021, 5, 13)),
        Map.entry(2022, LocalDate.of(2022, 5, 3)),
        Map.entry(2023, LocalDate.of(2023, 4, 21)),
        Map.entry(2024, LocalDate.of(2024, 4, 10)),
        Map.entry(2025, LocalDate.of(2025, 3, 31)),
        Map.entry(2026, LocalDate.of(2026, 3, 20)),
        Map.entry(2027, LocalDate.of(2027, 3, 10)),
        Map.entry(2028, LocalDate.of(2028, 2, 27)),
        Map.entry(2029, LocalDate.of(2029, 2, 16)),
        Map.entry(2030, LocalDate.of(2030, 2, 5))
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
