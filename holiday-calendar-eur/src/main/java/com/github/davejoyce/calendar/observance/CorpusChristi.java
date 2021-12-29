/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2021 David Joyce
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

package com.github.davejoyce.calendar.observance;

import com.github.davejoyce.calendar.function.EasterObservance;
import com.github.davejoyce.calendar.function.Observance;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

/**
 * Observance of the Feast of Corpus Christi, a liturgical solemnity celebrated
 * in the Roman Catholic, Anglican, and Western Orthodox churches. The holiday
 * is celebrated on the Thursday 60 days after Easter, or on the following
 * Sunday.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class CorpusChristi implements Observance {

    private final EasterObservance easterObservance;
    private final boolean adjustToSunday;

    public CorpusChristi(EasterObservance easterObservance,
                         boolean adjustToSunday) {
        this.easterObservance = requireNonNull(easterObservance, "Argument 'easterObservance' cannot be null");
        this.adjustToSunday = adjustToSunday;
    }

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        LocalDate actual = easterObservance.apply(year).plusDays(60);
        return adjustToSunday ? actual.plusDays(3) : actual;
    }

    @Override
    public boolean test(Integer year) {
        return easterObservance.test(year);
    }

}
