/*
 * $Id: TestRendererConversions.java,v 1.8 2003/10/02 00:40:18 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRendererConversions.java

package com.sun.faces.renderkit.html_basic;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;

import com.sun.faces.RIConstants;
import com.sun.faces.ServletFacesTestCase;


/**
 *
 *  <B>TestRendererConversions</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRendererConversions.java,v 1.8 2003/10/02 00:40:18 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRendererConversions extends ServletFacesTestCase
{
//
// Protected Constants
//
public static final String TEST_URI = "/components.jsp";

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

    public TestRendererConversions() {
	super("TestRendererConversions");
    }

    public TestRendererConversions(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//

public void beginEmptyStrings(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    theRequest.addParameter("number", "");
    theRequest.addParameter("date", "");
    theRequest.addParameter("text", "");
    theRequest.addParameter("hidden", "");
    theRequest.addParameter("secret", "");
}

public void setUp() {
    super.setUp();
    UIViewRoot page = new UIViewRoot();
    page.setViewId("viewId");    
    getFacesContext().setViewRoot(page);
 }
    /**

    * Test the built-in conversion for those renderers that have it.

    */

public void testEmptyStrings()
{
    UIViewRoot root = new UIViewRoot();
    UIInput 
	text = new UIInput(),
	hidden = new UIInput(),
	secret = new UIInput();
    
    text.setId("text");
    hidden.setId("hidden");
    secret.setId("secret");
    
    text.setRendererType("Text");
    hidden.setRendererType("Hidden");
    secret.setRendererType("Secret"); 
    
    root.getChildren().add(text);
    root.getChildren().add(hidden);
    root.getChildren().add(secret);
    TextRenderer textRenderer = new TextRenderer();
    HiddenRenderer hiddenRenderer = new HiddenRenderer();
    SecretRenderer secretRenderer = new SecretRenderer();

    try {
	textRenderer.decode(getFacesContext(), text);
	hiddenRenderer.decode(getFacesContext(), hidden);
	secretRenderer.decode(getFacesContext(), secret);
    }
    catch (Throwable e) {
        assertTrue(false);
    }
    assertTrue(text.isValid());
    assertTrue(hidden.isValid());
    assertTrue(secret.isValid());
}

public void beginNulls(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
}

public void testNulls()
{
    testEmptyStrings();
}


public void beginBadConversion(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
}

public void testBadConversion()
{
    UIComponent root = new UIViewRoot();
}




} // end of class TestRendererConversions
