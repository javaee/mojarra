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


import javax.faces.component.UIComponent;
import java.io.IOException;
import java.io.Writer;


/**
 * <p><strong>ResponseWriter</strong> is an abstract class describing an
 * adapter to an underlying output mechanism for character-based output.
 * In addition to the low-level <code>write()</code> methods inherited from
 * <code>java.io.Writer</code>, this class provides utility methods
 * that are useful in producing elements and attributes for markup languages
 * like HTML and XML.</p>
 */

public abstract class ResponseWriter extends Writer {


    /**
     * <p>Return the content type (such as "text/html") for this {@link
     * ResponseWriter}.  Note: this must not include the "charset="
     * suffix.</p>
     */
    public abstract String getContentType();


    /**
     * <p>Return the character encoding (such as "ISO-8859-1") for this
     * {@link ResponseWriter}.  Please see <a
     * href="http://www.iana.org/assignments/character-sets">the
     * IANA</a> for a list of character encodings.</p>
     */
    public abstract String getCharacterEncoding();


    /**
     * <p>Flush any ouput buffered by the output method to the
     * underlying Writer or OutputStream.  This method
     * will not flush the underlying Writer or OutputStream;  it
     * simply clears any values buffered by this {@link ResponseWriter}.</p>
     */
    public abstract void flush() throws IOException;


    /**
     * <p>Write whatever text should begin a response.</p>
     *
     * @throws IOException if an input/output error occurs
     */
    public abstract void startDocument() throws IOException;


    /**
     * <p>Write whatever text should end a response.  If there is an open
     * element that has been created by a call to <code>startElement()</code>,
     * that element will be closed first.</p>
     *
     * @throws IOException if an input/output error occurs
     */
    public abstract void endDocument() throws IOException;


    /**
     * <p>Write the start of an element, up to and including the
     * element name.  Once this method has been called, clients can
     * call the <code>writeAttribute()</code> or
     * <code>writeURIAttribute()</code> methods to add attributes and
     * corresponding values.  The starting element will be closed
     * (that is, the trailing '>' character added)
     * on any subsequent call to <code>startElement()</code>,
     * <code>writeComment()</code>,
     * <code>writeText()</code>, <code>endElement()</code>,
     * <code>endDocument()</code>, <code>close()</code>,
     * <code>flush()</code>, or <code>write()</code>.</p>
     *
     * @param name      Name of the element to be started
     * @param component The {@link UIComponent} (if any) to which
     *                  this element corresponds
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>name</code>
     *                              is <code>null</code>
     */
    public abstract void startElement(String name, UIComponent component)
            throws IOException;


    /**
     * <p>Write the end of an element, after closing any open element
     * created by a call to <code>startElement()</code>.  Elements must be
     * closed in the inverse order from which they were opened; it is an
     * error to do otherwise.</p>
     *
     * @param name Name of the element to be ended
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>name</code>
     *                              is <code>null</code>
     */
    public abstract void endElement(String name) throws IOException;


    /**
     * <p>Write an attribute name and corresponding value, after converting
     * that text to a String (if necessary), and after performing any escaping
     * appropriate for the markup language being rendered.
     * This method may only be called after a call to
     * <code>startElement()</code>, and before the opened element has been
     * closed.</p>
     *
     * @param name     Attribute name to be added
     * @param value    Attribute value to be added
     * @param property Name of the property or attribute (if any) of the
     *                 {@link UIComponent} associated with the containing element,
     *                 to which this generated attribute corresponds
     * @throws IllegalStateException if this method is called when there
     *                               is no currently open element
     * @throws IOException           if an input/output error occurs
     * @throws NullPointerException  if <code>name</code> is
     *                               <code>null</code>
     */
    public abstract void writeAttribute(String name, Object value,
                                        String property)
            throws IOException;


