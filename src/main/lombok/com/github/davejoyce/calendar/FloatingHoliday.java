package com.github.davejoyce.calendar;

import com.github.davejoyce.calendar.function.Observance;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * A holiday which occurs on a different date every year. Examples of floating
 * holidays are <a href="https://en.wikipedia.org/wiki/Diwali">Diwali</a> and
 * <a href="https://en.wikipedia.org/wiki/Easter">Easter</a>. Calculation of
 * the observed date of a floating holiday can vary widely in complexity.
 *
 * @see Observance
 */
public class FloatingHoliday extends Holiday {

    @Getter
    @NonNull
    private final Observance observance;

    public FloatingHoliday(String name,
                           String description,
                           Observance observance) {
        super(name, description);
        this.observance = requireNonNull(observance, "Argument 'observance' cannot be null");
    }

    @Override
    public Optional<LocalDate> dateForYear(int year) {
        return Optional.ofNullable(observance.apply(year));
    }

}
