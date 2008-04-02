/*
 * $Id: TestValueChangeListener01.java,v 1.1 2004/12/02 18:42:26 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.component.StateHolder;
import javax.faces.event.AbortProcessingException;

public class TestValueChangeListener01 implements ValueChangeListener {
    
    public TestValueChangeListener01() {}

    public void processValueChange(ValueChangeEvent vce)
        throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(vce.getComponent().getClientId(context),
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                vce.getComponent().getId() + " value was changed", null));
    } 
    
    
}
