/*
 * $Id: TestRenderKit.java,v 1.11 2002/04/11 22:52:42 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderKit.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.FacesTestCase;

import javax.servlet.http.HttpSession;

import javax.faces.RenderKit;
import javax.faces.Constants;
import javax.faces.AbstractFactory;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import javax.faces.FacesContext;
import javax.faces.FacesException;
import com.sun.faces.FacesTestCase;

/**
 *
 *  <B>TestRenderKit</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderKit.java,v 1.11 2002/04/11 22:52:42 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderKit extends FacesTestCase
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
    FacesContext context;
    AbstractFactory abstractFactory = new AbstractFactory();
    javax.faces.Renderer renderer;
    java.util.Iterator typeIt, rendererIt = null;
    String type, rendererName;
    boolean result = false;
    
    try {
	context = abstractFactory.newFacesContext(request, response);
	System.out.println("HtmlBasicFacesContextFactory: got context: " + 
			   context);
	// PENDING(edburns): test for context's clientCaps, and locale.

	kit = context.getRenderKit();
	System.out.println("HtmlBasicFacesContextFactory: got renderKit: " + 
			   kit);
    }
    catch (Exception e) {
	System.out.println("Exception getting factory!!! " + e.getMessage());
	assertTrue(false);
    }
    assertTrue(true);

    if (null == kit) {
	System.out.println("Can't find default RenderKit!");
    }
    System.out.println("Got RenderKit: " + kit.getName());
    typeIt = kit.getSupportedComponentTypes();
    assertTrue(null != typeIt);
    
    while (typeIt.hasNext()) {
	type = (String) typeIt.next();
	try {
	    rendererIt = kit.getRendererTypesForComponent(type);
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
                assertTrue(false);
	    }
	}
    }
}

} // end of class TestRenderKit
