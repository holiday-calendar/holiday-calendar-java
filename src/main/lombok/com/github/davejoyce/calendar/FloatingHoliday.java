package com.github.davejoyce.calendar;

import com.github.davejoyce.calendar.function.Observance;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

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
                           Observance observance,
                           boolean rollable) {
        super(name, description, rollable);
        this.observance = requireNonNull(observance, "Argument 'observance' cannot be null");
    }

    public FloatingHoliday(String name,
                           String description,
                           Observance observance) {
        this(name, description, observance, true);
    }

    @Override
    public Optional<LocalDate> dateForYear(int year) {
        return Optional.ofNullable(observance.apply(year));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FloatingHoliday that = (FloatingHoliday) o;
        return observance.equals(that.observance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), observance);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Holiday.class.getSimpleName() + "[", "]")
                .add("name='" + getName() + "'")
                .add("description='" + getDescription() + "'")
                .add("observance=" + getObservance())
                .toString();
    }

}
