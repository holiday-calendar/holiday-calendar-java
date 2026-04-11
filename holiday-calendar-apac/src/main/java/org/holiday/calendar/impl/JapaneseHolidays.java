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
import org.holiday.calendar.SpecialAnniversary;
import org.holiday.calendar.observance.jp.AutumnalEquinoxDay;
import org.holiday.calendar.observance.jp.ComingOfAgeDay;
import org.holiday.calendar.observance.jp.EmperorsBirthday;
import org.holiday.calendar.observance.jp.MarineDay;
import org.holiday.calendar.observance.jp.RespectForTheAgedDay;
import org.holiday.calendar.observance.jp.SportsDay;
import org.holiday.calendar.observance.jp.VernalEquinoxDay;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

/**
 * Package-private factory that builds the shared base set of Japanese national
 * holidays used by both the TSE ({@code JP}) and BOJ ({@code JPY}) calendars.
 */
class JapaneseHolidays {

    private JapaneseHolidays() {}

    /**
     * Returns the 18-entry base holiday list shared by all Japanese calendars.
     * This includes the 16 annual national holidays plus two one-time
     * {@link SpecialAnniversary} entries for the imperial transition of 2019.
     */
    static List<Holiday> baseHolidays() {
        return List.of(
            // ── Annual national holidays ──────────────────────────────────

            Holiday.builder()
                    .name("New Year's Day")
                    .description("First day of new year in the Common Era (CE)")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.JANUARY, 1)
                    .build(),

            Holiday.builder()
                    .name("Coming of Age Day")
                    .description("National holiday honouring those who have reached the age of majority. " +
                                 "Fixed on January 15 until 1999; 2nd Monday in January from 2000.")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new ComingOfAgeDay())
                    .build(),

            Holiday.builder()
                    .name("National Foundation Day")
                    .description("Commemoration of the founding of Japan (建国記念の日). " +
                                 "Observed from 1967.")
                    .type(Holiday.Type.FLOATING)
                    .rollable(true)
                    .observance(y -> y >= 1967 ? LocalDate.of(y, Month.FEBRUARY, 11) : null)
                    .build(),

            Holiday.builder()
                    .name("Emperor's Birthday")
                    .description("Birthday of the reigning Emperor (天皇誕生日). " +
                                 "April 29 (Hirohito, 1949–1988); December 23 (Akihito, 1989–2018); " +
                                 "February 23 (Naruhito, 2020+). Not observed in 2019.")
                    .type(Holiday.Type.FLOATING)
                    .rollable(true)
                    .observance(new EmperorsBirthday())
                    .build(),

            Holiday.builder()
                    .name("Vernal Equinox Day")
                    .description("Day of the astronomical vernal equinox in Japan Standard Time (春分の日). " +
                                 "Computed via NAOJ formula; valid 1980–2099.")
                    .type(Holiday.Type.FLOATING)
                    .rollable(true)
                    .observance(new VernalEquinoxDay())
                    .build(),

            Holiday.builder()
                    .name("Showa Day")
                    .description("Reflection on the Showa era (昭和の日), observed on April 29. " +
                                 "Previously Emperor's Birthday (Hirohito, 1949–1988) and " +
                                 "Greenery Day (1989–2006); named Showa Day from 2007.")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.APRIL, 29)
                    .build(),

            Holiday.builder()
                    .name("Constitution Memorial Day")
                    .description("Commemoration of the promulgation of the Constitution of Japan (憲法記念日)")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.MAY, 3)
                    .build(),

            Holiday.builder()
                    .name("Greenery Day")
                    .description("Appreciation of nature and the environment (みどりの日). " +
                                 "Fixed on May 4 from 2007.")
                    .type(Holiday.Type.FLOATING)
                    .rollable(true)
                    .observance(y -> y >= 2007 ? LocalDate.of(y, Month.MAY, 4) : null)
                    .build(),

            Holiday.builder()
                    .name("Children's Day")
                    .description("Celebration of children's happiness and respect for mothers (こどもの日)")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.MAY, 5)
                    .build(),

            Holiday.builder()
                    .name("Marine Day")
                    .description("Gratitude for the ocean's bounties and hope for maritime-nation Japan (海の日). " +
                                 "Fixed on July 20 (1996–1999); 3rd Monday in July from 2000.")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new MarineDay())
                    .build(),

            Holiday.builder()
                    .name("Mountain Day")
                    .description("Appreciation of mountains and their blessings (山の日). " +
                                 "Observed from 2016.")
                    .type(Holiday.Type.FLOATING)
                    .rollable(true)
                    .observance(y -> y >= 2016 ? LocalDate.of(y, Month.AUGUST, 11) : null)
                    .build(),

            Holiday.builder()
                    .name("Respect for the Aged Day")
                    .description("Respect and appreciation for the elderly (敬老の日). " +
                                 "Fixed on September 15 (1966–2002); 3rd Monday in September from 2003.")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new RespectForTheAgedDay())
                    .build(),

            Holiday.builder()
                    .name("Autumnal Equinox Day")
                    .description("Day of the astronomical autumnal equinox in Japan Standard Time (秋分の日). " +
                                 "Computed via NAOJ formula; valid 1980–2099.")
                    .type(Holiday.Type.FLOATING)
                    .rollable(true)
                    .observance(new AutumnalEquinoxDay())
                    .build(),

            Holiday.builder()
                    .name("Sports Day")
                    .description("Promotion of sporting activity and healthy mind and body (スポーツの日). " +
                                 "Fixed on October 10 (1966–1999); 2nd Monday in October from 2000. " +
                                 "Renamed from Health and Sports Day in 2020.")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new SportsDay())
                    .build(),

            Holiday.builder()
                    .name("Culture Day")
                    .description("Promotion of culture, arts, and academic endeavour (文化の日)")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.NOVEMBER, 3)
                    .build(),

            Holiday.builder()
                    .name("Labour Thanksgiving Day")
                    .description("Gratitude for labour and production (勤労感謝の日)")
                    .type(Holiday.Type.FIXED)
                    .rollable(true)
                    .monthDay(Month.NOVEMBER, 23)
                    .build(),

            // ── One-time imperial transition holidays (2019) ──────────────

            new SpecialAnniversary(
                    "Emperor's Abdication Day",
                    "One-time national holiday marking the abdication of Emperor Akihito (April 30, 2019)",
                    LocalDate.of(2019, Month.APRIL, 30),
                    false),

            new SpecialAnniversary(
                    "Enthronement Day",
                    "One-time national holiday marking the accession of Emperor Naruhito (May 1, 2019)",
                    LocalDate.of(2019, Month.MAY, 1),
                    false)
        );
    }
}
