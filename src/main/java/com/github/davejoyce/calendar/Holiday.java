package com.github.davejoyce.calendar;

import com.github.davejoyce.calendar.function.Observance;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Objects;
import java.util.Optional;

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

    /**
     * Enumerated type of {@link Holiday}.
     */
    public enum Type {
        FIXED, FLOATING
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

        /**
         * Build the {@link Holiday} object.
         *
         * @return constructed holiday
         */
        public Holiday build() {
            switch (Optional.ofNullable(type)
                            .orElseThrow(() -> new IllegalArgumentException("Parameter 'type' cannot be null"))) {
                case FIXED:
                    return new FixedHoliday(name, description, monthDay, rollable);
                case FLOATING:
                    return new FloatingHoliday(name, description, observance, rollable);
                default:
                    throw new IllegalArgumentException("Parameter 'type' cannot be null");
            }
        }
    }

    public static HolidayBuilder builder() {
        return new HolidayBuilder();
    }

    private final String name;
    private final String description;
    private final boolean rollable;

    public Holiday(String name, String description, boolean rollable) {
        this.name = requireNonNull(name, "Argument 'name' cannot be null");
        this.description = Optional.ofNullable(description).orElse("");
        this.rollable =rollable;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return description;
    }

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
