/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.model;

import java.util.Map;
import javax.annotation.PreDestroy;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostKeepFlashValueEvent;
import javax.faces.event.PostPutFlashValueEvent;
import javax.faces.event.PreClearFlashEvent;
import javax.faces.event.PreRemoveFlashValueEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class SessionScopedBean implements Serializable {
    
    private FlashListener listener;
    
    public SessionScopedBean() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        listener = new FlashListener();
        
        app.subscribeToEvent(PreRemoveFlashValueEvent.class, listener);
        app.subscribeToEvent(PostKeepFlashValueEvent.class, listener);
        app.subscribeToEvent(PreClearFlashEvent.class, listener);
        app.subscribeToEvent(PostPutFlashValueEvent.class, listener);
        
    }

    @PreDestroy
    public void destroy() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        
        app.unsubscribeFromEvent(PreRemoveFlashValueEvent.class, listener);
        app.unsubscribeFromEvent(PostKeepFlashValueEvent.class, listener);
        app.unsubscribeFromEvent(PreClearFlashEvent.class, listener);
        app.unsubscribeFromEvent(PostPutFlashValueEvent.class, listener);
        
    }
    
    public String getMessage() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        String result = (sessionMap.containsKey("builder")) ? ((StringBuilder)sessionMap.get("builder")).toString() : "no message";
        
        return result;
    }
    
    public static class FlashListener implements SystemEventListener {

        public boolean isListenerForSource(Object source) {
            return ((source instanceof String) || (source instanceof Map));
        }

        public void processEvent(SystemEvent event) throws AbortProcessingException {
            appendMessage("[received " + event.getClass().getName() + " source:"
                          + event.getSource() + "]");
            
        }
        
        private void appendMessage(String message) {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            StringBuilder builder;
            builder = (StringBuilder) sessionMap.get("builder");
            if (null == builder) {
                builder = new StringBuilder();
                sessionMap.put("builder", builder);
            }
            builder.append(message);
        }
        
        
        
    }
    
}
