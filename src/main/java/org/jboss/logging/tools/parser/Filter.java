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
 * Filters out log records being parsed.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public interface Filter {

    /**
     * Checks to see if the log messages should be in the parsed results.
     *
     * @param formatPart the format information
     * @param value      the value from the log record
     *
     * @return {@code true} if the log record should be in the results, otherwise {@code false}
     */
    boolean accept(FormatPart formatPart, String value);

    /**
     * Checks that this filter allows the format type to be checked. If this method returns {@code false}, {@link
     * #accept(FormatPart, String)} will not be invoked.
     *
     * @param formatType the format type to check
     *
     * @return {@code true} if this filter can processes this type, otherwise {@code false}
     */
    boolean allowType(FormatType formatType);
}
