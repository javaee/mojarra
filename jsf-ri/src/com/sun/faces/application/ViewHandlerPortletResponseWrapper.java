/*
 * $Id: ViewHandlerPortletResponseWrapper.java,v 1.2 2007/05/17 14:26:30 rlubke Exp $ 
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

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import java.io.*;
import java.util.Locale;


/**
 * This is an example of a Portlet implementation of the InterweavingResponse.
 * Bridges may use this code as an example, or refer to it directly.
 */
public class ViewHandlerPortletResponseWrapper implements RenderResponse, InterweavingResponse {

    private RenderResponse response;
    private ByteArrayWebOutputStream bawos;
    private PrintWriter pw ;
    private CharArrayWriter caw;



    public ViewHandlerPortletResponseWrapper(RenderResponse response) {
        this.response = response;
    }

    public void resetBuffers() throws IOException {
        if (caw != null) {
            caw.reset();
        } else if (bawos != null) {
            bawos.resetByteArray();
        }
    }

    public boolean isBytes() {
        return (bawos != null);
    }

    public boolean isChars() {
        return (caw != null);
    }

    public char[] getChars() {
        if (caw != null) {
            return caw.toCharArray();
        }
        return null;
    }

    public byte[] getBytes() {
        if (bawos != null) {
            return bawos.toByteArray();
        }
        return null;
    }

    public int getStatus() {
        return Integer.MIN_VALUE;
    }

    public String getContentType() {
        return response.getContentType();
    }

    public PortletURL createRenderURL() {
        return response.createRenderURL();
    }

    public PortletURL createActionURL() {
        return response.createActionURL();
    }

    public String getNamespace() {
        return response.getNamespace();
    }

    public void setTitle(String title) {
        response.setTitle(title);
    }

    public void setContentType(String type) {
        response.setContentType(type);
    }

    public String getCharacterEncoding() {
        return response.getCharacterEncoding();
    }

    public Locale getLocale() {
        return response.getLocale();
    }

    public void setBufferSize(int size) {
        response.setBufferSize(size);
    }

    public int getBufferSize() {
        return response.getBufferSize();
    }

    public void flushBuffer() throws IOException {
        response.flushBuffer();
    }

    public void resetBuffer() {
        response.resetBuffer();
    }

    public boolean isCommitted() {
        return response.isCommitted();
    }

    public void reset() {
        response.reset();
    }

    public PrintWriter getWriter() throws IOException {
        if (bawos != null) {
            throw new IllegalStateException();
        }
        if (pw == null) {
            caw = new CharArrayWriter(1024);
            pw = new PrintWriter(caw);
        }

        return pw;
    }

    public OutputStream getPortletOutputStream() throws IOException {
        if (pw != null) {
            throw new IllegalStateException();
        }
        if (bawos == null) {
            bawos = new ByteArrayWebOutputStream();
        }
        return bawos;
    }

    public void addProperty(String key, String value) {
        response.addProperty(key, value);
    }

    public void setProperty(String key, String value) {
        response.setProperty(key, value);
    }

    public String encodeURL(String path) {
        return response.encodeURL(path);
    }

    public void flushContentToWrappedResponse() throws IOException {
        if (caw != null) {
            pw.flush();
            caw.writeTo(response.getWriter());
            caw.reset();
        } else if (bawos != null) {
            try {
                bawos.writeTo(response.getWriter(),
                              response.getCharacterEncoding());
            } catch (IllegalStateException ise) {
                bawos.writeTo(response.getPortletOutputStream());
            }
            bawos.resetByteArray();
        }

    }
    public void flushToWriter(Writer writer, String encoding) throws IOException {
        if (caw != null) {
            pw.flush();
            caw.writeTo(writer);
            caw.reset();
        } else if (bawos != null) {
            bawos.writeTo(writer, encoding);
            bawos.resetByteArray();
        }
        writer.flush();
    }

}