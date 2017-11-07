/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id: MockHttpServletResponse.java,v 1.1 2005/10/18 17:47:57 edburns Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

package com.sun.faces.mock;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;



// Mock Object for HttpServletResponse (Version 2.3)
public class MockHttpServletResponse implements HttpServletResponse {



    private String encoding = "ISO-8859-1";
    private String contentType = "text/html";
    private int status;


    // -------------------------------------------- HttpServletResponse Methods


    public void addCookie(Cookie cookie) {
        throw new UnsupportedOperationException();
    }


    public void addDateHeader(String name, long value) {
        throw new UnsupportedOperationException();
    }


    public void addHeader(String name, String value) {
        throw new UnsupportedOperationException();
    }


    public void addIntHeader(String name, int value) {
        throw new UnsupportedOperationException();
    }


    public boolean containsHeader(String name) {
        throw new UnsupportedOperationException();
    }


    public String encodeRedirectUrl(String url) {
        return (encodeRedirectURL(url));
    }


    public String encodeRedirectURL(String url) {
        return (url);
    }


    public String encodeUrl(String url) {
        return (encodeURL(url));
    }


    public String encodeURL(String url) {
        return (url);
    }


    public void sendError(int status) {
        this.status = status;
    }


    public void sendError(int status, String message) {
        this.status = status;
    }


    public void sendRedirect(String location) {
        throw new UnsupportedOperationException();
    }


    public void setDateHeader(String name, long value) {
        throw new UnsupportedOperationException();
    }


    public void setHeader(String name, String value) {
        throw new UnsupportedOperationException();
    }


    public void setIntHeader(String name, int value) {
        throw new UnsupportedOperationException();
    }


    public void setStatus(int status) {
        this.status = status;
    }


    public void setStatus(int status, String message) {
        this.status = status;
    }


    // ------------------------------------------------ ServletResponse Methods


    public void flushBuffer() {
        throw new UnsupportedOperationException();
    }


    public int getBufferSize() {
        throw new UnsupportedOperationException();
    }


    public String getCharacterEncoding() {
        return (this.encoding);
    }

    public String getContentType() {
	return (this.contentType);
    }

    public Locale getLocale() {
        throw new UnsupportedOperationException();
    }


    public ServletOutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }
    
    public int getStatus() {
        return status;
    }


    public PrintWriter getWriter() throws IOException {
        throw new UnsupportedOperationException();
    }


    public boolean isCommitted() {
        throw new UnsupportedOperationException();
    }


    public void reset() {
        throw new UnsupportedOperationException();
    }


    public void resetBuffer() {
        throw new UnsupportedOperationException();
    }


    public void setBufferSize(int size) {
        throw new UnsupportedOperationException();
    }

    public void setCharacterEncoding(String charset) {
        this.encoding = charset;
    }

    public void setContentLength(int length) {
        throw new UnsupportedOperationException();
    }


    public void setContentType(String type) {
        contentType = type;
    }


    public void setLocale(Locale locale) {
        throw new UnsupportedOperationException();
    }

    public String getHeader(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Collection<String> getHeaders(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Collection<String> getHeaderNames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
