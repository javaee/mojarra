/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

// Mock Object for HttpServletRequest (Version 2.4)
public class MockHttpServletRequest implements HttpServletRequest {

    public MockHttpServletRequest() {
        super();
    }

    public MockHttpServletRequest(HttpSession session) {
        super();
        setHttpSession(session);
    }

    public MockHttpServletRequest(String contextPath, String servletPath,
            String pathInfo, String queryString) {
        super();
        setPathElements(contextPath, servletPath, pathInfo, queryString);
    }

    public MockHttpServletRequest(String contextPath, String servletPath,
            String pathInfo, String queryString,
            HttpSession session) {
        super();
        setPathElements(contextPath, servletPath, pathInfo, queryString);
        setHttpSession(session);
    }

    protected HashMap attributes = new HashMap();
    protected String contextPath = null;
    protected Locale locale = null;
    protected HashMap parameters = new HashMap();
    protected String pathInfo = null;
    protected Principal principal = null;
    protected String queryString = null;
    protected String servletPath = null;
    protected HttpSession session = null;
    protected String method = "GET";

    // --------------------------------------------------------- Public Methods
    public void addParameter(String name, String value) {
        String values[] = (String[]) parameters.get(name);
        if (values == null) {
            String results[] = new String[]{value};
            parameters.put(name, results);
            return;
        }
        String results[] = new String[values.length + 1];
        System.arraycopy(values, 0, results, 0, values.length);
        results[values.length] = value;
        parameters.put(name, results);
    }

    public void setHttpSession(HttpSession session) {
        this.session = session;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPathElements(String contextPath, String servletPath,
            String pathInfo, String queryString) {

        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.pathInfo = pathInfo;
        this.queryString = queryString;

    }

    public void setUserPrincipal(Principal principal) {
        this.principal = principal;
    }

    // --------------------------------------------- HttpServletRequest Methods
    @Override
    public String getAuthType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContextPath() {
        return (contextPath);
    }

    @Override
    public Cookie[] getCookies() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getDateHeader(String name) {
        return -1;
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public Enumeration getHeaderNames() {
        return Collections.enumeration(Collections.EMPTY_LIST);
    }

    @Override
    public Enumeration getHeaders(String name) {
        return Collections.enumeration(Collections.EMPTY_LIST);
    }

    @Override
    public int getIntHeader(String name) {
        return -1;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPathInfo() {
        return (pathInfo);
    }

    @Override
    public String getPathTranslated() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getQueryString() {
        return (queryString);
    }

    @Override
    public String getRemoteUser() {
        if (principal != null) {
            return (principal.getName());
        } else {
            return (null);
        }
    }

    @Override
    public String getRequestedSessionId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRequestURI() {
        StringBuffer sb = new StringBuffer();
        if (contextPath != null) {
            sb.append(contextPath);
        }
        if (servletPath != null) {
            sb.append(servletPath);
        }
        if (pathInfo != null) {
            sb.append(pathInfo);
        }
        if (sb.length() > 0) {
            return (sb.toString());
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public StringBuffer getRequestURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getServletPath() {
        return (servletPath);
    }

    @Override
    public HttpSession getSession() {
        return (getSession(true));
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (create && (session == null)) {
            throw new UnsupportedOperationException();
        }
        return (session);
    }

    @Override
    public Principal getUserPrincipal() {
        return (principal);
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isUserInRole(String role) {
        if ((principal != null) && (principal instanceof MockPrincipal)) {
            return (((MockPrincipal) principal).isUserInRole(role));
        } else {
            return (false);
        }
    }

    // ------------------------------------------------- ServletRequest Methods
    @Override
    public Object getAttribute(String name) {
        return (attributes.get(name));
    }

    @Override
    public Enumeration getAttributeNames() {
        return (new MockEnumeration(attributes.keySet().iterator()));
    }

    @Override
    public String getCharacterEncoding() {
        return "ISO-8859-1";
    }

    @Override
    public int getContentLength() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public ServletInputStream getInputStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale getLocale() {
        return (locale);
    }

    //
    // Servlet 2.4 methods
    // 
    @Override
    public int getRemotePort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLocalName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLocalAddr() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLocalPort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Enumeration getLocales() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getParameter(String name) {
        String values[] = (String[]) parameters.get(name);
        if (values != null) {
            return (values[0]);
        } else {
            return (null);
        }
    }

    @Override
    public Map getParameterMap() {
        return (parameters);
    }

    @Override
    public Enumeration getParameterNames() {
        return (new MockEnumeration(parameters.keySet().iterator()));
    }

    @Override
    public String[] getParameterValues(String name) {
        return ((String[]) parameters.get(name));
    }

    @Override
    public String getProtocol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BufferedReader getReader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRemoteAddr() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRemoteHost() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getScheme() {
        return ("http");
    }

    @Override
    public String getServerName() {
        return ("localhost");
    }

    @Override
    public int getServerPort() {
        return (8080);
    }

    @Override
    public boolean isSecure() {
        return (false);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (value == null) {
            attributes.remove(name);
        } else {
            attributes.put(name, value);
        }
    }

    @Override
    public void setCharacterEncoding(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean authenticate(HttpServletResponse hsr) throws IOException, ServletException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void login(String string, String string1) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void logout() throws ServletException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Part getPart(String string) throws IOException, ServletException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public ServletContext getServletContext() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public AsyncContext startAsync(ServletRequest sr, ServletResponse sr1) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean isAsyncStarted() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean isAsyncSupported() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public AsyncContext getAsyncContext() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public DispatcherType getDispatcherType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
