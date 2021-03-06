package com.github.davejoyce.calendar;

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
 * and national independence days.
 */
public class FixedHoliday extends Holiday {

    private final MonthDay monthDay;

    public FixedHoliday(String name,
                        String description,
                        Month month,
                        int dayOfMonth) {
        this(name, description, month, dayOfMonth, true);
    }

    public FixedHoliday(String name,
                        String description,
                        String monthAndDay) {
        this(name, description, monthAndDay, true);
    }

    public FixedHoliday(String name,
                        String description,
                        Month month,
                        int dayOfMonth,
                        boolean rollable) {
        this(name, description, MonthDay.of(month, dayOfMonth), rollable);
    }

    public FixedHoliday(String name,
                        String description,
                        String monthAndDay,
                        boolean rollable) {
        this(name, description, MonthDay.parse(monthAndDay), rollable);
    }

    public FixedHoliday(String name,
                        String description,
                        MonthDay monthDay,
                        boolean rollable) {
        super(name, description, rollable);
        this.monthDay = requireNonNull(monthDay, "Argument 'monthDay' cannot be null");
    }

    public Month getMonth() {
        return monthDay.getMonth();
    }

    public int getDayOfMonth() {
        return monthDay.getDayOfMonth();
    }

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
