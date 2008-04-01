/*
 * $Id: FileOutputResponseWriter.java,v 1.1 2002/06/06 00:15:02 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
 * @version $Id: FileOutputResponseWriter.java,v 1.1 2002/06/06 00:15:02 eburns Exp $
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
public static final String FACES_RESPONSE_ROOT = "./build/test/servers/tomcat40/webapps/test/";
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



} // end of class FileOutputResponseWriter


