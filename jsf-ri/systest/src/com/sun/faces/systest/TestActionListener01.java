/*
 * $Id: TestActionListener01.java,v 1.1 2004/12/02 18:42:26 rogerk Exp $
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
import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.component.StateHolder;
import javax.faces.event.AbortProcessingException;

public class TestActionListener01 implements ActionListener {
    
    public TestActionListener01() {}

    public void processAction(ActionEvent ae)
        throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(ae.getComponent().getClientId(context),
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            	ae.getComponent().getId() + " was pressed", null));
    } 
    
    
}
