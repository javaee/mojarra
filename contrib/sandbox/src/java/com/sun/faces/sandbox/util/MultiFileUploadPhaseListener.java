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
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sun.faces.sandbox.component.MultiFileUpload;
import com.sun.faces.sandbox.model.FileHolder;


/**
 * @author Jason Lee
 *
 */
public class MultiFileUploadPhaseListener implements PhaseListener {
    private static final long serialVersionUID = 1L;

    // TODO:  let the StaticResourcePhaseListener handle these 
    public void beforePhase(PhaseEvent e) {
        PhaseId phase = e.getPhaseId();
        if (phase == PhaseId.RESTORE_VIEW) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String uri = request.getRequestURI();
            if ((uri != null) && (uri.indexOf(MultiFileUpload.JARS_URI) > -1)){
                InputStream is = null;
                try {
                    String fileName = uri.substring(uri.lastIndexOf(MultiFileUpload.JARS_URI))
                        .substring(MultiFileUpload.JARS_URI.length());
                    int index = fileName.indexOf(".jar");
                    fileName = "/META-INF/static/" + fileName.substring(0, index+4);
                    HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                    is = getClass().getResourceAsStream(fileName);
                    OutputStream os = response.getOutputStream();

                    response.setContentType("application/x-java-applet");
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                    int count = 0;
                    byte[] buffer = new byte[4096];
                    while ((count = is.read(buffer)) != -1) {
                        if (count > 0) {
                            os.write(buffer, 0, count);
                        }
                    }
                    is.close();
                    os.flush();
                    context.responseComplete();
                } catch (IOException ioe) {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Exception ex) {
                            //
                        }
                    }
                } catch (Throwable t) {
                    System.err.println(t.getMessage());
                }
            }
        }
    }

    public void afterPhase(PhaseEvent e) {
        PhaseId phase = e.getPhaseId();
        if (phase == PhaseId.RESTORE_VIEW) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String uri = request.getRequestURI();
            if ((uri != null) && (uri.indexOf(MultiFileUpload.UPLOAD_URI) > -1)){
                HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                String clientId = request.getParameter(MultiFileUpload.REQUEST_PARAM);
                Object obj = request.getSession().getAttribute("HtmlMultiFileUpload-" + clientId);

                MultiFileUpload comp = (MultiFileUpload) obj;
                DiskFileItemFactory factory = new DiskFileItemFactory();
                int maxFileSize = 0;
                try {
                    maxFileSize = Integer.parseInt(comp.getMaxFileSize());
                } catch (Exception nfe) {
                    //
                }
                factory.setSizeThreshold(maxFileSize);
                ServletFileUpload upload = new ServletFileUpload(factory);

                try {
                    List items = upload.parseRequest(request);  // Get the FileItems from the request
                    FileHolder fileHolder = comp.getFileHolder();
                    if (fileHolder != null) {
                        for (int i = 0; i < items.size(); i++) {
                            FileItem item = (FileItem)items.get(i);
                            fileHolder.addFile(item.getName(), item.getInputStream());
                        }
                    }
                    String destinationUrl = comp.getDestinationUrl();
                    // If we have a complete URL, just run with that
                    if (!destinationUrl.startsWith("http")) {
                        // If we get just a view name, append the / so the API call below
                        // will be happy
                        if (destinationUrl.charAt(0) != '/') {
                            destinationUrl = "/" + destinationUrl;
                        }
                        // Generate the complete URL.
                        // NOTE:  This does *NOT* currently resolve a navigation case result string
                        // to the mapped view from the Faces configuration.  This assumes that the string
                        // in question is the *name* of a view, so "success" will become
                        // http://localhost:8080/MyContext/success.jsf, not
                        // http://localhost:8080/MyContext/youdidit.jsf that you may have mapped in
                        // your Faces config.  This is due to the lack of a portable way of making
                        // that resolution.
                        // TODO:  Fix this
                            
                        // Just pass back /AppContext/foo.jsf (or /AppContext/faces/foo.xhtml, etc)
                        // The applet will construct the full URL based on
                        // documentBase
                        destinationUrl = response.encodeURL(context.getApplication().getViewHandler().getActionURL(context, destinationUrl));
                    }
                    response.setContentType("text/text");
                    response.getOutputStream().write(destinationUrl.getBytes());
                    context.responseComplete();
                } catch (Exception e1) {
                    // TODO:  Determine the configured error page and go there?
                    // Return a 500 to the client?
                    e1.printStackTrace();
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.faces.event.PhaseListener#getPhaseId()
     */
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
