/*
 * $Id: TestInvokeApplicationPhase.java,v 1.15 2003/08/21 14:18:16 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestInvokeApplicationPhase.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIInput;
import javax.faces.component.UIPage;
import javax.faces.component.base.UIInputBase;
import javax.faces.component.base.UIPageBase;
import javax.faces.event.FacesEvent;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.lifecycle.LifecycleImpl;
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
 * @version $Id: TestInvokeApplicationPhase.java,v 1.15 2003/08/21 14:18:16 rlubke Exp $
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
    UIInput root = new UIInputBase();
    Lifecycle life = new LifecycleImpl();
    UIPage page = new UIPageBase();
    page.setTreeId("default.xul");   
    Phase invokeApplicationPhase = new InvokeApplicationPhase(life);
    getFacesContext().setRoot(page);

    invokeApplicationPhase.execute(getFacesContext());
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));
}

} // end of class TestInvokeApplicationPhase
