/*
 * $Id: MockResponseWriter.java,v 1.9 2005/08/22 22:08:25 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.mock;


import java.io.IOException;
import java.io.Writer;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;


public class MockResponseWriter extends ResponseWriter {


    public MockResponseWriter(Writer writer, String encoding) {
        this.writer = writer;
        this.encoding = encoding;
    }


    private Writer writer;
    private String encoding;


    // ---------------------------------------------------------- Writer Methods


    public void close() throws IOException {
        writer.close();
    }


    public void flush() throws IOException {
        writer.flush();
    }


    public void write(char c) throws IOException {
        writer.write(c);
    }


    public void write(char cbuf[], int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }


    public void write(int c) throws IOException {
        writer.write(c);
    }


    public void write(String s) throws IOException {
        writer.write(s);
    }


    public void write(String s, int off, int len) throws IOException {
        writer.write(s, off, len);
    }


    // --------------------------------------------------- ResponseWrter Methods

    public String getContentType() {
        return "text/html";
    }

    public String getCharacterEncoding() {
        return (this.encoding);
    }


    public void startDocument() throws IOException {
        throw new UnsupportedOperationException();
    }


    public void endDocument() throws IOException {
        throw new UnsupportedOperationException();
    }


    public void startElement(String name, 
			     UIComponent component) throws IOException {
        throw new UnsupportedOperationException();
    }


    public void endElement(String name) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void writeAttribute(String name, Object value, 
			       String componentPropertyName) throws IOException {
        throw new UnsupportedOperationException();
    }


    public void writeURIAttribute(String name, Object value, String componentPropertyName) throws IOException {
        throw new UnsupportedOperationException();
    }


    public void writeComment(Object comment) throws IOException {
        throw new UnsupportedOperationException();
    }


    public void writeText(Object text, String componentProperty) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void writeText(char text[], int off, int len) throws IOException {
        throw new UnsupportedOperationException();
    }


    public ResponseWriter cloneWithWriter(Writer writer) {
        throw new UnsupportedOperationException();
    }


}
