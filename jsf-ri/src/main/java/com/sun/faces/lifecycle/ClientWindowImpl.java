/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.lifecycle;

import java.util.Map;
import java.util.regex.Pattern;
import javax.faces.component.UINamingContainer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.ClientWindow;
import javax.faces.render.ResponseStateManager;
import javax.faces.FacesException;

public class ClientWindowImpl extends ClientWindow {
    
    String id;

    public ClientWindowImpl() {
    }

    @Override
    public Map<String, String> getQueryURLParameters(FacesContext context) {
        return null;
    }
    
    

    @Override
    public void decode(FacesContext context) {
        Map<String, String> requestParamMap = context.getExternalContext().getRequestParameterMap();
        if (isClientWindowRenderModeEnabled(context)) {
            id = requestParamMap.get(ResponseStateManager.CLIENT_WINDOW_URL_PARAM);
        }
        // The hidden field always takes precedence, if present.
        if (requestParamMap.containsKey(ResponseStateManager.CLIENT_WINDOW_PARAM)) {
            id = requestParamMap.get(ResponseStateManager.CLIENT_WINDOW_PARAM);
            Pattern safePattern = Pattern.compile(".*<(.*:script|script).*>[^&]*</\\s*\\1\\s*>.*");
            if (safePattern.matcher(id).matches()) {
                throw new FacesException("ClientWindow is illegal: " + id);
            }
        }
        if (null == id) {
            id = calculateClientWindow(context);
        }
    }
    
    private String calculateClientWindow(FacesContext context) {
        synchronized(context.getExternalContext().getSession(true)) {
            final String clientWindowCounterKey = "com.sun.faces.lifecycle.ClientWindowCounterKey";
            ExternalContext extContext = context.getExternalContext();
            Map<String, Object> sessionAttrs = extContext.getSessionMap();
            Integer counter = (Integer) sessionAttrs.get(clientWindowCounterKey);
            if (null == counter) {
                counter = Integer.valueOf(0);
            }
            char sep = UINamingContainer.getSeparatorChar(context);
            id = extContext.getSessionId(true) + sep +
                    + counter;

            sessionAttrs.put(clientWindowCounterKey, ++counter);
        }
        return id;
    }

    @Override
    public String getId() {
        return id;
    }
}
