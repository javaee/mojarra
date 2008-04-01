/*
 * $Id: TestLifecycleImpl_initial.java,v 1.4 2002/06/18 18:23:25 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestLifecycleImpl_initial.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;


import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.PhaseListener;
import javax.faces.context.FacesContext;

import com.sun.faces.RIConstants;

import com.sun.faces.JspFacesTestCase;

/**
 *
 *  <B>TestLifecycleImpl_initial</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestLifecycleImpl_initial.java,v 1.4 2002/06/18 18:23:25 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestLifecycleImpl_initial extends JspFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI_XUL = "/components.xul";
public static final String TEST_URI = "/components.jsp";

public String getExpectedOutputFilename() {
    return "TestLifecycleImpl_initial_correct";
}

public static final String ignore[] = {
    "        <form method=\"post\" action=\"/test/faces;jsessionid=C698E8893775F7F980C7F33784789447?action=form&name=basicForm&tree=/Faces_Basic.xul\">",
    "            <a href=\"/test/faces;jsessionid=C698E8893775F7F980C7F33784789447?action=command&name=null&tree=/Faces_Basic.xul\"></a>",
    "	    <!-- <a href=\"/test/faces;jsessionid=C698E8893775F7F980C7F33784789447?action=command&name=null&tree=/Faces_Basic.xul\"></a> -->"
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

    public TestLifecycleImpl_initial() {super("TestLifecycleImpl_initial");}
    public TestLifecycleImpl_initial(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//


protected void initWebRequest(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI_XUL, null);
   // theRequest.addParameter("tree", TEST_URI_XUL);
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
	life.execute(getFacesContext());
    }
    catch (Throwable e) {
	e.printStackTrace();
	assertTrue(e.getMessage(), false);
    }
    assertTrue(System.getProperty(ENTER_CALLED).equals(ENTER_CALLED));
    assertTrue(System.getProperty(EXIT_CALLED).equals(EXIT_CALLED));
    
    System.setProperty(ENTER_CALLED, EMPTY);
    System.setProperty(EXIT_CALLED, EMPTY);

    assertTrue(verifyExpectedOutput());

}



} // end of class TestLifecycleImpl_initial
