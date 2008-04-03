/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
 */

/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;

import com.sun.faces.sandbox.component.FileDownload;
import com.sun.faces.sandbox.util.Util;

/**
 * @author Jason Lee
 *
 */
public class FileDownloadRenderer extends Renderer {
    protected Object oldBinding = null;

    @Override
    public boolean getRendersChildren() {
        return false;
    }
    
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("param 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("param 'component' is null");
        }

        // suppress rendering if "rendered" property on the component is false.
        if (!component.isRendered()) {
            return;
        }
        FileDownload dl = (FileDownload) component;
        super.encodeBegin(context, component);
        if ((dl.getUrlVar() != null) || (component.getChildCount() > 0)){
            setElValue(context, dl);
        }
        if (FileDownload.METHOD_DOWNLOAD.equals(dl.getMethod())) { // || (comp.getChildCount() == 0)) {
            renderLink(context, dl);
        }
    }
    
    protected void setElValue(FacesContext context, FileDownload comp) {
        ValueBinding vb = Util.getValueBinding("#{"+comp.getUrlVar()+"}");
        vb.setValue(context, generateUri(context, comp));
        
    }
    
    protected void resetElValue(FacesContext context, FileDownload comp) {
        ValueBinding vb = Util.getValueBinding("#{"+comp.getUrlVar()+"}");
        vb.setValue(context, null);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("param 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("param 'component' is null");
        }

        // suppress rendering if "rendered" property on the component is false.
        if (!component.isRendered()) {
            return;
        }
        FileDownload dl = (FileDownload) component;
        if (FileDownload.METHOD_INLINE.equals(dl.getMethod())) {
            renderInline(context, dl);
        } else if (FileDownload.METHOD_DOWNLOAD.equals(dl.getMethod())) {
            ResponseWriter writer = context.getResponseWriter();
            writer.endElement("a"); // finsh up the link!
        }
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().setAttribute("HtmlDownload-" + dl.getClientId(context), dl);
        resetElValue(context, dl);
        super.encodeEnd(context, component);
    }

    protected void renderInline(FacesContext context, FileDownload comp) throws IOException {
//        String width = comp.getWidth();
//        String height = comp.getHeight();
        
        ResponseWriter writer = context.getResponseWriter();
        String uri = generateUri(context, comp);
        if (Boolean.TRUE.equals(comp.getIframe())) {
            writer.startElement("iframe", comp);
            writer.writeAttribute("src", uri, "src");
//            writer.writeAttribute("width", width, "width");
//            writer.writeAttribute("height", height, "height");
            Util.renderPassThruAttributes(writer, comp);
            writer.endElement("iframe");
        } else {
            writer.startElement("object", comp);
            writer.writeAttribute("data", uri, "data");
            writer.writeAttribute("type", comp.getMimeType(), "type");
//            writer.writeAttribute("width", width, "width");
//            writer.writeAttribute("height", height, "height");
            Util.renderPassThruAttributes(writer, comp);
            writer.endElement("object");
        }
    }

    protected void renderLink(FacesContext context, FileDownload comp) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("a", comp);
        writer.writeAttribute("href", generateUri(context, comp), "data");
        Util.renderPassThruAttributes(writer, comp);
        if (comp.getChildCount() > 0) {
            //
        } else {
            writer.writeText("Download", null);
        }

    }

    // TODO:  Look at the Util method by the same name.  There's a good opportunity
    // for a refactor here.  Thank you, merging code bases. :)
    protected String generateUri(FacesContext context, FileDownload comp) {
        String uri = "";
        String mapping = Util.getFacesMapping(context);
        if (Util.isPrefixMapped(mapping)) {
            uri = mapping + "/" + FileDownload.DOWNLOAD_URI;
        } else {
            uri = FileDownload.DOWNLOAD_URI + mapping;
        }

        uri += "?" + FileDownload.REQUEST_PARAM + "=" + comp.getClientId(context);

        return uri;
    }
}