/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2022 David Joyce
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
        return getService(code).getHolidayCalendar();
    }

    /**
     * Get the {@link HolidayCalendarService} object that provides the
     * {@link HolidayCalendar} object identified by the specified code.
     *
     * @param code short code identifier of desired holiday calendar
     * @return holiday calendar service
     * @throws NoSuchElementException if code does not match any available
     *         holiday calendar service
     */
    public HolidayCalendarService getService(String code) {
        final Spliterator<HolidayCalendarService> spliterator = serviceLoader.spliterator();
        return StreamSupport.stream(spliterator, false)
                .filter(service -> service.isProvided(code))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No HolidayCalendarService support: " + code));
    }

}
