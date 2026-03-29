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

package org.holiday.calendar.observance.eu;

import org.holiday.calendar.observance.AbstractObservance;

import java.time.LocalDate;
import java.time.Month;

/**
 * Observance of Victory in Europe Day (V-E Day) - commemorates the formal
 * acceptance by the Allies of World War II of Nazi Germany's unconditional
 * surrender of its armed forces on 8 May 1945.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class VictoryInEuropeDay extends AbstractObservance {

    @Override
    protected LocalDate computeDate(int year) {
        return LocalDate.of(year, Month.MAY, 8);
    }

}
