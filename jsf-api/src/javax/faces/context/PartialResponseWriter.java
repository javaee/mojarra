
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

package javax.faces.context;

import javax.faces.component.UIComponent;
import java.util.Map;
import java.io.IOException;

/**
 * <p class="changed_added_2_0"><strong>PartialResponseWriter</strong>
 * decorates an existing <code>ResponseWriter</code> to support the
 * generation of a partial response suitable for Ajax operations.
 * In addition to the markup generation methods inherited from
 * <code>javax.faces.context.ResponseWriter</code>, this class provides
 * methods for constructing the standard partial response elements.</p>
 *
 * @since 2.0
 */
public class PartialResponseWriter extends ResponseWriterWrapper {
    // True when we need to close a start tag
    //
    private boolean inChanges = false;

    // True when we need to close a before insert tag
    //
    private boolean inInsertBefore = false;

    // True when we need to close afer insert tag
    //
    private boolean inInsertAfter = false;

    CDataEscapingResponseWriter writer;

    // convenience strings for start end end of cdata blocks
    private static final String STARTCDATA = "<![CDATA[";
    private static final String ENDCDATA= "]]>";

    /**
     * <p class="changed_added_2_0">Reserved ID value to indicate
     * entire ViewRoot.</p>
     *
     * @since 2.0
     */
    public static final String RENDER_ALL_MARKER = "javax.faces.ViewRoot";

    /**
     * <p class="changed_added_2_0">Reserved ID value to indicate
     * serialized ViewState.</p>
     *
     * @since 2.0
     */
    public static final String VIEW_STATE_MARKER = "javax.faces.ViewState";

    /**
     * <p class="changed_added_2_0">Create a <code>PartialResponseWriter</code>.</p>
     *
     * @param writer The writer to wrap.
     *
     * @since 2.0
     */
    public PartialResponseWriter(ResponseWriter writer)  {
        this.writer = new CDataEscapingResponseWriter(writer);
    }

    /**
     * <p class="changed_added_2_0">Return the wrapped
     *{@link ResponseWriter} instance.</p>
     * @see ResponseWriterWrapper#getWrapped()
     *
     * @since 2.0
     */
    public ResponseWriter getWrapped()  {
        return writer;
    }

