package com.github.davejoyce.calendar.holiday;

import com.github.davejoyce.calendar.function.EasterObservance;
import com.github.davejoyce.calendar.function.Observance;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

/**
 * Observance of Easter Monday - that is, the day after Easter Sunday. Easter
 * Monday is a public holiday in several countries.
 */
public class EasterMonday implements Observance {

    private final EasterObservance easterObservance;

    public EasterMonday(EasterObservance easterObservance) {
        this.easterObservance = requireNonNull(easterObservance, "Argument 'easterObservance' cannot be null");
    }

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        return easterObservance.apply(year).plusDays(1);
    }

    @Override
    public boolean test(Integer year) {
        return easterObservance.test(year);
    }

}
