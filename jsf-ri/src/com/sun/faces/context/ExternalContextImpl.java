/*
 * $Id: ExternalContextImpl.java,v 1.36 2005/08/22 22:10:10 ofung Exp $
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

package com.sun.faces.context;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.AbstractSet;
import java.util.NoSuchElementException;
import java.util.AbstractCollection;
import java.util.Collection;

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

import com.sun.faces.RIConstants;
import com.sun.faces.context.BaseContextMap.EntryIterator;
import com.sun.faces.context.BaseContextMap.KeyIterator;
import com.sun.faces.context.BaseContextMap.ValueIterator;
import com.sun.faces.util.Util;
import java.io.UnsupportedEncodingException;

/**
 * <p>This implementation of {@link ExternalContext} is specific to the
 * servlet implementation.
 *
 * @author Brendan Murray
 * @version $Id: ExternalContextImpl.java,v 1.36 2005/08/22 22:10:10 ofung Exp $
 */
public class ExternalContextImpl extends ExternalContext {

    private ServletContext servletContext = null;
    private ServletRequest request = null;
    private ServletResponse response = null;

    private ApplicationMap applicationMap = null;
    private SessionMap sessionMap = null;
    private RequestMap requestMap = null;
    private Map requestParameterMap = null;
    private Map requestParameterValuesMap = null;
    private Map requestHeaderMap = null;
    private Map requestHeaderValuesMap = null;
    private Map cookieMap = null;
    private Map initParameterMap = null;

    static final Class theUnmodifiableMapClass =
        Collections.unmodifiableMap(new HashMap()).getClass();
    
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
        if (Util.isUnitTestModeEnabled()) {
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

    public void setRequest(Object request) {
	if (request instanceof ServletRequest) {
	    this.request = (ServletRequest) request;
	}
    }
    
    public void setRequestCharacterEncoding(String encoding) throws UnsupportedEncodingException {
        ((ServletRequest)request).setCharacterEncoding(encoding);
    }

    public Object getResponse() {
        return this.response;
    }

    public void setResponse(Object response) {
	if (response instanceof ServletResponse) {
	    this.response = (ServletResponse) response;
	}
    }
    
    public void setResponseCharacterEncoding(String encoding) {
        ((ServletResponse)response).setCharacterEncoding(encoding);
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
            Collections.unmodifiableMap(new RequestHeaderMap((HttpServletRequest) request));
        }
        return requestHeaderMap;
    }


    public Map getRequestHeaderValuesMap() {
        if (null == requestHeaderValuesMap) {
            requestHeaderValuesMap = 
                Collections.unmodifiableMap(new RequestHeaderValuesMap((HttpServletRequest) request));
        }
        return requestHeaderValuesMap;
    }


    public Map getRequestCookieMap() {
        if (null == cookieMap) {
            cookieMap =
            Collections.unmodifiableMap(new RequestCookieMap((HttpServletRequest) request));
        }
        return cookieMap;
    }


    public Map getInitParameterMap() {
        if (null == initParameterMap) {
            initParameterMap = 
            Collections.unmodifiableMap(new InitParameterMap(servletContext));
        }
        return initParameterMap;
    }


    public Map getRequestParameterMap() {
        if (null == requestParameterMap) {
            requestParameterMap = 
                Collections.unmodifiableMap(new RequestParameterMap(request));
        }
        return requestParameterMap;
    }


    public Map getRequestParameterValuesMap() {
        if (null == requestParameterValuesMap) {
            requestParameterValuesMap = 
            Collections.unmodifiableMap(new RequestParameterValuesMap(request));
        }
        return requestParameterValuesMap;
    }