    /**
     * <p class="changed_added_2_0">Write the start of a partial response.</p>
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void startDocument() throws IOException  {
        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        writer.startElement("partial-response", null);
    }

    /**
     * <p class="changed_added_2_0">Write the end of a partial response.</p>
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void endDocument() throws IOException  {
        endChangesIfNecessary();
        writer.endElement("partial-response");
    }

    /**
     * <p class="changed_added_2_0">Write the start of an insert operation
     * where the contents will be inserted before the specified target node.</p>
     *
     * @param targetId ID of the node insertion should occur before
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void startInsertBefore(String targetId)
        throws IOException  {
        startChangesIfNecessary();
        inInsertBefore = true;
        writer.startElement("insert", null);
        writer.startElement("before", null);
        writer.writeAttribute("id", targetId, null);
        writer.write(STARTCDATA);
        writer.setInCdata(true);
    }

    /**
     * <p class="changed_added_2_0">Write the start of an insert operation
     * where the contents will be inserted after the specified target node.</p>
     *
     * @param targetId ID of the node insertion should occur after
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void startInsertAfter(String targetId)
        throws IOException  {
        startChangesIfNecessary();
        inInsertAfter = true;
        writer.startElement("insert", null);
        writer.startElement("after", null);
        writer.writeAttribute("id", targetId, null);
        writer.write(STARTCDATA);
        writer.setInCdata(true);
    }

    /**
     * <p class="changed_added_2_0">Write the end of an insert operation.</p>
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void endInsert() throws IOException  {
        writer.endElement("CDATA");
        if (inInsertBefore) {
            writer.endElement("before");
            inInsertBefore = false;
        } else if (inInsertAfter) {
            writer.endElement("after");
            inInsertAfter = false;
        }
        writer.endElement("insert");
    }

    /**
     * <p class="changed_added_2_0">Write the start of an update operation.</p>
     *
     * @param targetId ID of the node to be updated
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void startUpdate(String targetId)  throws IOException {
        startChangesIfNecessary();
        writer.startElement("update", null);
        writer.writeAttribute("id", targetId, null);
        writer.write(STARTCDATA);
        writer.setInCdata(true);
    }

    /**
     * <p class="changed_added_2_0">Write the end of an update operation.</p>
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void endUpdate() throws IOException  {
        writer.setInCdata(false);
        writer.write(ENDCDATA);
        writer.endElement("update");
    }

    /**
     * <p class="changed_added_2_0">Write an attribute update operation.</p>
     *
     * @param targetId ID of the node to be updated
     * @param attributes Map of attribute name/value pairs to be updated
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void updateAttributes(String targetId, Map<String,String> attributes)
        throws IOException  {
        startChangesIfNecessary();
        writer.startElement("attributes", null);
        writer.writeAttribute("id", targetId, null);
        for (Map.Entry<String,String> entry : attributes.entrySet()) {
            writer.startElement("attribute", null);
            writer.writeAttribute("name", entry.getKey(), null);
            writer.writeAttribute("value", entry.getValue(), null);
            writer.endElement("attribute");
        }
        writer.endElement("attributes");
    }

    /**
     * <p class="changed_added_2_0">Write a delete operation.</p>
     *
     * @param targetId ID of the node to be deleted
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void delete(String targetId) throws IOException  {
        startChangesIfNecessary();
        writer.startElement("delete", null);
        writer.writeAttribute("id", targetId, null);
        writer.endElement("delete");
    }

    /**
     * <p class="changed_added_2_0">Write a redirect operation.</p>
     *
     * @param url URL to redirect to
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void redirect(String url) throws IOException {
        endChangesIfNecessary();
        writer.startElement("redirect", null);
        writer.writeAttribute("url", url, null);
        writer.endElement("redirect");
    }

    /**
     * <p class="changed_added_2_0">Write the start of an eval operation.</p>
     *
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void startEval() throws IOException  {
        startChangesIfNecessary();
        writer.startElement("eval", null);
        writer.write(STARTCDATA);
        writer.setInCdata(true);
    }

    /**
     * <p class="changed_added_2_0">Write the end of an eval operation.</p>
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void endEval() throws IOException  {
        writer.setInCdata(false);
        writer.write(ENDCDATA);
        writer.endElement("eval");
    }

    /**
     * <p class="changed_added_2_0">Write the start of an extension operation.</p>
     *
     * @param attributes String name/value pairs for extension element attributes
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void startExtension(Map<String,String> attributes) throws IOException  {
        endChangesIfNecessary();
        writer.startElement("extension", null);
        if (attributes != null && !attributes.isEmpty())  {
            for (Map.Entry<String,String> entry : attributes.entrySet()) {
                writer.writeAttribute(entry.getKey(), entry.getValue(), null);
            }
        }
    }

    /**
     * <p class="changed_added_2_0">Write the end of an extension operation.</p>
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void endExtension() throws IOException  {
        writer.endElement("extension");
    }

    /**
     * <p class="changed_added_2_0">Write the start of an error.</p>
     *
     * @param errorName Descriptive string for the error
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void startError(String errorName) throws IOException  {
        endChangesIfNecessary();
        writer.startElement("error", null);
        writer.startElement("error-name", null);
        writer.write(errorName);
        writer.endElement("error-name");
        writer.startElement("error-message", null);
        writer.write(STARTCDATA);
        writer.setInCdata(true);
    }

    /**
     * <p class="changed_added_2_0">Write the end of an error.</p>
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void endError() throws IOException  {
        writer.setInCdata(false);
        writer.write(ENDCDATA);
        writer.endElement("error-message");
        writer.endElement("error");
    }

    private void startChangesIfNecessary() throws IOException  {
        if (!inChanges)  {
            writer.startElement("changes", null);
            inChanges = true;
        }
    }

    private void endChangesIfNecessary() throws IOException  {
        if (inChanges)  {
            writer.endElement("changes");
            inChanges = false;
        }
    }

    /*
     *  Private class to handle cdata escaping.
     *  if the passed text contains either "]]" or "<!", then the CDATA block
     *  needs to be split into two cdata blocks - for example,
     *  "]]" will become "]]]><![CDATA[]", splitting the two brackets.
     *
     *  Also, we'll be keeping track of whether we're in a cdata block.
     *
     *  if we are in a cdata block, bypass all writeText methods, so no
     *  transformation is done on the passed data - it's a literal block,
     *  after all.
     *
     */
    private static class CDataEscapingResponseWriter extends ResponseWriterWrapper {

        private static final String BREAKCDATA= "]]><![CDATA[";
        private static final String ESCAPEDSINGLEBRACKET = "]"+BREAKCDATA;
        private static final String ESCAPEDLT= "&lt;"+BREAKCDATA;
        private static final String ESCAPEDSTART= "&lt;"+BREAKCDATA+"!";
        private static final String ESCAPEDEND= "]"+BREAKCDATA+"]";

        private static final int CLOSEBRACKET = (int)']';
        private static final int LT = (int)'<';

        private boolean inCdata = false;

        // Writer to wrap
        ResponseWriter writer;

        private CDataEscapingResponseWriter(ResponseWriter writer) {
            this.writer = writer;
        }

        public ResponseWriter getWrapped() {
            return writer;
        }

