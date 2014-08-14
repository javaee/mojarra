/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
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

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import javax.el.Expression;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Utility class for displaying Facelet error/debug information.
 * </p>
 *
 * <p>
 * The public static methods of this class are exposed as EL functions under
 * the namespace <code>http://java.sun.com/mojarra/private/functions</code>
 * </p>
 *
 *
 */
public final class DevTools {
    
    public final static String Namespace = "http://java.sun.com/mojarra/private/functions";
    public final static String NewNamespace = "http://xmlns.jcp.org/mojarra/private/functions";

    private static final Logger LOGGER = Logger.getLogger(DevTools.class.getPackage().getName());
    
    private final static String TS = "&lt;";
    
    private static final String ERROR_TEMPLATE = "META-INF/facelet-dev-error.xml";
    
    private static String[] ERROR_PARTS;
    
    private static final String DEBUG_TEMPLATE = "META-INF/facelet-dev-debug.xml";
    
    private static String[] DEBUG_PARTS;


    // ------------------------------------------------------------ Constructors


    private DevTools() {
        throw new IllegalStateException();
    }


    // ---------------------------------------------------------- Public Methods


     public static void debugHtml(Writer writer, FacesContext faces, Throwable e) throws IOException {

         init();
         Date now = new Date();
         for (String ERROR_PART : ERROR_PARTS) {
             if ("message".equals(ERROR_PART)) {
                 writeMessage(writer, e);
             } else if ("trace".equals(ERROR_PART)) {
                 writeException(writer, e);
             } else if ("now".equals(ERROR_PART)) {
                 writer.write(DateFormat.getDateTimeInstance().format(now));
             } else if ("tree".equals(ERROR_PART)) {
                 writeComponent(writer, faces.getViewRoot());
             } else if ("vars".equals(ERROR_PART)) {
                 writeVariables(writer, faces);
             } else {
                 writer.write(ERROR_PART);
             }
         }

    }


    public static void writeMessage(Writer writer, Throwable e)
    throws IOException {

        if (e != null) {
            String msg = e.getMessage();
            if (msg != null) {
                writer.write(msg.replaceAll("<", TS));
            } else {
                writer.write(e.getClass().getName());
            }
        }

    }


    public static void writeException(Writer writer, Throwable e)
    throws IOException {

        if (e != null) {
            StringWriter str = new StringWriter(256);
            PrintWriter pstr = new PrintWriter(str);
            e.printStackTrace(pstr);
            pstr.close();
            writer.write(str.toString().replaceAll("<", TS));
        }

    }


