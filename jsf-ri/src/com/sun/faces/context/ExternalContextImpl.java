/*
 * $Id: ExternalContextImpl.java,v 1.67 2008/01/25 20:06:16 rlubke Exp $
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

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.TypedCollections;
import com.sun.faces.util.Util;
import com.sun.faces.util.RequestStateManager;

/**
 * <p>This implementation of {@link ExternalContext} is specific to the
 * servlet implementation.
 *
 * @author Brendan Murray
 * @version $Id: ExternalContextImpl.java,v 1.67 2008/01/25 20:06:16 rlubke Exp $
 */
public class ExternalContextImpl extends ExternalContext {

    private ServletContext servletContext = null;
    private ServletRequest request = null;
    private ServletResponse response = null;

    private Map<String,Object> applicationMap = null;
    private Map<String,Object> sessionMap = null;
    private Map<String,Object> requestMap = null;
    private Map<String,String> requestParameterMap = null;
    private Map<String,String[]> requestParameterValuesMap = null;
    private Map<String,String> requestHeaderMap = null;
    private Map<String,String[]> requestHeaderValuesMap = null;
    private Map<String,Object> cookieMap = null;
    private Map<String,String> initParameterMap = null;

    static final Class theUnmodifiableMapClass =
        Collections.unmodifiableMap(new HashMap<Object,Object>()).getClass();
    
