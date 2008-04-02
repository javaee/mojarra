/*
 * $Id: EvaluationContext.java,v 1.3 2002/10/16 22:22:49 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;


import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;

import org.apache.taglibs.standard.jstl_el.jstl.VariableResolver;
import org.apache.taglibs.standard.jstl_el.jstl.ELEvaluator;
import org.apache.taglibs.standard.jstl_el.jstl.ELException;

import com.sun.faces.RIConstants;

import org.mozilla.util.Assert;

import com.sun.faces.util.Util;


/**
 * <p>Bean to be handed to BeanUtils and PropertyUtils representing the
 * evaluation context for model reference expressions.  The properties
 * represent the implicit objects that are exposed at the top level of
 * the context.</p>
 */

public class EvaluationContext {

    private ELEvaluator elEvaluator = null;


    public EvaluationContext(FacesContext facesContext) {
        this.facesContext = facesContext;
	
	// Lazily create elEvaluator
	if (null == (elEvaluator = (ELEvaluator)
		     facesContext.getServletContext().
		     getAttribute(RIConstants.ELEVALUATOR))) {
	    facesContext.getServletContext().
		setAttribute(RIConstants.ELEVALUATOR,
			     elEvaluator = new ELEvaluator(new ECResolver()));
	}
	Assert.assert_it(null != elEvaluator);
    }

    /**

    * The array of valid implicit objects.  Make sure the read-only
    * implicts are at the front of the array and that the
    * LAST_READONLY_IMPLICIT_INDEX is updated accordingly.

    */

    private String implicits[] =
    { 
	"facesContext", 
	"header",
	"headerValues",
	"param",
	"paramValues",
	"initParam",
	"cookie",
	"requestScope", 
	"sessionScope",
	"applicationScope"
    };
    /**

    * The index in the implicits array of the last read-only implicit
    * object.

    */
    private final int LAST_READONLY_IMPLICIT_INDEX = 2;


    private Map applicationScope = null;
    public Map getApplicationScope() {
        if (applicationScope == null) {
            applicationScope =
                new ApplicationScopeMap(facesContext.getServletContext());
        }
        return (applicationScope);
    }

    private Map header = null;
    public Map getHeader() {
        if (header == null) {
            header =
                new HeaderMap(facesContext);
        }
        return (header);
    }

    private Map headerValues = null;
    public Map getHeaderValues() {
        if (headerValues == null) {
            headerValues =
                new HeaderValuesMap(facesContext);
        }
        return (headerValues);
    }


    private Map param = null;
    public Map getParam() {
        if (param == null) {
            param =
                new ParamMap(facesContext);
        }
        return (param);
    }

    private Map paramValues = null;
    public Map getParamValues() {
        if (paramValues == null) {
            paramValues =
                new ParamValuesMap(facesContext);
        }
        return (paramValues);
    }

    private Map initParam = null;
    public Map getInitParam() {
        if (initParam == null) {
            initParam =
                new InitParamMap(facesContext);
        }
        return (initParam);
    }

    private Map cookie = null;
    public Map getCookie() {
        if (cookie == null) {
            cookie =
                new CookieMap(facesContext);
        }
        return (cookie);
    }


    private FacesContext facesContext = null;
    public FacesContext getFacesContext() {
        return (this.facesContext);
    }


    private Map requestScope = null;
    public Map getRequestScope() {
        if (requestScope == null) {
            requestScope =
                new RequestScopeMap(facesContext.getServletRequest());
        }
        return (requestScope);
    }


    private Map sessionScope = null;
    public Map getSessionScope() {
        if (sessionScope == null) {
            sessionScope =
                new SessionScopeMap(facesContext.getHttpSession());
        }
        return (sessionScope);
    }


    public void release() {
        applicationScope = null;
        facesContext = null;
        requestScope = null;
        sessionScope = null;
	header = null;
	headerValues = null;
	param = null;
	paramValues = null;
	initParam = null;
	cookie = null;
    }


    boolean isImplicit(String name) {
     
        for (int i = 0; i < implicits.length; i++) {
            if (name.equals(implicits[i])) {
                return (true);
            }
        }
        return (false);
    }

    private boolean isReadOnly(String name) {
	for (int i = 0; i < LAST_READONLY_IMPLICIT_INDEX; i++) {
            if (name.equals(implicits[i])) {
		return true;
            }
	}
	return false;
    }


