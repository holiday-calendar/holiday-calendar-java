package com.github.davejoyce.calendar;


import java.util.*;
import java.util.stream.StreamSupport;

/**
 * Factory for creation of {@link HolidayCalendar} objects.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarFactory {

    private final ServiceLoader<HolidayCalendarService> serviceLoader = ServiceLoader.load(HolidayCalendarService.class, Thread.currentThread().getContextClassLoader());

    /**
     * Create the {@link HolidayCalendar} object identified by the specified code.
     *
     * @param code short code identifier of desired holiday calendar
     * @return holiday calendar
     * @throws NoSuchElementException if code does not match any available
     *         holiday calendar
     */
    public HolidayCalendar create(String code) {
        final Spliterator<HolidayCalendarService> spliterator = serviceLoader.spliterator();
        final Optional<HolidayCalendarService> match = StreamSupport.stream(spliterator, false)
                                                                    .filter(service -> service.isProvided(code))
                                                                    .findFirst();
        return match.orElseThrow(() -> new NoSuchElementException("No HolidayCalendarService support: " + code)).getHolidayCalendar();
    }

}
