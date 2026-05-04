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
import org.holiday.calendar.util.CsvObservanceLoader;

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
 * <p>Date data is loaded at runtime from {@code vesak-day.csv} in this package.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class VesakDay extends AbstractObservance {

    private static final class DatesHolder {
        static final Map<Integer, LocalDate> DATA =
            CsvObservanceLoader.loadSingle(VesakDay.class, "vesak-day.csv");
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
