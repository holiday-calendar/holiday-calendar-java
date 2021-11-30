package com.github.davejoyce.calendar;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A named collection of {@link Holiday holidays} observed within a single
 * calendar year. An instance of this class defines the days on which
 * activities may not occur.
 */
public class HolidayCalendar {

    public static final HashSet<DayOfWeek> STANDARD_WEEKEND = new HashSet<>(Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
    private static final Holiday[] EMPTY = {};

    private final String code;
    private final String name;
    private final Set<DayOfWeek> weekendDays;
    private final ConcurrentMap<String, Holiday> holidays;


    public HolidayCalendar(String code, String name, Set<DayOfWeek> weekendDays, Holiday... holidays) {
        this.code = requireNonNull(code, "Argument 'code' cannot be null");
        this.name = requireNonNull(name, "Argument 'name' cannot be null");
        this.weekendDays = Collections.unmodifiableSet(requireNonNull(weekendDays, "Argument 'weekendDays' cannot be null"));
        this.holidays = Arrays.stream(requireNonNull(holidays, "Argument 'holidays' cannot be null"))
                              .collect(Collectors.toMap(Holiday::getName,
                                                        h -> h,
                                                        (original, replacement) -> original,
                                                        ConcurrentHashMap::new));
    }

    public HolidayCalendar(String code, String name, Set<DayOfWeek> weekendDays) {
        this(code, name, weekendDays, EMPTY);
    }

    public HolidayCalendar(String code, String name) {
        this(code, name, STANDARD_WEEKEND);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Set<DayOfWeek> getWeekendDays() {
        return weekendDays;
    }

    public Map<String, Holiday> getHolidays() {
        return Collections.unmodifiableMap(this.holidays);
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

    public HolidayCalendar merge(final HolidayCalendar other) {
        if (null == other || this == other || this.equals(other)) return this;
        final String code = String.format("%1s/%2s", this.code, other.getCode());
        final String name = String.format("%1s + %2s", this.name, other.getName());
        final Set<DayOfWeek> weekendDays = new HashSet<>(this.weekendDays);
        weekendDays.addAll(other.getWeekendDays());
        final ConcurrentMap<String, Holiday> combined = new ConcurrentHashMap<>(this.holidays);
        other.getHolidays().forEach((holidayName, holiday) -> combined.merge(holidayName, holiday, (v1, v2) -> v1));
        final Holiday[] holidays = combined.values().toArray(EMPTY);
        return new HolidayCalendar(code, name, weekendDays, holidays);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HolidayCalendar.class.getSimpleName() + "[", "]")
                .add("code='" + code + "'")
                .add("name='" + name + "'")
                .toString();
    }

}
