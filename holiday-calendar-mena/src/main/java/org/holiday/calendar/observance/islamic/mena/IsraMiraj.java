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

package org.holiday.calendar.observance.islamic.mena;

import org.holiday.calendar.observance.AbstractObservance;
import org.holiday.calendar.util.CsvObservanceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Observance of Isra and Mi'raj (27 Rajab AH), commemorating the night journey
 * and ascension of the Prophet Muhammad.
 *
 * <p>Dates are determined by official moon sighting and cannot be computed
 * algorithmically. Dates for 2024–2026 are sourced from official country
 * exchange and central bank holiday announcements. Dates for 2027–2055 are
 * projected from the Umm al-Qura tabular Islamic calendar (27 Rajab AH =
 * 1 Muharram + 204 days); verify against official announcements as each year
 * is published.</p>
 *
 * <p>Note: Gregorian year 2027 contains two 27 Rajab occurrences (~January 6
 * AH 1448 and ~December 25 AH 1449). Only the first (January) occurrence is
 * recorded in the CSV. The December occurrence cannot be represented under the
 * single-date-per-year-key design.</p>
 *
 * <p>Date data is loaded at runtime from {@code isra-miraj-{countryCode}.csv}
 * in this package, where {@code countryCode} is the ISO 3166-1 alpha-2 country
 * code in lower case (e.g. {@code kw}).</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class IsraMiraj extends AbstractObservance {

    static final int DATA_VALID_FROM = 2024;
    static final int DATA_VALID_THROUGH = 2055;

    private static final Logger log = LoggerFactory.getLogger(IsraMiraj.class);
    private static final ConcurrentHashMap<String, Map<Integer, LocalDate>> CACHE =
            new ConcurrentHashMap<>();

    private final Map<Integer, LocalDate> dates;

    public IsraMiraj(String countryCode) {
        this.dates = CACHE.computeIfAbsent(countryCode.toLowerCase(),
                cc -> CsvObservanceLoader.loadSingle(IsraMiraj.class, "isra-miraj-" + cc + ".csv"));
    }

    @Override
    protected LocalDate computeDate(int year) {
        return dates.get(year);
    }

    @Override
    protected boolean isValidYear(int year) {
        if (year > DATA_VALID_THROUGH) {
            log.warn("Year {} exceeds data ceiling {}; Isra and Mi'raj date unavailable", year, DATA_VALID_THROUGH);
            return false;
        }
        return dates.containsKey(year);
    }

}
