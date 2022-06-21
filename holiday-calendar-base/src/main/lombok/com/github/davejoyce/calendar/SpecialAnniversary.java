/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2022 David Joyce
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

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import static java.util.Objects.requireNonNull;

/**
 * A holiday which commemorates a particular anniversary of an event. Example
 * of a special anniversary is a Jubilee for a reigning monarch.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class SpecialAnniversary extends Holiday {

    @Getter
    @NonNull
    private final LocalDate anniversaryDate;

    public SpecialAnniversary(String name,
                              String description,
                              @NonNull LocalDate anniversaryDate,
                              boolean rollable) {
        super(name, description, rollable);
        this.anniversaryDate = requireNonNull(anniversaryDate, "Argument 'anniversaryDate' cannot be null");
    }

    public SpecialAnniversary(String name,
                              String description,
                              @NonNull LocalDate anniversaryDate) {
        this(name, description, anniversaryDate, false);
    }

    /**
     * Get month on which this holiday occurs.
     *
     * @return holiday month
     */
    public Month getMonth() {
        return anniversaryDate.getMonth();
    }

    /**
     * Get day of the month on which this holiday occurs.
     *
     * @return holiday day in month
     */
    public int getDayOfMonth() {
        return anniversaryDate.getDayOfMonth();
    }

    /**
     * Get year in which this holiday occurs.
     *
     * @return holiday year
     */
    public int getYear() {
        return anniversaryDate.getYear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<LocalDate> dateForYear(int year) {
        return anniversaryDate.getYear() == year
                ? Optional.of(anniversaryDate)
                : Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SpecialAnniversary that = (SpecialAnniversary) o;
        return anniversaryDate.equals(that.anniversaryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), anniversaryDate);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Holiday.class.getSimpleName() + "[", "]")
                .add("name='" + getName() + "'")
                .add("description='" + getDescription() + "'")
                .add("anniversaryDate=" + getAnniversaryDate())
                .toString();
    }

}
