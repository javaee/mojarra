/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.agnostic.ajax;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "issue2648Bean")
@SessionScoped
public class Issue2648Bean implements Serializable {

    public void processAjax(AjaxBehaviorEvent event) throws AbortProcessingException {
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExternalContext ectx = fctx.getExternalContext();
        HttpSession session = (HttpSession)ectx.getSession(true);
        String temp1 = session.getServletContext().getContextPath() + "/faces/issue2648-1.xhtml";
        String temp2 = session.getServletContext().getContextPath() + "/faces/issue2648-2.xhtml";
        try {
            boolean complete1 = fctx.getResponseComplete(); 
            ectx.redirect(temp1); 
            boolean complete2 = fctx.getResponseComplete(); 
            ectx.redirect(temp2); 
            boolean complete3 = fctx.getResponseComplete(); 
        } catch (Exception e) {
            if (e instanceof IllegalStateException) {
                session.setAttribute("IllegalStateException", "true");
            }
        }
    }

    public void process(ActionEvent event) {
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExternalContext ectx = fctx.getExternalContext();
        HttpSession session = (HttpSession)ectx.getSession(true);
        String temp1 = session.getServletContext().getContextPath() + "/faces/issue2648-1.xhtml";
        String temp2 = session.getServletContext().getContextPath() + "/faces/issue2648-2.xhtml";
        try {
            boolean complete1 = fctx.getResponseComplete(); 
            ectx.redirect(temp1); 
            boolean complete2 = fctx.getResponseComplete(); 
            ectx.redirect(temp2); 
            boolean complete3 = fctx.getResponseComplete(); 
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
