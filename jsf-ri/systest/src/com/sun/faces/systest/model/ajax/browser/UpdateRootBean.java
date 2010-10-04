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
 */

package com.sun.faces.systest.model.ajax.browser;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.FacesException;

@ManagedBean
@RequestScoped
@SuppressWarnings("unused")
public class UpdateRootBean {

    public String updateRootSimple() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeText("PASSED",null);
                writer.endElement("span");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateRootBody() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("body",null);
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeText("PASSED",null);
                writer.endElement("span");
                writer.endElement("body");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateRootAll() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("html",null);
                writer.startElement("head",null);
                writer.startElement("title",null);
                writer.writeText("PASSED",null);
                writer.endElement("title");
                writer.endElement("head");
                writer.startElement("body",null);
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeText("PASSED",null);
                writer.endElement("span");
                writer.endElement("body");
                writer.endElement("html");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateRootFaulty() {  // missing end tags
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("html",null);
                writer.startElement("head",null);
                writer.startElement("title",null);
                writer.writeText("PASSED",null);
                writer.endElement("title");
                writer.endElement("head");
                writer.startElement("body",null);
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeText("PASSED",null);
                writer.endElement("span");
                writer.endElement("body");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateBodySimple() {  // missing end tags
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeText("PASSED",null);
                writer.endElement("span");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateBodyTag() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewBody");
                writer.startElement("body",null);
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeText("PASSED",null);
                writer.endElement("span");
                writer.endElement("body");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateHead() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewHead");
                writer.startElement("head",null);
                writer.startElement("title",null);
                writer.writeText("PASSED",null);
                writer.endElement("title");
                writer.endElement("head");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateRootAllEvent() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("html",null);
                writer.startElement("head",null);
                writer.startElement("title",null);
                writer.writeText("PASSED",null);
                writer.endElement("title");
                writer.endElement("head");
                writer.startElement("body",null);
                writer.startElement("input", null);
                writer.writeAttribute("id","newbutton","id");
                writer.writeAttribute("type","button","type");
                writer.writeAttribute("onclick","checkPass();","onclick");
                writer.writeAttribute("value","Click Me","value");
                writer.endElement("input");
                writer.endElement("body");
                writer.endElement("html");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateRootBodyStyle() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("html",null);
                writer.startElement("head",null);
                writer.startElement("title",null);
                writer.writeText("PASSED",null);
                writer.endElement("title");
                writer.endElement("head");
                writer.startElement("body",null);
                writer.writeAttribute("id","bodyId","id");
                writer.writeAttribute("style", "background-color: green","style");
                writer.writeText("Green means PASSED",null);
                writer.endElement("body");
                writer.endElement("html");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateRootSimpleEvent() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("input", null);
                writer.writeAttribute("id","newbutton","id");
                writer.writeAttribute("type","button","type");
                writer.writeAttribute("onclick","checkPass();","onclick");
                writer.writeAttribute("value","Click Me","value");
                writer.endElement("input");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }


    public String updateRootAllStyle() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("html",null);
                writer.startElement("head",null);
                writer.startElement("title",null);
                writer.writeText("PASSED",null);
                writer.endElement("title");
                writer.endElement("head");
                writer.startElement("body",null);
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeAttribute("style", "background-color: green","style");
                writer.writeText("Green means PASSED",null);
                writer.endElement("span");
                writer.endElement("body");
                writer.endElement("html");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateRootSimpleStyle() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeAttribute("style", "background-color: green","style");
                writer.writeText("Green means PASSED",null);
                writer.endElement("span");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

}
