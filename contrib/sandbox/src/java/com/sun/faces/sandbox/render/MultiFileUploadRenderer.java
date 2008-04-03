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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;

import com.sun.faces.sandbox.component.MultiFileUpload;
import com.sun.faces.sandbox.util.Util;

/**
 * @author Jason Lee
 *
 */
public class MultiFileUploadRenderer extends Renderer {
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

        MultiFileUpload ul = (MultiFileUpload)component;
        renderAppletTag(context, ul);
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().setAttribute("HtmlMultiFileUpload-" + ul.getClientId(context), ul);
    }
    
    protected void renderAppletTag(FacesContext context, MultiFileUpload ul) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String template = readTemplateFile(MultiFileUpload.TEMPLATE_FILE);
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        // Just pass back /AppContext/foo.jsf (or /AppContext/faces/foo.xhtml, etc)
        // The applet will construct the full URL based on
        // documentBase
        String uploadUrl = 
            context.getApplication().getViewHandler().getActionURL(context, generateUri(context, MultiFileUpload.UPLOAD_URI)) +
            "?" + MultiFileUpload.REQUEST_PARAM + "=" + ul.getClientId(context);
        String appletClass = ("button".equalsIgnoreCase(ul.getType())) ?
                "ButtonApplet" : "FullApplet"; // default to "full"
            
        StringBuilder deps = new StringBuilder();
        String sep = "";
            
        for (String dep : MultiFileUpload.DEP_JARS) {
            String depURI =
                context.getApplication().getViewHandler().getActionURL(context, generateUri(context, MultiFileUpload.JARS_URI + dep));
            deps.append(sep)
//                .append(Util.generateStaticUri(dep))
                .append(depURI);
            sep = ",";
        }
        
        template = template.replaceAll("%%%DEPS%%%", deps.toString())
            .replaceAll("%%%APPLET_CLASS%%%", appletClass)
            .replaceAll("%%%WIDTH%%%", ul.getWidth())
            .replaceAll("%%%HEIGHT%%%", ul.getHeight())
            .replaceAll("%%%START_DIR%%%", ul.getStartDir())
            .replaceAll("%%%UPLOAD_URL%%%", uploadUrl)
            .replaceAll("%%%BUTTON_TEXT%%%", ul.getButtonText())
            .replaceAll("%%%FILE_FILTER%%%", ul.getFileFilter())
            .replaceAll("%%%SESSION_ID%%%", request.getSession().getId());
        writer.write(template);
        
    }
    
    protected String readTemplateFile(String fileName) {
        InputStream is = getClass().getResourceAsStream(fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = is.read(buffer)) != -1) {
                if (count > 0) {
                    baos.write(buffer, 0, count);
                }
            }
            is.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        return baos.toString();
    }
    
    // TODO:  Look at the Util method by the same name.  There's a good opportunity
    // for a refactor here.  Thank you, merging code bases. :)
    protected String generateUri(FacesContext context, String target) {
        String uri = "";
        String mapping = Util.getFacesMapping(context);
        if (Util.isPrefixMapped(mapping)) {
            uri = "/" + target + "." + mapping;
        } else {
            uri = target + mapping;
        }

        return uri;
    }
}