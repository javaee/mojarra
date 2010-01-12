/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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

        public String encodePartialActionURL(String viewId) {
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

        public String getMimeType(String file) {
            return servletContext.getMimeType(file);
        }

        public Object getContext() {
            return servletContext;
        }

	public String getContextName() {
	    return servletContext.getServletContextName();
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

        public void setResponseHeader(String name, String value) {
        }

        public void addResponseHeader(String name, String value) {
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

	public int getRequestContentLength() {
	    return -1;
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

