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
import org.holiday.calendar.observance.hebrew.ErevPassover;
import org.holiday.calendar.observance.hebrew.ErevRoshHashanah;
import org.holiday.calendar.observance.hebrew.ErevShavuot;
import org.holiday.calendar.observance.hebrew.ErevSukkot;
import org.holiday.calendar.observance.hebrew.ErevYomKippur;
import org.holiday.calendar.observance.hebrew.HoshanaRaba;
import org.holiday.calendar.observance.hebrew.IndependenceDay;
import org.holiday.calendar.observance.hebrew.Passover;
import org.holiday.calendar.observance.hebrew.YomHazikaron;
import org.holiday.calendar.observance.hebrew.PassoverEnd;
import org.holiday.calendar.observance.hebrew.RoshHashanah;
import org.holiday.calendar.observance.hebrew.RoshHashanahDay2;
import org.holiday.calendar.observance.hebrew.Shavuot;
import org.holiday.calendar.observance.hebrew.SheminiAtzeret;
import org.holiday.calendar.observance.hebrew.Sukkot;
import org.holiday.calendar.observance.hebrew.YomKippur;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Package-private factory building the Israel public holiday lists for the
 * national ({@code IL}) and settlement ({@code ILS}) calendars.
 *
 * <p>{@link #baseHolidays()} returns the nine holidays shared by both calendars.
 * {@link #ilHolidays()} returns ten holidays for the {@code IL} national calendar,
 * adding Yom Hazikaron (Israeli Memorial Day, 4 Iyar).
 *
 * <p>All holidays are computed algorithmically via
 * {@code net.time4j.calendar.HebrewCalendar}; no CSV lookup tables are used.
 * All holidays are declared {@code rollable(false)} because Hebrew calendar
 * dates are observed on specific calendar days regardless of the day of week.
 * Independence Day is additionally self-adjusting via the statutory
 * postponement rule embedded in {@link IndependenceDay#computeDate}.
 *
 * <p>{@link #earlyCloseHolidays()} returns six {@code EARLY_CLOSE} holidays: five
 * TASE half-day closures on holiday eves (Erev Rosh Hashanah, Erev Yom Kippur, Erev
 * Passover, Erev Shavuot, Erev Sukkot), each closing at 13:00 Asia/Jerusalem time,
 * plus Hoshana Raba (21 Tishri), on which the Bank of Israel operates with reduced
 * hours until approximately 13:15 Asia/Jerusalem time.
 *
 * <p>Omitted holidays (conscious decisions):
 * <ul>
 *   <li>Yom HaShoah (27 Nisan) — Holocaust Remembrance Day is a national
 *       commemoration day, but it is not a statutory public rest holiday under
 *       the Work and Rest Hours Law. TASE also remains open.</li>
 *   <li>Sigd (29 Heshvan) — low market relevance; not a TASE closure day.</li>
 * </ul>
 */
class IsraelHolidays {

    // Israeli weekend: Friday + Saturday; Sunday is the first business day.
    static final List<DayOfWeek> ISRAEL_WEEKEND = List.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);

    private static final ZoneId ISRAEL_ZONE = ZoneId.of("Asia/Jerusalem");

    private IsraelHolidays() {}

    /**
     * Returns the ten holidays for the {@code IL} national calendar.
     * Includes all nine holidays from {@link #baseHolidays()} plus Yom Hazikaron
     * (Israeli Memorial Day, 4 Iyar), which is a statutory national day where
     * government offices and schools close. TASE remains open on Yom Hazikaron,
     * so it is not included in the {@code ILS} settlement calendar.
     */
    static List<Holiday> ilHolidays() {
        List<Holiday> holidays = new ArrayList<>(baseHolidays());
        holidays.add(7,
            Holiday.builder()
                    .name("Yom Hazikaron")
                    .description("Israeli Memorial Day (4 Iyar); observed date is always the " +
                                 "day before Yom Ha'atzmaut per the 1963 statutory shift rule")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new YomHazikaron())
                    .build()
        );
        return List.copyOf(holidays);
    }

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

    /**
     * Returns six {@code EARLY_CLOSE} holidays: five representing TASE's half-day
     * closures on the eves of major Jewish holidays (closing at 13:00 Asia/Jerusalem
     * time), plus Hoshana Raba (21 Tishri), on which the Bank of Israel operates
     * with reduced hours until approximately 13:15 Asia/Jerusalem time per official
     * Bank of Israel Markets Department schedules. None are subject to date rolling.
     */
    static List<Holiday> earlyCloseHolidays() {
        return List.of(
            Holiday.builder()
                    .name("Erev Rosh Hashanah")
                    .description("Eve of the Jewish New Year; TASE half-day closure")
                    .type(Holiday.Type.EARLY_CLOSE)
                    .rollable(false)
                    .observance(new ErevRoshHashanah())
                    .closeTime(LocalTime.of(13, 0))
                    .zoneId(ISRAEL_ZONE)
                    .build(),
            Holiday.builder()
                    .name("Erev Yom Kippur")
                    .description("Eve of the Day of Atonement; TASE half-day closure")
                    .type(Holiday.Type.EARLY_CLOSE)
                    .rollable(false)
                    .observance(new ErevYomKippur())
                    .closeTime(LocalTime.of(13, 0))
                    .zoneId(ISRAEL_ZONE)
                    .build(),
            Holiday.builder()
                    .name("Erev Passover")
                    .description("Eve of Passover; TASE half-day closure")
                    .type(Holiday.Type.EARLY_CLOSE)
                    .rollable(false)
                    .observance(new ErevPassover())
                    .closeTime(LocalTime.of(13, 0))
                    .zoneId(ISRAEL_ZONE)
                    .build(),
            Holiday.builder()
                    .name("Erev Shavuot")
                    .description("Eve of the Feast of Weeks / Pentecost; TASE half-day closure")
                    .type(Holiday.Type.EARLY_CLOSE)
                    .rollable(false)
                    .observance(new ErevShavuot())
                    .closeTime(LocalTime.of(13, 0))
                    .zoneId(ISRAEL_ZONE)
                    .build(),
            Holiday.builder()
                    .name("Erev Sukkot")
                    .description("Eve of the Festival of Tabernacles; TASE half-day closure")
                    .type(Holiday.Type.EARLY_CLOSE)
                    .rollable(false)
                    .observance(new ErevSukkot())
                    .closeTime(LocalTime.of(13, 0))
                    .zoneId(ISRAEL_ZONE)
                    .build(),
            Holiday.builder()
                    .name("Hoshana Raba")
                    .description("Seventh day of Sukkot (21 Tishri); Bank of Israel operates " +
                                 "with reduced hours until approximately 13:15 IST, per official " +
                                 "Bank of Israel Markets Department schedules")
                    .type(Holiday.Type.EARLY_CLOSE)
                    .rollable(false)
                    .observance(new HoshanaRaba())
                    .closeTime(LocalTime.of(13, 15))
                    .zoneId(ISRAEL_ZONE)
                    .build()
        );
    }
}