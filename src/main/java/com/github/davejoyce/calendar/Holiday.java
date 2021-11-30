package com.github.davejoyce.calendar;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import static java.util.Objects.requireNonNull;

/**
 * An official day on which activity does not occur. A holiday may be
 * traditional, religious, or governmental in origin. Its date of
 * occurrence may be fixed,
 * <a href="https://en.wikipedia.org/wiki/Moveable_feast">moveable</a>, or
 * otherwise computed.
 * <p>Subclasses which extend this class determine the method of holiday date
 * calculation.</p>
 */
public abstract class Holiday {

    private final String name;
    private final String description;

    public Holiday(String name, String description) {
        this.name = requireNonNull(name, "Argument 'name' cannot be null");
        this.description = Optional.ofNullable(description).orElse("");
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Calculate the date of this holiday for the specified year. The date
     * returned by this method may be adjusted to a different date of
     * observance by the associated {@link HolidayCalendar holiday calendar}.
     *
     * @param year full calendar year (e.g. 1977, 2021)
     * @return date of this holiday
     */
    public abstract LocalDate dateForYear(int year);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Holiday)) return false;
        Holiday holiday = (Holiday) o;
        return getName().equals(holiday.getName()) &&
               getDescription().equals(holiday.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Holiday.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .toString();
    }

}
