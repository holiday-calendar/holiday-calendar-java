package com.github.davejoyce.calendar;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * A named collection of {@link Holiday holidays} observed within a single
 * calendar year. An instance of this class defines the days on which
 * activities may not occur.
 */
public class HolidayCalendar {

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

    private final Set<DayOfWeek> weekendDays = new HashSet<>();
    private final Set<Holiday> holidays = new HashSet<>();

    @Builder(toBuilder = true)
    public HolidayCalendar(String code,
                           String name,
                           @Singular Set<DayOfWeek> weekendDays,
                           @Singular Set<Holiday> holidays) {
        this.code = requireNonNull(code, "Argument 'code' cannot be null");
        this.name = requireNonNull(name, "Argument 'name' cannot be null");
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

    public HolidayCalendar merge(final HolidayCalendar other) {
        if (null == other || this == other || this.equals(other)) return this;
        final String code = String.format("%1s/%2s", this.code, other.getCode());
        final String name = String.format("%1s + %2s", this.name, other.getName());
        final Set<DayOfWeek> weekendDays = new HashSet<>(this.weekendDays);
        weekendDays.addAll(other.getWeekendDays());
        final Set<Holiday> combined = new HashSet<>(this.holidays);
        combined.addAll(other.getHolidays());
        return new HolidayCalendar(code, name, weekendDays, combined);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HolidayCalendar.class.getSimpleName() + "[", "]")
                .add("code='" + code + "'")
                .add("name='" + name + "'")
                .toString();
    }

}
