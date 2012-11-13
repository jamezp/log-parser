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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Filters out log records based on the log record date.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class DateTimeFilter implements Filter {

    private final Date lowDate;
    private final Date hiDate;

    /**
     * Create a new date/time filter.
     *
     * @param lowDate the lowest date or time value
     * @param hiDate  the highest date or time value
     */
    public DateTimeFilter(final Date lowDate, final Date hiDate) {
        this.lowDate = lowDate;
        this.hiDate = hiDate;
    }

    @Override
    public boolean accept(final FormatPart formatPart, final String value) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat(formatPart.getArgument());
            final Date recordDate = sdf.parse(value);
            // Format the dates and parse again to set defaults
            final String lowDate = sdf.format(this.lowDate);
            final String hiDate = sdf.format(this.hiDate);
            return recordDate.compareTo(sdf.parse(lowDate)) >= 0 && recordDate.compareTo(sdf.parse(hiDate)) <= 0;
        } catch (Exception ignore) {
            // do nothing
        }
        return false;
    }

    @Override
    public boolean allowType(final FormatType formatType) {
        return formatType == FormatType.DATE;
    }
}
