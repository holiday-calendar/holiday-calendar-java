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

import java.util.List;
import java.util.OptionalInt;

/**
 * Service for provision of Turkey national public holiday calendar.
 *
 * <p>Calendar code: {@code TR}. Covers public holidays observed in the Republic
 * of Turkey as declared by Borsa Istanbul (BIST) and the Central Bank of the
 * Republic of Turkey (TCMB).
 *
 * <p>Weekend: Saturday + Sunday (standard Western weekend). Fixed holidays that
 * fall on Saturday or Sunday are observed on the following Monday per
 * {@link DateRolls#followingMonday()}.
 *
 * <p>Islamic holidays (Ramazan Bayramı / Eid al-Fitr × 3 days; Kurban Bayramı /
 * Eid al-Adha × 4 days) are sourced from Diyanet (Presidency of Religious Affairs)
 * ilmi takvim (scientific calendar) via {@code eid-al-fitr-tr.csv} and
 * {@code eid-al-adha-tr.csv}. Dates for 2024–2026 are official Diyanet / BIST
 * announcements; 2027–2055 are projected from the Umm al-Qura tabular Islamic
 * calendar. Diyanet uses ilmi takvim, which may differ from the Umm al-Qura
 * calendar by ±1 day — verify projected dates against official announcements as
 * each year is published. Corrections require a new JAR release.
 *
 * <p><strong>Half-day closure not modelled:</strong>
 * Borsa Istanbul (BIST) and TCMB observe a partial closure on 28 October
 * (Republic Day Eve): BIST closes at 12:30 local time and TCMB suspends TRY
 * settlement from midday. This calendar does not include 28 October as a holiday.
 * Callers relying on afternoon liquidity or same-day settlement on this date must
 * apply their own adjustment.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
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
                .holidays(TurkeyHolidays.baseHolidays(true, List.of()))
                .build();
    }

}
