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
 * adjusted for weekend observance — e.g. a Christmas Eve early close always occurs
 * on Dec 24, never rolled to Dec 23 or Dec 26. Consequently, early closes are
 * excluded from {@link HolidayCalendar#calculate(int)} and are reported separately by
 * {@link HolidayCalendar#calculateEarlyCloses(int)}.</p>
 *
 * <p>Instances are immutable, thread-safe {@code record}s constructed via
 * {@link Holiday#builder()} with {@link Holiday.Type#EARLY_CLOSE}:</p>
 * <pre>{@code
 * Holiday christmasEveEarlyClose = Holiday.builder()
 *     .name("Christmas Eve")
 *     .description("NYSE 1:00pm ET early close")
 *     .type(Holiday.Type.EARLY_CLOSE)
 *     .rollable(false)
 *     .observance(new ChristmasEveEarlyClose())
 *     .closeTime(LocalTime.of(13, 0))
 *     .zoneId(ZoneId.of("America/New_York"))
 *     .build();
 * }</pre>
 *
 * @see Observance
 * @see HolidayCalendar#calculateEarlyCloses(int)
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public record EarlyCloseHoliday(String name, String description, Observance observance,
                                 LocalTime closeTime, ZoneId zoneId) implements Holiday {

    /**
     * Canonical constructor.
     */
    public EarlyCloseHoliday {
        requireNonNull(name, "Argument 'name' cannot be null");
        description = Optional.ofNullable(description).orElse("");
        requireNonNull(observance, "Argument 'observance' cannot be null");
        requireNonNull(closeTime, "Argument 'closeTime' cannot be null");
        requireNonNull(zoneId, "Argument 'zoneId' cannot be null");
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
    public String toString() {
        return "Holiday[name='" + name + "', description='" + description
            + "', observance=" + observance + ", closeTime=" + closeTime
            + ", zoneId=" + zoneId + "]";
    }

}
