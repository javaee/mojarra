/*
 * $Id: ConstantMethodBinding.java,v 1.8 2006/03/29 23:03:53 rlubke Exp $
 */

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

package com.sun.faces.util;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

public class ConstantMethodBinding extends MethodBinding
    implements StateHolder {

    private String outcome = null;


    public ConstantMethodBinding() {
    }


    public ConstantMethodBinding(String yourOutcome) {
        outcome = yourOutcome;
    }


    public Object invoke(FacesContext context, Object params[]) {
        return outcome;
    }


    public Class getType(FacesContext context) {
        return String.class;
    }

    // ----------------------------------------------------- StateHolder Methods

    public Object saveState(FacesContext context) {
        return outcome;
    }


    public void restoreState(FacesContext context, Object state) {
        outcome = (String) state;
    }


    private boolean transientFlag = false;


    public boolean isTransient() {
        return (this.transientFlag);
    }


    public void setTransient(boolean transientFlag) {
        this.transientFlag = transientFlag;
    }
}
