/*
 * $Id: CustomResponseWriter.java,v 1.3 2006/01/11 15:28:15 rlubke Exp $
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

package com.sun.faces.systest.render;

import com.sun.faces.util.HtmlUtils;
import com.sun.faces.util.MessageUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


/**
 * <p><strong>CustomResponseWriter</strong> is an Html specific implementation
 * of the <code>ResponseWriter</code> abstract class.
 * Kudos to Adam Winer (Oracle) for much of this code.
 */
public class CustomResponseWriter extends ResponseWriter {

    // Content Type for this Writer.
    //
    private String contentType = "text/html";

    // Character encoding of that Writer - this may be null
    // if the encoding isn't known.
    //
    private String encoding = null;

    // Writer to use for output;
    //
    private Writer writer = null;

    // True when we need to close a start tag
    //
    private boolean closeStart;

    // True when we shouldn't be escaping output (basically,
    // inside of <script> and <style> elements).
    //
    private boolean dontEscape;

    // Internal buffer used when outputting properly escaped information
    // using HtmlUtils class.
    //
    private char[] buffer = new char[1028];
    private char[] charHolder = new char[1];


    /**
     * Constructor sets the <code>ResponseWriter</code> and
     * encoding.
     *
     * @param writer      the <code>ResponseWriter</code>
     * @param contentType the content type.
     * @param encoding    the character encoding.
     * @throws if the encoding is not recognized.
     */
    public CustomResponseWriter(Writer writer, String contentType, String encoding)
        throws FacesException {
        this.writer = writer;
        if (null != contentType) {
            this.contentType = contentType;
        }
        this.encoding = encoding;

        // Check the character encoding
        try {
            HtmlUtils.validateEncoding(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(MessageUtils.getExceptionMessageString(
                MessageUtils.ENCODING_ERROR_MESSAGE_ID));
        }
    }


    /**
     * @return the content type such as "text/html" for this ResponseWriter.
     */
    public String getContentType() {
        return contentType;
    }


    /**
     * @return the character encoding, such as "ISO-8859-1" for this
     *         ResponseWriter.  Refer to:
     *         <a href="http://www.iana.org/assignments/character-sets">theIANA</a>
     *         for a list of character encodings.
     */
    public String getCharacterEncoding() {
        return encoding;
    }


    /**
     * <p>Write the text that should begin a response.</p>
     *
     * @throws IOException if an input/output error occurs
     */
    public void startDocument() throws IOException {
        // do nothing;
    }


    /**
     * Output the text for the end of a document.
     */
    public void endDocument() throws IOException {
        writer.flush();
    }


    /**
     * Flush any buffered output to the contained writer.
     *
     * @throws IOException if an input/output error occurs.
     */
    public void flush() throws IOException {
        // NOTE: Internal buffer's contents (the ivar "buffer") is
        // written to the contained writer in the HtmlUtils class - see
        // HtmlUtils.flushBuffer method; Buffering is done during
        // writeAttribute/writeText - otherwise, output is written
        // directly to the writer (ex: writer.write(....)..
        //
        // close any previously started element, if necessary
        closeStartIfNecessary();
    }


    /**
     * <p>Write the start of an element, up to and including the
     * element name.  Clients call <code>writeAttribute()</code> or
     * <code>writeURIAttribute()</code> methods to add attributes after
     * calling this method.
     *
     * @param name                Name of the starting element
     * @param componentForElement The UIComponent instance that applies to this
     *                            element.  This argument may be <code>null</code>.
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>name</code>
     *                              is <code>null</code>
     */
    public void startElement(String name, UIComponent componentForElement)
        throws IOException {
        if (name == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        closeStartIfNecessary();
        char firstChar = name.charAt(0);
        if ((firstChar == 's') ||
            (firstChar == 'S')) {
            if ("script".equalsIgnoreCase(name) ||
                "style".equalsIgnoreCase(name)) {
                dontEscape = true;
            }
        }
        
        
        //PENDING (horwat) using String as a result of Tomcat char writer
        //         ArrayIndexOutOfBoundsException (3584)
        writer.write("<");
        writer.write(name);
        closeStart = true;
    }


    /**
     * <p>Write the end of an element. This method will first
     * close any open element created by a call to
     * <code>startElement()</code>.
     *
     * @param name Name of the element to be ended
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>name</code>
     *                              is <code>null</code>
     */
    public void endElement(String name) throws IOException {
        if (name == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // always turn escaping back on once an element ends
        dontEscape = false;
        // See if we need to close the start of the last element
        if (closeStart) {
            boolean isEmptyElement = HtmlUtils.isEmptyElement(name);

            if (isEmptyElement) {
                writer.write(" />");
                closeStart = false;
                return;
            }

            writer.write(">");
            closeStart = false;
        }

        writer.write("</");
        writer.write(name);
        //PENDING (horwat) using String as a result of Tomcat char writer
        //         ArrayIndexOutOfBoundsException (3584)
        writer.write(">");
    }


    /**
     * <p>Write a properly escaped attribute name and the corresponding
     * value.  The value text will be converted to a String if
     * necessary.  This method may only be called after a call to
     * <code>startElement()</code>, and before the opened element has been
     * closed.</p>
     *
     * @param name                  Attribute name to be added
     * @param value                 Attribute value to be added
     * @param componentPropertyName The name of the component property to
     *                              which this attribute argument applies.  This argument may be
     *                              <code>null</code>.
     * @throws IllegalStateException if this method is called when there
     *                               is no currently open element
     * @throws IOException           if an input/output error occurs
     * @throws NullPointerException  if <code>name</code> or
     *                               <code>value</code> is <code>null</code>
     */
    public void writeAttribute(String name, Object value, String componentPropertyName)
        throws IOException {
        if (name == null || value == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        Class valueClass = value.getClass();

        // Output Boolean values specially
        if (valueClass == Boolean.class) {
            if (Boolean.TRUE.equals(value)) {
                //PENDING (horwat) using String as a result of
                //Tomcat char writer ArrayIndexOutOfBoundsException (3584)
                writer.write(" ");
                writer.write(name);
            } else {
                // Don't write anything for "false" booleans
            }
        } else {
            writer.write(" ");
            writer.write(name);
            writer.write("=\"");
            
            // write the attribute value
            HtmlUtils.writeAttribute(writer, buffer, value.toString());
            //PENDING (horwat) using String as a result of Tomcat char 
            //        writer ArrayIndexOutOfBoundsException (3584)
            writer.write("\"");
        }
    }


    /**
     * <p>Write a properly encoded URI attribute name and the corresponding
     * value. The value text will be converted to a String if necessary).
     * This method may only be called after a call to
     * <code>startElement()</code>, and before the opened element has been
     * closed.</p>
     *
     * @param name                  Attribute name to be added
     * @param value                 Attribute value to be added
     * @param componentPropertyName The name of the component property to
     *                              which this attribute argument applies.  This argument may be
     *                              <code>null</code>.
     * @throws IllegalStateException if this method is called when there
     *                               is no currently open element
     * @throws IOException           if an input/output error occurs
     * @throws NullPointerException  if <code>name</code> or
     *                               <code>value</code> is <code>null</code>
     */
    public void writeURIAttribute(String name, Object value,
                                  String componentPropertyName)
        throws IOException {
        if (name == null || value == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        //PENDING (horwat) using String as a result of Tomcat char writer
        //         ArrayIndexOutOfBoundsException (3584)
        writer.write(" ");
        writer.write(name);
        writer.write("=\"");

        String stringValue = value.toString();
        
        // Javascript URLs should not be URL-encoded
        if (stringValue.startsWith("javascript:")) {
            HtmlUtils.writeAttribute(writer, buffer, stringValue);
        } else {
            HtmlUtils.writeURL(writer, stringValue, encoding);
        }
        
        //PENDING (horwat) using String as a result of Tomcat char writer
        //         ArrayIndexOutOfBoundsException (3584)
        writer.write("\"");
    }


    /**
     * <p>Write a comment string containing the specified text.
     * The text will be converted to a String if necessary.
     * If there is an open element that has been created by a call
     * to <code>startElement()</code>, that element will be closed
     * first.</p>
     *
     * @param comment Text content of the comment
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>comment</code>
     *                              is <code>null</code>
     */
    public void writeComment(Object comment) throws IOException {
        if (comment == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        closeStartIfNecessary();
        writer.write("<!-- ");
        writer.write(comment.toString());
        writer.write(" -->");
    }


    /**
     * <p>Write a properly escaped object. The object will be converted
     * to a String if necessary.  If there is an open element
     * that has been created by a call to <code>startElement()</code>,
     * that element will be closed first.</p>
     *
     * @param text                  Text to be written
     * @param componentPropertyName The name of the component property to
     *                              which this text argument applies.  This argument may be <code>null</code>.
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>text</code>
     *                              is <code>null</code>
     */
    public void writeText(Object text, String componentPropertyName)
        throws IOException {
        if (text == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        closeStartIfNecessary();
        if (dontEscape) {
            writer.write(text.toString());
        } else {
            HtmlUtils.writeText(writer, buffer, text.toString());
        }
    }


    /**
     * <p>Write a properly escaped single character, If there
     * is an open element that has been created by a call to
     * <code>startElement()</code>, that element will be closed first.</p>
     * <p/>
     * <p>All angle bracket occurrences in the argument must be escaped
     * using the &amp;gt; &amp;lt; syntax.</p>
     *
     * @param text Text to be written
     * @throws IOException if an input/output error occurs
     */
    public void writeText(char text) throws IOException {
        closeStartIfNecessary();
        if (dontEscape) {
            writer.write(text);
        } else {
            charHolder[0] = text;
            HtmlUtils.writeText(writer, buffer, charHolder);
        }
    }


    /**
     * <p>Write properly escaped text from a character array.
     * The output from this command is identical to the invocation:
     * <code>writeText(c, 0, c.length)</code>.
     * If there is an open element that has been created by a call to
     * <code>startElement()</code>, that element will be closed first.</p>
     * </p>
     * <p/>
     * <p>All angle bracket occurrences in the argument must be escaped
     * using the &amp;gt; &amp;lt; syntax.</p>
     *
     * @param text Text to be written
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>text</code>
     *                              is <code>null</code>
     */
    public void writeText(char text[]) throws IOException {
        if (text == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        closeStartIfNecessary();
        if (dontEscape) {
            writer.write(text);
        } else {
            HtmlUtils.writeText(writer, buffer, text);
        }
    }


    /**
     * <p>Write properly escaped text from a character array.
     * If there is an open element that has been created by a call
     * to <code>startElement()</code>, that element will be closed
     * first.</p>
     * <p/>
     * <p>All angle bracket occurrences in the argument must be escaped
     * using the &amp;gt; &amp;lt; syntax.</p>
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
    public void writeText(char text[], int off, int len)
        throws IOException {
        if (text == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (off < 0 || off > text.length || len < 0 || len > text.length) {
            throw new IndexOutOfBoundsException();
        }
        closeStartIfNecessary();
        if (dontEscape) {
            writer.write(text, off, len);
        } else {
            HtmlUtils.writeText(writer, buffer, text, off, len);
        }
    }


    /**
     * <p>Create a new instance of this <code>ResponseWriter</code> using
     * a different <code>Writer</code>.
     *
     * @param writer The <code>Writer</code> that will be used to create
     *               another <code>ResponseWriter</code>.
     */
    public ResponseWriter cloneWithWriter(Writer writer) {
        try {
            return new CustomResponseWriter(writer, getContentType(),
                                          getCharacterEncoding());
        } catch (FacesException e) {
            // This should never happen
            throw new IllegalStateException();
        }
    }


    /**
     * This method automatically closes a previous element (if not
     * already closed).
     */
    private void closeStartIfNecessary() throws IOException {
        if (closeStart) {
            //PENDING (horwat) using String as a result of Tomcat char 
            //         writer ArrayIndexOutOfBoundsException (3584)
            writer.write(">");
            closeStart = false;
        }
    }


    /**
     * Methods From <code>java.io.Writer</code>
     */

    public void close() throws IOException {
        closeStartIfNecessary();
        writer.close();
    }


    public void write(char cbuf) throws IOException {
        closeStartIfNecessary();
        writer.write(cbuf);
    }


    public void write(char[] cbuf, int off, int len) throws IOException {
        closeStartIfNecessary();
        writer.write(cbuf, off, len);
    }


    public void write(int c) throws IOException {
        closeStartIfNecessary();
        writer.write(c);
    }


    public void write(String str) throws IOException {
        closeStartIfNecessary();
        writer.write(str);
    }


    public void write(String str, int off, int len) throws IOException {
        closeStartIfNecessary();
        writer.write(str, off, len);
    }
}
