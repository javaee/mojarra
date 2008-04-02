/*
 * $Id: FacesContextImpl.java,v 1.49 2003/09/18 19:10:30 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.FacesException;     
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.Message;
import javax.faces.application.RepeaterMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.Repeater;
import javax.faces.component.RepeaterSupport;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import javax.faces.event.FacesEvent;
import javax.faces.event.RepeaterEvent;
import javax.faces.lifecycle.Lifecycle;

import org.apache.commons.collections.CursorableLinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.util.Assert;

public class FacesContextImpl extends FacesContext
{
    
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    private static final Log log = LogFactory.getLog(FacesContextImpl.class);

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

    public FacesContextImpl(ExternalContext ec, Lifecycle lifecycle) {
	if (null == ec || null == lifecycle) {
            throw new NullPointerException
                (Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
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

        // If no messages have been enqueued at all,
        // return an empty List Iterator
        if (null == messageLists) {
            return (Collections.EMPTY_LIST.iterator());
        }

        // If no messages have been enqueued for the specified
        // (possibly null) UIComponent, return an empty list iterator
        List list = (List) messageLists.get(component);
        if (null == list) {
            return (Collections.EMPTY_LIST.iterator());
        }

        // If the specified UIComponent was null, just return the iterator
        if (component == null) {
            return (list.iterator());
        }

        // If the specified UIComponent is not nested inside a
        // Repeater, just return the iterator
        Repeater repeater =
            RepeaterSupport.findParentRepeater(component);
        if (repeater == null) {
            return (list.iterator());
        }
        int rowIndex = repeater.getRowIndex();

        // Return only the messages for the relevant rowIndex value
        List results = new ArrayList();
        Iterator items = list.iterator();
        while (items.hasNext()) {
            RepeaterMessage  item = (RepeaterMessage) items.next();
            if (rowIndex != item.getRowIndex()) {
                continue;
            }
            results.add(item.getMessage());
        }
        return (results.iterator());

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
        if (viewRoot != root) {
            facesEvents = null;
        }
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

        // Validate our preconditions
        if (event == null) {
            throw new NullPointerException
                (Util.getExceptionMessage(Util.NULL_EVENT_ERROR_MESSAGE_ID));
        }

        // If the source component is a descendant of a Repeater,
        // wrap this event so we can restore the rowIndex later
        Repeater repeater =
            RepeaterSupport.findParentRepeater(event.getComponent());
        if (repeater != null) {
            event = new RepeaterEvent((UIComponent) repeater, event,
                                      repeater.getRowIndex());
        }

        // Add this event to our internal queue
        if (facesEvents == null) {
            facesEvents = new CursorableLinkedList();
        }
        facesEvents.add(event);
        if (log.isDebugEnabled()) {
            if (event instanceof RepeaterEvent) {
                FacesEvent actual = ((RepeaterEvent) event).getFacesEvent();
                String id = actual.getComponent().getId();
                if (id == null) {
                    id = "<<NONE>>";
                }
                log.debug
                    ("Adding RepeaterEvent[sourceId=" + id +
                     ",type=" + actual.getClass().getName() +
                     ",rowIndex=" + ((RepeaterEvent) event).getRowIndex() +
                     "]");
            } else {
                String id = event.getComponent().getId();
                if (id == null) {
                    id = "<<NONE>>";
                }
                log.debug("Adding FacesEvent[sourceId=" + id +
                          ",type=" + event.getClass().getName());
            }
        }

    }

    public void addMessage(UIComponent component, Message message) {

        // Validate our preconditions
        if ( null == message ) {
            throw new NullPointerException
                (Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // If the relevant component is a descendant of a Repeater,
        // wrap the message so we can restore the rowIndex later
        Repeater repeater = null;
        if (component != null) {
            repeater = RepeaterSupport.findParentRepeater(component);
        }
        if (repeater != null) {
            message = new RepeaterMessage(message,
                                          repeater.getRowIndex());
        }

        // Add this message to our internal queue
        if (null == messageLists) {
            messageLists = new HashMap();
        }
        List list = (List) messageLists.get(component);
        if (list == null) {
            list = new ArrayList();
            messageLists.put(component, list);
        }
        list.add(message);
        if (log.isDebugEnabled()) {
            if (repeater != null) {
                log.debug("Adding Message[sourceId=" + component.getId() +
                          ",summary=" + message.getSummary() +
                          ",rowIndex=" + repeater.getRowIndex() +
                          ")");
            } else {
                log.debug("Adding Message[sourceId=" +
                          (component != null ? component.getId() : "<<NONE>>") +
                          ",summary=" + message.getSummary() + ")");
            }
        }

    }

    public void release() {
        externalContext = null;
        locale = null;        
        responseStream = null;
        responseWriter = null;
        facesEvents = null;
        messageLists = null;
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
