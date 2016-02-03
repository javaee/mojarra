/*
 * $Id: ExternalContextImpl.java,v 1.26.34.4 2007/04/27 21:27:39 ofung Exp $
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

package com.sun.faces.context;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>This implementation of {@link ExternalContext} is specific to the
 * servlet implementation.
 *
 * @author Brendan Murray
 * @version $Id: ExternalContextImpl.java,v 1.26.34.4 2007/04/27 21:27:39 ofung Exp $
 */
public class ExternalContextImpl extends ExternalContext {

    private ServletContext servletContext = null;
    private ServletRequest request = null;
    private ServletResponse response = null;

    private ApplicationMap applicationMap = null;
    private SessionMap sessionMap = null;
    private RequestMap requestMap = null;
    private RequestParameterMap requestParameterMap = null;
    private RequestParameterValuesMap requestParameterValuesMap = null;
    private RequestHeaderMap requestHeaderMap = null;
    private RequestHeaderValuesMap requestHeaderValuesMap = null;
    private RequestCookieMap cookieMap = null;
    private InitParameterMap initParameterMap = null;


    public ExternalContextImpl(ServletContext sc, ServletRequest request,
                               ServletResponse response) {

        // Validate the incoming parameters
        try {
            Util.parameterNonNull(sc);
            Util.parameterNonNull(request);
            Util.parameterNonNull(response);
        } catch (Exception e) {
            throw new FacesException(
                Util.getExceptionMessageString(
                    Util.FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID));
        }

        // Save references to our context, request, and response
        this.servletContext = sc;
        // PENDING(edburns): Craig's workaround breaks
        // TestValidatorTags.java because Cactus expects a certain type
        // to be present for the value of the request.
        if (RIConstants.IS_UNIT_TEST_MODE) {
            this.request = request;
        } else {
            // PENDING(craigmcc) - Work around a Tomcat 4.1 and 5.0 bug
            // where the request wrapper used on a
            // RequestDispatcher.forward() call delegates
            // removeAttribute() and setAttribute() to the wrapped
            // request, but not getAttribute().  This causes attributes
            // set via the RequestMap returned in this class to not be
            // visible via calls to getAttribute() on the underlying
            // request.
            if (request instanceof HttpServletRequest) {
                this.request = new MyHttpServletRequestWrapper
                    ((HttpServletRequest) request);
            } else {
                this.request = new MyServletRequestWrapper(request);
            }
        }
        this.response = response;

    }


    public Object getSession(boolean create) {
        return (((HttpServletRequest) request).getSession(create));
    }


    public Object getContext() {
        return this.servletContext;
    }


    public Object getRequest() {
        return this.request;
    }


    public Object getResponse() {
        return this.response;
    }


    public Map getApplicationMap() {
        if (applicationMap == null) {
            applicationMap = new ApplicationMap(servletContext);
        }
        return applicationMap;
    }


    public Map getSessionMap() {
        if (sessionMap == null) {
            sessionMap = new SessionMap((HttpServletRequest) request);
        }
        return sessionMap;
    }


    public Map getRequestMap() {
        if (requestMap == null) {
            requestMap = new RequestMap(this.request);
        }
        return requestMap;
    }


    public Map getRequestHeaderMap() {
        if (null == requestHeaderMap) {
            requestHeaderMap =
                new RequestHeaderMap((HttpServletRequest) request);
        }
        return requestHeaderMap;
    }


    public Map getRequestHeaderValuesMap() {
        if (null == requestHeaderValuesMap) {
            requestHeaderValuesMap =
                new RequestHeaderValuesMap((HttpServletRequest) request);
        }
        return requestHeaderValuesMap;
    }


    public Map getRequestCookieMap() {
        if (null == cookieMap) {
            cookieMap = new RequestCookieMap((HttpServletRequest) request);
        }
        return cookieMap;
    }


    public Map getInitParameterMap() {
        if (null == initParameterMap) {
            initParameterMap = new InitParameterMap(servletContext);
        }
        return initParameterMap;
    }


