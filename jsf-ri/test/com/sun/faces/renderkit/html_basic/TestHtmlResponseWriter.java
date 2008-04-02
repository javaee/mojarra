/*
 * $Id: TestHtmlResponseWriter.java,v 1.20 2006/03/29 23:05:01 rlubke Exp $
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

// TestHtmlResponseWriter.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.cactus.ServletFacesTestCase;
import junit.framework.TestCase;
import org.apache.cactus.ServletTestCase;

import javax.faces.FactoryFinder;
import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import java.io.IOException;
import java.io.StringWriter;

/**
 * <B>TestHtmlResponseWriter.java</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestHtmlResponseWriter.java,v 1.20 2006/03/29 23:05:01 rlubke Exp $
 */

public class TestHtmlResponseWriter extends ServletFacesTestCase // ServletTestCase
{

//
// Protected Constants
//

// Class Variables
//

//
// Instance Variables
//
    private ResponseWriter writer = null;
    private RenderKit renderKit = null;
    private StringWriter sw = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestHtmlResponseWriter() {
        super("TestHtmlResponseWriter.java");
    }


    public TestHtmlResponseWriter(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//
    public void setUp() {
        super.setUp();
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = renderKitFactory.getRenderKit(getFacesContext(),
                                                  RenderKitFactory.HTML_BASIC_RENDER_KIT);
        sw = new StringWriter();
        writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
    }


    public void testContentType() {
        assertTrue(writer.getContentType().equals("text/html"));

        // Test Invalid Encoding
        try {
            writer =
                renderKit.createResponseWriter(sw, "foobar", "ISO-8859-1");
            fail("IllegalArgumentException Should Have been Thrown!");
        } catch (IllegalArgumentException e) {
        }
    }


    public void testEncoding() {
        assertTrue(writer.getCharacterEncoding().equals("ISO-8859-1"));

        // Test Invalid Encoding
        try {
            writer = renderKit.createResponseWriter(sw, "text/html", "foobar");
            fail("IllegalArgumentException Should Have been Thrown!");
        } catch (IllegalArgumentException e) {
        }
    }


    // Test "startElement method including the automatic closure of a
    // previous "start element"
    //
    public void testStartElement() {
        try {
            writer.startElement("input", new UIInput());
            assertTrue(sw.toString().equals("<input"));
            writer.startElement("select", new UIInput());
            assertTrue(sw.toString().equals("<input><select"));
        } catch (IOException e) {
            assertTrue(false);
        }
    }


    // Test "endElement" method
    // 
    public void testEndElement() {
        try {
            writer.endElement("input");
            assertTrue(sw.toString().equals("</input>"));
            writer.startElement("frame", new UIInput());
            writer.endElement("frame");
            assertTrue(sw.toString().equals("</input><frame />"));
            writer.endElement("frame");
            assertTrue(sw.toString().equals("</input><frame /></frame>"));
            sw = new StringWriter();
            writer =
                renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
            writer.startElement("br", null);
            writer.endElement("br");
            assertTrue(sw.toString().equals("<br />"));

        } catch (IOException e) {
            assertTrue(false);
        }
    }


    // Test "writeAttribute" method
    //
    public void testWriteAttribute() {
        try {
            writer.startElement("input", new UIInput());
            writer.writeAttribute("type", "text", "type");
            assertTrue(sw.toString().equals("<input type=" + "\"text\""));
            Boolean bool = new Boolean("true");
            writer.writeAttribute("readonly", bool, "readonly");
            assertTrue(
                sw.toString().equals("<input type=" + "\"text\"" + " readonly=\"readonly\""));
            //
            //Assert that boolean "false" values don't get written out
            //
            bool = new Boolean("false");
            writer.writeAttribute("disabled", bool, "disabled");
            assertTrue(
                sw.toString().equals("<input type=" + "\"text\"" + " readonly=\"readonly\""));
            //
            //Assert correct escape char
            //
            writer.writeAttribute("greaterthan", ">", "greaterthan");
            assertTrue(sw.toString().equals("<input type=" + "\"text\"" +
                                            " readonly=\"readonly\"" +
                                            " greaterthan=" + "\"&gt;\""));
        } catch (IOException e) {
            assertTrue(false);
        }
    }


    //
    // Test "writeURIAttribute" method
    //
    public void testWriteURIAttribute() {
        try {
            writer.startElement("input", new UIInput());
            writer.writeAttribute("type", "image", "type");
            writer.writeURIAttribute("src", "/mygif/foo.gif", "src");
            writer.endElement("input");
            assertTrue(sw.toString().equals("<input type=" + "\"image\"" +
                                            " src=" + "\"/mygif/foo.gif\"" + " />"));
            //
            // test URL encoding
            //
            sw = new StringWriter();
            writer =
                renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
            writer.startElement("foo", new UIInput());
            writer.writeURIAttribute("player", "Bobby Orr", "player");
            assertTrue(sw.toString().equals("<foo player=" + "\"Bobby+Orr\""));
            //
            // test no URL encoding (javascript)
            //
            sw = new StringWriter();
            writer =
                renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
            writer.startElement("foo", new UIInput());
            writer.writeURIAttribute("player", "javascript:Bobby Orr", null);
            assertTrue(
                sw.toString().equals("<foo player=" +
                                     "\"javascript:Bobby Orr\""));
        } catch (IOException e) {
            assertTrue(false);
        }
    }


    public void testWriteComment() {
        try {
            writer.writeComment("This is a comment");
            assertTrue(sw.toString().equals("<!--This is a comment-->"));
        } catch (IOException e) {
            assertTrue(false);
        }
    }


    //
    // Test variations of the writeText method..
    //
    public void testWriteText() {
        try {
            //----------------------------
            // test Object param flavor...
            //----------------------------
            StringBuffer sb = new StringBuffer("Some & Text");
            writer.writeText(sb, null);
            assertTrue(sw.toString().equals("Some &amp; Text"));
            //-----------------------------------------
            // test char[], offset, len param flavor...
            //-----------------------------------------
            sw = new StringWriter();
            writer =
                renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
            char[] carr1 = {'a', 'b', 'c', 'd', 'e'};
            writer.writeText(carr1, 0, 2);
            assertTrue(sw.toString().equals("ab"));
            sw = new StringWriter();
            writer =
                renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
            writer.writeText(carr1, 0, 0);
            assertTrue(sw.toString().equals(""));

            boolean exceptionThrown = false;
            try {
                writer.writeText(carr1, -1, 3);
            } catch (IndexOutOfBoundsException iob) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
            exceptionThrown = false;
            try {
                writer.writeText(carr1, 10, 3);
            } catch (IndexOutOfBoundsException iob) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
            exceptionThrown = false;
            try {
                writer.writeText(carr1, 2, -1);
            } catch (IndexOutOfBoundsException iob) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
            exceptionThrown = false;
            try {
                writer.writeText(carr1, 2, 10);
            } catch (IndexOutOfBoundsException iob) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        } catch (IOException e) {
            assertTrue(false);
        }
    }


    //
    // Test Null Argument Exceptions
    //
    public void testNullArgExceptions() {
        boolean exceptionThrown = false;
        try {
            writer.startElement(null, null);
        } catch (IOException e) {
            assertTrue(false);
        } catch (NullPointerException npe) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        exceptionThrown = false;
        try {
            writer.endElement(null);
        } catch (IOException e) {
            assertTrue(false);
        } catch (NullPointerException npe) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        exceptionThrown = false;
        try {
            writer.writeAttribute("foo", null, null);
        } catch (IOException e) {
            assertTrue(false);
        } catch (NullPointerException npe) {
            exceptionThrown = true;
        }
        assertTrue(!exceptionThrown);
        exceptionThrown = false;
        try {
            writer.writeAttribute(null, "bar", null);
        } catch (IOException e) {
            assertTrue(false);
        } catch (NullPointerException npe) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        exceptionThrown = false;
        try {
            writer.writeURIAttribute("foo", null, null);
        } catch (IOException e) {
            assertTrue(false);
        } catch (NullPointerException npe) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        exceptionThrown = false;
        try {
            writer.writeURIAttribute(null, "bar", null);
        } catch (IOException e) {
            assertTrue(false);
        } catch (NullPointerException npe) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        exceptionThrown = false;
        try {
            writer.writeComment(null);
        } catch (IOException e) {
            assertTrue(false);
        } catch (NullPointerException npe) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        exceptionThrown = false;
        try {
            writer.writeText(null, null);
        } catch (IOException e) {
            assertTrue(false);
        } catch (NullPointerException npe) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
} // end of class TestHtmlResponseWriter
