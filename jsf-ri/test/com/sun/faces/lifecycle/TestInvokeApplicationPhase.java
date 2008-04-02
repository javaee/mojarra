/*
 * $Id: TestInvokeApplicationPhase.java,v 1.7 2002/10/07 22:58:01 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.component.UIInput;
import javax.faces.tree.Tree;
import javax.faces.event.FacesEvent;
import javax.faces.event.FormEvent;
import javax.faces.event.CommandEvent;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.lifecycle.LifecycleImpl;
import com.sun.faces.tree.XmlTreeImpl;
import com.sun.faces.RIConstants;
import java.io.IOException;

import java.util.Iterator;

/**
 *
 *  <B>TestInvokeApplicationPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestInvokeApplicationPhase.java,v 1.7 2002/10/07 22:58:01 jvisvanathan Exp $
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
    System.setProperty(DID_COMMAND, EMPTY);
    System.setProperty(DID_FORM, EMPTY);
    final UIInput root = new UIInput();
    Lifecycle life = new LifecycleImpl();
    Tree tree = new XmlTreeImpl(getFacesContext(),
				root, "default.xul", "");
    ApplicationHandler appHandler = new ApplicationHandler() {
        public boolean processEvent(FacesContext context, FacesEvent event){
		System.setProperty(DID_FORM, DID_FORM);
                System.setProperty(DID_COMMAND, DID_COMMAND);
		assertTrue(root == event.getComponent());
                return true;
	    }
	};
    Phase invokeApplicationPhase = new InvokeApplicationPhase(life, 
				      RIConstants.INVOKE_APPLICATION_PHASE);
    int rc = Phase.GOTO_NEXT;
    getFacesContext().setRequestTree(tree);

    life.setApplicationHandler(appHandler);
    getFacesContext().addApplicationEvent(new CommandEvent(root, "command"));
    getFacesContext().addApplicationEvent(new FormEvent(root, "formName",
							"commandName"));
    rc = invokeApplicationPhase.execute(getFacesContext());
    assertTrue(Phase.GOTO_RENDER == rc);
    assertTrue(System.getProperty(DID_COMMAND).equals(DID_COMMAND));
    assertTrue(System.getProperty(DID_FORM).equals(DID_FORM));
}

public void testInvokeNoOp()
{
    UIInput root = new UIInput();
    Lifecycle life = new LifecycleImpl();
    Tree tree = new XmlTreeImpl(getFacesContext(),
				root, "default.xul", "");
    Phase invokeApplicationPhase = new InvokeApplicationPhase(life, 
				      RIConstants.INVOKE_APPLICATION_PHASE);
    int rc = Phase.GOTO_NEXT;
    getFacesContext().setRequestTree(tree);

    rc = invokeApplicationPhase.execute(getFacesContext());
    assertTrue(Phase.GOTO_NEXT == rc);
}

} // end of class TestInvokeApplicationPhase
