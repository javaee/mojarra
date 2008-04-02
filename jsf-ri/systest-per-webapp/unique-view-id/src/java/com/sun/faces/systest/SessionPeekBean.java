/* 
 * $Id: SessionPeekBean.java,v 1.1 2004/06/01 17:06:28 eburns Exp $ 
 */ 


/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.systest;

import javax.faces.context.FacesContext;

import java.util.ArrayList;

public class SessionPeekBean extends Object {

    public String getNumberOfViewsInSession() {
	FacesContext context = FacesContext.getCurrentInstance();
	ArrayList viewList = (ArrayList)
	    context.getExternalContext().getSessionMap().get("com.sun.faces.VIEW_LIST");
	return ((viewList == null) ? "null" : "" + viewList.size());
    }

}
