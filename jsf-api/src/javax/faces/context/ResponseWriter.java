/*
 * $Id: ResponseWriter.java,v 1.7 2003/07/31 12:22:22 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


import java.io.IOException;
import java.io.Writer;


/**
 * <p><strong>ResponseWriter</strong> is an abstract class describing an
 * adapter to an underlying output mechanism for character-based output.
 * In addition to the low-level <code>write()</code> methods inherited from
 * <code>java.io.Writer</code>, this class provides utility methods
 * that are useful in producing elements and attributes for markup languages
 * like HTML and XML.</p>
 *
 *
 */

public abstract class ResponseWriter extends Writer {

    /**
     * @return the character encoding, such as "ISO-8859-1" for this
     * ResponseWriter.  Please see <a
     * href="http://www.iana.org/assignments/character-sets">the
     * IANA</a> for a list of character encodings.
     *
     */

    public abstract String getCharacterEncoding();

    /**
     * <p>Write whatever text should begin a response.</p>
     *
     * @exception IOException if an input/output error occurs
     */
    public abstract void startDocument() throws IOException;


    /**
     * <p>Write whatever text should end a response.  If there is an open
     * element that has been created by a call to <code>startElement()</code>,
     * that element will be closed first.</p>
     *
     * @exception IOException if an input/output error occurs
     */
    public abstract void endDocument() throws IOException;

    /**
     * Flushes any ouput buffered by the output method to the
     * underlying Writer or OutputStream.  This method
     * will not flush the underlying writer or output stream;  it
     * simply clears any values buffered by the OutputMethod.
     */
    public abstract void flush() throws IOException;

    /**
     * <p>Write the start of an element, up to and including the
     * element name.  Once this method has been called, clients can
     * call <code>writeAttribute()</code> or <code>writeURIAttribute()</code>
     * method to add attributes and corresponding values.  The starting
     * element will be closed (that is, the trailing '>' character added)
     * on any subsequent call to <code>startElement()</code>,
     * <code>writeComment()</code>,
     * <code>writeText()</code>, <code>endElement()</code>, or
     * <code>endDocument()</code>.</p>
     *
     * @param name Name of the element to be started
     *
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public abstract void startElement(String name) throws IOException;


    /**
     * <p>Write the end of an element, after closing any open element
     * created by a call to <code>startElement()</code>.
     *
     * @param name Name of the element to be ended
     *
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public abstract void endElement(String name) throws IOException;


    /**
     * <p>Write an attribute name and corresponding value (after converting
     * that text to a String if necessary), after escaping it properly.
     * This method may only be called after a call to
     * <code>startElement()</code>, and before the opened element has been
     * closed.</p>
     *
     * @param name Attribute name to be added
     * @param value Attribute value to be added
     *
     * @exception IllegalStateException if this method is called when there
     *  is no currently open element
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>name</code> or
     *  <code>value</code> is <code>null</code>
     */
    public abstract void writeAttribute(String name, Object value)
        throws IOException;


    /**
     * <p>Write a URI attribute name and corresponding value (after converting
     * that text to a String if necessary), after encoding it properly
     * (for example, '%' encoded for HTML).
     * This method may only be called after a call to
     * <code>startElement()</code>, and before the opened element has been
     * closed.</p>
     *
     * @param name Attribute name to be added
     * @param value Attribute value to be added
     *
     * @exception IllegalStateException if this method is called when there
     *  is no currently open element
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>name</code> or
     *  <code>value</code> is <code>null</code>
     */
    public abstract void writeURIAttribute(String name, Object value)
        throws IOException;


    /**
     * <p>Write a comment containing the specified text, after converting
     * that text to a String if necessary.  If there is an open element
     * that has been created by a call to <code>startElement()</code>,
     * that element will be closed first.</p>
     *
     * <p>All angle bracket occurrences in the argument must be escaped
     * using the &amp;gt; &amp;lt; syntax.</p>
     *
     * @param comment Text content of the comment
     *
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>comment</code>
     *  is <code>null</code>
     */
    public abstract void writeComment(Object comment) throws IOException;


    /**
     * <p>Write an object (after converting it to a String, if necessary),
     * after escaping it properly.  If there is an open element
     * that has been created by a call to <code>startElement()</code>,
     * that element will be closed first.</p>
     *
     * <p>All angle bracket occurrences in the argument must be escaped
     * using the &amp;gt; &amp;lt; syntax.</p>
     *
     * @param text Text to be written
     *
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>text</code>
     *  is <code>null</code>
     */
    public abstract void writeText(Object text) throws IOException;


    /**
     * <p>Write a single character, after escaping it properly.  If there
     * is an open element that has been created by a call to
     * <code>startElement()</code>, that element will be closed first.</p>
     *
     * <p>All angle bracket occurrences in the argument must be escaped
     * using the &amp;gt; &amp;lt; syntax.</p>
     *
     * @param text Text to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public abstract void writeText(char text) throws IOException;


    /**
     * <p>Write text from a character array, after escaping it properly.
     * This is equivalent to calling <code>writeText(c, 0, c.length)</code>.
     * If there is an open element that has been created by a call to
     * <code>startElement()</code>, that element will be closed first.</p>
     * </p>
     *
     * <p>All angle bracket occurrences in the argument must be escaped
     * using the &amp;gt; &amp;lt; syntax.</p>
     *
     * @param text Text to be written
     *
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>text</code>
     *  is <code>null</code>
     */
    public abstract void writeText(char text[]) throws IOException;


    /**
     * <p>Write text from a character array, after escaping it properly
     * for this method.  If there is an open element that has been
     * created by a call to <code>startElement()</code>, that element
     * will be closed first.</p>
     *
     * <p>All angle bracket occurrences in the argument must be escaped
     * using the &amp;gt; &amp;lt; syntax.</p>
     *
     * @param text Text to be written
     * @param off Starting offset (zero-relative)
     * @param len Number of characters to be written
     *
     * @exception IndexOutOfBoundsException if the calculated starting or
     *  ending position is outside the bounds of the character array
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>text</code>
     *  is <code>null</code>
     */
    public abstract void writeText(char text[], int off, int len)
        throws IOException;

    /**
     * Creates a new instance of this ResponseWriter, using a different
     * Writer.
     */
    public abstract ResponseWriter cloneWithWriter(Writer writer);

}
