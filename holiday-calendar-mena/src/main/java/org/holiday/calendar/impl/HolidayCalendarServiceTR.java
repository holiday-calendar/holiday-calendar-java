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

import org.holiday.calendar.AbstractHolidayCalendarService;
import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.function.DateRolls;

import java.util.OptionalInt;

/**
 * Service for provision of Turkey national public holiday calendar.
 *
 * <p>Calendar code: {@code TR}. Covers public holidays observed nationwide in the
 * Republic of Turkey under Law No. 2429 (Ulusal Bayram ve Genel Tatiller Hakkında
 * Kanun).
 *
 * <p>Weekend: Saturday + Sunday (standard Western weekend). Fixed holidays that
 * fall on Saturday or Sunday are observed on the following Monday per
 * {@link DateRolls#followingMonday()}.
 *
 * <p>Islamic holidays (Ramazan Bayramı / Eid al-Fitr × 3 days; Kurban Bayramı /
 * Eid al-Adha × 4 days) are sourced from Diyanet (Presidency of Religious Affairs)
 * ilmi takvim (scientific calendar) via {@code eid-al-fitr-tr.csv} and
 * {@code eid-al-adha-tr.csv}. Dates for 2024–2035 are official Diyanet-published
 * dates (confirmed against BIST announcements for 2024–2026); 2036–2055 are
 * computed via an ilmi takvim calculator (true lunar conjunction + Ankara sunset
 * visibility rule) pending Diyanet's own publication of those years. Diyanet's
 * method is confirmed to differ from the Umm al-Qura calendar used by other MENA
 * countries by ±1–2 days in some years — verify projected 2036–2055 dates
 * against official announcements as each year is
 * published. Corrections require a new JAR release.
 *
 * <p>Law No. 2429 itself declares Republic Day Eve (28 October) a nationwide
 * half-day holiday, in effect from 13:00 {@code Europe/Istanbul} through the end
 * of Republic Day on 29 October — it applies to all public institutions, not just
 * Borsa Istanbul (BIST) or the Central Bank of the Republic of Turkey (TCMB). This
 * is modelled as an {@code EARLY_CLOSE} holiday, non-rollable, and reported
 * separately via {@link HolidayCalendar#calculateEarlyCloses(int)} rather than
 * {@link HolidayCalendar#calculate(int)}. It does not shift when 28 October falls
 * on a Saturday or Sunday. The same closure is also carried by {@link
 * HolidayCalendarServiceTRY}, since it is a statutory holiday rather than a
 * market-only convention.
 *
 * @author <a href="mailto:dave@holiday-calendar.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceTR extends AbstractHolidayCalendarService {

    private static final String CODE = "TR";
    private static final String NAME = "Turkey (National) Holidays";

    public HolidayCalendarServiceTR() {
        super(CODE, NAME);
    }

    @Override
    public OptionalInt dataValidThrough() {
        return OptionalInt.of(TurkeyHolidays.DATA_VALID_THROUGH);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.followingMonday())
                .weekendDays(TurkeyHolidays.STANDARD_WEEKEND)
                .holidays(TurkeyHolidays.baseHolidays(true))
                .holidays(TurkeyHolidays.earlyCloseHolidays())
                .build();
    }

}
