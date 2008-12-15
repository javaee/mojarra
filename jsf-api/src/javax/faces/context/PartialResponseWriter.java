
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

import java.util.Map;
import java.io.IOException;
import java.io.Writer;

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

    ResponseWriter writer;

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
     * @since 2.0
     */
    public PartialResponseWriter(ResponseWriter writer)  {
        this.writer = writer;
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
     * @param fragmentId ID of the node to be inserted
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
        writer.write("<![CDATA[");
    }

    /**
     * <p class="changed_added_2_0">Write the start of an insert operation 
     * where the contents will be inserted after the specified target node.</p>
     *
     * @param fragmentId ID of the node to be inserted
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
        writer.write("<![CDATA[");
    }
   
   
    /**
     * <p class="changed_added_2_0">Write the end of an insert operation.</p>
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void endInsert() throws IOException  {
        writer.write("]]>");
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
        writer.write("<![CDATA[");
    }

    /**
     * <p class="changed_added_2_0">Write the end of an update operation.</p>
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void endUpdate() throws IOException  {
        writer.write("]]>");
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
        for (String name: attributes.keySet())  {
            writer.startElement("attribute", null);
            writer.writeAttribute("name", name, null);
            writer.writeAttribute("value", attributes.get(name), null);
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
        writer.endElement("recirect");
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
        writer.write("<![CDATA[");
    }

    /**
     * <p class="changed_added_2_0">Write the end of an eval operation.</p>
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void endEval() throws IOException  {
        writer.write("]]>");
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
        if (null != attributes)  {
            for (String name : attributes.keySet())  {
                writer.writeAttribute(name, attributes.get(name), null);
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
        writer.write("<![CDATA[");
    }

    /**
     * <p class="changed_added_2_0">Write the end of an error.</p>
     *
     * @throws IOException if an input/output error occurs
     *
     * @since 2.0
     */
    public void endError() throws IOException  {
        writer.write("]]>");
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
    
    
}
