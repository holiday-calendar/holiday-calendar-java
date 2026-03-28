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

package org.holiday.calendar.function;

import java.time.DayOfWeek;

/**
 * Factory providing common {@link DateRoll} strategies.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class DateRolls {

    private DateRolls() {}

    /** No adjustment — returns the date unchanged. */
    public static DateRoll noRoll() {
        return date -> date;
    }

    /**
     * Saturday rolls back to Friday; Sunday rolls forward to Monday.
     */
    public static DateRoll previousFridayOrFollowingMonday() {
        return date -> switch (date.getDayOfWeek()) {
            case SATURDAY -> date.minusDays(1);
            case SUNDAY   -> date.plusDays(1);
            default       -> date;
        };
    }

    /**
     * Saturday and Sunday both roll forward to Monday.
     */
    public static DateRoll followingMonday() {
        return date -> {
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY) return date.plusDays(2);
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY)   return date.plusDays(1);
            return date;
        };
    }

    /**
     * Compose two rolls: apply {@code first}, then apply {@code second}.
     */
    public static DateRoll compose(DateRoll first, DateRoll second) {
        return date -> second.rollToObservedDate(first.rollToObservedDate(date));
    }

}
