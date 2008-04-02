/*
 * $Id: MockFacesContext.java,v 1.11 2003/07/29 00:42:34 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.Application;
import javax.faces.application.Message;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.event.FacesEvent;
import javax.faces.lifecycle.Lifecycle;


// Mock Object for FacesContext
public class MockFacesContext extends FacesContext {


    // ------------------------------------------------------------ Constructors

    public MockFacesContext() {
        super();
        setCurrentInstance(this);
    }


    public MockFacesContext(ExternalContext externalContext) {
        setExternalContext(externalContext);
        setCurrentInstance(this);
    }


    public MockFacesContext(ExternalContext externalContext, Lifecycle lifecycle) {
        this(externalContext);
    }


    // -------------------------------------------------------------- Properties


    // application
    private Application application = null;
    public Application getApplication() {
        return (this.application);
    }
    public void setApplication(Application application) {
        this.application = application;
    }


    // componentsWithMessages
    public Iterator getComponentsWithMessages() {
        return (messages.keySet().iterator());
    }


    // externalContext
    private ExternalContext externalContext = null;
    public ExternalContext getExternalContext() {
        return (this.externalContext);
    }
    public void setExternalContext(ExternalContext externalContext) {
        this.externalContext = externalContext;
    }


    // facesEvents()
    private List facesEvents = new ArrayList();
    public Iterator getFacesEvents() {
        return (facesEvents.iterator());
    }


    // locale
    private Locale locale = null;
    public Locale getLocale() {
        return (this.locale);
    }
    public void setLocale(Locale locale) {
        this.locale = locale;
    }


    // maximumSeverity
    public int getMaximumSeverity() {
        throw new UnsupportedOperationException();
    }


    // messages
    private Map messages = new HashMap();
    public Iterator getMessages() {
        ArrayList results = new ArrayList();
        Iterator components = messages.keySet().iterator();
        while (components.hasNext()) {
            UIComponent component = (UIComponent) components.next();
            results.addAll((List) messages.get(component));
        }
        return (results.iterator());
    }
    public Iterator getMessages(UIComponent component) {
        List list = (List) messages.get(component);
        if (list == null) {
            list = new ArrayList();
        }
        return (list.iterator());
    }


    // renderResponse
    private boolean renderResponse = false;
    public boolean getRenderResponse() {
        return (this.renderResponse);
    }


    // responseComplete
    private boolean responseComplete = false;
    public boolean getResponseComplete() {
        return (this.responseComplete);
    }


    // responseStream
    private ResponseStream responseStream = null;
    public ResponseStream getResponseStream() {
        return (this.responseStream);
    }
    public void setResponseStream(ResponseStream responseStream) {
        this.responseStream = responseStream;
    }


    // responseWriter
    private ResponseWriter responseWriter = null;
    public ResponseWriter getResponseWriter() {
        return (this.responseWriter);
    }
    public void setResponseWriter(ResponseWriter responseWriter) {
        this.responseWriter = responseWriter;
    }


    // root
    private UIPage root = null;
    public UIPage getRoot() {
        return (this.root);
    }
    public void setRoot(UIPage root) {
        this.root = root;
    }


    // ---------------------------------------------------------- Public Methods


    public void addFacesEvent(FacesEvent event) {
        facesEvents.add(event);
    }


    public void addMessage(UIComponent component, Message message){ 
        List list = (List) messages.get(component);
        if (list == null) {
            list = new ArrayList();
            messages.put(component, list);
        }
        list.add(message);
    }


    public void release() {
        application = null;
        externalContext = null;
        facesEvents.clear();
        locale = null;
        messages.clear();
        renderResponse = false;
        responseComplete = false;
        responseStream = null;
        responseWriter = null;
        root = null;
    }


    public void renderResponse() {
        this.renderResponse = true;
    }


    public void responseComplete() {
        this.responseComplete = true;
    }


}
