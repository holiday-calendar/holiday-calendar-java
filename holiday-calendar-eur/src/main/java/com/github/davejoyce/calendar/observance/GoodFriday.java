package com.github.davejoyce.calendar.observance;

import com.github.davejoyce.calendar.function.EasterObservance;
import com.github.davejoyce.calendar.function.Observance;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

/**
 * Observance of Good Friday, the commemoration of the crucifixion of Jesus
 * Christ. Good Friday is a major Christian holiday that is widely instituted
 * as a legal holiday around the world. Good Friday is the Friday two days
 * preceding Easter Sunday.
 */
public class GoodFriday implements Observance {

    private final EasterObservance easterObservance;

    public GoodFriday(EasterObservance easterObservance) {
        this.easterObservance = requireNonNull(easterObservance, "Argument 'easterObservance' cannot be null");
    }

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        return easterObservance.apply(year).minusDays(2);
    }

    @Override
    public boolean test(Integer year) {
        return easterObservance.test(year);
    }

}
