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

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * A named collection of {@link Holiday holidays} observed within a single
 * calendar year. An instance of this class defines the days on which
 * activities may not occur.
 */
public class HolidayCalendar {

    /**
     * Default 'empty' {@link DateRoll date rolling} behavior.
     */
    public static final DateRoll NO_ROLL = (dateToRoll) -> dateToRoll;

    /**
     * Default {@link DayOfWeek days of week} that constitute the 'standard'
     * weekend worldwide.
     */
    public static final HashSet<DayOfWeek> STANDARD_WEEKEND = new HashSet<>(Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));

    @Getter
    @NonNull
    private final String code;

    @Getter
    @NonNull
    private final String name;

    @Getter
    @NonNull
    private final DateRoll dateRoll;

    private final Set<DayOfWeek> weekendDays = new HashSet<>();
    private final Set<Holiday> holidays = new HashSet<>();

    @Builder(toBuilder = true)
    public HolidayCalendar(String code,
                           String name,
                           DateRoll dateRoll,
                           @Singular Set<DayOfWeek> weekendDays,
                           @Singular Set<Holiday> holidays) {
        this.code = requireNonNull(code, "Argument 'code' cannot be null");
        this.name = requireNonNull(name, "Argument 'name' cannot be null");
        this.dateRoll = Optional.ofNullable(dateRoll).orElse(NO_ROLL);
        Optional.ofNullable(weekendDays)
                .ifPresent(wd -> {
                    if (wd.isEmpty()) {
                        this.weekendDays.addAll(STANDARD_WEEKEND);
                    } else {
                        wd.stream()
                          .filter(dayOfWeek -> !isNull(dayOfWeek))
                          .forEach(this.weekendDays::add);
                    }
                });
        Optional.ofNullable(holidays)
                .ifPresent(h -> h.stream()
                                 .filter(holiday -> !isNull(holiday))
                                 .forEach(this.holidays::add)
                );
    }

    public Set<Holiday> getHolidays() {
        return Collections.unmodifiableSet(this.holidays);
    }

    public Set<DayOfWeek> getWeekendDays() {
        return Collections.unmodifiableSet(this.weekendDays);
    }

    public boolean isWeekend(final Instant instant,
                             final ZoneId zoneId) {
        final DayOfWeek weekDay = requireNonNull(instant, "Argument 'instant' cannot be null")
                                  .atZone(requireNonNull(zoneId, "Argument 'zoneId' cannot be null"))
                                  .getDayOfWeek();
        return weekendDays.contains(weekDay);
    }

    public boolean isWeekendUTC(final Instant instant) {
        return isWeekend(instant, ZoneOffset.UTC.normalized());
    }

    public boolean isWeekend(final Date date, final TimeZone timeZone) {
        return isWeekend(requireNonNull(date, "Argument 'date' cannot be null").toInstant(),
                         requireNonNull(timeZone, "Argument 'timeZone' cannot be null").toZoneId());
    }

    public boolean isWeekendUTC(final Date date) {
        return isWeekend(date, TimeZone.getTimeZone(ZoneOffset.UTC.normalized()));
    }

    /**
     * Calculate the dates of the holidays on this calendar for the specified
     * year. The {@link HolidayDate dates} returned by this method are
     * adjusted according to the date rolling behavior of this calendar, for the
     * holidays which apply for the given year.
     *
     * @param year Common Era (CE) year for which to obtain holiday dates
     * @return list of observed holiday dates valid for the year
     */
    public List<HolidayDate> calculate(int year) {
        return holidays.stream()
                       .map(holiday -> {
                           Optional<LocalDate> date = holiday.dateForYear(year);
                           return date.map(localDate -> new HolidayDate(holiday, localDate));
                       })
                       .map(calculated -> calculated.map(hd -> (weekendDays.contains(hd.getDate().getDayOfWeek()))
                                                                ? new HolidayDate(hd.getHoliday(), dateRoll.rollToObservedDate(hd.getDate()))
                                                                : hd))
                       .map(adjusted -> adjusted.orElse(null))
                       .filter(Objects::nonNull)
                       .sorted(Comparator.comparing(HolidayDate::getDate))
                       .collect(Collectors.toList());
    }

    /**
     * Merge the given {@code HolidayCalendar} object with this one.
     *
     * @param other holiday calendar to be merged
     * @return union of this holiday calendar and the specified calendar
     */
    public HolidayCalendar merge(final HolidayCalendar other) {
        if (null == other || this == other || this.equals(other)) return this;
        final String code = String.format("%1s/%2s", this.code, other.getCode());
        final String name = String.format("%1s + %2s", this.name, other.getName());
        final DateRoll combinedDateRoll = (dateToRoll -> this.dateRoll.rollToObservedDate(other.dateRoll.rollToObservedDate(dateToRoll)));
        final Set<DayOfWeek> weekendDays = new HashSet<>(this.weekendDays);
        weekendDays.addAll(other.getWeekendDays());
        final Set<Holiday> combined = new HashSet<>(this.holidays);
        combined.addAll(other.getHolidays());
        return new HolidayCalendar(code, name, combinedDateRoll, weekendDays, combined);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HolidayCalendar.class.getSimpleName() + "[", "]")
                .add("code='" + code + "'")
                .add("name='" + name + "'")
                .toString();
    }

}
