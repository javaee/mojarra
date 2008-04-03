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
package com.sun.faces.sandbox.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jason Lee
 *
 */
public class StaticResourcePhaseListener implements PhaseListener {
//  TODO:  Class.forName("some.shale.class"); useShaleStuff(); catch (ClassNotFound) {useOurStuff()}; 
    private static final long serialVersionUID = 1L;
    protected static Map mimeTypes = new HashMap();
    {
        mimeTypes.put("css", "text/css");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("jpg", "images/jpg");
        mimeTypes.put("js", "text/javascript");
        mimeTypes.put("pdf", "application/pdf");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("jar", "application/x-java-applet");
    }
    protected static List attachmentTypes = new ArrayList();
    {
        attachmentTypes.add("application/x-java-applet");
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    public void beforePhase(PhaseEvent e) {
        PhaseId phase = e.getPhaseId();
        if (phase == PhaseId.RESTORE_VIEW) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String uri = request.getRequestURI();
            if ((uri != null) && (uri.indexOf(Util.STATIC_RESOURCE_IDENTIFIER) > -1)){
                // TODO:  make sure we can sandbox this correctly (i.e., no file=../foo.txt)
                String fileName = request.getParameter("file");
                if ((fileName != null) && !"".equals(fileName.trim())) {
                    fileName = "/META-INF/static" +
                    (fileName.startsWith("/") ? "" : "/") + fileName;
                    HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                    try {
                        InputStream is = getClass().getResourceAsStream(fileName);
                        if (is != null) {
                            OutputStream os = response.getOutputStream();
                            String mimeType = getMimeType(fileName);
                            response.setContentType(mimeType);
                            if (attachmentTypes.indexOf(mimeType) > -1) {
                                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                            }
                            /* This is a little ugly, but we need a way to make any CSS or JS references to static
                             * resources, such as images, point to a URL that this PL will pick up.  Ryan suggested
                             * a custom OutputStream like ViewHandlerImpl.WriteBehindStringWriter, which I'll have
                             * to chew on a bit.  This will get me by for now, I hope.
                             */
                            if ("text/css".equals(mimeType) || "text/javascript".equals(mimeType)) {
                                String text = Util.readInString(is);
                                text = text.replaceAll("%%%BASE_URL%%%", Util.generateStaticUri(""));
                                os.write(text.getBytes());
                            } else {
                                int count = 0;
                                byte[] buffer = new byte[4096];
                                while ((count = is.read(buffer)) != -1) {
                                    if (count > 0) {
                                        os.write(buffer, 0, count);
                                    }
                                }
                            }
                            is.close();
                        } else {
                            response.sendError(404);
                        }
                        context.responseComplete();
                    } catch (IOException ioe) {
                        System.err.println(ioe.getMessage());
                    }
                }
            }
        }
    }

    protected String getMimeType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        return (String)mimeTypes.get(extension);
    }

    public void afterPhase(PhaseEvent event) {
        // Do nothing
    }
}