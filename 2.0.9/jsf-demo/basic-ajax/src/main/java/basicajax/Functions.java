/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package basicajax;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * EL Functions.
 */
public class Functions {

    private static final Logger LOGGER = Logger.getLogger(Functions.class.getName());


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>
     * Write the file content to the current ResponseWriter.
     * </p>
     *
     * @param ctx the <code>FacesContext</code> for the current request
     * @param file the file to display
     */
    public static void writeSource(FacesContext ctx, String file) {

        // PENDING - add logic to colorize key words/XML elements?
        // PENDING - add logic to strip licence header

        ExternalContext extCtx = ctx.getExternalContext();
        BufferedReader r =
              new BufferedReader(
                    new InputStreamReader(extCtx.getResourceAsStream(file)));
        StringWriter w = new StringWriter();
        PrintWriter pw = new PrintWriter(w);

        try {
            int lineNumber = 1;
            for (String s = r.readLine(); s != null; s = r.readLine()) {
                pw.format("%3s", Integer.toString(lineNumber++));
                pw.write(": ");
                pw.write(s);
                pw.write('\n');
            }
            ctx.getResponseWriter().writeText(w.toString(), null);
        } catch (IOException ioe) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           ioe.toString(),
                           ioe);
            }
        }

    }
}
