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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;



public class MockExternalContext extends ExternalContext {


    public MockExternalContext(ServletContext context,
                               ServletRequest request,
                               ServletResponse response) {
        this.context = context;
        this.request = request;
        this.response = response;
    }
    

    private ServletContext context = null;
    private ServletRequest request = null;
    private ServletResponse response = null;
    private Map<String,String> initParams;


    public Object getSession(boolean create) {
        throw new UnsupportedOperationException();
    }
    

    public Object getContext() {
        return (context);
    }

    public String getContextName() { 
	return context.getServletContextName();
    }

    
    
    public Object getRequest() {
        return (request);
    }

    public void setRequest(Object request) {
	throw new UnsupportedOperationException();
    }

    public Object getResponse() {
        return (response);
    }

    public void setResponse(Object response) {
	throw new UnsupportedOperationException();
    }

    public void setResponseCharacterEncoding(String encoding) {
	throw new UnsupportedOperationException();
    }

    private Map applicationMap = null;
    public Map getApplicationMap() {
        if (applicationMap == null) {
            applicationMap = new MockApplicationMap(context);
        }
        return (applicationMap);
    }
    

    private Map sessionMap = null;
    public Map getSessionMap() {
        if (sessionMap == null) {
            sessionMap = new MockSessionMap
                (((HttpServletRequest) request).getSession(true));
        }
        return (sessionMap);
    }
    

    private Map requestMap = null;
    public Map getRequestMap() {
        if (requestMap == null) {
            requestMap = new MockRequestMap(request);
        }
        return (requestMap);
    }
    

    private Map requestParameterMap = null;
    public Map getRequestParameterMap() {
        if (requestParameterMap != null) {
            return (requestParameterMap);
        } else {
            throw new UnsupportedOperationException();
        }
    }
    public void setRequestParameterMap(Map requestParameterMap) {
        this.requestParameterMap = requestParameterMap;
    }

    public void setRequestCharacterEncoding(String encoding) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException();
    }
    

    public Map getRequestParameterValuesMap() {
        throw new UnsupportedOperationException();        
    }

    
    public Iterator getRequestParameterNames() {
        throw new UnsupportedOperationException();
    }

    
    public Map getRequestHeaderMap() {
        throw new UnsupportedOperationException();
    }


    public Map getRequestHeaderValuesMap() {
        throw new UnsupportedOperationException();
    }


    public Map getRequestCookieMap() {
        throw new UnsupportedOperationException();
    }


    public Locale getRequestLocale() {
        return (request.getLocale());
    }
    

    public Iterator getRequestLocales() {
        return (new LocalesIterator(request.getLocales()));
    }
    

    public String getRequestPathInfo() {
        throw new UnsupportedOperationException();
    }


    public String getRequestContextPath() {
        throw new UnsupportedOperationException();
    }

    public String getRequestServletPath() {
        throw new UnsupportedOperationException();
    }
    
    public String getRequestCharacterEncoding() {
        throw new UnsupportedOperationException();
    }

    
    public String getRequestContentType() {
        throw new UnsupportedOperationException();
    }

    public int getRequestContentLength() {
        throw new UnsupportedOperationException();
    }


    public String getResponseCharacterEncoding() {
        throw new UnsupportedOperationException();
    }
    
    public String getResponseContentType() {
        throw new UnsupportedOperationException();
    }


    public String getInitParameter(String name) {
        if (name
              .equals(javax.faces.application.StateManager.STATE_SAVING_METHOD_PARAM_NAME)) {
            return null;
        }
        if (name.equals(javax.faces.webapp.FacesServlet.LIFECYCLE_ID_ATTR)) {
            return null;
        }
        return ((initParams == null) ? null : initParams.get(name));
    }

    public void addInitParameter(String name, String value) {
        if (initParams == null) {
            initParams = new HashMap<String,String>();
        }
        initParams.put(name, value);
    }

    @Override
    public void addResponseHeader(String arg0, String arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setResponseHeader(String arg0, String arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSessionMaxInactiveInterval(int interval) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
    @Override
    public boolean isSecure() {
	throw new UnsupportedOperationException("Not supported yet.");
    }    

    public Map getInitParameterMap() {
        throw new UnsupportedOperationException();
    }


    public Set getResourcePaths(String path) {
       return context.getResourcePaths(path);
    }


    public URL getResource(String path) throws MalformedURLException {
        throw new UnsupportedOperationException();
    }


    public InputStream getResourceAsStream(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSessionMaxInactiveInterval() {
        throw new UnsupportedOperationException();
    }

    public String encodeActionURL(String sb) {
        throw new UnsupportedOperationException();
    }


    public String encodeResourceURL(String sb) {
        throw new UnsupportedOperationException();
    }


    public String encodeNamespace(String aValue) {
        throw new UnsupportedOperationException();
    }


    public void dispatch(String requestURI)
        throws IOException, FacesException {
        throw new UnsupportedOperationException();
    }

    
    public void redirect(String requestURI)
        throws IOException {
        throw new UnsupportedOperationException();
    }

    
    public void log(String message) {
        context.log(message);
    }


    public void log(String message, Throwable throwable) {
        context.log(message, throwable);
    }


    public String getAuthType() {
        return (((HttpServletRequest) request).getAuthType());
    }

    public String getRemoteUser() {
        return (((HttpServletRequest) request).getRemoteUser());
    }



    public java.security.Principal getUserPrincipal() {
        return (((HttpServletRequest) request).getUserPrincipal());
    }

    public boolean isUserInRole(String role) {
        return (((HttpServletRequest) request).isUserInRole(role));
    }


    private class LocalesIterator implements Iterator {

	public LocalesIterator(Enumeration locales) {
	    this.locales = locales;
	}

	private Enumeration locales;

	public boolean hasNext() { return locales.hasMoreElements(); }

	public Object next() { return locales.nextElement(); }

	public void remove() { throw new UnsupportedOperationException(); }

    }


}
