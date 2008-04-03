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
