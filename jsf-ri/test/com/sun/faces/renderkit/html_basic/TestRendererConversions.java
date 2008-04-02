/*
 * $Id: TestRendererConversions.java,v 1.16 2006/03/29 22:39:47 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// TestRendererConversions.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;
import org.apache.cactus.WebRequest;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;


/**
 * <B>TestRendererConversions</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRendererConversions.java,v 1.16 2006/03/29 22:39:47 rlubke Exp $
 */

public class TestRendererConversions extends ServletFacesTestCase {

    public static final String TEST_URI = "/components.jsp";


    // ------------------------------------------------------------ Constructors


    public TestRendererConversions() {

        super("TestRendererConversions");

    }


    public TestRendererConversions(String name) {

        super(name);

    }


    // ---------------------------------------------------------- Public Methods


    public void beginBadConversion(WebRequest theRequest) {

        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);

    }


    public void beginEmptyStrings(WebRequest theRequest) {

        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
        theRequest.addParameter("number", "");
        theRequest.addParameter("date", "");
        theRequest.addParameter("text", "");
        theRequest.addParameter("hidden", "");
        theRequest.addParameter("secret", "");

    }


    public void beginNulls(WebRequest theRequest) {

        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);

    }


    public void setUp() {

        super.setUp();
        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.setViewId("viewId");
        getFacesContext().setViewRoot(page);

    }


    public void testBadConversion() {

        UIComponent root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);

    }


    /**
     * Test the built-in conversion for those renderers that have it.
     */

    public void testEmptyStrings() {

        UIViewRoot root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
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


    public void testNulls() {

        testEmptyStrings();

    }

} // end of class TestRendererConversions
