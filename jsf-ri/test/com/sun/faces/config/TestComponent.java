/*
 * $Id: TestComponent.java,v 1.2 2004/01/27 21:05:55 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;


import javax.faces.component.UIOutput;


// Dummy component that can be instantiated
public class TestComponent extends UIOutput {


    public String getFamily() { return "Test"; }

}