    public Map getRequestParameterMap() {
        if (null == requestParameterMap) {
            requestParameterMap = new RequestParameterMap(request);
        }
        return requestParameterMap;
    }


    public Map getRequestParameterValuesMap() {
        if (null == requestParameterValuesMap) {
            requestParameterValuesMap = new RequestParameterValuesMap(request);
        }
        return requestParameterValuesMap;
    }


    public Iterator getRequestParameterNames() {
        final Enumeration namEnum = request.getParameterNames();

        Iterator result = new Iterator() {
            public boolean hasNext() {
                return namEnum.hasMoreElements();
            }


            public Object next() {
                return namEnum.nextElement();
            }


            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

        return result;
    }


    public Locale getRequestLocale() {
        return request.getLocale();
    }


    public Iterator getRequestLocales() {
        return (new LocalesIterator(request.getLocales()));
    }


    public String getRequestPathInfo() {
        return (((HttpServletRequest) request).getPathInfo());
    }


    public Cookie[] getRequestCookies() {
        return (((HttpServletRequest) request).getCookies());
    }


    public String getRequestContextPath() {
        return (((HttpServletRequest) request).getContextPath());
    }


    public String getRequestServletPath() {
        return (((HttpServletRequest) request).getServletPath());
    }


    /**
     * <p>Manage attributes associated with the <code>ServletContext</code>
     * instance associated with the current request.</p>
     */
    public String getInitParameter(String name) {
        return servletContext.getInitParameter(name);
    }


    public Set getResourcePaths(String path) {
        return servletContext.getResourcePaths(path);
    }


    public InputStream getResourceAsStream(String path) {
        return servletContext.getResourceAsStream(path);
    }


    public URL getResource(String path) {
        URL url = null;
        try {
            url = servletContext.getResource(path);
        } catch (MalformedURLException e) {
            return null;
        }
        return url;
    }


    /**
     * <p>Force any URL that causes an action to work within a portal/portlet.
     * This causes the URL to have the required redirection for the specific
     * portal to be included</p>
     *
     * @param sb The input URL to be reformatted
     */
    public String encodeActionURL(String sb) {
        return ((HttpServletResponse) response).encodeURL(sb);
    }


    /**
     * <p>Force any URL that references a resource to work within a
     * portal/portlet. This causes the URL to have the required
     * redirection for the specific portal to be included. In reality,
     * it simply returns an absolute URL.</p>
     *
     * @param sb The input URL to be reformatted
     */
    public String encodeResourceURL(String sb) {
        return ((HttpServletResponse) response).encodeURL(sb);
    }


    public String encodeNamespace(String aValue) {
        return aValue; // Do nothing for servlets
    }


    public String encodeURL(String url) {
        return ((HttpServletResponse) response).encodeURL(url);
    };

    public void dispatch(String requestURI) throws IOException, FacesException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(
            requestURI);
        if (requestDispatcher == null) {
            ((HttpServletResponse) response).sendError(
                  HttpServletResponse.SC_NOT_FOUND);
        }
        try {
            requestDispatcher.forward(this.request, this.response);
        } catch (IOException ioe) {
            // e.printStackTrace();
            throw ioe;
        } catch (ServletException se) {
            throw new FacesException(se);
        }
    }


    public void redirect(String requestURI) throws IOException {
        ((HttpServletResponse) response).sendRedirect(requestURI);
        FacesContext.getCurrentInstance().responseComplete();
    }


    public void log(String message) {
        servletContext.log(message);
    }


    public void log(String message, Throwable throwable) {
        servletContext.log(message, throwable);
    }


    public String getAuthType() {
        return ((HttpServletRequest) request).getAuthType();
    }


    public String getRemoteUser() {
        return ((HttpServletRequest) request).getRemoteUser();
    }


    public java.security.Principal getUserPrincipal() {
        return ((HttpServletRequest) request).getUserPrincipal();
    }


    public boolean isUserInRole(String role) {
        return ((HttpServletRequest) request).isUserInRole(role);
    }


