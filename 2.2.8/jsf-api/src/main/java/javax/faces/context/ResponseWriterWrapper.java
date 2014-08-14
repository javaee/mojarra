/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package javax.faces.context;

import javax.faces.FacesWrapper;
import javax.faces.component.UIComponent;
import java.io.IOException;
import java.io.Writer;

/**
 * <p><span class="changed_modified_2_0">Provides</span> a simple implementation
 * of {@link ResponseWriter} that
 * can be subclassed by developers wishing to provide specialized
 * behavior to an existing {@link ResponseWriter} instance.  The default
 * implementation of all methods is to call through to the wrapped
 * {@link ResponseWriter}.</p>
 * <p/>
 * <p>Usage: extend this class and override {@link #getWrapped} to
 * return the instance we are wrapping.</p>
 *
 * @since 1.2
 */
public abstract class ResponseWriterWrapper extends ResponseWriter implements FacesWrapper<ResponseWriter> {


    /**
     * <p class="changed_modified_2_0">Return the instance that we are wrapping.
     * As of version 2, this method is public.</p>
     */
    @Override
    public abstract ResponseWriter getWrapped();

    // -------------------------- Methods from javax.faces.contxt.ResponseWriter


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#getContentType()}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#getContentType()
     * @since 1.2
     */
    @Override
    public String getContentType() {

        return getWrapped().getContentType();

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#getCharacterEncoding()}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#getCharacterEncoding()
     * @since 1.2
     */
    @Override
    public String getCharacterEncoding() {

        return getWrapped().getCharacterEncoding();

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#flush()}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#flush()
     * @since 1.2
     */
    @Override
    public void flush() throws IOException {

        getWrapped().flush();

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#startDocument()}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#startDocument()
     * @since 1.2
     */
    @Override
    public void startDocument() throws IOException {

        getWrapped().startDocument();

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#endDocument()}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#endDocument()
     * @since 1.2
     */
    @Override
    public void endDocument() throws IOException {

        getWrapped().endDocument();

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#startElement(String, javax.faces.component.UIComponent)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#startElement(String, javax.faces.component.UIComponent)
     * @since 1.2
     */
    @Override
    public void startElement(String name, UIComponent component)
            throws IOException {

        getWrapped().startElement(name, component);

    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link ResponseWriter#startCDATA} on the wrapped
     * {@link ResponseWriter} object.</p>
     * @since 2.0
     * @throws IOException on any read/write error
     */
    @Override
    public void startCDATA() throws IOException {
        getWrapped().startCDATA();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link ResponseWriter#endCDATA} on the wrapped
     * {@link ResponseWriter} object.</p>
     * @since 2.0
     * @throws IOException on any read/write error
     */
    @Override
    public void endCDATA() throws IOException {
        getWrapped().endCDATA();
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#endElement(String)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#endElement(String)
     * @since 1.2
     * @throws IOException on any read/write error
     */
    @Override
    public void endElement(String name) throws IOException {

        getWrapped().endElement(name);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#writeAttribute(String, Object, String)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#writeAttribute(String, Object, String)
     * @since 1.2
     */
    @Override
    public void writeAttribute(String name, Object value, String property)
            throws IOException {

        getWrapped().writeAttribute(name, value, property);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#writeURIAttribute(String, Object, String)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#writeURIAttribute(String, Object, String)
     * @since 1.2
     */
    @Override
    public void writeURIAttribute(String name, Object value, String property)
            throws IOException {

        getWrapped().writeURIAttribute(name, value, property);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#writeComment(Object)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#writeComment(Object)
     * @since 1.2
     */
    @Override
    public void writeComment(Object comment) throws IOException {

        getWrapped().writeComment(comment);

    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#writeDoctype}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#writeDoctype
     * @since 2.2
     */
    @Override
    public void writeDoctype(String doctype) throws IOException {
        getWrapped().writeDoctype(doctype);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#writePreamble}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#writePreamble
     * @since 2.2
     */
    @Override
    public void writePreamble(String preamble) throws IOException {
        getWrapped().writePreamble(preamble);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#writeText(Object, String)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#writeText(Object, String)
     * @since 1.2
     */
    @Override
    public void writeText(Object text, String property) throws IOException {

        getWrapped().writeText(text, property);

    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#writeText(Object, UIComponent, String)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#writeText(Object, String)
     * @since 1.2
     */

    @Override
    public void writeText(Object text, UIComponent component, String property)
            throws IOException {
        getWrapped().writeText(text, component, property);
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#writeText(char[], int, int)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#writeText(char[], int, int)
     * @since 1.2
     */
    @Override
    public void writeText(char[] text, int off, int len) throws IOException {

        getWrapped().writeText(text, off, len);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#cloneWithWriter(java.io.Writer)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#cloneWithWriter(java.io.Writer)
     * @since 1.2
     */
    @Override
    public ResponseWriter cloneWithWriter(Writer writer) {

        return getWrapped().cloneWithWriter(writer);

    }


    // --------------------------------------------- Methods from java.io.Writer


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#close()}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#close()
     * @since 1.2
     */
    @Override
    public void close() throws IOException {

        getWrapped().close();

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#write(char[], int, int)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#write(char[], int, int)
     * @since 1.2
     */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {

        getWrapped().write(cbuf, off, len);

    }

}
