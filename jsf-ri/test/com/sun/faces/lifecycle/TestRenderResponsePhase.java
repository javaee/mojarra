/*
 * $Id: TestRenderResponsePhase.java,v 1.15 2002/08/15 23:23:03 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderResponsePhase.java

package com.sun.faces.lifecycle;

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

import com.sun.faces.TestBean;

import java.io.IOException;

import java.util.Iterator;
import java.util.ArrayList;

import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;

import javax.servlet.jsp.PageContext;

/**
 *
 *  <B>TestRenderResponsePhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderResponsePhase.java,v 1.15 2002/08/15 23:23:03 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderResponsePhase extends JspFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI = "/TestRenderResponsePhase.jsp";

public String getExpectedOutputFilename() {
    return "RenderResponse_correct";
}

public static final String ignore[] = {
    "<FORM METHOD=\"post\" ACTION=\"/test/faces/form/basicForm;jsessionid=A2E2E1E1A5B5A45544B8A063005509B3\">"
};
    
public String [] getLinesToIgnore() {
    return ignore;
}

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

    public TestRenderResponsePhase() {
	super("TestRenderResponsePhase");
    }

    public TestRenderResponsePhase(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//


public void beginHtmlBasicRenderKit(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
   // theRequest.addParameter("tree", TEST_URI_XUL);
}

public void testHtmlBasicRenderKit()
{
    System.setProperty(RIConstants.DISABLE_RENDERERS, "");

    boolean result = false;
    int rc = Phase.GOTO_NEXT;
    UIComponentBase root = null;
    String value = null;
    LifecycleImpl lifecycle = new LifecycleImpl();
    Phase 
	renderResponse = new JspRenderResponsePhase(lifecycle, 
				       Lifecycle.RENDER_RESPONSE_PHASE);
    root = new UIComponentBase() {
	    public String getComponentType() { return "Root"; }
	};
    root.setComponentId("root");
 
    TreeFactory treeFactory = (TreeFactory)
         FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
    Assert.assert_it(treeFactory != null);
    Tree requestTree = treeFactory.getTree(getFacesContext().getServletContext(),
            TEST_URI );
    getFacesContext().setRequestTree(requestTree);

    rc = renderResponse.execute(getFacesContext());
    assertTrue(Phase.GOTO_NEXT == rc);

    assertTrue(verifyExpectedOutput());
}

} // end of class TestRenderResponsePhase
