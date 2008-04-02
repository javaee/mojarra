/*
 * $Id: InputBean.java,v 1.4 2006/03/29 22:39:19 rlubke Exp $
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

import javax.faces.component.UIData;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.List;

public class InputBean extends Object {


    protected BeanList list;

    protected String stringProperty;

    /** so we know when to reset our counters */

    protected int max;

    // ------------------------------------------------------------ Constructors


    public InputBean(BeanList list, int max, String stringProperty) {

        this.list = list;
        this.max = max;
        this.stringProperty = stringProperty;

    }

    // ---------------------------------------------------------- Public Methods


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
        } else {
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

    // --------------------------------------------------------- Private Methods


    private int getFlatIndex() {

        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();

        UIData
              outerData = (UIData) root.findComponent(list.getOuterDataName()),
              innerData = (UIData) root.findComponent(list.getInnerDataName());

        int
              outerIndex = outerData.getRowIndex(),
              innerIndex = innerData.getRowIndex(),
              innerRowCount = innerData.getRowCount(),
              index = innerRowCount * outerIndex + innerIndex;

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
