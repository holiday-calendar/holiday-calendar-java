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
 * Observance of Ash Wednesday - the first day of the Christian season of Lent.
 * This holiday is traditionally observed by <em>Western</em> Christians.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class AshWednesday implements Observance {

    private final EasterObservance easterObservance;

    public AshWednesday(EasterObservance easterObservance) {
        this.easterObservance = requireNonNull(easterObservance, "Argument 'easterObservance' cannot be null");
    }

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        return easterObservance.apply(year).minusDays(46);
    }

    @Override
    public boolean test(Integer year) {
        return easterObservance.test(year);
    }

}