        public void setInCdata(boolean inCdata) {
            this.inCdata = inCdata;
        }

    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#writeText(Object, String)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#writeText(Object, String)
     * @since 1.2
     */
        public void writeText(Object text, String property) throws IOException {

        if (text == null) {  // if null, passthru for error generation
            getWrapped().writeText(text, property);
            return;
        }
        if (inCdata) {
            getWrapped().write(escapeArray(text.toString().toCharArray()));
        } else {  // not in cdata, just passthru
            getWrapped().writeText(text, property);
        }
        }

    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#writeText(Object, javax.faces.component.UIComponent , String)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#writeText(Object, String)
     * @since 1.2
     */
        public void writeText(Object text, UIComponent component, String property)
            throws IOException {
        if (text == null) {  // if null, passthru for error generation
            getWrapped().writeText(text, component, property);
            return;
        }
        if (inCdata) {
            getWrapped().write(escapeArray(text.toString().toCharArray()));
        } else {  // not in cdata, just passthru
            getWrapped().writeText(text, component, property);
        }
        }


    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#writeText(char[], int, int)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#writeText(char[], int, int)
     * @since 1.2
     */
    public void writeText(char[] text, int off, int len) throws IOException {

        if (off < 0 || len < 0 || off + len > text.length ) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > text.length");
        }

        if (inCdata) {
            char[] cbuf = new char[off+len];
            for (int i = off, j = 0; j < len; i++, j++) {
                cbuf[j] = text[i];
            }
            getWrapped().write(escapeArray(cbuf));
        } else {
            getWrapped().writeText(text,off,len);
        }

    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ResponseWriter#write(char[], int, int)}
     * on the wrapped {@link ResponseWriter} object.</p>
     *
     * @see ResponseWriter#write(char[], int, int)
     * @since 1.2
     */
    public void write(char[] cbuf, int off, int len) throws IOException {

        if (off < 0 || len < 0 || off + len > cbuf.length ) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > cbuf.length");
        }

        if (inCdata) {
            char[] nbuf = new char[off+len];
            for (int i = off, j = 0; j < len; i++, j++) {
                nbuf[j] = cbuf[i];
            }
            getWrapped().write(escapeArray(nbuf));
        } else {
            getWrapped().write(cbuf, off, len);
        }

    }

    @Override
    public void write(char[] cbuf) throws IOException {

        if (inCdata) {
            getWrapped().write(escapeArray(cbuf));
        } else {
            getWrapped().write(cbuf);
        }
    }

    @Override
    public void write(int c) throws IOException {


        // RELEASE_PENDING add buffer for lookahead
        if (inCdata) {
            if (c == CLOSEBRACKET) {
                inCdata = false;
                getWrapped().write(ESCAPEDSINGLEBRACKET);
                inCdata = true;
            } else if (c == LT) {
                getWrapped().write(ESCAPEDLT);
            } else {
                getWrapped().write(c);
            }
        } else {  // not in cdata, just passthru
            getWrapped().write(c);
        }

    }

    @Override
    public void write(String str) throws IOException {

        if (str == null) {  // if null, passthru for error generation
            getWrapped().write(str);
            return;
        }

        if (inCdata) {
            write(str.toCharArray());
        } else {  // not in cdata, just passthru
            getWrapped().write(str);
        }
    }

    @Override
    public void write(String str, int off, int len) throws IOException {

        if (off < 0 || len < 0 || off + len > str.length() ) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > str.length()");
        }

        if (inCdata) {
            write(str.substring(off, off+len).toCharArray());
        } else {
            getWrapped().write(str, off, len);
        }

    }

        // RELEASE_PENDING Consider changing from returning a String to returning a char array
       // RELEASE_PENDING Add a character array buffer, as well as unwinding the final Strings into char[]
        /*
         *  Method to escape all CDATA instances in a character array.
         */
    private String escapeArray(char cbuf[]) {
        if (cbuf == null || cbuf.length == 0) {
            return "";
        }
        boolean last = false;
        StringBuilder builder = new StringBuilder(cbuf.length);
        for (int i = 0; i < cbuf.length-1; i++) {
            if (cbuf[i] == '<' && cbuf[i+1] == '!') {
                builder.append(ESCAPEDSTART);
                i++;
            } else if (cbuf[i] == ']' && cbuf[i+1] == ']') {
                builder.append(ESCAPEDEND);
                i++;
            } else {
                builder.append(cbuf[i]);
            }
            if (i == cbuf.length - 1) {
                    last = true;
            }
        }
        // if we didn't look at the last, look at it now
        // RELEASE_PENDING consider buffering the last character, in this case
        if (!last) {
            if (cbuf[cbuf.length-1] == '<') {
                builder.append(ESCAPEDLT);
            } else if (cbuf[cbuf.length-1] == '[') {
                builder.append(ESCAPEDSINGLEBRACKET);
            } else {
                builder.append(cbuf[cbuf.length-1]);
            }
        }

        return builder.toString();
    }

    }

}
