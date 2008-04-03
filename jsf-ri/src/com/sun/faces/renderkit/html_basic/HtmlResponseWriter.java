/*
 * $Id: HtmlResponseWriter.java,v 1.53 2008/02/27 17:09:05 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.RIConstants;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.io.FastStringWriter;
import com.sun.faces.util.HtmlUtils;
import com.sun.faces.util.MessageUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.ExternalContext;


/**
 * <p><strong>HtmlResponseWriter</strong> is an Html specific implementation
 * of the <code>ResponseWriter</code> abstract class.
 * Kudos to Adam Winer (Oracle) for much of this code.
 */
public class HtmlResponseWriter extends ResponseWriter {


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

    // flag to indicate we're writing a CDATA section
    private boolean writingCdata;

    // flat to indicate the current element is CDATA
    private boolean isCdata;

    // flag to indicate that we're writing a 'script' or 'style' element
    private boolean isScript;

    // flag to indicate that we're writing a 'style' element
    private boolean isStyle;

    // flag to indicate that we're writing a 'src' attribute as part of
    // 'script' or 'style' element
    private boolean scriptOrStyleSrc;

    // flag to indicate if the content type is Xhtml
    private boolean isXhtml;

    // HtmlResponseWriter to use when buffering is required
    private Writer origWriter;

    // Keep one instance of the script buffer per Writer
    private FastStringWriter scriptBuffer;

    // Keep one instance of attributesBuffer to buffer the writting
    // of all attributes for a particular element to reducr the number
    // of writes
    private FastStringWriter attributesBuffer;
    
    // Enables hiding of inlined script and style
    // elements from old browsers
    private Boolean isScriptHidingEnabled;
    
    // Enables scripts to be included in attribute values
    private Boolean isScriptInAttributeValueEnabled;
    
    // Keep the map passed in our ctor so we can pass it to
    // cloneWithWriter
    private Map<WebConfiguration.BooleanWebContextInitParameter,
                                  Boolean> configPrefs;

    // Internal buffer used when outputting properly escaped information
    // using HtmlUtils class.
    //
    private char[] buffer = new char[1028];

    // Internal buffer for to store the result of String.getChars() for
    // values passed to the writer as String to reduce the overhead
    // of String.charAt().  This buffer will be grown, if necessary, to
    // accomodate larger values.
    private char[] textBuffer = new char[128];

    private char[] charHolder = new char[1];

    static final Pattern CDATA_START_SLASH_SLASH;

    static final Pattern CDATA_END_SLASH_SLASH;

    static final Pattern CDATA_START_SLASH_STAR;

    static final Pattern CDATA_END_SLASH_STAR;

    static {
        // At the beginning of a line, match // followed by any amount of
        // whitespace, followed by <![CDATA[
        CDATA_START_SLASH_SLASH = Pattern.compile("^//\\s*\\Q<![CDATA[\\E");

        // At the end of a line, match // followed by any amout of whitespace,
        // followed by ]]>
        CDATA_END_SLASH_SLASH = Pattern.compile("//\\s*\\Q]]>\\E$");

        // At the beginning of a line, match /* followed by any amout of
        // whitespace, followed by <![CDATA[, followed by any amount of whitespace,
        // followed by */
        CDATA_START_SLASH_STAR = Pattern.compile("^/\\*\\s*\\Q<![CDATA[\\E\\s*\\*/");

        // At the end of a line, match /* followed by any amount of whitespace,
        // followed by ]]> followed by any amount of whitespace, followed by */
        CDATA_END_SLASH_STAR = Pattern.compile("/\\*\\s*\\Q]]>\\E\\s*\\*/$");

    }

    // ------------------------------------------------------------ Constructors


    /**
     * Constructor sets the <code>ResponseWriter</code> and
     * encoding, and enables script hiding by default.
     *
     * @param writer      the <code>ResponseWriter</code>
     * @param contentType the content type.
     * @param encoding    the character encoding.
     *
     * @throws javax.faces.FacesException the encoding is not recognized.
     */
    public HtmlResponseWriter(Writer writer,
                              String contentType,
                              String encoding)
    throws FacesException {
        this(writer, contentType, encoding, null);

    }
    
    private Map initConfigPrefs() {
        WebConfiguration webConfig = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            ExternalContext extContext = context.getExternalContext();
            if (null != extContext) {
                webConfig = WebConfiguration.getInstance(extContext);
            }
        }
        
        
        Map<WebConfiguration.BooleanWebContextInitParameter,
                Boolean> prefs = 
            new HashMap<WebConfiguration.BooleanWebContextInitParameter,
                        Boolean>();

