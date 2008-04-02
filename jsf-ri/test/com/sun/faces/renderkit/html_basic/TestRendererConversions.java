/*
 * $Id: TestRendererConversions.java,v 1.1 2003/02/18 23:05:15 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIInput;

import com.sun.faces.RIConstants;
import com.sun.faces.ServletFacesTestCase;

/**
 *
 *  <B>TestRendererConversions</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRendererConversions.java,v 1.1 2003/02/18 23:05:15 eburns Exp $
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

    /**

    * Test the built-in conversion for those renderers that have it.

    */

public void testEmptyStrings()
{
    UIComponent root = new UINamingContainer() {
	    public String getComponentType() { return "root"; }
        };
    UIInput 
	number = new UIInput(),
	date = new UIInput(),
	text = new UIInput(),
	hidden = new UIInput(),
	secret = new UIInput();
    root.addChild(number);
    root.addChild(date);
    root.addChild(text);
    root.addChild(hidden);
    root.addChild(secret);
    NumberRenderer numberRenderer = new NumberRenderer();
    DateRenderer dateRenderer = new DateRenderer();
    TextRenderer textRenderer = new TextRenderer();
    HiddenRenderer hiddenRenderer = new HiddenRenderer();
    SecretRenderer secretRenderer = new SecretRenderer();

    number.setComponentId("number");
    date.setComponentId("date");
    text.setComponentId("text");
    hidden.setComponentId("hidden");
    secret.setComponentId("secret");
    try {
	numberRenderer.decode(getFacesContext(), number);
	dateRenderer.decode(getFacesContext(), date);
	textRenderer.decode(getFacesContext(), text);
	hiddenRenderer.decode(getFacesContext(), hidden);
	secretRenderer.decode(getFacesContext(), secret);
    }
    catch (Throwable e) {
	assertTrue(false);
    }
    assertTrue(number.isValid());
    assertTrue(date.isValid());
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
    theRequest.addParameter("number", "oeuoeu");
    theRequest.addParameter("date", "oeuoeuoue");
}

    /**

    * Test the built-in conversion for those renderers that have it.

    */

public void testBadConversion()
{
    UIComponent root = new UINamingContainer() {
	    public String getComponentType() { return "root"; }
        };
    UIInput 
	number = new UIInput(),
	date = new UIInput();
    root.addChild(number);
    root.addChild(date);
    NumberRenderer numberRenderer = new NumberRenderer();
    DateRenderer dateRenderer = new DateRenderer();

    number.setComponentId("number");
    date.setComponentId("date");
    try {
	numberRenderer.decode(getFacesContext(), number);
	dateRenderer.decode(getFacesContext(), date);
    }
    catch (Throwable e) {
	assertTrue(false);
    }
    assertTrue(!number.isValid());
    assertTrue(!date.isValid());
}




} // end of class TestRendererConversions
