/*
 * $Id: InputBean.java,v 1.1 2004/06/16 20:24:25 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

import javax.faces.component.UIData;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import java.util.List;
import java.util.ArrayList;

public class InputBean extends Object {

    /**
     *so we know when to reset our counters
     */

    protected int max;

    protected BeanList list;

    public InputBean(BeanList list, int max, String stringProperty) {
	this.list = list;
	this.max = max;
	this.stringProperty = stringProperty;
    }

    protected String stringProperty;
    public String getStringProperty() {
	String result = null;
	if (null != stringProperty) {
	    result = stringProperty;
	    return result;
	}

	FacesContext context = FacesContext.getCurrentInstance();
	int 
	    index = getFlatIndex();
	List inputValues = null;	
	
	if (null == (inputValues = list.getInputValues())) {
	    result = null;
	}
	else {
	    result = (String) inputValues.get(index);
	}

	return result;
    }

    public void setStringProperty(String newStringProperty) {
	FacesContext context = FacesContext.getCurrentInstance();
	int 
	    size = getFlatSize(),
	    index = getFlatIndex();
	List inputValues = null;	

	if (null == (inputValues = list.getInputValues())) {
	    list.setInputValues(inputValues = new ArrayList(size));
	    for (int i = 0; i < size; i++) {
		inputValues.add(new Object());
	    }
	}
	
	inputValues.set(index, newStringProperty);
	this.stringProperty = null;
    }

    private int getFlatIndex() {
	FacesContext context = FacesContext.getCurrentInstance();
	UIViewRoot root = context.getViewRoot();
	
	UIData 
	    outerData = (UIData) root.findComponent(list.getOuterDataName()),
	    innerData = (UIData) root.findComponent(list.getInnerDataName());
	
	int 
	    index = outerData.getRowCount() * outerData.getRowIndex() + 
	    innerData.getRowIndex();

	return index;
    }

    private int getFlatSize() {
	FacesContext context = FacesContext.getCurrentInstance();
	UIViewRoot root = context.getViewRoot();
	
	UIData 
	    outerData = (UIData) root.findComponent(list.getOuterDataName()),
	    innerData = (UIData) root.findComponent(list.getInnerDataName());
	
	int 
	    size = outerData.getRowCount() * innerData.getRowCount();
	return size;
    }


}
