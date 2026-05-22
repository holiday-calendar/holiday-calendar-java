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

import java.time.LocalDate;

/**
 * Observance of the second day of Ashura (11 Muharram AH).
 *
 * <p>The date is synthesized by advancing the first-day date ({@link Ashura})
 * by one calendar day. Data validity range and country-specific CSV source are
 * inherited from the first-day observance.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class AshuraDay2 extends AbstractObservance {

    private final Ashura base;

    public AshuraDay2(String countryCode) {
        this.base = new Ashura(countryCode);
    }

    @Override
    protected LocalDate computeDate(int year) {
        return base.apply(year).plusDays(1);
    }

    @Override
    protected boolean isValidYear(int year) {
        return base.test(year);
    }

}
