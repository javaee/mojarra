/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.test.servlet30.el;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

@ManagedBean(name = "valueBindingSetBean")
@RequestScoped
public class ValueBindingSetBean {

    private String value1;

    private String value2 = "two";

    private String value3;

    private String value4 = "four";

    public String getTest1() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueBinding valueBinding = facesContext.getApplication().createValueBinding("#{valueBindingSetBean.value1}");
        valueBinding.setValue(facesContext, "one");
        ValueBindingSetBean bean = (ValueBindingSetBean) facesContext.getExternalContext().getRequestMap().get("valueBindingSetBean");
        return bean.getValue1();
    }

    public String getTest2() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueBinding valueBinding = facesContext.getApplication().createValueBinding("#{valueBindingSetBean.value2}");
        valueBinding.setValue(facesContext, null);
        ValueBindingSetBean bean = (ValueBindingSetBean) facesContext.getExternalContext().getRequestMap().get("valueBindingSetBean");
        String value = bean.getValue2();
        if (value == null || value.trim().equals("")) {
            value = "NULL";
        }
        return value;
    }
    
    public String getTest3() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueBinding valueBinding = facesContext.getApplication().createValueBinding("#{requestScope['valueBindingSetBean'].value3}");
        valueBinding.setValue(facesContext, "three");
        ValueBindingSetBean bean = (ValueBindingSetBean) facesContext.getExternalContext().getRequestMap().get("valueBindingSetBean");
        return bean.getValue3();
    }
    
    public String getTest4() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueBinding valueBinding = facesContext.getApplication().createValueBinding("#{requestScope['valueBindingSetBean'].value4}");
        valueBinding.setValue(facesContext, null);
        ValueBindingSetBean bean = (ValueBindingSetBean) facesContext.getExternalContext().getRequestMap().get("valueBindingSetBean");
        String value = bean.getValue4();
        if (value == null || value.trim().equals("")) {
            value = "NULL";
        }
        return value;
    }
    
    public String getTest5() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueBinding valueBinding = facesContext.getApplication().createValueBinding("#{value5}");
        valueBinding.setValue(facesContext, "five");
        return (String) facesContext.getExternalContext().getRequestMap().get("value5");
    }
    
    public String getTest6() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().getRequestMap().put("value6", "five");
        ValueBinding valueBinding = facesContext.getApplication().createValueBinding("#{value6}");
        valueBinding.setValue(facesContext, "six");
        return (String) facesContext.getExternalContext().getRequestMap().get("value6");
    }
    
    public String getValue1() {
        return value1;
    }
    
    public String getValue2() {
        return value2;
    }
    
    public String getValue3() {
        return value3;
    }
    
    public String getValue4() {
        return value4;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }
}
