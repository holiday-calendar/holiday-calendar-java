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

package org.holiday.calendar.observance.eg;

import org.holiday.calendar.observance.CompositeObservance;
import org.holiday.calendar.observance.christian.OrthodoxEaster;

import java.time.LocalDate;

/**
 * Observance of <em>Sham El-Nessim</em> (شم النسيم), the Egyptian spring festival.
 *
 * <p>Sham El-Nessim is celebrated on the day after Coptic Easter (Monday after
 * Coptic Easter Sunday). The Coptic Orthodox Church uses the same Julian-calendar
 * Easter algorithm as the Eastern Orthodox churches, implemented here via
 * {@link OrthodoxEaster}.
 *
 * <p>Sham El-Nessim is an ancient Egyptian public holiday predating both Christianity
 * and Islam. It is observed as a national public holiday in Egypt by all Egyptians
 * regardless of religion and is a bank and exchange closure day.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class ShamElNessim extends CompositeObservance {

    public ShamElNessim() {
        super(new OrthodoxEaster());
    }

    @Override
    protected LocalDate computeDate(int year) {
        return base.apply(year).plusDays(1);
    }

}