    public static void debugHtml(Writer writer, FacesContext faces)
    throws IOException {

        // PENDING - this and debugHtml(Writer, FacesContext, Exception) should
        //           be refactored to share code.
        init();
        Date now = new Date();
        for (String DEBUG_PART : DEBUG_PARTS) {
            if ("message".equals(DEBUG_PART)) {
                writer.write(faces.getViewRoot().getViewId());
            } else if ("now".equals(DEBUG_PART)) {
                writer.write(DateFormat.getDateTimeInstance().format(now));
            } else if ("tree".equals(DEBUG_PART)) {
                writeComponent(writer, faces.getViewRoot());
            } else if ("vars".equals(DEBUG_PART)) {
                writeVariables(writer, faces);
            } else {
                writer.write(DEBUG_PART);
            }
        }

    }

    
    public static void writeVariables(Writer writer, FacesContext faces)
    throws IOException {

        ExternalContext ctx = faces.getExternalContext();
        writeVariables(writer, ctx.getRequestParameterMap(), "Request Parameters");
        if (faces.getViewRoot() != null) {
            Map<String, Object> viewMap = faces.getViewRoot().getViewMap(false);
            if (viewMap != null) {
                writeVariables(writer, viewMap, "View Attributes");
            } else {
                writeVariables(writer, Collections.<String,Object>emptyMap(), "View Attributes");
            }
        } else {
            writeVariables(writer, Collections.<String,Object>emptyMap(), "View Attributes");
        }
        writeVariables(writer, ctx.getRequestMap(), "Request Attributes");
        Flash flash = ctx.getFlash();
        try {
            flash = ctx.getFlash();
        } catch (UnsupportedOperationException uoe) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "Flash not supported", uoe);
            }
        }
        if (flash != null) {
            writeVariables(writer, flash, "Flash Attributes");
        } else {
            writeVariables(writer, Collections.<String,Object>emptyMap(), "Flash Attributes");
        }
        if (ctx.getSession(false) != null) {
            writeVariables(writer, ctx.getSessionMap(), "Session Attributes");
        } else {
            writeVariables(writer, Collections.<String,Object>emptyMap(), "Session Attributes");
        }
        writeVariables(writer, ctx.getApplicationMap(), "Application Attributes");

    }


    public static void writeComponent(Writer writer, UIComponent c)
    throws IOException {

        writer.write("<dl style=\"color: #006;\"><dt style=\"border: 1px solid #DDD; padding: 4px; border-left: 2px solid #666; font-family: 'Courier New', Courier, mono; font-size: small;");
        if (c != null) {
            if (isText(c)) {
                writer.write("color: #999;");
            }
        }
        writer.write("\">");
        if (c == null) {
            return;
        }

        boolean hasChildren = c.getChildCount() > 0 || c.getFacets().size() > 0;

        writeStart(writer, c, hasChildren);
        writer.write("</dt>");
        if (hasChildren) {
            if (c.getFacets().size() > 0) {
                for (Map.Entry entry : c.getFacets().entrySet()) {
                    writer.write(
                          "<dd style=\"margin-top: 2px; margin-bottom: 2px;\">");
                    writer.write(
                          "<span style=\"font-family: 'Trebuchet MS', Verdana, Arial, Sans-Serif; font-size: small;\">");
                    writer.write((String) entry.getKey());
                    writer.write("</span>");
                    writeComponent(writer, (UIComponent) entry.getValue());
                    writer.write("</dd>");
                }
            }
            if (c.getChildCount() > 0) {
                for (UIComponent child : c.getChildren()) {
                    writer.write(
                          "<dd style=\"margin-top: 2px; margin-bottom: 2px;\">");
                    writeComponent(writer, child);
                    writer.write("</dd>");
                }
            }
            writer.write("<dt style=\"border: 1px solid #DDD; padding: 4px; border-left: 2px solid #666; font-family: 'Courier New', Courier, mono; font-size: small;\">");
            writeEnd(writer, c);
            writer.write("</dt>");
        }
        writer.write("</dl>");

    }


    // --------------------------------------------------------- Private Methods


    private static void init() throws IOException {

        if (ERROR_PARTS == null) {
            ERROR_PARTS = splitTemplate(ERROR_TEMPLATE);
        }
        
        if (DEBUG_PARTS == null) {
            DEBUG_PARTS = splitTemplate(DEBUG_TEMPLATE);
        }

    }


    private static String[] splitTemplate(String rsc) throws IOException {

        ClassLoader loader = Util.getCurrentLoader(DevTools.class);
        InputStream is = loader.getResourceAsStream(rsc);
        if (is == null) {
            loader = DevTools.class.getClassLoader();
            is = loader.getResourceAsStream(rsc);
            if (is == null) {
                throw new FileNotFoundException(rsc);
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[512];
        int read;
        while ((read = is.read(buff)) != -1) {
            baos.write(buff, 0, read);
        }
        String str = baos.toString(RIConstants.CHAR_ENCODING);
        return str.split("@@");

    }
    



    
    private static void writeVariables(Writer writer, Map<String,?> vars, String caption) throws IOException {

        writer.write("<table style=\"border: 1px solid #CCC; border-collapse: collapse; border-spacing: 0px; width: 100%; text-align: left;\"><caption style=\"text-align: left; padding: 10px 0; font-size: large;\">");
        writer.write(caption);
        writer.write("</caption><thead stype=\"padding: 2px; color: #030; background-color: #F9F9F9;\"><tr style=\"padding: 2px; color: #030; background-color: #F9F9F9;\"><th style=\"padding: 2px; color: #030; background-color: #F9F9F9;width: 10%; \">Name</th><th style=\"padding: 2px; color: #030; background-color: #F9F9F9;width: 90%; \">Value</th></tr></thead><tbody style=\"padding: 10px 6px;\">");
        boolean written = false;
        if (!vars.isEmpty()) {
            SortedMap<String,Object> map = new TreeMap<String,Object>(vars);
            for (Map.Entry<String,Object> entry : map.entrySet()) {
                String key = entry.getKey();
                if (key.indexOf('.') == -1) {
                    writer.write(
                          "<tr style=\"padding: 10px 6px;\"><td style=\"padding: 10px 6px;\">");
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
            writer.write("<tr style=\"padding: 10px 6px;\"><td colspan=\"2\" style=\"padding: 10px 6px;\"><em>None</em></td></tr>");
        }
        writer.write("</tbody></table>");

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
            for (PropertyDescriptor aPd : pd) {
                if (aPd.getWriteMethod() != null
                    && Arrays.binarySearch(IGNORE, aPd.getName()) < 0) {
                    Method m = aPd.getReadMethod();
                    try {
                        Object v = m.invoke(c);
                        if (v != null) {
                            if (v instanceof Collection
                                || v instanceof Map
                                || v instanceof Iterator) {
                                continue;
                            }
                            writer.write(" ");
                            writer.write(aPd.getName());
                            writer.write("=\"");
                            String str;
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
                        if (LOGGER.isLoggable(Level.FINEST)) {
                            LOGGER.log(Level.FINEST, "Error writing out attribute", e);
                        }
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
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "Error writing out attributes", e);
            }
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
