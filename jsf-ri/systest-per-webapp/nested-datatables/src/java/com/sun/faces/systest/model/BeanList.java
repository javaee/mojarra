/*
 * $Id: BeanList.java,v 1.4 2006/03/29 22:39:19 rlubke Exp $
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

import java.util.ArrayList;
import java.util.List;

public class BeanList extends Object {


    protected List inputValues = null;

    protected String innerDataName;


    protected String name = "name";

    protected String outerDataName;

    protected int size = 10;

    private ListDataModel listDataModel = null;

    // ------------------------------------------------------------ Constructors


    public BeanList() {
    }

    // ---------------------------------------------------------- Public Methods


    public String getInnerDataName() {

        return innerDataName;

    }


    public void setInnerDataName(String newInnerDataName) {

        innerDataName = newInnerDataName;

    }


    public List getInputValues() {

        return inputValues;

    }


    public void setInputValues(List newInputValues) {

        inputValues = newInputValues;

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


    public String getName() {

        return name;

    }


    public void setName(String newName) {

        if (null == newName) {
            return;
        }
        name = newName;

    }


    public String getOuterDataName() {

        return outerDataName;

    }


    public void setOuterDataName(String newOuterDataName) {

        outerDataName = newOuterDataName;

    }


    public int getSize() {

        return size;

    }


    public void setSize(int newSize) {

        size = newSize;

    }

}
