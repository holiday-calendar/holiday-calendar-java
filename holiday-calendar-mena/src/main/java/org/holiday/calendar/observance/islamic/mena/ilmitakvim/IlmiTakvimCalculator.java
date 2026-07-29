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

package org.holiday.calendar.observance.islamic.mena.ilmitakvim;

import net.time4j.Moment;
import net.time4j.PlainDate;
import net.time4j.calendar.astro.MoonPhase;
import net.time4j.calendar.astro.SolarTime;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Computes Turkish Diyanet ilmi takvim (scientific calendar) dates for Eid al-Fitr
 * (1 Shawwal AH) and Eid al-Adha (10 Dhu al-Hijjah AH), for years beyond Diyanet's
 * own published range.
 *
 * <p><strong>Methodology</strong>: a Hijri month begins the day after the true
 * (perturbed, not mean-motion) astronomical lunar conjunction, computed via
 * {@link MoonPhase#NEW_MOON} (Meeus Ch. 49) — unless the conjunction occurs after
 * local sunset at Ankara (39.93°N, 32.85°E, via {@link SolarTime}), in which case
 * the month begins two days after the conjunction's civil date
 * ({@code Europe/Istanbul}, fixed UTC+3 since Turkey's 2016 DST abolition). Each
 * Hijri year always advances by exactly 12 lunations (no intercalation), so a
 * target year's relevant month start is located by walking forward one Hijri
 * year at a time from a verified anchor date and re-snapping to the true
 * conjunction at each step, rather than by independently computing each year.</p>
 *
 * <p>This rule was calibrated against, and exactly reproduces, all 24 Diyanet
 * officially-published dates (Eid al-Fitr and Eid al-Adha, 2024–2035) fetched
 * directly from {@code vakithesaplama.diyanet.gov.tr} — see
 * {@code IlmiTakvimCalculatorTest} for the full verification dataset. It is used
 * here only for years beyond Diyanet's own published range (2036 onward, as of
 * this writing); for years Diyanet has already published, the CSV lookup tables
 * in {@code eid-al-fitr-tr.csv}/{@code eid-al-adha-tr.csv} are the source of
 * truth and take precedence.</p>
 *
 * <p><strong>Caveat</strong>: this remains a best-effort approximation, not a
 * guarantee of Diyanet's future published dates — Diyanet's actual process may
 * involve committee judgment beyond a pure astronomical rule. Projected dates
 * should be verified against Diyanet's own publications as they become
 * available (Diyanet has historically published several years ahead of the
 * current year).</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class IlmiTakvimCalculator {

    static final double ANKARA_LATITUDE_DEG = 39.93;
    static final double ANKARA_LONGITUDE_DEG = 32.85;

    private static final ZoneId ANKARA_ZONE = ZoneId.of("Europe/Istanbul");
    private static final SolarTime ANKARA_SOLAR_TIME = SolarTime.ofLocation(ANKARA_LATITUDE_DEG, ANKARA_LONGITUDE_DEG);
    private static final double MEAN_SYNODIC_MONTH_DAYS = 29.530588861;
    private static final int LUNATIONS_PER_HIJRI_YEAR = 12;

    /** Days after 1 Dhu al-Hijjah that Eid al-Adha (10 Dhu al-Hijjah) falls on. */
    private static final int ADHA_DAY_OFFSET = 9;

    /**
     * Verified anchor: 1 Shawwal 1456 AH, per Diyanet's own published "Dini
     * Günler" table (confirmed 2035-12-01).
     */
    private static final LocalDate FITR_MONTH_START_ANCHOR = LocalDate.of(2035, 12, 1);

    /**
     * Verified anchor: 1 Dhu al-Hijjah 1456 AH, derived from Diyanet's published
     * Eid al-Adha 2035 date (2035-02-18) minus {@value ADHA_DAY_OFFSET} days.
     */
    private static final LocalDate ADHA_MONTH_START_ANCHOR = LocalDate.of(2035, 2, 9);

    private IlmiTakvimCalculator() {}

    /**
     * @param gregorianYear the Gregorian year in which Eid al-Fitr's first day falls
     * @return the computed Gregorian date of 1 Shawwal AH (Eid al-Fitr, 1st day)
     */
    public static LocalDate eidAlFitr(int gregorianYear) {
        return monthStartForYear(FITR_MONTH_START_ANCHOR, gregorianYear, 0);
    }

    /**
     * @param gregorianYear the Gregorian year in which Eid al-Adha's first day falls
     * @return the computed Gregorian date of 10 Dhu al-Hijjah AH (Eid al-Adha, 1st day)
     */
    public static LocalDate eidAlAdha(int gregorianYear) {
        return monthStartForYear(ADHA_MONTH_START_ANCHOR, gregorianYear, ADHA_DAY_OFFSET)
                .plusDays(ADHA_DAY_OFFSET);
    }

    /**
     * Walks one Hijri year (12 lunations) at a time from the anchor month start,
     * re-snapping to the true conjunction at each step, until the bucketed
     * occurrence (month start + {@code dayOffsetForBucketing}) falls in
     * {@code targetGregorianYear}. Walks forward for years after the anchor's own
     * year, backward for years before it. When a Gregorian year contains two
     * occurrences of the same Hijri anniversary, the earlier (chronologically
     * first) one is returned regardless of walk direction, matching the
     * convention already documented on {@code EidAlFitr}/{@code EidAlAdha} for
     * CSV-sourced years.
     */
    private static LocalDate monthStartForYear(LocalDate anchorMonthStart, int targetGregorianYear, int dayOffsetForBucketing) {
        int anchorBucketYear = anchorMonthStart.plusDays(dayOffsetForBucketing).getYear();
        long approxLunarYearDays = Math.round(MEAN_SYNODIC_MONTH_DAYS * LUNATIONS_PER_HIJRI_YEAR);
        boolean forward = targetGregorianYear >= anchorBucketYear;

        LocalDate monthStart = anchorMonthStart;
        LocalDate earliestMatch = null;
        while (true) {
            int bucketYear = monthStart.plusDays(dayOffsetForBucketing).getYear();
            if (bucketYear == targetGregorianYear) {
                if (earliestMatch == null || monthStart.isBefore(earliestMatch)) {
                    earliestMatch = monthStart;
                }
            } else if (earliestMatch != null) {
                // Walked past the target year's occurrence(s); the earliest one recorded wins.
                return earliestMatch;
            } else if ((forward && bucketYear > targetGregorianYear) || (!forward && bucketYear < targetGregorianYear)) {
                throw new IllegalArgumentException(
                        "No ilmi takvim occurrence found for year " + targetGregorianYear
                                + " (anchor " + anchorMonthStart + " does not reach it)");
            }
            LocalDate approxNext = forward
                    ? monthStart.plusDays(approxLunarYearDays)
                    : monthStart.minusDays(approxLunarYearDays);
            monthStart = nextConjunctionMonthStart(approxNext);
        }
    }

    /**
     * Finds the true conjunction nearest (on or after 3 days before)
     * {@code approxTarget}, then applies the Ankara-sunset visibility rule to
     * determine the resulting Hijri month's start date.
     */
    private static LocalDate nextConjunctionMonthStart(LocalDate approxTarget) {
        Instant seed = approxTarget.minusDays(3).atStartOfDay(ANKARA_ZONE).toInstant();
        Moment conjunction = MoonPhase.NEW_MOON.after(Moment.from(seed));
        LocalDate conjunctionCivilDate = toAnkaraCivilDate(conjunction);
        Moment sunset = sunsetAt(conjunctionCivilDate);
        return conjunction.isBefore(sunset) ? conjunctionCivilDate.plusDays(1) : conjunctionCivilDate.plusDays(2);
    }

    private static LocalDate toAnkaraCivilDate(Moment moment) {
        long epochMillis = moment.getPosixTime() * 1000L + moment.getNanosecond() / 1_000_000L;
        return Instant.ofEpochMilli(epochMillis).atZone(ANKARA_ZONE).toLocalDate();
    }

    private static Moment sunsetAt(LocalDate date) {
        PlainDate plainDate = PlainDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        return plainDate.get(ANKARA_SOLAR_TIME.sunset())
                .orElseThrow(() -> new IllegalStateException("No sunset computed for Ankara on " + date));
    }

}
