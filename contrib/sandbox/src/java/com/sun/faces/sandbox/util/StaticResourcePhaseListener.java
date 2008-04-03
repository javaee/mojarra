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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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
    protected static Map<String, String> mimeTypes = new HashMap<String, String>();
    protected static SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
    protected static List<String> attachmentTypes = new ArrayList<String>();
    private String URL_PREFIX = Util.STATIC_RESOURCE_IDENTIFIER;

    static {
        mimeTypes.put("css", "text/css");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("htm", "text/html");
        mimeTypes.put("html", "text/html");
        mimeTypes.put("jpg", "images/jpg");
        mimeTypes.put("js", "text/javascript");
        mimeTypes.put("pdf", "application/pdf");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("jar", "application/x-java-applet");

        attachmentTypes.add("application/x-java-applet");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public void afterPhase(PhaseEvent event) {
        // Do nothing
    }
    
    protected boolean isThisMyFile(String uri) {
        return (uri != null) && (uri.indexOf(URL_PREFIX) > -1);
    }
    
    protected String buildFileName(HttpServletRequest req) {
        String fileName = req.getParameter("file");
        if (fileName == null) {
            fileName = req.getRequestURI().toString();
            int index = fileName.indexOf(URL_PREFIX) + URL_PREFIX.length();
            fileName = fileName.substring(index);
            String mapping = Util.getFacesMapping(FacesContext.getCurrentInstance());
            if (!Util.isPrefixMapped(mapping)) {
                fileName = fileName.substring(0, fileName.length() - mapping.length());
            }
        }
        return (fileName.startsWith("/") ? "" : "/") + fileName;
    }

    public void beforePhase(PhaseEvent e) {
        if (e.getPhaseId() == PhaseId.RESTORE_VIEW) {
            FacesContext context = e.getFacesContext();

            // TODO:  make this work in a portlet environment
            // TODO:  find a portlet environment in which I can test :P
            if (!(context.getExternalContext().getRequest() instanceof HttpServletRequest)) {
                return;
            }
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            String uri = request.getRequestURI();
            if (isThisMyFile(uri)){
                // TODO:  make sure we can sandbox this correctly (i.e., no file=../foo.txt)
                String fileName = buildFileName(request); //request.getParameter("file");
                if ((fileName != null) && !"".equals(fileName.trim())) {
                    if (request.getAttribute("STATIC-FILE-"+fileName) != Boolean.TRUE) {
                        File file = new File (fileName);
                        try {
                            if (!hasBeenModified(context, file)) {
                                String mimeType = getMimeType(fileName);
                                response.setContentType(mimeType);
                                synchronized(format) {
                                    response.setHeader("Last-Modified", 
                                            format.format(file.lastModified()));
                                }
                                processFile(context, fileName, response, mimeType);
                                request.setAttribute("STATIC-FILE"+fileName, Boolean.TRUE);
                            }
                            context.responseComplete();
                        } catch (IOException ioe) {
                            try {
                                response.sendError(404, "Could not find " + fileName);
                            } catch (Exception exc) {
                                //
                            }
                        }
                    }
                }
            }
        }
    }

    protected void processFile(FacesContext context, String fileName, HttpServletResponse response, String mimeType) throws IOException {
        InputStream is = getClass().getResourceAsStream(fileName);
        if (is != null) {
            try {
                OutputStream os = response.getOutputStream();
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
                    streamContent(is, os);
                }
            } finally {
                is.close();
            }
        } else {
            response.sendError(404, "Could not find " + fileName);
        }
        context.responseComplete();
    }

    protected boolean hasBeenModified(FacesContext context, File file) throws IOException {
        Object obj = context.getExternalContext().getRequest();
        if (obj instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) obj;
            long lastModified = req.getDateHeader("If-Modified-Since");
            if ((lastModified > -1) && (file.lastModified() > lastModified)) {
                HttpServletResponse resp = (HttpServletResponse) context.getExternalContext().getResponse();
                resp.sendError(HttpServletResponse.SC_NOT_MODIFIED);
                return true;
            }
        }
        return false;
    }

    protected void streamContent(InputStream is, OutputStream os) throws IOException {
        int count = 0;
        byte[] buffer = new byte[4096];
        while ((count = is.read(buffer)) != -1) {
            if (count > 0) {
                os.write(buffer, 0, count);
            }
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    protected String getMimeType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        return (String)mimeTypes.get(extension);
    }
}