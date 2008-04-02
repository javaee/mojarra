/*
 * HtmlResponseWriterTest.java
 * JUnit based test
 *
 * Created on November 16, 2006, 2:59 PM
 */

package com.sun.faces.renderkit.html_basic;

import java.io.StringWriter;
import junit.framework.*;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

/**
 *
 * @author edburns
 */
public class HtmlResponseWriterTest extends TestCase {
    
    public HtmlResponseWriterTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testCDATAWithXHTML() throws Exception {
        System.out.println("startElement");
        
        String name = null;
        UIComponent componentForElement = new UIOutput();
        
        final String expected = "<script>\n//<![CDATA[\n\n function queueEvent() {\n  return false;\n}\n\n\n//]]>\n</script>";
 
        // Case 1 start is // end is //
        StringWriter stringWriter = new StringWriter();
        HtmlResponseWriter instance = new HtmlResponseWriter(stringWriter,
                "application/xhtml+xml", "UTF-8");

        instance.startElement("script", componentForElement);
        instance.write("    // <![CDATA[\n function queueEvent() {\n  return false;\n}\n\n//   ]]>  \n");
        instance.endElement("script");
        
        instance.flush();
        assertEquals(expected, stringWriter.toString());

        // Case 2 start is // end is /* */
        stringWriter = new StringWriter();
        instance = new HtmlResponseWriter(stringWriter,
                "application/xhtml+xml", "UTF-8");
 
        instance.startElement("script", componentForElement);
        instance.write("    // <![CDATA[\n function queueEvent() {\n  return false;\n}\n\n/*\n  ]]> \n*/ \n");
        instance.endElement("script");
        
        instance.flush();
        assertEquals(expected, stringWriter.toString());

        // Case 3 start is /* */  end is /* */
        stringWriter = new StringWriter();
        instance = new HtmlResponseWriter(stringWriter,
                "application/xhtml+xml", "UTF-8");
 
        instance.startElement("script", componentForElement);
        instance.write("    /* \n <![CDATA[ \n*/\n function queueEvent() {\n  return false;\n}\n\n/*\n  ]]> \n*/ \n");
        instance.endElement("script");
        
        instance.flush();
        assertEquals(expected, stringWriter.toString());

        // Case 4 start is /* */  end is //
        stringWriter = new StringWriter();
        instance = new HtmlResponseWriter(stringWriter,
                "application/xhtml+xml", "UTF-8");
 
        instance.startElement("script", componentForElement);
        instance.write("    /* \n <![CDATA[ \n*/\n function queueEvent() {\n  return false;\n}\n\n//\n  ]]>\n");
        instance.endElement("script");
        
        instance.flush();
        assertEquals(expected, stringWriter.toString());
        
        // Case 5 start is /* */  end is //
        stringWriter = new StringWriter();
        instance = new HtmlResponseWriter(stringWriter,
                "application/xhtml+xml", "UTF-8");
 
        instance.startElement("script", componentForElement);
        instance.write("    /* \n <![CDATA[ \n*/\n function queueEvent() {\n  return false;\n}\n\n//\n  ]]>\n");
        instance.endElement("script");
        
        instance.flush();
        assertEquals(expected, stringWriter.toString());
        
        
    }

    
}
