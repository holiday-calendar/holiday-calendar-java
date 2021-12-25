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
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class FloatingHoliday extends Holiday {

    @Getter
    @NonNull
    private final Observance observance;

    /**
     * Construct a new instance of a fixed holiday with the specified name,
     * description, and occurring on the given month and day. The constructed
     * holiday object will support or disallow {@link DateRoll date roll}, based
     * upon the specified boolean flag argument.
     *
     * @param name name of this holiday
     * @param description brief description of this holiday
     * @param observance observance algorithm to calculate dates of this holiday
     * @param rollable flag indicating whether this holiday may be rolled for
     *                 observance
     */
    public FloatingHoliday(String name,
                           String description,
                           Observance observance,
                           boolean rollable) {
        super(name, description, rollable);
        this.observance = requireNonNull(observance, "Argument 'observance' cannot be null");
    }

    /**
     * Construct a new instance of a fixed holiday with the specified name,
     * description, and occurring on the given month and day. The constructed
     * holiday object will be {@link #isRollable() rollable} by default.
     *
     * @param name name of this holiday
     * @param description brief description of this holiday
     * @param observance observance algorithm to calculate dates of this holiday
     */
    public FloatingHoliday(String name,
                           String description,
                           Observance observance) {
        this(name, description, observance, true);
    }

    /**
     * {@inheritDoc}
     */
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
