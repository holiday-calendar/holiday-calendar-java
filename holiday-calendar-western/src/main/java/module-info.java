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
 * Holiday Calendar western module.
 *
 * <p>Provides holiday calendar implementations and observances for Western
 * countries: Australia, Canada, France, Germany, Switzerland, the United
 * Kingdom, and the United States. Also provides the corresponding
 * equities-market/exchange trading calendars — ASX (XASX), Xetra (XETR),
 * London Stock Exchange (XLON), NYSE (XNYS), Euronext Paris (XPAR), Toronto
 * Stock Exchange (XTSE), and SIX Swiss Exchange (XSWX) — each distinct from
 * its national-holiday counterpart: market calendars additionally include
 * market-convention closures (e.g. Good Friday on NYSE) and, where
 * applicable, {@code EARLY_CLOSE} half-day sessions such as NYSE's Christmas
 * Eve/July 3rd/day-after-Thanksgiving 13:00 ET closes and ASX's 14:10 Sydney
 * Christmas Eve/New Year's Eve closes.
 *
 * <p>This module's {@code HolidayCalendarService} providers are consumed via
 * {@link org.holiday.calendar.HolidayCalendarFactory} in the core module:
 * <pre>{@code
 * HolidayCalendar nyse = new HolidayCalendarFactory().create("XNYS");
 * List<HolidayDate> holidays2026 = nyse.calculate(2026);
 * }</pre>
 */
module org.holiday.calendar.western {
    requires org.holiday.calendar.core;

    exports org.holiday.calendar.observance.christian;
    exports org.holiday.calendar.observance.au;
    exports org.holiday.calendar.observance.ca;
    exports org.holiday.calendar.observance.de;
    exports org.holiday.calendar.observance.eu;
    exports org.holiday.calendar.observance.fr;
    exports org.holiday.calendar.observance.uk;
    exports org.holiday.calendar.observance.us;

    provides org.holiday.calendar.HolidayCalendarService with
        org.holiday.calendar.impl.HolidayCalendarServiceAU,
        org.holiday.calendar.impl.HolidayCalendarServiceAUD,
        org.holiday.calendar.impl.HolidayCalendarServiceCA,
        org.holiday.calendar.impl.HolidayCalendarServiceCAD,
        org.holiday.calendar.impl.HolidayCalendarServiceCH,
        org.holiday.calendar.impl.HolidayCalendarServiceCHF,
        org.holiday.calendar.impl.HolidayCalendarServiceDE,
        org.holiday.calendar.impl.HolidayCalendarServiceEUR,
        org.holiday.calendar.impl.HolidayCalendarServiceFR,
        org.holiday.calendar.impl.HolidayCalendarServiceGBP,
        org.holiday.calendar.impl.HolidayCalendarServiceUK,
        org.holiday.calendar.impl.HolidayCalendarServiceUS,
        org.holiday.calendar.impl.HolidayCalendarServiceUSD,
        org.holiday.calendar.impl.HolidayCalendarServiceXASX,
        org.holiday.calendar.impl.HolidayCalendarServiceXETR,
        org.holiday.calendar.impl.HolidayCalendarServiceXLON,
        org.holiday.calendar.impl.HolidayCalendarServiceXNYS,
        org.holiday.calendar.impl.HolidayCalendarServiceXPAR,
        org.holiday.calendar.impl.HolidayCalendarServiceXTSE,
        org.holiday.calendar.impl.HolidayCalendarServiceXSWX;
}
