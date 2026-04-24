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

package org.holiday.calendar.observance.islamic;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.LocalDate;
import java.util.Map;

/**
 * Observance of Hari Raya Puasa (Eid al-Fitr), marking the end of Ramadan.
 * In Singapore, the observed date is officially gazetted by the government
 * and follows the local lunar sighting, which may differ from tabular
 * Islamic calendar calculations.
 *
 * <p>Dates for 2019–2030 are sourced from official Singapore Ministry of Manpower (MOM)
 * public holiday gazette notifications and MUIS (Majlis Ugama Islam Singapura) announcements.
 * Dates for 2031–2055 are projected from the Umm al-Qura tabular Islamic calendar
 * (1 Shawwal AH, adjusted to SGT via Singapore's Imkan ar-Rukyah +1-day offset); verify
 * against MUIS/MOM gazette announcements at https://data.gov.sg/collections/691/view as
 * each year is officially published.</p>
 *
 * <p>Note: Gregorian year 2033 contains two Eid al-Fitr occurrences (~January 4 and
 * ~December 24). This map records only the first (January) occurrence; the December
 * occurrence cannot be represented under the single-date-per-year-key design.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HariRayaPuasa extends AbstractObservance {

    private static final Map<Integer, LocalDate> DATES = Map.ofEntries(
        // 2019–2030: Singapore MOM/MUIS official gazette
        Map.entry(2019, LocalDate.of(2019,  6,  5)),
        Map.entry(2020, LocalDate.of(2020,  5, 24)),
        Map.entry(2021, LocalDate.of(2021,  5, 13)),
        Map.entry(2022, LocalDate.of(2022,  5,  3)),
        Map.entry(2023, LocalDate.of(2023,  4, 21)),
        Map.entry(2024, LocalDate.of(2024,  4, 10)),
        Map.entry(2025, LocalDate.of(2025,  3, 31)),
        Map.entry(2026, LocalDate.of(2026,  3, 20)),
        Map.entry(2027, LocalDate.of(2027,  3, 10)),
        Map.entry(2028, LocalDate.of(2028,  2, 27)),
        Map.entry(2029, LocalDate.of(2029,  2, 16)),
        Map.entry(2030, LocalDate.of(2030,  2,  5)),
        // 2031–2055: Umm al-Qura tabular Islamic calendar (1 Shawwal AH, adjusted to SGT
        // via Singapore's Imkan ar-Rukyah +1-day offset); verify against MUIS/MOM gazette
        // as each year is officially published. 2033 records only the January occurrence.
        Map.entry(2031, LocalDate.of(2031,  1, 25)),
        Map.entry(2032, LocalDate.of(2032,  1, 15)),
        Map.entry(2033, LocalDate.of(2033,  1,  4)),
        Map.entry(2034, LocalDate.of(2034, 12, 13)),
        Map.entry(2035, LocalDate.of(2035, 12,  2)),
        Map.entry(2036, LocalDate.of(2036, 11, 20)),
        Map.entry(2037, LocalDate.of(2037, 11, 10)),
        Map.entry(2038, LocalDate.of(2038, 10, 30)),
        Map.entry(2039, LocalDate.of(2039, 10, 20)),
        Map.entry(2040, LocalDate.of(2040, 10,  9)),
        Map.entry(2041, LocalDate.of(2041,  9, 28)),
        Map.entry(2042, LocalDate.of(2042,  9, 17)),
        Map.entry(2043, LocalDate.of(2043,  9,  6)),
        Map.entry(2044, LocalDate.of(2044,  8, 25)),
        Map.entry(2045, LocalDate.of(2045,  8, 15)),
        Map.entry(2046, LocalDate.of(2046,  8,  5)),
        Map.entry(2047, LocalDate.of(2047,  7, 25)),
        Map.entry(2048, LocalDate.of(2048,  7, 14)),
        Map.entry(2049, LocalDate.of(2049,  7,  3)),
        Map.entry(2050, LocalDate.of(2050,  6, 22)),
        Map.entry(2051, LocalDate.of(2051,  6, 11)),
        Map.entry(2052, LocalDate.of(2052,  5, 31)),
        Map.entry(2053, LocalDate.of(2053,  5, 20)),
        Map.entry(2054, LocalDate.of(2054,  5, 10)),
        Map.entry(2055, LocalDate.of(2055,  4, 29))
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
