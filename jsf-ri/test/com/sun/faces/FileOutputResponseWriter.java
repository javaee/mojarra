/*
 * $Id: FileOutputResponseWriter.java,v 1.4 2003/05/15 21:33:10 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FileOutputResponseWriter.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.faces.context.ResponseWriter;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

/**
 *
 *  The sole purpose of <B>FileOutputResponseWriter</B> is to wrap an
 *  be a ResponseWriter object that writes its 
 *  output to a file.  <P>
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FileOutputResponseWriter.java,v 1.4 2003/05/15 21:33:10 jvisvanathan Exp $
 * 
 *
 */

public class FileOutputResponseWriter extends ResponseWriter
{
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
public static final String FACES_RESPONSE_ROOT = "./build/test/servers/tomcat/webapps/test/";
public static final String  RESPONSE_WRITER_FILENAME = FACES_RESPONSE_ROOT + 
    "ResponseWriter.txt";

// Attribute Instance Variables


// Relationship Instance Variables

//
// Constructors and Initializers    
//

public FileOutputResponseWriter()
{
    super();
    try {
        File file = new File ( RESPONSE_WRITER_FILENAME );
        FileOutputStream fs = new FileOutputStream(file);
        out = new PrintWriter(fs);
    } catch ( Exception e ) {
        System.out.println(e.getMessage());
    }
}

//
// Class methods
//

//
// Methods from Writer
//

public void write(int c) throws IOException
{
    out.write(c);
}

public void write(char[] cbuf) throws IOException
{
    out.write(cbuf);
}

public void write(char[] cbuf, int off, int len) throws IOException
{
    out.write(cbuf, off, len);
}

public void write(String str) throws IOException
{
    out.write(str);
}

public void write(String str, int off, int len) throws IOException
{
    out.write(str, off, len);
}

public void flush() throws IOException
{
    out.flush();
}

public void close() throws IOException
{
    out.close();
}

public void writeText(char[] text,int off, int len) {
}

public void writeText(char[] text ) {
}

public void writeText(char text ) {
}

public void writeText(Object text ) {
}

public void writeComment(Object text ) {
}

public void writeAttribute(String name, Object value) throws IOException {
}    

public void writeURIAttribute(String name, Object value) throws IOException {
}    

public void startElement(String name) throws IOException {
}

public void endElement(String name) throws IOException {
}    

public void startDocument() throws IOException {
    throw new UnsupportedOperationException();
}

public void endDocument() throws IOException {
    throw new UnsupportedOperationException();
}




} // end of class FileOutputResponseWriter


