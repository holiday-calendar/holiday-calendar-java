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

package org.holiday.calendar.observance.hindu;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.LocalDate;
import java.util.Map;

/**
 * Observance of Deepavali (Diwali), the Hindu Festival of Lights. In
 * Singapore, Deepavali is observed on the first day of the Tamil month of
 * Karthigai and is officially gazetted by the Singapore government each year.
 *
 * <p>Dates for 2019–2030 are sourced from official Singapore Ministry of Manpower (MOM)
 * public holiday gazette notifications. Dates for 2031–2055 are derived from Tamil
 * Panchang calculations (Karthigai month commencement / Karthigai Amavasai, IST/SGT)
 * cross-referenced with Drik Panchang astronomical tables, pending official Singapore
 * MOM public holiday gazette confirmation. All entries should be verified against MOM
 * announcements as Singapore approaches each year.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class Deepavali extends AbstractObservance {

    private static final Map<Integer, LocalDate> DATES = Map.ofEntries(
        // 2019–2030: Singapore MOM official gazette
        Map.entry(2019, LocalDate.of(2019, 10, 27)),
        Map.entry(2020, LocalDate.of(2020, 11, 14)),
        Map.entry(2021, LocalDate.of(2021, 11,  4)),
        Map.entry(2022, LocalDate.of(2022, 10, 24)),
        Map.entry(2023, LocalDate.of(2023, 11, 12)),
        Map.entry(2024, LocalDate.of(2024, 10, 31)),
        Map.entry(2025, LocalDate.of(2025, 10, 20)),
        Map.entry(2026, LocalDate.of(2026, 11,  8)),
        Map.entry(2027, LocalDate.of(2027, 10, 29)),
        Map.entry(2028, LocalDate.of(2028, 10, 17)),
        Map.entry(2029, LocalDate.of(2029, 11,  5)),
        Map.entry(2030, LocalDate.of(2030, 10, 26)),
        // 2031–2055: Tamil Panchang projection (1st day of Tamil month Karthigai, SGT),
        // cross-referenced with Drik Panchang astronomical tables; verify against MOM
        // gazette as each year is officially published.
        Map.entry(2031, LocalDate.of(2031, 11, 14)),
        Map.entry(2032, LocalDate.of(2032, 11,  2)),
        Map.entry(2033, LocalDate.of(2033, 10, 23)),
        Map.entry(2034, LocalDate.of(2034, 11, 10)),
        Map.entry(2035, LocalDate.of(2035, 10, 31)),
        Map.entry(2036, LocalDate.of(2036, 10, 19)),
        Map.entry(2037, LocalDate.of(2037, 11,  7)),
        Map.entry(2038, LocalDate.of(2038, 10, 27)),
        Map.entry(2039, LocalDate.of(2039, 10, 16)),
        Map.entry(2040, LocalDate.of(2040, 11,  3)),
        Map.entry(2041, LocalDate.of(2041, 10, 24)),
        Map.entry(2042, LocalDate.of(2042, 11, 11)),
        Map.entry(2043, LocalDate.of(2043, 11,  1)),
        Map.entry(2044, LocalDate.of(2044, 10, 20)),
        Map.entry(2045, LocalDate.of(2045, 11,  8)),
        Map.entry(2046, LocalDate.of(2046, 10, 29)),
        Map.entry(2047, LocalDate.of(2047, 10, 19)),
        Map.entry(2048, LocalDate.of(2048, 11,  5)),
        Map.entry(2049, LocalDate.of(2049, 10, 25)),
        Map.entry(2050, LocalDate.of(2050, 10, 15)),
        Map.entry(2051, LocalDate.of(2051, 11,  3)),
        Map.entry(2052, LocalDate.of(2052, 10, 22)),
        Map.entry(2053, LocalDate.of(2053, 11, 10)),
        Map.entry(2054, LocalDate.of(2054, 10, 30)),
        Map.entry(2055, LocalDate.of(2055, 10, 19))
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
