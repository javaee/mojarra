/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
package com.sun.faces.renderkit.html_basic;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class HtmlResponseWriterTest {

    /**
     * Test cloneWithWriter method.
     */
    @Test
    @Ignore
    public void testCloneWithWriter() throws Exception {
        Writer writer = new StringWriter();
        HtmlResponseWriter responseWriter = new HtmlResponseWriter(writer, "text/html", "UTF-8");
        Field field = responseWriter.getClass().getDeclaredField("dontEscape");
        field.setAccessible(true);
        field.set(responseWriter, Boolean.TRUE);

        HtmlResponseWriter clonedWriter = (HtmlResponseWriter) responseWriter.cloneWithWriter(writer);
        assertTrue((Boolean) field.get(clonedWriter));

        responseWriter = new HtmlResponseWriter(writer, "text/html", "UTF-8");
        field.set(responseWriter, Boolean.FALSE);

        clonedWriter = (HtmlResponseWriter) responseWriter.cloneWithWriter(writer);
        assertFalse((Boolean) field.get(clonedWriter));
    }

    /**
     * Test cloneWithWriter method.
     */
    @Test
    @Ignore
    public void testCloneWithWriter2() throws Exception {
        Writer writer = new StringWriter();
        HtmlResponseWriter responseWriter = new HtmlResponseWriter(writer, "text/html", "UTF-8");
        Field field = responseWriter.getClass().getDeclaredField("writingCdata");
        field.setAccessible(true);
        field.set(responseWriter, Boolean.TRUE);

        HtmlResponseWriter clonedWriter = (HtmlResponseWriter) responseWriter.cloneWithWriter(writer);
        assertTrue((Boolean) field.get(clonedWriter));

        responseWriter = new HtmlResponseWriter(writer, "text/html", "UTF-8");
        field.set(responseWriter, Boolean.FALSE);

        clonedWriter = (HtmlResponseWriter) responseWriter.cloneWithWriter(writer);
        assertFalse((Boolean) field.get(clonedWriter));
    }

    /**
     * Test CDATA.
     */
    @Test
    @Ignore
    public void testCDATAWithXHTML() throws Exception {
        UIComponent componentForElement = new UIOutput();
        String expected = "<script>\n//<![CDATA[\n\n function queueEvent() {\n  return false;\n}\n\n\n//]]>\n</script>";

        // Case 1 start is // end is //
        StringWriter stringWriter = new StringWriter();
        HtmlResponseWriter responseWriter = new HtmlResponseWriter(stringWriter, "application/xhtml+xml", "UTF-8");
        responseWriter.startElement("script", componentForElement);
        responseWriter.write("    // <![CDATA[\n function queueEvent() {\n  return false;\n}\n\n//   ]]>  \n");
        responseWriter.endElement("script");
        responseWriter.flush();
        assertEquals(expected, stringWriter.toString());

        // Case 2 start is // end is /* */
        stringWriter = new StringWriter();
        responseWriter = new HtmlResponseWriter(stringWriter, "application/xhtml+xml", "UTF-8");
        responseWriter.startElement("script", componentForElement);
        responseWriter.write("    // <![CDATA[\n function queueEvent() {\n  return false;\n}\n\n/*\n  ]]> \n*/ \n");
        responseWriter.endElement("script");
        responseWriter.flush();
        assertEquals(expected, stringWriter.toString());

        // Case 3 start is /* */  end is /* */
        stringWriter = new StringWriter();
        responseWriter = new HtmlResponseWriter(stringWriter, "application/xhtml+xml", "UTF-8");
        responseWriter.startElement("script", componentForElement);
        responseWriter.write("    /* \n <![CDATA[ \n*/\n function queueEvent() {\n  return false;\n}\n\n/*\n  ]]> \n*/ \n");
        responseWriter.endElement("script");
        responseWriter.flush();
        assertEquals(expected, stringWriter.toString());

        // Case 4 start is /* */  end is //
        stringWriter = new StringWriter();
        responseWriter = new HtmlResponseWriter(stringWriter, "application/xhtml+xml", "UTF-8");
        responseWriter.startElement("script", componentForElement);
        responseWriter.write("    /* \n <![CDATA[ \n*/\n function queueEvent() {\n  return false;\n}\n\n//\n  ]]>\n");
        responseWriter.endElement("script");
        responseWriter.flush();
        assertEquals(expected, stringWriter.toString());

        // Case 5 start is /* */  end is //
        stringWriter = new StringWriter();
        responseWriter = new HtmlResponseWriter(stringWriter, "application/xhtml+xml", "UTF-8");
        responseWriter.startElement("script", componentForElement);
        responseWriter.write("    /* \n <![CDATA[ \n*/\n function queueEvent() {\n  return false;\n}\n\n//\n  ]]>\n");
        responseWriter.endElement("script");
        responseWriter.flush();
        assertEquals(expected, stringWriter.toString());
    }
}
