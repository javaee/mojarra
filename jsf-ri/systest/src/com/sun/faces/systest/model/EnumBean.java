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
