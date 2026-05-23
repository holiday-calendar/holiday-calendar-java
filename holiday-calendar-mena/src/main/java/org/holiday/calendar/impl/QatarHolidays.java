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
import org.holiday.calendar.observance.islamic.mena.EidAlFitr;
import org.holiday.calendar.observance.islamic.mena.EidAlFitrDay2;
import org.holiday.calendar.observance.islamic.mena.EidAlFitrDay3;
import org.holiday.calendar.observance.qa.QatarBanksHoliday;
import org.holiday.calendar.observance.qa.QatarNationalSportsDay;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Package-private factory building the Qatar public holiday lists for the
 * national ({@code QA}) and settlement ({@code QAR}) calendars.
 *
 * <p>The national calendar ({@code QA}) observes 9 public holidays: New Year's Day,
 * Qatar National Sports Day, three days of Eid al-Fitr, three days of Eid al-Adha,
 * and National Day.
 *
 * <p>The settlement calendar ({@code QAR}) adds one QCB-specific closure:
 * the Qatar Banks Holiday (first Sunday of March, Cabinet Decision No. (33) of 2009),
 * for a total of 10 holidays.
 *
 * <p>Islamic New Year and Prophet's Birthday are not gazetted public holidays in
 * Qatar and are not included (cf. UAE and Saudi Arabia which observe both).
 *
 * <p>Eid al-Fitr and Eid al-Adha dates are populated through
 * {@value DATA_VALID_THROUGH} via country-specific CSV lookup tables
 * (country code {@code qa}). Dates for 2024–2026 are official Qatar Central Bank
 * (QCB) announcements; dates for 2027–2055 are projected from the Umm al-Qura
 * tabular Islamic calendar. Verify against official QCB/QSE announcements as
 * each year is published.
 *
 * <p>Note: the 2033 Gregorian year contains two Eid al-Fitr occurrences; only the
 * January occurrence is recorded in the CSV. See {@link EidAlFitr} for details.
 */
class QatarHolidays {

    static final int DATA_VALID_THROUGH = 2055;

    // GCC market weekend: Friday + Saturday; Sunday is the first business day.
    static final List<DayOfWeek> QATAR_WEEKEND = List.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);

    private QatarHolidays() {}

    /**
     * Returns the 9 Qatar public holidays.
     *
     * @param rollableFixed whether fixed-date holidays (New Year's Day, National Day)
     *                      are rollable; the floating Sports Day and all Islamic
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
                    .name("Qatar National Sports Day")
                    .description("Qatar National Sports Day — second Tuesday of February (Emiri Decree No. 80 of 2011)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new QatarNationalSportsDay())
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr")
                    .description("First day of Eid al-Fitr, marking the end of Ramadan (1 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitr("qa"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr (2nd Day)")
                    .description("Second day of Eid al-Fitr (2 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitrDay2("qa"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Fitr (3rd Day)")
                    .description("Third day of Eid al-Fitr (3 Shawwal AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlFitrDay3("qa"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha")
                    .description("First day of Eid al-Adha, the Feast of Sacrifice (10 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdha("qa"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha (2nd Day)")
                    .description("Second day of Eid al-Adha (11 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdhaDay2("qa"))
                    .build(),
            Holiday.builder()
                    .name("Eid al-Adha (3rd Day)")
                    .description("Third day of Eid al-Adha (12 Dhu al-Hijjah AH)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new EidAlAdhaDay3("qa"))
                    .build(),
            Holiday.builder()
                    .name("National Day")
                    .description("Qatar National Day, marking independence from Britain on 18 December 1971")
                    .type(Holiday.Type.FIXED)
                    .rollable(rollableFixed)
                    .monthDay(Month.DECEMBER, 18)
                    .build()
        );
    }

    /**
     * Returns the 10 Qatar QSE/QCB holidays: the 9 base holidays (with
     * {@code rollableFixed=false}) plus the Qatar Banks Holiday.
     */
    static List<Holiday> qarHolidays() {
        List<Holiday> holidays = new ArrayList<>(baseHolidays(false));
        holidays.add(
            Holiday.builder()
                    .name("Qatar Banks Holiday")
                    .description("Annual bank holiday for all QCB-supervised financial institutions — first Sunday of March (Cabinet Decision No. (33) of 2009)")
                    .type(Holiday.Type.FLOATING)
                    .rollable(false)
                    .observance(new QatarBanksHoliday())
                    .build()
        );
        return Collections.unmodifiableList(holidays);
    }
}
