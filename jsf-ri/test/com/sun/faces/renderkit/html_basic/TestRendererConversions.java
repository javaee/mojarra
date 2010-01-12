/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// TestRendererConversions.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;
import org.apache.cactus.WebRequest;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import java.util.Locale;


/**
 * <B>TestRendererConversions</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
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
        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.setViewId("viewId");
        page.setLocale(Locale.US);
        getFacesContext().setViewRoot(page);
    }


    /**
     * Test the built-in conversion for those renderers that have it.
     */

    public void testEmptyStrings() {
        UIViewRoot root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        root.setLocale(Locale.US);
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
        UIComponent root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
    }


} // end of class TestRendererConversions
