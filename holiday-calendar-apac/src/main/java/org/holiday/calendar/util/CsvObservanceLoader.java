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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility for loading observance date tables from classpath CSV resources.
 *
 * <p>CSV format: {@code year,YYYY-MM-DD[,comment]}. Lines that are blank or
 * start with {@code #} (after stripping leading/trailing whitespace) are
 * ignored. Malformed data rows (unparseable year or date, fewer than two
 * fields) are logged at WARN level and skipped; valid rows in the same file
 * are still loaded.
 *
 * <p>Both methods accept a {@code Class<?>} anchor whose
 * {@link Class#getResourceAsStream} is used to locate the resource. This
 * follows JPMS resource-access semantics: pass the class that lives in the
 * same module package as the resource file.
 *
 * <p>This class is stateless and thread-safe.
 */
public final class CsvObservanceLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvObservanceLoader.class);

    private CsvObservanceLoader() {}

    /**
     * Loads a CSV where each year maps to exactly one date.
     *
     * <p>If the same year appears more than once, the last row wins
     * (standard {@link HashMap} put semantics).
     *
     * @param anchor            class used to resolve the classpath resource
     * @param classpathResource resource path, relative to {@code anchor}'s package
     *                          or absolute (starting with {@code /})
     * @return unmodifiable {@code Map<Integer, LocalDate>}, preserving CSV order
     * @throws IllegalStateException       if the resource is not found on the classpath
     * @throws ExceptionInInitializerError wrapping {@link IOException} on read failure
     */
    public static Map<Integer, LocalDate> loadSingle(Class<?> anchor, String classpathResource) {
        Map<Integer, LocalDate> result = new LinkedHashMap<>();
        try (InputStream is = openStream(anchor, classpathResource);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            int lineNumber = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.strip();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(",", 3);
                if (parts.length < 2) {
                    LOGGER.warn("Skipping malformed line {} in {}: '{}'", lineNumber, classpathResource, line);
                    continue;
                }
                parseSingleLine(parts, lineNumber, classpathResource, line, result);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load observance data from {}", classpathResource, e);
            throw new ExceptionInInitializerError(e);
        }
        return Collections.unmodifiableMap(result);
    }

    /**
     * Loads a CSV where each year may map to multiple dates (insertion order
     * preserved within each year's list).
     *
     * @param anchor            class used to resolve the classpath resource
     * @param classpathResource resource path, relative to {@code anchor}'s package
     *                          or absolute (starting with {@code /})
     * @return unmodifiable {@code Map<Integer, List<LocalDate>>} with unmodifiable
     *         value lists
     * @throws IllegalStateException       if the resource is not found on the classpath
     * @throws ExceptionInInitializerError wrapping {@link IOException} on read failure
     */
    public static Map<Integer, List<LocalDate>> loadMultiple(Class<?> anchor, String classpathResource) {
        Map<Integer, List<LocalDate>> result = new HashMap<>();
        try (InputStream is = openStream(anchor, classpathResource);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            int lineNumber = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.strip();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(",", 3);
                if (parts.length < 2) {
                    LOGGER.warn("Skipping malformed line {} in {}: '{}'", lineNumber, classpathResource, line);
                    continue;
                }
                parseMultipleLine(parts, lineNumber, classpathResource, line, result);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load observance data from {}", classpathResource, e);
            throw new ExceptionInInitializerError(e);
        }
        Map<Integer, List<LocalDate>> frozen = HashMap.newHashMap(result.size());
        result.forEach((year, dates) -> frozen.put(year, Collections.unmodifiableList(new ArrayList<>(dates))));
        return Collections.unmodifiableMap(frozen);
    }

    private static InputStream openStream(Class<?> anchor, String classpathResource) {
        InputStream is = anchor.getResourceAsStream(classpathResource);
        if (is == null) {
            throw new IllegalStateException("Required resource not found: " + classpathResource);
        }
        return is;
    }

    private static void parseSingleLine(String[] parts, int lineNumber, String resource, String line,
                                        Map<Integer, LocalDate> result) {
        try {
            int year = Integer.parseInt(parts[0].strip());
            LocalDate date = LocalDate.parse(parts[1].strip());
            result.put(year, date);
        } catch (NumberFormatException | DateTimeParseException e) {
            LOGGER.warn("Skipping malformed line {} in {}: '{}' — {}", lineNumber, resource, line, e.getMessage());
        }
    }

    private static void parseMultipleLine(String[] parts, int lineNumber, String resource, String line,
                                          Map<Integer, List<LocalDate>> result) {
        try {
            int year = Integer.parseInt(parts[0].strip());
            LocalDate date = LocalDate.parse(parts[1].strip());
            result.computeIfAbsent(year, k -> new ArrayList<>()).add(date);
        } catch (NumberFormatException | DateTimeParseException e) {
            LOGGER.warn("Skipping malformed line {} in {}: '{}' — {}", lineNumber, resource, line, e.getMessage());
        }
    }
}
