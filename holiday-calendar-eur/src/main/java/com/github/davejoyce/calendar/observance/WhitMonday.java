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

package com.github.davejoyce.calendar.observance;

import com.github.davejoyce.calendar.function.EasterObservance;
import com.github.davejoyce.calendar.function.Observance;

import java.time.LocalDate;

/**
 * Observance of Whit Monday, the Monday following
 * {@link WhitSunday Whit Sunday}. Whit Monday is exactly 50 days after Easter
 * Sunday.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class WhitMonday implements Observance {

    private final WhitSunday whitSunday;

    public WhitMonday(EasterObservance easterObservance) {
        this.whitSunday = new WhitSunday(easterObservance);
    }

    @Override
    public LocalDate apply(Integer year) {
        if (!test(year)) return null;
        return whitSunday.apply(year).plusDays(1);
    }

    @Override
    public boolean test(Integer year) {
        return whitSunday.test(year);
    }

}
