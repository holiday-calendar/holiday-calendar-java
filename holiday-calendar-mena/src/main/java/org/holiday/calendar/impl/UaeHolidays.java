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
import org.holiday.calendar.observance.islamic.mena.EidAlAdha;
import org.holiday.calendar.observance.islamic.mena.EidAlAdhaDay2;
import org.holiday.calendar.observance.islamic.mena.EidAlFitr;
import org.holiday.calendar.observance.islamic.mena.EidAlFitrDay2;
import org.holiday.calendar.observance.islamic.mena.IslamicNewYear;
import org.holiday.calendar.observance.islamic.mena.ProphetsBirthday;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

/**
 * Package-private factory building the UAE public holiday list shared by the
 * national ({@code AE}) and settlement ({@code AED}) calendars.
 *
 * <p>All six Islamic holidays are populated through {@value DATA_VALID_THROUGH}
 * via country-specific CSV lookup tables (country code {@code ae}).
 * Dates for 2024–2026 are official UAE SCA/DFM announcements; dates for
 * 2027–2055 are projected from the Umm al-Qura tabular Islamic calendar.
 *
 * <p>Note: Arafat Day (9 Dhu al-Hijjah) and Isra' Mi'raj (27 Rajab) are not
 * modelled here; include in a future revision if the UAE SCA declares them as
 * settlement closure days.
 */
class UaeHolidays {

    static final int DATA_VALID_THROUGH = 2055;

    // GCC market weekend: Friday + Saturday; Sunday is the first business day.
    static final List<DayOfWeek> UAE_WEEKEND = List.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);

    private UaeHolidays() {}

    /**
     * Returns the 10 UAE public holidays.
     *
     * @param rollableFixed whether fixed-date holidays (New Year's Day,
     *                      Commemoration Day, National Day, National Day 2nd Day)
     *                      are rollable; all Islamic holidays are always
     *                      {@code rollable(false)}
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
                    .observance(new EidAlFitr("ae"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr (2nd Day)")
                    .description("Second day of Eid al-Fitr (2 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitrDay2("ae"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha")
                    .description("First day of Eid al-Adha, the Feast of Sacrifice (10 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdha("ae"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha (2nd Day)")
                    .description("Second day of Eid al-Adha (11 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdhaDay2("ae"))
                    .build(),
            Holiday.builder()
                    .name("Islamic New Year")
                    .description("First day of the Islamic lunar year (1 Muharram AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new IslamicNewYear("ae"))
                    .build(),
            Holiday.builder()
                    .name("Prophet's Birthday")
                    .description("Birthday of the Prophet Muhammad (12 Rabi' al-Awwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new ProphetsBirthday("ae"))
                    .build(),
            Holiday.builder()
                    .name("Commemoration Day")
                    .description("UAE Commemoration Day, honouring Emirati Martyrs who died in service")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.NOVEMBER, 30)
                    .build(),
            Holiday.builder()
                    .name("National Day")
                    .description("UAE National Day, marking the formation of the United Arab Emirates on 2 December 1971")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.DECEMBER, 2)
                    .build(),
            Holiday.builder()
                    .name("National Day (2nd Day)")
                    .description("UAE National Day (2nd Day), continuation of National Day celebrations")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.DECEMBER, 3)
                    .build()
        );
    }
}
