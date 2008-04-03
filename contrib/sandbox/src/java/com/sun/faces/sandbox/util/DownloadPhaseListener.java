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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.faces.sandbox.component.FileDownload;

/**
 * @author Jason Lee
 *
 */
//TODO:  Can this be merged with the StaticResourcePhaseListener?
public class DownloadPhaseListener implements PhaseListener {
    private static final long serialVersionUID = 1L;
    private transient Logger logger = Logger.getLogger(DownloadPhaseListener.class.getName());

    /* (non-Javadoc)
     * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    public void beforePhase(PhaseEvent e) {
        PhaseId phase = e.getPhaseId();
        if (phase == PhaseId.RENDER_RESPONSE) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String uri = request.getRequestURI();
            if ((uri != null) && (uri.indexOf(FileDownload.DOWNLOAD_URI) > -1)){
                try {
                    HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                    String clientId = request.getParameter(FileDownload.REQUEST_PARAM);
                    FileDownload comp = (FileDownload)request.getSession().getAttribute("HtmlDownload-" + clientId);
                    if (comp != null) {
                        Object value = comp.getData();
                        if (value != null) {
                            byte[] data = null;
                            if (value instanceof byte[]) {
                                data = (byte[]) value;
                            } else if (value instanceof ByteArrayOutputStream) {
                                data = ((ByteArrayOutputStream) value).toByteArray();
                            } else if (value instanceof InputStream) {
                                data = getBytes((InputStream)value);
                            } else {
                                throw new FacesException("HtmlDownload:  an unsupported data type was found:  " +
                                        value.getClass().getName());
                            }
                            String mimeType = comp.getMimeType();
                            if (FileDownload.METHOD_DOWNLOAD.equals(comp.getMethod())) {
                                response.setHeader("Content-Disposition", "attachment; filename=\"" +
                                        comp.getFileName() + "\"");
                            } else {
                                response.setHeader("Content-Disposition", "inline; filename=\"" +
                                        comp.getFileName() + "\"");                        }
                            response.setContentType(mimeType);
                            ServletOutputStream sos = response.getOutputStream();
                            sos.write(data);
                            sos.flush();
                            context.responseComplete();
                        }
                    }
                } catch (IOException e1) {
                    logger.severe(e1.getMessage());
                    e1.printStackTrace();
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     */
    public void afterPhase(PhaseEvent e) {
        // nothing to do here
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    protected  byte[] getBytes(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = is.read(buffer)) != -1) {
                if (count > 0) {
                    baos.write(buffer, 0, count);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return baos.toByteArray();
    }

}
