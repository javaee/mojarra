/*
 * $Id: ReplaceApplicationBean.java,v 1.1 2005/03/18 22:12:51 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */



package com.sun.faces.systest;

import javax.faces.context.FacesContext;

public class ReplaceApplicationBean {

    public String getStateManagerClass() {
	FacesContext context = FacesContext.getCurrentInstance();
	return context.getApplication().getStateManager().toString();
    }

    public String getViewHandlerClass() {
	FacesContext context = FacesContext.getCurrentInstance();
	return context.getApplication().getViewHandler().toString();
    }

    public String getApplicationClass() {
	FacesContext context = FacesContext.getCurrentInstance();
	return context.getApplication().toString();
    }

}
