/*******************************************************************************
 * Holiday Calendar - A library for definition and calculation of holiday calendars
 * Copyright (C) 2021 David Joyce
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

package com.github.davejoyce.calendar.observance.christian;

import com.github.davejoyce.calendar.observance.CompositeObservance;

import java.time.LocalDate;

/**
 * Observance of Whit Monday, the Monday following
 * {@link WhitSunday Whit Sunday}. Whit Monday is exactly 50 days after Easter
 * Sunday.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class WhitMonday extends CompositeObservance {

    public WhitMonday(EasterObservance easterObservance) {
        super(easterObservance);
    }

    @Override
    protected LocalDate computeDate(int year) {
        return base.apply(year).plusDays(50);
    }

}
