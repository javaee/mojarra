/*
 * $Id: BeanList.java,v 1.1 2004/06/09 21:28:17 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

import javax.faces.model.ListDataModel;

import java.util.ArrayList;

public class BeanList extends Object {

    private ListDataModel listDataModel = null;

    public BeanList() {
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
	    ArrayList beans = new ArrayList(10);
	    TestBean curBean = null;
	    
	    for (int i = 0; i < size; i++) {
		curBean = new TestBean();
		curBean.setStringProperty(getName() + " " + i);
		
		beans.add(curBean);
	    }
	    listDataModel = new ListDataModel(beans);

	}
	
	return listDataModel;
    }

    public void setListDataModel(ListDataModel newListDataModel) {
	listDataModel = newListDataModel;
    }

}
