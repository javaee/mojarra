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

package com.sun.faces.test.agnostic.ajax;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "issue2500Bean")
@SessionScoped
public class Issue2500Bean {

    
    public Issue2500Bean() {
    }

    private boolean visible = false;

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getVisible() {
        return visible;
    }

    public void toggle(ActionEvent ae) {
        if (visible) {
            visible = false;
        } else if (!visible) {
            visible = true;
        }

        determineViewStateValueOccurences();
    }

    public String msg = null;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    private void determineViewStateValueOccurences() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map <String, String[]>requestParams = context.getExternalContext().getRequestParameterValuesMap();
        Set s = requestParams.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry)it.next();
            String key = (String)m.getKey();
            String[] values = (String[])m.getValue();
            if (key.equals("javax.faces.ViewState")) {
                if (null != values) {
                    if (values.length > 1) {
                        msg = "javax.faces.ViewState Has More than One Value";
                    } else if (values.length == 1) {
                        msg = "javax.faces.ViewState Has One Value";
                    }
                } 
               break;
           }
        }
    } 
}
