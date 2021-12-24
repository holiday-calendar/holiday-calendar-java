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

package com.github.davejoyce.calendar.function;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Function for computation of observed date of a holiday for a specified year.
 * Implementations of this functional interface also behave as a Predicate for
 * determination of whether the represented holiday occurs in the given year.
 */
@FunctionalInterface
public interface Observance extends Function<Integer, LocalDate>, Predicate<Integer> {

    /**
     * {@inheritDoc}
     * Determines whether this observance applies to the specified year.
     *
     * @param year year of potential observance
     * @return true if the input year applies for this observance, otherwise false
     */
    @Override
    default boolean test(Integer year) {
        return true;
    }

}
