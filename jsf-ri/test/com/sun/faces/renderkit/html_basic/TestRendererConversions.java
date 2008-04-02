/*
 * $Id: TestRendererConversions.java,v 1.12 2004/02/26 20:34:38 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRendererConversions.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.ServletFacesTestCase;
import org.apache.cactus.WebRequest;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;


/**
 * <B>TestRendererConversions</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRendererConversions.java,v 1.12 2004/02/26 20:34:38 eburns Exp $
 */

public class TestRendererConversions extends ServletFacesTestCase {

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

    public void beginEmptyStrings(WebRequest theRequest) {
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

    public void testEmptyStrings() {
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
        } catch (Throwable e) {
            assertTrue(false);
        }
        assertTrue(text.isValid());
        assertTrue(hidden.isValid());
        assertTrue(secret.isValid());
    }


    public void beginNulls(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    }


    public void testNulls() {
        testEmptyStrings();
    }


    public void beginBadConversion(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    }


    public void testBadConversion() {
        UIComponent root = new UIViewRoot();
    }


} // end of class TestRendererConversions
