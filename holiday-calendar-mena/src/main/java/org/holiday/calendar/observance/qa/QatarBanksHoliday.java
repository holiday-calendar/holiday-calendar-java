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

package org.holiday.calendar.observance.qa;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

/**
 * Observance of the Qatar Banks Holiday — the first Sunday of March.
 *
 * <p>Mandated by Cabinet Decision No. (33) of 2009 for all banks, money exchange
 * outlets, investment and financial companies, insurance firms, and insurance
 * broker companies operating in the State of Qatar. Sunday is a working day in
 * Qatar (Friday + Saturday weekend), so this closure falls on the first business
 * day of March in most years.
 *
 * <p>This holiday applies to the {@code QAR} (QSE/QCB) settlement calendar only;
 * it is not a gazetted national public holiday and is not included in {@code QA}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class QatarBanksHoliday extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        return LocalDate.of(year, Month.MARCH, 1)
                        .with(TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.SUNDAY));
    }

    @Override
    protected boolean isValidYear(int year) {
        return year >= 2009;
    }

}