    private class LocalesIterator implements Iterator {

        public LocalesIterator(Enumeration locales) {
            this.locales = locales;
        }


        private Enumeration locales;


        public boolean hasNext() {
            return locales.hasMoreElements();
        }


        public Object next() {
            return locales.nextElement();
        }


        public void remove() {
            throw new UnsupportedOperationException();
        }

    }


}

abstract class BaseContextMap extends AbstractMap {

    // Unsupported by all Maps.
    public void clear() {
        throw new UnsupportedOperationException();
    }


    // Unsupported by all Maps.
    public void putAll(Map t) {
        throw new UnsupportedOperationException();
    }


    // Supported by maps if overridden
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }


    static class Entry implements Map.Entry {

        // immutable Entry
        private final Object key;
        private final Object value;


        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }


        public Object getKey() {
            return key;
        }


        public Object getValue() {
            return value;
        }


        // No support of setting the value
        public Object setValue(Object value) {
            throw new UnsupportedOperationException();
        }


        public int hashCode() {
            return ((key == null ? 0 : key.hashCode()) ^
                (value == null ? 0 : value.hashCode()));
        }


        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Map.Entry)) {
                return false;
            }

            Map.Entry input = (Map.Entry) obj;
            Object inputKey = input.getKey();
            Object inputValue = input.getValue();

            if (inputKey == key ||
                (inputKey != null && inputKey.equals(key))) {
                if (inputValue == value ||
                    (inputValue != null && inputValue.equals(value))) {
                    return true;
                }
            }
            return false;
        }
    }
}

abstract class StringArrayValuesMap extends BaseContextMap {
    
    public boolean equals(Object obj) {
        
        if (obj == null || 
            !(obj.getClass().isInstance(obj))) {
            return false;
        }
        Map objMap = (Map) obj;
        
        if (this.size() != objMap.size()) {
            return false;
        }
        String[] thisKeys = (String[])
              keySet().toArray(new String[this.size()]);
        String[] objKeys = (String[]) 
              objMap.keySet().toArray(new String[objMap.size()]);
        
        Arrays.sort(thisKeys);
        Arrays.sort(objKeys);
        
        if (!(Arrays.equals(thisKeys, objKeys))) {
            return false;
        } else {
            for (int i = 0; i < thisKeys.length; i++) { 
                String key = thisKeys[i];
                Object[] thisVal = (Object[]) this.get(key);
                Object[] objVal = (Object[]) objMap.get(key);
                if (!(Arrays.equals(thisVal, objVal))) {
                    return false;
                }
            }
        }
        
        return true;        
        
    }
    
    protected int hashCode(Object someObject) {        
        int hashCode = 7 * someObject.hashCode();
        for (Iterator it = entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();           
            hashCode += entry.getKey().hashCode();
            Object[] values = (Object[]) entry.getValue();
            for (int i = 0; i < values.length; i++) {
                hashCode += values[i].hashCode();
            }            
        }
        return hashCode;
    }

    public boolean containsValue(Object value) {
                
        if (value == null || !value.getClass().isArray()) {
            return false;
        }
               
        for (Iterator it = entrySet().iterator(); it.hasNext();) {            
            Map.Entry entry = (Map.Entry) it.next();
            // values will be arrays
            if (Arrays.equals((Object[]) value, (Object[]) entry.getValue())) {
                return true;
            }
        }
        return false;
    }
}


class ApplicationMap extends BaseContextMap {

    private final ServletContext servletContext;


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
        Set entries = new HashSet();
        for (Enumeration e = servletContext.getAttributeNames();
             e.hasMoreElements();) {
            String key = (String) e.nextElement();
            entries.add(new Entry(key, servletContext.getAttribute(key)));
        }
        return entries;
    }


    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ApplicationMap)) {
            return false;
        }
        return super.equals(obj);
    }

} // END ApplicationMap

class SessionMap extends BaseContextMap {

    private final HttpServletRequest request;


