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
import org.holiday.calendar.observance.ca.BoxingDayCAD;
import org.holiday.calendar.observance.ca.CivicHoliday;
import org.holiday.calendar.observance.ca.FamilyDayCAD;
import org.holiday.calendar.observance.ca.LabourDay;
import org.holiday.calendar.observance.ca.NationalDayForTruthAndReconciliation;
import org.holiday.calendar.observance.ca.Thanksgiving;
import org.holiday.calendar.observance.ca.VictoriaDay;
import org.holiday.calendar.observance.christian.EasterObservance;
import org.holiday.calendar.observance.christian.GoodFriday;
import org.holiday.calendar.observance.christian.WesternEaster;

import java.time.DayOfWeek;
import java.time.Month;

import static org.holiday.calendar.HolidayCalendar.STANDARD_WEEKEND;

/**
 * Service for provision of the Bank of Canada (Lynx) holiday calendar.
 *
 * <p>The Lynx Real-Time Gross Settlement (RTGS) system is the authoritative
 * settlement calendar for CAD high-value payments in Canada. This calendar
 * models the statutory holiday schedule under the <em>Bank Act</em> (Canada)
 * that governs Lynx settlement days.</p>
 *
 * <p>This calendar is distinct from the general {@code CA} (Canada National
 * Holidays) calendar in the following respects:</p>
 * <ul>
 *   <li>Easter Monday is <strong>not</strong> included — it is not a Bank Act
 *       statutory holiday.</li>
 *   <li>Family Day is gated from 2013, reflecting its federal (Bank Act)
 *       adoption date, not the earlier provincial origin.</li>
 *   <li>National Day for Truth and Reconciliation is included from 2021 and
 *       is a rollable holiday (following-Monday substitution).</li>
 *   <li>All fixed holidays roll to the <strong>following Monday</strong> when
 *       they fall on either Saturday or Sunday.</li>
 *   <li>Boxing Day is computed as a floating observance to correctly handle
 *       date collisions when Christmas Day consumes 26 December as its own
 *       observed substitute.</li>
 * </ul>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceCAD extends AbstractHolidayCalendarService {

    private static final String CODE = "CAD";
    private static final String NAME = "Bank of Canada (Lynx) Holiday Schedule";

    public HolidayCalendarServiceCAD() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        final EasterObservance easter = new WesternEaster();

        final Holiday newYearsDay = Holiday.builder()
                .name("New Year's Day")
                .description("First day of new year in the Common Era (CE)")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.JANUARY, 1)
                .build();
        final Holiday familyDay = Holiday.builder()
                .name("Family Day")
                .description("Federal statutory Family Day (Bank Act)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new FamilyDayCAD())
                .build();
        final Holiday goodFriday = Holiday.builder()
                .name("Good Friday")
                .description("Friday before Easter Sunday")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new GoodFriday(easter))
                .build();
        final Holiday victoriaDay = Holiday.builder()
                .name("Victoria Day")
                .description("Official celebration of birthday of Canada's Sovereign")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new VictoriaDay())
                .build();
        final Holiday canadaDay = Holiday.builder()
                .name("Canada Day")
                .description("Anniversary of Canadian Confederation")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.JULY, 1)
                .build();
        final Holiday civicHoliday = Holiday.builder()
                .name("Civic Holiday")
                .description("Civic Holiday (first Monday in August)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new CivicHoliday())
                .build();
        final Holiday labourDay = Holiday.builder()
                .name("Labour Day")
                .description("Celebration of workers in Canada")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new LabourDay())
                .build();
        final Holiday ndtr = Holiday.builder()
                .name("National Day For Truth and Reconciliation")
                .description("Recognition of the legacy of the Canadian Indian residential school system")
                .type(Holiday.Type.FLOATING)
                .rollable(true)
                .observance(new NationalDayForTruthAndReconciliation())
                .build();
        final Holiday thanksgiving = Holiday.builder()
                .name("Thanksgiving Day")
                .description("National day for giving thanks")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new Thanksgiving())
                .build();
        final Holiday remembranceDay = Holiday.builder()
                .name("Remembrance Day")
                .description("Commemoration of armed forces members who have died in the line of duty")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.NOVEMBER, 11)
                .build();
        final Holiday christmas = Holiday.builder()
                .name("Christmas Day")
                .description("Christmas Day")
                .type(Holiday.Type.FIXED)
                .rollable(true)
                .monthDay(Month.DECEMBER, 25)
                .build();
        final Holiday boxingDay = Holiday.builder()
                .name("Boxing Day")
                .description("Day after Christmas (observed)")
                .type(Holiday.Type.FLOATING)
                .rollable(false)
                .observance(new BoxingDayCAD())
                .build();

        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(dateToRoll -> {
                    final DayOfWeek dow = dateToRoll.getDayOfWeek();
                    if (dow == DayOfWeek.SATURDAY) return dateToRoll.plusDays(2L);
                    if (dow == DayOfWeek.SUNDAY)   return dateToRoll.plusDays(1L);
                    return dateToRoll;
                })
                .weekendDays(STANDARD_WEEKEND)
                .holiday(newYearsDay)
                .holiday(familyDay)
                .holiday(goodFriday)
                .holiday(victoriaDay)
                .holiday(canadaDay)
                .holiday(civicHoliday)
                .holiday(labourDay)
                .holiday(ndtr)
                .holiday(thanksgiving)
                .holiday(remembranceDay)
                .holiday(christmas)
                .holiday(boxingDay)
                .build();
    }

}