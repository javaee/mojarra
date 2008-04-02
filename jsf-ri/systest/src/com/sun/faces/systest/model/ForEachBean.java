/*
 * $Id: ForEachBean.java,v 1.6 2006/03/29 22:38:52 rlubke Exp $
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


/** <p>Test JavaBean for <code>&lt;c:forEach&gt;</code> testing.</p> */

public class ForEachBean {


    // String List property with pre-initialized values
    List listProperty = new ArrayList();


    // String Array property with pre-initialized values
    private String arrayProperty[] = {
          "First String",
          "Second String",
          "Third String",
          "Fourth String",
          "Fifth String"
    };


    // Scalar string property
    private String stringProperty = "This is a String property";

    // ---------------------------------------------------------- Public Methods


    public void setArrayProperty(String arrayProperty[]) {

        this.arrayProperty = arrayProperty;

    }


    public void setListProperty(List listProperty) {

        this.listProperty = listProperty;

    }


    public void setStringProperty(String stringProperty) {

        this.stringProperty = stringProperty;

    }


    public String[] getArrayProperty() {

        return (arrayProperty);

    }


    public List getListProperty() {

        return (listProperty);

    }


    public String getStringProperty() {

        return (this.stringProperty);

    }

    // ------------------------------------------------- Package Private Methods


    {
        listProperty.add("List First");
        listProperty.add("List Second");
        listProperty.add("List Third");
        listProperty.add("List Fourth");
        listProperty.add("List Fifth");
    }

}
