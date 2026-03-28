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

package org.holiday.calendar.observance;

import org.holiday.calendar.function.Observance;

import java.time.LocalDate;

/**
 * Abstract base class for {@link Observance} implementations, providing
 * standard null-guard and year-validity logic.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractObservance implements Observance {

    @Override
    public final LocalDate apply(Integer year) {
        return (year != null && isValidYear(year)) ? computeDate(year) : null;
    }

    @Override
    public final boolean test(Integer year) {
        return year != null && isValidYear(year);
    }

    /**
     * Compute the date for this observance in the given year.
     * Only called when {@link #isValidYear(int)} returns {@code true}.
     *
     * @param year the year for which to compute the date
     * @return computed holiday date
     */
    protected abstract LocalDate computeDate(int year);

    /**
     * Determine whether this observance applies in the given year.
     * Defaults to {@code true} (all years are valid).
     *
     * @param year the year to test
     * @return {@code true} if this observance applies
     */
    protected boolean isValidYear(int year) {
        return true;
    }

}
