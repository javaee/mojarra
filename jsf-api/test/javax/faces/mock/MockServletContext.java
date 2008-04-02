/*
 * $Id: MockServletContext.java,v 1.6 2005/08/22 22:08:26 ofung Exp $
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
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.mock;


import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;


// Mock Object for ServletContext (Version 2.3)
public class MockServletContext implements ServletContext {


    private Hashtable attributes = new Hashtable();
    private Hashtable parameters = new Hashtable();


    // --------------------------------------------------------- Public Methods


    public void addInitParameter(String name, String value) {
        parameters.put(name, value);
    }


    // ------------------------------------------------- ServletContext Methods


    public Object getAttribute(String name) {
        return (attributes.get(name));
    }

    public Enumeration getAttributeNames() {
        return (attributes.keys());
    }

    public ServletContext getContext(String uripath) {
        throw new UnsupportedOperationException();
    }

    public String getInitParameter(String name) {
        return ((String) parameters.get(name));
    }

    public Enumeration getInitParameterNames() {
        return (parameters.keys());
    }

    public int getMajorVersion() {
        return (2);
    }

    public String getMimeType(String path) {
        throw new UnsupportedOperationException();
    }

    public int getMinorVersion() {
        return (3);
    }

    public RequestDispatcher getNamedDispatcher(String name) {
        throw new UnsupportedOperationException();
    }

    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException();
    }

    public URL getResource(String path) throws MalformedURLException {
        throw new UnsupportedOperationException();
    }

    public InputStream getResourceAsStream(String path) {
        throw new UnsupportedOperationException();
    }

    public Set getResourcePaths(String path) {
        throw new UnsupportedOperationException();
    }

    public Servlet getServlet(String name) throws ServletException {
        throw new UnsupportedOperationException();
    }

    public String getServletContextName() {
        return ("MockServletContext");
    }

    public String getServerInfo() {
        return ("MockServletContext");
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

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }


}
