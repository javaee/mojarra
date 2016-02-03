/*
 * $Id: FileOutputResponseWriter.java,v 1.13.30.2 2007/04/27 21:28:19 ofung Exp $
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

// FileOutputResponseWriter.java

package com.sun.faces;

import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;
import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * The sole purpose of <B>FileOutputResponseWriter</B> is to wrap an
 * be a ResponseWriter object that writes its
 * output to a file.  <P>
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FileOutputResponseWriter.java,v 1.13.30.2 2007/04/27 21:28:19 ofung Exp $
 */

public class FileOutputResponseWriter extends ResponseWriter {

//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    protected PrintWriter out = null;
    public static String FACES_RESPONSE_ROOT = null;
    public static String RESPONSE_WRITER_FILENAME = "ResponseWriter.txt";
    protected HtmlResponseWriter writer = null;

// Attribute Instance Variables


// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public FileOutputResponseWriter() {
        try {
            initializeFacesResponseRoot();
            File file = new File(RESPONSE_WRITER_FILENAME);
            FileOutputStream fs = new FileOutputStream(file);
            out = new PrintWriter(fs);
            writer = new HtmlResponseWriter(out, "text/html", "ISO-8859-1");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Util.doAssert(false);
        }
    }

//
// Class methods
//

    public static void initializeFacesResponseRoot() {
        if (null == FACES_RESPONSE_ROOT) {
            String testRootDir;
            // prepend the testRootDir to the RESPONSE_WRITER_FILENAME
            testRootDir = System.getProperty("testRootDir");
            Util.doAssert(null != testRootDir);
            FACES_RESPONSE_ROOT = testRootDir + "/";
            RESPONSE_WRITER_FILENAME =
                FACES_RESPONSE_ROOT + RESPONSE_WRITER_FILENAME;
            FileOutputResponseWrapper.FACES_RESPONSE_FILENAME =
                FACES_RESPONSE_ROOT +
                FileOutputResponseWrapper.FACES_RESPONSE_FILENAME;
        }
    }

//
// Methods from Writer
//

    public void write(int c) throws IOException {
        out.write(c);
    }


    public void write(char[] cbuf) throws IOException {
        out.write(cbuf);
    }


    public void write(char[] cbuf, int off, int len) throws IOException {
        out.write(cbuf, off, len);
    }


    public void write(String str) throws IOException {
        out.write(str);
    }


    public void write(String str, int off, int len) throws IOException {
        out.write(str, off, len);
    }


    public void flush() throws IOException {
        out.flush();
    }


    public void close() throws IOException {
        out.close();
    }


    public void writeText(char[] text, int off, int len) throws IOException {
        writer.writeText(text, off, len);
    }


    public void writeText(Object text, String componentPropertyName)
        throws IOException {
        writer.writeText(text, componentPropertyName);
    }


    public void writeComment(Object text) throws IOException {
        writer.writeComment(text);
    }


    public void writeAttribute(String name, Object value, String componentPropertyName)
        throws IOException {
        writer.writeAttribute(name, value, componentPropertyName);
    }


    public void writeURIAttribute(String name, Object value, String componentPropertyName)
        throws IOException {
        writer.writeURIAttribute(name, value, componentPropertyName);
    }


    public void startElement(String name, UIComponent componentForElement)
        throws IOException {
        writer.startElement(name, componentForElement);
    }


    public void endElement(String name) throws IOException {
        writer.endElement(name);
    }


    public void startDocument() throws IOException {
        writer.startDocument();
    }


    public void endDocument() throws IOException {
        writer.endDocument();
    }


    public ResponseWriter cloneWithWriter(Writer writer) {
        return this.writer.cloneWithWriter(writer);
    }


    public String getCharacterEncoding() {
        return writer.getCharacterEncoding();
    }


    public String getContentType() {
        return writer.getContentType();
    }

} // end of class FileOutputResponseWriter


