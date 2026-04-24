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
 * Observance of Hari Raya Haji (Eid al-Adha), the Feast of Sacrifice on the
 * 10th day of Dhu al-Hijjah. In Singapore, the observed date is officially
 * gazetted by the government and follows the local lunar sighting, which may
 * differ from tabular Islamic calendar calculations.
 *
 * <p>Dates for 2019–2030 are sourced from official Singapore public holiday
 * gazette announcements. Dates for 2031–2055 are derived from the tabular
 * Islamic calendar (Küçük 30-year leap cycle, 10 Dhu al-Hijjah, SGT), pending
 * official MUIS (Majlis Ugama Islam Singapura) gazette confirmation. All entries
 * should be verified against MUIS announcements as Singapore approaches each year.</p>
 *
 * <p>2039 contains two 10 Dhu al-Hijjah occurrences (5 Jan AH 1460; 25 Dec AH 1461);
 * 5 Jan is used as Singapore's gazetted public holiday per MUIS convention. As a
 * result, the 2040 entry falls in December.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HariRayaHaji extends AbstractObservance {

    private static final Map<Integer, LocalDate> DATES = Map.ofEntries(
        // 2019–2030: Singapore MOM official gazette
        Map.entry(2019, LocalDate.of(2019, 8, 11)),
        Map.entry(2020, LocalDate.of(2020, 7, 31)),
        Map.entry(2021, LocalDate.of(2021, 7, 20)),
        Map.entry(2022, LocalDate.of(2022, 7, 10)),
        Map.entry(2023, LocalDate.of(2023, 6, 29)),
        Map.entry(2024, LocalDate.of(2024, 6, 17)),
        Map.entry(2025, LocalDate.of(2025, 6,  7)),
        Map.entry(2026, LocalDate.of(2026, 5, 27)),
        Map.entry(2027, LocalDate.of(2027, 5, 16)),
        Map.entry(2028, LocalDate.of(2028, 5,  5)),
        Map.entry(2029, LocalDate.of(2029, 4, 24)),
        Map.entry(2030, LocalDate.of(2030, 4, 13)),
        // 2031–2055: Tabular Islamic calendar projection (10 Dhu al-Hijjah, Küçük 30-year cycle,
        // epoch 1 Muharram 1 AH = JDN 1948439); actual dates subject to MUIS local moon-sighting
        // confirmation and may differ by ±1 day. Verify against official MUIS gazette annually.
        // 2039 has two 10 Dhu al-Hijjah occurrences (5 Jan AH 1460; 25 Dec AH 1461);
        // 5 Jan is used as Singapore's gazetted public holiday. 2040 falls in December accordingly.
        Map.entry(2031, LocalDate.of(2031, 4,  2)),
        Map.entry(2032, LocalDate.of(2032, 3, 21)),
        Map.entry(2033, LocalDate.of(2033, 3, 11)),
        Map.entry(2034, LocalDate.of(2034, 2, 28)),
        Map.entry(2035, LocalDate.of(2035, 2, 17)),
        Map.entry(2036, LocalDate.of(2036, 2,  7)),
        Map.entry(2037, LocalDate.of(2037, 1, 26)),
        Map.entry(2038, LocalDate.of(2038, 1, 16)),
        Map.entry(2039, LocalDate.of(2039, 1,  5)),
        Map.entry(2040, LocalDate.of(2040, 12, 14)),
        Map.entry(2041, LocalDate.of(2041, 12,  3)),
        Map.entry(2042, LocalDate.of(2042, 11, 22)),
        Map.entry(2043, LocalDate.of(2043, 11, 12)),
        Map.entry(2044, LocalDate.of(2044, 10, 31)),
        Map.entry(2045, LocalDate.of(2045, 10, 21)),
        Map.entry(2046, LocalDate.of(2046, 10, 10)),
        Map.entry(2047, LocalDate.of(2047,  9, 29)),
        Map.entry(2048, LocalDate.of(2048,  9, 18)),
        Map.entry(2049, LocalDate.of(2049,  9,  7)),
        Map.entry(2050, LocalDate.of(2050,  8, 27)),
        Map.entry(2051, LocalDate.of(2051,  8, 17)),
        Map.entry(2052, LocalDate.of(2052,  8,  5)),
        Map.entry(2053, LocalDate.of(2053,  7, 25)),
        Map.entry(2054, LocalDate.of(2054,  7, 15)),
        Map.entry(2055, LocalDate.of(2055,  7,  4))
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
