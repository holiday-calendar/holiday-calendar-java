/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2021-2026 The Holiday Calendar Project Contributors
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

package org.holiday.calendar;

import org.holiday.calendar.function.Observance;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * A holiday which occurs on a different date every year. Examples of floating
 * holidays are <a href="https://en.wikipedia.org/wiki/Diwali">Diwali</a> and
 * <a href="https://en.wikipedia.org/wiki/Easter">Easter</a>. Calculation of
 * the observed date of a floating holiday can vary widely in complexity.
 *
 * @see Observance
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class FloatingHoliday implements Holiday {

    private final String name;
    private final String description;
    private final Observance observance;
    private final boolean rollable;

    /**
     * Construct a new instance with explicit rollable control.
     */
    public FloatingHoliday(String name, String description, Observance observance, boolean rollable) {
        this.name = requireNonNull(name, "Argument 'name' cannot be null");
        this.description = Optional.ofNullable(description).orElse("");
        this.observance = requireNonNull(observance, "Argument 'observance' cannot be null");
        this.rollable = rollable;
    }

    /**
     * Construct a new instance that is {@link #isRollable() rollable} by default.
     */
    public FloatingHoliday(String name, String description, Observance observance) {
        this(name, description, observance, true);
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    @Override
    public boolean isRollable() { return rollable; }

    public Observance getObservance() { return observance; }

    @Override
    public Optional<LocalDate> dateForYear(int year) {
        return Optional.ofNullable(observance.apply(year));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FloatingHoliday that)) return false;
        return rollable == that.rollable
            && name.equals(that.name)
            && description.equals(that.description)
            && observance.equals(that.observance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, observance, rollable);
    }

    @Override
    public String toString() {
        return "Holiday[name='" + name + "', description='" + description
            + "', observance=" + observance + "]";
    }

}
