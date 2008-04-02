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
 * [JsfJsResourcePhaseListener] [$Id: JsfJsResourcePhaseListener.java,v 1.1 2006/08/30 17:42:50 rlubke Exp $]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.renderkit;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.sun.faces.RIConstants;
import com.sun.faces.util.MessageUtils;

/**
 * This PhaseListener supports the externalization of the JavaScript
 * (https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=403).  In
 * beforePhase(), the request URI is examined to see if contains the configured
 * URI for the JavaScript file.  If found, it calls RenderKitUtils.writeSunJs()
 * to write the scrip to the client, then marks the response as complete.  This
 * does not currently work in a Portlet environment, so this feature will need
 * to be turned off via the context init parameter.
 *
 * @author Jason D. Lee (jdlee at dev dot java not net)
 */
public class JsfJsResourcePhaseListener implements PhaseListener {


    private static final long serialVersionUID = 1L;


    // ---------------------------------------------- Methods From PhaseListener

    
    public void afterPhase(PhaseEvent event) {
        //
    }


    public void beforePhase(PhaseEvent event) {

        FacesContext context = event.getFacesContext();
        Object obj = context.getExternalContext().getRequest();
        // PENDING
        // We need to look into how to make this work in a portlet environment.
        // For the time being, this feature will need to be disabled when running
        // in a portlet.
        if (obj instanceof HttpServletRequest) {         
            HttpServletRequest request = (HttpServletRequest) obj;
            if (request.getRequestURI().contains(RIConstants.SUN_JSF_JS_URI)) {
                HttpServletResponse response = (HttpServletResponse) context
                      .getExternalContext().getResponse();
                response.setContentType("text/javascript");
                try {
                    RenderKitUtils.writeSunJS(context, response.getWriter());
                } catch (IOException ioe) {
                    throw new FacesException(
                          MessageUtils.getExceptionMessageString(MessageUtils.JS_RESOURCE_WRITING_ERROR_ID),
                          ioe);
                }
                context.responseComplete();
            }
        }
    }


    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
