/*
 * $Id: TestRenderResponsePhase.java,v 1.1 2002/06/07 00:01:13 eburns Exp $
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

import com.sun.faces.FacesContextTestCaseJsp;
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
 * @version $Id: TestRenderResponsePhase.java,v 1.1 2002/06/07 00:01:13 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderResponsePhase extends FacesContextTestCaseJsp
{
//
// Protected Constants
//

public static final String TEST_URI_XUL = "/Faces_Basic.xul";

public static final String TEST_URI = "/components.jsp";

public static final String PATH_ROOT = "./build/test/servers/tomcat40/webapps/test/";

public static final String EXPECTED_OUTPUT_FILENAME = PATH_ROOT +
        "RenderResponse_correct";
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

public void setUp()
{
    Util.initServletContextForFaces(config.getServletContext());
    
    facesContextFactory = (FacesContextFactory) config.getServletContext().
	getAttribute(FactoryFinder.FACES_CONTEXT_FACTORY);
    assertTrue(null != facesContextFactory);
    
    facesContext = 
	facesContextFactory.createFacesContext(config.getServletContext(),
					       request, 
				      new FileOutputResponseWrapper(response));
    pageContext.setAttribute(FacesContext.FACES_CONTEXT_ATTR, facesContext,
			     PageContext.REQUEST_SCOPE);

    assertTrue(null != facesContext);

    TestBean testBean = new TestBean();
    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
}

public void beginRender(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    theRequest.addParameter("tree", TEST_URI_XUL);
}

public void testRender()
{
    // Make sure we use the default component rendering behavior
    System.setProperty(RIConstants.DISABLE_RENDERERS, 
		       RIConstants.DISABLE_RENDERERS);
    boolean result = false;
    int rc = Phase.GOTO_NEXT;
    UIComponent root = null;
    CompareFiles cf = new CompareFiles();
    String errorMessage = null, value = null;
    Phase 
	createTree = new CreateRequestTreePhase(null, 
					Lifecycle.CREATE_REQUEST_TREE_PHASE),
	renderResponse = new JspRenderResponsePhase(null, 
				       Lifecycle.RENDER_RESPONSE_PHASE);
    rc = createTree.execute(facesContext);
    assertTrue(Phase.GOTO_NEXT == rc);

    rc = renderResponse.execute(facesContext);
    assertTrue(Phase.GOTO_NEXT == rc);

    errorMessage = "File Comparison failed: diff -u " + 
	FileOutputResponseWrapper.FACES_RESPONSE_FILENAME + " " + 
	EXPECTED_OUTPUT_FILENAME;
    try {
	ArrayList ignoreList = new ArrayList();
	String ignore[] = {
	    "        <form method=\"post\" action=\"/test/faces;jsessionid=A433F5F10481B2B2D78397146970C253?action=form&name=basicForm&tree=/Faces_Basic.xul\">",
	    "            <a href=\"/test/faces;jsessionid=A433F5F10481B2B2D78397146970C253?action=command&name=null&tree=/Faces_Basic.xul\"></a>",
	    "	    <!-- <a href=\"/test/faces;jsessionid=A433F5F10481B2B2D78397146970C253?action=command&name=null&tree=/Faces_Basic.xul\"></a> -->"
	};
	for (int i = 0; i < ignore.length; i++) {
	    ignoreList.add(ignore[i]);
	}

	result = 
	   cf.filesIdentical(FileOutputResponseWrapper.FACES_RESPONSE_FILENAME,
			     EXPECTED_OUTPUT_FILENAME, ignoreList);
    }
    catch (Throwable e) {
	System.out.println(e.getMessage());
	e.printStackTrace();
	assertTrue(false);
    }

    assertTrue(errorMessage, result);
}

} // end of class TestRenderResponsePhase
