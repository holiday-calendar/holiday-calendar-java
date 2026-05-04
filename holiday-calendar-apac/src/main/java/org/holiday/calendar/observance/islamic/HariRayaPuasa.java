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
import org.holiday.calendar.util.CsvObservanceLoader;

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
 * against MUIS/MOM gazette announcements at <a href="https://data.gov.sg/collections/691/view">...</a> as
 * each year is officially published.</p>
 *
 * <p>Note: Gregorian year 2033 contains two Eid al-Fitr occurrences (~January 4 and
 * ~December 24). Only the first (January) occurrence is recorded; the December
 * occurrence cannot be represented under the single-date-per-year-key design.</p>
 *
 * <p>Date data is loaded at runtime from {@code hari-raya-puasa.csv} in this package.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HariRayaPuasa extends AbstractObservance {

    private static final class DatesHolder {
        static final Map<Integer, LocalDate> DATA =
            CsvObservanceLoader.loadSingle(HariRayaPuasa.class, "hari-raya-puasa.csv");
    }

    @Override
    protected LocalDate computeDate(int year) {
        return DatesHolder.DATA.get(year);
    }

    @Override
    protected boolean isValidYear(int year) {
        return DatesHolder.DATA.containsKey(year);
    }

}
