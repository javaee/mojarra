/*
 * $Id: ConstantMethodBinding.java,v 1.2 2003/12/17 15:14:21 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.util;

import javax.faces.el.MethodBinding;

import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;

public class ConstantMethodBinding extends MethodBinding implements StateHolder {

    private String outcome = null;

    public ConstantMethodBinding() {}

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
