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

package org.holiday.calendar;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertFalse;

/**
 * Repo-wide invariant test (issue #241): every national holiday calendar —
 * identified by a 2-character ISO 3166-1 alpha-2 code — must never carry
 * early-close (half-day) entries. Early closes are a market/exchange concept
 * only, exposed through market/currency/MIC codes of 3 or more characters.
 *
 * <p>The set of codes under test is derived from
 * {@link HolidayCalendarFactory#listAvailableCodes()} rather than a
 * hardcoded list, so it stays correct as new national calendars are added.
 *
 * <p>{@code SG} and {@code TR} are excluded pending the still-open sibling
 * fixes for those two codes: {@code SG} under APAC issue #232, {@code TR}
 * under MENA issue #233 (part of the same #229 national-vs-market audit that
 * produced this test). Remove them from {@link #KNOWN_CONFLATED_CODES} once
 * those issues land.
 */
public class NationalCalendarNoEarlyClosesIT {

    private static final Set<String> KNOWN_CONFLATED_CODES = Set.of("SG", "TR");

    private final HolidayCalendarFactory factory = new HolidayCalendarFactory();

    @DataProvider(name = "nationalCalendarCodes")
    public Iterator<Object[]> nationalCalendarCodes() {
        List<String> codes = factory.listAvailableCodes().stream()
                .filter(code -> code.length() == 2)
                .filter(code -> !KNOWN_CONFLATED_CODES.contains(code))
                .toList();
        return codes.stream().map(code -> new Object[]{code}).iterator();
    }

    @Test(dataProvider = "nationalCalendarCodes",
          description = "National calendars (2-character codes) must never report early closes")
    public void testNationalCalendarHasNoEarlyCloses(String code) {
        HolidayCalendar calendar = factory.create(code);
        assertFalse(calendar.hasEarlyCloses(),
                code + ": national calendar must not carry early-close (half-day) entries");
    }

}
