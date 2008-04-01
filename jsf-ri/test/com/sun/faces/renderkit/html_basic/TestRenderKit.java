/*
 * $Id: TestRenderKit.java,v 1.4 2001/12/13 01:17:13 edburns Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

// TestRenderKit.java

package com.sun.faces.renderkit.html_basic;

import junit.framework.TestCase;
import org.apache.cactus.ServletTestCase;

import javax.servlet.http.HttpSession;

import javax.faces.RenderKit;
import javax.servlet.ServletRequest;
import javax.faces.RenderContextFactory;
import javax.faces.RenderContext;
import javax.faces.FacesException;


/**
 *
 *  <B>TestRenderKit</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderKit.java,v 1.4 2001/12/13 01:17:13 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderKit extends ServletTestCase
{
//
// Protected Constants
//

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

    public TestRenderKit() {super("TestRenderKit");}
    public TestRenderKit(String name) {super(name);}
//
// Class methods
//

//
// General Methods
//

public void testFactory() {
    RenderKit kit = null;
    RenderContext context;
    RenderContextFactory factory;
    javax.faces.Renderer renderer;
    java.util.Iterator typeIt, rendererIt = null;
    String type, rendererName;
    boolean result = false;
    
    try {
	factory = RenderContextFactory.newInstance();
	System.out.println("HtmlBasicRenderContextFactory: got factory: " + 
			   factory);
	context = factory.newRenderContext(request);
	System.out.println("HtmlBasicRenderContextFactory: got context: " + 
			   context);
	result = null != context.getSession();
	assertTrue(result);

	// PENDING(edburns): test for context's clientCaps, and locale.

	kit = context.getRenderKit();
	System.out.println("HtmlBasicRenderContextFactory: got renderKit: " + 
			   kit);
    }
    catch (Exception e) {
	System.out.println("Exception getting factory!!! " + e.getMessage());
	assertTrue(false);
    }
    assertTrue(true);

    com.sun.faces.util.DebugUtil.waitForDebugger();

    if (null == kit) {
	System.out.println("Can't find default RenderKit!");
    }
    System.out.println("Got RenderKit: " + kit.getName());
    typeIt = kit.getSupportedComponentTypes();
    assertTrue(null != typeIt);
    
    while (typeIt.hasNext()) {
	type = (String) typeIt.next();
	try {
	    rendererIt = kit.getRendererNamesForComponent(type);
	}
	catch (FacesException e) {
	    System.out.println("FacesException!!! " + e.getMessage());
	}
	assertTrue(null != rendererIt);

	while(rendererIt.hasNext()) {
	    rendererName = (String) rendererIt.next();
	    	    assertTrue(null != rendererName);
	    try {
		renderer = kit.getRenderer(rendererName);
				assertTrue(null != renderer);
		System.out.println("\nFor Component Type \"" + type + 
				   "\"\n\tRendererName is \"" + 
				   rendererName + "\"" +
				   "\"\n\tRender is \"" + 
				   renderer.toString() + "\"");
	    }
	    catch (FacesException e) {
		System.out.println("FacesException!!! " + e.getMessage());
	    }
	}
    }
}

} // end of class TestRenderKit