    SessionMap(HttpServletRequest request) {
        this.request = request;
    }


    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return getSession().getAttribute(key.toString());
    }


    public Object put(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException();
        }
        String keyString = key.toString();
        HttpSession session = getSession();
        Object result = session.getAttribute(keyString);
        session.setAttribute(keyString, value);
        return (result);
    }


    public Object remove(Object key) {
        if (key == null) {
            return null;
        }
        String keyString = key.toString();
        HttpSession session = getSession();
        Object result = session.getAttribute(keyString);
        session.removeAttribute(keyString);
        return (result);
    }


    public Set entrySet() {
        Set entries = new HashSet();
        HttpSession session = getSession();
        for (Enumeration e = session.getAttributeNames();
             e.hasMoreElements();) {
            String key = (String) e.nextElement();
            entries.add(new Entry(key, session.getAttribute(key)));
        }
        return entries;
    }


    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SessionMap)) {
            return false;
        }
        return super.equals(obj);
    }


    private HttpSession getSession() {
        return request.getSession(true);
    }

} // END SessionMap

class RequestMap extends BaseContextMap {

    private final ServletRequest request;


    RequestMap(ServletRequest request) {
        this.request = request;
    }


    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return request.getAttribute(key.toString());
    }


    public Object put(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException();
        }
        String keyString = key.toString();
        Object result = request.getAttribute(keyString);
        request.setAttribute(keyString, value);
        return (result);
    }


    public Object remove(Object key) {
        if (key == null) {
            return null;
        }
        String keyString = key.toString();
        Object result = request.getAttribute(keyString);
        request.removeAttribute(keyString);
        return (result);
    }


    public Set entrySet() {
        Set entries = new HashSet();
        for (Enumeration e = request.getAttributeNames();
             e.hasMoreElements();) {
            String key = (String) e.nextElement();
            entries.add(new Entry(key, request.getAttribute(key)));
        }
        return entries;
    }


    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RequestMap)) {
            return false;
        }
        return super.equals(obj);
    }
} // END RequestMap

class RequestParameterMap extends BaseContextMap {

    private final ServletRequest request;


    RequestParameterMap(ServletRequest request) {
        this.request = request;
    }


    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (key == RIConstants.IMMUTABLE_MARKER) {
            return RIConstants.IMMUTABLE_MARKER;
        }
        return request.getParameter(key.toString());
    }


    public Set entrySet() {
        Set entries = new HashSet();
        for (Enumeration e = request.getParameterNames();
             e.hasMoreElements();) {
            String paramName = (String) e.nextElement();
            entries.add(new Entry(paramName, request.getParameter(paramName)));
        }
        return entries;
    }


    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RequestParameterMap)) {
            return false;
        }
        return super.equals(obj);
    }
} // END RequestParameterMap

class RequestParameterValuesMap extends StringArrayValuesMap {

    private final ServletRequest request;


    RequestParameterValuesMap(ServletRequest request) {
        this.request = request;
    }


    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (key == RIConstants.IMMUTABLE_MARKER) {
            return RIConstants.IMMUTABLE_MARKER;
        }
        return request.getParameterValues(key.toString());
    }


    public Set entrySet() {
        Set entries = new HashSet();
        for (Enumeration e = request.getParameterNames();
             e.hasMoreElements();) {
            String paramName = (String) e.nextElement();
            entries.add(
                new Entry(paramName, request.getParameterValues(paramName)));
        }
        return entries;
    }

    public int hashCode() {
        return hashCode(request);
    }

} // END RequestParameterValuesMap

class RequestHeaderMap extends BaseContextMap {

    private final HttpServletRequest request;


    RequestHeaderMap(HttpServletRequest request) {
        this.request = request;
    }


    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (key == RIConstants.IMMUTABLE_MARKER) {
            return RIConstants.IMMUTABLE_MARKER;
        }
        return (request.getHeader(key.toString()));
    }


    public Set entrySet() {
        Set entries = new HashSet();
        for (Enumeration e = request.getHeaderNames();
             e.hasMoreElements();) {
            String headerName = (String) e.nextElement();
            entries.add(new Entry(headerName, request.getHeader(headerName)));
        }
        return entries;
    }


    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RequestHeaderMap)) {
            return false;
        }
        return super.equals(obj);
    }
} // END RequestHeaderMap

