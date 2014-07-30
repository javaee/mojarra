/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.test.servlet30.ajax.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.ResponseWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class BeforeFilter implements Filter {
    
    private FilterConfig filterConfig = null;
    
    public BeforeFilter() {
    }    
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        
        try {
            HttpServletResponse resp = (HttpServletResponse) response;
            PrintWriter pw = resp.getWriter();

            ResponseWriter responseWriter;
            Class htmlResponseWriterClass = Class.forName("com.sun.faces.renderkit.html_basic.HtmlResponseWriter");
            Constructor ctor = htmlResponseWriterClass.getConstructor(Writer.class, String.class, String.class);
            responseWriter = (ResponseWriter) ctor.newInstance(pw, "text/xml", "UTF-8");
            PartialResponseWriter partialResponseWriter = new PartialResponseWriter(responseWriter);
            partialResponseWriter.writePreamble("<?xml version='1.0' encoding='UTF-8'?>\n");
            partialResponseWriter.startDocument();
            partialResponseWriter.startUpdate("foo");
            partialResponseWriter.endUpdate();
            partialResponseWriter.endDocument();
            partialResponseWriter.close();
        } catch (Exception t) {
            HttpServletResponse resp = (HttpServletResponse) response;
            PrintWriter pw = resp.getWriter();
            try {
                pw.print("<html><body><p id=\"result\">FAILURE</p>");
                int indentLevel = 0;
                String indent;
                Throwable cause = t;
                do {
                    StringBuilder indentBuilder = new StringBuilder();
                    for (int i = 0; i < indentLevel; i++) {
                        indentBuilder.append("&nbsp;&nbsp;");
                    }
                    indent = indentBuilder.toString();
                    pw.print("<p>" + indent + " Exception: " + cause.getClass().getName() + "</p>");
                    pw.print("<p>" + indent + " Exception Message: " + cause.getLocalizedMessage() + "</p>");
                        pw.print("<code><pre>");
                        cause.printStackTrace(pw);
                        pw.print("</pre></code>");
                } while (null != (cause = cause.getCause()));
                pw.print("</body></html>");
                resp.setStatus(200);
                pw.close();
            } catch (Exception e) {
            }
        }
        
    }

    public void destroy() {        
    }

    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
    }
    
}