    public ExternalContextImpl(ServletContext sc, ServletRequest request,
                               ServletResponse response) {

        // Validate the incoming parameters
        try {
            Util.parameterNonNull(sc);
            Util.parameterNonNull(request);
            Util.parameterNonNull(response);
        } catch (Exception e) {
            throw new FacesException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID));
        }

        // Save references to our context, request, and response
        this.servletContext = sc;
        this.request = request;        
        this.response = response;
        WebConfiguration config = WebConfiguration.getInstance(sc);
        if (config
              .isOptionEnabled(BooleanWebContextInitParameter.SendPoweredByHeader)) {
            ((HttpServletResponse) response)
                  .addHeader("X-Powered-By", "JSF/1.2");
        }
        
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
            requestHeaderMap = null;
            requestHeaderValuesMap = null;
            requestHeaderValuesMap = null;
            requestMap = null;
            requestParameterMap = null;
            requestParameterValuesMap = null;
        }
    }
    
    public void setRequestCharacterEncoding(String encoding) throws UnsupportedEncodingException {
        request.setCharacterEncoding(encoding);
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
        response.setCharacterEncoding(encoding);
    }


    @SuppressWarnings("unchecked")
    public Map<String,Object> getApplicationMap() {
        if (applicationMap == null) {
            applicationMap = new ApplicationMap(servletContext);
        }
        return applicationMap;
    }


    @SuppressWarnings("unchecked")
    public Map<String,Object> getSessionMap() {
        if (sessionMap == null) {
            sessionMap = new SessionMap((HttpServletRequest) request);
        }
        return sessionMap;
    }


    @SuppressWarnings("unchecked")
    public Map<String,Object> getRequestMap() {
        if (requestMap == null) {
            requestMap = new RequestMap(this.request);
        }
        return requestMap;
    }


    public Map<String,String> getRequestHeaderMap() {
        if (null == requestHeaderMap) {
            requestHeaderMap = 
                Collections.unmodifiableMap(
                    new RequestHeaderMap((HttpServletRequest) request));                                                      
        }
        return requestHeaderMap;
    }


    public Map<String,String[]> getRequestHeaderValuesMap() {
        if (null == requestHeaderValuesMap) {
            requestHeaderValuesMap = 
                Collections.unmodifiableMap(
                    new RequestHeaderValuesMap((HttpServletRequest) request));
        }
        return requestHeaderValuesMap;
    }


    public Map<String,Object> getRequestCookieMap() {
        if (null == cookieMap) {
            cookieMap =
                Collections.unmodifiableMap(
                    new RequestCookieMap((HttpServletRequest) request));
        }
        return cookieMap;
    }


    public Map<String,String> getInitParameterMap() {
        if (null == initParameterMap) {
            initParameterMap = 
                Collections.unmodifiableMap(
                    new InitParameterMap(servletContext));
        }
        return initParameterMap;
    }


    public Map<String,String> getRequestParameterMap() {
        if (null == requestParameterMap) {
            requestParameterMap = 
                Collections.unmodifiableMap(
                    new RequestParameterMap(request));
        }
        return requestParameterMap;
    }


    public Map<String,String[]> getRequestParameterValuesMap() {
        if (null == requestParameterValuesMap) {
            requestParameterValuesMap = 
                Collections.unmodifiableMap(
                    new RequestParameterValuesMap(request));
        }
        return requestParameterValuesMap;
    }


    public Iterator<String> getRequestParameterNames() {
        final Enumeration namEnum = request.getParameterNames();

        return new Iterator<String>() {
            public boolean hasNext() {
                return namEnum.hasMoreElements();
            }


            public String next() {
                return (String) namEnum.nextElement();
            }


            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }


    public Locale getRequestLocale() {
        return request.getLocale();
    }


    public Iterator<Locale> getRequestLocales() {
        return (new LocalesIterator(request.getLocales()));
    }


    public String getRequestPathInfo() {
        return (((HttpServletRequest) request).getPathInfo());
    }


    public Cookie[] getRequestCookies() {
        return (((HttpServletRequest) request).getCookies());
    }

    public String getRealPath(String path) {
	return servletContext.getRealPath(path);
    }

    public String getRequestContextPath() {
        return (((HttpServletRequest) request).getContextPath());
    }


    public String getRequestServletPath() {
        return (((HttpServletRequest) request).getServletPath());
    }
    
    public String getRequestCharacterEncoding() {
        return (request.getCharacterEncoding());
    }

     
    public String getRequestContentType() {
        return (request.getContentType());
    }
    
    public String getResponseCharacterEncoding() {
        return (response.getCharacterEncoding());
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


    public Set<String> getResourcePaths(String path) {
        return TypedCollections.dynamicallyCastSet(servletContext.getResourcePaths(path), String.class);
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
    }

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

    /**
     * @see ExternalContext#getMimeType(String)
     */
    public String getMimeType(String file) {
        return servletContext.getMimeType(file);
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


    private static class LocalesIterator implements Iterator<Locale> {

        public LocalesIterator(Enumeration locales) {
            this.locales = locales;
        }


        private Enumeration locales;


        public boolean hasNext() {
            return locales.hasMoreElements();
        }


        public Locale next() {
            return (Locale) locales.nextElement();
        }


        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}

abstract class BaseContextMap<V> extends AbstractMap<String,V> {

    private Set<Map.Entry<String, V>> entrySet;
    private Set<String> keySet;
    private Collection<V> values;
    
    // Supported by maps if overridden
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }


    // Supported by maps if overridden
    @Override
    public void putAll(Map t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<String, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet();
        }

        return entrySet;
    }

    @Override
    public Set<String> keySet() {
        if (keySet == null) {
            keySet = new KeySet();
        }

        return keySet;
    }

    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new ValueCollection();
        }

        return values;
    }


    // Supported by maps if overridden
    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    protected boolean removeKey(Object key) {
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

    protected abstract Iterator<Map.Entry<String, V>> getEntryIterator();
    protected abstract Iterator<String> getKeyIterator();
    protected abstract Iterator<V> getValueIterator();

    abstract class BaseSet<E> extends AbstractSet<E> {

        @Override
        public int size() {
            int size = 0;
            for (Iterator<E> i = iterator(); i.hasNext(); size++) {
                i.next();
            }
            return size;
        }

    }

    class EntrySet extends BaseSet<Map.Entry<String, V>> {

        @Override
        public Iterator<Map.Entry<String, V>> iterator() {
            return getEntryIterator();
        }

        @Override
        public boolean remove(Object o) {
            return o instanceof Map.Entry
                   && removeKey(((Map.Entry) o).getKey());
        }

    }

    class KeySet extends BaseSet<String> {

        @Override
        public Iterator<String> iterator() {
            return getKeyIterator();
        }


        @Override
        public boolean contains(Object o) {
            return BaseContextMap.this.containsKey(o);
        }

        @Override
        public boolean remove(Object o) {
            return o instanceof String && removeKey(o);
        }
    }

    class ValueCollection extends AbstractCollection<V> {

        @Override
        public int size() {
            int size = 0;
            for (Iterator i = iterator(); i.hasNext(); size++) {
                i.next();
            }
            return size;
        }

        @Override
        public Iterator<V> iterator() {
            return getValueIterator();
        }

        @Override
        public boolean remove(Object o) {
            return removeValue(o);
        }
    }

    abstract class BaseIterator<E> implements Iterator<E> {

        protected Enumeration e;
        protected String currentKey;
        protected boolean removeCalled = false;

        BaseIterator(Enumeration e) {
            this.e = e;
        }

        public boolean hasNext() {
            return e.hasMoreElements();
        }

        public String nextKey() {
            removeCalled = false;
            currentKey = (String) e.nextElement();
            return currentKey;
        }
    }

    class EntryIterator extends BaseIterator<Map.Entry<String,V>> {

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

        public Map.Entry<String,V> next() {
            nextKey();
            return new Entry<V>(currentKey, get(currentKey));
        }
    }

     class KeyIterator extends BaseIterator<String> {

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

        public String next() {
            return nextKey();
        }
    }

    class ValueIterator extends BaseIterator<V> {

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

        public V next() {
            nextKey();
            return get(currentKey);
        }
    }


    static class Entry<V> implements Map.Entry<String,V> {

        // immutable Entry
        private final String key;
        private final V value;


        Entry(String key, V value) {
            this.key = key;
            this.value = value;
        }


        public String getKey() {
            return key;
        }


        public V getValue() {
            return value;
        }


        // No support of setting the value
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int hashCode() {
            return ((key == null ? 0 : key.hashCode()) ^
                (value == null ? 0 : value.hashCode()));
        }

        @Override
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

abstract class StringArrayValuesMap extends BaseContextMap<String[]> {

    @Override
    public boolean equals(Object obj) {
        
        if (obj == null || 
            !(obj.getClass() == ExternalContextImpl.theUnmodifiableMapClass)) {
            return false;
        }
        Map objMap = (Map) obj;
        
        if (this.size() != objMap.size()) {
            return false;
        }
        String[] thisKeys = keySet().toArray(new String[this.size()]);
        Object[] objKeys = objMap.keySet().toArray();
        
        Arrays.sort(thisKeys);
        Arrays.sort(objKeys);
        
        if (!(Arrays.equals(thisKeys, objKeys))) {
            return false;
        } else {
            for (Object key : thisKeys) {
                Object[] thisVal = this.get(key);
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
         for (Object o : entrySet()) {             
             Map.Entry entry = (Map.Entry) o;             
             hashCode += entry.getKey().hashCode();
             hashCode +=
                   (Arrays.hashCode((Object[]) entry.getValue()));
         }
        return hashCode;
    }

    @Override
    public boolean containsValue(Object value) {
                
        if (value == null || !value.getClass().isArray()) {
            return false;
        }
        
        Set entrySet = entrySet();
        for (Object anEntrySet : entrySet) {
            Map.Entry entry = (Map.Entry) anEntrySet;
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

    @Override
    public void clear() {
        for (Enumeration e = servletContext.getAttributeNames();
             e.hasMoreElements(); ) {
            servletContext.removeAttribute((String) e.nextElement());
        }
    }

    // Supported by maps if overridden
    @Override
    public void putAll(Map t) {
        for (Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            servletContext.setAttribute((String) entry.getKey(),
                                        entry.getValue());
        }
    }

    @Override
    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return servletContext.getAttribute(key.toString());
    }

    @Override
    public Object put(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException();
        }       
        Object result = servletContext.getAttribute(key.toString());
        servletContext.setAttribute(key.toString(), value);
        return (result);
    }

    @Override
    public Object remove(Object key) {
        if (key == null) {
            return null;
        }
        String keyString = key.toString();
        Object result = servletContext.getAttribute(keyString);
        servletContext.removeAttribute(keyString);
        return (result);
    }


    @Override
    public boolean containsKey(Object key) {
        return (servletContext.getAttribute(key.toString()) != null);
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !(obj instanceof ApplicationMap))
                   && super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hashCode = 7 * servletContext.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap


    @SuppressWarnings("unchecked")
    protected Iterator<Map.Entry<String, Object>> getEntryIterator() {
        return new EntryIterator(servletContext.getAttributeNames());
    }

    @SuppressWarnings("unchecked")
    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(servletContext.getAttributeNames());
    }

    @SuppressWarnings("unchecked")
    protected Iterator<Object> getValueIterator() {
        return new ValueIterator(servletContext.getAttributeNames());
    }

} // END ApplicationMap

class SessionMap extends BaseContextMap {

    private final HttpServletRequest request;

    SessionMap(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void clear() {
        HttpSession session = getSession(false);
        if (session != null) {
            for (Enumeration e = session.getAttributeNames();
                 e.hasMoreElements();) {
                String name = (String) e.nextElement();
                session.removeAttribute(name);
            }
        }
    }

    // Supported by maps if overridden
    @Override
    public void putAll(Map t) {
        HttpSession session = getSession(true);
        for (Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            session.setAttribute((String) entry.getKey(),
                                 entry.getValue());
        }
    }

    @Override
    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        HttpSession session = getSession(false);
        return ((session != null) ? session.getAttribute(key.toString()) : null);

    }


    public Object put(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException();
        }        
        HttpSession session = getSession(true);
        Object result = session.getAttribute(key.toString());
        session.setAttribute(key.toString(), value);
        return (result);
    }

    @Override
    public Object remove(Object key) {
        if (key == null) {
            return null;
        }
        HttpSession session = getSession(false);
        if (session != null) {
            String keyString = key.toString();
            Object result = session.getAttribute(keyString);
            session.removeAttribute(keyString);
            return (result);
        }
        return null;
    }


    @Override
    public boolean containsKey(Object key) {
        HttpSession session = getSession(false);
        return ((session != null)
                && session.getAttribute(key.toString()) != null);
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !(obj instanceof SessionMap))
               && super.equals(obj);
    }


    private HttpSession getSession(boolean createNew) {
        return request.getSession(createNew);
    }

    @Override
    public int hashCode() {
        HttpSession session = getSession(false);
        int hashCode =
              7 * ((session != null) ? session.hashCode() : super.hashCode());
        if (session != null) {
            for (Iterator i = entrySet().iterator(); i.hasNext();) {
                hashCode += i.next().hashCode();
            }
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap

    @SuppressWarnings("unchecked")
    protected Iterator<Map.Entry<String,Object>> getEntryIterator() {
        HttpSession session = getSession(false);
        return ((session != null)
                ? new EntryIterator(session.getAttributeNames())
                : Collections.emptyMap().entrySet().iterator());
    }

    @SuppressWarnings("unchecked")
    protected Iterator<String> getKeyIterator() {
        HttpSession session = getSession(false);
        return ((session != null)
                ? new KeyIterator(session.getAttributeNames())
                : Collections.emptyMap().entrySet().iterator());
    }

    @SuppressWarnings("unchecked")
    protected Iterator<Object> getValueIterator() {
        HttpSession session = getSession(false);
        return ((session != null)
                ? new ValueIterator(session.getAttributeNames())
                : Collections.emptyMap().entrySet().iterator());
    }

} // END SessionMap

class RequestMap extends BaseContextMap {

    private final ServletRequest request;    


    RequestMap(ServletRequest request) {
        this.request = request;     
    }

    @Override
    public void clear() {
        for (Enumeration e = request.getAttributeNames();
             e.hasMoreElements(); ) {
            request.removeAttribute((String) e.nextElement());
        }
    }

    // Supported by maps if overridden
    @Override
    public void putAll(Map t) {
        for (Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            request.setAttribute((String) entry.getKey(),
                                        entry.getValue());
        }
    }

    @Override
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
        Object result = request.getAttribute(key.toString());
        request.setAttribute(key.toString(), value);
        return (result);
    }

    @Override
    public Object remove(Object key) {
        if (key == null) {
            return null;
        }
        String keyString = key.toString();
        Object result = request.getAttribute(keyString);
        request.removeAttribute(keyString);
        return (result);
    }


    @Override
    public boolean containsKey(Object key) {
        return (request.getAttribute(key.toString()) != null);
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !(obj instanceof RequestMap))
               && super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hashCode = 7 * request.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap

    @SuppressWarnings("unchecked")
    protected Iterator<Map.Entry<String,Object>> getEntryIterator() {
        return new EntryIterator(request.getAttributeNames());
    }

    @SuppressWarnings("unchecked")
    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(request.getAttributeNames());
    }

    @SuppressWarnings("unchecked")
    protected Iterator<Object> getValueIterator() {
        return new ValueIterator(request.getAttributeNames());
    }

} // END RequestMap

class RequestParameterMap extends BaseContextMap<String> {

    private final ServletRequest request;


    RequestParameterMap(ServletRequest request) {
        this.request = request;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        return request.getParameter(key.toString());
    }

    @Override
    public Set<Map.Entry<String,String>> entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    @Override
    public Collection<String> values() {
        return Collections.unmodifiableCollection(super.values());
    }


    @Override
    public boolean containsKey(Object key) {
        return (request.getParameter(key.toString()) != null);
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null ||
                 !(obj.getClass()
                   == ExternalContextImpl
                       .theUnmodifiableMapClass)) && super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hashCode = 7 * request.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap

    protected Iterator<Map.Entry<String,String>> getEntryIterator() {
        return new EntryIterator(request.getParameterNames());
    }

    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(request.getParameterNames());
    }

    protected Iterator<String> getValueIterator() {
        return new ValueIterator(request.getParameterNames());
    }

} // END RequestParameterMap

