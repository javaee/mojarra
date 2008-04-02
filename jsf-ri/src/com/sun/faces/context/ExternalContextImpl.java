/*
 * $Id: ExternalContextImpl.java,v 1.20 2004/01/16 21:31:02 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.faces.context;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.AbstractMap;

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
import com.sun.faces.util.Util;

/**
 * <p>This implementation of {@link ExternalContext} is specific to the
 * servlet implementation.
 *
 * @author Brendan Murray
 * @version $Id: ExternalContextImpl.java,v 1.20 2004/01/16 21:31:02 craigmcc Exp $
 *
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
        } catch (Exception e ) {
            throw new FacesException(Util.getExceptionMessage(Util.FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID));
        }

        // Save references to our context, request, and response
        this.servletContext = sc;
	// PENDING(edburns): Craig's workaround breaks
	// TestValidatorTags.java because Cactus expects a certain type
	// to be present for the value of the request.
	if (RIConstants.IS_UNIT_TEST_MODE) {
	    this.request = request;
	}
	else {
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
        if (sessionMap == null)
            sessionMap = new SessionMap((HttpServletRequest) request);
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
	    requestHeaderMap = new RequestHeaderMap((HttpServletRequest) request);
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
        final Enumeration  namEnum = request.getParameterNames();

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

    public void dispatchMessage(String requestURI) throws IOException, FacesException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(requestURI);
        try {
            requestDispatcher.forward(this.request, this.response);
        } catch (IOException ioe) {
            // e.printStackTrace();
            throw ioe;
        } catch (ServletException se) {
            throw new FacesException(se);
        }
    }

    public void redirectMessage(String requestURI) throws IOException {
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
	return ((HttpServletRequest)request).getAuthType();
    }

    public String getRemoteUser() {
	return ((HttpServletRequest)request).getRemoteUser();
    }

    public java.security.Principal getUserPrincipal() {
	return ((HttpServletRequest)request).getUserPrincipal();
    }

    public boolean isUserInRole(String role) {
	return ((HttpServletRequest)request).isUserInRole(role);
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
            if (obj == null || !(obj instanceof Map.Entry))
                return false;

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
        Set entries = new HashSet();
        for (Enumeration e = servletContext.getAttributeNames();
           e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            entries.add(new Entry(key, servletContext.getAttribute(key)));
        }
        return entries;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ApplicationMap))
            return false;
        return super.equals(obj);
    }

} // END ApplicationMap

class SessionMap extends BaseContextMap {
    private HttpServletRequest request = null;

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
             e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            entries.add(new Entry(key, session.getAttribute(key)));
        }
        return entries;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SessionMap))
            return false;
        return super.equals(obj);
    }

    private HttpSession getSession() {
        return request.getSession(true);
    }

} // END SessionMap

class RequestMap extends BaseContextMap {
    private ServletRequest request = null;

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
        if (obj == null || !(obj instanceof RequestMap))
            return false;
        return super.equals(obj);
    }
} // END RequestMap

class RequestParameterMap extends BaseContextMap {
    private ServletRequest request = null;

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
           e.hasMoreElements(); ) {
            String paramName = (String) e.nextElement();
            entries.add(new Entry(paramName, request.getParameter(paramName)));
        }
        return entries;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RequestParameterMap))
            return false;
        return super.equals(obj);
    }
} // END RequestParameterMap

class RequestParameterValuesMap extends BaseContextMap {
    private ServletRequest request = null;

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

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RequestParameterValuesMap))
            return false;
        return super.equals(obj);
    }
} // END RequestParameterValuesMap

class RequestHeaderMap extends BaseContextMap {
    private HttpServletRequest request = null;

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
        if (obj == null || !(obj instanceof RequestHeaderMap))
            return false;
        return super.equals(obj);
    }
} // END RequestHeaderMap

class RequestHeaderValuesMap extends BaseContextMap {
    private HttpServletRequest request = null;

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
        Set entries = new HashSet();
        for (Enumeration e = request.getHeaderNames();
             e.hasMoreElements();) {
            String headerName = (String) e.nextElement();
            entries.add(new Entry(headerName, request.getHeaders(headerName)));
        }
        return entries;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RequestHeaderValuesMap))
            return false;
        return super.equals(obj);
    }

    // Override of containsValue was necessary as Enumeration.equals(Enumeration)
    // returned false.
    public boolean containsValue(Object value) {
        if (value == null || !(value instanceof Enumeration))
            return false;

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
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            int thisHash = 0;
            int thisCount = 0;
            Map.Entry entry = (Map.Entry) i.next();
            Enumeration thisMap = (Enumeration) entry.getValue();

            while (thisMap.hasMoreElements()) {
                thisHash += thisMap.nextElement().hashCode();
                thisCount++;
            }
            if (thisCount == valCount && thisHash == valHash)
                return true;
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
} // END RequestHeaderValuesMap

class RequestCookieMap extends BaseContextMap {
    private HttpServletRequest request = null;

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
        if (obj == null || !(obj instanceof RequestCookieMap))
            return false;
        return super.equals(obj);
    }
} // END RequestCookiesMap

class InitParameterMap extends BaseContextMap {
    private ServletContext servletContext;

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
             e.hasMoreElements(); ) {
            String initParamName = (String) e.nextElement();
            entries.add(new Entry(initParamName,
                servletContext.getInitParameter(initParamName)));
        }
        return entries;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof InitParameterMap))
            return false;
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
