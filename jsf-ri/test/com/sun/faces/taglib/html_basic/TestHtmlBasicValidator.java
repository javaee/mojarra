/*
 * $Id: TestHtmlBasicValidator.java,v 1.2 2003/02/04 19:57:32 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestHtmlBasicValidator.java

package com.sun.faces.taglib.html_basic;

import org.apache.cactus.WebRequest;
import org.apache.cactus.JspTestCase;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIComponentBase;
import javax.faces.validator.Validator;
import javax.faces.component.AttributeDescriptor;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.FileOutputResponseWrapper;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import com.sun.faces.CompareFiles;
import com.sun.faces.lifecycle.LifecycleImpl;
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
 *  <B>TestHtmlBasicValidator</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestHtmlBasicValidator.java,v 1.2 2003/02/04 19:57:32 rogerk Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestHtmlBasicValidator extends JspFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI_FAIL = "/TestHtmlBasicValidatorFail.jsp";
public static final String TEST_URI_SUCCEED = "/TestHtmlBasicValidatorSucceed.jsp";

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

    public TestHtmlBasicValidator() {
	super("TestHtmlBasicValidator");
    }

    public TestHtmlBasicValidator(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//


public void beginPageShouldFail(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI_FAIL, null);
}

public void testPageShouldFail()
{
    boolean result = false;
    int rc = Phase.GOTO_NEXT;
    UIComponentBase root = null;
    String value = null;
    LifecycleImpl lifecycle = new LifecycleImpl();
    Phase 
	renderResponse = new RenderResponsePhase(lifecycle, 
				       RIConstants.RENDER_RESPONSE_PHASE);
    root = new UIComponentBase() {
	    public String getComponentType() { return "Root"; }
	};
    root.setComponentId("root");
 
    TreeFactory treeFactory = (TreeFactory)
         FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
    Assert.assert_it(treeFactory != null);
    Tree requestTree = treeFactory.getTree(getFacesContext(),
            TEST_URI_FAIL );
    getFacesContext().setTree(requestTree);

    boolean exceptionThrown = false;
    try {
	rc = renderResponse.execute(getFacesContext());
    }
    catch (Throwable e) {
	// If this exception message contains the string "command_button"
	if (-1 != e.getMessage().indexOf("command_button")) {
	    exceptionThrown = true;
	}
    }
    assertTrue(exceptionThrown);

}

public void beginPageShouldSucceed(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI_SUCCEED, null);
}

public void testPageShouldSucceed()
{
    boolean result = false;
    int rc = Phase.GOTO_NEXT;
    UIComponentBase root = null;
    String value = null;
    LifecycleImpl lifecycle = new LifecycleImpl();
    Phase 
	renderResponse = new RenderResponsePhase(lifecycle, 
				       RIConstants.RENDER_RESPONSE_PHASE);
    root = new UIComponentBase() {
	    public String getComponentType() { return "Root"; }
	};
    root.setComponentId("root");
 
    TreeFactory treeFactory = (TreeFactory)
         FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
    Assert.assert_it(treeFactory != null);
    Tree requestTree = treeFactory.getTree(getFacesContext(),
            TEST_URI_SUCCEED );
    getFacesContext().setTree(requestTree);

    boolean exceptionThrown = false;
    try {
	rc = renderResponse.execute(getFacesContext());
    }
    catch (Throwable e) {
	exceptionThrown = true;
    }
    assertTrue(!exceptionThrown);

}

} // end of class TestHtmlBasicValidator
