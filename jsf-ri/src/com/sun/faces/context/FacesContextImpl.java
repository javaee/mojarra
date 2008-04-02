/*
 * $Id: FacesContextImpl.java,v 1.31 2003/02/20 22:48:35 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Locale;
import java.util.Properties;
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
import javax.faces.event.ApplicationEvent;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.ViewHandler;

import javax.faces.context.MessageResourcesFactory;
import javax.faces.context.MessageResources;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CursorableLinkedList;

import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import com.sun.faces.renderkit.FormatPool;

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
    private Tree tree = null;
    private ServletResponse response = null;
    private Tree responseTree = null;
    private ResponseStream responseStream = null;
    private ResponseWriter responseWriter = null;
    private HttpSession session = null;
    private ArrayList applicationEvents = null;
    private CursorableLinkedList facesEvents = null;
    private EvaluationContext evaluationContext = null;

    /**

    * Store mapping of UIComponent instance to ArrayList of Message
    * instances.  The null key is used to represent Message instances
    * that are not associated with a UIComponent instance.

    */

    private HashMap messageLists = null;
    private ViewHandler viewHandler = null;
    private ApplicationHandler applicationHandler = null;
    
    private boolean renderResponse = false;
    private boolean responseComplete = false;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    // 
    public FacesContextImpl() {
    }

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

	// Verify the FormatPool is in the ServletContext

	// PENDING(edburns): when we have a startup/shutdown hook,
	// remove this from the ServletContext.
	FormatPool formatPool = null;
	if (null == (formatPool = (FormatPool)
		  getServletContext().getAttribute(RIConstants.FORMAT_POOL))){
	    getServletContext().setAttribute(RIConstants.FORMAT_POOL,
			         new com.sun.faces.renderkit.FormatPoolImpl());
	}

        setCurrentInstance(this);
        
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

    public Iterator getFacesEvents() {
        if (facesEvents != null) {
            return (facesEvents.cursor());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }
    }

    public HttpSession getHttpSession() {
        return (this.session);
    }
    
    public Locale getLocale() {
        return (this.locale);
    }


    public void setLocale(Locale locale) {
        this.locale = locale;
        // update the JSTL configuration parameter with the new locale instance,
        // so that the new LocalizationContext that gets created when the setBundle
        // tag is processed is based on the modified locale.
        javax.servlet.jsp.jstl.core.Config.set(getServletRequest(),
                javax.servlet.jsp.jstl.core.Config.FMT_LOCALE, locale);
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

    public Tree getTree() {
        return (this.tree);
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

    public void setTree(Tree tree) {
        if (tree == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_REQUEST_TREE_ERROR_MESSAGE_ID));
        }
        this.tree = tree;
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


    public void addApplicationEvent(ApplicationEvent event) {
        if (event == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_EVENT_ERROR_MESSAGE_ID));
        }
        if (applicationEvents == null) {
            applicationEvents = new ArrayList();
        }
        applicationEvents.add(event);
    }

    public void addFacesEvent(FacesEvent event) {
        if (event == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_EVENT_ERROR_MESSAGE_ID));
        }
        if (facesEvents == null) {
            facesEvents = new CursorableLinkedList();
        }
        facesEvents.add(event);
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

    private EvaluationContext getEvaluationContext() {
        if (evaluationContext == null) {
            evaluationContext = new EvaluationContext(this);
        }
        return (evaluationContext);
    }
    
    /**
     * POSTCONDITION: Class type of the property is returned as identified by  
     * the model reference string.
     *
     * @param modelReference A string referencing a bean's property.
     *
     * @exception FacesException If the model bean identified by the
     *     model reference string cannot be found in the any scope,
     *     or the property value could not be retrieved.
     * @exception NullPointerException if model argument is <code>null</code>
     */
    public Class getModelType(String modelReference) throws FacesException {
        if (modelReference == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        return (getEvaluationContext().getModelType(modelReference));
    } 
    
    /**
     * POSTCONDITION: An object is returned as identified by  
     * the model reference string.
     *
     * @param modelReference A string referencing a bean's property.
     *
     * @exception FacesException If the model bean identified by the
     *     model reference string cannot be found in the any scope,
     *     or the property value could not be retrieved.
     * @exception NullPointerException if model argument is <code>null</code>
     *
     */
    public Object getModelValue(String modelReference) throws FacesException {
        if (modelReference == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        Object value = getEvaluationContext().get(modelReference);
        return (value);
    }

    /**
     * 
     * POSTCONDITION: The property value is set, where the property
     * is identified by the model reference string.  
     * @param modelReference A string referencing a bean's property.
     * @param value The value of the property to be set.
     *
     * @exception FacesException If the model bean identified by the
     *     model reference string cannot be found in the any scope,
     *     or the property value could not be set.
     * @exception NullPointerException if model argument is <code>null</code>
     */
    public void setModelValue(String modelReference, Object value) 
            throws FacesException {
        if (modelReference == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        getEvaluationContext().set(modelReference, value);
    }
    
    public void release() {
        request = null;
        response = null;
        servletContext = null;
        session = null;
        locale = null;
        tree = null;
        responseStream = null;
        responseWriter = null;
        applicationEvents = null;
        facesEvents = null;
        viewHandler = null;
        applicationHandler = null;
        renderResponse = false;
        responseComplete = false;
    }

    public void renderResponse() {
        renderResponse = true;
    }

    public void responseComplete() {
        responseComplete = true;
    }

    public boolean getRenderResponse() {
        return renderResponse;
    }

    public boolean getResponseComplete() {
        return responseComplete;
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
