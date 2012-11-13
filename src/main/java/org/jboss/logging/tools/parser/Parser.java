/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.logging.tools.parser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser for log files.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class Parser {

    /**
     * The regular expression for format strings.  Ain't regex grand?
     */
    private static final Pattern FORMAT_PATTERN = Pattern.compile(
            // greedily match all non-format characters
            "([^%]++)" +
                    // match a format string...
                    "|(?:%" +
                    // optional minimum width plus justify flag
                    "(?:(-)?(\\d+))?" +
                    // optional maximum width
                    "(?:\\.(\\d+))?" +
                    // the actual format character
                    "(.)" +
                    // an optional argument string
                    "(?:\\{([^}]*)\\})?" +
                    // end format string
                    ")"
    );

    private final List<FormatPart> parts;
    private final Pattern pattern;
    private final List<Filter> filters = new ArrayList<Filter>();
    private final boolean useNewLine;
    private int maxResults;

    private Parser(final int maxResults, final String pattern) {
        this.maxResults = maxResults;
        parts = new ArrayList<FormatPart>();
        final StringBuilder stringPattern = new StringBuilder();
        final Matcher formatMatcher = FORMAT_PATTERN.matcher(pattern);
        boolean useNewLine = false;
        while (formatMatcher.find()) {
            final String otherText = formatMatcher.group(1);
            if (otherText != null) {
                stringPattern.append(sanitize(otherText));
            } else {
                final String hyphen = formatMatcher.group(2);
                final String minWidthString = formatMatcher.group(3);
                final String maxWidthString = formatMatcher.group(4);
                final String formatCharString = formatMatcher.group(5);
                final String argument = formatMatcher.group(6);
                final int minimumWidth = minWidthString == null ? 0 : Integer.parseInt(minWidthString);
                final boolean leftJustify = hyphen != null;
                final int maximumWidth = maxWidthString == null ? 0 : Integer.parseInt(maxWidthString);
                final char formatChar = formatCharString.charAt(0);
                final FormatType formatType = FormatType.fromChar(formatChar);
                if (formatType == FormatType.NEW_LINE) useNewLine = true;
                parts.add(new FormatPart(formatType, argument, leftJustify, minimumWidth, maximumWidth));
                stringPattern.append(formatType.toPattern());
            }
        }
        this.useNewLine = useNewLine;
        System.out.println(stringPattern);
        this.pattern = Pattern.compile(stringPattern.toString());
    }

    /**
     * Creates a new parser for the format pattern.
     *
     * @param formatPattern the pattern the log output was written with
     *
     * @return a new parser
     */
    public static Parser of(final String formatPattern) {
        return of(0, formatPattern);
    }

    /**
     * Creates a new parser for the format pattern.
     *
     * @param maxResults    the maximum number of results returned by the parser, 0 for all results
     * @param formatPattern the pattern the log output was written with
     *
     * @return a new parser
     */
    public static Parser of(final int maxResults, final String formatPattern) {
        return new Parser(maxResults, formatPattern);
    }

    /**
     * Parses the output stream and returns a collection of results.
     *
     * @param resourceStream the log output stream
     *
     * @return a collection of log results
     *
     * @throws IOException if an error occurs reading the stream
     */
    public Collection<LogResult> parse(final InputStream resourceStream) throws IOException {
        final List<LogResult> results = new ArrayList<LogResult>();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));
        try {
            int lineCounter = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                final Matcher matcher;
                // Append a new line character if we expect it
                if (useNewLine) {
                    matcher = pattern.matcher(line.concat("\n"));
                } else {
                    matcher = pattern.matcher(line);
                }
                boolean add = false;
                final List<LogRecordPart> logRecordParts = new ArrayList<LogRecordPart>();
                while (matcher.find()) {
                    add = true;
                    if (matcher.groupCount() + 1 > parts.size()) {
                        throw new IllegalStateException("Parsing error: Group count is greater than the format size.");
                    }
                    for (int i = 0; i < matcher.groupCount(); i++) {
                        final FormatPart formatPart = parts.get(i);
                        final FormatType formatType = formatPart.getFormatType();
                        final String value = matcher.group(i + 1).trim();
                        for (Filter filter : filters) {
                            if (filter.allowType(formatType)) {
                                if (!filter.accept(formatPart, value)) {
                                    add = false;
                                }
                            }
                        }
                        logRecordParts.add(new LogRecordPart(formatType, value));
                    }
                }
                lineCounter++;
                if (add) {
                    results.add(new LogResult(lineCounter, logRecordParts, line));
                }
            }
            // Trim the results
            if (maxResults > 0 && results.size() > maxResults) {
                final int start = results.size() - maxResults;
                final int end = results.size();
                return results.subList(start, end);
            }
        } finally {
            safeClose(reader);
        }
        return results;
    }

    /**
     * Adds a filter for processing the log records.
     *
     * @param filter the filter
     */
    public void addFilter(final Filter filter) {
        filters.add(filter);
    }

    /**
     * Removes the filter.
     *
     * @param filter the filter
     */
    public void removeFilter(final Filter filter) {
        filters.remove(filter);
    }

    /**
     * Returns the maximum number of results to be returned when parsing.
     *
     * @return the maximum number of results allowed
     */
    public int getMaxResults() {
        return maxResults;
    }

    /**
     * Sets the maximum number of results to be returned when parsing. Setting the value to 0 returns all results.
     *
     * @param maxResults the maximum number of results
     */
    public void setMaxResults(final int maxResults) {
        this.maxResults = maxResults;
    }

    static String sanitize(final String pattern) {
        final StringBuilder result = new StringBuilder();
        for (char c : pattern.toCharArray()) {
            switch (c) {
                case '\\':
                case '(':
                case ')':
                case '[':
                case ']':
                case '{':
                case '}':
                case '$':
                case '^':
                case '.':
                case '*':
                case '+':
                case '|':
                case '?':
                    result.append('\\').append(c);
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                default:
                    result.append(c);
                    break;
            }
        }
        return result.toString();
    }

    static void safeClose(final Closeable closeable) {
        if (closeable != null) try {
            closeable.close();
        } catch (Exception ignore) {
            // ignore
        }
    }
}
