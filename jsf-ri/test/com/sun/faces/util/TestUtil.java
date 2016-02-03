/*
 * $Id: TestUtil.java,v 1.22.32.2 2007/04/27 21:28:27 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

// TestUtil.java

package com.sun.faces.util;

import com.sun.faces.RIConstants;
import com.sun.faces.ServletFacesTestCase;

import javax.faces.FactoryFinder;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletContext;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * <B>TestUtil</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUtil.java,v 1.22.32.2 2007/04/27 21:28:27 ofung Exp $
 */

public class TestUtil extends ServletFacesTestCase {

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

    public TestUtil() {
        super("TestUtil");
    }


    public TestUtil(String name) {
        super(name);
    }

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
            RenderKit renderKit =
                renderKitFactory.getRenderKit(getFacesContext(),
                                              RenderKitFactory.HTML_BASIC_RENDER_KIT);
            StringWriter sw = new StringWriter();
            ResponseWriter writer = renderKit.createResponseWriter(sw,
                                                                   "text/html",
                                                                   "ISO-8859-1");
            getFacesContext().setResponseWriter(writer);

            UIInput input = new UIInput();
            input.setId("testRenderPassthruAttributes");
            input.getAttributes().put("notPresent", "notPresent");
            input.getAttributes().put("onblur", "javascript:f.blur()");
            input.getAttributes().put("onchange", "javascript:h.change()");
            Util.renderPassThruAttributes(writer, input);
            String expectedResult = " onblur=\"javascript:f.blur()\" onchange=\"javascript:h.change()\"";
            assertEquals(expectedResult, sw.toString());

            // verify no passthru attributes returns empty string
            sw = new StringWriter();
            writer =
                renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
            getFacesContext().setResponseWriter(writer);
            input.getAttributes().remove("onblur");
            input.getAttributes().remove("onchange");
            Util.renderPassThruAttributes(writer, input);
            assertTrue(0 == sw.toString().length());
        } catch (IOException e) {
            assertTrue(false);
        }
    }


    public void testRenderPassthruAttributesFromConcreteHtmlComponent() {
        try {
            RenderKitFactory renderKitFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit =
                renderKitFactory.getRenderKit(getFacesContext(),
                                              RenderKitFactory.HTML_BASIC_RENDER_KIT);
            StringWriter sw = new StringWriter();
            ResponseWriter writer = renderKit.createResponseWriter(sw,
                                                                   "text/html",
                                                                   "ISO-8859-1");
            getFacesContext().setResponseWriter(writer);

            HtmlInputText input = new HtmlInputText();
            input.setId("testRenderPassthruAttributes");
            input.setSize(12);
            Util.renderPassThruAttributes(writer, input);
            String expectedResult = " size=\"12\"";
            assertEquals(expectedResult, sw.toString());

            sw = new StringWriter();
            writer = renderKit.createResponseWriter(sw, "text/html",
                                                    "ISO-8859-1");
            input.setReadonly(true);
            Util.renderBooleanPassThruAttributes(writer, input);
            expectedResult = " readonly=\"readonly\"";
            assertEquals(expectedResult, sw.toString());


            // test that setting the values to the default value causes
            // the attributes to not be rendered.
            sw = new StringWriter();
            writer = renderKit.createResponseWriter(sw, "text/html",
                                                    "ISO-8859-1");
            input.setSize(Integer.MIN_VALUE);
            Util.renderPassThruAttributes(writer, input);
            expectedResult = "";
            assertEquals(expectedResult, sw.toString());

            sw = new StringWriter();
            writer = renderKit.createResponseWriter(sw, "text/html",
                                                    "ISO-8859-1");
            input.setReadonly(false);
            Util.renderBooleanPassThruAttributes(writer, input);
            expectedResult = "";
            assertEquals(expectedResult, sw.toString());


        } catch (IOException e) {
            assertTrue(false);
        }
    }


    public void testRenderBooleanPassthruAttributes() {
        try {
            RenderKitFactory renderKitFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit =
                renderKitFactory.getRenderKit(getFacesContext(),
                                              RenderKitFactory.HTML_BASIC_RENDER_KIT);
            StringWriter sw = new StringWriter();
            ResponseWriter writer = renderKit.createResponseWriter(sw,
                                                                   "text/html",
                                                                   "ISO-8859-1");
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
            writer =
                renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
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
        ServletContext servletContext = (ServletContext) getFacesContext()
            .getExternalContext()
            .getContext();
        servletContext.removeAttribute(RIConstants.HAS_REQUIRED_CLASSES_ATTR);
        try {
            Util.verifyRequiredClasses(getFacesContext());
        } catch (Throwable e) {
            assertTrue(false);
        }

        try {
            Util.verifyRequiredClasses(getFacesContext());
        } catch (Throwable e) {
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