class RequestHeaderValuesMap extends StringArrayValuesMap {

    private final HttpServletRequest request;


    RequestHeaderValuesMap(HttpServletRequest request) {
        this.request = request;
    }


    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (key == RIConstants.IMMUTABLE_MARKER) {
            return RIConstants.IMMUTABLE_MARKER;
        }

        List valuesList = new ArrayList();
        Enumeration valuesEnum = this.request.getHeaders(key.toString());
        while (valuesEnum.hasMoreElements()) {
            valuesList.add(valuesEnum.nextElement());
        } 

        return valuesList.toArray(new String[valuesList.size()]); 
    }


    public Set entrySet() {
        Set entries = new HashSet();
        for (Enumeration e = request.getHeaderNames();
             e.hasMoreElements();) {
            String headerName = (String) e.nextElement();
            entries.add(new Entry(headerName, this.get(headerName)));
        }
        return entries;
    }

    public int hashCode() {
        return hashCode(request);
    }
} // END RequestHeaderValuesMap

class RequestCookieMap extends BaseContextMap {

    private final HttpServletRequest request;


    RequestCookieMap(HttpServletRequest newRequest) {
        this.request = newRequest;
    }


    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (key == RIConstants.IMMUTABLE_MARKER) {
            return RIConstants.IMMUTABLE_MARKER;
        }

        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            return null;
        }

        String keyString = key.toString();
        Object result = null;

        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(keyString)) {
                result = cookies[i];
                break;
            }
        }
        return result;
    }


    public Set entrySet() {
        Set entries = new HashSet();
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                entries.add(new Entry(cookies[i].getName(), cookies[i]));
            }
        }
        return entries;
    }


    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RequestCookieMap)) {
            return false;
        }
        return super.equals(obj);
    }
} // END RequestCookiesMap

class InitParameterMap extends BaseContextMap {

    private final ServletContext servletContext;


    InitParameterMap(ServletContext newServletContext) {
        servletContext = newServletContext;
    }


    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (key == RIConstants.IMMUTABLE_MARKER) {
            return RIConstants.IMMUTABLE_MARKER;
        }
        String keyString = key.toString();
        return servletContext.getInitParameter(keyString);
    }


    public Set entrySet() {
        Set entries = new HashSet();

        for (Enumeration e = servletContext.getInitParameterNames();
             e.hasMoreElements();) {
            String initParamName = (String) e.nextElement();
            entries.add(new Entry(initParamName,
                                  servletContext.getInitParameter(
                                      initParamName)));
        }
        return entries;
    }


    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof InitParameterMap)) {
            return false;
        }
        return super.equals(obj);
    }
} // END InitParameterMap


class MyServletRequestWrapper extends ServletRequestWrapper {

    public MyServletRequestWrapper(ServletRequest request) {
        super(request);
    }


    public Object getAttribute(String key) {
        Object result = super.getAttribute(key);
        if (result == null) {
            ServletRequest wrapped = getRequest();
            if ((wrapped != null) &&
                (wrapped instanceof ServletRequestWrapper)) {
                wrapped = ((ServletRequestWrapper) wrapped).getRequest();
            }
            if (wrapped != null) {
                result = wrapped.getAttribute(key);
            }
        }
        return (result);
    }

}


class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public MyHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }


    public Object getAttribute(String key) {
        Object result = super.getAttribute(key);
        if (result == null) {
            ServletRequest wrapped = getRequest();
            if ((wrapped != null) &&
                (wrapped instanceof ServletRequestWrapper)) {
                wrapped = ((ServletRequestWrapper) wrapped).getRequest();
            }
            if (wrapped != null) {
                result = wrapped.getAttribute(key);
            }
        }
        return (result);
    }

}
