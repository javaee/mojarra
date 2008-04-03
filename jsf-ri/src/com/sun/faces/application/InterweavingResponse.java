/*
 * $Id: InterweavingResponse.java,v 1.2 2007/05/17 14:26:30 rlubke Exp $
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
* Copyright 2007 Sun Microsystems Inc. All Rights Reserved
*/


package com.sun.faces.application;

import java.io.Writer;
import java.io.IOException;

/**
 * Allow content interweaving via Servlet or Porlet using
 * a common interface.
 *
 * <p>
 * Implementations of this interface must cache all content,
 * beit byte or character following similar semantics of the
 * Servlet and Porlet response classes.  Buffered content should
 * only be written when explicitly requested.
 */
public interface InterweavingResponse {

    /**
     * Flush the current buffered content to the wrapped
     * response (this could be a Servlet or Portlet response)
     * @throws IOException if content cannot be written
     */
    public void flushContentToWrappedResponse() throws IOException;

    /**
     * Flush the current buffered content to the provided <code>Writer</code>
     * @param writer target <code>Writer</code>
     * @param encoding the encoding that should be used
     * @throws IOException if content cannot be written
     */
    public void flushToWriter(Writer writer, String encoding) throws IOException;

    /**
     * Clear the internal buffers.
     * @throws IOException if some odd error occurs
     */
    public void resetBuffers() throws IOException;

    /**
     * @return <code>true</code> if content has been
     * written using an <code>OutputStream</code>.
     */
    public boolean isBytes();


    /**
     * @return <code>true</code> if content has been
     * written using a <code>Writer</code>.
     */
    public boolean isChars();


    /**
     * @return the buffered character data
     */
    public char[] getChars();


    /**
     * @return the buffered byte data
     */
    public byte[] getBytes();


    /**
     * @return the status code of the wrapped response, if it makes
     *  sense to do so.
     */
    public int getStatus();
}
