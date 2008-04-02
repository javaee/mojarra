/*
 * $Id: TestSaveStateInPage.java,v 1.7 2003/02/04 19:57:32 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestSaveStateInPage.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIComponentBase;
import com.sun.faces.RIConstants;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.RIConstants;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;


/**
 *
 *  <B>TestSaveStateInPage</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestSaveStateInPage.java,v 1.7 2003/02/04 19:57:32 rogerk Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestSaveStateInPage extends JspFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI = "/TestSaveState.jsp";

public String getExpectedOutputFilename() {
    return "SaveState_correct";
}

public static final String ignore[] = {
   "<form method=\"post\" action=\"/test/faces/TestSaveState.jsp;jsessionid=5DD4AFE4BEB34D9118D5F1CE6B8F93CE\">"
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

    public TestSaveStateInPage() {
	super("TestRenderResponsePhase");
    }

    public TestSaveStateInPage(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//


public void beginSaveStateInPage(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
   // theRequest.addParameter("tree", TEST_URI_XUL);
}

public void testSaveStateInPage()
{
    System.setProperty(RIConstants.DISABLE_RENDERERS, "");

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
    assertTrue(treeFactory != null);
    Tree requestTree = treeFactory.getTree(getFacesContext(),
            TEST_URI );
    getFacesContext().setTree(requestTree);

    rc = renderResponse.execute(getFacesContext());
    assertTrue(Phase.GOTO_NEXT == rc);

    assertTrue(verifyExpectedOutput());
}

} // end of class TestRenderResponsePhase
