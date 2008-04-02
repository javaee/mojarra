/*
 * $Id: StoreServletContext.java,v 1.8 2005/10/19 19:51:30 edburns Exp $
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


package com.sun.faces.config;

import java.io.UnsupportedEncodingException;
import javax.servlet.ServletContext;
import javax.faces.context.ExternalContext;

import java.util.Map;
import java.util.Set;
import java.util.Locale;
import java.util.Iterator;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import com.sun.faces.RIConstants;
import com.sun.faces.cactus.TestingUtil;

/**
 * <p>The purpose of this class is to call the package private
 * getThreadLocalServletContext() method to set the ServletContext into
 * ThreadLocalStorage, if IS_UNIT_TEST_MODE == true.</p>
 */

public class StoreServletContext extends Object {
    ExternalContext ec = null;

    public void setServletContext(ServletContext sc) {
        ec = new ServletContextAdapter(sc);

        ThreadLocal<ServletContextAdapter> threadLocal =
            (ThreadLocal<ServletContextAdapter>)
                TestingUtil.invokePrivateMethod("getThreadLocalExternalContext",
                                                RIConstants.EMPTY_CLASS_ARGS,
                                                RIConstants.EMPTY_METH_ARGS,
                                                ConfigureListener.class,
                                                null);
        threadLocal.set(new ServletContextAdapter(sc));
    }

    public ExternalContext getServletContextWrapper() {
        return ec;
    }

    public class ServletContextAdapter extends ExternalContext {

        private ServletContext servletContext = null;
        private ApplicationMap applicationMap = null;

        public ServletContextAdapter(ServletContext sc) {
            this.servletContext = sc;
        }

        public void dispatch(String path) throws java.io.IOException {
        }

        public String encodeActionURL(String url) {
            return null;
        }

        public String encodeNamespace(String name) {
            return null;
        }


        public String encodeResourceURL(String url) {
            return null;
        }

       public Map getApplicationMap() {
            if (applicationMap == null) {
                applicationMap = new ApplicationMap(servletContext);
            }
            return applicationMap;
        }

        public String getAuthType() {
            return null;
        }

        public Object getContext() {
            return servletContext;
        }

        public String getInitParameter(String name) {
            return null;
        }

        public Map getInitParameterMap() {
            return null;
        }

        public String getRemoteUser() {
            return null;
        }


        public Object getRequest() {
            return null;
        }

    public void setRequest(Object request) {}


        public String getRequestCharacterEncoding() {
            return null;
        }

        public void setRequestCharacterEncoding(String requestCharacterEncoding) throws UnsupportedEncodingException {

        }

        public String getResponseCharacterEncoding() {
            return null;
        }

        public void setResponseCharacterEncoding(String responseCharacterEncoding) {
        }

        public String getRequestContextPath() {
            return null;
        }

        public Map getRequestCookieMap() {
            return null;
        }

        public Map getRequestHeaderMap() {
            return null;
        }


        public Map getRequestHeaderValuesMap() {
            return null;
        }


        public Locale getRequestLocale() {
            return null;
        }

        public Iterator getRequestLocales() {
            return null;
        }



        public Map getRequestMap() {
            return null;
        }


        public Map getRequestParameterMap() {
            return null;
        }


        public Iterator getRequestParameterNames() {
            return null;
        }


        public Map getRequestParameterValuesMap() {
            return null;
        }


        public String getRequestPathInfo() {
            return null;
        }


        public String getRequestServletPath() {
            return null;
        }


        public String getRequestContentType() {
            return null;
        }

        public String getResponseContentType() {
            return null;
        }

        public java.net.URL getResource(String path) throws
                                                     java.net.MalformedURLException {
            return null;
        }


        public java.io.InputStream getResourceAsStream(String path) {
            return null;
        }

        public Set getResourcePaths(String path) {
            return null;
        }

        public Object getResponse() {
            return null;
        }

    public void setResponse(Object response) {}

        public Object getSession(boolean create) {
            return null;
        }

        public Map getSessionMap() {
            return null;
        }

        public java.security.Principal getUserPrincipal() {
            return null;
        }

        public boolean isUserInRole(String role) {
            return false;
        }

        public void log(String message) {
        }

        public void log(String message, Throwable exception){
        }

        public void redirect(String url) throws java.io.IOException {
        }

    }

    class ApplicationMap extends java.util.AbstractMap {

        private ServletContext servletContext = null;

        ApplicationMap(ServletContext servletContext) {
            this.servletContext = servletContext;
        }


        public Object get(Object key) {
            if (key == null) {
                throw new NullPointerException();
            }
            return servletContext.getAttribute(key.toString());
        }


        public Object put(Object key, Object value) {
            if (key == null) {
                throw new NullPointerException();
            }
            String keyString = key.toString();
            Object result = servletContext.getAttribute(keyString);
            servletContext.setAttribute(keyString, value);
            return (result);
        }


        public Object remove(Object key) {
            if (key == null) {
                return null;
            }
            String keyString = key.toString();
            Object result = servletContext.getAttribute(keyString);
            servletContext.removeAttribute(keyString);
            return (result);
        }


        public Set entrySet() {
           throw new UnsupportedOperationException();
        }


        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof ApplicationMap))
                return false;
            return super.equals(obj);
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map t) {
            throw new UnsupportedOperationException();
        }


    } // END ApplicationMap
}

