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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;

import org.jboss.logmanager.Level;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class Main {

    public static void main(final String[] args) throws IOException {

        final Parser parser = Parser.of("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n");
        // parser.addFilter(FormatType.LEVEL, new LevelFilter(org.jboss.logmanager.Level.INFO));
        // parser.addFilter(FormatType.CATEGORY, new CategoryFilter("org.jboss.as.server"));
        // parser.setCategory("org.jboss.jca.core.connectionmanager.pool.strategy.OnePool");
        final Collection<LogResult> logResults = parser.parse(Main.class.getResourceAsStream("/last-5000.txt"));
        // final Collection<LogResult> logResults = parser.parse(new FileInputStream("/home/jperkins/servers/jboss-as-7.1.1.Final/standalone/log/server.log"));
        int count = 0;
        for (LogResult logResult : logResults) {
            System.out.printf("Count: %03d [%04d]: %s%n", ++count, logResult.getLineNumber(), logResult.getText());
        }

    }
}
