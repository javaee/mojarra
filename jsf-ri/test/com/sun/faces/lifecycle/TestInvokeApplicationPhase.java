/*
 * $Id: TestInvokeApplicationPhase.java,v 1.2 2002/06/18 05:02:27 rkitain Exp $
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
import javax.faces.component.UITextEntry;
import javax.faces.tree.Tree;
import javax.faces.event.CommandEvent;
import javax.faces.event.FormEvent;

import com.sun.faces.FacesContextTestCase;
import com.sun.faces.lifecycle.LifecycleImpl;
import com.sun.faces.tree.XmlTreeImpl;

import java.io.IOException;

import java.util.Iterator;

/**
 *
 *  <B>TestInvokeApplicationPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestInvokeApplicationPhase.java,v 1.2 2002/06/18 05:02:27 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestInvokeApplicationPhase extends FacesContextTestCase
{
//
// Protected Constants
//

public static final String DID_COMMAND = "didCommand";
public static final String DID_FORM = "didForm";
public static final String EMPTY = "EMPTY";

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
    final UITextEntry root = new UITextEntry();
    Lifecycle life = new LifecycleImpl();
    Tree tree = new XmlTreeImpl(config.getServletContext(),
				root, "default.xul", "");
    ApplicationHandler appHandler = new ApplicationHandler() {

	    public void commandEvent(FacesContext context, CommandEvent event){
		System.setProperty(DID_COMMAND, DID_COMMAND);
		assertTrue(root == event.getComponent());
	    }
	    public void formEvent(FacesContext context, FormEvent event){
		System.setProperty(DID_FORM, DID_FORM);
		assertTrue(root == event.getComponent());
	    }
	};
    Phase invokeApplicationPhase = new InvokeApplicationPhase(life, 
				      Lifecycle.INVOKE_APPLICATION_PHASE);
    int rc = Phase.GOTO_NEXT;
    facesContext.setRequestTree(tree);

    life.setApplicationHandler(appHandler);
    facesContext.addApplicationEvent(new CommandEvent(root, "command"));
    facesContext.addApplicationEvent(new FormEvent(root, "form"));
    rc = invokeApplicationPhase.execute(facesContext);
    assertTrue(Phase.GOTO_NEXT == rc);
    assertTrue(System.getProperty(DID_COMMAND).equals(DID_COMMAND));
    assertTrue(System.getProperty(DID_FORM).equals(DID_FORM));
}

public void testInvokeNoOp()
{
    UITextEntry root = new UITextEntry();
    Lifecycle life = new LifecycleImpl();
    Tree tree = new XmlTreeImpl(config.getServletContext(),
				root, "default.xul", "");
    Phase invokeApplicationPhase = new InvokeApplicationPhase(life, 
				      Lifecycle.INVOKE_APPLICATION_PHASE);
    int rc = Phase.GOTO_NEXT;
    facesContext.setRequestTree(tree);

    rc = invokeApplicationPhase.execute(facesContext);
    assertTrue(Phase.GOTO_NEXT == rc);
}

} // end of class TestInvokeApplicationPhase
