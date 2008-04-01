/*
 * $Id: TestTreebuilder.java,v 1.4 2002/03/19 19:25:02 eburns Exp $
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
import javax.faces.UIPage;


import org.xml.sax.Attributes;

import com.sun.faces.FacesTestCase;
import com.sun.faces.ParamBlockingRequestWrapper;

import org.apache.cactus.WebRequest;

import com.sun.faces.treebuilder.TreeEngine;
import com.sun.faces.treebuilder.TreeBuilder;
import com.sun.faces.treebuilder.BuildComponentFromTag;
import com.sun.faces.taglib.html_basic.BuildComponentFromTagImpl;
import com.sun.faces.util.Util;

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
 * @version $Id: TestTreebuilder.java,v 1.4 2002/03/19 19:25:02 eburns Exp $
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
public static final String WHILE_URI = "/testWhile.jsp";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

private PreParser preParser = null;
private BuildComponentFromTag componentBuilder = null;

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
	UIPage root = new UIPage();
	root.setId(Util.generateId());
	
	treeNav = treeEng.getTreeForURI(rc, root, requestURI);
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
    componentBuilder = new TestBuildComponentFromTagImpl();
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

    // Simulate the LifecyleDriver's parsing of the tree
    try {
	System.out.println("Testing getTreeForURI:");

	getTreeForURI(objectManager, wrapped, TEST_URI);

	treeNav = (TreeNavigator) objectManager.get(wrapped, 
						 Constants.REF_TREENAVIGATOR);
	assertTrue(true);
    }
    catch(Exception e) {
	System.out.println("testTree: caught exception");
	assertTrue(false);
    }
    assertTrue(null != treeNav);

    UIPage root = new UIPage();
    root.setId(Util.generateId());
	
    // simulate the JSP Engine's parsing of the tree
    JspSimulator jspSim = new JspSimulator(componentBuilder, renderContext, 
					   root, TEST_URI, treeNav);
    // Account for the UIPage root
    treeNav.getNextStart();
    preParser.addJspParseListener(jspSim);
    preParser.preParse(TEST_URI);
    preParser.removeJspParseListener(jspSim);
    // Account for the UIPage root
    treeNav.getNextEnd();


    System.out.println("Resetting TreeNav");
    treeNav.reset();
    // Account for the UIPage root
    treeNav.getNextStart();
    preParser.addJspParseListener(jspSim);
    preParser.preParse(TEST_URI);
    preParser.removeJspParseListener(jspSim);
    // Account for the UIPage root
    treeNav.getNextEnd();
}

public void beginWhile(WebRequest theRequest) {
    theRequest.setURL("localhost:8080", null, null, WHILE_URI,null);
}

public void testWhile() {
    ParamBlockingRequestWrapper wrapped = 
	new ParamBlockingRequestWrapper(request);

    // put the rc in the session
    HttpSession session = request.getSession();
    objectManager.put(session, Constants.REF_RENDERCONTEXT, renderContext);

    TreeNavigator treeNav = null;

    // Simulate the LifecyleDriver's parsing of the tree
    try {
	System.out.println("Testing getTreeForURI:");

	getTreeForURI(objectManager, wrapped, WHILE_URI);

	treeNav = (TreeNavigator) objectManager.get(wrapped, 
						 Constants.REF_TREENAVIGATOR);
	assertTrue(true);
    }
    catch(Exception e) {
	System.out.println("testTree: caught exception");
	assertTrue(false);
    }
    assertTrue(null != treeNav);
	
    treeNav.reset();

    UIComponent next;
    
    while (null != (next = treeNav.getNextStart())) {
	System.out.println("cur = " + next.getId());
    }


}


public static class JspSimulator extends TreeBuilder implements JspParseListener {

protected TreeNavigator treeNav;

public JspSimulator(BuildComponentFromTag newComponentBuilder, 
		    RenderContext newRenderContext, UIPage root, 
		    String newRequestURI, TreeNavigator newTreeNav) {
    super(newComponentBuilder, newRenderContext, root, newRequestURI);
    treeNav = newTreeNav;
}

protected void compareComponentAndTag(UIComponent comp, Attributes attrs,
				      String shortTagName) 
{
    String attrName, id = null;
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

    ((TestBuildComponentFromTagImpl)this.componentBuilder).testTagCorrespondence(shortTagName, id, comp);
}

public void handleTagBegin(Attributes attrs, String prefix,
			   String shortTagName, TagLibraryInfo tli,
			   TagInfo ti, boolean hasBody, boolean isXml)
{
    if (!prefix.equalsIgnoreCase("faces")) {
	return;
    }
    if (!this.componentBuilder.tagHasComponent(shortTagName)) {
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
    if (!this.componentBuilder.tagHasComponent(shortTagName)) {
	return;
    }
    UIComponent comp = treeNav.getNextEnd();
    System.out.println("---tagEnd: " + shortTagName + "\n\n");
    compareComponentAndTag(comp, attrs, shortTagName);
}

} // end of class JspSimulator

public static class TestBuildComponentFromTagImpl extends BuildComponentFromTagImpl {

public void testTagCorrespondence(String shortTagName, String id, 
				  UIComponent comp) 
{
    boolean result = false;
    String className;

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

} // end of class TestBuildComponentFromTagImpl


} // end of class TestTreebuilder
