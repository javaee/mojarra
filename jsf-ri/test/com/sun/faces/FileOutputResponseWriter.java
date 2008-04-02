/*
 * $Id: FileOutputResponseWriter.java,v 1.12 2004/02/06 18:56:17 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
 * @version $Id: FileOutputResponseWriter.java,v 1.12 2004/02/06 18:56:17 rlubke Exp $
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


