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
import org.holiday.calendar.observance.tr.RepublicDayEveEarlyClose;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Package-private factory building the Turkey public holiday list shared by
 * the national ({@code TR}) and settlement ({@code TRY}) calendars.
 *
 * <p>All seven Islamic holidays are populated through {@value DATA_VALID_THROUGH}
 * via Diyanet-sourced CSV lookup tables (country code {@code tr}).
 * Dates for 2024–2035 are official Diyanet (Presidency of Religious Affairs)
 * published dates (Diyanet publishes several years ahead of the current year,
 * not just 1–2 years as originally assumed — see GitHub issue #180), confirmed
 * against BIST market calendar announcements for 2024–2026; dates for 2036–2055
 * are computed via {@code IlmiTakvimCalculator} (true lunar conjunction + Ankara
 * sunset visibility rule, calibrated against and exactly reproducing all 24
 * Diyanet-published dates 2024–2035) pending Diyanet's own publication of those
 * years. Diyanet's ilmi takvim (scientific method) is confirmed to differ from
 * the Umm al-Qura calendar used by other MENA countries in this package by
 * ±1–2 days in some years (e.g. 2026 Eid al-Adha: Diyanet 27 May vs.
 * UAE/Umm al-Qura 26 May) — verify projected 2036–2055 dates against Diyanet
 * announcements as each year is published.
 * Corrections require a new JAR release; no runtime update mechanism exists.
 *
 * <p>Turkey observes three days of Eid al-Fitr (Ramazan Bayramı) and four days
 * of Eid al-Adha (Kurban Bayramı), consistent with BIST market closure announcements.
 *
 * <p>Law No. 2429 (Ulusal Bayram ve Genel Tatiller Hakkında Kanun) declares
 * Republic Day Eve (28 October) a nationwide half-day holiday, in effect from
 * 13:00 {@code Europe/Istanbul}. It applies to all public institutions, not just
 * Borsa Istanbul (BIST) or the Central Bank of the Republic of Turkey (TCMB), so
 * it is modelled as an {@link Holiday.Type#EARLY_CLOSE} holiday via {@link
 * #earlyCloseHolidays()} and consumed by both {@link HolidayCalendarServiceTR}
 * and {@link HolidayCalendarServiceTRY}. It does not shift when 28 October falls
 * on a Saturday or Sunday — see {@link RepublicDayEveEarlyClose}.
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
     * Returns the 14 Turkey full-day public holidays. Does not include the
     * Republic Day Eve early close; see {@link #earlyCloseHolidays()}.
     *
     * @param rollableFixed whether fixed-date holidays are rollable; all Islamic
     *                      holidays are always {@code rollable(false)}
     */
    static List<Holiday> baseHolidays(boolean rollableFixed) {
        List<Holiday> holidays = new ArrayList<>(14);
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
        return List.copyOf(holidays);
    }

    /**
     * Returns the single {@code EARLY_CLOSE} holiday representing the
     * nationwide Republic Day Eve half-day (28 October, in effect from 13:00
     * {@code Europe/Istanbul} per Law No. 2429). Does not shift when 28 October
     * falls on a Saturday or Sunday; see {@link RepublicDayEveEarlyClose}.
     */
    static List<Holiday> earlyCloseHolidays() {
        return List.of(
            Holiday.builder()
                    .name("Republic Day Eve")
                    .description("Nationwide half-day ahead of Republic Day per Law No. 2429; no "
                            + "adjustment when 28 October falls on a Saturday or Sunday")
                    .type(Holiday.Type.EARLY_CLOSE)
                    .rollable(false)
                    .observance(new RepublicDayEveEarlyClose())
                    .closeTime(LocalTime.of(13, 0))
                    .zoneId(ZoneId.of("Europe/Istanbul"))
                    .build()
        );
    }
}