    /**
     * <p>Write a URI attribute name and corresponding value, after converting
     * that text to a String (if necessary), and after performing any encoding
     * appropriate to the markup language being rendered.
     * This method may only be called after a call to
     * <code>startElement()</code>, and before the opened element has been
     * closed.</p>
     *
     * @param name     Attribute name to be added
     * @param value    Attribute value to be added
     * @param property Name of the property or attribute (if any) of the
     *                 {@link UIComponent} associated with the containing element,
     *                 to which this generated attribute corresponds
     * @throws IllegalStateException if this method is called when there
     *                               is no currently open element
     * @throws IOException           if an input/output error occurs
     * @throws NullPointerException  if <code>name</code> is
     *                               <code>null</code>
     */
    public abstract void writeURIAttribute(String name, Object value,
                                           String property)
            throws IOException;

    /**
     * <p class="changed_added_2_0">Open an XML <code>CDATA</code>
     * block.  Note that XML does not allow nested <code>CDATA</code>
     * blocks, though this method does not enforce that constraint.  The
     * default implementation of this method takes no action when
     * invoked.</p>
     * @throws IOException if input/output error occures
     */
    public void startCDATA() throws IOException {

    }


    /**

     * <p class="changed_added_2_0">Close an XML <code>CDATA</code>
     * block.  The default implementation of this method takes no action
     * when invoked.</p>

     * @throws IOException if input/output error occures
     */
    public void endCDATA() throws IOException {
        throw new UnsupportedOperationException();
    }


    /**
     * <p>Write a comment containing the specified text, after converting
     * that text to a String (if necessary), and after performing any escaping
     * appropriate for the markup language being rendered.  If there is
     * an open element that has been created by a call to
     * <code>startElement()</code>, that element will be closed first.</p>
     *
     * @param comment Text content of the comment
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>comment</code>
     *                              is <code>null</code>
     */
    public abstract void writeComment(Object comment) throws IOException;


    /**
     * <p>Write an object, after converting it to a String (if necessary),
     * and after performing any escaping appropriate for the markup language
     * being rendered.  If there is an open element that has been created
     * by a call to <code>startElement()</code>, that element will be closed
     * first.</p>
     *
     * @param text     Text to be written
     * @param property Name of the property or attribute (if any) of the
     *                 {@link UIComponent} associated with the containing element,
     *                 to which this generated text corresponds
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>text</code>
     *                              is <code>null</code>
     */
    public abstract void writeText(Object text, String property)
            throws IOException;

    /**
     * <p>Write an object, after converting it to a String (if
     * necessary), and after performing any escaping appropriate for the
     * markup language being rendered.  This method is equivalent to
     * {@link #writeText(java.lang.Object,java.lang.String)} but adds a
     * <code>component</code> property to allow custom
     * <code>ResponseWriter</code> implementations to associate a
     * component with an arbitrary portion of text.</p>
     * <p/>
     * <p>The default implementation simply ignores the
     * <code>component</code> argument and calls through to {@link
     * #writeText(java.lang.Object,java.lang.String)}</p>
     *
     * @param text      Text to be written
     * @param component The {@link UIComponent} (if any) to which
     *                  this element corresponds
     * @param property  Name of the property or attribute (if any) of the
     *                  {@link UIComponent} associated with the containing element,
     *                  to which this generated text corresponds
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>text</code>
     *                              is <code>null</code>
     * @since 1.2
     */
    public void writeText(Object text, UIComponent component, String property)
            throws IOException {
        writeText(text, property);
    }


    /**
     * <p>Write text from a character array, after any performing any
     * escaping appropriate for the markup language being rendered.
     * If there is an open element that has been created by a call to
     * <code>startElement()</code>, that element will be closed first.</p>
     *
     * @param text Text to be written
     * @param off  Starting offset (zero-relative)
     * @param len  Number of characters to be written
     * @throws IndexOutOfBoundsException if the calculated starting or
     *                                   ending position is outside the bounds of the character array
     * @throws IOException               if an input/output error occurs
     * @throws NullPointerException      if <code>text</code>
     *                                   is <code>null</code>
     */
    public abstract void writeText(char text[], int off, int len)
            throws IOException;


    /**
     * <p>Create and return a new instance of this {@link ResponseWriter},
     * using the specified <code>Writer</code> as the output destination.</p>
     *
     * @param writer The <code>Writer</code> that is the output destination
     */
    public abstract ResponseWriter cloneWithWriter(Writer writer);


}
