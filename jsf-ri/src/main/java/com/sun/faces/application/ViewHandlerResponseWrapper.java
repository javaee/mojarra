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

// ViewHandlerResponseWrapper.java 

package com.sun.faces.application;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;


/**
 * <p>This class is used by {@link javax.faces.application.ViewHandler#createView} to obtain the
 * text that exists after the &lt;f:view&gt; tag.</p>
 */

public class ViewHandlerResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayWebOutputStream basos;
    private WebPrintWriter pw ;
    private CharArrayWriter caw;
    private int status = HttpServletResponse.SC_OK;


    public ViewHandlerResponseWrapper(HttpServletResponse wrapped) {
        super(wrapped);
    }


    @Override
    public void sendError(int sc, String msg) throws IOException {
        super.sendError(sc, msg);
        status = sc;
    }

    @Override
    public void sendError(int sc) throws IOException {
        super.sendError(sc);
        status = sc;
    }

    @Override
    public void setStatus(int sc) {
        super.setStatus(sc);
        status = sc;
    }

    @Override
    public void setStatus(int sc, String sm) {
        super.setStatus(sc, sm);
        status = sc;
    }


    public int getStatus() {
        return status;
    }

    public boolean isBytes() {
        return (null != basos);
    }

    public boolean isChars() {
        return (null != caw);
    }

    public byte[] getBytes() {
        byte[] result = null;
        if (null != basos) {
            result = basos.toByteArray();
        }
        return result;
    }

    public char[] getChars() {
        char[] result = null;
        if (null != caw) {
            result = caw.toCharArray();
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "null";
        if (null != caw) {
            result = caw.toString();
        } else if (null != basos) {
            result = basos.toString();
        }
        return result;
    }

    public void flushContentToWrappedResponse() throws IOException {
        ServletResponse wrapped = this.getResponse();
        if (null != caw) {
            pw.flush();
            caw.writeTo(wrapped.getWriter());
            caw.reset();
        } else if (null != basos) {
            try {
                basos.writeTo(wrapped.getWriter(),
                              wrapped.getCharacterEncoding());
            } catch (IllegalStateException ise) {
                basos.writeTo(wrapped.getOutputStream());
            }           
            basos.resetByteArray();
        }

    }
    
    public void flushToWriter(Writer writer, String encoding) throws IOException {
        if (null != caw) {
            pw.flush();
            caw.writeTo(writer);
            caw.reset();
        } else if (null != basos) {
            basos.writeTo(writer, encoding);
            basos.resetByteArray();
        }
        writer.flush();
    }

    public void resetBuffers() throws IOException {
        if (null != caw) {
            caw.reset();
        } else if (null != basos) {
            basos.resetByteArray();
        }
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (pw != null && (!pw.isComitted() && !isCommitted())) {
            throw new IllegalStateException();
        } else if (pw != null && (pw.isComitted() || isCommitted())) {
            return ByteArrayWebOutputStream.NOOP_STREAM;
        }
        if (null == basos) {
            basos = new ByteArrayWebOutputStream();
        }
        return basos;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (basos != null && (!basos.isCommitted() && !isCommitted())) {
            throw new IllegalStateException();
        } else if (basos != null && (basos.isCommitted() || isCommitted())) {
            return new WebPrintWriter(WebPrintWriter.NOOP_WRITER);
        }
        if (null == pw) {
            caw = new CharArrayWriter(1024);
            pw = new WebPrintWriter(caw);
        }

        return pw;
    }


} // end of class ViewHandlerResponseWrapper
