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
 * Service for provision of Qatar national public holiday calendar.
 *
 * <p>Calendar code: {@code QA}. Covers public holidays observed in the State of
 * Qatar as declared by the Amiri Diwan and the Qatar Central Bank (QCB).
 *
 * <p>Weekend: Friday + Saturday (GCC market convention). Fixed holidays that fall
 * on Friday are observed on the preceding Thursday; holidays that fall on Saturday
 * are observed on the following Sunday, per {@link DateRolls#previousThursdayOrFollowingSunday()}
 * and confirmed Amiri Diwan precedents (National Day 2020 and 2021).
 *
 * <p>Islamic holidays are sourced from {@code eid-al-fitr-qa.csv} and
 * {@code eid-al-adha-qa.csv}. Dates for 2024–2026 are official QCB announcements;
 * 2027–2055 are projected from the Umm al-Qura tabular Islamic calendar.
 *
 * <p>Islamic New Year and Prophet's Birthday are not gazetted public holidays in
 * Qatar and are not included.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceQA extends AbstractHolidayCalendarService {

    private static final String CODE = "QA";
    private static final String NAME = "Qatar (National) Holidays";

    public HolidayCalendarServiceQA() {
        super(CODE, NAME);
    }

    @Override
    public OptionalInt dataValidThrough() {
        return OptionalInt.of(QatarHolidays.DATA_VALID_THROUGH);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                // Friday → preceding Thursday; Saturday → following Sunday (Amiri Diwan rule)
                .dateRoll(DateRolls.previousThursdayOrFollowingSunday())
                .weekendDays(QatarHolidays.QATAR_WEEKEND)
                .holidays(QatarHolidays.baseHolidays(true))
                .build();
    }

}
