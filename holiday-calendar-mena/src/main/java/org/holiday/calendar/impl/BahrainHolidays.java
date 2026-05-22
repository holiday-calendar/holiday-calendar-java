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
import org.holiday.calendar.observance.islamic.mena.Ashura;
import org.holiday.calendar.observance.islamic.mena.AshuraDay2;
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
 * Package-private factory building the Bahrain public holiday lists for the
 * national ({@code BH}) and settlement ({@code BHD}) calendars.
 *
 * <p>The national calendar ({@code BH}) observes 15 public holidays: New Year's
 * Day, Labour Day, National Day, Accession Day (fixed Gregorian), Arafat Day,
 * three days of Eid al-Fitr, three days of Eid al-Adha, Islamic New Year, two days
 * of Ashura, and Prophet's Birthday (floating Islamic).
 *
 * <p>The settlement calendar ({@code BHD}) uses the identical holiday set with
 * {@code rollable(false)} for all holidays — the Central Bank of Bahrain (CBB) and
 * Boursa Bahrain apply no date adjustment per the no-roll convention consistent with
 * other GCC settlement calendars (AED, SAR, KWD, QAR, EGP).
 *
 * <p>Ashura (10–11 Muharram) is included in both BH and BHD. Bahrain gazetted
 * Ashura as a national public holiday under the Labour Law, and CBB circulars
 * confirm Boursa Bahrain closes on both days.
 *
 * <p>Islamic holiday dates are populated through {@value DATA_VALID_THROUGH} via
 * country-specific CSV lookup tables (country code {@code bh}). Dates for 2024–2025
 * are sourced from official CBB and Boursa Bahrain holiday announcements; dates for
 * 2026–2055 are projected from the Umm al-Qura tabular Islamic calendar. Bahrain
 * determines Islamic holiday dates by moon sighting and may differ from Saudi Arabia
 * by ±1 day; verify against official CBB / Boursa Bahrain announcements as each year
 * is published.
 *
 * <p>Note: the 2033 Gregorian year contains two Eid al-Fitr occurrences; only the
 * January occurrence is recorded in the CSV. See {@link EidAlFitr} for details.
 */
class BahrainHolidays {

    static final int DATA_VALID_THROUGH = 2055;

    // GCC market weekend: Friday + Saturday; Sunday is the first business day.
    static final List<DayOfWeek> BAHRAIN_WEEKEND = List.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);

    private BahrainHolidays() {}

    /**
     * Returns the 15 Bahrain public holidays.
     *
     * @param rollableFixed whether fixed-date holidays (New Year's Day, Labour Day,
     *                      National Day, Accession Day) are rollable; all Islamic
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
                    .observance(new EidAlFitr("bh"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr (2nd Day)")
                    .description("Second day of Eid al-Fitr (2 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitrDay2("bh"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr (3rd Day)")
                    .description("Third day of Eid al-Fitr (3 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitrDay3("bh"))
                    .build(),
            Holiday.builder()
                    .name("Labour Day")
                    .description("International Workers' Day")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.MAY, 1)
                    .build(),
            Holiday.builder()
                    .name("Arafat Day")
                    .description("Day of standing at Mount Arafat, the day before Eid al-Adha (9 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new ArafatDay("bh"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha")
                    .description("First day of Eid al-Adha, the Feast of Sacrifice (10 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdha("bh"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha (2nd Day)")
                    .description("Second day of Eid al-Adha (11 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdhaDay2("bh"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha (3rd Day)")
                    .description("Third day of Eid al-Adha (12 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdhaDay3("bh"))
                    .build(),
            Holiday.builder()
                    .name("Islamic New Year")
                    .description("First day of the Islamic lunar year (1 Muharram AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new IslamicNewYear("bh"))
                    .build(),
            Holiday.builder()
                    .name("Ashura")
                    .description("First day of Ashura, marking 10 Muharram AH; two-day national holiday in Bahrain")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new Ashura("bh"))
                    .build(),
            Holiday.builder()
                    .name("Ashura (2nd Day)")
                    .description("Second day of Ashura (11 Muharram AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new AshuraDay2("bh"))
                    .build(),
            Holiday.builder()
                    .name("Prophet's Birthday")
                    .description("Birthday of the Prophet Muhammad (12 Rabi' al-Awwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new ProphetsBirthday("bh"))
                    .build(),
            Holiday.builder()
                    .name("National Day")
                    .description("National Day of the Kingdom of Bahrain, marking independence from Britain on 15 August 1971; observed 16 December")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.DECEMBER, 16)
                    .build(),
            Holiday.builder()
                    .name("Accession Day")
                    .description("Accession of King Hamad bin Isa Al Khalifa to the throne on 6 March 1999; observed 17 December")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.DECEMBER, 17)
                    .build()
        );
    }

    /**
     * Returns the 15 BHD holidays: the base holiday set with all holidays
     * {@code rollable(false)}, for use with {@link org.holiday.calendar.function.DateRolls#noRoll()}.
     */
    static List<Holiday> bhdHolidays() {
        return baseHolidays(false);
    }

}
