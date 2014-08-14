/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.mock;

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

    @Override
    public void clearBuffer() throws IOException {
        writer = new StringWriter(getBufferSize());
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            flush();
            closed = true;
        }
    }

    @Override
    public void flush() throws IOException {
        if (closed) {
            throw new IOException("Already closed");
        }
        flushed = true;
    }

    @Override
    public int getRemaining() {
        return (getBufferSize() - writer.getBuffer().length());
    }

    @Override
    public void newLine() throws IOException {
        write(lineSeparator);
    }

    @Override
    public void print(boolean b) throws IOException {
        write(b ? "true" : "false");
    }

    @Override
    public void print(char c) throws IOException {
        write(String.valueOf(c));
    }

    @Override
    public void print(char c[]) throws IOException {
        write(c);
    }

    @Override
    public void print(double d) throws IOException {
        write(String.valueOf(d));
    }

    @Override
    public void print(float f) throws IOException {
        write(String.valueOf(f));
    }

    @Override
    public void print(int i) throws IOException {
        write(String.valueOf(i));
    }

    @Override
    public void print(long l) throws IOException {
        write(String.valueOf(l));
    }

    @Override
    public void print(Object o) throws IOException {
        write(String.valueOf(o));
    }

    @Override
    public void print(String s) throws IOException {
        if (s == null) {
            s = "null";
        }
        write(s);
    }

    @Override
    public void println() throws IOException {
        newLine();
    }

    @Override
    public void println(boolean b) throws IOException {
        print(b);
        newLine();
    }

    @Override
    public void println(char c) throws IOException {
        print(c);
        newLine();
    }

    @Override
    public void println(char c[]) throws IOException {
        print(c);
        newLine();
    }

    @Override
    public void println(double d) throws IOException {
        print(d);
        newLine();
    }

    @Override
    public void println(float f) throws IOException {
        print(f);
        newLine();
    }

    @Override
    public void println(int i) throws IOException {
        print(i);
        newLine();
    }

    @Override
    public void println(long l) throws IOException {
        print(l);
        newLine();
    }

    @Override
    public void println(Object o) throws IOException {
        print(o);
        newLine();
    }

    @Override
    public void println(String s) throws IOException {
        print(s);
        newLine();
    }

    // --------------------------------------------------------- Writer Methods
    @Override
    public void write(char c[]) throws IOException {
        if (closed) {
            throw new IOException("Already closed");
        }
        writer.write(c);
    }

    @Override
    public void write(char c[], int off, int len) throws IOException {
        if (closed) {
            throw new IOException("Already closed");
        }
        writer.write(c, off, len);
    }

    @Override
    public void write(int c) throws IOException {
        if (closed) {
            throw new IOException("Already closed");
        }
        writer.write(c);
    }

    @Override
    public void write(String s) throws IOException {
        if (closed) {
            throw new IOException("Already closed");
        }
        writer.write(s);
    }

    @Override
    public void write(String s, int off, int len) throws IOException {
        if (closed) {
            throw new IOException("Already closed");
        }
        writer.write(s, off, len);
    }
}
