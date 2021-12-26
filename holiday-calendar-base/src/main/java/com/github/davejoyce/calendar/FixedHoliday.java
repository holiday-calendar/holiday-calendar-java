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

import com.github.davejoyce.calendar.function.DateRoll;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import static java.util.Objects.requireNonNull;

/**
 * A holiday which occurs on the same date every year. Examples of a fixed
 * holiday are
 * <a href="https://en.wikipedia.org/wiki/New_Year%27s_Day">New Year's Day</a>
 * and a national independence day.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class FixedHoliday extends Holiday {

    private final MonthDay monthDay;

    /**
     * Construct a new instance of a fixed holiday with the specified name,
     * description, and occurring on the given month and day. The constructed
     * holiday object will be {@link #isRollable() rollable} by default.
     *
     * @param name name of this holiday
     * @param description brief description of this holiday
     * @param month month in which holiday occurs
     * @param dayOfMonth day of month on which holiday occurs
     */
    public FixedHoliday(String name,
                        String description,
                        Month month,
                        int dayOfMonth) {
        this(name, description, month, dayOfMonth, true);
    }

    /**
     * Construct a new instance of a fixed holiday with the specified name,
     * description, and occurring on the given month and day. The constructed
     * holiday object will be {@link #isRollable() rollable} by default.
     *
     * @param name name of this holiday
     * @param description brief description of this holiday
     * @param monthAndDay text notation of month and day on which holiday occurs
     */
    public FixedHoliday(String name,
                        String description,
                        String monthAndDay) {
        this(name, description, monthAndDay, true);
    }

    /**
     * Construct a new instance of a fixed holiday with the specified name,
     * description, and occurring on the given month and day. The constructed
     * holiday object will support or disallow {@link DateRoll date roll}, based
     * upon the specified boolean flag argument.
     *
     * @param name name of this holiday
     * @param description brief description of this holiday
     * @param month month in which holiday occurs
     * @param dayOfMonth day of month on which holiday occurs
     * @param rollable flag indicating whether this holiday may be rolled for
     *                 observance
     */
    public FixedHoliday(String name,
                        String description,
                        Month month,
                        int dayOfMonth,
                        boolean rollable) {
        this(name, description, MonthDay.of(month, dayOfMonth), rollable);
    }

    /**
     * Construct a new instance of a fixed holiday with the specified name,
     * description, and occurring on the given month and day. The constructed
     * holiday object will support or disallow {@link DateRoll date roll}, based
     * upon the specified boolean flag argument.
     *
     * @param name name of this holiday
     * @param description brief description of this holiday
     * @param monthAndDay text notation of month and day on which holiday occurs
     * @param rollable flag indicating whether this holiday may be rolled for
     *                 observance
     */
    public FixedHoliday(String name,
                        String description,
                        String monthAndDay,
                        boolean rollable) {
        this(name, description, MonthDay.parse(monthAndDay), rollable);
    }

    /**
     * Construct a new instance of a fixed holiday with the specified name,
     * description, and occurring on the given month and day. The constructed
     * holiday object will support or disallow {@link DateRoll date roll}, based
     * upon the specified boolean flag argument.
     *
     * @param name name of this holiday
     * @param description brief description of this holiday
     * @param monthDay {@code MonthDay} specification of when holiday occurs
     * @param rollable flag indicating whether this holiday may be rolled for
     *                 observance
     */
    public FixedHoliday(String name,
                        String description,
                        MonthDay monthDay,
                        boolean rollable) {
        super(name, description, rollable);
        this.monthDay = requireNonNull(monthDay, "Argument 'monthDay' cannot be null");
    }

    /**
     * Get month on which this holiday occurs.
     *
     * @return holiday month
     */
    public Month getMonth() {
        return monthDay.getMonth();
    }

    /**
     * Get day of the month on which this holiday occurs.
     *
     * @return holiday day in month
     */
    public int getDayOfMonth() {
        return monthDay.getDayOfMonth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<LocalDate> dateForYear(int year) {
        return Optional.of(LocalDate.of(year, monthDay.getMonth(), monthDay.getDayOfMonth()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FixedHoliday that = (FixedHoliday) o;
        return monthDay.equals(that.monthDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), monthDay);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Holiday.class.getSimpleName() + "[", "]")
                .add("name='" + getName() + "'")
                .add("description='" + getDescription() + "'")
                .add("month=" + getMonth())
                .add("dayOfMonth=" + getDayOfMonth())
                .toString();
    }

}
