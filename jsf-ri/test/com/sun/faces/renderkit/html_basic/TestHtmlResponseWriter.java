/*
 * $Id: TestHtmlResponseWriter.java,v 1.30 2008/02/27 17:09:05 rlubke Exp $
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

// TestHtmlResponseWriter.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.FactoryFinder;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import com.sun.faces.cactus.ServletFacesTestCase;

/**
 * <B>TestHtmlResponseWriter.java</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestHtmlResponseWriter.java,v 1.30 2008/02/27 17:09:05 rlubke Exp $
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
            UIInput in = new UIInput();
            writer.startElement("input", in);
            writer.writeAttribute("type", "text", "type");
            writer.writeAttribute("readonly", Boolean.TRUE, "readonly");
            writer.writeAttribute("disabled", Boolean.FALSE, "disabled");
            writer.writeAttribute("greaterthan", ">", "greaterthan");
            writer.endElement("input");
            assertTrue(sw.toString().equals("<input type=" + "\"text\"" +
                                            " readonly=\"readonly\"" +
                                            " greaterthan=" + "\"&gt;\" />"));
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
            writer.endElement("input");
            assertTrue(sw.toString().equals("<foo player=" + "\"Bobby+Orr\" />"));
            //
            // test no URL encoding (javascript)
            //
            sw = new StringWriter();
            writer =
                renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
            writer.startElement("foo", new UIInput());
            writer.writeURIAttribute("player", "javascript:Bobby Orr", null);
            writer.endElement("foo");
            assertTrue(
                sw.toString().equals("<foo player=" +
                                     "\"javascript:Bobby Orr\"></foo>"));
        } catch (IOException e) {
            assertTrue(false);
        }
    }
    
    
    public void testWriteCdata() {
        sw = new StringWriter();
        try {
            writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
            writer.startElement("cdata", new UIOutput());
            // should be ignored
            writer.writeAttribute("id", "value", "id");
            // should be ignored
            writer.writeURIAttribute("id", "value", "id");
            // should be ignored
            writer.writeComment("comment");
            // should be present in the StringWriter
            writer.writeText("Text between the cdata section", null);            
            writer.endElement("cdata");
            assertTrue(
                  sw.toString().equals("<![CDATA[Text between the cdata section]]>"));
        } catch (IOException e) {
            assertTrue(false);
        }

        // cdata with script
        sw = new StringWriter();
        try {
            writer = renderKit.createResponseWriter(sw, "application/xhtml+xml", "UTF-8");
            writer.startElement("cdata", new UIOutput());
            writer.startElement("script", new UIOutput());
            writer.writeText("alert('hello');", null);
            writer.endElement("script");
            writer.endElement("cdata");
            assertTrue("<![CDATA[<script>alert('hello');</script>]]>".equals(sw.toString()));
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

    public void testWriteScriptElement() throws Exception {
        StringWriter sw = new StringWriter();
        StringWriter swx = new StringWriter();
        writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
        ResponseWriter xmlWriter = renderKit.createResponseWriter(swx, "application/xhtml+xml", "UTF-8");
        UIOutput output = new UIOutput();
        writer.startElement("script", output);
        writer.writeURIAttribute("src", "http://foo.net/some.js", "src");
        writer.writeAttribute("type", "text/javascript", "type");
        writer.writeAttribute("language", "Javascript", "language");
        writer.endElement("script");
        String result = sw.toString();
        System.out.println(result);
        assertTrue((!result.contains("<!--") && !result.contains("-->")));

        xmlWriter.startElement("script", output);
        xmlWriter.writeAttribute("src", "http://foo.net/some.js", "src");
        xmlWriter.writeAttribute("type", "text/javascript", "type");
        xmlWriter.writeAttribute("language", "Javascript", "language");
        xmlWriter.endElement("script");
        result = swx.toString();
        System.out.println(result);
        assertTrue((!result.contains("<[CDATA[") && !result.contains("]]>")));

        sw = new StringWriter();
        swx = new StringWriter();
        writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
        xmlWriter = renderKit.createResponseWriter(swx, "application/xhtml+xml", "UTF-8");
        output = new UIOutput();
        writer.startElement("script", output);
        writer.writeAttribute("type", "text/javascript", "type");
        writer.writeAttribute("language", "Javascript", "language");
        writer.writeText("<!-- alert('foo') //-->", null);
        writer.endElement("script");
        result = sw.toString();
        String expected = "<script type=\"text/javascript\" language=\"Javascript\">\n" +
             "<!--\n" +
             " alert('foo') \n" +
             "//-->\n" +
             "</script>";
        System.out.println("1:" + result);
        assertTrue(expected.equals(result));
        

        xmlWriter.startElement("script", output);
        xmlWriter.writeAttribute("type", "text/javascript", "type");
        xmlWriter.writeAttribute("language", "Javascript", "language");
        xmlWriter.writeText("//<![CDATA[ alert('foo') //]]>", null);
        xmlWriter.endElement("script");
        result = swx.toString();
        expected = "<script type=\"text/javascript\" language=\"Javascript\">\n" +
             "//<![CDATA[\n" +
             " alert('foo') \n" +
             "//]]>\n" +
             "</script>";
        System.out.println("2:" + result);
        assertTrue(expected.equals(result));

        swx = new StringWriter();
        xmlWriter = renderKit.createResponseWriter(swx, "application/xhtml+xml", "UTF-8");
        xmlWriter.startElement("ajax", output);
        xmlWriter.startElement("cdata", output);
        xmlWriter.startElement("script", output);
        xmlWriter.writeAttribute("type", "text/javascript", "type");
        xmlWriter.writeAttribute("language", "Javascript", "language");
        xmlWriter.writeText("if (true && true) { alert('foo'); }", null);
        xmlWriter.endElement("script");
        xmlWriter.endElement("cdata");
        xmlWriter.endElement("ajax");
        result = swx.toString();
        expected = "<ajax><![CDATA[<script type=\"text/javascript\" language=\"Javascript\">if (true && true) { alert('foo'); }</script>]]></ajax>";
        System.out.println("3:" + result);
        assertTrue(expected.equals(result));

    }

    public void testWriteStyleElement() throws Exception {
        StringWriter sw = new StringWriter();
        StringWriter swx = new StringWriter();
        writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
        ResponseWriter xmlWriter = renderKit.createResponseWriter(swx, "application/xhtml+xml", "UTF-8");
        UIOutput output = new UIOutput();
        writer.startElement("style", output);
        writer.writeAttribute("src", "http://foo.net/some.css", "src");
        writer.writeAttribute("type", "text/css", "type");
        writer.endElement("style");
        String result = sw.toString();
        System.out.println(result);
        assertTrue((!result.contains("<!--") && !result.contains("-->")));

        xmlWriter.startElement("style", output);
        xmlWriter.writeAttribute("src", "http://foo.net/some.css", "src");
        xmlWriter.writeAttribute("type", "text/css", "type");
        xmlWriter.endElement("style");
        result = swx.toString();
        System.out.println(result);
        assertTrue((!result.contains("<![CDATA[") && !result.contains("]]>")));

        sw = new StringWriter();
        swx = new StringWriter();
        writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
        xmlWriter = renderKit.createResponseWriter(swx, "application/xhtml+xml", "UTF-8");
        writer.startElement("style", output);
        writer.writeAttribute("type", "text/css", "type");
        writer.write(".h1 { color: red }");
        writer.endElement("style");
        result = sw.toString();
        System.out.println(result);
        assertTrue((result.contains("<!--") && result.contains("-->")));

        xmlWriter.startElement("style", output);
        xmlWriter.writeAttribute("type", "text/css", "type");
        xmlWriter.write(".h1 { color: red }");
        xmlWriter.endElement("style");
        result = swx.toString();
        System.out.println(result);
        assertTrue((result.contains("<![CDATA[") && result.contains("]]>")));

        sw = new StringWriter();
        swx = new StringWriter();
        writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
        xmlWriter = renderKit.createResponseWriter(swx, "application/xhtml+xml", "UTF-8");
        writer.startElement("style", output);
        writer.writeAttribute("type", "text/css", "type");
        writer.write("<!-- .h1 { color: red } //-->");
        writer.endElement("style");
        result = sw.toString();
        System.out.println("3:" +result);
        String expected = "<style type=\"text/css\">\n" +
             "<!--\n" +
             " .h1 { color: red } \n" +
             "//-->\n" +
             "</style>";
        assertTrue(expected.equals(result));

        xmlWriter.startElement("style", output);
        xmlWriter.writeAttribute("type", "text/css", "type");
        xmlWriter.write("<![CDATA[ .h1 { color: red } ]]>");
        xmlWriter.endElement("style");
        result = swx.toString();
        System.out.println("4:" +result);
        expected = "<style type=\"text/css\">\n" +
             "<![CDATA[\n" +
             " .h1 { color: red } \n" +
             "]]>\n" +
             "</style>";
        assertTrue(expected.equals(result));
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

    /**
     * Added for issue 704.
     */
    public void testWriteDecRefRegressionTest() throws Exception {
        Character c = '\u4300';
        String test = c.toString();
        sw = new StringWriter();
        writer = renderKit.createResponseWriter(sw, "text/html", "UTF-8");
        writer.writeText(test, "value");
        assertTrue("&#17152;", "&#17152;".equals(sw.toString()));
    }


    /**
     * Added for issue 705.
     */
    public void testAttributesSumGreaterThan1024RegresssionTest() throws Exception {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < 2046; i++) {
            value.append('a');
        }
        sw = new StringWriter();
        writer = renderKit.createResponseWriter(sw, "text/html", "UTF-8");
        writer.startElement("input", null);
        writer.writeAttribute("onclick", value.toString(), "onclick");
        writer.endElement("input");
        StringBuilder control = new StringBuilder();
        control.append("<input onclick=\"").append(value.toString()).append("\" />");
        assertTrue(sw.toString(), control.toString().trim().equals(sw.toString().trim()));

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
