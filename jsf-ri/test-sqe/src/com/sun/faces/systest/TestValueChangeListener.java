/*
 * $Id: TestValueChangeListener.java,v 1.1 2005/07/25 18:34:28 rajprem Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.component.StateHolder;
import javax.faces.event.AbortProcessingException;

public class TestValueChangeListener implements ValueChangeListener {
    
    public TestValueChangeListener() {}

    public void processValueChange(ValueChangeEvent vce)
    throws AbortProcessingException {
       vce.getComponent().getAttributes().put("onblur",
                                               vce.getNewValue().toString());
    } 
    
    
}