class RequestParameterValuesMap extends StringArrayValuesMap {

    private final ServletRequest request;


    RequestParameterValuesMap(ServletRequest request) {
        this.request = request;
    }


    @Override
    public String[] get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        return request.getParameterValues(key.toString());
    }


    @Override
    public boolean containsKey(Object key) {
        return (request.getParameterValues(key.toString()) != null);
    }

    @Override
    public Set<Map.Entry<String,String[]>> entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    @Override
    public Collection<String[]> values() {
        return Collections.unmodifiableCollection(super.values());
    }

    @Override
    public int hashCode() {        
        return hashCode(request);
    }


    // --------------------------------------------- Methods from BaseContextMap

    protected Iterator<Map.Entry<String, String[]>> getEntryIterator() {
        return new EntryIterator(request.getParameterNames());
    }

    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(request.getParameterNames());
    }

    protected Iterator<String[]> getValueIterator() {
        return new ValueIterator(request.getParameterNames());
    }

} // END RequestParameterValuesMap

class RequestHeaderMap extends BaseContextMap<String> {

    private final HttpServletRequest request;


    RequestHeaderMap(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
       
        return (request.getHeader(key.toString()));
    }

    @Override
    public Set<Map.Entry<String,String>> entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    @Override
    public Collection<String> values() {
        return Collections.unmodifiableCollection(super.values());
    }