        prefs.put(BooleanWebContextInitParameter.PreferXHTMLContentType,
                (null == webConfig) ? BooleanWebContextInitParameter.PreferXHTMLContentType.getDefaultValue() :
                        webConfig.isOptionEnabled(
                         BooleanWebContextInitParameter.PreferXHTMLContentType));
        prefs.put(BooleanWebContextInitParameter.EnableJSStyleHiding,
                (null == webConfig) ? BooleanWebContextInitParameter.EnableJSStyleHiding.getDefaultValue() :
                        webConfig.isOptionEnabled(
                         BooleanWebContextInitParameter.EnableJSStyleHiding));
        prefs.put(BooleanWebContextInitParameter.EnableScriptInAttributeValue,
                (null == webConfig) ? BooleanWebContextInitParameter.EnableScriptInAttributeValue.getDefaultValue() :
                        webConfig.isOptionEnabled(
                         BooleanWebContextInitParameter.EnableScriptInAttributeValue));
        
        return Collections.unmodifiableMap(prefs);
    }

    /**
     * <p>Constructor sets the <code>ResponseWriter</code> and
     * encoding.</p>
     * 
     * <p>The argument configPrefs is a map of configurable prefs that affect 
     * this instance's behavior.  Supported keys are:</p>
     * 
     * <p>BooleanWebContextInitParameter.EnableJSStyleHiding: <code>true</code> 
     * if the writer should attempt to hide JS from older browsers</p>
     *
     * @param writer      the <code>ResponseWriter</code>
     * @param contentType the content type.
     * @param encoding    the character encoding.
     * @param configPrefs 
     *
     * @throws javax.faces.FacesException the encoding is not recognized.
     */
    public HtmlResponseWriter(Writer writer,
                              String contentType,
                              String encoding,
                              Map<WebConfiguration.BooleanWebContextInitParameter,
                                  Boolean> configPrefs)
    throws FacesException {

        this.writer = writer;

        if (null != contentType) {
            this.contentType = contentType;
        }

        this.encoding = encoding;
        
        this.configPrefs = (null != configPrefs) ? configPrefs : initConfigPrefs();
        this.isScriptHidingEnabled =
                (this.configPrefs.containsKey(BooleanWebContextInitParameter.EnableJSStyleHiding)) ?
                    this.configPrefs.get(BooleanWebContextInitParameter.EnableJSStyleHiding) : Boolean.FALSE;
        this.isScriptInAttributeValueEnabled = 
                (this.configPrefs.containsKey(BooleanWebContextInitParameter.EnableScriptInAttributeValue)) ?
                    this.configPrefs.get(BooleanWebContextInitParameter.EnableScriptInAttributeValue) : Boolean.FALSE;
                
        this.attributesBuffer = new FastStringWriter(128);

        // Check the character encoding
        if (!HtmlUtils.validateEncoding(encoding)) {
            throw new IllegalArgumentException(MessageUtils.getExceptionMessageString(
                  MessageUtils.ENCODING_ERROR_MESSAGE_ID));
        }

    }
    
    // -------------------------------------------------- Methods From Closeable


    /** Methods From <code>java.io.Writer</code> */

    public void close() throws IOException {

        closeStartIfNecessary();
        writer.close();

    }

    // -------------------------------------------------- Methods From Flushable


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

    // ---------------------------------------------------------- Public Methods


    /** @return the content type such as "text/html" for this ResponseWriter. */
    public String getContentType() {

        return contentType;

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
            return new HtmlResponseWriter(writer,
                                          getContentType(),
                                          getCharacterEncoding(),
                                          configPrefs);
        } catch (FacesException e) {
            // This should never happen
            throw new IllegalStateException();
        }

    }


    /** Output the text for the end of a document. */
    public void endDocument() throws IOException {

        writer.flush();

    }


    /**
     * <p>Write the end of an element. This method will first
     * close any open element created by a call to
     * <code>startElement()</code>.
     *
     * @param name Name of the element to be ended
     *
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>name</code>
     *                              is <code>null</code>
     */
    public void endElement(String name) throws IOException {

        if (name == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "name"));
        }

        if (!writingCdata) {
            // always turn escaping back on once an element ends unless we're
            // still writing cdata content
            dontEscape = false;
        }
        isXhtml = getContentType().equals(
            RIConstants.XHTML_CONTENT_TYPE);
        // Ensure we have a writer to which we can write.  Make sure
        // to honor decoration.
        FacesContext context = FacesContext.getCurrentInstance();
        ResponseWriter writerFromContext = (null != context) ?
            context.getResponseWriter() :
            this;
        writerFromContext = (null == writerFromContext) ? this : writerFromContext;


        if (isScriptOrStyle(name)
             && !scriptOrStyleSrc
             && writer instanceof FastStringWriter) {
            String result = ((FastStringWriter) writer).getBuffer().toString();
            writer = origWriter;

            if (result != null) {
                String trim = result.trim();
                if (isXhtml) {


                    if (isScript) {
                        Matcher
                            cdataStartSlashSlash =
                              CDATA_START_SLASH_SLASH.matcher(trim),
                            cdataEndSlashSlash =
                              CDATA_END_SLASH_SLASH.matcher(trim),
                            cdataStartSlashStar =
                              CDATA_START_SLASH_STAR.matcher(trim),
                            cdataEndSlashStar =
                              CDATA_END_SLASH_STAR.matcher(trim);
                        int trimLen = trim.length(), start, end;
                        // case 1 start is // end is //
                        if (cdataStartSlashSlash.find() &&
                            cdataEndSlashSlash.find()) {
                            start = cdataStartSlashSlash.end() - cdataStartSlashSlash.start();
                            end = trimLen - (cdataEndSlashSlash.end() - cdataEndSlashSlash.start());
                            writerFromContext.write(trim.substring(start, end));
                        }
                        // case 2 start is // end is /* */
                        else if ((null != cdataStartSlashSlash.reset() && cdataStartSlashSlash.find()) &&
                                 cdataEndSlashStar.find()) {
                            start = cdataStartSlashSlash.end() - cdataStartSlashSlash.start();
                            end = trimLen - (cdataEndSlashStar.end() - cdataEndSlashStar.start());
                            writerFromContext.write(trim.substring(start, end));
                        }
                        // case 3 start is /* */ end is /* */
                        else if (cdataStartSlashStar.find() &&
                                 (null != cdataEndSlashStar.reset() && cdataEndSlashStar.find())) {
                            start = cdataStartSlashStar.end() - cdataStartSlashStar.start();
                            end = trimLen - (cdataEndSlashStar.end() - cdataEndSlashStar.start());
                            writerFromContext.write(trim.substring(start, end));
                        }
                        // case 4 start is /* */ end is //
                        else if ((null != cdataStartSlashStar.reset() && cdataStartSlashStar.find()) &&
                                 (null != cdataEndSlashStar.reset() && cdataEndSlashSlash.find())) {
                            start = cdataStartSlashStar.end() - cdataStartSlashStar.start();
                            end = trimLen - (cdataEndSlashSlash.end() - cdataEndSlashSlash.start());
                            writerFromContext.write(trim.substring(start, end));
                        }
                        // case 5 no commented out cdata present.
                        else {
                            writerFromContext.write(result);
                        }
                    } else {
                        if (trim.startsWith("<![CDATA[") && trim.endsWith("]]>")) {
                            writerFromContext.write(trim.substring(9, trim.length() - 3));
                        } else {
                            writerFromContext.write(result);
                        }
                    }
                } else {
                    if (trim.startsWith("<!--") && trim.endsWith("//-->")) {
                        writerFromContext.write(trim.substring(4, trim.length() - 5));
                    } else {
                        writerFromContext.write(result);
                    }
                }
            }
            if (isXhtml) {
                if (!writingCdata) {
                    if (isScript) {
                        writerFromContext.write("\n//]]>\n");
                    } else {
                        writerFromContext.write("\n]]>\n");
                    }
                }
            } else {
                if (isScriptHidingEnabled) {
                    writerFromContext.write("\n//-->\n");
                }
            }
        }
        isScript = false;
        isStyle = false;
        if ("cdata".equalsIgnoreCase(name)) {
            writerFromContext.write("]]>");
            writingCdata = false;
            isCdata = false;
            dontEscape = false;
            return;
        }
        // See if we need to close the start of the last element
        if (closeStart) {
            boolean isEmptyElement = HtmlUtils.isEmptyElement(name);

            // Tricky: we need to use the writer ivar here, rather than the
            // one from the FacesContext because we don't want
            // spurious /> characters to appear in the output.
            if (isEmptyElement) {
                flushAttributes();
                writer.write(" />");
                closeStart = false;
                return;
            }
            flushAttributes();
            writer.write('>');
            closeStart = false;
        }

        writerFromContext.write("</");
        writerFromContext.write(name);
        writerFromContext.write('>');

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
     * <p>Write the start of an element, up to and including the
     * element name.  Clients call <code>writeAttribute()</code> or
     * <code>writeURIAttribute()</code> methods to add attributes after
     * calling this method.
     *
     * @param name                Name of the starting element
     * @param componentForElement The UIComponent instance that applies to this
     *                            element.  This argument may be <code>null</code>.
     *
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>name</code>
     *                              is <code>null</code>
     */
    public void startElement(String name, UIComponent componentForElement)
          throws IOException {

        if (name == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "name"));
        }
        closeStartIfNecessary();
        isScriptOrStyle(name);
        scriptOrStyleSrc = false;
        if ("cdata".equalsIgnoreCase(name)) {
            isCdata = true;
            writingCdata = true;
            dontEscape = true;
            writer.write("<![CDATA[");
            closeStart = false;
            return;
        } else if (writingCdata) {
            // starting an element within a cdata section,
            // keep escaping disabled
            isCdata = false;
            writingCdata = true;
            dontEscape = true;
        }

        writer.write('<');
        writer.write(name);
        closeStart = true;

    }

    @Override
    public void write(char[] cbuf) throws IOException {

        closeStartIfNecessary();
        writer.write(cbuf);

    }

    @Override
    public void write(int c) throws IOException {

        closeStartIfNecessary();
        writer.write(c);

    }

    @Override
    public void write(String str) throws IOException {

        closeStartIfNecessary();
        writer.write(str);

    }


    public void write(char[] cbuf, int off, int len) throws IOException {

        closeStartIfNecessary();
        writer.write(cbuf, off, len);

    }

    @Override
    public void write(String str, int off, int len) throws IOException {

        closeStartIfNecessary();
        writer.write(str, off, len);

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
     *
     * @throws IllegalStateException if this method is called when there
     *                               is no currently open element
     * @throws IOException           if an input/output error occurs
     * @throws NullPointerException  if <code>name</code> is <code>null</code>
     */
    public void writeAttribute(String name, Object value,
                               String componentPropertyName)
          throws IOException {

        if (name == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "name"));
        }
        if (value == null) {
            return;
        }

        if (isCdata) {
            return;
        }

        if (name.equalsIgnoreCase("src") && isScriptOrStyle()) {
            scriptOrStyleSrc = true;
        }

        Class valueClass = value.getClass();

        // Output Boolean values specially
        if (valueClass == Boolean.class) {
            if (Boolean.TRUE.equals(value)) {
                // NOTE:  HTML 4.01 states that boolean attributes
                //        may legally take a single value which is the
                //        name of the attribute itself or appear using
                //        minimization.  
                //  http://www.w3.org/TR/html401/intro/sgmltut.html#h-3.3.4.2
                attributesBuffer.write(' ');
                attributesBuffer.write(name);
                attributesBuffer.write("=\"");
                attributesBuffer.write(name);
                attributesBuffer.write('"');
            }
        } else {
            attributesBuffer.write(' ');
            attributesBuffer.write(name);
            attributesBuffer.write("=\"");
            // write the attribute value
            String val = value.toString();
            ensureTextBufferCapacity(val);
            HtmlUtils.writeAttribute(attributesBuffer,
                                     buffer,
                                     val,
                                     textBuffer, isScriptInAttributeValueEnabled);
            attributesBuffer.write('"');
        }

    }


    /**
     * <p>Write a comment string containing the specified text.
     * The text will be converted to a String if necessary.
     * If there is an open element that has been created by a call
     * to <code>startElement()</code>, that element will be closed
     * first.</p>
     *
     * @param comment Text content of the comment
     *
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>comment</code>
     *                              is <code>null</code>
     */
    public void writeComment(Object comment) throws IOException {

        if (comment == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (writingCdata) {
            return;
        }

        closeStartIfNecessary();
        // Don't include a trailing space after the '<!--'
        // or a leading space before the '-->' to support
        // IE conditional commentsoth
        writer.write("<!--");
        writer.write(comment.toString());
        writer.write("-->");

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
     *
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
     *
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>text</code>
     *                              is <code>null</code>
     */
    public void writeText(char text[]) throws IOException {

        if (text == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "text"));
        }
        closeStartIfNecessary();
        if (dontEscape) {
            writer.write(text);
        } else {
            HtmlUtils.writeText(writer, buffer, text);
        }

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
     *
     * @throws IOException          if an input/output error occurs
     * @throws NullPointerException if <code>text</code>
     *                              is <code>null</code>
     */
    public void writeText(Object text, String componentPropertyName)
          throws IOException {

        if (text == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "text"));
        }
        closeStartIfNecessary();
        if (dontEscape) {
            writer.write(text.toString());
        } else {
            String val = text.toString();
            ensureTextBufferCapacity(val);
            HtmlUtils.writeText(writer,
                                buffer,
                                val,
                                textBuffer);
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
     *
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
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "text"));
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
     *
     * @throws IllegalStateException if this method is called when there
     *                               is no currently open element
     * @throws IOException           if an input/output error occurs
     * @throws NullPointerException  if <code>name</code> or
     *                               <code>value</code> is <code>null</code>
     */
    public void writeURIAttribute(String name, Object value,
                                  String componentPropertyName)
          throws IOException {

        if (name == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "name"));
        }
        if (value == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "value"));
        }

        if (isCdata) {
            return;
        }

        if (name.equalsIgnoreCase("src") && isScriptOrStyle()) {
            scriptOrStyleSrc = true;
        }

        attributesBuffer.write(' ');
        attributesBuffer.write(name);
        attributesBuffer.write("=\"");

        String stringValue = value.toString();
        ensureTextBufferCapacity(stringValue);
        // Javascript URLs should not be URL-encoded
        if (stringValue.startsWith("javascript:")) {
            HtmlUtils.writeAttribute(attributesBuffer,
                                     buffer,
                                     stringValue,
                                     textBuffer, isScriptInAttributeValueEnabled);
        } else {
            HtmlUtils.writeURL(attributesBuffer,
                               stringValue,
                               textBuffer,
                               encoding,
                               getContentType());
        }

        attributesBuffer.write('"');

    }

    // --------------------------------------------------------- Private Methods

    private void ensureTextBufferCapacity(String source) {
        int len = source.length();
        if (textBuffer.length < len) {
            textBuffer = new char[len * 2];
        }
    }

    /**
     * This method automatically closes a previous element (if not
     * already closed).
     * @throws IOException if an error occurs writing
     */
    private void closeStartIfNecessary() throws IOException {

        if (closeStart) {
            flushAttributes();
            writer.write('>');
            closeStart = false;
            if (isScriptOrStyle() && !scriptOrStyleSrc) {
                isXhtml = getContentType().equals(
                     RIConstants.XHTML_CONTENT_TYPE);
                if (isXhtml) {
                    if (!writingCdata) {
                        if (isScript) {
                            writer.write("\n//<![CDATA[\n");
                        } else {
                            writer.write("\n<![CDATA[\n");
                        }
                    }
                } else {
                    if (isScriptHidingEnabled) {
                        writer.write("\n<!--\n");
                    }
                }
                origWriter = writer;
                if (scriptBuffer == null) {
                    scriptBuffer = new FastStringWriter(1024);
                }
                scriptBuffer.reset();
                writer = scriptBuffer;
                isScript = false;
                isStyle = false;
            }
        }

    }


    private void flushAttributes() throws IOException {

        // a little complex, but the end result is, potentially, two
        // fewer temp objects created per call.
        StringBuilder b = attributesBuffer.getBuffer();
        int totalLength = b.length();
        if (totalLength != 0) {
            int curIdx = 0;
            while (curIdx < totalLength) {
                if ((totalLength - curIdx) > buffer.length) {
                    int end = curIdx + buffer.length - 1;
                    b.getChars(curIdx, end, buffer, 0);
                    writer.write(buffer);
                    curIdx += buffer.length;
                } else {
                    int len = totalLength - curIdx;
                    b.getChars(curIdx, curIdx + len, buffer, 0);
                    writer.write(buffer, 0, len);
                    curIdx += len;
                }
            }
            attributesBuffer.reset();
        }

    }


    private boolean isScriptOrStyle(String name) {
        if ("script".equalsIgnoreCase(name)) {
            isScript = true;
            dontEscape = true;
        } else if ("style".equalsIgnoreCase(name)) {
            isStyle = true;
            dontEscape = true;
        } else {
            isScript = false;
            isStyle = false;
            dontEscape = false;
        }

        return (isScript || isStyle);
    }

    private boolean isScriptOrStyle() {
        return (isScript || isStyle);
    }

}
