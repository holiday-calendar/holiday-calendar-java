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
 * Observance of Ashura (10 Muharram AH), the first day of the two-day Ashura
 * public holiday in Bahrain.
 *
 * <p>Ashura marks the 10th day of Muharram and is of particular significance for
 * Shia Muslims. Bahrain, which has a large Shia Muslim population, gazetted it as
 * a two-day national public holiday (10–11 Muharram). The Central Bank of Bahrain
 * (CBB) and Boursa Bahrain observe both days as closure days.</p>
 *
 * <p>Dates are determined by official moon sighting and cannot be computed
 * algorithmically. Dates for 2024–2026 are sourced from official CBB / Boursa
 * Bahrain holiday announcements. Dates for 2027–2055 are projected as
 * 10 Muharram (Islamic New Year + 9 days) from the Umm al-Qura tabular Islamic
 * calendar; verify against official CBB announcements as each year is published.</p>
 *
 * <p>The second day of Ashura (11 Muharram) is provided by {@link AshuraDay2}.</p>
 *
 * <p>Date data is loaded at runtime from {@code ashura-{countryCode}.csv} in this
 * package, where {@code countryCode} is the ISO 3166-1 alpha-2 country code in
 * lower case (e.g. {@code bh}).</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class Ashura extends AbstractObservance {

    static final int DATA_VALID_FROM = 2024;
    static final int DATA_VALID_THROUGH = 2055;

    private static final Logger log = LoggerFactory.getLogger(Ashura.class);
    private static final ConcurrentHashMap<String, Map<Integer, LocalDate>> CACHE =
            new ConcurrentHashMap<>();

    private final Map<Integer, LocalDate> dates;

    public Ashura(String countryCode) {
        this.dates = CACHE.computeIfAbsent(countryCode.toLowerCase(),
                cc -> CsvObservanceLoader.loadSingle(Ashura.class, "ashura-" + cc + ".csv"));
    }

    @Override
    protected LocalDate computeDate(int year) {
        return dates.get(year);
    }

    @Override
    protected boolean isValidYear(int year) {
        if (year > DATA_VALID_THROUGH) {
            log.warn("Year {} exceeds data ceiling {}; Ashura date unavailable", year, DATA_VALID_THROUGH);
            return false;
        }
        return dates.containsKey(year);
    }

}
