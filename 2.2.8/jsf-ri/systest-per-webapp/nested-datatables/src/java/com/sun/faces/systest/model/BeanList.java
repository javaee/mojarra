/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