    public Iterator getRequestParameterNames() {
        final Enumeration namEnum = request.getParameterNames();

        return new Iterator() {
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
    
    public String getRequestCharacterEncoding() {
        return (((ServletRequest) request).getCharacterEncoding());
    }

     
    public String getRequestContentType() {
        return (request.getContentType());
    }
    
    public String getResponseCharacterEncoding() {
        return (((ServletResponse) response).getCharacterEncoding());
    }
    
    public String getResponseContentType() {
        return (response.getContentType());
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
        URL url;
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
            ((HttpServletResponse) response).
                    sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
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


    private static class LocalesIterator implements Iterator {

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

    private Set entrySet;
    private Set keySet;
    private Collection values;

    // Supported by maps if overridden
    public void clear() {
        throw new UnsupportedOperationException();
    }


    // Supported by maps if overridden
    public void putAll(Map t) {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet();
        }

        return entrySet;
    }

    public Set keySet() {
        if (keySet == null) {
            keySet = new KeySet();
        }

        return keySet;
    }

    public Collection values() {
        if (values == null) {
            values = new ValueCollection();
        }

        return values;
    }


    // Supported by maps if overridden
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    protected boolean removeKey(String key) {
        return (this.remove(key) != null);
    }

    protected boolean removeValue(Object value) {
        boolean valueRemoved = false;
        if (value == null) {
            return false;
        }
        if (containsValue(value)) {
            for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
                Map.Entry e = (Map.Entry) i.next();
                if (value.equals(e.getValue())) {                    
                    valueRemoved = (remove(e.getKey()) != null);
                }
            }
        }
        return valueRemoved;
    }

    protected abstract Iterator getEntryIterator();
    protected abstract Iterator getKeyIterator();
    protected abstract Iterator getValueIterator();

    abstract class BaseSet extends AbstractSet {

        public int size() {
            int size = 0;
            for (Iterator i = iterator(); i.hasNext(); size++) {
                i.next();
            }
            return size;
        }

    }

    class EntrySet extends BaseSet {

        public Iterator iterator() {
            return getEntryIterator();
        }

        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            return removeKey((String) ((Map.Entry) o).getKey());
        }

    }

    class KeySet extends BaseSet {

        public Iterator iterator() {
            return getKeyIterator();
        }

        public boolean remove(Object o) {
            if (!(o instanceof String)) {
                return false;
            }
            return removeKey((String) o);
        }
    }

    class ValueCollection extends AbstractCollection {

        public int size() {
            int size = 0;
            for (Iterator i = iterator(); i.hasNext(); size++) {
                i.next();
            }
            return size;
        }

        public Iterator iterator() {
            return getValueIterator();
        }

        public boolean remove(Object o) {
            return removeValue(o);
        }
    }

    abstract class BaseIterator implements Iterator {

        protected Enumeration e;
        protected String currentKey;
        protected boolean removeCalled = false;

        BaseIterator(Enumeration e) {
            this.e = e;
        }

        public boolean hasNext() {
            return e.hasMoreElements();
        }
    }

    class EntryIterator extends BaseIterator {

        EntryIterator(Enumeration e) {
            super(e);
        }

        public void remove() {
            if (currentKey != null && !removeCalled) {
                removeCalled = true;
                removeKey(currentKey);
            } else {
                throw new IllegalStateException();
            }
        }

        public Object next() {
            removeCalled = false;
            currentKey = (String) e.nextElement();
            return new Entry(currentKey, get(currentKey));
        }
    }

     class KeyIterator extends BaseIterator {

        KeyIterator(Enumeration e) {
            super(e);
        }

        public void remove() {
            if (currentKey != null && !removeCalled) {
                removeCalled = true;
                removeKey(currentKey);
            } else {
                throw new IllegalStateException();
            }
        }

        public Object next() {
            removeCalled = false;
            currentKey = (String) e.nextElement();
            return currentKey;
        }
    }

    class ValueIterator extends BaseIterator {

        ValueIterator(Enumeration e) {
            super(e);
        }

        public void remove() {
            if (currentKey != null && !removeCalled) {
                removeCalled = true;
                removeValue(get(currentKey));
            } else {
                throw new IllegalStateException();
            }
        }

        public Object next() {
            removeCalled = false;
            currentKey = (String) e.nextElement();
            return get(currentKey);
        }
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

class ApplicationMap extends BaseContextMap {

    private final ServletContext servletContext;

    ApplicationMap(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void clear() {
        for (Enumeration e = servletContext.getAttributeNames();
             e.hasMoreElements(); ) {
            servletContext.removeAttribute((String) e.nextElement());
        }
    }

    // Supported by maps if overridden
    public void putAll(Map t) {
        for (Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            servletContext.setAttribute((String) entry.getKey(),
                                        entry.getValue());
        }
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


    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ApplicationMap)) {
            return false;
        }
        return super.equals(obj);
    }


    public int hashCode() {
        int hashCode = 7 * servletContext.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap



    protected Iterator getEntryIterator() {
        return new EntryIterator(servletContext.getAttributeNames());
    }

    protected Iterator getKeyIterator() {
        return new KeyIterator(servletContext.getAttributeNames());
    }

    protected Iterator getValueIterator() {
        return new ValueIterator(servletContext.getAttributeNames());
    }

} // END ApplicationMap

class SessionMap extends BaseContextMap {

    private final HttpServletRequest request;

    SessionMap(HttpServletRequest request) {
        this.request = request;
    }

    public void clear() {
         HttpSession session = getSession();
        for (Enumeration e = getSession().getAttributeNames();
             e.hasMoreElements(); ) {
            session.removeAttribute((String) e.nextElement());
        }
    }

    // Supported by maps if overridden
    public void putAll(Map t) {
        HttpSession session = getSession();
        for (Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            session.setAttribute((String) entry.getKey(),
                                 entry.getValue());
        }
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

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SessionMap)) {
            return false;
        }
        return super.equals(obj);
    }


    private HttpSession getSession() {
        return request.getSession(true);
    }

    public int hashCode() {
        int hashCode = 7 * request.getSession().hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap

    protected Iterator getEntryIterator() {
        return new EntryIterator(getSession().getAttributeNames());
    }

    protected Iterator getKeyIterator() {
        return new KeyIterator(getSession().getAttributeNames());
    }

    protected Iterator getValueIterator() {
        return new ValueIterator(getSession().getAttributeNames());
    }

} // END SessionMap

class RequestMap extends BaseContextMap {

    private final ServletRequest request;


    RequestMap(ServletRequest request) {
        this.request = request;
    }

    public void clear() {
        for (Enumeration e = request.getAttributeNames();
             e.hasMoreElements(); ) {
            request.removeAttribute((String) e.nextElement());
        }
    }

    // Supported by maps if overridden
    public void putAll(Map t) {
        for (Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            request.setAttribute((String) entry.getKey(),
                                        entry.getValue());
        }
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

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RequestMap)) {
            return false;
        }
        return super.equals(obj);
    }

