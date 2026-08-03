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
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.ZoneId;
import java.util.Optional;

/**
 * An official day on which work does not occur. A holiday may be traditional,
 * religious, or governmental in origin. Its date of occurrence may be fixed,
 * <a href="https://en.wikipedia.org/wiki/Moveable_feast">moveable</a>, or
 * otherwise computed.
 * <p>Permitted subtypes determine the method of holiday date calculation:</p>
 * <ul>
 *     <li>{@link FixedHoliday} — same {@link java.time.MonthDay} every year</li>
 *     <li>{@link FloatingHoliday} — date computed per year via an {@link Observance}</li>
 *     <li>{@link SpecialAnniversary} — anniversary of a fixed {@link java.time.LocalDate}</li>
 *     <li>{@link EarlyCloseHoliday} — a non-rollable partial trading day (since 2.0.0);
 *     excluded from {@link HolidayCalendar#calculate(int)} and reported separately by
 *     {@link HolidayCalendar#calculateEarlyCloses(int)}</li>
 * </ul>
 * <p>Code that exhaustively switches over these subtypes (e.g. {@code switch (holiday) {
 * case FixedHoliday f -> ...; case FloatingHoliday f -> ...; case SpecialAnniversary s -> ...;
 * case EarlyCloseHoliday e -> ...; }}) must be updated to add the {@code EarlyCloseHoliday}
 * case when upgrading from a pre-2.0.0 release. If the switch is used as an expression
 * over a nullable {@code Holiday} reference, also add an explicit {@code case null} branch.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public sealed interface Holiday permits FixedHoliday, FloatingHoliday, SpecialAnniversary, EarlyCloseHoliday {

    /**
     * Enumerated type of {@link Holiday}.
     */
    enum Type {
        FIXED, FLOATING, SPECIAL_ANNIVERSARY, EARLY_CLOSE
    }

    /**
     * Builder for configurable construction of {@link Holiday} objects.
     */
    class HolidayBuilder {

        private Type type = Type.FIXED;
        private String name;
        private String description;
        private boolean rollable = true;
        private MonthDay monthDay;
        private Observance observance;
        private LocalDate anniversaryDate;
        private LocalTime closeTime;
        private ZoneId zoneId;

        /**
         * Package private constructor. Not intended to be called directly by
         * client code.
         *
         * @see #builder()
         */
        HolidayBuilder() {}

        public HolidayBuilder type(final Type type) {
            this.type = type;
            return this;
        }

        public HolidayBuilder type(final String type) {
            return type(Type.valueOf(type.trim().toUpperCase()));
        }

        public HolidayBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public HolidayBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public HolidayBuilder rollable(final boolean rollable) {
            this.rollable = rollable;
            return this;
        }

        public HolidayBuilder monthDay(final MonthDay monthDay) {
            this.monthDay = monthDay;
            return this;
        }

        public HolidayBuilder monthDay(final Month month, final int dayOfMonth) {
            return monthDay(MonthDay.of(month, dayOfMonth));
        }

        public HolidayBuilder monthDay(final String monthAndDay) {
            return monthDay(MonthDay.parse(monthAndDay));
        }

        public HolidayBuilder observance(final Observance observance) {
            this.observance = observance;
            return this;
        }

        public HolidayBuilder anniversaryDate(final LocalDate anniversaryDate) {
            this.anniversaryDate = anniversaryDate;
            return this;
        }

        public HolidayBuilder closeTime(final LocalTime closeTime) {
            this.closeTime = closeTime;
            return this;
        }

        public HolidayBuilder zoneId(final ZoneId zoneId) {
            this.zoneId = zoneId;
            return this;
        }

        /**
         * Build the {@link Holiday} object.
         *
         * @return constructed holiday
         * @throws NullPointerException if {@code type} was not set prior to
         *         invocation of this method
         */
        public Holiday build() {
            return switch (java.util.Objects.requireNonNull(type, "Parameter 'type' cannot be null")) {
                case FIXED            -> new FixedHoliday(name, description, monthDay, rollable);
                case FLOATING         -> new FloatingHoliday(name, description, observance, rollable);
                case SPECIAL_ANNIVERSARY -> new SpecialAnniversary(name, description, anniversaryDate, rollable);
                case EARLY_CLOSE      -> new EarlyCloseHoliday(name, description, observance, closeTime, zoneId);
            };
        }
    }

    /**
     * Get a new builder object for construction of a configured {@code Holiday}
     * instance to be used with one or more holiday calendars.
     *
     * @return new {@code Holiday} builder instance
     */
    static HolidayBuilder builder() {
        return new HolidayBuilder();
    }

    /**
     * Get the name of this holiday.
     *
     * @return holiday name
     */
    String getName();

    /**
     * Get the description (if any) of this holiday.
     *
     * @return holiday description
     */
    String getDescription();

    /**
     * Determine if this holiday supports date rolling for actual observance on
     * a holiday calendar.
     *
     * @return {@code true} if the date of occurrence for this holiday may be
     *         rolled for observance, {@code false} otherwise
     */
    boolean isRollable();

    /**
     * Calculate the date of this holiday for the specified year. The date
     * returned by this method may be adjusted to a different date of
     * observance by the associated {@link HolidayCalendar holiday calendar}.
     *
     * @param year full calendar year (e.g. 1977, 2021)
     * @return date of this holiday, or {@link Optional#empty() empty} if not
     *         observed on the specified year
     */
    Optional<LocalDate> dateForYear(int year);

}
