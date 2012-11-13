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

import java.util.logging.Level;

/**
 * Filters out lo
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class LevelFilter implements Filter {
    private final Level level;
    private final boolean exclusive;

    public LevelFilter(final Level level) {
        this(level, false);
    }

    public LevelFilter(final Level level, final boolean exclusive) {
        this.level = level;
        this.exclusive = exclusive;
    }

    @Override
    public boolean accept(final FormatPart formatPart, final String value) {
        try {
            final Level level = org.jboss.logmanager.Level.parse(value);
            return (exclusive ? (this.level == level) : (level == Level.ALL || (level.intValue() >= this.level.intValue())));
        } catch (Exception ignore) {
            // do nothing
        }
        return false;
    }

    @Override
    public boolean allowType(final FormatType formatType) {
        return formatType == FormatType.LEVEL;
    }
}