    public int hashCode() {
        int hashCode = 7 * request.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap

    protected Iterator getEntryIterator() {
        return new EntryIterator(request.getAttributeNames());
    }

    protected Iterator getKeyIterator() {
        return new KeyIterator(request.getAttributeNames());
    }

    protected Iterator getValueIterator() {
        return new ValueIterator(request.getAttributeNames());
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
        return Collections.unmodifiableSet(super.entrySet());
    }

    public Set keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    public Collection values() {
        return Collections.unmodifiableCollection(super.values());
    }

    public boolean equals(Object obj) {
        if (obj == null || 
            !(obj.getClass() == ExternalContextImpl.theUnmodifiableMapClass)) {
            return false;
        }
        return super.equals(obj);
    }

    public int hashCode() {
        int hashCode = 7 * request.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap

    protected Iterator getEntryIterator() {
        return new EntryIterator(request.getParameterNames());
    }

    protected Iterator getKeyIterator() {
        return new KeyIterator(request.getParameterNames());
    }

    protected Iterator getValueIterator() {
        return new ValueIterator(request.getParameterNames());
    }

} // END RequestParameterMap

class RequestParameterValuesMap extends BaseContextMap {

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
        return Collections.unmodifiableSet(super.entrySet());
    }

    public Set keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    public Collection values() {
        return Collections.unmodifiableCollection(super.values());
    }

    public boolean equals(Object obj) {
        if (obj == null || 
            !(obj.getClass() == ExternalContextImpl.theUnmodifiableMapClass)) {
            return false;
        }
        return super.equals(obj);
    }

     public int hashCode() {
        int hashCode = 7 * request.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap

    protected Iterator getEntryIterator() {
        return new EntryIterator(request.getParameterNames());
    }

    protected Iterator getKeyIterator() {
        return new KeyIterator(request.getParameterNames());
    }

    protected Iterator getValueIterator() {
        return new ValueIterator(request.getParameterNames());
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
        return Collections.unmodifiableSet(super.entrySet());
    }

    public Set keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    public Collection values() {
        return Collections.unmodifiableCollection(super.values());
    }


    public boolean equals(Object obj) {
        if (obj == null ||
            !(obj.getClass() == ExternalContextImpl.theUnmodifiableMapClass)) {
            return false;
        }
        return super.equals(obj);
    }

     public int hashCode() {
        int hashCode = 7 * request.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap

    protected Iterator getEntryIterator() {
        return new EntryIterator(request.getHeaderNames());
    }

    protected Iterator getKeyIterator() {
        return new KeyIterator(request.getHeaderNames());
    }

    protected Iterator getValueIterator() {
        return new ValueIterator(request.getHeaderNames());
    }

} // END RequestHeaderMap

class RequestHeaderValuesMap extends BaseContextMap {

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
        return (request).getHeaders(key.toString());
    }

    public Set entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    }

    public Set keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    public Collection values() {
        return Collections.unmodifiableCollection(super.values());
    }

    public boolean equals(Object obj) {
        if (obj == null || 
            !(obj.getClass() == ExternalContextImpl.theUnmodifiableMapClass)) {
            return false;
        }
        return super.equals(obj);
    }


    // Override of containsValue was necessary as Enumeration.equals(Enumeration)
    // returned false.
    public boolean containsValue(Object value) {
        if (value == null || !(value instanceof Enumeration)) {
            return false;
        }

        int valHash = 0;
        int valCount = 0;

        // get sum of the hashcode for all elements for the
        // input value.
        Enumeration val = (Enumeration) value;
        while (val.hasMoreElements()) {
            valHash += val.nextElement().hashCode();
            valCount++;
        }

        // For each Map.Entry within this instance, compute
        // the hash for each value and compare against the
        // sum computed above.  Ensure that the number of elements
        // in each enumeration is the same as well.
        for (Iterator i = entrySet().iterator(); i.hasNext();) {
            int thisHash = 0;
            int thisCount = 0;
            Map.Entry entry = (Map.Entry) i.next();
            Enumeration thisMap = (Enumeration) entry.getValue();

            while (thisMap.hasMoreElements()) {
                thisHash += thisMap.nextElement().hashCode();
                thisCount++;
            }
            if (thisCount == valCount && thisHash == valHash) {
                return true;
            }
        }
        return false;
    }


    // necessary to break the rules somewhat here as it couldn't be
    // guaranteed that the hashCode of the Enumeration would
    // be the same from call to call even if the underlying values contained
    // within are the same.
    public int hashCode() {
        int hashSum = 0;
        for (Iterator i = entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            hashSum += entry.getKey().hashCode();
            for (Enumeration e = (Enumeration) entry.getValue();
                 e.hasMoreElements();) {
                hashSum += e.nextElement().hashCode();
            }
        }
        return hashSum;
    }

    // --------------------------------------------- Methods from BaseContextMap

    protected Iterator getEntryIterator() {
        return new EntryIterator(request.getHeaderNames());
    }

    protected Iterator getKeyIterator() {
        return new KeyIterator(request.getHeaderNames());
    }

    protected Iterator getValueIterator() {
        return new ValueIterator(request.getHeaderNames());
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
        return Collections.unmodifiableSet(super.entrySet());
    }

    public Set keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    public Collection values() {
        return Collections.unmodifiableCollection(super.values());
    }

    public boolean equals(Object obj) {
        if (obj == null || 
            !(obj.getClass() == ExternalContextImpl.theUnmodifiableMapClass)) {
            return false;
        }
        return super.equals(obj);
    }

     public int hashCode() {
        int hashCode = 7 * request.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap


    protected Iterator getEntryIterator() {
        return new EntryIterator(
                new CookieArrayEnumerator(request.getCookies()));
    }

    protected Iterator getKeyIterator() {
        return new KeyIterator(
                new CookieArrayEnumerator(request.getCookies()));
    }

    protected Iterator getValueIterator() {
        return new ValueIterator(
            new CookieArrayEnumerator(request.getCookies()));
    }

    private static class CookieArrayEnumerator implements Enumeration {

        Cookie[] cookies;
        int curIndex = -1;
        int upperBound;

        public CookieArrayEnumerator(Cookie[] cookies) {
            this.cookies = cookies;
            upperBound = cookies.length;
        }

        public boolean hasMoreElements() {
            return (curIndex + 2 <= upperBound);
        }

        public Object nextElement() {
            curIndex++;
            if (curIndex < upperBound) {
                return cookies[curIndex].getName();
            } else {
                throw new NoSuchElementException();
            }
        }
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
        return Collections.unmodifiableSet(super.entrySet());
    }

    public Set keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    public Collection values() {
        return Collections.unmodifiableCollection(super.values());
    }

    public boolean equals(Object obj) {
        if (obj == null || 
            !(obj.getClass() == ExternalContextImpl.theUnmodifiableMapClass)) {
            return false;
        }
        return super.equals(obj);
    }

     public int hashCode() {
        int hashCode = 7 * servletContext.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap

    protected Iterator getEntryIterator() {
        return new EntryIterator(servletContext.getInitParameterNames());
    }

    protected Iterator getKeyIterator() {
        return new KeyIterator(servletContext.getInitParameterNames());
    }

    protected Iterator getValueIterator() {
        return new ValueIterator(servletContext.getInitParameterNames());
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
