package com.github.davejoyce.calendar.observance;

import com.github.davejoyce.calendar.function.EasterObservance;
import com.github.davejoyce.calendar.function.Observance;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

/**
 * Observance of Whit Sunday (also known as Whitsun or Pentecost), the
 * commemoration of the descent of the Holy Spirit upon Christ's disciples. It
 * is observed on the 7th Sunday after Easter, or exactly 49 days following
 * Easter Sunday.
 */
public class WhitSunday implements Observance {

    private final EasterObservance easterObservance;

    public WhitSunday(EasterObservance easterObservance) {
        this.easterObservance = requireNonNull(easterObservance, "Argument 'easterObservance' cannot be null");
    }

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        return easterObservance.apply(year).plusDays(49);
    }

    @Override
    public boolean test(Integer year) {
        return easterObservance.test(year);
    }

}
