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

/**
 * Holiday Calendar APAC module.
 *
 * <p>Provides holiday calendar implementations and observances for Asia-Pacific
 * countries, including lunar, Islamic, and Hindu calendar-based holidays.
 * Supports Singapore SGX (SG), Singapore MAS MEPS+ (SGD), Tokyo Stock Exchange (JP),
 * Bank of Japan (JPY), People's Bank of China (CNY), and China national (CN).
 */
module org.holiday.calendar.apac {
    requires org.holiday.calendar.core;
    requires org.holiday.calendar.western;
    requires net.time4j.base;
    requires org.slf4j;

    exports org.holiday.calendar.observance.lunar;
    exports org.holiday.calendar.observance.islamic.apac;
    exports org.holiday.calendar.observance.hindu;
    exports org.holiday.calendar.observance.jp;

    provides org.holiday.calendar.HolidayCalendarService with
        org.holiday.calendar.impl.HolidayCalendarServiceCNY,
        org.holiday.calendar.impl.HolidayCalendarServiceSG,
        org.holiday.calendar.impl.HolidayCalendarServiceSGD,
        org.holiday.calendar.impl.HolidayCalendarServiceJP,
        org.holiday.calendar.impl.HolidayCalendarServiceJPY,
        org.holiday.calendar.impl.HolidayCalendarServiceCN;
}
