/*
 * $Id: MockServletContext.java,v 1.1 2002/12/03 01:05:01 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;


// Mock Object for ServletContext (Version 2.3)
public class MockServletContext implements ServletContext {


    public ServletContext getContext(String uripath) {
        throw new UnsupportedOperationException();
    }

    public int getMajorVersion() {
        return (2);
    }

    public int getMinorVersion() {
        return (3);
    }

    public String getMimeType(String path) {
        throw new UnsupportedOperationException();
    }

    public Set getResourcePaths(String path) {
        throw new UnsupportedOperationException();
    }

    public URL getResource(String path) throws MalformedURLException {
        throw new UnsupportedOperationException();
    }

    public InputStream getResourceAsStream(String path) {
        throw new UnsupportedOperationException();
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException();
    }

    public RequestDispatcher getNamedDispatcher(String name) {
        throw new UnsupportedOperationException();
    }

    public Servlet getServlet(String name) throws ServletException {
        throw new UnsupportedOperationException();
    }

    public Enumeration getServlets() {
        throw new UnsupportedOperationException();
    }

    public Enumeration getServletNames() {
        throw new UnsupportedOperationException();
    }

    public void log(String message) {
        throw new UnsupportedOperationException();
    }

    public void log(Exception exception, String message) {
        throw new UnsupportedOperationException();
    }

    public void log(String message, Throwable exception) {
        throw new UnsupportedOperationException();
    }

    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
    }

    public String getServerInfo() {
        throw new UnsupportedOperationException();
    }

    public String getInitParameter(String name) {
        throw new UnsupportedOperationException();
    }

    public Enumeration getInitParameterNames() {
        throw new UnsupportedOperationException();
    }

    public Object getAttribute(String name) {
        throw new UnsupportedOperationException();
    }

    public Enumeration getAttributeNames() {
        throw new UnsupportedOperationException();
    }

    public void setAttribute(String name, Object object) {
        throw new UnsupportedOperationException();
    }

    public void removeAttribute(String name) {
        throw new UnsupportedOperationException();
    }

    public String getServletContextName() {
        throw new UnsupportedOperationException();
    }



}
