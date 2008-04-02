/*
 * $Id: TestHtmlResponseWriter.java,v 1.5 2003/12/17 15:15:37 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestHtmlResponseWriter.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import java.io.StringWriter;
import java.util.Iterator;


import java.io.IOException;

import junit.framework.TestCase;

import org.apache.cactus.ServletTestCase;
import com.sun.faces.util.Util;

/**
 *
 *  <B>TestHtmlResponseWriter.java</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestHtmlResponseWriter.java,v 1.5 2003/12/17 15:15:37 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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

    public TestHtmlResponseWriter() {super("TestHtmlResponseWriter.java");}
    public TestHtmlResponseWriter(String name) {super(name);}

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
	renderKit = renderKitFactory.getRenderKit("DEFAULT");
	sw = new StringWriter();
        writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
    }

    public void testContentType() {
        assertTrue(writer.getContentType().equals("text/html"));

	// Test Invalid Encoding
	boolean exceptionThrown = false;
	try {
	    writer = renderKit.createResponseWriter(sw, "foobar", "ISO-8859-1");
	} catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
    }

    public void testEncoding() {
        assertTrue(writer.getCharacterEncoding().equals("ISO-8859-1"));

	// Test Invalid Encoding
	boolean exceptionThrown = false;
	try {
	    writer = renderKit.createResponseWriter(sw, "text/html", "foobar");
	} catch (FacesException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
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
            writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
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
	    assertTrue(sw.toString().equals("<input type="+"\"text\""));
	    Boolean bool = new Boolean("true");
	    writer.writeAttribute("readonly", bool, "readonly");
	    assertTrue(sw.toString().equals("<input type="+"\"text\""+" readonly")); 
	    //
	    //Assert that boolean "false" values don't get written out
	    //
	    bool = new Boolean("false");
	    writer.writeAttribute("disabled", bool, "disabled");
	    assertTrue(sw.toString().equals("<input type="+"\"text\""+" readonly")); 
	    //
	    //Assert correct escape char
	    //
	    writer.writeAttribute("greaterthan", ">", "greaterthan");
	    assertTrue(sw.toString().equals("<input type="+"\"text\""+" readonly"+
	        " greaterthan="+"\"&gt;\"")); 
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
	    assertTrue(sw.toString().equals("<input type="+"\"image\""+
	        " src="+"\"/mygif/foo.gif\""+" />")); 
	    //
	    // test URL encoding
	    //
	    sw = new StringWriter();
            writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
	    writer.startElement("foo", new UIInput());
	    writer.writeURIAttribute("player","Bobby Orr", "player");
	    assertTrue(sw.toString().equals("<foo player="+"\"Bobby+Orr\""));
	    //
	    // test no URL encoding (javascript)
	    //
	    sw = new StringWriter();
            writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
	    writer.startElement("foo", new UIInput());
	    writer.writeURIAttribute("player","javascript:Bobby Orr", null);
	    assertTrue(sw.toString().equals("<foo player="+"\"javascript:Bobby Orr\""));
	} catch (IOException e) {
	    assertTrue(false);
        }
    }

    public void testWriteComment() {
        try {
            writer.writeComment("This is a comment");
	    assertTrue(sw.toString().equals("<!-- This is a comment -->"));
	} catch(IOException e) {
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
            writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
	    char[] carr1 = {'a','b','c','d','e'}; 
            writer.writeText(carr1, 0, 2);
	    assertTrue(sw.toString().equals("ab"));
	    sw = new StringWriter();
            writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
            writer.writeText(carr1, 0, 0);
	    assertTrue(sw.toString().equals(""));

	    boolean exceptionThrown = false;
	    try {
                writer.writeText(carr1,-1,3);
            } catch (IndexOutOfBoundsException iob) {
	        exceptionThrown = true;
	    }
	    assertTrue(exceptionThrown);
	    exceptionThrown = false;
	    try {
                writer.writeText(carr1,10,3);
            } catch (IndexOutOfBoundsException iob) {
	        exceptionThrown = true;
	    }
	    assertTrue(exceptionThrown);
	    exceptionThrown = false;
	    try {
                writer.writeText(carr1,2,-1);
            } catch (IndexOutOfBoundsException iob) {
	        exceptionThrown = true;
	    }
	    assertTrue(exceptionThrown);
	    exceptionThrown = false;
	    try {
                writer.writeText(carr1,2,10);
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
	assertTrue(exceptionThrown);
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
