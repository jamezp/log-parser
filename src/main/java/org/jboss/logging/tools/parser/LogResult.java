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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class LogResult implements Iterable<LogRecordPart> {

    private final int lineNumber;
    private final String text;
    private final List<LogRecordPart> parts;

    protected LogResult(final int lineNumber, final List<LogRecordPart> parts, final String text) {
        this.lineNumber = lineNumber;
        this.text = text;
        this.parts = parts;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getText() {
        return text;
    }

    public List<LogRecordPart> getFormatParts() {
        return new ArrayList<LogRecordPart>(parts);
    }

    @Override
    public Iterator<LogRecordPart> iterator() {
        return getFormatParts().iterator();
    }
}
