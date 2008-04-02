/*
 * $Id: TestUtil.java,v 1.21 2004/02/06 18:57:16 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
 * @version $Id: TestUtil.java,v 1.21 2004/02/06 18:57:16 rlubke Exp $
 * @see	Blah
 * @see	Bloo
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
