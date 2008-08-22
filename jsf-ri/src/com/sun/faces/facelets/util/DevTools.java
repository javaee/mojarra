/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.el.Expression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

public final class DevTools {
    
    private final static String TS = "&lt;";
    
    private static final String ERROR_TEMPLATE = "META-INF/facelet-dev-error.xml";
    
    private static String[] ERROR_PARTS;
    
    private static final String DEBUG_TEMPLATE = "META-INF/facelet-dev-debug.xml";
    
    private static String[] DEBUG_PARTS;

    public DevTools() {
        super();
    }
    
    public static void main(String[] argv) throws Exception {
        DevTools.init();
    }
    
    private static void init() throws IOException {
        if (ERROR_PARTS == null) {
            ERROR_PARTS = splitTemplate(ERROR_TEMPLATE);
        }
        
        if (DEBUG_PARTS == null) {
            DEBUG_PARTS = splitTemplate(DEBUG_TEMPLATE);
        }
    }
    
    private static String[] splitTemplate(String rsc) throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(rsc);
        if (is == null) {
            throw new FileNotFoundException(rsc);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[512];
        int read;
        while ((read = is.read(buff)) != -1) {
            baos.write(buff, 0, read);
        }
        String str = baos.toString();
        return str.split("@@");
    }
    
    public static void debugHtml(Writer writer, FacesContext faces, Exception e) throws IOException {
        init();
        Date now = new Date();
        for (int i = 0; i < ERROR_PARTS.length; i++) {
            if ("message".equals(ERROR_PARTS[i])) {
                String msg = e.getMessage();
                if (msg != null) {
                    writer.write(msg.replaceAll("<", TS));
                } else {
                    writer.write(e.getClass().getName());
                }
            } else if ("trace".equals(ERROR_PARTS[i])) {
                writeException(writer, e);
            } else if ("now".equals(ERROR_PARTS[i])) {
                writer.write(DateFormat.getDateTimeInstance().format(now));
            } else if ("tree".equals(ERROR_PARTS[i])) {
                writeComponent(writer, faces.getViewRoot());
            } else if ("vars".equals(ERROR_PARTS[i])) {
                writeVariables(writer, faces);
            } else {
                writer.write(ERROR_PARTS[i]);
            }
        }
    }
    
    private static void writeException(Writer writer, Exception e) throws IOException {
        StringWriter str = new StringWriter(256);
        PrintWriter pstr = new PrintWriter(str);
        e.printStackTrace(pstr);
        pstr.close();
        writer.write(str.toString().replaceAll("<", TS));
    }
    
    public static void debugHtml(Writer writer, FacesContext faces) throws IOException {
        init();
        Date now = new Date();
        for (int i = 0; i < DEBUG_PARTS.length; i++) {
            if ("message".equals(DEBUG_PARTS[i])) {
                writer.write(faces.getViewRoot().getViewId());
            } else if ("now".equals(DEBUG_PARTS[i])) {
                writer.write(DateFormat.getDateTimeInstance().format(now));
            } else if ("tree".equals(DEBUG_PARTS[i])) {            
                writeComponent(writer, faces.getViewRoot());
            } else if ("vars".equals(DEBUG_PARTS[i])) {
                writeVariables(writer, faces);
            } else {
                writer.write(DEBUG_PARTS[i]);
            }
        }
    }
    
    private static void writeVariables(Writer writer, FacesContext faces) throws IOException {
        ExternalContext ctx = faces.getExternalContext();
        writeVariables(writer, ctx.getRequestParameterMap(), "Request Parameters");
        writeVariables(writer, ctx.getRequestMap(), "Request Attributes");
        if (ctx.getSession(false) != null) {
            writeVariables(writer, ctx.getSessionMap(), "Session Attributes");
        }
        writeVariables(writer, ctx.getApplicationMap(), "Application Attributes");
    }
    
    private static void writeVariables(Writer writer, Map vars, String caption) throws IOException {
        writer.write("<table><caption>");
        writer.write(caption);
        writer.write("</caption><thead><tr><th style=\"width: 10%; \">Name</th><th style=\"width: 90%; \">Value</th></tr></thead><tbody>");
        boolean written = false;
        if (!vars.isEmpty()) {
            SortedMap map = new TreeMap(vars);
            Map.Entry entry = null;
            String key = null;
            for (Iterator itr = map.entrySet().iterator(); itr.hasNext(); ) {
                entry = (Map.Entry) itr.next();
                key = entry.getKey().toString();
                if (key.indexOf('.') == -1) {
                    writer.write("<tr><td>");
                    writer.write(key.replaceAll("<", TS));
                    writer.write("</td><td>");
                    writer.write(entry.getValue() == null
                                 ? "null"
                                 : entry.getValue().toString()
                                       .replaceAll("<", TS));
                    writer.write("</td></tr>");
                    written = true;
                }
            }
        }
        if (!written) {
            writer.write("<tr><td colspan=\"2\"><em>None</em></td></tr>");
        }
        writer.write("</tbody></table>");
    }
    