    @Override
    public boolean containsKey(Object key) {
        return (request.getHeader(key.toString()) != null);
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null ||
                 !(obj.getClass()
                   == ExternalContextImpl
                       .theUnmodifiableMapClass)) && super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hashCode = 7 * request.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap

    protected Iterator<Map.Entry<String,String>> getEntryIterator() {
        return new EntryIterator(request.getHeaderNames());
    }

    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(request.getHeaderNames());
    }

    protected Iterator<String> getValueIterator() {
        return new ValueIterator(request.getHeaderNames());
    }

} // END RequestHeaderMap

class RequestHeaderValuesMap extends StringArrayValuesMap {

    private final HttpServletRequest request;


    RequestHeaderValuesMap(HttpServletRequest request) {
        this.request = request;
    }


    @Override
    public boolean containsKey(Object key) {
        return (request.getHeaders(key.toString()) != null);
    }

    @Override
    public String[] get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
      
        List<String> valuesList = new ArrayList<String>();
        Enumeration valuesEnum = this.request.getHeaders(key.toString());
        while (valuesEnum.hasMoreElements()) {
            valuesList.add((String) valuesEnum.nextElement());
        } 

        return valuesList.toArray(new String[valuesList.size()]); 
    }

    @Override
    public Set<Map.Entry<String,String[]>> entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    @Override
    public Collection<String[]> values() {
        return Collections.unmodifiableCollection(super.values());
    }        

    @Override
    public int hashCode() {        
        return hashCode(request);
    }

    // --------------------------------------------- Methods from BaseContextMap

    protected Iterator<Map.Entry<String,String[]>> getEntryIterator() {
        return new EntryIterator(request.getHeaderNames());
    }

    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(request.getHeaderNames());
    }

    protected Iterator<String[]> getValueIterator() {
        return new ValueIterator(request.getHeaderNames());
    }

} // END RequestHeaderValuesMap

