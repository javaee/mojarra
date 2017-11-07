/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id: FileOutputResponseWriter.java,v 1.1 2005/10/18 16:41:33 edburns Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

// FileOutputResponseWriter.java

package com.sun.faces.cactus;

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
 * @version $Id: FileOutputResponseWriter.java,v 1.1 2005/10/18 16:41:33 edburns Exp $
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
  
    public FileOutputResponseWriter(String rootDir) {
        try {
            initializeFacesResponseRoot(rootDir);
            File file = new File(RESPONSE_WRITER_FILENAME);
            FileOutputStream fs = new FileOutputStream(file);
            out = new PrintWriter(fs);
            writer = new HtmlResponseWriter(out, "text/html", "ISO-8859-1");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert (false);
        }
    }

//
// Class methods
//

    public static void initializeFacesResponseRoot(String testRootDir) {
        if (null == FACES_RESPONSE_ROOT) {
            assert (null != testRootDir);
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
        writer.write(c);
    }


    public void write(char[] cbuf) throws IOException {
        writer.write(cbuf);
    }


    public void write(char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }


    public void write(String str) throws IOException {
        writer.write(str);
    }


    public void write(String str, int off, int len) throws IOException {
        writer.write(str, off, len);
    }


    public void flush() throws IOException {
        writer.flush();
        out.flush();
    }


    public void close() throws IOException {
        writer.close();
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


