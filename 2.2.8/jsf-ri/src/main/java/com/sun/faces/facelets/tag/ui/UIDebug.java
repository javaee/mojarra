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
package com.sun.faces.facelets.tag.ui;

import com.sun.faces.facelets.util.DevTools;
import com.sun.faces.facelets.util.FastWriter;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jacob Hookom
 */
public final class UIDebug extends UIComponentBase {

    public final static String COMPONENT_TYPE = "facelets.ui.Debug";
    public final static String COMPONENT_FAMILY = "facelets";
    private static long nextId = System.currentTimeMillis();
    private final static String KEY = "facelets.ui.DebugOutput";
    public final static String DEFAULT_HOTKEY = "D";
    private String hotkey = DEFAULT_HOTKEY;

    public UIDebug() {
        super();
        this.setTransient(true);
        this.setRendererType(null);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public List getChildren() {
        return new ArrayList() {

            public boolean add(Object o) {
                throw new IllegalStateException("<ui:debug> does not support children");
            }

            public void add(int index, Object o) {
                throw new IllegalStateException("<ui:debug> does not support children");
            }
        };
    }

    public void encodeBegin(FacesContext facesContext) throws IOException {
        
        if (isRendered()) {
            pushComponentToEL(facesContext, this);
            String actionId = facesContext.getApplication().getViewHandler().getActionURL(facesContext, facesContext.getViewRoot().getViewId());

            StringBuffer sb = new StringBuffer(512);
            sb.append("//<![CDATA[\n");
            sb.append("function faceletsDebug(URL) {");
            sb.append("day = new Date();");
            sb.append("id = day.getTime();");
            sb.append("eval(\"page\" + id + \" = window.open(URL, '\" + id + \"', 'toolbar=0,scrollbars=1,location=0,statusbar=0,menubar=0,resizable=1,width=800,height=600,left = 240,top = 212');\"); };");
            sb.append("(function() { if (typeof jsfFaceletsDebug === 'undefined') { var jsfFaceletsDebug = false; } if (!jsfFaceletsDebug) {");
            sb.append("var faceletsOrigKeyup = document.onkeyup;");
            sb.append("document.onkeyup = function(e) { if (window.event) e = window.event; if (String.fromCharCode(e.keyCode) == '" + this.getHotkey() + "' & e.shiftKey & e.ctrlKey) faceletsDebug('");
            sb.append(actionId);
            sb.append(actionId.indexOf('?') == -1 ? '?' : '&');
            sb.append(KEY);
            sb.append('=');
            sb.append(writeDebugOutput(facesContext));
            sb.append("'); jsfFaceletsDebug = true; if (faceletsOrigKeyup) faceletsOrigKeyup(e); };\n");
            sb.append("}})();");
            sb.append("//]]>\n");

            ResponseWriter writer = facesContext.getResponseWriter();
            writer.startElement("span", this);
            writer.writeAttribute("id", getClientId(facesContext), "id");
            writer.startElement("script", this);
            writer.writeAttribute("language", "javascript", "language");
            writer.writeAttribute("type", "text/javascript", "type");
            writer.writeText(sb.toString(), this, null);
            writer.endElement("script");
            writer.endElement("span");
        }
    }

    private static String writeDebugOutput(FacesContext faces) throws IOException {
        FastWriter fw = new FastWriter();
        DevTools.debugHtml(fw, faces);

        Map session = faces.getExternalContext().getSessionMap();
        Map debugs = (Map) session.get(KEY);
        if (debugs == null) {
            debugs = new LinkedHashMap() {

                protected boolean removeEldestEntry(Map.Entry eldest) {
                    return (this.size() > 5);
                }
            };
        }
        session.put(KEY, debugs);
        String id = "" + nextId++;
        debugs.put(id, fw.toString());
        return id;
    }

    private static String fetchDebugOutput(FacesContext faces, String id) {
        Map session = faces.getExternalContext().getSessionMap();
        Map debugs = (Map) session.get(KEY);
        if (debugs != null) {
            return (String) debugs.get(id);
        }
        return null;
    }

    public static boolean debugRequest(FacesContext faces) {
        String id = (String) faces.getExternalContext().getRequestParameterMap().get(KEY);
        if (id != null) {
            Object resp = faces.getExternalContext().getResponse();
            if (!faces.getResponseComplete()
                    && resp instanceof HttpServletResponse) {
                try {
                    HttpServletResponse httpResp = (HttpServletResponse) resp;
                    String page = fetchDebugOutput(faces, id);
                    if (page != null) {
                        httpResp.setContentType("text/html");
                        httpResp.getWriter().write(page);
                    } else {
                        httpResp.setContentType("text/plain");
                        httpResp.getWriter().write("No Debug Output Available");
                    }
                    httpResp.flushBuffer();
                    faces.responseComplete();
                } catch (IOException e) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public String getHotkey() {
        return this.hotkey;
    }

    public void setHotkey(String hotkey) {
        this.hotkey = (hotkey != null) ? hotkey.toUpperCase() : "";
    }
}
