/*
 * $Id: EvaluationContext.java,v 1.1 2002/08/29 00:28:02 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;


import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;


/**
 * <p>Bean to be handed to BeanUtils and PropertyUtils representing the
 * evaluation context for model reference expressions.  The properties
 * represent the implicit objects that are exposed at the top level of
 * the context.</p>
 */

public class EvaluationContext {


    public EvaluationContext(FacesContext facesContext) {
        this.facesContext = facesContext;
    }

    private String implicits[] =
    { "applicationScope", "facesContext", "requestScope", "sessionScope" };


    private Map applicationScope = null;
    public Map getApplicationScope() {
        if (applicationScope == null) {
            applicationScope =
                new ApplicationScopeMap(facesContext.getServletContext());
        }
        return (applicationScope);
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
    }


    private boolean isImplicit(String name) {
     
        for (int i = 0; i < implicits.length; i++) {
            if (name.equals(implicits[i])) {
                return (true);
            }
        }
        return (false);
    }


    public Object get(String modelReference) throws FacesException {

        // Extract the actual expression to be evaluated
        String expression = expression(modelReference);
       
        // Direct access to implicit objects returns them
        if (isImplicit(expression)) {
            try {
                return (PropertyUtils.getSimpleProperty(this, expression));
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
                base = lookup(first);
                expression = expression.substring(period + 1);
            }
          
            try {
                return (PropertyUtils.getProperty(base, expression));
            } catch (Exception e) {
                throw new FacesException(e);
            }
         } else {
             // Simple expression -- scoped lookup
             return (lookup(expression));
         }
     }
    
     public Class getModelType (String modelReference) throws FacesException {

         Class modelClass = null;
         Object modelObj = null;
         // Extract the actual expression to be evaluated
         String expression = expression(modelReference);
  
        // Direct access to implicit objects returns them
        if (isImplicit(expression)) {
            try {
                modelClass = PropertyUtils.getPropertyType(this, expression);
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
                base = lookup(first);
                expression = expression.substring(period+1);
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

        // Extract the actual expression to be evaluated
        String expression = expression(modelReference);

        // Direct access to implicit objects is prohibited
        if (isImplicit(expression)) {
            throw new IllegalArgumentException(modelReference);
        }

        int period = expression.indexOf(".");
        if (period >= 0) {
            // Dotted expression -- explicit or implicit scoped lookup
            Object base = this;
            String first = expression.substring(0, period);
            if (!isImplicit(first)) {
                base = lookup(first);
                expression = expression.substring(period + 1);
            }
            try {
                PropertyUtils.setProperty(base, expression, value);
            } catch (Exception e) {
                throw new FacesException(e);
            }
        } else {
            // Simple expression -- store as request attribute
            facesContext.getServletRequest().setAttribute(expression, value);
        }
    }


    private String expression(String modelReference) {
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


    private Object lookup(String name) {
        
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
