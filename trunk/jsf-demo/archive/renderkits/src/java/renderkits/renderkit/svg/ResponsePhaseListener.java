/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package renderkits.renderkit.svg;

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
