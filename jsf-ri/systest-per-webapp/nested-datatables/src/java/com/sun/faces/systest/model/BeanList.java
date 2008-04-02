/*
 * $Id: BeanList.java,v 1.5 2006/03/29 23:04:28 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
