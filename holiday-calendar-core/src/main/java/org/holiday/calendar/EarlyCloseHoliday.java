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
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * A holiday which represents a partial trading day — a day on which a market or
 * exchange closes early rather than closing for the entire day. Examples are the
 * holiday eves observed by the Tel Aviv Stock Exchange (e.g. Erev Rosh Hashanah,
 * Erev Yom Kippur), on which trading halts around midday.
 *
 * <p>Like a {@link FloatingHoliday}, the calendar date of an early close is
 * computed per year via an {@link Observance}. In addition, an early close
 * carries the {@link #getCloseTime() local time} at which trading ceases and the
 * {@link #getZoneId() time zone} in which that time is expressed.</p>
 *
 * <p>An early close is inherently {@link #isRollable() non-rollable}: it is tied
 * to the specific calendar day preceding the associated full holiday and is never
 * adjusted for weekend observance. Consequently, early closes are excluded from
 * {@link HolidayCalendar#calculate(int)} and are reported separately by
 * {@link HolidayCalendar#calculateEarlyCloses(int)}.</p>
 *
 * @see Observance
 * @see HolidayCalendar#calculateEarlyCloses(int)
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class EarlyCloseHoliday implements Holiday {

    private final String name;
    private final String description;
    private final Observance observance;
    private final LocalTime closeTime;
    private final ZoneId zoneId;

    /**
     * Canonical constructor.
     */
    public EarlyCloseHoliday(String name, String description, Observance observance, LocalTime closeTime, ZoneId zoneId) {
        this.name = requireNonNull(name, "Argument 'name' cannot be null");
        this.description = Optional.ofNullable(description).orElse("");
        this.observance = requireNonNull(observance, "Argument 'observance' cannot be null");
        this.closeTime = requireNonNull(closeTime, "Argument 'closeTime' cannot be null");
        this.zoneId = requireNonNull(zoneId, "Argument 'zoneId' cannot be null");
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    /**
     * {@inheritDoc}
     *
     * <p>An early close is always non-rollable; this method always returns
     * {@code false}.</p>
     */
    @Override
    public boolean isRollable() { return false; }

    public Observance getObservance() { return observance; }

    /**
     * Get the local time at which trading ceases on this early-close day.
     *
     * @return early close time, expressed in the {@link #getZoneId() zone} of
     *         this holiday
     */
    public LocalTime getCloseTime() { return closeTime; }

    /**
     * Get the time zone in which this holiday's {@link #getCloseTime() close time}
     * is expressed.
     *
     * @return early close time zone
     */
    public ZoneId getZoneId() { return zoneId; }

    @Override
    public Optional<LocalDate> dateForYear(int year) {
        return Optional.ofNullable(observance.apply(year));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EarlyCloseHoliday that)) return false;
        return name.equals(that.name)
            && description.equals(that.description)
            && observance.equals(that.observance)
            && closeTime.equals(that.closeTime)
            && zoneId.equals(that.zoneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, observance, closeTime, zoneId);
    }

    @Override
    public String toString() {
        return "Holiday[name='" + name + "', description='" + description
            + "', observance=" + observance + ", closeTime=" + closeTime
            + ", zoneId=" + zoneId + "]";
    }

}
