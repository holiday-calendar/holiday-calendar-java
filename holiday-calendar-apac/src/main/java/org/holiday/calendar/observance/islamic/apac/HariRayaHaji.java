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

package org.holiday.calendar.observance.islamic.apac;

import org.holiday.calendar.observance.AbstractObservance;
import org.holiday.calendar.util.CsvObservanceLoader;

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
 * <p>Date data is loaded at runtime from {@code hari-raya-haji.csv} in this package.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HariRayaHaji extends AbstractObservance {

    private static final class DatesHolder {
        static final Map<Integer, LocalDate> DATA =
            CsvObservanceLoader.loadSingle(HariRayaHaji.class, "hari-raya-haji.csv");
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