class RequestCookieMap extends BaseContextMap<Object> {

    private final HttpServletRequest request;


    RequestCookieMap(HttpServletRequest newRequest) {
        this.request = newRequest;
    }

    @Override
    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
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

    @Override
    public Set<Map.Entry<String,Object>> entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    @Override
    public Collection<Object> values() {
        return Collections.unmodifiableCollection(super.values());
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null ||
                 !(obj.getClass()
                   == ExternalContextImpl
                       .theUnmodifiableMapClass)) && super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hashCode = 7 * request.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap


    protected Iterator<Map.Entry<String,Object>> getEntryIterator() {
        return new EntryIterator(
                new CookieArrayEnumerator(request.getCookies()));
    }

    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(
                new CookieArrayEnumerator(request.getCookies()));
    }

    protected Iterator<Object> getValueIterator() {
        return new ValueIterator(
            new CookieArrayEnumerator(request.getCookies()));
    }

    private static class CookieArrayEnumerator implements Enumeration {

        Cookie[] cookies;
        int curIndex = -1;
        int upperBound;

        public CookieArrayEnumerator(Cookie[] cookies) {
            this.cookies = cookies;
            upperBound = ((this.cookies != null) ? this.cookies.length : -1);
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

class InitParameterMap extends BaseContextMap<String> {

    private final ServletContext servletContext;


    InitParameterMap(ServletContext newServletContext) {
        servletContext = newServletContext;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }        
        String keyString = key.toString();
        return servletContext.getInitParameter(keyString);
    }

    @Override
    public Set<Map.Entry<String,String>> entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    @Override
    public Collection<String> values() {
        return Collections.unmodifiableCollection(super.values());
    }

    @Override
    public boolean containsKey(Object key) {
        return (servletContext.getInitParameter(key.toString()) != null);
    }



    public boolean equals(Object obj) {
        return !(obj == null ||
                 !(obj.getClass()
                   == ExternalContextImpl
                       .theUnmodifiableMapClass)) && super.equals(obj);
    }



    public int hashCode() {
        int hashCode = 7 * servletContext.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    // --------------------------------------------- Methods from BaseContextMap

    protected Iterator<Map.Entry<String,String>> getEntryIterator() {
        return new EntryIterator(servletContext.getInitParameterNames());
    }

    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(servletContext.getInitParameterNames());
    }

    protected Iterator<String> getValueIterator() {
        return new ValueIterator(servletContext.getInitParameterNames());
    }

} // END InitParameterMap
