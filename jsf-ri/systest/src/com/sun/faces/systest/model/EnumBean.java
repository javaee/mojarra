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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.systest.model;

import java.util.List;
import java.util.ArrayList;

public class EnumBean {

    public enum Simple {
        Value1,
        Value2,
        Value3,
        Value4
    }

    private Simple selected;
    private Simple selected2;
    private Simple selected3;
    private Simple[] mSelected;
    public List<Simple> lSelected;

    public EnumBean() {
        selected = Simple.Value2;
        selected2 = Simple.Value3;
        selected3 = Simple.Value4;
        mSelected = new Simple[] { Simple.Value1, Simple.Value3 };
        lSelected = new ArrayList<Simple>(4);
        lSelected.add(Simple.Value2);
        lSelected.add(Simple.Value4);
    }   

    public Simple getSelected() {
        return selected;
    }

    public void setSelected(Simple selected) {
        this.selected = selected;
    }
    
     public Simple getSelected2() {
        return selected2;
    }

    public void setSelected2(Simple selected2) {
        this.selected2 = selected2;
    }


    public Simple getSelected3() {
        return selected3;
    }

    public void setSelected3(Simple selected3) {
        this.selected3 = selected3;
    }

    public Simple[] getSelectedArray() {
        return mSelected;
    }
    
    public void setSelectedArray(Simple[] mSelected) {
        this.mSelected = mSelected;
    }
    
    public List getSelectedList() {
        return lSelected;
    }
    
    public void setSelectedList(List<Simple> lSelected) {
        this.lSelected = lSelected;
    }


} // END EnumBean