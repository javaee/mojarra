/*
 * $Id: TestLifecycleImpl_initial.java,v 1.1 2002/06/07 22:47:37 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestLifecycleImpl_initial.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.PhaseListener;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.FormEvent;
import javax.faces.event.CommandEvent;

import java.util.Iterator;
import java.util.ArrayList;

import com.sun.faces.FacesContextTestCaseJsp;
import com.sun.faces.FileOutputResponseWrapper;
import com.sun.faces.CompareFiles;

import com.sun.faces.RIConstants;

import javax.servlet.http.HttpServletResponse;

/**
 *
 *  <B>TestLifecycleImpl_initial</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestLifecycleImpl_initial.java,v 1.1 2002/06/07 22:47:37 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestLifecycleImpl_initial extends FacesContextTestCaseJsp
{
//
// Protected Constants
//

public static final String TEST_URI_XUL = "/Faces_Basic.xul";
public static final String TEST_URI = "/components.jsp";

public static final String EXPECTED_OUTPUT_FILENAME = 
    FileOutputResponseWrapper.FACES_RESPONSE_ROOT + 
    "TestLifecycleImpl_initial_correct";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

protected FileOutputResponseWrapper fileResponse = null;

//
// Constructors and Initializers    
//

    public TestLifecycleImpl_initial() {super("TestLifecycleImpl_initial");}
    public TestLifecycleImpl_initial(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//

public HttpServletResponse getResponse()
{
    if (null == fileResponse) {
	fileResponse = new FileOutputResponseWrapper(super.getResponse());
    }
    return fileResponse;
}

protected void initWebRequest(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    theRequest.addParameter("tree", TEST_URI_XUL);
    theRequest.addParameter(RIConstants.INITIAL_REQUEST_NAME, 
			    RIConstants.INITIAL_REQUEST_VALUE);
}

public void beginExecuteInitial(WebRequest theRequest)
{
    initWebRequest(theRequest);
}

public void testExecuteInitial()
{
    boolean result = false;
    final String ENTER_CALLED = "enterCalled";
    final String EXIT_CALLED = "exitCalled";
    final String EMPTY = "empty";
    LifecycleImpl life = new LifecycleImpl();
    System.setProperty(ENTER_CALLED, EMPTY);
    System.setProperty(EXIT_CALLED, EMPTY);

    PhaseListener listener = new PhaseListener() {
	    public void entering(FacesContext context, int phaseId, 
				 Phase phase) {
	       assertTrue(phaseId < Lifecycle.RECONSTITUTE_REQUEST_TREE_PHASE);
	       System.setProperty(ENTER_CALLED, ENTER_CALLED);
	    }
	    public void exiting(FacesContext context, int phaseId,
				Phase phase, int stateChange) {
	       assertTrue(phaseId < Lifecycle.RECONSTITUTE_REQUEST_TREE_PHASE
			  ||
			  phaseId == Lifecycle.RENDER_RESPONSE_PHASE);
	       System.setProperty(EXIT_CALLED, EXIT_CALLED);
	    }
	};
    
    life.addPhaseListener(listener);

    try {
	life.execute(facesContext);
    }
    catch (Throwable e) {
	e.printStackTrace();
	assertTrue(e.getMessage(), false);
    }
    assertTrue(System.getProperty(ENTER_CALLED).equals(ENTER_CALLED));
    assertTrue(System.getProperty(EXIT_CALLED).equals(EXIT_CALLED));
    
    System.setProperty(ENTER_CALLED, EMPTY);
    System.setProperty(EXIT_CALLED, EMPTY);

    CompareFiles cf = new CompareFiles();
    String errorMessage = "File Comparison failed: diff -u " + 
	FileOutputResponseWrapper.FACES_RESPONSE_FILENAME + " " + 
	EXPECTED_OUTPUT_FILENAME;
    try {
	ArrayList ignoreList = new ArrayList();
	String ignore[] = {
	    "        <form method=\"post\" action=\"/test/faces;jsessionid=C698E8893775F7F980C7F33784789447?action=form&name=basicForm&tree=/Faces_Basic.xul\">",
	    "            <a href=\"/test/faces;jsessionid=C698E8893775F7F980C7F33784789447?action=command&name=null&tree=/Faces_Basic.xul\"></a>",
	    "	    <!-- <a href=\"/test/faces;jsessionid=C698E8893775F7F980C7F33784789447?action=command&name=null&tree=/Faces_Basic.xul\"></a> -->"
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



} // end of class TestLifecycleImpl_initial
