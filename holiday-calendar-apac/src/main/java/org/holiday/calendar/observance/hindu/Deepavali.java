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
import org.holiday.calendar.util.CsvObservanceLoader;

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
 * <p>Date data is loaded at runtime from {@code deepavali.csv} in this package.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class Deepavali extends AbstractObservance {

    private static final class DatesHolder {
        static final Map<Integer, LocalDate> DATA =
            CsvObservanceLoader.loadSingle(Deepavali.class, "deepavali.csv");
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
