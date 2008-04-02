/*
 * $Id: ForEachBean.java,v 1.3 2004/02/06 18:56:11 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
