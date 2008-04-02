/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

import javax.faces.component.UIComponentBase;

public class TestComponent extends UIComponentBase {

    public String getFamily() {
        return "TestFamily";
    }


    public String getComponentType() {
        return "TestComponent";
    }

}
