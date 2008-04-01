/*
 * $Id: TestTreebuilder.java,v 1.2 2002/03/08 22:16:10 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestTreebuilder.java

package com.sun.faces.treebuilder;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;

import javax.faces.Constants;
import javax.faces.UIComponent;
import javax.faces.RenderContext;
import javax.faces.ObjectManager;
import javax.faces.TreeNavigator;


import org.xml.sax.Attributes;

import com.sun.faces.FacesTestCase;
import com.sun.faces.ParamBlockingRequestWrapper;

import org.apache.cactus.WebRequest;

import com.sun.faces.treebuilder.TreeEngine;
import com.sun.faces.treebuilder.TreeBuilder;

import java.util.List;
import java.util.Iterator;

import org.apache.jasper_hacked.compiler.PreParser;
import org.apache.jasper_hacked.compiler.JspParseListener;

/**
 *
 *  Exercise PreParser and TreeNavigator.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestTreebuilder.java,v 1.2 2002/03/08 22:16:10 eburns Exp $
 * 
 * @see	com.sun.faces.TreeNavigator
 * @see	com.sun.faces.TreeEngine
 *
 */

public class TestTreebuilder extends FacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI = "/treeTest.jsp";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

private PreParser preParser = null;

//
// Constructors and Initializers    
//

    public TestTreebuilder() {super("TestTreebuilder");}
    public TestTreebuilder(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

public void getTreeForURI(ObjectManager objectManager, 
			  ServletRequest req, String requestURI) {
    HttpSession session = ((HttpServletRequest)req).getSession();
    TreeNavigator treeNav = null;
    
    // PENDING(edburns): Using the session is a temporary hack until
    // we have save state.
    if (null == (treeNav = 
		 (TreeNavigator) session.getAttribute(requestURI))) {
	RenderContext rc;
	rc = (RenderContext)objectManager.get(req,
					      Constants.REF_RENDERCONTEXT);
	assertTrue(null != rc);
	
	TreeEngine treeEng = (TreeEngine) 
	    objectManager.get(Constants.REF_TREEENGINE);
	assertTrue(null != treeEng);
	
	treeNav = treeEng.getTreeForURI(rc, requestURI);
	// stick this here so we can have access to it on the postback.
	if (null != treeNav) {
	    session.setAttribute(requestURI, treeNav);
	}
    }
    else {
	session.removeAttribute(requestURI);
    }
    if (null != treeNav) {
	objectManager.put(req, Constants.REF_TREENAVIGATOR, treeNav);
    }
    
}
    
public void setUp() {
    super.setUp();
    ServletContext servletContext = config.getServletContext();
    preParser = new PreParser(servletContext);    
    TreeEngine treeEngine = 
	new com.sun.faces.treebuilder.TreeEngineImpl(servletContext);
    assertTrue(null != treeEngine);
    objectManager.put(servletContext, Constants.REF_TREEENGINE, 
		      treeEngine);

}

public void tearDown() {
    super.tearDown();
    preParser = null;
}

public void beginTree(WebRequest theRequest) {
    theRequest.setURL("localhost:8080", null, null, TEST_URI,null);
}

public void testTree() {
    ParamBlockingRequestWrapper wrapped = 
	new ParamBlockingRequestWrapper(request);

    // put the rc in the session
    HttpSession session = request.getSession();
    objectManager.put(session, Constants.REF_RENDERCONTEXT, renderContext);

    TreeNavigator treeNav = null;

    // Simulate the filter's parsing of the tree
    try {
	System.out.println("Testing getTreeForURI:");

	getTreeForURI(objectManager, wrapped, TEST_URI);

	treeNav = (TreeNavigator) objectManager.get(wrapped, 
						 Constants.REF_TREENAVIGATOR);
	assertTrue(true);
    }
    catch(Exception e) {
	System.out.println("testEnterExit: caught exception");
	assertTrue(false);
    }
    assertTrue(null != treeNav);
	
    // simulate the JSP Engine's parsing of the tree
    JspSimulator jspSim = new JspSimulator(renderContext, TEST_URI, treeNav);
    preParser.addJspParseListener(jspSim);
    preParser.preParse(TEST_URI);
    preParser.removeJspParseListener(jspSim);

    System.out.println("Resetting TreeNav");
    treeNav.reset();
    preParser.addJspParseListener(jspSim);
    preParser.preParse(TEST_URI);
    preParser.removeJspParseListener(jspSim);
}

public static class JspSimulator extends TreeBuilder implements JspParseListener {

protected TreeNavigator treeNav;

public JspSimulator(RenderContext newRenderContext, String newRequestURI,
		    TreeNavigator newTreeNav) {
    super(newRenderContext, newRequestURI);
    treeNav = newTreeNav;
}

protected void compareComponentAndTag(UIComponent comp, Attributes attrs,
				      String shortTagName) 
{
    String className, attrName, id = null;
    boolean result = false;
    int i, attrLen;

    assertTrue(null != comp);

    // get the id from the attrs
    attrLen = attrs.getLength();
    for (i = 0; i < attrLen; i++) {
	attrName = attrs.getLocalName(i);
	if (attrName.equals("id")) {
	    id = attrs.getValue(i);
	}
    }
    assertTrue(null != id);

    className = comp.getClass().getName();
    result = className.equals(classMap.get(shortTagName));
    System.out.println("class from Tree: " + className);
    System.out.println("\tclass from page: " + classMap.get(shortTagName));
    System.out.println("\tcorrect: " + result);
    assertTrue(result);

    result = id.equals(comp.getId());
    System.out.println("id from Tree: " + comp.getId());
    System.out.println("\tclass from page: " + id);
    System.out.println("\tcorrect: " + result);
    assertTrue(result);
}

public void handleTagBegin(Attributes attrs, String prefix,
			   String shortTagName, TagLibraryInfo tli,
			   TagInfo ti, boolean hasBody, boolean isXml)
{
    if (!prefix.equalsIgnoreCase("faces")) {
	return;
    }
    if (!tagHasComponent(shortTagName)) {
	return;
    }
    UIComponent comp = treeNav.getNextStart();
    System.out.println("+++tagBegin: " + shortTagName + "\n\n");
    compareComponentAndTag(comp, attrs, shortTagName);
}

public void handleTagEnd(Attributes attrs, String prefix,
			 String shortTagName, TagLibraryInfo tli,
			 TagInfo ti, boolean hasBody)
{
    if (!prefix.equalsIgnoreCase("faces")) {
	return;
    }
    if (!tagHasComponent(shortTagName)) {
	return;
    }
    UIComponent comp = treeNav.getNextEnd();
    System.out.println("---tagEnd: " + shortTagName + "\n\n");
    compareComponentAndTag(comp, attrs, shortTagName);
}

}


} // end of class TestTreebuilder
