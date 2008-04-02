/*
 * $Id: BeanList.java,v 1.2 2004/06/16 20:00:08 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

import javax.faces.model.ListDataModel;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;

import java.util.List;
import java.util.ArrayList;

public class BeanList extends Object {

    private ListDataModel listDataModel = null;

    public BeanList() {
    }

    protected String outerDataName;
    public String getOuterDataName() {
	return outerDataName;
    }

    public void setOuterDataName(String newOuterDataName) {
	outerDataName = newOuterDataName;
    }

    protected String innerDataName;
    public String getInnerDataName() {
	return innerDataName;
    }

    public void setInnerDataName(String newInnerDataName) {
	innerDataName = newInnerDataName;
    }


    protected String name = "name";
    public String getName() {
	return name;
    }

    public void setName(String newName) {
	if (null == newName) {
	    return;
	}
	name = newName;
    }

    protected int size = 10;
    public int getSize() {
	return size;
    }

    public void setSize(int newSize) {
	size = newSize;
    }

    public ListDataModel getListDataModel() {
	if (null == listDataModel) {
	    ArrayList beans = new ArrayList(size);
	    InputBean curBean = null;
	    
	    for (int i = 0; i < size; i++) {
		curBean = new InputBean(this, size, getName() + " " + i);
		
		beans.add(curBean);
	    }
	    listDataModel = new ListDataModel(beans);

	}
	
	return listDataModel;
    }

    public void setListDataModel(ListDataModel newListDataModel) {
	listDataModel = newListDataModel;
    }

    protected List inputValues = null;
    public List getInputValues() {
	return inputValues;
    }

    public void setInputValues(List newInputValues) {
	inputValues = newInputValues;
    }


}
