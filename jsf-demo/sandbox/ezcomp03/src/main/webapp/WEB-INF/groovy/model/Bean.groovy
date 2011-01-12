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

package model;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

public class Bean implements ActionListener, ValueChangeListener {
    
    private String requestKey = "Hello World!";

    public void setRequestKey(String message) {
        this.requestKey = message;
    }
    
    public String getRequestKey() {
        return requestKey;
    }
    
    public String loginAction() {
        return "login";
    }

    public String backAction() {
        return "back";
    }

    public ActionListener getLoginEventListener() {
        return this;
    }
    
    public ValueChangeListener getUseridValueChangeListener() {
        Bean other = new Bean();
        other.setRequestKey("useridValueChangeListener");
        return ((ValueChangeListener) other);
    }

    public ValueChangeListener getPasswordValueChangeListener() {
        Bean other = new Bean();
        other.setRequestKey("passwordValueChangeListener");
        return ((ValueChangeListener) other);
    }

    public ValueChangeListener getAllInputsListener() {
        Bean other = new Bean();
        other.setRequestKey("allInputsValueChangeListener");
        return ((ValueChangeListener) other);
    }


    public void processValueChange(ValueChangeEvent arg0) throws AbortProcessingException {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().
                put(getRequestKey(), Boolean.TRUE);
    }
    
    

    public void processAction(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().
                put("loginEventCalled", Boolean.TRUE);
        
    }

}
