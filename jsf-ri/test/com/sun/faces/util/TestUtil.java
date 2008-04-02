/*
 * $Id: TestUtil.java,v 1.14 2003/10/06 22:48:11 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestUtil.java

package com.sun.faces.util;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.RIConstants;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import javax.servlet.ServletContext;

/**
 *
 *  <B>TestUtil</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUtil.java,v 1.14 2003/10/06 22:48:11 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestUtil extends ServletFacesTestCase
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

    public TestUtil() {super("TestUtil");}
    public TestUtil(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//

    public void testRenderPassthruAttributes() {
	try {
            RenderKitFactory renderKitFactory = (RenderKitFactory)
	        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	    RenderKit renderKit = renderKitFactory.getRenderKit("DEFAULT");
	    StringWriter sw = new StringWriter();
	    ResponseWriter writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
	    getFacesContext().setResponseWriter(writer);

	    UIInput input = new UIInput();
	    input.setId("testRenderPassthruAttributes");
	    input.getAttributes().put("notPresent", "notPresent");
	    input.getAttributes().put("onblur", "javascript:f.blur()");
	    input.getAttributes().put("onchange", "javascript:h.change()");
	    Util.renderPassThruAttributes(writer,input);
	    String expectedResult = " onblur=\"javascript:f.blur()\" onchange=\"javascript:h.change()\"";
	    assertEquals(expectedResult, sw.toString());

	    // verify no passthru attributes returns empty string
	    sw = new StringWriter();
	    writer = renderKit.createResponseWriter(sw,"text/html", "ISO-8859-1");
	    getFacesContext().setResponseWriter(writer);
	    input.getAttributes().remove("onblur");
	    input.getAttributes().remove("onchange");
	    Util.renderPassThruAttributes(writer, input);
	    assertTrue(0 == sw.toString().length());
	} catch (IOException e) {
	    assertTrue(false);
	}
    }

    public void testRenderBooleanPassthruAttributes() {
	try {
            RenderKitFactory renderKitFactory = (RenderKitFactory)
	        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	    RenderKit renderKit = renderKitFactory.getRenderKit("DEFAULT");
	    StringWriter sw = new StringWriter();
	    ResponseWriter writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
	    getFacesContext().setResponseWriter(writer);

	    UIInput input = new UIInput();
	    input.setId("testBooleanRenderPassthruAttributes");
	    input.getAttributes().put("disabled", "true");
	    input.getAttributes().put("readonly", "false");
	    Util.renderBooleanPassThruAttributes(writer, input);
	    String expectedResult = " disabled=\"disabled\"";
	    assertEquals(expectedResult, sw.toString());

	// verify no passthru attributes returns empty string
	    sw = new StringWriter();
	    writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
	    getFacesContext().setResponseWriter(writer);
	    input.getAttributes().remove("disabled");
	    input.getAttributes().remove("readonly");
	    Util.renderBooleanPassThruAttributes(writer, input);
	    assertTrue(0 == sw.toString().length());
	} catch (IOException e) {
	    assertTrue(false);
	}
    }

    public void testVerifyRequiredClasses() {
	ServletContext servletContext = (ServletContext)getFacesContext().getExternalContext().getContext();
	servletContext.removeAttribute(RIConstants.HAS_REQUIRED_CLASSES_ATTR);
	try {
	    Util.verifyRequiredClasses(getFacesContext());
	}
	catch (Throwable e) {
	    assertTrue(false);
	}

	try {
	    Util.verifyRequiredClasses(getFacesContext());
	}
	catch (Throwable e) {
	    assertTrue(false);
	}
    }

    /**
     * This method tests the <code>Util.getSessionMap</code> method.
     */
    public void testGetSessionMap() {
        // Test with null FacesContext
	//
	Map sessionMap = Util.getSessionMap(null);
	assertTrue(sessionMap != null);

	// Test with FacesContext
	//
	sessionMap = Util.getSessionMap(getFacesContext());
	assertTrue(sessionMap != null);

	// Test with no session
	//
	session.invalidate();
	sessionMap = Util.getSessionMap(getFacesContext());
	assertTrue(sessionMap != null);
    }

	
} // end of class TestUtil
