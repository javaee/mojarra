/*
 * $Id: ForEachBean.java,v 1.2 2005/08/22 22:11:39 ofung Exp $
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


import java.util.ArrayList;
import java.util.List;


/**
 * <p>Test JavaBean for <code>&lt;c:forEach&gt;</code> testing.</p>
 */

public class ForEachBean {


    // String Array property with pre-initialized values
    private String arrayProperty[] = {
        "First String",
        "Second String",
        "Third String",
        "Fourth String",
        "Fifth String"
    };


    public String[] getArrayProperty() {
        return (arrayProperty);
    }


    public void setArrayProperty(String arrayProperty[]) {
        this.arrayProperty = arrayProperty;
    }


    // String List property with pre-initialized values
    List listProperty = new ArrayList();


    {
        listProperty.add("List First");
        listProperty.add("List Second");
        listProperty.add("List Third");
        listProperty.add("List Fourth");
        listProperty.add("List Fifth");
    }


    public List getListProperty() {
        return (listProperty);
    }


    public void setListProperty(List listProperty) {
        this.listProperty = listProperty;
    }


    // Scalar string property
    private String stringProperty = "This is a String property";


    public String getStringProperty() {
        return (this.stringProperty);
    }


    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }


}
