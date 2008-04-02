/*
 * $Id: SetPropertyActionListenerImpl.java,v 1.5 2006/05/17 19:00:50 rlubke Exp $
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

package com.sun.faces.taglib.jsf_core;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class SetPropertyActionListenerImpl extends Object implements ActionListener, StateHolder {
    
    private ValueExpression targetExpression = null;
    
    private ValueExpression valueExpression = null;
    
    public SetPropertyActionListenerImpl() {}
    
    public SetPropertyActionListenerImpl(ValueExpression target, ValueExpression value) {
        this.targetExpression = target;
        this.valueExpression = value;
    }
    
    public void processAction(ActionEvent e) throws AbortProcessingException {
        ELContext elc = FacesContext.getCurrentInstance().getELContext();
        
        try {
            targetExpression.setValue(elc, valueExpression.getValue(elc));
        } catch (ELException ele) {
            // PENDING logging
        }
    }
    
    public void setTransient(boolean newTransientValue) {}
    
    public boolean isTransient() { return false; }
    
    public Object saveState(FacesContext context) {
        Object [] state = new Object[2];
        state[0] = targetExpression;
        state[1] = valueExpression;
        return state;
    }
    
    public void restoreState(FacesContext context, Object state) {
        Object [] stateArray = (Object []) state;
        targetExpression = (ValueExpression) stateArray[0];
        valueExpression = (ValueExpression) stateArray[1];
    }
    
}
