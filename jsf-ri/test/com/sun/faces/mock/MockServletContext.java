/*
 * $Id: MockServletContext.java,v 1.7 2006/02/15 22:33:27 rlubke Exp $
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

package com.sun.faces.mock;


import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;


// Mock Object for ServletContext (Version 2.3)

public class MockServletContext implements ServletContext {


    // ------------------------------------------------------------ Constructors


    // Zero-args constructor for no associated directory
    public MockServletContext() {
        ;
    }


    // Constructor with File object for associated directory
    public MockServletContext(File directory) {
        setDirectory(directory);
    }


    // ------------------------------------------------------ Instance Variables


    private Hashtable attributes = new Hashtable();
    private Hashtable parameters = new Hashtable();
    private String name = "MockServletContext";


    // -------------------------------------------------------------- Properties


    // The directory that is the base of our application resources
    private File directory = null;


    public File getDirectory() {
        return (this.directory);
    }


    public void setDirectory(File directory) {
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException
                (directory.getAbsolutePath() +
                 " is not an existing directory");
        }
        this.directory = directory;
    }


    // ---------------------------------------------------------- Public Methods


    // Add a context innitialization parameter
    public void addInitParameter(String name, String value) {
        parameters.put(name, value);
    }


    // Set the servlet context name
    public void setServletContextName(String name) {
        this.name = name;
    }


    // -------------------------------------------------- ServletContext Methods


    public Object getAttribute(String name) {
        return (attributes.get(name));
    }


    public Enumeration getAttributeNames() {
        return (attributes.keys());
    }


    public ServletContext getContext(String uripath) {
        throw new UnsupportedOperationException();
    }

    public String getContextPath() {
       return ('/' + name);
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
        if (!path.startsWith("/") || (directory == null)) {
            return (null);
        }
        File file = new File(directory, path.substring(1));
        if (!file.exists() || !file.isFile()) {
            return (null);
        }
        return (file.getAbsolutePath());
    }


    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException();
    }


    public URL getResource(String path) throws MalformedURLException {
        if (!path.startsWith("/") || (directory == null)) {
            return (null);
        }
        File file = new File(directory, path.substring(1));
        if (!file.exists() || !file.isFile()) {
            return (null);
        }
        return (file.toURL());
    }


    public InputStream getResourceAsStream(String path) {
        URL url = null;
        try {
            url = getResource(path);
        } catch (MalformedURLException e) {
            return (null);
        }
        if (url == null) {
            return (null);
        }
        try {
            return (url.openStream());
        } catch (IOException e) {
            return (null);
        }
    }


    public Set getResourcePaths(String path) {
        throw new UnsupportedOperationException();
        // PENDING(craigmcc) - Flesh out the following implementation
        /*
        if (!path.startsWith("/") || (directory == null)) {
            return (null);
        }
        File base = new File(directory, path.substring(1));
        if (!base.exists() || !base.isDirectory()) {
            return (false);
        }
        Set results = new HashSet();
        // PENDING(craigmcc) recursive descent search
        return (results);
        */
    }


    public Servlet getServlet(String name) throws ServletException {
        throw new UnsupportedOperationException();
    }


    public String getServletContextName() {
        return (name);
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
