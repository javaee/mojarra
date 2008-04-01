/*
 * $Id: FacesContextImpl.java,v 1.21 2002/08/08 00:46:13 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Locale;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import javax.faces.FacesException;     
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import javax.faces.context.Message;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.tree.Tree;
import javax.faces.FactoryFinder;
import javax.faces.event.FacesEvent;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.ViewHandler;

import javax.faces.context.MessageResourcesFactory;
import javax.faces.context.MessageResources;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.BeanUtils;

import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.util.Util;

public class FacesContextImpl extends FacesContext
{
    
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private ServletContext servletContext = null;
    private Locale locale = null;
    private ServletRequest request = null;
    private Tree requestTree = null;
    private ServletResponse response = null;
    private Tree responseTree = null;
    private ResponseStream responseStream = null;
    private ResponseWriter responseWriter = null;
    private HttpSession session = null;
    private ArrayList applicationEvents = null;
    private HashMap requestEvents = null;
    private int requestEventsCount = 0;

    /**

    * Store mapping of UIComponent instance to ArrayList of Message
    * instances.  The null key is used to represent Message instances
    * that are not associated with a UIComponent instance.

    */

    private HashMap messageLists = null;
    private ViewHandler viewHandler = null;
    private ApplicationHandler applicationHandler = null;
    
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    // 
    public FacesContextImpl(ServletContext sc, ServletRequest request,
			    ServletResponse response, Lifecycle lifecycle)
        throws FacesException {
        
        try {
            ParameterCheck.nonNull(sc);
            ParameterCheck.nonNull(request);
            ParameterCheck.nonNull(response);
            ParameterCheck.nonNull(lifecycle);
        } catch (Exception e ) {
            throw new FacesException(Util.getExceptionMessage(Util.FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID));
        }
        
        this.servletContext = sc;
        this.request = request;
        this.response = response;
        this.locale = request.getLocale();
	// PENDING(edburns): don't depend on the session being there!
        if (this.request instanceof HttpServletRequest) {
            this.session =
                ((HttpServletRequest) request).getSession();
        }
        this.viewHandler = lifecycle.getViewHandler();
        this.applicationHandler = lifecycle.getApplicationHandler();
        
    }

    //
    // Class methods
    //

    //
    // General Methods
    
    //
    //
    // Methods from FacesContext
    //
    public Iterator getApplicationEvents() {
        if (applicationEvents != null) {
            return (applicationEvents.iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }
    }

    public int getApplicationEventsCount() {
	if (null == applicationEvents) {
	    return 0;
	}
	return applicationEvents.size();
    }


    public HttpSession getHttpSession() {
        return (this.session);
    }
    
    public Locale getLocale() {
        return (this.locale);
    }


    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getMaximumSeverity() {
        Iterator outerIter = null, innerIter = null;
        int max = 0;
        ArrayList list = new ArrayList();
        
        if (null == messageLists) {
             return max;
        }
	// Get an Iterator over the ArrayList instances
	outerIter = messageLists.values().iterator();
        while ( outerIter.hasNext()) {
            list = (ArrayList) outerIter.next();
	    // Get an Iterator over the elements of the current
	    // ArrayList
	    innerIter = list.iterator();
	    while(innerIter.hasNext()) {
		Message msg = (Message)innerIter.next();
		if (msg.getSeverity() > max) {
		    max = msg.getSeverity();
		}    
	    }
        }
        return max;
    }    

    public Iterator getMessages() {
        Iterator listsIter = null, result = null;
        ArrayList list = new ArrayList();
        
        if (null == messageLists) {
             return (Collections.EMPTY_LIST.iterator());
        }

	// Get an Iterator over the ArrayList instances
	listsIter = messageLists.values().iterator();
        while ( listsIter.hasNext()) {
	    list.addAll((ArrayList) listsIter.next());
	}

	if (list.size() > 0 ) {
            result = list.iterator();
	} else {
            result = Collections.EMPTY_LIST.iterator();
	}
        return result;
    }

    public Iterator getMessages(UIComponent component) {
        Iterator result = null;
        ArrayList list = null;

        if (null == messageLists) {
            result = Collections.EMPTY_LIST.iterator();
        }
	else {
	    // Look up the ArrayList for this (possibly null) component
	    if (null != (list = (ArrayList) messageLists.get(component))) {
		if (list.size() > 0 ) {
		    result = list.iterator();
		} else {
		    result = Collections.EMPTY_LIST.iterator();
		}
	    }
	    else {
		result = Collections.EMPTY_LIST.iterator();
	    }
        }
        return result;
    }

    public Iterator getRequestEvents(UIComponent component) {
	Iterator result = null;
        ArrayList list = null;

	if (null == component) {
	    throw new NullPointerException(Util.getExceptionMessage(Util.NULL_COMPONENT_ERROR_MESSAGE_ID));
	}

        if (null == requestEvents) {
            result = Collections.EMPTY_LIST.iterator();
        }
	else {
	    list = (ArrayList) requestEvents.get(component);
	    if (null != list) {
		result = list.iterator();
	    } else {
		result = Collections.EMPTY_LIST.iterator();
	    }
        }

	return result;
    }

    public int getRequestEventsCount() {
	return requestEventsCount;
    }

    public int getRequestEventsCount(UIComponent component) {
	int result = 0;
        ArrayList list = null;

	if (null == component) {
	    throw new NullPointerException(Util.getExceptionMessage(Util.NULL_COMPONENT_ERROR_MESSAGE_ID));
	}

        if (null != requestEvents) {
	    list = (ArrayList) requestEvents.get(component);
	    if (null != list) {
		result = list.size();
	    } 
        }
	
	return result;
    }

    public Tree getRequestTree() {
        return (this.requestTree);
    }

    public ResponseStream getResponseStream() {
	return responseStream;
    }

    public void setResponseStream(ResponseStream newResponseStream) {
        if (newResponseStream == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_RESPONSE_STREAM_ERROR_MESSAGE_ID));
        }
	responseStream = newResponseStream;
    }

    public void setRequestTree(Tree requestTree) {
        if (requestTree == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_REQUEST_TREE_ERROR_MESSAGE_ID));
        }
        if (this.requestTree != null) {
            throw new IllegalStateException(Util.getExceptionMessage(Util.REQUEST_TREE_ALREADY_SET_ERROR_MESSAGE_ID));
        }
        this.requestTree = requestTree;
        if (this.responseTree == null) {
            this.responseTree = this.requestTree;
        }
    }

    public Tree getResponseTree() {
        return (this.responseTree);
    }


    public void setResponseTree(Tree responseTree) {
        if (responseTree == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_RESPONSE_TREE_ERROR_MESSAGE_ID));
        }
        this.responseTree = responseTree;
    }

    public ResponseWriter getResponseWriter() {
	return responseWriter;
    }

    public void setResponseWriter(ResponseWriter newResponseWriter) {
        if (newResponseWriter == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_RESPONSE_WRITER_ERROR_MESSAGE_ID));
        }
	responseWriter = newResponseWriter;
    }

    public ServletContext getServletContext() {
        return (this.servletContext);
    }


    public ServletRequest getServletRequest() {
        return (this.request);
    }


    public ServletResponse getServletResponse() {
        return (this.response);
    }


    public void addApplicationEvent(FacesEvent event) {
        if (event == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_EVENT_ERROR_MESSAGE_ID));
        }
        if (applicationEvents == null) {
            applicationEvents = new ArrayList();
        }
        applicationEvents.add(event);
    }

    public void addMessage(Message message) {
        ArrayList list = null;
        if ( message == null ) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_MESSAGE_ERROR_MESSAGE_ID));
        }
        if (null == messageLists) {
            messageLists = new HashMap();
        }
        
        list = (ArrayList) messageLists.get(null);
        if (list == null) {
            list = new ArrayList();
            messageLists.put(null, list);
        }
        list.add(message);  
    }

    public void addMessage(UIComponent component, Message message) {
        ArrayList list = null;
        if ( null == message ) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (null == messageLists) {
            messageLists = new HashMap();
        }
        
        list = (ArrayList) messageLists.get(component);
        if (list == null) {
            list = new ArrayList();
            messageLists.put(component, list);
        }
        list.add(message);
    }

    public void addRequestEvent(UIComponent component, FacesEvent event) {
        ArrayList list = null;

	if (null == component || null == event) {
	    throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}

        if (null == requestEvents) {
            requestEvents = new HashMap();
        }

        list = (ArrayList) requestEvents.get(component);
        if (list == null) {
            list = new ArrayList();
            requestEvents.put(component, list);
        }
        list.add(event);
        requestEventsCount++;
    } 
    
    /**
     * POSTCONDITION: Class type of the property is returned as identified by  
     * the model reference string.
     *
     * @param request ServletRequest object representing the client request
     * @param objectReference A string referencing a bean's property.
     *
     * @exception FacesException If the model bean identified by the
     *     model reference string cannot be found in the any scope,
     *     or the property value could not be retrieved.
     * @exception NullPointerException if model argument is <code>null</code>
     */
    public Class getModelType(String expression) throws FacesException {
        
        if (expression == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        String property = null;
        String baseName = null;
        Object object = null;
        Class returnClass = null;
        
        if (expression.startsWith("${") && expression.endsWith("}")) {
	    expression = expression.substring(2, expression.length() - 1);
	}
	// if it is not a nested property, then it directly references
	// a model object. So there should be a model bean existing
	// in the one of the scopes with this name.
	if ( expression.indexOf(".") == -1 ) {
	    // PENDING (visvan) temporary: assume the model bean is stored
	    // in session
	    object = getObjectFromScope(expression);
	    if (object == null) {
		Object [] params = {expression};
		throw new FacesException(Util.getExceptionMessage(Util.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID,
								  params));
	    }
	    returnClass = object.getClass();
	} else {    
	    property = expression.substring((expression.indexOf(".")+1));
	    baseName = expression.substring(0, expression.indexOf("."));
	    // PENDING (visvan) temporary: assume the model bean is stored
	    // in session
	    object = getObjectFromScope(baseName);
	    if (object == null) {
		throw new FacesException("Named Object: '"+baseName+
					 "' not found in.");
	    }
	    try {
		returnClass = PropertyUtils.getPropertyType(object, 
							    property);
	    } catch (IllegalAccessException iae) {
		throw new FacesException(iae.getMessage());
	    } catch (InvocationTargetException ite) {
		throw new FacesException(ite.getMessage());
	    } catch (NoSuchMethodException nme) {
		throw new FacesException(nme.getMessage());
	    }
	}    
        return returnClass;
    }

    /**
     * POSTCONDITION: An object is returned as identified by  
     * the model reference string.
     *
     * @param request ServletRequest object representing the client request
     * @param objectReference A string referencing a bean's property.
     *
     * @exception FacesException If the model bean identified by the
     *     model reference string cannot be found in the any scope,
     *     or the property value could not be retrieved.
     * @exception NullPointerException if model argument is <code>null</code>
     *
     */
    public Object getModelValue(String expression) throws FacesException {
        
        if (expression == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        String property = null;
        String baseName = null;
        Object object = null;
        Object returnObject = null;
     
        if (expression.startsWith("${") && expression.endsWith("}")) {
	    expression = expression.substring(2, expression.length() - 1);
	}
	// if it is not a nested property, then it directly references
	// a model object. So there should be a model bean existing
	// in the one of scopes with this name.
	if ( expression.indexOf(".") == -1 ) {
	    returnObject = getObjectFromScope(expression);
	    if (returnObject == null) {
		throw new FacesException("Named Object: '"+expression+
					 "' not found.");
	    }
	} else {    
	    property = expression.substring((expression.indexOf(".")+1));
	    baseName = expression.substring(0, expression.indexOf("."));
	    object = getObjectFromScope(baseName);
	    if (object == null) {
		throw new FacesException("Named Object: '"+baseName+
					 "' not found.");
	    }
	    try {
		returnObject = PropertyUtils.getNestedProperty(object, 
							       property);
	    } catch (IllegalAccessException iae) {
		throw new FacesException(iae.getMessage());
	    } catch (InvocationTargetException ite) {
		throw new FacesException(ite.getMessage());
	    } catch (NoSuchMethodException nme) {
		throw new FacesException(nme.getMessage());
	    }
	}    
	return returnObject;
    }

    /**
     * // PENDING (visvan) update Java docs.
     * POSTCONDITION: The property value is set, where the property
     * is identified by the model reference string.  The model bean
     * with the new value is put back into the session scope.
     * This could change in future.
     *
     * @param request ServletRequest object representing the client request
     * @param modelReference A string referencing a bean's property.
     * @param value The value of the property to be set.
     *
     * @exception FacesException If the model bean identified by the
     *     model reference string cannot be found in the any scope,
     *     or the property value could not be set.
     * @exception NullPointerException if model argument is <code>null</code>
     */
    public void setModelValue(String expression, Object value) 
            throws FacesException {
        
        if (expression == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        String property = null;
        String baseName = null;
        Object object = null;
        
       // expression reference string complies with JSTL syntax
        if (expression.startsWith("${") && expression.endsWith("}")) {
	    expression = expression.substring(2, expression.length() - 1);
	}
	// if it is not a nested property, then it directly references
	// a model object. So there should be a model bean existing
	// in one of the scopes with this name.
	if ( expression.indexOf(".") == -1 ) {
	    object = getObjectFromScope(baseName);
	    if (object == null) {
		throw new FacesException("Named Object: '"+expression+
					 "' not found.");
	    }
	    // PENDING (visvan) store model objects in correct scope.
	    object = value;
	    getHttpSession().setAttribute(baseName, object);   
	} else {
	    property = expression.substring((expression.indexOf(".")+1));
	    baseName = expression.substring(0, expression.indexOf("."));
	    // PENDING (visvan) temporary: store model bean in 
	    // correct scope.
	    object = getObjectFromScope(baseName);
	    if (object == null) {
		throw new FacesException("Named Object: '"+baseName+
					 "' not found.");
	    }
	    try {
		PropertyUtils.setNestedProperty(object, property, value);
	    } catch (IllegalAccessException iae) {
		throw new FacesException(iae.getMessage());
	    } catch (InvocationTargetException ite) {
		throw new FacesException(ite.getMessage());
	    } catch (NoSuchMethodException nme) {
		throw new FacesException(nme.getMessage());
	    }
	}    
    }

    /**
     * This method does a narrow to broad search to locate a model
     * bean
     */
    private Object getObjectFromScope(String modelRef) {
        // search request, session and application scope in that order.
        Object modelObj = null;
        modelObj = getServletRequest().getAttribute(modelRef);
        if ( modelObj != null ) {
            return modelObj;
        }
        if (getHttpSession() != null ) {
            modelObj = getHttpSession().getAttribute(modelRef);
            if ( modelObj != null ) {
                return modelObj;
            }
        }    
        modelObj = getServletContext().getAttribute(modelRef);
        return modelObj;
    }    
    
    public void release() {
        request = null;
        response = null;
        servletContext = null;
        session = null;
        locale = null;
        requestTree = null;
        responseTree = null;
        responseStream = null;
        responseWriter = null;
        applicationEvents = null;
        requestEvents = null;
        requestEventsCount = 0;
        viewHandler = null;
        applicationHandler = null;
    }
    
    public ViewHandler getViewHandler() {
        return this.viewHandler;
    }
   
    public ApplicationHandler getApplicationHandler() {
        return this.applicationHandler;
    }
    
    // The testcase for this class is TestFacesContextImpl.java 
    // The testcase for this class is TestFacesContextImpl_Model.java

} // end of class FacesContextImpl