    public Object get(String modelReference) throws FacesException {
	Object result = null;
	try {
	    result = elEvaluator.evaluate(addBracketsIfNecessary(modelReference), 
					  this, Object.class);
	}
	catch (Throwable e) {
	    throw new FacesException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID), e);
	}
	return result;
     }
    
     public Class getModelType (String modelReference) throws FacesException {

         Class modelClass = null;
         Object modelObj = null;
       
         // Extract the actual expression to be evaluated
         String expression = stripBracketsIfNecessary(modelReference);
  
        // Direct access to implicit objects returns them
        if (isImplicit(expression)) {
            try {
                modelClass = PropertyUtils.getPropertyType(this, expression);
                return modelClass;
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
         
        int period = expression.indexOf(".");
        if (period >= 0) {
            // Dotted expression -- explicit or implicit scoped lookup
            Object base = this;
            String first = expression.substring(0, period);
            if (!isImplicit(first)) {
                // not a scoped expression
                base = lookup(first);
                expression = expression.substring(period+1);
               
            } else {
                // in the case of scoped expression, scope name should be
                // removed from the expression. For example if expr is
                // applicationScope.TestBean.pin, remove "applicationScope"
                // Otherwise PropertyUtils would misinterpret TestBean to 
                // a property in applicationScope bean.
                String withoutFirst = expression.substring(period+1); 
                // check if withoutFirst directly references a bean. 
                period = withoutFirst.indexOf(".");
                if ( period != -1 ) {
                    // expre refers to nested property.
                    // for the above example, baseName would be TestBean
                    // necessary to get base here for reasons explained above.
                    String baseName = withoutFirst.substring(0, period);
                    base = get(baseName);
                    // expression would refer to a property within bean.
                    // ex: pin
                    expression = withoutFirst.substring(period+1);
                } else {
                    // expr refers directly to a bean in scope object.
                    // ex: applicationScope.TestBean. Then return modelType
                    // directly by using getClass.
                    base = get(expression);
                    if ( base != null ) {
                        modelClass = base.getClass();
                        return modelClass;
                    }  
                }    
            }    
            try {
                modelClass = PropertyUtils.getPropertyType(base, expression);
            } catch (Exception e) {
                throw new FacesException(e);
            }
        } else {
            // Simple expression -- scoped lookup
            modelObj =  lookup(expression);
            if ( modelObj != null ) {
                modelClass = modelObj.getClass();
            }          
        }
        return modelClass; 
    }


    public void set(String modelReference, Object value)
        throws FacesException {
	if (null == modelReference) {
	    throw new NullPointerException();
	}
	
        String expression = stripBracketsIfNecessary(modelReference);
	boolean isReadOnly = false;
	int period = expression.indexOf(".");
        if (period >= 0) {
            // Dotted expression -- explicit or implicit scoped lookup
	    isReadOnly = isReadOnly(expression.substring(0, period));
	    
	}
	else {
	    isReadOnly = isReadOnly(expression);
	}
	if (isReadOnly) {
	    String params[] = new String[1];
	    params[0] = modelReference;
	    throw new 
		IllegalArgumentException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID,
								  params));
	}
	try {
	    elEvaluator.evaluate(addBracketsIfNecessary(modelReference), 
				 value, this, Object.class);
	    return;
	}
	catch (Throwable e) {
	    throw new FacesException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID), e);
	}
    }


    private String stripBracketsIfNecessary(String modelReference) {
        if (modelReference == null) {
            throw new NullPointerException();
        }
        String result = modelReference;
        if (result.startsWith("${") &&
            result.endsWith("}")) {
            result = modelReference.substring(2, modelReference.length() - 1);
        }
        return (result);
    }

    private String addBracketsIfNecessary(String modelReference) {
        if (modelReference == null) {
            throw new NullPointerException();
        }
        String result = modelReference;
        if (!(result.startsWith("${") &&
	      result.endsWith("}"))) {
            result = "${" + modelReference + "}";
        }
        return (result);
    }

    Object lookup(String name) {
        Object value = null;
        if (value == null) {
            value = facesContext.getServletRequest().getAttribute(name);
        }
        if ((value == null) &&
            (facesContext.getHttpSession() != null)) {
             value = facesContext.getHttpSession().getAttribute(name);
        }
        if (value == null) {
            value = facesContext.getServletContext().getAttribute(name);
        } 
        return (value);
    }
}

class ECResolver extends Object implements VariableResolver {

    //
    // Methods from VariableResolver
    // 

