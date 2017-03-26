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
package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.io.Writer;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

/**
 * A test ResponseWriter used for testing renderers.
 */
public class TestResponseWriter extends ResponseWriter {

    private Writer writer;

    /**
     * Constructor.
     *
     * @param writer the writer.
     */
    public TestResponseWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void flush() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void startDocument() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void endDocument() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Start the element.
     *
     * @param name the name.
     * @param component the component.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void startElement(String name, UIComponent component) throws IOException {
        writer.write("<" + name);
    }

    /**
     * End the element.
     *
     * @param name the name.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void endElement(String name) throws IOException {
        writer.write("</" + name + ">");
    }

    /**
     * Write an attribute.
     *
     * @param name the name.
     * @param value the value.
     * @param property the property.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void writeAttribute(String name, Object value, String property) throws IOException {
        writer.write(" " + name + "=\"" + value.toString() + "\"");
    }

    @Override
    public void writeURIAttribute(String name, Object value, String property) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeComment(Object comment) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeText(Object text, String property) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeText(char[] text, int off, int len) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResponseWriter cloneWithWriter(Writer writer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
