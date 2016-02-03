/*
 * $Id: MockHttpServletResponse.java,v 1.6.38.2 2007/04/27 21:27:01 ofung Exp $
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

package javax.faces.mock;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;



// Mock Object for HttpServletResponse (Version 2.3)
public class MockHttpServletResponse implements HttpServletResponse {



    private String encoding = "ISO-8859-1";
    private String contentType = "text/html";


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
        throw new UnsupportedOperationException();
    }


    public void sendError(int status, String message) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }


    public void setStatus(int status, String message) {
        throw new UnsupportedOperationException();
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


}
