/*
 * $Id: FacesContextImpl.java,v 1.52 2003/09/24 18:25:17 rlubke Exp $
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
import java.util.Locale;
import java.util.Map;
     
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
import javax.servlet.jsp.jstl.core.Config;
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
    private List globalMessages;
    private Map componentMessageLists;   
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

    public Iterator getComponentsWithMessages() {
        assertNotReleased();
        if (null == componentMessageLists) {
            return Collections.EMPTY_LIST.iterator();
        } else {
            return componentMessageLists.keySet().iterator();
        }
    }


    public Iterator getFacesEvents() {
        assertNotReleased();
        if (facesEvents != null) {
            return (facesEvents.cursor());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }
    }

    public Locale getLocale() {
        assertNotReleased();
        return (this.locale);
    }


    public void setLocale(Locale locale) {
        assertNotReleased();
        this.locale = locale;
        // update the JSTL configuration parameter with the new locale instance,
        // so that the new LocalizationContext that gets created when the setBundle
        // tag is processed is based on the modified locale.
        Config.set((ServletRequest) externalContext.getRequest(), Config.FMT_LOCALE, locale);
    }

    public int getMaximumSeverity() {
        assertNotReleased();        
        int max = 0;       
        
        if (null == componentMessageLists && null == globalMessages) {
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
        if (null == componentMessageLists && null == globalMessages) {
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

    public Iterator getMessages(UIComponent component) {
        assertNotReleased();
        // If no messages have been enqueued at all,
        // return an empty List Iterator
        if (null == componentMessageLists && null == globalMessages) {
            return (Collections.EMPTY_LIST.iterator());
        }
        
        // if the component specified is null, return messages not associated
        // with a component, i.e. the global messages.
        if (component == null) {
            return globalMessages.iterator();
        } else {
            List list = (List) componentMessageLists.get(component);
            if (list == null) {
                return (Collections.EMPTY_LIST.iterator());
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
                RepeaterMessage item = (RepeaterMessage) items.next();
                if (rowIndex != item.getRowIndex()) {
                    continue;
                }
                results.add(item.getMessage());
            }
            return (results.iterator());
        }       
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
        assertNotReleased();
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
            
            if (repeater != null) {
                message = new RepeaterMessage(message,
                        repeater.getRowIndex());
            }
            
            if (componentMessageLists == null) {
                componentMessageLists = new HashMap();
            }
            
            // Add this message to our internal queue
            List list = (List) componentMessageLists.get(component);
            if (list == null) {
                list = new ArrayList();
                componentMessageLists.put(component, list);
            }
            list.add(message);
        } else {
            // component was null, add to the global message queue
            if (globalMessages == null) {
                globalMessages = new ArrayList();
            }
            globalMessages.add(message);
        }
              
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
        released = true;
        externalContext = null;
        locale = null;        
        responseStream = null;
        responseWriter = null;
        facesEvents = null;
        componentMessageLists = null;
        globalMessages = null;
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

    public ViewHandler getViewHandler() {
        assertNotReleased();
        return this.viewHandler;
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
        if (globalMessages != null) {
            mergedList.addAll(globalMessages);
        }
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
