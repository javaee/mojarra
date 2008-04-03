/*
 * $Id: JsfJsResourcePhaseListener.java,v 1.3 2007/04/27 22:01:00 ofung Exp $
 */
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
                response.addHeader("Cache-Control", "max-age=3600");
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
