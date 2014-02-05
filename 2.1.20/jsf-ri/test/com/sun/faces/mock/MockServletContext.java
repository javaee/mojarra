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

package com.sun.faces.mock;


import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.FilterRegistration;
import javax.servlet.Filter;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import java.util.Collections;
import java.util.Map;
import java.util.EventListener;

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
        return (5);
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
        return Collections.emptySet();
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


    public int getEffectiveMajorVersion() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getEffectiveMinorVersion() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean setInitParameter(String s, String s1) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ServletRegistration.Dynamic addServlet(String s, String s1) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ServletRegistration.Dynamic addServlet(String s, Servlet servlet) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ServletRegistration.Dynamic addServlet(String s, Class<? extends Servlet> aClass) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public <T extends Servlet> T createServlet(Class<T> tClass)
          throws ServletException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ServletRegistration getServletRegistration(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public FilterRegistration.Dynamic addFilter(String s, String s1) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public FilterRegistration.Dynamic addFilter(String s, Filter filter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public FilterRegistration.Dynamic addFilter(String s, Class<? extends Filter> aClass) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public <T extends Filter> T createFilter(Class<T> tClass)
          throws ServletException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public FilterRegistration getFilterRegistration(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public SessionCookieConfig getSessionCookieConfig() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addListener(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public <T extends EventListener> void addListener(T t) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addListener(Class<? extends EventListener> aClass) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public <T extends EventListener> T createListener(Class<T> tClass)
          throws ServletException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public JspConfigDescriptor getJspConfigDescriptor() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ClassLoader getClassLoader() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void declareRoles(String... strings) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
