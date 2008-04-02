/*
 * $Id: ResponseWriter.java,v 1.18 2006/03/24 19:05:33 edburns Exp $
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

package javax.faces.context;


import java.io.IOException;
import java.io.Writer;

import javax.faces.component.UIComponent;


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
     * @param name Name of the element to be started
     * @param component The {@link UIComponent} (if any) to which
     *  this element corresponds
     *
     * @throws IOException if an input/output error occurs
     * @throws NullPointerException if <code>name</code>
     *  is <code>null</code>
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
     *
     * @throws IOException if an input/output error occurs
     * @throws NullPointerException if <code>name</code>
     *  is <code>null</code>
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
     * @param name Attribute name to be added
     * @param value Attribute value to be added
     * @param property Name of the property or attribute (if any) of the
     *  {@link UIComponent} associated with the containing element,
     *  to which this generated attribute corresponds
     *
     * @throws IllegalStateException if this method is called when there
     *  is no currently open element
     * @throws IOException if an input/output error occurs
     * @throws NullPointerException if <code>name</code> is
     * <code>null</code>
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
     * @param name Attribute name to be added
     * @param value Attribute value to be added
     * @param property Name of the property or attribute (if any) of the
     *  {@link UIComponent} associated with the containing element,
     *  to which this generated attribute corresponds
     *
     * @throws IllegalStateException if this method is called when there
     *  is no currently open element
     * @throws IOException if an input/output error occurs
     * @throws NullPointerException if <code>name</code> is
     * <code>null</code>
     */
    public abstract void writeURIAttribute(String name, Object value, 
					   String property)
        throws IOException;


    /**
     * <p>Write a comment containing the specified text, after converting
     * that text to a String (if necessary), and after performing any escaping
     * appropriate for the markup language being rendered.  If there is
     * an open element that has been created by a call to
     * <code>startElement()</code>, that element will be closed first.</p>
     *
     * @param comment Text content of the comment
     *
     * @throws IOException if an input/output error occurs
     * @throws NullPointerException if <code>comment</code>
     *  is <code>null</code>
     */
    public abstract void writeComment(Object comment) throws IOException;


    /**
     * <p>Write an object, after converting it to a String (if necessary),
     * and after performing any escaping appropriate for the markup language
     * being rendered.  If there is an open element that has been created
     * by a call to <code>startElement()</code>, that element will be closed
     * first.</p>
     *
     * @param text Text to be written
     * @param property Name of the property or attribute (if any) of the
     *  {@link UIComponent} associated with the containing element,
     *  to which this generated text corresponds
     * 
     * @throws IOException if an input/output error occurs
     * @throws NullPointerException if <code>text</code>
     *  is <code>null</code>
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
     *
     * <p>The default implementation simply ignores the
     * <code>component</code> argument and calls through to {@link
     * #writeText(java.lang.Object,java.lang.String)}</p>
     *
     * @param text Text to be written
     * @param component The {@link UIComponent} (if any) to which
     *  this element corresponds
     * @param property Name of the property or attribute (if any) of the
     *  {@link UIComponent} associated with the containing element,
     *  to which this generated text corresponds
     * 
     * @throws IOException if an input/output error occurs
     * @throws NullPointerException if <code>text</code>
     *  is <code>null</code>
     *
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
     * @param off Starting offset (zero-relative)
     * @param len Number of characters to be written
     *
     * @throws IndexOutOfBoundsException if the calculated starting or
     *  ending position is outside the bounds of the character array
     * @throws IOException if an input/output error occurs
     * @throws NullPointerException if <code>text</code>
     *  is <code>null</code>
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
