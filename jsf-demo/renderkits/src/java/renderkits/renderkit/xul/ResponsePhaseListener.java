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
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package renderkits.renderkit.xul;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * This <code>PhaseListener</code> executes after Invoke Applications
 * Phase of the request processing lifecycle.  It detects the presence
 * of an <code>XMLHttpRequest</code> and:
 * <ul><li>grabs the request URI from the request (this will be the
 * URI of the new uocoming view) and adds it to the response;</li>
 * </ul>
 */
public class ResponsePhaseListener implements PhaseListener {

    private static final String XML_HTTP = "XML-HTTP";
    private static final String VIEW_URI = "VIEW-URI";

    public ResponsePhaseListener() {
    }

    public void afterPhase(PhaseEvent event) {
        // Disregard requests that are not XMLHttpRequest(s) 
        Map<String, String> requestHeaderMap =
              event.getFacesContext().getExternalContext().
                    getRequestHeaderMap();
        if (requestHeaderMap.get(XML_HTTP) == null) {
            return;
        }
        // If we're dealing with an XMLHttpRequest...
        // Get the URI and stuff it in the response header.
        FacesContext context = event.getFacesContext();
        String viewId = context.getViewRoot().getViewId();
        String actionURL = context.getApplication().getViewHandler()
              .getActionURL(context, viewId);
        HttpServletResponse response =
              (HttpServletResponse) context.getExternalContext().getResponse();
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader(VIEW_URI, actionURL);
    }

    public void beforePhase(PhaseEvent event) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.INVOKE_APPLICATION;
    }
}
