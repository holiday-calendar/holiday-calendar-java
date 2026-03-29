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

package com.github.davejoyce.calendar.observance.christian;

import com.github.davejoyce.calendar.observance.CompositeObservance;

import java.time.LocalDate;

/**
 * The 40th day of Easter. Ascension Day commemorates Jesus Christ's ascension
 * into heaven, according to Christian belief.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class AscensionDay extends CompositeObservance {

    public static final int DEFAULT_DAYS_AFTER_EASTER = 39;

    private final int daysAfterEaster;

    public AscensionDay(EasterObservance easterObservance, int daysAfterEaster) {
        super(easterObservance);
        if (DEFAULT_DAYS_AFTER_EASTER > daysAfterEaster) {
            throw new IllegalArgumentException("Argument 'daysAfterEaster' must be at least " + DEFAULT_DAYS_AFTER_EASTER);
        }
        this.daysAfterEaster = daysAfterEaster;
    }

    public AscensionDay(EasterObservance easterObservance) {
        this(easterObservance, DEFAULT_DAYS_AFTER_EASTER);
    }

    @Override
    protected LocalDate computeDate(int year) {
        return base.apply(year).plusDays(daysAfterEaster);
    }

}
