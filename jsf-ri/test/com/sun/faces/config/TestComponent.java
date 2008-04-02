/*
 * $Id: TestComponent.java,v 1.5 2004/02/26 20:34:15 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;


import javax.faces.component.UIOutput;


// Dummy component that can be instantiated

public class TestComponent extends UIOutput {


    public String getFamily() {
        return "Test";
    }

}
