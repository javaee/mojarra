/*
 * $Id: TestRenderResponsePhase.java,v 1.54 2003/08/15 19:15:39 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UINamingContainer;
import javax.faces.validator.Validator;

import com.sun.faces.lifecycle.Phase;
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
 * @version $Id: TestRenderResponsePhase.java,v 1.54 2003/08/15 19:15:39 rlubke Exp $
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
    "<form id=\"basicForm\" method=\"post\" action=\"/test/faces/TestRenderResponsePhase.jsp;jsessionid=4F77D965C89220B57F988ED1419FC083\" class=\"formClass\" title=\"basicForm\" accept=\"html,wml\">",
    "            <img id=\"graphicImage\" src=\"/test/duke.gif;jsessionid=4F77D965C89220B57F988ED1419FC083\" usemap=\"#map1\" ismap> "
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
}

public void testHtmlBasicRenderKit()
{    
    
    
    boolean result = false;
    UINamingContainer root = null;
    String value = null;
    LifecycleImpl lifecycle = new LifecycleImpl();
    Phase renderResponse = new RenderResponsePhase(lifecycle);
    root = new UINamingContainer() {
        public String getComponentType() { return "root"; }
    };
    root.setComponentId("root");
 
    TreeFactory treeFactory = (TreeFactory)
         FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
    Assert.assert_it(treeFactory != null);
    Tree requestTree = treeFactory.getTree(getFacesContext(),
            TEST_URI );
    getFacesContext().setTree(requestTree);

    renderResponse.execute(getFacesContext());
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(verifyExpectedOutput());
}

} // end of class TestRenderResponsePhase
