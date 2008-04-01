/*
 * $Id: OutputMethod.java,v 1.2 2001/12/20 22:25:45 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * Interface to encapsulate functionality for outputting rendering
 * code for user-interface components.
 *
 * Note: need to get more details from Oracle to do javadoc
 */
public interface OutputMethod {
     public String getContentType();
     public void startDocument() throws java.io.IOException;
     public void endDocument() throws java.io.IOException;
     public void flush() throws java.io.IOException;
     public void writeComment(String text) throws java.io.IOException;
     public void writeText(String text) throws java.io.IOException;
     public void writeText(char[] c, int start, int len) throws java.io.IOException;
     public void writeText(char c) throws java.io.IOException;
     public void writeRawText(String text) throws java.io.IOException;
     public void writeRawText(char[] c, int start, int len) throws java.io.IOException;
     public void writeRawText(char c) throws java.io.IOException;
     public void startElement(String name) throws java.io.IOException;
     public void endElement(String name) throws java.io.IOException;
     public void writeAttribute(String name, Object value)
       throws java.io.IOException;
     public void writeURIAttribute(String name, Object value)
        throws java.io.IOException;
}
