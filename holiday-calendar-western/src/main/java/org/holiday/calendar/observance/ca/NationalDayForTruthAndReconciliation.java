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

package org.holiday.calendar.observance.ca;

import org.holiday.calendar.function.Observance;

import java.time.LocalDate;
import java.time.Month;

/**
 * Observance of the National Day for Truth and Reconciliation - a federal
 * statutory holiday on 30 September, honouring the survivors and victims of
 * Canada's Indian residential school system and acknowledging the ongoing
 * impacts on Indigenous communities.
 *
 * <p>This holiday was established by the federal government and first observed
 * on <strong>30 September 2021</strong>. It is a rollable holiday: when 30
 * September falls on a Saturday or Sunday, it is observed the following Monday
 * under Canada's federal following-Monday substitution rule.</p>
 *
 * <p>This class is implemented as a {@link Observance} rather than a fixed
 * {@code MonthDay} in order to enforce the year constraint via
 * {@link #test(Integer)}, since {@code FixedHoliday} does not support year
 * gating.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class NationalDayForTruthAndReconciliation implements Observance {

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        return LocalDate.of(year, Month.SEPTEMBER, 30);
    }

    @Override
    public boolean test(Integer year) {
        return year != null && year >= 2021;
    }

}