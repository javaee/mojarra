/*
 * $Id: TestCoreValidator.java,v 1.4 2003/03/12 19:54:20 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestCoreValidator.java

package com.sun.faces.taglib.jsf_core;

import org.apache.cactus.WebRequest;
import org.apache.cactus.JspTestCase;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIComponentBase;
import javax.faces.validator.Validator;
import javax.faces.component.AttributeDescriptor;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.FileOutputResponseWrapper;
import com.sun.faces.util.Util;
import com.sun.faces.CompareFiles;
import com.sun.faces.lifecycle.LifecycleImpl;
import com.sun.faces.lifecycle.Phase;
import com.sun.faces.lifecycle.RenderResponsePhase;

import com.sun.faces.TestBean;
import javax.servlet.ServletException;

import java.io.IOException;

import java.util.Iterator;
import java.util.ArrayList;

import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;

import javax.servlet.jsp.PageContext;

/**
 *
 *  <B>TestCoreValidator</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestCoreValidator.java,v 1.4 2003/03/12 19:54:20 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestCoreValidator extends JspFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI_IF_FAIL = "/TestCoreValidatorIfFail.jsp";
public static final String TEST_URI_IF_SUCCESS = "/TestCoreValidatorIfSuccess.jsp";
public static final String TEST_URI_ITERATOR_FAIL = "/TestCoreValidatorIteratorFail.jsp";
public static final String TEST_URI_ITERATOR_SUCCESS = "/TestCoreValidatorIteratorSuccess.jsp";

public boolean sendResponseToFile() 
{
    return true;
}

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

    public TestCoreValidator() {
	super("TestCoreValidator");
    }

    public TestCoreValidator(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//


public void beginIfShouldFail(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI_IF_FAIL, null);
}

public void testIfShouldFail()
{
    boolean result = false;
    UIComponentBase root = null;
    String value = null;
    LifecycleImpl lifecycle = new LifecycleImpl();
    Phase renderResponse = new RenderResponsePhase(lifecycle); 
    root = new UIComponentBase() {
	    public String getComponentType() { return "Root"; }
	};
    root.setComponentId("root");
 
    TreeFactory treeFactory = (TreeFactory)
         FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
    Assert.assert_it(treeFactory != null);
    Tree requestTree = treeFactory.getTree(getFacesContext(),
            TEST_URI_IF_FAIL );
    getFacesContext().setTree(requestTree);

    boolean exceptionThrown = false;
    try {
	renderResponse.execute(getFacesContext());
    }
    catch (Throwable e) {
	// If this exception message contains the string "output_text"
	if (-1 != e.getMessage().indexOf("output_text")) {
	    exceptionThrown = true;
	}
    }
    assertTrue(exceptionThrown);

}

public void beginIfShouldSuccess(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI_IF_SUCCESS, null);
}

public void testIfShouldSuccess()
{
    boolean result = false;
    UIComponentBase root = null;
    String value = null;
    LifecycleImpl lifecycle = new LifecycleImpl();
    Phase 
	renderResponse = new RenderResponsePhase(lifecycle);
    root = new UIComponentBase() {
	    public String getComponentType() { return "Root"; }
	};
    root.setComponentId("root");
 
    TreeFactory treeFactory = (TreeFactory)
         FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
    Assert.assert_it(treeFactory != null);
    Tree requestTree = treeFactory.getTree(getFacesContext(),
            TEST_URI_IF_SUCCESS );
    getFacesContext().setTree(requestTree);

    boolean exceptionThrown = false;
    try {
	renderResponse.execute(getFacesContext());
    }
    catch (Throwable e) {
	exceptionThrown = true;
    }
    assertTrue(!exceptionThrown);

}

public void beginIteratorShouldFail(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI_ITERATOR_FAIL, null);
}

public void testIteratorShouldFail()
{
    boolean result = false;
    UIComponentBase root = null;
    String value = null;
    LifecycleImpl lifecycle = new LifecycleImpl();
    Phase 
	renderResponse = new RenderResponsePhase(lifecycle);
    root = new UIComponentBase() {
	    public String getComponentType() { return "Root"; }
	};
    root.setComponentId("root");
 
    TreeFactory treeFactory = (TreeFactory)
         FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
    Assert.assert_it(treeFactory != null);
    Tree requestTree = treeFactory.getTree(getFacesContext(),
            TEST_URI_ITERATOR_FAIL );
    getFacesContext().setTree(requestTree);

    boolean exceptionThrown = false;

    try {
	renderResponse.execute(getFacesContext());
    }
    catch (Throwable e) {
	// If this exception message contains the string "output_text"
	if (-1 != e.getMessage().indexOf("output_text")) {
	    exceptionThrown = true;
	}
    }
    assertTrue(exceptionThrown);

}

public void beginIteratorShouldSuccess(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI_ITERATOR_SUCCESS, null);
}

public void testIteratorShouldSuccess()
{
    boolean result = false;
    UIComponentBase root = null;
    String value = null;
    LifecycleImpl lifecycle = new LifecycleImpl();
    Phase 
	renderResponse = new RenderResponsePhase(lifecycle);
    root = new UIComponentBase() {
	    public String getComponentType() { return "Root"; }
	};
    root.setComponentId("root");
 
    TreeFactory treeFactory = (TreeFactory)
         FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
    Assert.assert_it(treeFactory != null);
    Tree requestTree = treeFactory.getTree(getFacesContext(),
            TEST_URI_ITERATOR_SUCCESS );
    getFacesContext().setTree(requestTree);

    boolean exceptionThrown = false;
    try {
	renderResponse.execute(getFacesContext());
    }
    catch (Throwable e) {
	exceptionThrown = true;
    }
    assertTrue(!exceptionThrown);

}


} // end of class TestCoreValidator
