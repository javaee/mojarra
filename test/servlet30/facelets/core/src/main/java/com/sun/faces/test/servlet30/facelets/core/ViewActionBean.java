/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.servlet30.facelets.core;

import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

@RequestScoped
@ManagedBean
public class ViewActionBean {

    @ManagedProperty(value = "#{facesContext}")
    private FacesContext context;

    public String action() {
        return "viewActionResult";
    }

    public String pageA() {
        getContext().getExternalContext().getRequestMap().put("message", "pageA action");
        return "pageA";
    }

    public String pageAExplicitRedirect() {
        getContext().getExternalContext().getRequestMap().put("message", "pageA explicit redirect");
        return "pageAExplicitRedirect";
    }

    public String returnEmpty() {
        getContext().getExternalContext().getRequestMap().put("message", "pageA empty");
        return "";
    }

    public String returnNull() {
        getContext().getExternalContext().getRequestMap().put("message", "pageA null");
        return null;
    }

    public FacesContext getContext() {
        return context;
    }

    public void setContext(FacesContext context) {
        this.context = context;
    }

    private static class ActionListenerImpl implements ActionListener {

        private final String id;

        private ActionListenerImpl(String id) {
            this.id = id;
        }

        @Override
        public void processAction(ActionEvent event) throws AbortProcessingException {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();

            String message = (String) sessionMap.get("message");
            message = (null == message) ? "" : message + " ";
            message = message + id + " " + event.getComponent().getId();
            sessionMap.put("message", message);
        }
    }

    public ActionListener getActionListener1() {
        return new ActionListenerImpl("1");
    }

    public ActionListener getActionListener2() {
        return new ActionListenerImpl("2");
    }

    public void actionListenerMethod(ActionEvent event) {
        ActionListenerImpl actionListener = new ActionListenerImpl("method");
        actionListener.processAction(event);
    }
}
