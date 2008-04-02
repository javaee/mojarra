/*
 * $Id: ValueChangeListenerBean.java,v 1.1 2004/06/02 13:57:46 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

import javax.faces.event.ValueChangeEvent;
import javax.faces.event.AbortProcessingException;


public class ValueChangeListenerBean extends Object {

    public ValueChangeListenerBean() {
	System.out.println("debug: edburns: ValueChangeListenerBean ctor");
    }

    protected String textAResult;
    public String getTextAResult() {
	return textAResult;
    }

    public void setTextAResult(String newTextAResult) {
	textAResult = newTextAResult;
    }

    protected String textBResult;
    public String getTextBResult() {
	return textBResult;
    }

    public void setTextBResult(String newTextBResult) {
	textBResult = newTextBResult;
    }
    
    public void textAChanged(ValueChangeEvent event) throws AbortProcessingException {
	setTextAResult("Received valueChangeEvent for textA: " + 
		       event.hashCode());
    }

    public void textBChanged(ValueChangeEvent event) throws AbortProcessingException {
	setTextBResult("Received valueChangeEvent for textB: " + 
		       event.hashCode());
    }
}
