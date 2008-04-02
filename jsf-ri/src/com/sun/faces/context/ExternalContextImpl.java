/*
 * $Id: ExternalContextImpl.java,v 1.2 2003/03/24 19:45:26 eburns Exp $
 */

/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002, 2003 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */
package com.sun.faces.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mozilla.util.ParameterCheck;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

/**
 * <p>This implementation of {@Link ExternalContext} is specific to the
 * servlet implementation.
 * 
 * @author Brendan Murray
 * @version 0.1
 * 
 */
public class ExternalContextImpl extends ExternalContext {

    private ServletContext servletContext = null;
    private ServletRequest request = null;
    private HttpSession session = null;
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
									   	
        try {
            ParameterCheck.nonNull(sc);
            ParameterCheck.nonNull(request);
            ParameterCheck.nonNull(response);
        } catch (Exception e ) {
            throw new FacesException(Util.getExceptionMessage(Util.FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID));
        }
        
        this.servletContext = sc;
        this.request = request;
        this.response = response;
        if (this.request instanceof HttpServletRequest) {
            // If saveStateInPage is false, we need to create the
            // session at this point.
            boolean createSession = true;
            String paramValue = null;
	    
            if (null != (paramValue = 
                sc.getInitParameter(RIConstants.SAVESTATE_INITPARAM))){
                createSession = !paramValue.equalsIgnoreCase("true");
            }

            this.session = ((HttpServletRequest) request).getSession(createSession);
        }

        applicationMap = new ApplicationMap(servletContext);
        sessionMap = new SessionMap(session);
        requestMap = new RequestMap(request);
    }


    public Object getSession(boolean create) {
        HttpSession result = null;
        if (null != request) {
            result = ((HttpServletRequest) request).getSession(create);
        }
        return result;
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
        return applicationMap;
    }

    public Map getSessionMap() {
        return sessionMap;
    }

    public Map getRequestMap() {
        return requestMap;
    }

    public Map getRequestHeaderMap() {
	if (null == requestHeaderMap) {
	    requestHeaderMap = new RequestHeaderMap(request);
	}
        return requestHeaderMap;
    }

    public Map getRequestHeaderValuesMap() {
	if (null == requestHeaderValuesMap) {
	    requestHeaderValuesMap = new RequestHeaderValuesMap(request);
	}
        return requestHeaderValuesMap;
    }

    public Map getRequestCookieMap() {
	if (null == cookieMap) {
	    cookieMap = new RequestCookieMap(request);
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

    public String getRequestPathInfo() {
        return (((HttpServletRequest) request).getPathInfo());
    }

    public Cookie[] getRequestCookies() {
     	return (((HttpServletRequest) request).getCookies());
    }
     
    public String getRequestContextPath() {
        return (((HttpServletRequest) request).getContextPath());
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

    /**
     * <p>Dispatch a request to the apropriate context. In the
     * case of servlets, this is done via "forward", but for
     * portlets, it must use "include".</p>
     *
     * @param requestURI The input URI of the request tree.
     */
    public void dispatchMessage(String requestURI) throws IOException, FacesException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(requestURI); 
        try {
            requestDispatcher.forward(this.request, this.response);
        } catch (IOException ioe) {
            // e.printStackTrace();
            throw new IOException(ioe.getMessage()); 
        } catch (ServletException se) {
            throw new FacesException(se.getMessage()); 
        }
    }
}

class ApplicationMap implements Map {
    private ServletContext servletContext = null;

    public ApplicationMap(ServletContext servletContext) {
        this.servletContext = servletContext;
    }     

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return servletContext.getAttribute(key.toString());
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
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

    public void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        String keyString = key.toString();
        Object result = servletContext.getAttribute(keyString);
        servletContext.removeAttribute(keyString);
        return (result);
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }
}

class SessionMap implements Map {
    private HttpSession session = null;

    public SessionMap(HttpSession session) {
        this.session = session;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return session.getAttribute(key.toString());
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    public Object put(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException();
        }
        String keyString = key.toString();
        Object result = session.getAttribute(keyString);
        session.setAttribute(keyString, value);
        return (result);
    }

    public void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        String keyString = key.toString();
        Object result = session.getAttribute(keyString);
        session.removeAttribute(keyString);
        return (result);
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }
}

class RequestMap implements Map {
    private ServletRequest request = null;

    public RequestMap(ServletRequest request) {
        this.request = request;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return request.getAttribute(key.toString());
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
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

    public void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        String keyString = key.toString();
        Object result = request.getAttribute(keyString);
        request.removeAttribute(keyString);
        return (result);
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }
}

class RequestParameterMap implements Map {
    private ServletRequest request = null;

    public RequestParameterMap(ServletRequest request) {
        this.request = request;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return request.getParameter(key.toString());
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }
}

class RequestParameterValuesMap implements Map {
    private ServletRequest request = null;

    public RequestParameterValuesMap(ServletRequest request) {
        this.request = request;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return request.getParameterValues(key.toString());
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }
}

class RequestHeaderMap implements Map {
    private ServletRequest request = null;

    public RequestHeaderMap(ServletRequest request) {
        this.request = request;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return ((HttpServletRequest)request).getHeader(key.toString());
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }
}

class RequestHeaderValuesMap implements Map {
    private ServletRequest request = null;

    public RequestHeaderValuesMap(ServletRequest request) {
        this.request = request;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return ((HttpServletRequest)request).getHeaders(key.toString());
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }
}

class RequestCookieMap implements Map {
    private HttpServletRequest request = null;
    protected Cookie[] cookies = null;
    protected final int cookieLen;

    public RequestCookieMap(ServletRequest newRequest) {
        this.request = (HttpServletRequest) newRequest;
        cookies = request.getCookies();
	if (null != cookies) {
	    cookieLen = cookies.length;
	}
	else {
	    cookieLen = -1;
	}
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
	if (null == cookies) {
	    return null;
	}
        String keyString = key.toString();
	Object result = null;
	int i = 0;

	for (i = 0; i < cookieLen; i++) {
	    if (cookies[i].getName().equals(keyString)) {
		result = cookies[i];
		break;
	    }
	}
        return result;
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }
}

class InitParameterMap implements Map {
    private ServletContext servletContext;

    public InitParameterMap(ServletContext newServletContext) {
	servletContext = newServletContext;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }


    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }


    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }


    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        String keyString = key.toString();
        return servletContext.getInitParameter(keyString);
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }


    public Set keySet() {
        throw new UnsupportedOperationException();
    }


    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }


    public void putAll(Map map) {
        throw new UnsupportedOperationException();
    }


    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }


    public int size() {
        throw new UnsupportedOperationException();
    }


    public Collection values() {
        throw new UnsupportedOperationException();
    }


}
