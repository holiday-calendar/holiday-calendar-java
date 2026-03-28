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

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A named collection of {@link Holiday holidays} observed within a single
 * calendar year. An instance of this class defines the days on which
 * activities may not occur.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendar {

    /**
     * Default 'empty' {@link DateRoll date rolling} behavior.
     */
    public static final DateRoll NO_ROLL = dateToRoll -> dateToRoll;

    /**
     * Default {@link DayOfWeek days of week} that constitute the 'standard'
     * weekend worldwide.
     */
    public static final Set<DayOfWeek> STANDARD_WEEKEND =
        Collections.unmodifiableSet(new HashSet<>(Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)));

    private final String code;
    private final String name;
    private final DateRoll dateRoll;
    private final Set<DayOfWeek> weekendDays;
    private final Set<Holiday> holidays;

    /**
     * Construct a new holiday calendar object.
     *
     * @param code short text code symbol by which this calendar may be located
     * @param name name of this holiday calendar object
     * @param dateRoll date rolling behavior to be employed by this holiday calendar
     * @param weekendDays days of the week to be treated as the weekend
     * @param holidays set of {@link Holiday} objects to be observed
     */
    public HolidayCalendar(String code,
                           String name,
                           DateRoll dateRoll,
                           Set<DayOfWeek> weekendDays,
                           Set<Holiday> holidays) {
        this.code = requireNonNull(code, "Argument 'code' cannot be null");
        this.name = requireNonNull(name, "Argument 'name' cannot be null");
        this.dateRoll = dateRoll != null ? dateRoll : NO_ROLL;

        Set<DayOfWeek> wd = new HashSet<>();
        if (weekendDays == null || weekendDays.isEmpty()) {
            wd.addAll(STANDARD_WEEKEND);
        } else {
            weekendDays.stream().filter(Objects::nonNull).forEach(wd::add);
        }
        this.weekendDays = Collections.unmodifiableSet(wd);

        Set<Holiday> h = new HashSet<>();
        if (holidays != null) {
            holidays.stream().filter(Objects::nonNull).forEach(h::add);
        }
        this.holidays = Collections.unmodifiableSet(h);
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    public static HolidayCalendarBuilder builder() {
        return new HolidayCalendarBuilder();
    }

    public static class HolidayCalendarBuilder {
        private String code;
        private String name;
        private DateRoll dateRoll;
        private final Set<DayOfWeek> weekendDays = new HashSet<>();
        private final Set<Holiday> holidays = new HashSet<>();

        HolidayCalendarBuilder() {}

        public HolidayCalendarBuilder code(String code) {
            this.code = code;
            return this;
        }

        public HolidayCalendarBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HolidayCalendarBuilder dateRoll(DateRoll dateRoll) {
            this.dateRoll = dateRoll;
            return this;
        }

        public HolidayCalendarBuilder weekendDay(DayOfWeek day) {
            if (day != null) this.weekendDays.add(day);
            return this;
        }

        public HolidayCalendarBuilder weekendDays(Collection<DayOfWeek> days) {
            if (days != null) days.stream().filter(Objects::nonNull).forEach(this.weekendDays::add);
            return this;
        }

        public HolidayCalendarBuilder clearWeekendDays() {
            this.weekendDays.clear();
            return this;
        }

        public HolidayCalendarBuilder holiday(Holiday holiday) {
            if (holiday != null) this.holidays.add(holiday);
            return this;
        }

        public HolidayCalendarBuilder holidays(Collection<Holiday> holidays) {
            if (holidays != null) holidays.stream().filter(Objects::nonNull).forEach(this.holidays::add);
            return this;
        }

        public HolidayCalendarBuilder clearHolidays() {
            this.holidays.clear();
            return this;
        }

        public HolidayCalendar build() {
            return new HolidayCalendar(code, name, dateRoll, weekendDays, holidays);
        }
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public String getCode() { return code; }

    public String getName() { return name; }

    public DateRoll getDateRoll() { return dateRoll; }

    /**
     * Get holidays observed on this calendar.
     *
     * @return unmodifiable set of {@link Holiday} objects
     */
    public Set<Holiday> getHolidays() { return holidays; }

    /**
     * Get weekend days recognized by this calendar.
     *
     * @return unmodifiable set of weekend {@link DayOfWeek days}
     */
    public Set<DayOfWeek> getWeekendDays() { return weekendDays; }

    // -------------------------------------------------------------------------
    // Weekend detection
    // -------------------------------------------------------------------------

    /**
     * Determine if the given instant, in the specified time zone, falls on
     * the weekend as defined by this holiday calendar.
     */
    public boolean isWeekend(final Instant instant, final ZoneId zoneId) {
        final DayOfWeek weekDay = requireNonNull(instant, "Argument 'instant' cannot be null")
                                  .atZone(requireNonNull(zoneId, "Argument 'zoneId' cannot be null"))
                                  .getDayOfWeek();
        return weekendDays.contains(weekDay);
    }

    /**
     * Determine if the given instant, in UTC standard time, falls on
     * the weekend as defined by this holiday calendar.
     */
    public boolean isWeekendUTC(final Instant instant) {
        return isWeekend(instant, ZoneOffset.UTC.normalized());
    }

    /**
     * Determine if the given date, in the specified time zone, falls on the
     * weekend as defined by this holiday calendar.
     */
    public boolean isWeekend(final Date date, final TimeZone timeZone) {
        return isWeekend(requireNonNull(date, "Argument 'date' cannot be null").toInstant(),
                         requireNonNull(timeZone, "Argument 'timeZone' cannot be null").toZoneId());
    }

    /**
     * Determine if the given date, in UTC standard time, falls on the weekend
     * as defined by this holiday calendar.
     */
    public boolean isWeekendUTC(final Date date) {
        return isWeekend(date, TimeZone.getTimeZone(ZoneOffset.UTC.normalized()));
    }

    // -------------------------------------------------------------------------
    // Holiday calculation
    // -------------------------------------------------------------------------

    /**
     * Calculate the dates of the holidays on this calendar for the specified
     * year. The {@link HolidayDate dates} returned by this method are
     * adjusted according to the date rolling behavior of this calendar.
     *
     * @param year Common Era (CE) year for which to obtain holiday dates
     * @return chronologically-sorted list of observed holiday dates
     */
    public List<HolidayDate> calculate(int year) {
        return holidays.stream()
            .<HolidayDate>mapMulti((holiday, sink) ->
                holiday.dateForYear(year).ifPresent(date -> {
                    LocalDate observed = weekendDays.contains(date.getDayOfWeek())
                        ? dateRoll.rollToObservedDate(date)
                        : date;
                    sink.accept(new HolidayDate(holiday, observed));
                })
            )
            .sorted(Comparator.comparing(HolidayDate::getDate))
            .toList();
    }

    // -------------------------------------------------------------------------
    // Merge
    // -------------------------------------------------------------------------

    /**
     * Merge the given {@code HolidayCalendar} object with this one.
     *
     * @param other holiday calendar to be merged
     * @return union of this holiday calendar and the specified calendar
     */
    public HolidayCalendar merge(final HolidayCalendar other) {
        if (null == other || this == other || this.equals(other)) return this;
        final String mergedCode = this.code + "/" + other.getCode();
        final String mergedName = this.name + " + " + other.getName();
        final DateRoll combinedDateRoll = d -> this.dateRoll.rollToObservedDate(other.getDateRoll().rollToObservedDate(d));
        final Set<DayOfWeek> mergedWeekendDays = new HashSet<>(this.weekendDays);
        mergedWeekendDays.addAll(other.getWeekendDays());
        final Set<Holiday> mergedHolidays = new HashSet<>(this.holidays);
        mergedHolidays.addAll(other.getHolidays());
        return new HolidayCalendar(mergedCode, mergedName, combinedDateRoll, mergedWeekendDays, mergedHolidays);
    }

    @Override
    public String toString() {
        return "HolidayCalendar[code='" + code + "', name='" + name + "']";
    }

}
