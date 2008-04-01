/*
 * $Id: HtmlBasicRenderContextFactory.java,v 1.1 2001/11/09 23:48:50 edburns Exp $
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

// HtmlBasicRenderContextFactory.java

package com.sun.faces.renderkit.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletRequest;
import javax.faces.RenderContextFactory;
import javax.faces.RenderContext;
import javax.faces.FacesException;


/**
 *
 *  <B>HtmlBasicRenderContextFactory</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HtmlBasicRenderContextFactory.java,v 1.1 2001/11/09 23:48:50 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class HtmlBasicRenderContextFactory extends RenderContextFactory
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

public HtmlBasicRenderContextFactory()
{
    super();
}

//
// Methods from RenderContextFactory
//

//
// Class methods
//

public RenderContext newRenderContext(ServletRequest request) throws FacesException {
    RenderContext result = new HtmlBasicRenderContext();
    return result;
}

//
// General Methods
//


// ----VERTIGO_TEST_START

//
// Test methods
//

public static void main(String [] args)
{
    Assert.setEnabled(true);
    /* PENDING(edburns): replace with log4j */
    Log.setApplicationName("HtmlBasicRenderContextFactory");
    Log.setApplicationVersion("0.0");
    Log.setApplicationVersionDate("$Id: HtmlBasicRenderContextFactory.java,v 1.1 2001/11/09 23:48:50 edburns Exp $");

    javax.faces.RenderKit kit = null;
    RenderContext context;
    RenderContextFactory factory;
    javax.faces.Renderer renderer;
    java.util.Iterator typeIt, rendererIt;
    String type, rendererName;
    
    try {
	factory = RenderContextFactory.newInstance();
	System.out.println("HtmlBasicRenderContextFactory: got factory: " + 
			   factory);
	context = factory.newRenderContext(null);
	System.out.println("HtmlBasicRenderContextFactory: got context: " + 
			   context);
	kit = context.getRenderKit();
	System.out.println("HtmlBasicRenderContextFactory: got renderKit: " + 
			   kit);
    }
    catch (Exception e) {
	System.out.println("Exception getting factory!!! " + e.getMessage());
    }
    
    if (null == kit) {
	System.out.println("Can't find default RenderKit!");
    }
    System.out.println("Got RenderKit: " + kit.getName());
    typeIt = kit.getSupportedComponentTypes();
    Assert.assert_it(null != typeIt);
    
    while (typeIt.hasNext()) {
	type = (String) typeIt.next();
	rendererIt = kit.getRendererNamesForComponent(type);
	Assert.assert_it(null != rendererIt);

	while(rendererIt.hasNext()) {
	    rendererName = (String) rendererIt.next();
	    Assert.assert_it(null != rendererName);
	    try {
		renderer = kit.getRenderer(rendererName);
		Assert.assert_it(null != renderer);
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

// ----VERTIGO_TEST_END

} // end of class HtmlBasicRenderContextFactory
