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
 * Holiday Calendar - MENA regional calendar module.
 */
module org.holiday.calendar.mena {
    requires org.holiday.calendar.core;
    requires org.holiday.calendar.western;
    requires net.time4j.base;
    requires org.slf4j;

    exports org.holiday.calendar.observance.eg;
    exports org.holiday.calendar.observance.islamic.mena;
    exports org.holiday.calendar.observance.hebrew;
    exports org.holiday.calendar.observance.qa;

    provides org.holiday.calendar.HolidayCalendarService with
        org.holiday.calendar.impl.HolidayCalendarServiceAE,
        org.holiday.calendar.impl.HolidayCalendarServiceAED,
        org.holiday.calendar.impl.HolidayCalendarServiceSA,
        org.holiday.calendar.impl.HolidayCalendarServiceSAR,
        org.holiday.calendar.impl.HolidayCalendarServiceIL,
        org.holiday.calendar.impl.HolidayCalendarServiceILS,
        org.holiday.calendar.impl.HolidayCalendarServiceTR,
        org.holiday.calendar.impl.HolidayCalendarServiceTRY,
        org.holiday.calendar.impl.HolidayCalendarServiceQA,
        org.holiday.calendar.impl.HolidayCalendarServiceQAR,
        org.holiday.calendar.impl.HolidayCalendarServiceEG,
        org.holiday.calendar.impl.HolidayCalendarServiceEGP,
        org.holiday.calendar.impl.HolidayCalendarServiceKW,
        org.holiday.calendar.impl.HolidayCalendarServiceKWD,
        org.holiday.calendar.impl.HolidayCalendarServiceBH,
        org.holiday.calendar.impl.HolidayCalendarServiceBHD,
        org.holiday.calendar.impl.HolidayCalendarServiceMA,
        org.holiday.calendar.impl.HolidayCalendarServiceMAD;
}
