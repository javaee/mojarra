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

package com.sun.faces.io;

import com.sun.faces.application.ViewHandlerResponseWrapper;
import com.sun.faces.cactus.ServletFacesTestCase;

import javax.servlet.ServletOutputStream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class TestIO extends ServletFacesTestCase {
    
    public TestIO() {
        super("TestIO");
    }


    public TestIO(String name) {
        super(name);
    }
    
    
    // ------------------------------------------------------------ Test Methods
    
    
    public void testBase64Streams() throws Exception {
        // create a string over 2048 bytes in length
        String testString = "This is a test String";
        for (int i = testString.length(); i < 6000; i++) {
            testString += 'a';
        }
        
        StringWriter writer = new StringWriter();
        Base64OutputStreamWriter sw = new Base64OutputStreamWriter(2048, writer);
        ObjectOutputStream os = new ObjectOutputStream(sw);
        os.writeObject(testString);
        os.flush();
        os.close();
        sw.finish();
        
        String encodedString = writer.toString();
        // no take the encodedString and reverse the operation
        Base64InputStream bin = new Base64InputStream(encodedString);
        ObjectInputStream input = new ObjectInputStream(bin);
        
        String result = (String) input.readObject();
        input.close();
        
        assertTrue(result != null);
        assertTrue(result.length() == testString.length());
        assertTrue(testString.equals(result));
                               
    }


    public void testViewHandlerResponseWrapperStreamIO() throws Exception {

        ViewHandlerResponseWrapper w1 =
              new ViewHandlerResponseWrapper(getResponse());
        ServletOutputStream os = w1.getOutputStream();
        os.write('1');
        try {
            w1.getWriter();
            assertTrue(false);
        } catch (IllegalStateException ise) {
            // expected
        }
        os.flush();
        os.close();
        PrintWriter writer = null;
        try {
            writer = w1.getWriter();
        } catch (IllegalStateException ise) {
            assertTrue(false);
        }

        // we've closed the stream - and should have a fake
        // writer.  Ensure writing to it doesn't impact the result;
        writer.print("Some junk");
        StringWriter buf = new StringWriter();
        w1.flushToWriter(buf, "ISO-8859-1");
        assertTrue(!buf.toString().contains("Some junk"));

        // ensure that the content that was written to the stream is
        // present
        assertTrue("1".equals(buf.toString()));

        w1 = new ViewHandlerResponseWrapper(getResponse());
        os = w1.getOutputStream();
        // flushBuffer should commit the response so getWriter()
        // should throw no Exception
        w1.flushBuffer();
        try {
            w1.getWriter();
        } catch (IllegalStateException ise) {
            assertTrue(false);
        }

    }

    public void testViewHandlerResponseWrapperCharIO() throws Exception {

        ViewHandlerResponseWrapper w1 =
              new ViewHandlerResponseWrapper(getResponse());
        PrintWriter pw = w1.getWriter();
        pw.write("some stuff");
        try {
            w1.getOutputStream();
            assertTrue(false);
        } catch (IllegalStateException ise) {
            // expected
        }
        pw.flush();
        pw.close();
        ServletOutputStream os = null;
        try {
            os = w1.getOutputStream();
        } catch (IllegalStateException ise) {
            assertTrue(false);
        }

        // we've closed the stream - and should have a fake
        // writer.  Ensure writing to it doesn't impact the result;
        os.write('1');
        StringWriter buf = new StringWriter();
        w1.flushToWriter(buf, "ISO-8859-1");
        assertTrue(!buf.toString().contains("1"));

        // ensure that the content that was written to the stream is
        // present
        assertTrue("some stuff".equals(buf.toString()));

        w1 = new ViewHandlerResponseWrapper(getResponse());
        pw = w1.getWriter();
        // flushBuffer should commit the response so getWriter()
        // should throw no Exception
        w1.flushBuffer();
        try {
            w1.getOutputStream();
        } catch (IllegalStateException ise) {
            assertTrue(false);
        }

    }

} // END TestIO