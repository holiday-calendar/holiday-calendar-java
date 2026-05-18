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

package org.holiday.calendar.observance.hebrew;

import net.time4j.PlainDate;
import net.time4j.calendar.HebrewCalendar;
import net.time4j.calendar.HebrewMonth;
import org.holiday.calendar.observance.AbstractObservance;

import java.time.LocalDate;

/**
 * Observance of Yom Ha'atzmaut — Israeli Independence Day (5 Iyar).
 *
 * <p>The natural date is 5 Iyar, but Israeli law (amended 2004) requires the
 * observed date to be shifted when Yom Hazikaron (Memorial Day, 4 Iyar) would
 * fall on a problematic day relative to Shabbat (Friday–Saturday) or its
 * immediate aftermath (Sunday). The complete postponement rule based on the
 * day of week of natural 5 Iyar:
 *
 * <table>
 *   <tr><th>Natural 5 Iyar</th><th>Observed date</th></tr>
 *   <tr><td>Sunday</td><td>Monday (+1) — Iyar 4 would be Shabbat</td></tr>
 *   <tr><td>Monday</td><td>Tuesday (+1) — Iyar 4 would be Sunday (post-Shabbat)</td></tr>
 *   <tr><td>Tuesday</td><td>Tuesday (no shift)</td></tr>
 *   <tr><td>Wednesday</td><td>Wednesday (no shift)</td></tr>
 *   <tr><td>Thursday</td><td>Thursday (no shift)</td></tr>
 *   <tr><td>Friday</td><td>Thursday (−1) — Iyar 4 would be Erev Shabbat</td></tr>
 *   <tr><td>Saturday</td><td>Thursday (−2) — 5 Iyar itself is Shabbat</td></tr>
 * </table>
 *
 * <p>This observance applies the shift internally and must be declared
 * {@code rollable(false)} — the calendar's {@code DateRoll} must not be
 * applied on top of this already-adjusted date.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class IndependenceDay extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        LocalDate natural = HebrewCalendar.of(year + 3760, HebrewMonth.IYAR, 5)
                .transform(PlainDate.axis())
                .toTemporalAccessor();

        return switch (natural.getDayOfWeek()) {
            case SUNDAY   -> natural.plusDays(1);   // Iyar 4 = Shabbat → observed Monday
            case MONDAY   -> natural.plusDays(1);   // Iyar 4 = Sunday → observed Tuesday
            case FRIDAY   -> natural.minusDays(1);  // Erev Shabbat → observed Thursday
            case SATURDAY -> natural.minusDays(2);  // Shabbat itself → observed Thursday
            default       -> natural;
        };
    }

}
