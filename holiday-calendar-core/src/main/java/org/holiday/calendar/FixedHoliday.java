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

package org.holiday.calendar;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * A holiday which occurs on the same date every year. Examples of a fixed
 * holiday are
 * <a href="https://en.wikipedia.org/wiki/New_Year%27s_Day">New Year's Day</a>
 * and a national independence day.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class FixedHoliday implements Holiday {

    private final String name;
    private final String description;
    private final MonthDay monthDay;
    private final boolean rollable;

    /**
     * Construct a new instance of a fixed holiday occurring on the given month
     * and day. The constructed holiday will be {@link #isRollable() rollable} by
     * default.
     */
    public FixedHoliday(String name, String description, Month month, int dayOfMonth) {
        this(name, description, MonthDay.of(month, dayOfMonth), true);
    }

    /**
     * Construct a new instance of a fixed holiday occurring on the given month
     * and day. The constructed holiday will be {@link #isRollable() rollable} by
     * default.
     */
    public FixedHoliday(String name, String description, String monthAndDay) {
        this(name, description, MonthDay.parse(monthAndDay), true);
    }

    /**
     * Construct a new instance of a fixed holiday occurring on the given month
     * and day, with explicit rollable control.
     */
    public FixedHoliday(String name, String description, Month month, int dayOfMonth, boolean rollable) {
        this(name, description, MonthDay.of(month, dayOfMonth), rollable);
    }

    /**
     * Construct a new instance of a fixed holiday occurring on the given month
     * and day, with explicit rollable control.
     */
    public FixedHoliday(String name, String description, String monthAndDay, boolean rollable) {
        this(name, description, MonthDay.parse(monthAndDay), rollable);
    }

    /**
     * Canonical constructor.
     */
    public FixedHoliday(String name, String description, MonthDay monthDay, boolean rollable) {
        this.name = requireNonNull(name, "Argument 'name' cannot be null");
        this.description = Optional.ofNullable(description).orElse("");
        this.monthDay = requireNonNull(monthDay, "Argument 'monthDay' cannot be null");
        this.rollable = rollable;
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    @Override
    public boolean isRollable() { return rollable; }

    public MonthDay getMonthDay() { return monthDay; }

    public Month getMonth() { return monthDay.getMonth(); }

    public int getDayOfMonth() { return monthDay.getDayOfMonth(); }

    @Override
    public Optional<LocalDate> dateForYear(int year) {
        return Optional.of(LocalDate.of(year, monthDay.getMonth(), monthDay.getDayOfMonth()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FixedHoliday that)) return false;
        return rollable == that.rollable
            && name.equals(that.name)
            && description.equals(that.description)
            && monthDay.equals(that.monthDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, monthDay, rollable);
    }

    @Override
    public String toString() {
        return "Holiday[name='" + name + "', description='" + description
            + "', month=" + getMonth() + ", dayOfMonth=" + getDayOfMonth() + "]";
    }

}
