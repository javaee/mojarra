/*
 * $Id: MockJspWriter.java,v 1.5 2004/02/26 20:31:52 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.jsp.JspWriter;


// Mock Object for JspWriter
public class MockJspWriter extends JspWriter {


    // Public constructor for convenient setup
    public MockJspWriter(int bufferSize, boolean autoFlush) {
        super(bufferSize, autoFlush);
        writer = new StringWriter(bufferSize);
    }


    StringWriter writer;
    String lineSeparator = System.getProperty("line.separator");
    boolean closed = false;
    boolean flushed = false;


    // Mock Accessor for retrieving the contents that have been written
    public String getBuffer() {
        return (writer.getBuffer().toString());
    }


    // ------------------------------------------------------ JspWriter Methods


    public void clear() throws IOException {
        if (flushed) {
            throw new IOException("Already flushed");
        }
        writer = new StringWriter(getBufferSize());
    }


    public void clearBuffer() throws IOException {
        writer = new StringWriter(getBufferSize());
    }


    public void close() throws IOException {
        if (!closed) {
            flush();
            closed = true;
        }
    }


    public void flush() throws IOException {
        if (closed) {
            throw new IOException("Already closed");
        }
        flushed = true;
    }


    public int getRemaining() {
        return (getBufferSize() - writer.getBuffer().length());
    }


    public void newLine() throws IOException {
        write(lineSeparator);
    }


    public void print(boolean b) throws IOException {
        write(b ? "true" : "false");
    }


    public void print(char c) throws IOException {
        write(String.valueOf(c));
    }


    public void print(char c[]) throws IOException {
        write(c);
    }


    public void print(double d) throws IOException {
        write(String.valueOf(d));
    }


    public void print(float f) throws IOException {
        write(String.valueOf(f));
    }


    public void print(int i) throws IOException {
        write(String.valueOf(i));
    }


    public void print(long l) throws IOException {
        write(String.valueOf(l));
    }


    public void print(Object o) throws IOException {
        write(String.valueOf(o));
    }


    public void print(String s) throws IOException {
        if (s == null) {
            s = "null";
        }
        write(s);
    }


    public void println() throws IOException {
        newLine();
    }


    public void println(boolean b) throws IOException {
        print(b);
        newLine();
    }


    public void println(char c) throws IOException {
        print(c);
        newLine();
    }


    public void println(char c[]) throws IOException {
        print(c);
        newLine();
    }


    public void println(double d) throws IOException {
        print(d);
        newLine();
    }


    public void println(float f) throws IOException {
        print(f);
        newLine();
    }


    public void println(int i) throws IOException {
        print(i);
        newLine();
    }


    public void println(long l) throws IOException {
        print(l);
        newLine();
    }


    public void println(Object o) throws IOException {
        print(o);
        newLine();
    }


    public void println(String s) throws IOException {
        print(s);
        newLine();
    }


    // --------------------------------------------------------- Writer Methods


    public void write(char c[]) throws IOException {
        if (closed) {
            throw new IOException("Already closed");
        }
        writer.write(c);
    }


    public void write(char c[], int off, int len) throws IOException {
        if (closed) {
            throw new IOException("Already closed");
        }
        writer.write(c, off, len);
    }


    public void write(int c) throws IOException {
        if (closed) {
            throw new IOException("Already closed");
        }
        writer.write(c);
    }


    public void write(String s) throws IOException {
        if (closed) {
            throw new IOException("Already closed");
        }
        writer.write(s);
    }


    public void write(String s, int off, int len) throws IOException {
        if (closed) {
            throw new IOException("Already closed");
        }
        writer.write(s, off, len);
    }


}
