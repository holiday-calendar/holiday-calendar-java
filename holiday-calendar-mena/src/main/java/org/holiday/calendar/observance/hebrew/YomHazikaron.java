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

package org.holiday.calendar.observance.hebrew;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.LocalDate;

/**
 * Observance of Yom Hazikaron — Israeli Memorial Day (4 Iyar).
 *
 * <p>Yom Hazikaron always falls on the day immediately before the observed
 * Yom Ha'atzmaut. The two days are coupled by the Memorial Day and Independence
 * Day Law (1963): when Independence Day shifts to avoid Shabbat, Memorial Day
 * shifts with it by exactly one day. This observance derives the date from
 * {@link IndependenceDay} rather than replicating the shift logic.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class YomHazikaron extends AbstractObservance {

    private final IndependenceDay independenceDay = new IndependenceDay();

    @Override
    protected LocalDate computeDate(int year) {
        return independenceDay.apply(year).minusDays(1);
    }

}
