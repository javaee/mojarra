/*
 * $Id: TestInvokeApplicationPhase.java,v 1.13 2003/04/03 18:39:04 rkitain Exp $
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
import javax.faces.tree.Tree;
import javax.faces.event.FacesEvent;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.context.FacesContextImpl;
import com.sun.faces.lifecycle.LifecycleImpl;
import com.sun.faces.lifecycle.Phase;
import com.sun.faces.tree.SimpleTreeImpl;
import com.sun.faces.RIConstants;
import java.io.IOException;

import java.util.Iterator;

/**
 *
 *  <B>TestInvokeApplicationPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestInvokeApplicationPhase.java,v 1.13 2003/04/03 18:39:04 rkitain Exp $
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
    Lifecycle life = new LifecycleImpl();
    Tree tree = new SimpleTreeImpl(getFacesContext(),
				root, "default.xul");
    Phase invokeApplicationPhase = new InvokeApplicationPhase(life);
    getFacesContext().setTree(tree);

    invokeApplicationPhase.execute(getFacesContext());
    assertTrue(!((FacesContextImpl)getFacesContext()).getRenderResponse() &&
        !((FacesContextImpl)getFacesContext()).getResponseComplete());
}

} // end of class TestInvokeApplicationPhase
