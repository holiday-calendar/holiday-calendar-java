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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Verifies that README.md version references stay in sync with the POM version
 * (GitHub issue #143).
 *
 * <p>The build uses maven-resources-plugin filtering to replace
 * {@code @project.version@} placeholders in {@code src/readme/README.md}
 * and write the result to {@code README.md} during the {@code validate} phase.
 *
 * <p><strong>Must be run via the full reactor</strong> ({@code mvn verify} from
 * the repository root) so that the root module's {@code validate} phase fires
 * before this test executes. Running {@code mvn verify -pl tests} in isolation
 * skips the root module and reads a potentially stale committed README.md.
 *
 * <p>Two system properties are injected by Surefire (see {@code tests/pom.xml}):
 * <ul>
 *   <li>{@code project.version} — the resolved Maven project version for this build.</li>
 *   <li>{@code project.root.basedir} — absolute path to the repository root.</li>
 * </ul>
 */
public class ReadmeVersionIT {

    private static final String VERSION_PLACEHOLDER = "@project.version@";

    private String projectVersion;
    private Path readmePath;
    private Path templatePath;

    @BeforeClass
    public void resolveArtifacts() {
        projectVersion = System.getProperty("project.version");
        assertNotNull(projectVersion,
                "System property 'project.version' must be set by maven-surefire-plugin");
        assertFalse(projectVersion.isBlank(),
                "System property 'project.version' must not be blank");

        String rootBasedir = System.getProperty("project.root.basedir");
        assertNotNull(rootBasedir,
                "System property 'project.root.basedir' must be set by maven-surefire-plugin");

        Path root = Paths.get(rootBasedir).toAbsolutePath().normalize();
        readmePath   = root.resolve("README.md");
        templatePath = root.resolve("src/readme/README.md");

        assertTrue(Files.exists(readmePath),
                "README.md not found at: " + readmePath +
                " — confirm maven-resources-plugin is configured in the root POM");
        assertTrue(Files.exists(templatePath),
                "Template not found at: " + templatePath +
                " — confirm src/readme/README.md exists in the repository");
    }

    @Test(description = "All <version> elements in README.md must match the current POM version")
    public void testReadmeVersionMatchesPomVersion() throws IOException {
        List<String> lines = Files.readAllLines(readmePath);

        List<String> versionLines = lines.stream()
                .filter(line -> {
                    String t = line.strip();
                    return t.startsWith("<version>") && t.endsWith("</version>");
                })
                .toList();

        assertFalse(versionLines.isEmpty(),
                "README.md contains no <version>…</version> lines — " +
                "has the Installation section been removed from the template?");

        String expected = "<version>" + projectVersion + "</version>";
        for (String versionLine : versionLines) {
            assertEquals(versionLine.strip(), expected,
                    "README.md version line does not match POM version.\n" +
                    "  Hint: run 'mvn validate -N' from the repository root and commit the updated README.md.");
        }
    }

    @Test(description = "README.md must not contain any un-expanded @project.version@ placeholder tokens")
    public void testReadmeContainsNoRawPlaceholders() throws IOException {
        List<String> lines = Files.readAllLines(readmePath);

        List<String> placeholderLines = lines.stream()
                .filter(line -> line.contains(VERSION_PLACEHOLDER))
                .toList();

        assertTrue(placeholderLines.isEmpty(),
                "README.md still contains unexpanded placeholder tokens — " +
                "maven-resources-plugin filtering did not replace them:\n  " +
                String.join("\n  ", placeholderLines));
    }

    @Test(description = "src/readme/README.md must contain exactly 3 @project.version@ placeholder tokens")
    public void testTemplateContainsVersionPlaceholders() throws IOException {
        List<String> templateLines = Files.readAllLines(templatePath);

        long placeholderCount = templateLines.stream()
                .filter(line -> line.contains(VERSION_PLACEHOLDER))
                .count();

        assertEquals(placeholderCount, 3L,
                "Expected exactly 3 occurrences of '" + VERSION_PLACEHOLDER +
                "' in " + templatePath.getFileName() +
                " (one per dependency block: holiday-calendar-core, holiday-calendar-western, holiday-calendar-apac), " +
                "but found " + placeholderCount + ".\n" +
                "  If you added a new dependency block, update this assertion.\n" +
                "  If placeholders were replaced with a hardcoded version, restore them.");
    }
}
