/*
 * $Id: TestRenderResponsePhase.java,v 1.5 2002/06/20 01:34:26 eburns Exp $
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
import javax.faces.component.UIComponent;
import javax.faces.component.UITextEntry;
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

import javax.servlet.jsp.PageContext;

/**
 *
 *  <B>TestRenderResponsePhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderResponsePhase.java,v 1.5 2002/06/20 01:34:26 eburns Exp $
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

public static final String TEST_URI_XUL = "/components.xul";
public static final String TEST_URI = "/components.jsp";

public String getExpectedOutputFilename() {
    return "RenderResponse_correct";
}

public static final String ignore[] = {
    "        <form method=\"post\" action=\"/test/faces;jsessionid=A433F5F10481B2B2D78397146970C253?action=form&name=basicForm&tree=/Faces_Basic.xul\">",
    "            <a href=\"/test/faces;jsessionid=A433F5F10481B2B2D78397146970C253?action=command&name=null&tree=/Faces_Basic.xul\"></a>",
    "	    <!-- <a href=\"/test/faces;jsessionid=A433F5F10481B2B2D78397146970C253?action=command&name=null&tree=/Faces_Basic.xul\"></a> -->"
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


public void beginRender(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI_XUL, null);
   // theRequest.addParameter("tree", TEST_URI_XUL);
}

public void testRender()
{
    boolean result = false;
    int rc = Phase.GOTO_NEXT;
    UIComponent root = null;
    String value = null;
    Phase 
	createTree = new CreateRequestTreePhase(null, 
					Lifecycle.CREATE_REQUEST_TREE_PHASE),
	renderResponse = new JspRenderResponsePhase(null, 
				       Lifecycle.RENDER_RESPONSE_PHASE);
    rc = createTree.execute(getFacesContext());
    assertTrue(Phase.GOTO_NEXT == rc);

    rc = renderResponse.execute(getFacesContext());
    assertTrue(Phase.GOTO_NEXT == rc);

    assertTrue(verifyExpectedOutput());
}

} // end of class TestRenderResponsePhase
