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

/**
 * Represents a part of a {@link org.jboss.logmanager.formatters.PatternFormatter string pattern}.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class FormatPart {
    private final FormatType formatType;
    private final String argument;
    private final boolean leftJustify;
    private final int minimumWidth;
    private final int maximumWidth;

    protected FormatPart(final FormatType formatType, final String argument, final boolean leftJustify, final int minimumWidth, final int maximumWidth) {
        this.formatType = formatType;
        this.argument = argument;
        this.leftJustify = leftJustify;
        this.minimumWidth = minimumWidth;
        this.maximumWidth = maximumWidth;
    }

    /**
     * The format type.
     *
     * @return the format type
     */
    public FormatType getFormatType() {
        return formatType;
    }

    /**
     * The argument for the format type, may be {@code null}.
     *
     * @return the argument for the format type or {@code null}
     */
    public String getArgument() {
        return argument;
    }

    /**
     * Returns {@code true} if left justification is enabled for this part.
     *
     * @return {@code true} if left justification is enabled, otherwise {@code false}
     */
    public boolean isLeftJustify() {
        return leftJustify;
    }

    /**
     * The minimum width value should be. Whitespace padding is used to achieve he minimum width.
     *
     * @return the minimum width
     */
    public int getMinimumWidth() {
        return minimumWidth;
    }

    /**
     * The maximum width value should be.
     *
     * @return the maximum length
     */
    public int getMaximumWidth() {
        return maximumWidth;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder(getClass().getName()).append('[');
        result.append("argument=").append(argument).append(',');
        result.append("leftJustify=").append(leftJustify).append(',');
        result.append("minimumWidth=").append(minimumWidth).append(',');
        result.append("maximumWidth=").append(maximumWidth);
        return result.append(']').toString();
    }
}
