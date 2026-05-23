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
import org.holiday.calendar.observance.islamic.mena.EidAlFitr;
import org.holiday.calendar.observance.islamic.mena.EidAlFitrDay2;
import org.holiday.calendar.observance.islamic.mena.EidAlFitrDay3;
import org.holiday.calendar.observance.islamic.mena.IslamicNewYear;
import org.holiday.calendar.observance.islamic.mena.IsraMiraj;
import org.holiday.calendar.observance.islamic.mena.ProphetsBirthday;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

/**
 * Package-private factory building the Kuwait public holiday lists for the
 * national ({@code KW}) and settlement ({@code KWD}) calendars.
 *
 * <p>The national calendar ({@code KW}) observes 13 public holidays: New Year's Day,
 * National Day, Liberation Day, Isra and Mi'raj, Arafat Day, three days of Eid al-Fitr,
 * three days of Eid al-Adha, Islamic New Year, and Prophet's Birthday.
 *
 * <p>The settlement calendar ({@code KWD}) uses the identical holiday set with
 * {@code rollable(false)} for all holidays — the exchange applies no date adjustment
 * per the Boursa Kuwait / CBK no-roll convention.
 *
 * <p>Islamic holiday dates are populated through {@value DATA_VALID_THROUGH} via
 * country-specific CSV lookup tables (country code {@code kw}). Dates for 2024–2026
 * are sourced from official CBK and Boursa Kuwait market-holiday announcements;
 * dates for 2027–2055 are projected from the Umm al-Qura tabular Islamic calendar.
 * Kuwait determines Islamic holiday dates by moon sighting and may differ from
 * Saudi Arabia by ±1 day; verify against official CBK / Boursa Kuwait announcements
 * as each year is published.
 *
 * <p>Note: the 2033 Gregorian year contains two Eid al-Fitr occurrences; only the
 * January occurrence is recorded in the CSV. See {@link EidAlFitr} for details.
 */
class KuwaitHolidays {

    static final int DATA_VALID_THROUGH = 2055;

    // GCC market weekend: Friday + Saturday; Sunday is the first business day.
    static final List<DayOfWeek> KUWAIT_WEEKEND = List.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);

    private KuwaitHolidays() {}

    /**
     * Returns the 13 Kuwait public holidays.
     *
     * @param rollableFixed whether fixed-date holidays (New Year's Day, National Day,
     *                      Liberation Day) are rollable; all Islamic holidays are
     *                      always {@code rollable(false)}
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
                    .name("National Day")
                    .description("Kuwait National Day, marking independence from Britain on 19 June 1961; observed 25 February")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.FEBRUARY, 25)
                    .build(),
            Holiday.builder()
                    .name("Liberation Day")
                    .description("Kuwait Liberation Day, marking liberation from Iraqi occupation on 26 February 1991")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.FEBRUARY, 26)
                    .build(),
            Holiday.builder()
                    .name("Isra and Mi'raj")
                    .description("Night journey and ascension of the Prophet Muhammad (27 Rajab AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new IsraMiraj("kw"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr")
                    .description("First day of Eid al-Fitr, marking the end of Ramadan (1 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitr("kw"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr (2nd Day)")
                    .description("Second day of Eid al-Fitr (2 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitrDay2("kw"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr (3rd Day)")
                    .description("Third day of Eid al-Fitr (3 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitrDay3("kw"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha")
                    .description("First day of Eid al-Adha, the Feast of Sacrifice (10 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdha("kw"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha (2nd Day)")
                    .description("Second day of Eid al-Adha (11 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdhaDay2("kw"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha (3rd Day)")
                    .description("Third day of Eid al-Adha (12 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdhaDay3("kw"))
                    .build(),
            Holiday.builder()
                    .name("Arafat Day")
                    .description("Day of standing at Mount Arafat, the day before Eid al-Adha (9 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new ArafatDay("kw"))
                    .build(),
            Holiday.builder()
                    .name("Islamic New Year")
                    .description("First day of the Islamic lunar year (1 Muharram AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new IslamicNewYear("kw"))
                    .build(),
            Holiday.builder()
                    .name("Prophet's Birthday")
                    .description("Birthday of the Prophet Muhammad (12 Rabi' al-Awwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new ProphetsBirthday("kw"))
                    .build()
        );
    }

    /**
     * Returns the 13 KWD holidays: the base holiday set with all holidays
     * {@code rollable(false)}, for use with {@link org.holiday.calendar.function.DateRolls#noRoll()}.
     */
    static List<Holiday> kwdHolidays() {
        return baseHolidays(false);
    }
}