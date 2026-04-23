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

package org.holiday.calendar.observance.lunar;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.LocalDate;
import java.util.Map;

/**
 * Observance of Vesak Day (also known as Buddha's Birthday), commemorating
 * the birth, enlightenment, and death of Gautama Buddha. In Singapore, Vesak
 * Day falls on the full moon of the fourth month of the Chinese lunisolar
 * calendar, observed as a public holiday on the date officially gazetted by
 * the Singapore government.
 *
 * <p>Dates for 2019–2030 are sourced from official Singapore Ministry of Manpower (MOM)
 * public holiday gazette notifications. Dates for 2031–2055 are pre-computed from the
 * Hong Kong Observatory Gregorian-Lunar Calendar Conversion Tables (day 15 of Chinese
 * lunisolar month 4 in SGT), pending official MOM gazette confirmation. All entries
 * should be verified against MOM announcements as Singapore approaches each year.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class VesakDay extends AbstractObservance {

    private static final Map<Integer, LocalDate> DATES = Map.ofEntries(
        // 2019–2030: Singapore MOM official gazette
        Map.entry(2019, LocalDate.of(2019, 5, 19)),
        Map.entry(2020, LocalDate.of(2020, 5,  7)),
        Map.entry(2021, LocalDate.of(2021, 5, 26)),
        Map.entry(2022, LocalDate.of(2022, 5, 15)),
        Map.entry(2023, LocalDate.of(2023, 6,  2)),
        Map.entry(2024, LocalDate.of(2024, 5, 22)),
        Map.entry(2025, LocalDate.of(2025, 5, 12)),
        Map.entry(2026, LocalDate.of(2026, 5, 31)),
        Map.entry(2027, LocalDate.of(2027, 5, 20)),
        Map.entry(2028, LocalDate.of(2028, 5,  8)),
        Map.entry(2029, LocalDate.of(2029, 5, 27)),
        Map.entry(2030, LocalDate.of(2030, 5, 16)),
        // 2031–2055: HKO Gregorian-Lunar Conversion Tables (M4 day 15 in SGT);
        // verify against MOM gazette as each year is officially published.
        Map.entry(2031, LocalDate.of(2031, 6,  4)),
        Map.entry(2032, LocalDate.of(2032, 5, 23)),
        Map.entry(2033, LocalDate.of(2033, 5, 13)),
        Map.entry(2034, LocalDate.of(2034, 6,  1)),
        Map.entry(2035, LocalDate.of(2035, 5, 22)),
        Map.entry(2036, LocalDate.of(2036, 5, 10)),
        Map.entry(2037, LocalDate.of(2037, 5, 29)),
        Map.entry(2038, LocalDate.of(2038, 5, 18)),
        Map.entry(2039, LocalDate.of(2039, 5,  7)),
        Map.entry(2040, LocalDate.of(2040, 5, 25)),
        Map.entry(2041, LocalDate.of(2041, 5, 14)),
        Map.entry(2042, LocalDate.of(2042, 6,  2)),
        Map.entry(2043, LocalDate.of(2043, 5, 23)),
        Map.entry(2044, LocalDate.of(2044, 5, 12)),
        Map.entry(2045, LocalDate.of(2045, 5, 31)),
        Map.entry(2046, LocalDate.of(2046, 5, 20)),
        Map.entry(2047, LocalDate.of(2047, 5,  9)),
        Map.entry(2048, LocalDate.of(2048, 5, 27)), // computed via Xiaoman method; HKO 2048e PDF is image-rendered
        Map.entry(2049, LocalDate.of(2049, 5, 16)),
        Map.entry(2050, LocalDate.of(2050, 6,  4)),
        Map.entry(2051, LocalDate.of(2051, 5, 24)),
        Map.entry(2052, LocalDate.of(2052, 5, 13)),
        Map.entry(2053, LocalDate.of(2053, 6,  1)),
        Map.entry(2054, LocalDate.of(2054, 5, 22)),
        Map.entry(2055, LocalDate.of(2055, 5, 11))
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
