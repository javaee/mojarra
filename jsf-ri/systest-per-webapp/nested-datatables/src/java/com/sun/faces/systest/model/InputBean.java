/*
 * $Id: InputBean.java,v 1.6 2007/04/27 22:01:54 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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
