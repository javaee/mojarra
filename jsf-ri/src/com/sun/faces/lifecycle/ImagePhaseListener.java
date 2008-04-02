/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.lifecycle;

import javax.faces.event.PhaseListener;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseEvent;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.faces.util.Util;

/**
 * This <code>PhaseListener</code> is present for
 * the express purpose of avoiding image requests 
 * from going through the full lifecycle.  This generally
 * will happen when the FacesServlet is prefix mapped and
 * image paths are defined using relative URIs.
 * 
 * PENDING: We should technically only insert this 
 *  PhaseListener if we detect that the FacesServlet
 *  for this application is prefix mapped.  
 */
public class ImagePhaseListener implements PhaseListener {


    /**
     * Our <code>Logger</code>.
     */
    private static Logger LOGGER = Util.getLogger(Util.FACES_LOGGER
                                                  + Util.LIFECYCLE_LOGGER);
    
    /**
     * The <code>ServletContext</code> for this Application.
     */
    private ServletContext servletContext;


    // ---------------------------------------------- Methods From PhaseListener


    /**
     * This is a <code>no-op</code>
     * @param phaseEvent PhaseEvent
     */
    public void afterPhase(PhaseEvent phaseEvent) {

        // no-op

    }


    /**
     * <p>If the request that invoked this lifecycle was
     * <code>prefix</code> mapped, check the mime-type of
     * the request path info.  If the mime-type is an image
     * type, then dispatch the request directly to the resource
     * and bypass the remainder of the lifecycle.
     * @param phaseEvent PhaseEvent
     */
    public void beforePhase(PhaseEvent phaseEvent) {

        FacesContext context = phaseEvent.getFacesContext();
        ExternalContext extContext = context.getExternalContext();
        if (servletContext == null) {
            servletContext = (ServletContext) extContext.getContext();
        }
        String pathInfo = extContext.getRequestPathInfo();
        if (pathInfo != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Request is prefix mapped");
            }
            
            // invoked via prefix mapped FacesServlet
            String mimeType = servletContext.getMimeType(pathInfo);            
            if (mimeType != null && mimeType.startsWith("image")) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Image mime type requested: " 
                                + mimeType 
                                + " - bypassing request lifecycle.");
                }
                try {                    
                    context.responseComplete();
                    extContext.dispatch(pathInfo);                    
                } catch (IOException ioe) {
                    throw new FacesException(ioe);
                }
            }          
        }

    }


    /**
     * Invoked during <code>PhaseId.RESTORE_VIEW</code>.
     * @return
     */
    public PhaseId getPhaseId() {

        return PhaseId.RESTORE_VIEW;

    }

} // END ImagePhaseListener