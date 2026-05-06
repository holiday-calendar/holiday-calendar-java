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

import java.time.Month;
import java.util.OptionalInt;

/**
 * Service for provision of China national public holidays.
 *
 * <p>The calendar code is {@code CN}, reflecting the seven statutory public
 * holidays as defined by China's State Council Ordinance on Public Holidays
 * for National Festivals and Memorial Days (as amended in 2007 and 2013). It
 * covers the minimum statutory closure window for each:
 *
 * <ul>
 *   <li>New Year's Day — 1 January</li>
 *   <li>Spring Festival (Chinese New Year) — Days 1–3 of the 1st lunar month</li>
 *   <li>Qingming Festival — solar term at ecliptic longitude 15° (~4–5 April)</li>
 *   <li>Labour Day — 1 May</li>
 *   <li>Dragon Boat Festival — 5th day of the 5th lunar month</li>
 *   <li>Mid-Autumn Festival — 15th day of the 8th lunar month</li>
 *   <li>National Day Golden Week — 1–3 October</li>
 * </ul>
 *
 * <h2>Distinction from CNY</h2>
 *
 * <p>This calendar ({@code CN}) models the statutory minimum windows as enacted
 * in the Ordinance. It differs from {@link HolidayCalendarServiceCNY} ({@code CNY},
 * the PBOC/CNAPS operational calendar) in three ways:
 *
 * <ol>
 *   <li><strong>Window length</strong> — Spring Festival is 3 days here (Days 1–3)
 *       versus 7 days in CNY; National Day is 3 days here versus 7 days in CNY.</li>
 *   <li><strong>Labour Day</strong> — 1 day here (the statutory minimum) versus 3
 *       days in CNY. The 5-day extended Labour Day window observed in practice since
 *       2019 arises from the State Council's annual adjustment notice, not the
 *       Ordinance itself.</li>
 *   <li><strong>Roll strategy</strong> — {@link DateRolls#followingMonday()} applied
 *       to single-day holidays; multi-day blocks use {@code rollable(false)}.</li>
 * </ol>
 *
 * <h2>Roll Strategy</h2>
 *
 * <p>China's weekend adjustment mechanism (调休, <em>tiaoxiu</em>) is block-based,
 * not per-day. When Spring Festival or National Day falls on a weekend, the State
 * Council shifts the <em>entire block</em> into a contiguous window and designates
 * nearby Saturdays or Sundays as compensatory working days (补班) via its annual
 * holiday notice. No individual day within the block is independently moved to the
 * following Monday.
 *
 * <p>This library's per-holiday {@link org.holiday.calendar.function.DateRoll} model
 * cannot replicate that block-shifting mechanism. Applying {@code followingMonday()}
 * to each day of a multi-day block independently would produce definitively wrong
 * results (e.g., all three Spring Festival days collapsing to the same date when
 * Day 1 falls on a Saturday). Therefore:
 *
 * <ul>
 *   <li><strong>Single-day holidays</strong> (New Year's Day, Qingming Festival,
 *       Labour Day, Dragon Boat Festival, Mid-Autumn Festival) are {@code rollable(true)}
 *       — {@code followingMonday()} is a sound approximation of the Ordinance's
 *       per-day substitution rule (Article 3).</li>
 *   <li><strong>Multi-day blocks</strong> (Spring Festival Days 1–3, National Day
 *       Days 1–3) are {@code rollable(false)} — dates are reported on their natural
 *       calendar positions. For precise year-specific adjustments, consult the
 *       State Council's annual holiday notice (gov.cn); the {@code CNY} calendar's
 *       {@code getCompensatoryWorkingDays(int)} method provides curated data.</li>
 * </ul>
 *
 * <h2>Data Validity</h2>
 *
 * <p>All public holidays are computed algorithmically via Time4J Chinese calendar
 * mathematics. {@link HolidayCalendar#calculate(int)} is authoritative for any
 * year with no upper bound; {@link #dataValidThrough()} returns
 * {@link OptionalInt#empty()} accordingly.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceCN extends AbstractHolidayCalendarService {

    private static final String CODE = "CN";
    private static final String NAME = "China National Holidays";
    private static final String NATIONAL_DAY_CONTINUED = "National Day Golden Week (continued)";

    public HolidayCalendarServiceCN() {
        super(CODE, NAME);
    }

    /**
     * Returns {@link OptionalInt#empty()} because all CN public holidays are
     * computed algorithmically (Time4J Chinese calendar math for floating
     * holidays; fixed calendar dates for the rest).
     * {@link HolidayCalendar#calculate(int)} is authoritative for any year
     * with no upper bound.
     */
    @Override
    public OptionalInt dataValidThrough() {
        return OptionalInt.empty();
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final Holiday newYearsDay = Holiday.builder()
                .name("New Year's Day")
                .description("First day of new year in the Common Era (CE)")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.JANUARY, 1)
                .build();

        // Spring Festival (Chinese New Year) — 3-day statutory window.
        // rollable=false: China's block-shift adjustment cannot be modelled
        // per-day; dates are reported on their natural lunar calendar positions.
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

        final Holiday qingmingFestival = Holiday.builder()
                .name("Qingming Festival")
                .description("Tomb Sweeping Day; solar term at ecliptic longitude 15°")
                .type(Holiday.Type.FLOATING)
                .rollable(true)
                .observance(new QingmingFestival())
                .build();

        final Holiday labourDay = Holiday.builder()
                .name("Labour Day")
                .description("International Workers' Day")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.MAY, 1)
                .build();

        final Holiday dragonBoatFestival = Holiday.builder()
                .name("Dragon Boat Festival")
                .description("Duanwu; 5th day of the 5th lunar month")
                .type(Holiday.Type.FLOATING)
                .rollable(true)
                .observance(new DragonBoatFestival())
                .build();

        final Holiday midAutumnFestival = Holiday.builder()
                .name("Mid-Autumn Festival")
                .description("15th day of the 8th lunar month; full moon of autumn")
                .type(Holiday.Type.FLOATING)
                .rollable(true)
                .observance(new MidAutumnFestival())
                .build();

        // National Day Golden Week — 3-day statutory window (1–3 October).
        // rollable=false: same rationale as Spring Festival above.
        final Holiday nationalDay1 = Holiday.builder()
                .name("National Day (Day 1)")
                .description("Founding of the People's Republic of China, 1 October 1949")
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.OCTOBER, 1)
                .build();
        final Holiday nationalDay2 = Holiday.builder()
                .name("National Day (Day 2)")
                .description(NATIONAL_DAY_CONTINUED)
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.OCTOBER, 2)
                .build();
        final Holiday nationalDay3 = Holiday.builder()
                .name("National Day (Day 3)")
                .description(NATIONAL_DAY_CONTINUED)
                .type(Holiday.Type.FIXED)
                .rollable(false)
                .monthDay(Month.OCTOBER, 3)
                .build();

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.followingMonday())
                .weekendDays(HolidayCalendar.STANDARD_WEEKEND)
                .holiday(newYearsDay)
                .holiday(springFestivalDay1)
                .holiday(springFestivalDay2)
                .holiday(springFestivalDay3)
                .holiday(qingmingFestival)
                .holiday(labourDay)
                .holiday(dragonBoatFestival)
                .holiday(midAutumnFestival)
                .holiday(nationalDay1)
                .holiday(nationalDay2)
                .holiday(nationalDay3)
                .build();
    }

}
