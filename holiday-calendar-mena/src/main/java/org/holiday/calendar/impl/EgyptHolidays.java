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
import org.holiday.calendar.observance.eg.ShamElNessim;
import org.holiday.calendar.observance.islamic.mena.ArafatDay;
import org.holiday.calendar.observance.islamic.mena.EidAlAdha;
import org.holiday.calendar.observance.islamic.mena.EidAlAdhaDay2;
import org.holiday.calendar.observance.islamic.mena.EidAlAdhaDay3;
import org.holiday.calendar.observance.islamic.mena.EidAlFitr;
import org.holiday.calendar.observance.islamic.mena.EidAlFitrDay2;
import org.holiday.calendar.observance.islamic.mena.EidAlFitrDay3;
import org.holiday.calendar.observance.islamic.mena.IslamicNewYear;
import org.holiday.calendar.observance.islamic.mena.ProphetsBirthday;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

/**
 * Package-private factory building the Egypt public holiday lists for the
 * national ({@code EG}) and settlement ({@code EGP}) calendars.
 *
 * <p>The national calendar ({@code EG}) observes 17 public holidays: Coptic
 * Christmas, Revolution Day (25 January), Sinai Liberation Day, Labour Day,
 * June 30 Revolution, Revolution Day (23 July), Armed Forces Day, Sham El-Nessim,
 * Arafat Day, three days of Eid al-Fitr, three days of Eid al-Adha, Islamic New
 * Year, and Prophet's Birthday.
 *
 * <p>The settlement calendar ({@code EGP}) uses the same holiday set minus
 * Arafat Day (16 holidays), with {@code rollable(false)} for all holidays — the
 * Egyptian Exchange (EGX) and Central Bank of Egypt (CBE) apply no date adjustment
 * per the no-roll convention.
 *
 * <p>Islamic holiday dates are populated through {@value DATA_VALID_THROUGH} via
 * country-specific CSV lookup tables (country code {@code eg}). Dates for 2024–2025
 * are sourced from official CBE / EGX announcements; dates for 2026–2055 are
 * projected from the Umm al-Qura tabular Islamic calendar. Egypt determines Islamic
 * holiday dates by moon sighting and may differ from Saudi Arabia by ±1 day; verify
 * against official CBE / EGX announcements as each year is published.
 *
 * <p>Sham El-Nessim is computed algorithmically as the day after Coptic Easter
 * (which uses the same Julian-calendar algorithm as Orthodox Easter) and is
 * not subject to the CSV data ceiling.
 *
 * <p>Note: Egypt frequently announces ad hoc public holidays and bridge days via
 * Prime Minister's decree. These are not capturable in a static calendar; consult
 * official CBE / EGX announcements for the current year.
 *
 * <p>Note: the 2033 Gregorian year contains two Eid al-Fitr occurrences; only the
 * January occurrence is recorded in the CSV. See {@link EidAlFitr} for details.
 */
class EgyptHolidays {

    static final int DATA_VALID_THROUGH = 2055;

    // GCC market weekend: Friday + Saturday; Sunday is the first business day.
    static final List<DayOfWeek> EGYPT_WEEKEND = List.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);

    private EgyptHolidays() {}

    /**
     * Returns the 17 Egypt public holidays.
     *
     * @param rollableFixed whether fixed-date holidays are rollable; Sham El-Nessim
     *                      and all Islamic holidays are always {@code rollable(false)}
     */
    static List<Holiday> baseHolidays(boolean rollableFixed) {
        return List.of(
            Holiday.builder()
                    .name("Coptic Christmas")
                    .description("Christmas celebration in the Coptic Orthodox Christian tradition (7 January)")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.JANUARY, 7)
                    .build(),
            Holiday.builder()
                    .name("Revolution Day")
                    .description("25 January Revolution, marking the start of the 2011 Egyptian uprising")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.JANUARY, 25)
                    .build(),
            Holiday.builder()
                    .name("Sinai Liberation Day")
                    .description("Return of the Sinai Peninsula to Egyptian sovereignty on 25 April 1982")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.APRIL, 25)
                    .build(),
            Holiday.builder()
                    .name("Sham El-Nessim")
                    .description("Egyptian spring festival celebrated the day after Coptic Easter; observed by all Egyptians regardless of religion")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new ShamElNessim())
                    .build(),
            Holiday.builder()
                    .name("Labour Day")
                    .description("International Workers' Day")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.MAY, 1)
                    .build(),
            Holiday.builder()
                    .name("June 30 Revolution")
                    .description("30 June Revolution, marking the removal of Mohamed Morsi from the presidency in 2013")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.JUNE, 30)
                    .build(),
            Holiday.builder()
                    .name("Revolution Day (July 23)")
                    .description("23 July Revolution, marking the Free Officers' overthrow of King Farouk in 1952")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.JULY, 23)
                    .build(),
            Holiday.builder()
                    .name("Armed Forces Day")
                    .description("Egyptian Armed Forces Day, commemorating the 1973 October War crossing of the Suez Canal")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.OCTOBER, 6)
                    .build(),
            Holiday.builder()
                    .name("Arafat Day")
                    .description("Day of standing at Mount Arafat, the day before Eid al-Adha (9 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new ArafatDay("eg"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr")
                    .description("First day of Eid al-Fitr, marking the end of Ramadan (1 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitr("eg"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr (2nd Day)")
                    .description("Second day of Eid al-Fitr (2 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitrDay2("eg"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr (3rd Day)")
                    .description("Third day of Eid al-Fitr (3 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitrDay3("eg"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha")
                    .description("First day of Eid al-Adha, the Feast of Sacrifice (10 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdha("eg"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha (2nd Day)")
                    .description("Second day of Eid al-Adha (11 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdhaDay2("eg"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha (3rd Day)")
                    .description("Third day of Eid al-Adha (12 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdhaDay3("eg"))
                    .build(),
            Holiday.builder()
                    .name("Islamic New Year")
                    .description("First day of the Islamic lunar year (1 Muharram AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new IslamicNewYear("eg"))
                    .build(),
            Holiday.builder()
                    .name("Prophet's Birthday")
                    .description("Birthday of the Prophet Muhammad (12 Rabi' al-Awwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new ProphetsBirthday("eg"))
                    .build()
        );
    }

    /**
     * Returns the 16 EGP holidays: the base holiday set without Arafat Day, with
     * all holidays {@code rollable(false)}, for use with
     * {@link org.holiday.calendar.function.DateRolls#noRoll()}.
     */
    static List<Holiday> egpHolidays() {
        return baseHolidays(false).stream()
                .filter(h -> !"Arafat Day".equals(h.getName()))
                .toList();
    }

}
