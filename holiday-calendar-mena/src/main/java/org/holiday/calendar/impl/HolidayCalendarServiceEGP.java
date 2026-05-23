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
 * Service for provision of Egypt exchange and central bank holiday calendar.
 *
 * <p>Calendar code: {@code EGP}. Covers market closure days for the Egyptian
 * Exchange (EGX) and Central Bank of Egypt (CBE) financial institutions.
 *
 * <p>Weekend: Friday + Saturday (GCC market convention). Fixed holidays are not
 * rolled — the settlement calendar uses natural calendar dates per
 * {@link DateRolls#noRoll()}, consistent with the AED, SAR, KWD, and QAR
 * convention.
 *
 * <p>Arafat Day is excluded from this calendar: it is a national public holiday
 * but is not an EGX / CBE settlement closure day.
 *
 * <p>Islamic holidays are sourced from {@code eid-al-fitr-eg.csv},
 * {@code eid-al-adha-eg.csv}, {@code islamic-new-year-eg.csv}, and
 * {@code mawlid-eg.csv}. Dates for 2024–2025 are official CBE / EGX announcements;
 * 2026–2055 are projected from the Umm al-Qura tabular Islamic calendar. Egypt
 * determines Islamic holiday dates by moon sighting and may differ from Saudi
 * Arabia by ±1 day.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceEGP extends AbstractHolidayCalendarService {

    private static final String CODE = "EGP";
    private static final String NAME = "Egypt (EGX/CBE) Holidays";

    public HolidayCalendarServiceEGP() {
        super(CODE, NAME);
    }

    @Override
    public OptionalInt dataValidThrough() {
        return OptionalInt.of(EgyptHolidays.DATA_VALID_THROUGH);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.noRoll())
                .weekendDays(EgyptHolidays.EGYPT_WEEKEND)
                .holidays(EgyptHolidays.egpHolidays())
                .build();
    }

}
