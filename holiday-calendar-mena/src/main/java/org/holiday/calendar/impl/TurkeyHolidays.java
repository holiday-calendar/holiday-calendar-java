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
import org.holiday.calendar.observance.islamic.mena.EidAlAdhaDay3;
import org.holiday.calendar.observance.islamic.mena.EidAlAdhaDay4;
import org.holiday.calendar.observance.islamic.mena.EidAlFitr;
import org.holiday.calendar.observance.islamic.mena.EidAlFitrDay2;
import org.holiday.calendar.observance.islamic.mena.EidAlFitrDay3;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * Package-private factory building the Turkey public holiday list shared by
 * the national ({@code TR}) and settlement ({@code TRY}) calendars.
 *
 * <p>All seven Islamic holidays are populated through {@value DATA_VALID_THROUGH}
 * via Diyanet-sourced CSV lookup tables (country code {@code tr}).
 * Dates for 2024–2026 are official Diyanet (Presidency of Religious Affairs) /
 * BIST market calendar announcements; dates for 2027–2055 are projected from
 * the Umm al-Qura tabular Islamic calendar. Diyanet uses ilmi takvim (scientific
 * method), which may differ from the Umm al-Qura calendar by ±1 day — verify
 * projected dates against Diyanet announcements as each year is published.
 * Corrections require a new JAR release; no runtime update mechanism exists.
 *
 * <p>Turkey observes three days of Eid al-Fitr (Ramazan Bayramı) and four days
 * of Eid al-Adha (Kurban Bayramı), consistent with BIST market closure announcements.
 *
 * <p><strong>Half-day closure not modelled:</strong>
 * Borsa Istanbul (BIST) and TCMB observe a partial closure on 28 October
 * (Republic Day Eve): BIST closes at 12:30 local time and TCMB suspends TRY
 * settlement from midday. This calendar does not include 28 October as a holiday.
 * Callers relying on afternoon liquidity or same-day settlement on this date must
 * apply their own adjustment.
 *
 * <p>Note: the 2033 Gregorian year contains two Eid al-Fitr occurrences; only the
 * January occurrence is recorded in the CSV. See {@link EidAlFitr} for details.
 * Similarly, 2039 contains two Eid al-Adha occurrences; only January is recorded.
 */
class TurkeyHolidays {

    static final int DATA_VALID_THROUGH = 2055;

    static final List<DayOfWeek> STANDARD_WEEKEND = List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    private TurkeyHolidays() {}

    /**
     * Returns the 15 Turkey public holidays.
     *
     * @param rollableFixed whether fixed-date holidays are rollable; all Islamic
     *                      holidays are always {@code rollable(false)}
     * @param additional    additional holidays to append (e.g. future BIST-specific
     *                      closures); pass {@link List#of()} when not needed
     */
    static List<Holiday> baseHolidays(boolean rollableFixed, List<Holiday> additional) {
        List<Holiday> holidays = new ArrayList<>(15 + additional.size());
        holidays.add(Holiday.builder()
                .name("New Year's Day")
                .description("First day of the new year in the Common Era (CE)")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.JANUARY, 1)
                .build());
        holidays.add(Holiday.builder()
                .name("National Sovereignty and Children's Day")
                .description("Commemorates the opening of the Grand National Assembly on 23 April 1920")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.APRIL, 23)
                .build());
        holidays.add(Holiday.builder()
                .name("Labour and Solidarity Day")
                .description("International Workers' Day")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.MAY, 1)
                .build());
        holidays.add(Holiday.builder()
                .name("Commemoration of Atatürk, Youth and Sports Day")
                .description("Commemorates Mustafa Kemal Atatürk's arrival in Samsun on 19 May 1919, "
                        + "marking the start of the Turkish War of Independence")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.MAY, 19)
                .build());
        holidays.add(Holiday.builder()
                .name("Eid al-Fitr")
                .description("First day of Ramazan Bayramı (Eid al-Fitr), marking the end of Ramadan "
                        + "(1 Shawwal AH) per Diyanet ilmi takvim")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EidAlFitr("tr"))
                .build());
        holidays.add(Holiday.builder()
                .name("Eid al-Fitr (2nd Day)")
                .description("Second day of Ramazan Bayramı (2 Shawwal AH)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EidAlFitrDay2("tr"))
                .build());
        holidays.add(Holiday.builder()
                .name("Eid al-Fitr (3rd Day)")
                .description("Third day of Ramazan Bayramı (3 Shawwal AH)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EidAlFitrDay3("tr"))
                .build());
        holidays.add(Holiday.builder()
                .name("Democracy and National Unity Day")
                .description("Commemorates the failed coup attempt of 15 July 2016")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.JULY, 15)
                .build());
        holidays.add(Holiday.builder()
                .name("Eid al-Adha")
                .description("First day of Kurban Bayramı (Eid al-Adha), the Feast of Sacrifice "
                        + "(10 Dhu al-Hijjah AH) per Diyanet ilmi takvim")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EidAlAdha("tr"))
                .build());
        holidays.add(Holiday.builder()
                .name("Eid al-Adha (2nd Day)")
                .description("Second day of Kurban Bayramı (11 Dhu al-Hijjah AH)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EidAlAdhaDay2("tr"))
                .build());
        holidays.add(Holiday.builder()
                .name("Eid al-Adha (3rd Day)")
                .description("Third day of Kurban Bayramı (12 Dhu al-Hijjah AH)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EidAlAdhaDay3("tr"))
                .build());
        holidays.add(Holiday.builder()
                .name("Eid al-Adha (4th Day)")
                .description("Fourth day of Kurban Bayramı (13 Dhu al-Hijjah AH)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EidAlAdhaDay4("tr"))
                .build());
        holidays.add(Holiday.builder()
                .name("Victory Day")
                .description("Commemorates the decisive Turkish victory at the Battle of Dumlupınar "
                        + "on 30 August 1922")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.AUGUST, 30)
                .build());
        holidays.add(Holiday.builder()
                .name("Republic Day")
                .description("Commemorates the proclamation of the Republic of Turkey on 29 October 1923")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.OCTOBER, 29)
                .build());
        holidays.addAll(additional);
        return List.copyOf(holidays);
    }
}
