/*
 * $Id: MockHttpServletRequest.java,v 1.1 2002/12/03 01:05:01 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


// Mock Object for HttpServletRequest (Version 2.3)
public class MockHttpServletRequest implements HttpServletRequest {


    private Hashtable attributes = new Hashtable();

    public Object getAttribute(String name) {
        return (attributes.get(name));
    }

    public Enumeration getAttributeNames() {
        return (attributes.keys());
    }

    public String getCharacterEncoding() {
        throw new UnsupportedOperationException();
    }

    public void setCharacterEncoding(String encoding) throws java.io.UnsupportedEncodingException {
        throw new UnsupportedOperationException();
    }

    public int getContentLength() {
        throw new UnsupportedOperationException();
    }

    public String getContentType() {
        throw new UnsupportedOperationException();
    }

    public ServletInputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    private Hashtable parameters = new Hashtable();

    public String getParameter(String name) {
        Object value = parameters.get(name);
        if (value == null) {
            return (null);
        } else if (value instanceof String[]) {
            return ( ((String[]) value)[0] );
        } else {
            return ((String) value);
        }
    }

    public Enumeration getParameterNames() {
        return (parameters.keys());
    }

    public String[] getParameterValues(String name) {
        Object value = parameters.get(name);
        if (value == null) {
            return (null);
        } else if (value instanceof String) {
            String values[] = new String[1];
            values[0] = (String) value;
            return (values);
        } else {
            return ((String[]) value);
        }
    }

    public Map getParameterMap() {
        return ((Map) parameters);
    }

    // Mock object setter
    public void setParameter(String name, String value) {
        parameters.put(name, value);
    }

    // Mock object setter
    public void setParameters(String name, String values[]) {
        parameters.put(name, values);
    }

    public String getProtocol() {
        throw new UnsupportedOperationException();
    }

    public String getScheme() {
        throw new UnsupportedOperationException();
    }

    public String getServerName() {
        throw new UnsupportedOperationException();
    }

    public int getServerPort() {
        throw new UnsupportedOperationException();
    }

    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException();
    }

    public String getRemoteAddr() {
        throw new UnsupportedOperationException();
    }

    public String getRemoteHost() {
        throw new UnsupportedOperationException();
    }

    public int getRemotePort() {
        throw new UnsupportedOperationException();
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public Locale getLocale() {
        throw new UnsupportedOperationException();
    }

    public Enumeration getLocales() {
        throw new UnsupportedOperationException();
    }

    public boolean isSecure() {
        throw new UnsupportedOperationException();
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException();
    }

    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
    }

    public String getAuthType() {
        throw new UnsupportedOperationException();
    }

    public Cookie[] getCookies() {
        throw new UnsupportedOperationException();
    }

    public long getDateHeader(String name) {
        throw new UnsupportedOperationException();
    }

    public String getHeader(String name) {
        throw new UnsupportedOperationException();
    }

    public Enumeration getHeaders(String name) {
        throw new UnsupportedOperationException();
    }

    public Enumeration getHeaderNames() {
        throw new UnsupportedOperationException();
    }

    public int getIntHeader(String name) {
        throw new UnsupportedOperationException();
    }

    public String getMethod() {
        throw new UnsupportedOperationException();
    }

    public String getPathInfo() {
        throw new UnsupportedOperationException();
    }

    public String getPathTranslated() {
        throw new UnsupportedOperationException();
    }

    public String getContextPath() {
        throw new UnsupportedOperationException();
    }

    public String getQueryString() {
        throw new UnsupportedOperationException();
    }

    public String getRemoteUser() {
        throw new UnsupportedOperationException();
    }

    public boolean isUserInRole(String role) {
        throw new UnsupportedOperationException();
    }

    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException();
    }

    public String getRequestedSessionId() {
        throw new UnsupportedOperationException();
    }

    public String getRequestURI() {
        throw new UnsupportedOperationException();
    }

    public StringBuffer getRequestURL() {
        throw new UnsupportedOperationException();
    }

    public String getServletPath() {
        throw new UnsupportedOperationException();
    }

    public HttpSession getSession(boolean create) {
        throw new UnsupportedOperationException();
    }

    public HttpSession getSession() {
        return (getSession(true));
    }

    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException();
    }

    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException();
    }

    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException();
    }

    public boolean isRequestedSessionIdFromUrl() {
        return (isRequestedSessionIdFromURL());
    }

}
