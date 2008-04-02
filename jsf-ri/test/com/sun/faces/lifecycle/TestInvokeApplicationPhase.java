/*
 * $Id: TestInvokeApplicationPhase.java,v 1.21 2004/02/04 23:44:33 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestInvokeApplicationPhase.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;

import com.sun.faces.util.Util;


import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.event.FacesEvent;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.lifecycle.Phase;
import com.sun.faces.RIConstants;
import java.io.IOException;

import java.util.Iterator;

/**
 *
 *  <B>TestInvokeApplicationPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestInvokeApplicationPhase.java,v 1.21 2004/02/04 23:44:33 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestInvokeApplicationPhase extends ServletFacesTestCase
{
//
// Protected Constants
//

public static final String DID_COMMAND = "didCommand";
public static final String DID_FORM = "didForm";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestInvokeApplicationPhase() {
	super("TestInvokeApplicationPhase");
    }

    public TestInvokeApplicationPhase(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//

public void testInvokeNormal()
{
}

public void testInvokeNoOp()
{
    UIInput root = new UIInput();
    UIViewRoot page = new UIViewRoot();
    page.setViewId("default.xul");   
    Phase invokeApplicationPhase = new InvokeApplicationPhase();
    getFacesContext().setViewRoot(page);

    invokeApplicationPhase.execute(getFacesContext());
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));
}

} // end of class TestInvokeApplicationPhase
