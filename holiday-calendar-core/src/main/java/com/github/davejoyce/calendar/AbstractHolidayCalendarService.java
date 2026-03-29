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

package com.github.davejoyce.calendar;

/**
 * Abstract base class for {@link HolidayCalendarService} implementations,
 * providing the identity behavior common to all concrete service classes.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractHolidayCalendarService implements HolidayCalendarService {

    private final String code;
    private final String name;

    protected AbstractHolidayCalendarService(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public boolean isProvided(String code) {
        return this.code.equalsIgnoreCase(code);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getRegion() {
        return name;
    }

}