    public Object resolveVariable(String pName,
				   Object pContext) throws ELException {
	Object result = null;

	EvaluationContext eContext = (EvaluationContext) pContext;

	if (eContext.isImplicit(pName)) {
	    // PENDING(edburns): doing string comparisons doesn't seem
	    // quite right here.

	    if (pName.equals("requestScope")) {
		result = eContext.getRequestScope();
	    }	    
	    else if (pName.equals("sessionScope")) {
		result = eContext.getSessionScope();
	    }
	    else if (pName.equals("applicationScope")) {
		result = eContext.getApplicationScope();
	    }
	    else if (pName.equals("param")) {
		// We can't use getServletRequest().getParameterMap();
		// because that Map has keys that are arrays of Strings.
		// We want a map whose keys *are* Strings.
		result = eContext.getParam();
	    }
	    else if (pName.equals("paramValues")) {
		result = eContext.getParamValues();
	    }
	    else if (pName.equals("initParam")) {
		result = eContext.getInitParam();
	    }
	    else if (pName.equals("cookie")) {
		result = eContext.getCookie();
	    }
	    else if (pName.equals("header")) {
		result = eContext.getHeader();
	    }
	    else if (pName.equals("headerValues")) {
		result = eContext.getHeaderValues();
	    }
	    else if (pName.equals("facesContext")) {
		result = eContext.getFacesContext();
	    }

	    else {
		Assert.assert_it(false, "not reached");
	    }
	}
	else if (isThrowException(pName)) {
	    throwException(pName, pContext);
	}
	else {    
	    result = eContext.lookup(pName);
	}
	return result;
    }

    public boolean isThrowException(String pName) {
	boolean result = false;
	if (null != pName) {
	    if (pName.equals("ELEvaluator.throwException")) {
		result = true;
	    }
	}
	return result;
    }
    
    public void throwException(String pName, Object pContext) throws ELException {
	throw new ELException("Can't evaluate");
    }

}


// -------------------------------------------------------- ApplicationScopeMap


class ApplicationScopeMap implements Map {


    public ApplicationScopeMap(ServletContext application) {
        this.application = application;
        ; // FIXME - initialize contents
    }


    ServletContext application = null;

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
        return (application.getAttribute(key.toString()));
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
        Object result = application.getAttribute(keyString);
        application.setAttribute(keyString, value);
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
        Object result = application.getAttribute(keyString);
        application.removeAttribute(keyString);
        return (result);
    }


    public int size() {
        throw new UnsupportedOperationException();
    }


    public Collection values() {
        throw new UnsupportedOperationException();
    }


}


// ------------------------------------------------------------ RequestScopeMap


class RequestScopeMap implements Map {


    public RequestScopeMap(ServletRequest request) {
        this.request = request;
        ; // FIXME - initialize contents
    }


    ServletRequest request = null;


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
        return (request.getAttribute(key.toString()));
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


// ------------------------------------------------------------ SessionScopeMap


class SessionScopeMap implements Map {


    public SessionScopeMap(HttpSession session) {
        this.session = session;
        ; // FIXME - initialize contents
    }


    HttpSession session = null;


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
        return (session.getAttribute(key.toString()));
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

class HeaderMap implements Map {
    protected FacesContext facesContext = null;


    public HeaderMap(FacesContext newFacesContext) {
	facesContext= newFacesContext;
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
        return (((HttpServletRequest)facesContext.getServletRequest()).
		getHeader(keyString));
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

class HeaderValuesMap implements Map {
    protected FacesContext facesContext = null;


    public HeaderValuesMap(FacesContext newFacesContext) {
	facesContext= newFacesContext;
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
	Enumeration headerValues = null;
	ArrayList headerArray = null;
	Object result = null;

	if (null != (headerValues = 
		     ((HttpServletRequest)facesContext.getServletRequest()).
		     getHeaders(keyString))) {
	    headerArray = new ArrayList();
	    while (headerValues.hasMoreElements()) {
		headerArray.add(headerValues.nextElement());
	    }
	    result = new String[headerArray.size()];
	    headerArray.toArray((Object []) result);
	}
        return result;
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

class ParamMap implements Map {
    protected FacesContext facesContext = null;


    public ParamMap(FacesContext newFacesContext) {
	facesContext= newFacesContext;
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
        return (((HttpServletRequest)facesContext.getServletRequest()).
		getParameter(keyString));
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

class ParamValuesMap implements Map {
    protected FacesContext facesContext = null;


    public ParamValuesMap(FacesContext newFacesContext) {
	facesContext= newFacesContext;
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
        return (((HttpServletRequest)facesContext.getServletRequest()).
		getParameterValues(keyString));
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

class InitParamMap implements Map {
    protected ServletContext servletContext = null;

    public InitParamMap(FacesContext newFacesContext) {
	servletContext = newFacesContext.getServletContext();
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

class CookieMap implements Map {
    protected Cookie[] cookies = null;
    protected final int cookieLen;

    public CookieMap(FacesContext newFacesContext) {
	cookies = ((HttpServletRequest)newFacesContext.getServletRequest()).
	    getCookies();
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
