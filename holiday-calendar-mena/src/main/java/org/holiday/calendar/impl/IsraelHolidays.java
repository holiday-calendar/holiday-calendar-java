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

package org.holiday.calendar.impl;

import org.holiday.calendar.Holiday;
import org.holiday.calendar.observance.hebrew.IndependenceDay;
import org.holiday.calendar.observance.hebrew.Passover;
import org.holiday.calendar.observance.hebrew.PassoverEnd;
import org.holiday.calendar.observance.hebrew.RoshHashanah;
import org.holiday.calendar.observance.hebrew.RoshHashanahDay2;
import org.holiday.calendar.observance.hebrew.Shavuot;
import org.holiday.calendar.observance.hebrew.SheminiAtzeret;
import org.holiday.calendar.observance.hebrew.Sukkot;
import org.holiday.calendar.observance.hebrew.YomKippur;

import java.time.DayOfWeek;
import java.util.List;

/**
 * Package-private factory building the Israel public holiday list shared by
 * the national ({@code IL}) and settlement ({@code ILS}) calendars.
 *
 * <p>All nine holidays are computed algorithmically via
 * {@code net.time4j.calendar.HebrewCalendar}; no CSV lookup tables are used.
 * All holidays are declared {@code rollable(false)} because Hebrew calendar
 * dates are observed on specific calendar days regardless of the day of week.
 * Independence Day is additionally self-adjusting via the statutory
 * postponement rule embedded in {@link IndependenceDay#computeDate}.
 *
 * <p>Scope limitation: TASE closes early on holiday eves (Erev Rosh Hashanah,
 * Erev Yom Kippur, Erev Passover, Erev Shavuot, Erev Sukkot). These half-day
 * closures are not modelled here. See {@link HolidayCalendarServiceILS} for the
 * full limitation notice.
 *
 * <p>Omitted holidays (conscious decisions):
 * <ul>
 *   <li>Yom Hazikaron (4 Iyar) — TASE remains open; not a market closure day.</li>
 *   <li>Sigd (29 Heshvan) — low market relevance; not a TASE closure day.</li>
 * </ul>
 */
class IsraelHolidays {

    // Israeli weekend: Friday + Saturday; Sunday is the first business day.
    static final List<DayOfWeek> ISRAEL_WEEKEND = List.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);

    private IsraelHolidays() {}

    static List<Holiday> baseHolidays() {
        return List.of(
            Holiday.builder()
                    .name("Rosh Hashanah")
                    .description("Jewish New Year, first day (1 Tishri)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new RoshHashanah())
                    .build(),
            Holiday.builder()
                    .name("Rosh Hashanah (2nd Day)")
                    .description("Jewish New Year, second day (2 Tishri)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new RoshHashanahDay2())
                    .build(),
            Holiday.builder()
                    .name("Yom Kippur")
                    .description("Day of Atonement (10 Tishri)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new YomKippur())
                    .build(),
            Holiday.builder()
                    .name("Sukkot")
                    .description("Festival of Tabernacles, first day (15 Tishri)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new Sukkot())
                    .build(),
            Holiday.builder()
                    .name("Shemini Atzeret / Simchat Torah")
                    .description("Eighth Day of Assembly / Rejoicing of the Torah (22 Tishri); " +
                                 "combined on a single day in Israel")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new SheminiAtzeret())
                    .build(),
            Holiday.builder()
                    .name("Passover")
                    .description("First day of Passover (15 Nisan)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new Passover())
                    .build(),
            Holiday.builder()
                    .name("Passover (7th Day)")
                    .description("Seventh and final day of Passover (21 Nisan); " +
                                 "Israel observes a 7-day Passover (Nisan 15–21)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new PassoverEnd())
                    .build(),
            Holiday.builder()
                    .name("Yom Ha'atzmaut")
                    .description("Israeli Independence Day (5 Iyar); shifted per statutory " +
                                 "postponement rule when natural date falls on Sun/Mon/Fri/Sat")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new IndependenceDay())
                    .build(),
            Holiday.builder()
                    .name("Shavuot")
                    .description("Feast of Weeks / Pentecost (6 Sivan)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new Shavuot())
                    .build()
        );
    }
}
