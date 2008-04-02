/*
 * $Id: FacesContextImpl.java,v 1.45 2003/08/26 18:37:01 horwat Exp $
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

import javax.faces.FacesException;     
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.FactoryFinder;
import javax.faces.application.Message;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.event.FacesEvent;

import org.apache.commons.collections.CursorableLinkedList;

import org.mozilla.util.ParameterCheck;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import org.mozilla.util.Assert;

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

    // Relationship Instance Variables
    private Locale locale = null;    
    private ResponseStream responseStream = null;
    private ResponseWriter responseWriter = null;
    private CursorableLinkedList facesEvents = null;
    private ExternalContext externalContext = null;
    private Application application = null; 
    private UIViewRoot viewRoot = null;

    /**

    * Store mapping of UIComponent instance to ArrayList of Message
    * instances.  The null key is used to represent Message instances
    * that are not associated with a UIComponent instance.

    */

    private HashMap messageLists = null;
    private ViewHandler viewHandler = null;

    
    
    // Attribute Instance Variables

    private boolean renderResponse = false;
    private boolean responseComplete = false;



    //
    // Constructors and Initializers    
    // 
    public FacesContextImpl() {
    }

    public FacesContextImpl(ExternalContext ec, Lifecycle lifecycle)
        throws FacesException {
        
        try {
            ParameterCheck.nonNull(ec);
            ParameterCheck.nonNull(lifecycle);
        } catch (Exception e ) {
            throw new FacesException(Util.getExceptionMessage(Util.FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID));
        }
        
        this.externalContext = ec;
        this.locale = externalContext.getRequestLocale();
         
        this.viewHandler = this.getApplication().getViewHandler();
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

    public ExternalContext getExternalContext() {
        return externalContext;
    }

    /**
     * <p>Return the {@link Application} instance associated with this
     * web application.</p>
     */
    public Application getApplication() {
	if (null != application) {
	    return application;
	}
        ApplicationFactory aFactory = 
	    (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = aFactory.getApplication();
	Assert.assert_it(null != application);
	return application;
    }

    public Iterator getComponentsWithMessages() {
	Iterator result = null;

	if (null == messageLists) {
	    result = Collections.EMPTY_LIST.iterator();
	}
	else {
	    result = messageLists.keySet().iterator();
	}

        return result;
    }


    public Iterator getFacesEvents() {
        if (facesEvents != null) {
            return (facesEvents.cursor());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }
    }

    public Locale getLocale() {
        return (this.locale);
    }


    public void setLocale(Locale locale) {
        this.locale = locale;
        // update the JSTL configuration parameter with the new locale instance,
        // so that the new LocalizationContext that gets created when the setBundle
        // tag is processed is based on the modified locale.
        javax.servlet.jsp.jstl.core.Config.set((javax.servlet.ServletRequest) externalContext.getRequest(),
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

    public ResponseStream getResponseStream() {
	return responseStream;
    }

    public void setResponseStream(ResponseStream newResponseStream) {
        if (newResponseStream == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_RESPONSE_STREAM_ERROR_MESSAGE_ID));
        }
	responseStream = newResponseStream;
    }

    public UIViewRoot getViewRoot() {
	return viewRoot;
    }
   
    public void setViewRoot(UIViewRoot root) {
	viewRoot = root;
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

    public void release() {
        externalContext = null;
        locale = null;        
        responseStream = null;
        responseWriter = null;
        facesEvents = null;
        viewHandler = null;
        renderResponse = false;
        responseComplete = false;
	viewRoot = null;

	// PENDING(edburns): write testcase that verifies that release
	// actually works.  This will be important to keep working as
	// ivars are added and removed on this class over time.

	// Make sure to clear our ThreadLocal instance.
	setCurrentInstance(null);
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
   
    // The testcase for this class is TestFacesContextImpl.java 
    // The testcase for this class is TestFacesContextImpl_Model.java

} // end of class FacesContextImpl
