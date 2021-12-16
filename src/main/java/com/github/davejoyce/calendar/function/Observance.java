package com.github.davejoyce.calendar.function;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Function for computation of observed date of a holiday for a specified year.
 * Implementations of this functional interface also behave as a Predicate for
 * determination of whether the represented holiday occurs in the given year.
 */
public interface Observance extends Function<Integer, LocalDate>, Predicate<Integer> {

    /**
     * {@inheritDoc}
     * Determines whether this observance applies to the specified year.
     *
     * @param integer year of potential observance
     * @return true if the input argument matches the predicate, otherwise false
     */
    @Override
    default boolean test(Integer integer) {
        return true;
    }

}
