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

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * An official day on which work does not occur. A holiday may be traditional,
 * religious, or governmental in origin. Its date of occurrence may be fixed,
 * <a href="https://en.wikipedia.org/wiki/Moveable_feast">moveable</a>, or
 * otherwise computed.
 * <p>Subclasses which extend this class determine the method of holiday date
 * calculation.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class Holiday {

    /**
     * Enumerated type of {@link Holiday}.
     */
    public enum Type {
        FIXED, FLOATING, SPECIAL_ANNIVERSARY
    }

    /**
     * Builder for configurable construction of {@link Holiday} objects;
     */
    public static class HolidayBuilder {

        private Type type = Type.FIXED;
        private String name;
        private String description;
        private boolean rollable = true;
        private MonthDay monthDay;
        private Observance observance;
        private LocalDate anniversaryDate;

        /**
         * Package private constructor. Not intended to be called directly by
         * client code.
         *
         * @see #builder()
         */
        HolidayBuilder() {}

        /**
         * Set the type of holiday to build.
         *
         * @param type holiday type
         * @return this builder
         */
        public HolidayBuilder type(final Type type) {
            this.type = type;
            return this;
        }

        /**
         * Set the type of holiday to build.
         *
         * @param type holiday type
         * @return this builder
         */
        public HolidayBuilder type(final String type) {
            return type(Type.valueOf(type.trim().toUpperCase()));
        }


        /**
         * Set the name of holiday to build.
         *
         * @param name holiday name
         * @return this builder
         */
        public HolidayBuilder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * Set the description of holiday to build.
         *
         * @param description holiday description
         * @return this builder
         */
        public HolidayBuilder description(final String description) {
            this.description = description;
            return this;
        }

        /**
         * Specify rollable nature of holiday to build.
         *
         * @param rollable flag indicating whether the holiday can be rolled
         * @return this builder
         */
        public HolidayBuilder rollable(final boolean rollable) {
            this.rollable = rollable;
            return this;
        }

        /**
         * Set the day of the month of holiday to build.
         *
         * @param monthDay month and day
         * @return this builder
         */
        public HolidayBuilder monthDay(final MonthDay monthDay) {
            this.monthDay = monthDay;
            return this;
        }

        /**
         * Set the day of the month of holiday to build.
         *
         * @param month month
         * @param dayOfMonth day of month
         * @return this builder
         */
        public HolidayBuilder monthDay(final Month month, final int dayOfMonth) {
            return monthDay(MonthDay.of(month, dayOfMonth));
        }

        /**
         * Set the day of the month of holiday to build.
         *
         * @param monthAndDay month and day expression
         * @return this builder
         */
        public HolidayBuilder monthDay(final String monthAndDay) {
            return monthDay(MonthDay.parse(monthAndDay));
        }

        /**
         * Set the observance of holiday to build.
         *
         * @param observance algorithm to calculate observance
         * @return this builder
         */
        public HolidayBuilder observance(final Observance observance) {
            this.observance = observance;
            return this;
        }

        public HolidayBuilder anniversaryDate(final LocalDate anniversaryDate) {
            this.anniversaryDate = anniversaryDate;
            return this;
        }

        /**
         * Build the {@link Holiday} object.
         *
         * @return constructed holiday
         * @throws IllegalStateException if {@code type} was not set prior to
         *         invocation of this method
         */
        public Holiday build() {
            switch (Optional.ofNullable(type)
                            .orElseThrow(() -> new IllegalStateException("Parameter 'type' cannot be null"))) {
                case FIXED:
                    return new FixedHoliday(name, description, monthDay, rollable);
                case FLOATING:
                    return new FloatingHoliday(name, description, observance, rollable);
                case SPECIAL_ANNIVERSARY:
                    return new SpecialAnniversary(name, description, anniversaryDate, rollable);
                default:
                    throw new IllegalStateException("Parameter 'type' unsupported: " + type);
            }
        }
    }

    /**
     * Get a new builder object for construction of a configured {@code Holiday}
     * instance to be used with one or more holiday calendars.
     *
     * @return new {@code Holiday} builder instance
     */
    public static HolidayBuilder builder() {
        return new HolidayBuilder();
    }

    private final String name;
    private final String description;
    private final boolean rollable;

    /**
     * Parent constructor inherited by concrete subclasses.
     *
     * @param name name of this holiday
     * @param description brief description of this holiday
     * @param rollable flag indicating whether this holiday may be rolled for
     *                 observance
     * @throws NullPointerException if {@code name} is null
     */
    public Holiday(String name, String description, boolean rollable) {
        this.name = requireNonNull(name, "Argument 'name' cannot be null");
        this.description = Optional.ofNullable(description).orElse("");
        this.rollable = rollable;
    }

    /**
     * Get the name of this holiday.
     *
     * @return holiday name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the description (if any) of this holiday.
     *
     * @return holiday description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Determine if this holiday supports date rolling for actual observance on
     * a holiday calendar.
     *
     * @return {@code true} if the date of occurrence for this holiday may be
     *         rolled for observance, {@code false} otherwise
     */
    public boolean isRollable() {
        return rollable;
    }

    /**
     * Calculate the date of this holiday for the specified year. The date
     * returned by this method may be adjusted to a different date of
     * observance by the associated {@link HolidayCalendar holiday calendar}.
     *
     * @param year full calendar year (e.g. 1977, 2021)
     * @return date of this holiday, or {@link Optional#empty() empty} if not
     *         observed on the specified year
     */
    public abstract Optional<LocalDate> dateForYear(int year);

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

    /**
     * {@inheritDoc}
     * <p>Subclasses must implement this method.</p>
     *
     * @return text representation of this object
     */
    @Override
    public abstract String toString();

}