    private static void writeComponent(Writer writer, UIComponent c) throws IOException {
        writer.write("<dl><dt");
        if (isText(c)) {
            writer.write(" class=\"uicText\"");
        }
        writer.write(">");
        
        boolean hasChildren = c.getChildCount() > 0 || c.getFacets().size() > 0;
        
        writeStart(writer, c, hasChildren);
        writer.write("</dt>");
        if (hasChildren) {
            if (c.getFacets().size() > 0) {
                Map.Entry entry;
                for (Iterator itr = c.getFacets().entrySet().iterator(); itr.hasNext(); ) {
                    entry = (Map.Entry) itr.next();
                    writer.write("<dd class=\"uicFacet\">");
                    writer.write("<span>");
                    writer.write((String) entry.getKey());
                    writer.write("</span>");
                    writeComponent(writer, (UIComponent) entry.getValue());
                    writer.write("</dd>");
                }
            }
            if (c.getChildCount() > 0) {
                for (Iterator itr = c.getChildren().iterator(); itr.hasNext(); ) {
                    writer.write("<dd>");
                    writeComponent(writer, (UIComponent) itr.next());
                    writer.write("</dd>");
                }
            }
            writer.write("<dt>");
            writeEnd(writer, c);
            writer.write("</dt>");
        }
        writer.write("</dl>");
    }
    
    private static void writeEnd(Writer writer, UIComponent c) throws IOException {
        if (!isText(c)) {
            writer.write(TS);
            writer.write('/');
            writer.write(getName(c));
            writer.write('>');
        }
    }
    
    private final static String[] IGNORE = new String[] { "parent", "rendererType" };
    
    private static void writeAttributes(Writer writer, UIComponent c) {
        try {
            BeanInfo info = Introspector.getBeanInfo(c.getClass());
            PropertyDescriptor[] pd = info.getPropertyDescriptors();
            Method m = null;
            Object v = null;
            String str = null;
            for (int i = 0; i < pd.length; i++) {
                if (pd[i].getWriteMethod() != null && Arrays.binarySearch(IGNORE, pd[i].getName()) < 0) {
                    m = pd[i].getReadMethod();
                    try {
                        v = m.invoke(c);
                        if (v != null) {
                            if (v instanceof Collection || v instanceof Map || v instanceof Iterator) {
                                continue;
                            }
                            writer.write(" ");
                            writer.write(pd[i].getName());
                            writer.write("=\"");
                            if (v instanceof Expression) {
                                str = ((Expression) v).getExpressionString();
                            } else if (v instanceof ValueBinding) {
                                str = ((ValueBinding) v).getExpressionString();
                            } else if (v instanceof MethodBinding) {
                                str = ((MethodBinding) v).getExpressionString();
                            } else {
                                str = v.toString();
                            }
                            writer.write(str.replaceAll("<", TS));
                            writer.write("\"");
                        }
                    } catch (Exception e) {
                        // do nothing
                    }
                }
            }

            ValueBinding binding = c.getValueBinding("binding");
            if (binding != null) {
                writer.write(" binding=\"");
                writer.write(binding.getExpressionString().replaceAll("<", TS));
                writer.write("\"");
            }
        } catch (Exception e) {
            // do nothing
        }
    }
    
    private static void writeStart(Writer writer, UIComponent c, boolean children) throws IOException {
        if (isText(c)) {
            String str = c.toString().trim();
            writer.write(str.replaceAll("<", TS));
        } else {
            writer.write(TS);
            writer.write(getName(c));
            writeAttributes(writer, c);
            if (children) {
                writer.write('>');
            } else {
                writer.write("/>");
            }
        }
    }
    
    private static String getName(UIComponent c) {
        String nm = c.getClass().getName();
        return nm.substring(nm.lastIndexOf('.') + 1);
    }
    
    private static boolean isText(UIComponent c) {
        return (c.getClass().getName().startsWith("com.sun.faces.facelets.compiler"));
    }

}
