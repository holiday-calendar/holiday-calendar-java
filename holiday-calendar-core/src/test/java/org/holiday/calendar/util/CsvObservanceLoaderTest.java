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

package org.holiday.calendar.util;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class CsvObservanceLoaderTest {

    private static final String BASE = "";

    // --- loadSingle happy path ---

    @Test(groups = "csv.loader.happy")
    public void testLoadSingleReturnsCorrectSize() {
        Map<Integer, LocalDate> result = CsvObservanceLoader.loadSingle(
                CsvObservanceLoaderTest.class, BASE + "csv-single-happy.csv");
        assertEquals(result.size(), 3);
    }

    @Test(groups = "csv.loader.happy")
    public void testLoadSingleCorrectDateWithComment() {
        Map<Integer, LocalDate> result = CsvObservanceLoader.loadSingle(
                CsvObservanceLoaderTest.class, BASE + "csv-single-happy.csv");
        assertEquals(result.get(2020), LocalDate.of(2020, 1, 19));
    }

    @Test(groups = "csv.loader.happy")
    public void testLoadSingleCorrectDateWithoutComment() {
        Map<Integer, LocalDate> result = CsvObservanceLoader.loadSingle(
                CsvObservanceLoaderTest.class, BASE + "csv-single-happy.csv");
        assertEquals(result.get(2021), LocalDate.of(2021, 2, 12));
    }

    @Test(groups = "csv.loader.happy",
          expectedExceptions = UnsupportedOperationException.class)
    public void testLoadSingleMapIsUnmodifiable() {
        Map<Integer, LocalDate> result = CsvObservanceLoader.loadSingle(
                CsvObservanceLoaderTest.class, BASE + "csv-single-happy.csv");
        result.put(9999, LocalDate.of(9999, 1, 1));
    }

    // --- loadMultiple happy path ---

    @Test(groups = "csv.loader.happy")
    public void testLoadMultipleReturnsAllYears() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-multi-happy.csv");
        assertEquals(result.size(), 2);
    }

    @Test(groups = "csv.loader.happy")
    public void testLoadMultipleCorrectListSizePerYear() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-multi-happy.csv");
        assertEquals(result.get(2020).size(), 2);
        assertEquals(result.get(2021).size(), 2);
    }

    @Test(groups = "csv.loader.happy")
    public void testLoadMultipleContainsExpectedDates() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-multi-happy.csv");
        List<LocalDate> dates2020 = result.get(2020);
        assertTrue(dates2020.contains(LocalDate.of(2020, 1, 19)));
        assertTrue(dates2020.contains(LocalDate.of(2020, 2, 1)));
    }

    @Test(groups = "csv.loader.happy")
    public void testLoadMultipleYear2021TwoFieldRowIncluded() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-multi-happy.csv");
        assertTrue(result.get(2021).contains(LocalDate.of(2021, 2, 12)));
    }

    @Test(groups = "csv.loader.happy")
    public void testLoadMultiplePreservesInsertionOrderPerYear() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-multi-happy.csv");
        List<LocalDate> dates = result.get(2020);
        assertEquals(dates.get(0), LocalDate.of(2020, 1, 19));
        assertEquals(dates.get(1), LocalDate.of(2020, 2, 1));
    }

    @Test(groups = "csv.loader.happy",
          expectedExceptions = UnsupportedOperationException.class)
    public void testLoadMultipleMapIsUnmodifiable() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-multi-happy.csv");
        result.put(9999, List.of());
    }

    @Test(groups = "csv.loader.happy",
          expectedExceptions = UnsupportedOperationException.class)
    public void testLoadMultipleInnerListIsUnmodifiable() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-multi-happy.csv");
        result.get(2020).add(LocalDate.of(2020, 12, 25));
    }

    // --- comment and blank-line skipping ---

    @Test(groups = "csv.loader.happy")
    public void testLoadSingleSkipsCommentLines() {
        Map<Integer, LocalDate> result = CsvObservanceLoader.loadSingle(
                CsvObservanceLoaderTest.class, BASE + "csv-comments-blanks.csv");
        assertEquals(result.size(), 2);
    }

    @Test(groups = "csv.loader.happy")
    public void testLoadMultipleSkipsCommentLines() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-comments-blanks.csv");
        assertEquals(result.size(), 2);
    }

    @Test(groups = "csv.loader.happy")
    public void testLoadSingleSkipsBlankLines() {
        Map<Integer, LocalDate> result = CsvObservanceLoader.loadSingle(
                CsvObservanceLoaderTest.class, BASE + "csv-comments-blanks.csv");
        assertFalse(result.containsKey(0));
    }

    @Test(groups = "csv.loader.happy")
    public void testLoadMultipleSkipsBlankLines() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-comments-blanks.csv");
        assertFalse(result.containsKey(0));
    }

    // --- empty and comment-only files ---

    @Test(groups = "csv.loader.boundary")
    public void testLoadSingleEmptyFileReturnsEmptyMap() {
        Map<Integer, LocalDate> result = CsvObservanceLoader.loadSingle(
                CsvObservanceLoaderTest.class, BASE + "csv-empty.csv");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test(groups = "csv.loader.boundary")
    public void testLoadMultipleEmptyFileReturnsEmptyMap() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-empty.csv");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test(groups = "csv.loader.boundary")
    public void testLoadSingleOnlyCommentsReturnsEmptyMap() {
        Map<Integer, LocalDate> result = CsvObservanceLoader.loadSingle(
                CsvObservanceLoaderTest.class, BASE + "csv-only-comments.csv");
        assertTrue(result.isEmpty());
    }

    @Test(groups = "csv.loader.boundary")
    public void testLoadMultipleOnlyCommentsReturnsEmptyMap() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-only-comments.csv");
        assertTrue(result.isEmpty());
    }

    // --- resource-not-found ---

    @Test(groups = "csv.loader.error",
          expectedExceptions = IllegalStateException.class,
          expectedExceptionsMessageRegExp = ".*does-not-exist\\.csv.*")
    public void testLoadSingleResourceNotFoundThrowsIllegalStateException() {
        CsvObservanceLoader.loadSingle(
                CsvObservanceLoaderTest.class, BASE + "does-not-exist.csv");
    }

    @Test(groups = "csv.loader.error",
          expectedExceptions = IllegalStateException.class,
          expectedExceptionsMessageRegExp = ".*does-not-exist\\.csv.*")
    public void testLoadMultipleResourceNotFoundThrowsIllegalStateException() {
        CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "does-not-exist.csv");
    }

    // --- malformed rows: warn-and-skip contract ---

    @Test(groups = "csv.loader.error")
    public void testLoadMultipleMalformedYearSkippedValidRowsPresent() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-malformed-year.csv");
        assertEquals(result.size(), 2, "Valid rows must be loaded; malformed year row must be skipped");
        assertTrue(result.containsKey(2020));
        assertTrue(result.containsKey(2022));
    }

    @DataProvider(name = "singleMalformedFiles")
    public Object[][] singleMalformedFiles() {
        return new Object[][] {
            { "csv-malformed-year.csv" },
            { "csv-malformed-date.csv" },
            { "csv-missing-fields.csv" }
        };
    }

    @Test(groups = "csv.loader.error", dataProvider = "singleMalformedFiles")
    public void testLoadSingleMalformedRowSkippedValidRowsPresent(String csvFile) {
        Map<Integer, LocalDate> result = CsvObservanceLoader.loadSingle(
                CsvObservanceLoaderTest.class, BASE + csvFile);
        assertEquals(result.size(), 2);
        assertTrue(result.containsKey(2020));
        assertTrue(result.containsKey(2022));
    }

    @Test(groups = "csv.loader.error")
    public void testLoadMultipleMalformedDateSkippedValidRowsPresent() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-malformed-date.csv");
        assertEquals(result.size(), 2);
        assertTrue(result.containsKey(2020));
        assertTrue(result.containsKey(2022));
    }

    @Test(groups = "csv.loader.error")
    public void testLoadMultipleMissingFieldsSkippedValidRowsPresent() {
        Map<Integer, List<LocalDate>> result = CsvObservanceLoader.loadMultiple(
                CsvObservanceLoaderTest.class, BASE + "csv-missing-fields.csv");
        assertEquals(result.size(), 2);
        assertTrue(result.containsKey(2020));
        assertTrue(result.containsKey(2022));
    }

    // --- loadSingle duplicate year: last-writer-wins ---

    @Test(groups = "csv.loader.boundary")
    public void testLoadSingleDuplicateYearLastWriterWins() {
        Map<Integer, LocalDate> result = CsvObservanceLoader.loadSingle(
                CsvObservanceLoaderTest.class, BASE + "csv-duplicate-year.csv");
        assertEquals(result.size(), 1);
        assertEquals(result.get(2020), LocalDate.of(2020, 2, 1),
                "loadSingle must retain the last row for a duplicate year (last-writer-wins)");
    }
}
