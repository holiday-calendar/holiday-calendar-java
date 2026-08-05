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

/**
 * Service for provision of Israel TASE / Bank of Israel settlement holiday calendar.
 *
 * <p>Calendar code: {@code ILS}. Covers settlement closure days for the Tel Aviv
 * Stock Exchange (TASE) and the Bank of Israel. The settlement holiday schedule
 * mirrors the Israel national calendar ({@code IL}).
 *
 * <p>Roll strategy: {@link DateRolls#noRoll()} — settlement requires both
 * counterparties to be available on the same calendar date; there is no
 * roll-forward convention. All holidays are {@code rollable(false)}.
 *
 * <p><strong>Weekend: {@code Friday+Saturday} confirmed still correct for
 * settlement purposes; see #321 for the investigation.</strong> TASE shifted
 * its <em>trading</em> week from Sunday–Thursday to Monday–Friday effective
 * January 5, 2026 (MSCI and Solactive index-provider announcements). This did
 * <em>not</em> change TASE's settlement/clearing calendar: TASE's own
 * Clearing House By-Laws (Part One — General, "Update of March 03, 2026")
 * define {@code "business day"}/{@code "clearing day"} as "a day on which the
 * Bank of Israel performs monetary activity," deliberately distinct from
 * {@code "trading day"} ("a day on which trading takes place on TASE") — the
 * By-Laws' own "day of receipt" clause explicitly contemplates a business day
 * that is not a trading day. TASE settlement therefore remains anchored to
 * the Bank of Israel's business week, which is unchanged: Sunday–Friday, per
 * the Bank of Israel's own 2026 ZAHAV RTGS and Markets Department schedules
 * (read directly), with Saturday as the standing closure day.
 * <ul>
 *   <li>Because all holidays here are {@code rollable(false)} and the roll
 *       strategy is {@link DateRolls#noRoll()}, {@code weekendDays} has no
 *       effect on {@link HolidayCalendar#calculate(int)} for this calendar —
 *       its only live effect is the public {@code isWeekend()}/
 *       {@code isWeekendUTC()} query methods.</li>
 *   <li>One genuine, narrower nuance remains: the Bank of Israel's own
 *       schedules show Friday as a <em>short</em> business day (13:15–14:00
 *       close, depending on department), not a full closure. Per this class's
 *       established convention — see the Hoshana Raba handling below, where a
 *       TASE-vs-BOI difference in hours is modeled as an {@code EARLY_CLOSE}
 *       holiday rather than a change to {@code weekendDays} — this argues at
 *       most for a possible future {@code EARLY_CLOSE} treatment of Fridays,
 *       not for a change to {@code weekendDays} itself.</li>
 * </ul>
 *
 * <p><strong>Hoshana Raba (21 Tishri) — modeled as an early close:</strong>
 * TASE closes fully on Hoshana Raba (the 7th day of Sukkot, 21 Tishri) consistently;
 * this was confirmed in official TASE vacation schedules for 2022–2025. However,
 * the Bank of Israel operates with reduced hours on that day (until approximately
 * 13:15 IST) rather than a full settlement closure, per official Bank of Israel
 * Markets Department schedules. Since {@code ILS} represents shekel settlement
 * availability (Bank of Israel), Hoshana Raba is included as an {@code EARLY_CLOSE}
 * holiday closing at 13:15 Asia/Jerusalem time — distinct from the 13:00 close
 * used for the five TASE holiday-eve early closes. Consumers building TASE-only
 * trading calendars, where the day is a full closure, should treat this date
 * accordingly rather than relying on the 13:15 close time.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayCalendarServiceILS extends AbstractHolidayCalendarService {

    private static final String CODE = "ILS";
    private static final String NAME = "Israel (TASE/Bank of Israel) Holidays";

    public HolidayCalendarServiceILS() {
        super(CODE, NAME);
    }

    @Override
    public HolidayCalendar getHolidayCalendar() {
        return HolidayCalendar.builder()
                .code(CODE)
                .name(NAME)
                .dateRoll(DateRolls.noRoll())
                .weekendDays(IsraelHolidays.ISRAEL_WEEKEND)
                .holidays(IsraelHolidays.baseHolidays())
                .holidays(IsraelHolidays.earlyCloseHolidays())
                .build();
    }

}