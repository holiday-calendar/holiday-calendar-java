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
     * Only Sunday rolls forward to the following Monday; Saturday is returned
     * unchanged (no substitute observance).
     *
     * <p>Implements the Japanese substitute-holiday rule (振替休日): Article 3,
     * paragraph 2 of the Act on National Holidays (国民の祝日に関する法律, 1948,
     * amended 2007) creates a make-up Monday only when a national holiday falls
     * on Sunday. A Saturday holiday has no substitute — it falls on an already-
     * closed day and is returned at its natural date.
     *
     * @see #followingMonday() for a roll that advances both Saturday and Sunday
     */
    public static DateRoll sundayToMonday() {
        return date -> date.getDayOfWeek() == DayOfWeek.SUNDAY ? date.plusDays(1) : date;
    }

    /**
     * Compose two rolls: apply {@code first}, then apply {@code second}.
     */
    public static DateRoll compose(DateRoll first, DateRoll second) {
        return date -> second.rollToObservedDate(first.rollToObservedDate(date));
    }

}
