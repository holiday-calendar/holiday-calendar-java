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
import org.holiday.calendar.observance.islamic.mena.ProphetsBirthdayDay2;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * Package-private factory building the Morocco public holiday list shared by
 * the national ({@code MA}) and settlement ({@code MAD}) calendars.
 *
 * <p>Morocco observes 17 public holidays: ten fixed Gregorian holidays and seven
 * Islamic holidays (two days each of Eid al-Fitr and Eid al-Adha, Islamic New Year,
 * and two days of the Prophet's Birthday / Mawlid an-Nabi).
 *
 * <p>Morocco uses a <strong>Saturday + Sunday weekend</strong> — unlike GCC countries,
 * which observe Friday + Saturday. Morocco switched to the Monday–Friday workweek in
 * 2004 to align with European trading partners.
 *
 * <p>The national calendar ({@code MA}) applies {@link org.holiday.calendar.function.DateRolls#followingMonday()}
 * to fixed Gregorian holidays ({@code rollable(true)}): when a fixed holiday falls on
 * Saturday or Sunday, it is observed on the following Monday, consistent with Moroccan
 * Labour Code practice. Islamic holidays are always {@code rollable(false)} — Islamic
 * holiday substitutions in Morocco are issued by case-by-case government decree rather
 * than by a statutory automatic roll rule.
 *
 * <p>The settlement calendar ({@code MAD}) uses {@link org.holiday.calendar.function.DateRolls#noRoll()}
 * with all holidays {@code rollable(false)}: the Casablanca Stock Exchange (CSE /
 * Bourse de Casablanca) observes holidays on their natural calendar dates, as reflected
 * in official CSE circular AV-2025-078.
 *
 * <p>The Amazigh New Year (Yennayer, January 14) was officially gazetted in November
 * 2023 and takes effect from 2024. It is included unconditionally as a
 * {@code FixedHoliday}; callers computing dates prior to 2024 should discard this
 * holiday as it was not legally observed before that year.
 *
 * <p>All Islamic holiday dates are populated through {@value DATA_VALID_THROUGH}
 * via country-specific CSV lookup tables (country code {@code ma}).
 * Dates for 2024–2025 are sourced from official Moroccan government / CSE holiday
 * announcements. Dates for 2026–2055 are projected from the Umm al-Qura tabular
 * Islamic calendar. Morocco determines Islamic holiday dates by moon sighting and
 * may differ from GCC countries by ±1 day — verify projected dates against official
 * Moroccan announcements as each year is published. Corrections require a new JAR
 * release; no runtime update mechanism exists.
 *
 * <p>Note: the 2033 Gregorian year contains two Eid al-Fitr occurrences; only the
 * January occurrence is recorded in the CSV. Similarly, 2039 contains two Eid al-Adha
 * occurrences; only January is recorded.
 */
class MoroccoHolidays {

    static final int DATA_VALID_THROUGH = 2055;

    static final List<DayOfWeek> MOROCCO_WEEKEND = List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    private MoroccoHolidays() {}

    /**
     * Returns the 17 Morocco public holidays.
     *
     * @param rollableFixed whether fixed-date holidays are rollable; all Islamic
     *                      holidays are always {@code rollable(false)}
     */
    static List<Holiday> baseHolidays(boolean rollableFixed) {
        List<Holiday> holidays = new ArrayList<>(17);
        holidays.add(Holiday.builder()
                .name("New Year's Day")
                .description("First day of the new year in the Common Era (CE)")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.JANUARY, 1)
                .build());
        holidays.add(Holiday.builder()
                .name("Manifesto of Independence Day")
                .description("Anniversary of the submission of the Independence Manifesto to the French "
                        + "Protectorate on 11 January 1944")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.JANUARY, 11)
                .build());
        holidays.add(Holiday.builder()
                .name("Amazigh New Year")
                .description("Yennayer — Amazigh (Berber) New Year; gazetted as a national public holiday "
                        + "in November 2023, effective from 2024. Callers computing dates prior to 2024 "
                        + "should discard this holiday as it was not legally observed before that year.")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.JANUARY, 14)
                .build());
        holidays.add(Holiday.builder()
                .name("Eid al-Fitr")
                .description("First day of Eid al-Fitr, marking the end of Ramadan (1 Shawwal AH)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EidAlFitr("ma"))
                .build());
        holidays.add(Holiday.builder()
                .name("Eid al-Fitr (2nd Day)")
                .description("Second day of Eid al-Fitr (2 Shawwal AH)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EidAlFitrDay2("ma"))
                .build());
        holidays.add(Holiday.builder()
                .name("Labour Day")
                .description("International Workers' Day")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.MAY, 1)
                .build());
        holidays.add(Holiday.builder()
                .name("Eid al-Adha")
                .description("First day of Eid al-Adha, the Feast of Sacrifice (10 Dhu al-Hijjah AH)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EidAlAdha("ma"))
                .build());
        holidays.add(Holiday.builder()
                .name("Eid al-Adha (2nd Day)")
                .description("Second day of Eid al-Adha (11 Dhu al-Hijjah AH)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new EidAlAdhaDay2("ma"))
                .build());
        holidays.add(Holiday.builder()
                .name("Throne Day")
                .description("Anniversary of the accession of King Mohammed VI to the throne on 30 July 1999")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.JULY, 30)
                .build());
        holidays.add(Holiday.builder()
                .name("Islamic New Year")
                .description("First day of the Islamic lunar year (1 Muharram AH)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new IslamicNewYear("ma"))
                .build());
        holidays.add(Holiday.builder()
                .name("Oued Ed-Dahab Allegiance Day")
                .description("Anniversary of the allegiance of Oued Ed-Dahab to Morocco on 14 August 1979")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.AUGUST, 14)
                .build());
        holidays.add(Holiday.builder()
                .name("Revolution of the King and the People")
                .description("Anniversary of the exile of Sultan Mohammed V by France on 20 August 1953")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.AUGUST, 20)
                .build());
        holidays.add(Holiday.builder()
                .name("Youth Day")
                .description("Birthday of King Mohammed VI (21 August 1963); national Youth Day")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.AUGUST, 21)
                .build());
        holidays.add(Holiday.builder()
                .name("Prophet's Birthday")
                .description("First day of Mawlid an-Nabi, commemorating the birth of the Prophet Muhammad "
                        + "(12 Rabi' al-Awwal AH)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new ProphetsBirthday("ma"))
                .build());
        holidays.add(Holiday.builder()
                .name("Prophet's Birthday (2nd Day)")
                .description("Second day of Mawlid an-Nabi (13 Rabi' al-Awwal AH)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new ProphetsBirthdayDay2("ma"))
                .build());
        holidays.add(Holiday.builder()
                .name("Green March Anniversary")
                .description("Anniversary of the Green March on 6 November 1975, marking Morocco's claim "
                        + "to the Western Sahara")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.NOVEMBER, 6)
                .build());
        holidays.add(Holiday.builder()
                .name("Independence Day")
                .description("Independence of Morocco from the French Protectorate on 18 November 1956")
                .type(Holiday.Type.FIXED)
                .rollable(rollableFixed)
                .monthDay(Month.NOVEMBER, 18)
                .build());
        return List.copyOf(holidays);
    }

    static List<Holiday> maHolidays() {
        return baseHolidays(true);
    }

    static List<Holiday> madHolidays() {
        return baseHolidays(false);
    }

}
