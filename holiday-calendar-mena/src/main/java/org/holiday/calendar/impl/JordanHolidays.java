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
import org.holiday.calendar.observance.islamic.mena.ArafatDay;
import org.holiday.calendar.observance.islamic.mena.EidAlAdha;
import org.holiday.calendar.observance.islamic.mena.EidAlAdhaDay2;
import org.holiday.calendar.observance.islamic.mena.EidAlAdhaDay3;
import org.holiday.calendar.observance.islamic.mena.EidAlAdhaDay4;
import org.holiday.calendar.observance.islamic.mena.EidAlFitr;
import org.holiday.calendar.observance.islamic.mena.EidAlFitrDay2;
import org.holiday.calendar.observance.islamic.mena.EidAlFitrDay3;
import org.holiday.calendar.observance.islamic.mena.EidAlFitrDay4;
import org.holiday.calendar.observance.islamic.mena.IslamicNewYear;
import org.holiday.calendar.observance.islamic.mena.ProphetsBirthday;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

/**
 * Package-private factory building the Jordan public holiday lists for the
 * national ({@code JO}) and settlement ({@code JOD}) calendars.
 *
 * <p>The national calendar ({@code JO}) observes 15 public holidays: New Year's
 * Day, Labour Day, Independence Day, Christmas Day (fixed Gregorian), four days of
 * Eid al-Fitr, Arafat Day, four days of Eid al-Adha, Islamic New Year, and
 * Prophet's Birthday (floating Islamic).
 *
 * <p>The settlement calendar ({@code JOD}) uses the identical holiday set with
 * {@code rollable(false)} for all holidays — the Amman Stock Exchange (ASE) and
 * Central Bank of Jordan (CBJ) apply no date adjustment per the no-roll convention.
 *
 * <p>Jordan observes four days of Eid al-Fitr (1–4 Shawwal) and four days of
 * Eid al-Adha (10–13 Dhu al-Hijjah), with Arafat Day (9 Dhu al-Hijjah) as a
 * separate public holiday. These durations are confirmed by official ASE and CBJ
 * holiday announcements for 2024–2026.
 *
 * <p>Christmas Day (December 25) is gazetted as a national public holiday in Jordan
 * and is an ASE closure day, as confirmed by official ASE holiday schedules for
 * 2025 and 2026.
 *
 * <p>Islamic holiday dates are populated through {@value DATA_VALID_THROUGH} via
 * country-specific CSV lookup tables (country code {@code jo}). Dates for 2024–2026
 * are sourced from official CBJ and ASE holiday announcements; dates for 2027–2055
 * are projected from the Umm al-Qura tabular Islamic calendar. Jordan determines
 * Islamic holiday dates by moon sighting (Ministry of Awqaf / Higher Judiciary
 * Council) and may differ from Saudi Arabia by ±1 day; verify against official
 * CBJ / ASE announcements as each year is published.
 *
 * <p>Note: the 2033 Gregorian year contains two Eid al-Fitr occurrences; only the
 * January occurrence is recorded in the CSV. See {@link EidAlFitr} for details.
 */
class JordanHolidays {

    static final int DATA_VALID_THROUGH = 2055;

    // Jordan market weekend: Friday + Saturday; Sunday is the first business day.
    static final List<DayOfWeek> JORDAN_WEEKEND = List.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);

    private JordanHolidays() {}

    /**
     * Returns the 15 Jordan public holidays.
     *
     * @param rollableFixed whether fixed-date holidays (New Year's Day, Labour Day,
     *                      Independence Day, Christmas Day) are rollable; all Islamic
     *                      holidays are always {@code rollable(false)}
     */
    static List<Holiday> baseHolidays(boolean rollableFixed) {
        return List.of(
            Holiday.builder()
                    .name("New Year's Day")
                    .description("First day of new year in the Common Era (CE)")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.JANUARY, 1)
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr")
                    .description("First day of Eid al-Fitr, marking the end of Ramadan (1 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitr("jo"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr (2nd Day)")
                    .description("Second day of Eid al-Fitr (2 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitrDay2("jo"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr (3rd Day)")
                    .description("Third day of Eid al-Fitr (3 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitrDay3("jo"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr (4th Day)")
                    .description("Fourth day of Eid al-Fitr (4 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitrDay4("jo"))
                    .build(),
            Holiday.builder()
                    .name("Labour Day")
                    .description("International Workers' Day")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.MAY, 1)
                    .build(),
            Holiday.builder()
                    .name("Independence Day")
                    .description("Jordanian Independence Day, marking independence from the British Mandate on 25 May 1946")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.MAY, 25)
                    .build(),
            Holiday.builder()
                    .name("Arafat Day")
                    .description("Day of standing at Mount Arafat, the day before Eid al-Adha (9 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new ArafatDay("jo"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha")
                    .description("First day of Eid al-Adha, the Feast of Sacrifice (10 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdha("jo"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha (2nd Day)")
                    .description("Second day of Eid al-Adha (11 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdhaDay2("jo"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha (3rd Day)")
                    .description("Third day of Eid al-Adha (12 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdhaDay3("jo"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha (4th Day)")
                    .description("Fourth day of Eid al-Adha (13 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdhaDay4("jo"))
                    .build(),
            Holiday.builder()
                    .name("Islamic New Year")
                    .description("First day of the Islamic lunar year (1 Muharram AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new IslamicNewYear("jo"))
                    .build(),
            Holiday.builder()
                    .name("Prophet's Birthday")
                    .description("Birthday of the Prophet Muhammad (12 Rabi' al-Awwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new ProphetsBirthday("jo"))
                    .build(),
            Holiday.builder()
                    .name("Christmas Day")
                    .description("Christmas Day; gazetted national public holiday in Jordan and ASE closure day")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.DECEMBER, 25)
                    .build()
        );
    }

    /**
     * Returns the 15 JOD holidays: the base holiday set with all holidays
     * {@code rollable(false)}, for use with {@link org.holiday.calendar.function.DateRolls#noRoll()}.
     */
    static List<Holiday> jodHolidays() {
        return baseHolidays(false);
    }

}
