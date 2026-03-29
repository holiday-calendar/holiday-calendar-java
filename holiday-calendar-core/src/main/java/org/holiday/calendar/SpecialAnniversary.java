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

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * A holiday which commemorates a particular anniversary of an event. Example
 * of a special anniversary is a Jubilee for a reigning monarch.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class SpecialAnniversary implements Holiday {

    private final String name;
    private final String description;
    private final LocalDate anniversaryDate;
    private final boolean rollable;

    public SpecialAnniversary(String name, String description, LocalDate anniversaryDate, boolean rollable) {
        this.name = requireNonNull(name, "Argument 'name' cannot be null");
        this.description = Optional.ofNullable(description).orElse("");
        this.anniversaryDate = requireNonNull(anniversaryDate, "Argument 'anniversaryDate' cannot be null");
        this.rollable = rollable;
    }

    public SpecialAnniversary(String name, String description, LocalDate anniversaryDate) {
        this(name, description, anniversaryDate, false);
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    @Override
    public boolean isRollable() { return rollable; }

    public LocalDate getAnniversaryDate() { return anniversaryDate; }

    public Month getMonth() { return anniversaryDate.getMonth(); }

    public int getDayOfMonth() { return anniversaryDate.getDayOfMonth(); }

    public int getYear() { return anniversaryDate.getYear(); }

    @Override
    public Optional<LocalDate> dateForYear(int year) {
        return anniversaryDate.getYear() == year
            ? Optional.of(anniversaryDate)
            : Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpecialAnniversary that)) return false;
        return rollable == that.rollable
            && name.equals(that.name)
            && description.equals(that.description)
            && anniversaryDate.equals(that.anniversaryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, anniversaryDate, rollable);
    }

    @Override
    public String toString() {
        return "Holiday[name='" + name + "', description='" + description
            + "', anniversaryDate=" + anniversaryDate + "]";
    }

}
