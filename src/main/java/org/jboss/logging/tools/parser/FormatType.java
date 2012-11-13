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

import java.util.HashMap;
import java.util.Map;

/**
 * Represents for the format type character for a {@link org.jboss.logmanager.formatters.PatternFormatter}.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public enum FormatType {
    CATEGORY('c', "(\\D+)"),
    CLASS_NAME('C', "(\\S+)"),
    DATE('d', "(.+)"),
    EXCEPTION('e', "(.*)"),
    EXCEPTION_EXTENDED('E', "(.*)"),
    SOURCE_FILE_NAME('F', "(\\S+)"),
    RESOURCE_KEY('k', "(\\S+)"),
    COLOR('K', "(\\S+)"),
    SOURCE_LOCATION('l', "(\\S+)"),
    SOURCE_LINE_NUMBER('L', "(\\S+)"),
    MESSAGE('m', "(.+)"),
    SOURCE_METHOD('M', "(\\S+)"),
    NEW_LINE('n', "\\n"),
    LEVEL('p', "(\\D+)"),
    LOCALIZED_LEVEL('P', "(\\D+)"),
    RELATIVE_TIME('r', "(\\S+)"),
    THREAD('t', "(.+)"),
    SIMPLE_MESSAGE('s', "(.+)"),
    NDC('x', "(.+)"),
    MDC('X', "(.+)"),
    TIMEZONE('z', "(\\S+)"),
    PERCENTAGE('%', "%");

    private final char formatChar;
    private final String pattern;

    private FormatType(final char formatChar, final String pattern) {
        this.formatChar = formatChar;
        this.pattern = pattern;
    }

    /**
     * The format character.
     *
     * @return the format character
     */
    public char getFormatChar() {
        return formatChar;
    }

    /**
     * The pattern used for the regular expression when parsing the log output.
     *
     * @return the pattern for the group
     */
    public String toPattern() {
        return pattern;
    }

    private static final Map<Character, FormatType> MAP;

    static {
        MAP = new HashMap<Character, FormatType>();
        for (FormatType formatType : values()) {
            MAP.put(formatType.formatChar, formatType);
        }
    }

    /**
     * Returns the format type for the character in a format pattern.
     *
     * @param c the format character
     *
     * @return the format type
     *
     * @throws IllegalArgumentException if the character is not a valid format character
     */
    public static FormatType fromChar(final char c) {
        if (MAP.containsKey(c)) {
            return MAP.get(c);
        }
        throw new IllegalArgumentException("Invalid format character: " + c);
    }
}
