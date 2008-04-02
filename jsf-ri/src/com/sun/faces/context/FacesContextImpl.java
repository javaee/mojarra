/*
 * $Id: FacesContextImpl.java,v 1.56 2003/10/21 16:41:47 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import com.sun.faces.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
     
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.Message;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import javax.faces.event.FacesEvent;
import javax.faces.event.RepeaterEvent;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletRequest;

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
    private boolean released;

    // Relationship Instance Variables
    private ResponseStream responseStream = null;
    private ResponseWriter responseWriter = null;
    private CursorableLinkedList facesEvents = null;
    private ExternalContext externalContext = null;
    private Application application = null; 
    private UIViewRoot viewRoot = null;

    /**

    * Store mapping of clientId to ArrayList of Message
    * instances.  The null key is used to represent Message instances
    * that are not associated with a clientId instance.

    */
    private Map componentMessageLists;       

    
    
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
        assertNotReleased();
        return externalContext;
    }

    /**
     * <p>Return the {@link Application} instance associated with this
     * web application.</p>
     */
    public Application getApplication() {
        assertNotReleased();
	if (null != application) {
	    return application;
	}
        ApplicationFactory aFactory = 
	    (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = aFactory.getApplication();
	Assert.assert_it(null != application);
	return application;
    }

    public Iterator getClientIdsWithMessages() {
	Iterator result = null;
	if (null == componentMessageLists) {
	    result = Collections.EMPTY_LIST.iterator();
	}
	else {
	    result = componentMessageLists.keySet().iterator();

	}
	return result;
    }

    public Iterator getFacesEvents() {
        assertNotReleased();
        if (facesEvents != null) {
            return (facesEvents.cursor());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }
    }

    public int getMaximumSeverity() {
        assertNotReleased();        
        int max = 0;       
        
        if (null == componentMessageLists) {
             return max;
        }
	    // Get an Iterator over the ArrayList instances
        List messages = getMergedMessageLists();
        for (int i = 0, size = getMergedMessageLists().size(); i < size; i++) {            
            int severity = ((Message) messages.get(i)).getSeverity();
            if (severity > max) {
                max = severity;
            }

            if (severity == Message.SEVERITY_FATAL) {
                break;
            }
        }	   
        return max;
    }    

    public Iterator getMessages() {
        assertNotReleased();        
        if (null == componentMessageLists) {
            return (Collections.EMPTY_LIST.iterator());
        }

        // Get an Iterator over the ArrayList instances
        List messages = getMergedMessageLists();
        if (messages.size() > 0) {
            return messages.iterator();
        } else {
            return Collections.EMPTY_LIST.iterator();
        }           
    }

    public Iterator getMessages(String clientId) {
        assertNotReleased();
        // If no messages have been enqueued at all,
        // return an empty List Iterator
        if (null == componentMessageLists) {
            return (Collections.EMPTY_LIST.iterator());
        }
        
	List list = (List) componentMessageLists.get(clientId);
	if (list == null) {
	    return (Collections.EMPTY_LIST.iterator());
	}
	return (list.iterator());
    }    

    public ResponseStream getResponseStream() {
        assertNotReleased();
	    return responseStream;
    }

    public void setResponseStream(ResponseStream newResponseStream) {
        assertNotReleased();
        if (newResponseStream == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_RESPONSE_STREAM_ERROR_MESSAGE_ID));
        }
	responseStream = newResponseStream;
    }

    public UIViewRoot getViewRoot() {
        assertNotReleased();
	    return viewRoot;
    }
   
    public void setViewRoot(UIViewRoot root) {
        assertNotReleased();
        if (viewRoot != root) {
            facesEvents = null;
        }
	    viewRoot = root;
    }
    

    public ResponseWriter getResponseWriter() {
        assertNotReleased();
	    return responseWriter;
    }

    public void setResponseWriter(ResponseWriter newResponseWriter) {
        assertNotReleased();
        if (newResponseWriter == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_RESPONSE_WRITER_ERROR_MESSAGE_ID));
        }
	    responseWriter = newResponseWriter;
    }

    public void addFacesEvent(FacesEvent event) {
        assertNotReleased();
        // Validate our preconditions
        if (event == null) {
            throw new NullPointerException
                (Util.getExceptionMessage(Util.NULL_EVENT_ERROR_MESSAGE_ID));
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

    public void addMessage(String clientId, Message message) {
        assertNotReleased();
        // Validate our preconditions
        if ( null == message ) {
            throw new NullPointerException
                (Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

	if (componentMessageLists == null) {
	    componentMessageLists = new HashMap();
	}
	
	// Add this message to our internal queue
	List list = (List) componentMessageLists.get(clientId);
	if (list == null) {
	    list = new ArrayList();
	    componentMessageLists.put(clientId, list);
	}
	list.add(message);
              
        if (log.isDebugEnabled()) {
	    log.debug("Adding Message[sourceId=" +
		      (clientId != null ? clientId : "<<NONE>>") +
		      ",summary=" + message.getSummary() + ")");
        }

    }

    public void release() {
        released = true;
        externalContext = null;
        responseStream = null;
        responseWriter = null;
        facesEvents = null;
        componentMessageLists = null;
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
        assertNotReleased();
        renderResponse = true;
    }

    public void responseComplete() {
        assertNotReleased();
        responseComplete = true;
    }

    public boolean getRenderResponse() {
        assertNotReleased();
        return renderResponse;
    }

    public boolean getResponseComplete() {
        assertNotReleased();
        return responseComplete;
    }  
    
    //
    // Private methods
    //
    private void assertNotReleased() {
        if (released) {
            throw new IllegalStateException();                   
        }    
    }
    
    private List getMergedMessageLists() {
        List mergedList = new ArrayList();
        if (componentMessageLists != null) {
            for (Iterator i = componentMessageLists.values().iterator(); i.hasNext(); ) {
                for (Iterator ii = ((ArrayList) i.next()).iterator(); ii.hasNext(); )
                mergedList.add(ii.next());
            }            
        }        
        return mergedList;
    }
   
    // The testcase for this class is TestFacesContextImpl.java 
    // The testcase for this class is TestFacesContextImpl_Model.java

} // end of class FacesContextImpl
