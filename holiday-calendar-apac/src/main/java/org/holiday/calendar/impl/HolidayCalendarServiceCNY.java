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
import org.holiday.calendar.Holiday;
import org.holiday.calendar.HolidayCalendar;
import org.holiday.calendar.function.DateRolls;
import org.holiday.calendar.observance.lunar.ChineseNewYearDay;
import org.holiday.calendar.observance.lunar.DragonBoatFestival;
import org.holiday.calendar.observance.lunar.MidAutumnFestival;
import org.holiday.calendar.observance.lunar.QingmingFestival;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service for provision of the China (PBOC / CNAPS) holiday calendar.
 *
 * <p>The calendar code is {@code CNY}, reflecting the operational calendar of
 * the People's Bank of China (PBOC) and the Chinese interbank payment system
 * (CNAPS/HVPS). It covers the seven statutory public holidays as defined by
 * China's State Council, modelling the full statutory closure window for each:
 *
 * <ul>
 *   <li>New Year's Day — 1 January</li>
 *   <li>Spring Festival (Chinese New Year) — Days 1–7 of the 1st lunar month</li>
 *   <li>Qingming Festival — solar term at ecliptic longitude 15° (~4–5 April)</li>
 *   <li>Labour Day — 1–3 May</li>
 *   <li>Dragon Boat Festival — 5th day of the 5th lunar month</li>
 *   <li>Mid-Autumn Festival — 15th day of the 8th lunar month</li>
 *   <li>National Day Golden Week — 1–7 October</li>
 * </ul>
 *
 * <h2>Compensatory (Make-up) Working Days</h2>
 *
 * <p>China's State Council annually publishes a schedule that extends some
 * holiday windows into adjacent weekdays and designates nearby Saturdays or
 * Sundays as compensatory working days. These make-up working days are not
 * modelled within the {@link HolidayCalendar} abstraction (which tracks
 * closures, not openings), but are accessible via
 * {@link #getCompensatoryWorkingDays(int)}.
 *
 * <p><strong>Annual update required:</strong> compensatory working day data
 * must be updated each year once the State Council issues its holiday notice,
 * typically published in late November or early December of the preceding year.
 * See <a href="http://www.gov.cn/">www.gov.cn</a> for official notices.
 * Requesting a year beyond the known data range logs a warning and returns an
 * empty list.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceCNY extends AbstractHolidayCalendarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HolidayCalendarServiceCNY.class);

    private static final String CODE = "CNY";
    private static final String NAME = "China (PBOC) Holidays";

    /**
     * Compensatory (make-up) working days published by China's State Council.
     * These are Saturdays or Sundays that are designated as working days to
     * compensate for extended holiday windows.
     *
     * <p>Sources: annual State Council holiday notices at www.gov.cn.
     * Dates must be verified against official notices before each calendar year.</p>
     */
    private static final Map<Integer, List<LocalDate>> COMPENSATORY_WORKING_DAYS = Map.ofEntries(
        Map.entry(2020, List.of(
            LocalDate.of(2020, 1, 19),   // New Year's bridge
            LocalDate.of(2020, 2, 1),    // Spring Festival bridge
            LocalDate.of(2020, 4, 26),   // Labour Day bridge
            LocalDate.of(2020, 5, 9),    // Labour Day bridge
            LocalDate.of(2020, 6, 28),   // Dragon Boat bridge
            LocalDate.of(2020, 9, 27),   // National Day bridge
            LocalDate.of(2020, 10, 10)   // National Day bridge
        )),
        Map.entry(2021, List.of(
            LocalDate.of(2021, 2, 7),    // Spring Festival bridge
            LocalDate.of(2021, 2, 20),   // Spring Festival bridge
            LocalDate.of(2021, 4, 25),   // Labour Day bridge
            LocalDate.of(2021, 5, 8),    // Labour Day bridge
            LocalDate.of(2021, 9, 18),   // Mid-Autumn bridge
            LocalDate.of(2021, 10, 9)    // National Day bridge
        )),
        Map.entry(2022, List.of(
            LocalDate.of(2022, 1, 29),   // Spring Festival bridge
            LocalDate.of(2022, 4, 2),    // Qingming bridge
            LocalDate.of(2022, 4, 24),   // Labour Day bridge
            LocalDate.of(2022, 5, 7),    // Labour Day bridge
            LocalDate.of(2022, 10, 8),   // National Day bridge
            LocalDate.of(2022, 10, 9)    // National Day bridge
        )),
        Map.entry(2023, List.of(
            LocalDate.of(2023, 1, 28),   // Spring Festival bridge
            LocalDate.of(2023, 1, 29),   // Spring Festival bridge
            LocalDate.of(2023, 4, 23),   // Labour Day bridge
            LocalDate.of(2023, 5, 6),    // Labour Day bridge
            LocalDate.of(2023, 6, 25),   // Dragon Boat bridge
            LocalDate.of(2023, 10, 7),   // National Day bridge
            LocalDate.of(2023, 10, 8)    // National Day bridge
        )),
        Map.entry(2024, List.of(
            LocalDate.of(2024, 2, 4),    // Spring Festival bridge
            LocalDate.of(2024, 2, 18),   // Spring Festival bridge
            LocalDate.of(2024, 4, 7),    // Qingming bridge
            LocalDate.of(2024, 4, 28),   // Labour Day bridge
            LocalDate.of(2024, 5, 11),   // Labour Day bridge
            LocalDate.of(2024, 9, 14),   // Mid-Autumn bridge
            LocalDate.of(2024, 9, 29),   // National Day bridge
            LocalDate.of(2024, 10, 12)   // National Day bridge
        )),
        Map.entry(2025, List.of(
            LocalDate.of(2025, 1, 26),   // Spring Festival bridge
            LocalDate.of(2025, 2, 8),    // Spring Festival bridge
            LocalDate.of(2025, 4, 27),   // Labour Day bridge
            LocalDate.of(2025, 9, 28),   // National Day bridge
            LocalDate.of(2025, 10, 11)   // National Day bridge
        ))
    );

    public HolidayCalendarServiceCNY() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final Holiday newYearsDay = Holiday.builder()
                .name("New Year's Day")
                .description("First day of new year in the Common Era (CE)")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.JANUARY, 1)
                .build();

        // Spring Festival (Chinese New Year) — 7-day statutory window
        final Holiday springFestivalDay1 = Holiday.builder()
                .name("Spring Festival (Day 1)")
                .description("First day of the Chinese lunisolar new year")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new ChineseNewYearDay(1))
                .build();
        final Holiday springFestivalDay2 = Holiday.builder()
                .name("Spring Festival (Day 2)")
                .description("Second day of the Chinese lunisolar new year")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new ChineseNewYearDay(2))
                .build();
        final Holiday springFestivalDay3 = Holiday.builder()
                .name("Spring Festival (Day 3)")
                .description("Third day of the Chinese lunisolar new year")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new ChineseNewYearDay(3))
                .build();
        final Holiday springFestivalDay4 = Holiday.builder()
                .name("Spring Festival (Day 4)")
                .description("Fourth day of the Chinese lunisolar new year")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new ChineseNewYearDay(4))
                .build();
        final Holiday springFestivalDay5 = Holiday.builder()
                .name("Spring Festival (Day 5)")
                .description("Fifth day of the Chinese lunisolar new year")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new ChineseNewYearDay(5))
                .build();
        final Holiday springFestivalDay6 = Holiday.builder()
                .name("Spring Festival (Day 6)")
                .description("Sixth day of the Chinese lunisolar new year")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new ChineseNewYearDay(6))
                .build();
        final Holiday springFestivalDay7 = Holiday.builder()
                .name("Spring Festival (Day 7)")
                .description("Seventh day of the Chinese lunisolar new year")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new ChineseNewYearDay(7))
                .build();

        final Holiday qingmingFestival = Holiday.builder()
                .name("Qingming Festival")
                .description("Tomb Sweeping Day; solar term at ecliptic longitude 15°")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new QingmingFestival())
                .build();

        // Labour Day — 3-day statutory window
        final Holiday labourDay1 = Holiday.builder()
                .name("Labour Day (Day 1)")
                .description("International Workers' Day")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.MAY, 1)
                .build();
        final Holiday labourDay2 = Holiday.builder()
                .name("Labour Day (Day 2)")
                .description("International Workers' Day (continued)")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.MAY, 2)
                .build();
        final Holiday labourDay3 = Holiday.builder()
                .name("Labour Day (Day 3)")
                .description("International Workers' Day (continued)")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.MAY, 3)
                .build();

        final Holiday dragonBoatFestival = Holiday.builder()
                .name("Dragon Boat Festival")
                .description("Duanwu; 5th day of the 5th lunar month")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new DragonBoatFestival())
                .build();

        final Holiday midAutumnFestival = Holiday.builder()
                .name("Mid-Autumn Festival")
                .description("15th day of the 8th lunar month; full moon of autumn")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new MidAutumnFestival())
                .build();

        // National Day Golden Week — 7-day statutory window (1–7 October)
        final Holiday nationalDay1 = Holiday.builder()
                .name("National Day (Day 1)")
                .description("Founding of the People's Republic of China, 1 October 1949")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.OCTOBER, 1)
                .build();
        final Holiday nationalDay2 = Holiday.builder()
                .name("National Day (Day 2)")
                .description("National Day Golden Week (continued)")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.OCTOBER, 2)
                .build();
        final Holiday nationalDay3 = Holiday.builder()
                .name("National Day (Day 3)")
                .description("National Day Golden Week (continued)")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.OCTOBER, 3)
                .build();
        final Holiday nationalDay4 = Holiday.builder()
                .name("National Day (Day 4)")
                .description("National Day Golden Week (continued)")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.OCTOBER, 4)
                .build();
        final Holiday nationalDay5 = Holiday.builder()
                .name("National Day (Day 5)")
                .description("National Day Golden Week (continued)")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.OCTOBER, 5)
                .build();
        final Holiday nationalDay6 = Holiday.builder()
                .name("National Day (Day 6)")
                .description("National Day Golden Week (continued)")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.OCTOBER, 6)
                .build();
        final Holiday nationalDay7 = Holiday.builder()
                .name("National Day (Day 7)")
                .description("National Day Golden Week (continued)")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.OCTOBER, 7)
                .build();

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.noRoll())
                .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                .holiday(newYearsDay)
                .holiday(springFestivalDay1)
                .holiday(springFestivalDay2)
                .holiday(springFestivalDay3)
                .holiday(springFestivalDay4)
                .holiday(springFestivalDay5)
                .holiday(springFestivalDay6)
                .holiday(springFestivalDay7)
                .holiday(qingmingFestival)
                .holiday(labourDay1)
                .holiday(labourDay2)
                .holiday(labourDay3)
                .holiday(dragonBoatFestival)
                .holiday(midAutumnFestival)
                .holiday(nationalDay1)
                .holiday(nationalDay2)
                .holiday(nationalDay3)
                .holiday(nationalDay4)
                .holiday(nationalDay5)
                .holiday(nationalDay6)
                .holiday(nationalDay7)
                .build();
    }

    /**
     * Returns the compensatory (make-up) working days for the given year, as
     * published annually by China's State Council. These are Saturdays or Sundays
     * that are designated as normal working days to compensate for extended holiday
     * windows, and are therefore <em>not</em> non-business days despite falling on
     * a weekend.
     *
     * <p>If data for the requested year is not yet available (i.e., the State
     * Council has not yet published its notice), a warning is logged and an empty
     * list is returned. Callers should treat an empty result as "data unavailable"
     * rather than "no make-up days" for future years.
     *
     * <p><strong>Annual update required:</strong> add each year's data once the
     * State Council notice is published (typically November–December of the
     * preceding year). See <a href="http://www.gov.cn/">www.gov.cn</a>.
     *
     * @param year the Gregorian year for which to retrieve make-up working days
     * @return unmodifiable list of make-up working day dates, or empty list if
     *         data is unavailable for the requested year
     */
    public List<LocalDate> getCompensatoryWorkingDays(int year) {
        List<LocalDate> dates = COMPENSATORY_WORKING_DAYS.get(year);
        if (dates == null) {
            LOGGER.warn("Compensatory working day data for {} is not available. " +
                    "Update HolidayCalendarServiceCNY with the State Council annual notice " +
                    "once published at http://www.gov.cn/",
                    year);
            return Collections.emptyList();
        }
        return dates;
    }

}
