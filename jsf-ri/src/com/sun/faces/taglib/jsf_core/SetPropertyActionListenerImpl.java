/*
 * $Id: SetPropertyActionListenerImpl.java,v 1.1 2005/07/25 18:40:25 edburns Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
        ActionSource host = (ActionSource) e.getComponent();
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
